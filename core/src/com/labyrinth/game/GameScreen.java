package com.labyrinth.game;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.labyrinth.maze.MazeGenerator;

public class GameScreen implements Screen {

	private mainGame game;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	// Atributos del menu
	private Image menuImg;
	private Table menu;
	private Stage stage;
	private SwapImage arrowUp, arrowDown, arrowLeft, arrowRight;
	// Cursor
	private AbsCursor cursor;

	// Atributos de recuros
	private Texture teseoSprite, wallSprite, menuSprite, cursorSprite;
	private Sound[] footsteps = new Sound[9];
	private Sound roar;

	// elementos del juego
	private Rectangle teseo;
	private List<Rectangle> walls;
	private Rectangle exit;
	private int difficulty;

	public String playerName;
	private boolean paused = false;
	private boolean notWalking = true;
	private float numroars;
	public static final int step = 128;

	/**
	 * Constructor llamado para una partida nueva.
	 * 
	 * @param game
	 * @param name
	 * @param difficulty
	 */
	public GameScreen(final mainGame game, String name, int difficulty) {
		gameSetup(game);

		this.difficulty = difficulty;

		playerName = name;
		exit = generateLevel(walls, difficulty, step);

		teseo.x = difficulty / 2 * step;
		teseo.y = -difficulty / 2 * step;
	}

	/**
	 * Constructor llamado para continuar una partida guardada.
	 * 
	 * @param game
	 */
	public GameScreen(final mainGame game) {
		gameSetup(game);
		load();

	}

	/**
	 * Inicializa todos los recursos y atributos del juego
	 * 
	 * @param game
	 */
	private void gameSetup(final mainGame game) {
		this.game = game;
		camera = new OrthographicCamera();
		batch = new SpriteBatch();
		camera.setToOrtho(false, 800 * 2, 400 * 2);

		teseoSprite = new Texture(Gdx.files.internal("sprites/teseo.png"));
		wallSprite = new Texture(Gdx.files.internal("sprites/wall.png"));
		menuSprite = new Texture(Gdx.files.internal("sprites/blankMenu.jpg"));
		cursorSprite = new Texture(Gdx.files.internal("sprites/menuArrow.png"));

		loadSteps();
		roar = Gdx.audio.newSound(Gdx.files.internal("sounds/troll-roars.ogg"));
		walls = new ArrayList<>();
		teseo = new Rectangle();
		teseo.width = 48;
		teseo.height = 48;
		cursor = new AbsCursor(new Vector2(-205, 30), new Vector2(-205, -60), new Vector2(0 - 205, -150), teseo) {

			@Override
			public void act() {
				switch (this.menuCursor) {
				case 0:
					paused = false;
					toggleMenu();
					break;
				case 1:
					dispose();
					game.setScreen(new GameScreen(game, playerName, difficulty));

					break;
				case 2:
					save();
					dispose();
					game.setScreen(new MainMenuScreen(game));
				default:
					break;
				}

			}
		};

		stageSetup();
		Timer.schedule(new Task() {

			@Override
			public void run() {
				minoRoar();

			}
		}, 5f, 20f);
	}

	/**
	 * Carga del archivo de guardado en disco la informacion de la partida. las
	 * posiciones de las paredes y las posiciones de la salida y el jugador
	 */
	private void load() {
		Path savePath = Path.of("data/maze.sav");

		List<String> data = null;
		try {
			data = Files.readAllLines(savePath);
			playerName = data.remove(0);

			String[] teseoData = data.remove(0).split(" ");
			teseo.x = Float.parseFloat(teseoData[0]);
			teseo.y = Float.parseFloat(teseoData[1]);

			exit = new Rectangle(0, 0, 64, 64);
			String[] exitData = data.remove(0).split(" ");
			exit.x = Float.parseFloat(exitData[0]);
			exit.y = Float.parseFloat(exitData[1]);

			data.stream().forEach(line -> {
				String[] wallData = line.split(" ");
				float x = Float.parseFloat(wallData[0]);
				float y = Float.parseFloat(wallData[1]);
				walls.add(new Rectangle(x, y, 64, 64));
			});

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	/**
	 * Guarda en un archivo la información de la partida
	 */
	protected void save() {
		Path savePath = Path.of("data/maze.sav");
		String file = "";
		file += playerName + "\n";
		file += String.valueOf(teseo.x) + " " + String.valueOf(teseo.y) + "\n";
		file += String.valueOf(exit.x) + " " + String.valueOf(exit.y) + "\n";

		for (Rectangle r : walls) {
			file += String.valueOf(r.x) + " " + String.valueOf(r.y) + "\n";
		}
		try {
			Files.writeString(savePath, file);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Carga iterativamente recursos de sonido similares.
	 */
	private void loadSteps() {
		for (int i = 1; i <= 9; i++) {
			footsteps[i - 1] = Gdx.audio.newSound(Gdx.files.internal("sounds/step_" + i + ".wav"));
		}
	}

	/**
	 * Inicializa y da estilo a los elementos del menu
	 */
	private void stageSetup() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		Table arrows = new Table();

		arrows.setPosition(755, 35);
		arrows.align(Align.bottomRight);
		stage.addActor(arrows);

		arrowUp = setArrow("up");
		arrowDown = setArrow("down");
		arrowLeft = setArrow("left");
		arrowRight = setArrow("right");

		int aSize = 75;
		int pad = -10;

		arrows.add();
		arrows.add(arrowUp).pad(pad).width(aSize).height(aSize);
		arrows.add();

		arrows.row();

		arrows.add(arrowLeft).pad(pad).width(aSize).height(aSize);
		arrows.add();
		arrows.add(arrowRight).pad(pad).width(aSize).height(aSize);

		arrows.row();

		arrows.add();
		arrows.add(arrowDown).pad(pad).width(aSize).height(aSize);
		arrows.add();

		menuImg = new Image(menuSprite);
		menuImg.setWidth(1200 / 3);
		menuImg.setHeight(900 / 3);
		menuImg.setPosition(200, 50);
		menuImg.toFront();
		menuImg.setVisible(false);
		menu = new Table();
		menu.setPosition(400, 180);
		menu.setVisible(false);
		menu.add(new Label("continuar", game.lStyle)).pad(10).left();
		menu.row();
		menu.add(new Label("Reiniciar", game.lStyle)).pad(10).left();
		menu.row();
		menu.add(new Label("Guardar y Salir", game.lStyle)).pad(10).left();

		stage.addActor(menuImg);
		stage.addActor(menu);
	}

	/**
	 * inicializa las SwapImage de las flechas segun la direccion
	 * 
	 * @param name
	 * @return
	 */
	private SwapImage setArrow(String name) {
		Texture sprite = new Texture(Gdx.files.internal("sprites/" + name + "Arrow.png"));
		Texture actSprite = new Texture(Gdx.files.internal("sprites/" + name + "ArrowActive.png"));

		SwapImage arrow = new SwapImage(sprite, actSprite);

		return arrow;
	}

	/**
	 * Chequea si un rectangulo dado esta superpuesto con alguno de los rectangulos
	 * de la lista dada.
	 * 
	 * @param r
	 * @param walls
	 * @param x
	 * @param y
	 * @return
	 */
	public static Rectangle checkCollision(Rectangle r, List<Rectangle> walls, float x, float y) {
		Rectangle checker = new Rectangle(r);
		Rectangle collision = null;
		checker.x += x;
		checker.y += y;
		try {
			collision = walls.stream().filter(w -> w.overlaps(checker)).findFirst().get();
		} catch (NoSuchElementException e) {

		}

		return collision;
	}

	/**
	 * Comprueba si el jugador colisionaría con una pared si se desplaza
	 * verticalmente 'y' unidades
	 * 
	 * @param y
	 * @return
	 */
	public Rectangle checkVCollision(float y) {
		Rectangle checker = new Rectangle(teseo);
		Rectangle collision = null;
		checker.y += y;
		try {
			collision = walls.stream().filter(r -> r.overlaps(checker)).findFirst().get();
		} catch (NoSuchElementException e) {

		}
//		 (walls.parallelStream().anyMatch((r) -> r.contains(x, y))) 

		return collision;
	}

	/**
	 * Comprueba si el jugador colisionaría con una pared si se desplaza
	 * horizontalmente x unidades
	 * 
	 * @param x
	 * @return
	 */
	public Rectangle checkHCollision(float x) {
		Rectangle checker = new Rectangle(teseo);
		Rectangle collision = null;
		checker.x += x;
		try {
			collision = walls.parallelStream().filter(r -> r.overlaps(checker)).findFirst().get();
		} catch (NoSuchElementException e) {

		}

		return collision;
	}

	/**
	 * Rellena la lista dada con rectangulos que forman un laberinto completo, y
	 * devuelve el rectangulo de salida.
	 * 
	 * @param walls
	 * @param x
	 * @param step
	 * @return
	 */
	public static Rectangle generateLevel(List<Rectangle> walls, int x, int step) {
		Rectangle exit = null;
		MazeGenerator level = new MazeGenerator(x, x);
		int[][] levelMatrix = level.maze;

		boolean addedExit = false;

		for (int i = 0; i < x; i++) {
			// draw the north edge
			for (int j = 0; j < x; j++) {

				if ((levelMatrix[j][i] & 1) == 0) {
					// "+---"
					Rectangle wallCorner = new Rectangle(j * step - 64, -i * step + 64, 64, 64);
					Rectangle wallNorth = new Rectangle(j * step, -i * step + 64, 64, 64);
					walls.add(wallCorner);
					walls.add(wallNorth);

				} else {
					Rectangle wallCorner = new Rectangle(j * step - 64, -i * step + 64, 64, 64);
					// "+ "
					walls.add(wallCorner);
				}
			}

			// draw the west edge
			for (int j = 0; j < x; j++) {
				if ((levelMatrix[j][i] & 8) == 0) {
					// "| "
					Rectangle wallWest = new Rectangle(j * step - 64, -i * step, 64, 64);
					walls.add(wallWest);
				}
			}

			// draw the east border

		}
		// draw the bottom line
		float prob = 0f;
		for (int j = -1; j < 2 * x; j++) {

			Rectangle wallEast = new Rectangle((x - 1) * step + 64, -j * step / 2 + 1, 64, 64);

			Rectangle wallSouth = new Rectangle(j * step / 2, -(x - 1) * step - 64, 64, 64);
			walls.add(wallEast);

			if (!addedExit && Math.random() < prob) {
				Rectangle blocked = checkCollision(wallSouth, walls, 0, 64);
				if (blocked != null)
					walls.remove(blocked);
				addedExit = true;
				exit = wallSouth;

			} else
				walls.add(wallSouth);
			prob += 0.2 / x * 0.8;
		}
		// open an exit

		return exit;
	}

	@Override
	public void show() {
		// things to do as the screen gains focus

	}

	/**
	 * Dibuja los objetos del juego y controla la lógica en tiempo real.
	 */
	@Override
	public void render(float delta) {
		ScreenUtils.clear(250f / 255f, 237f / 255f, 205f / 255f, 1f);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(teseoSprite, teseo.x, teseo.y);
		for (Rectangle wall : walls) {
			batch.draw(wallSprite, wall.x, wall.y);
		}

		batch.end();

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {

			paused = !paused;
			cursor.setCursor(0);
			toggleMenu();
		}

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		if (!paused)
			movementUpdate();
		else {
			cursor.update();
			batch.begin();
			batch.draw(cursorSprite, cursor.rect.x, cursor.rect.y, cursorSprite.getWidth() * 0.75f,
					cursorSprite.getHeight() * 0.75f, 0, 0, cursorSprite.getWidth(), cursorSprite.getHeight(), false,
					false);
			batch.end();

			if (Gdx.input.isKeyJustPressed(Keys.ENTER))
				cursor.act();
		}

		if (teseo.overlaps(exit)) {
			game.setScreen(new WinScreen(game, playerName));
			this.dispose();
		}

	}

	/**
	 * Hace sonar el rugido del minotauro, suena mas fuerte cada vez que se llama al
	 * metodo
	 */
	private void minoRoar() {
		numroars += 0.05f;

		long id = roar.play(numroars);
		Timer.schedule(new Task() {
			@Override
			public void run() {
				roar.stop(id);
			}
		}, 1.5f);

	}

	/**
	 * Muestra o esconde el menu de pausa.
	 */
	private void toggleMenu() {
		menu.setVisible(paused);
		menuImg.setVisible(paused);

	}

	/**
	 * Se llama en render, y escucha inputs para determinar el movimiento del
	 * jugador. Ademas se encarga de que la camara siga al jugador y que suenen los
	 * sonidos asocioados
	 */
	private void movementUpdate() {
		float nextX = 0;
		float nextY = 0;

		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			nextY += 300 * Gdx.graphics.getDeltaTime();

			arrowUp.setCurrent(1);
		} else {
			arrowUp.setCurrent(0);
		}

		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			nextY -= 300 * Gdx.graphics.getDeltaTime();
			arrowDown.setCurrent(1);
		} else {
			arrowDown.setCurrent(0);
		}

		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			nextX -= 300 * Gdx.graphics.getDeltaTime();
			arrowLeft.setCurrent(1);
		} else {
			arrowLeft.setCurrent(0);
		}

		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			nextX += 300 * Gdx.graphics.getDeltaTime();
			arrowRight.setCurrent(1);
		} else {
			arrowRight.setCurrent(0);
		}

		moveTeseo(nextX, nextY);

		if (notWalking && Math.abs(nextY) + Math.abs(nextX) > 0) {
			notWalking = false;
			int f = (int) Math.floor(Math.random() * 9);
			footsteps[f].play(0.3f);
			Timer.schedule(new Task() {
				@Override
				public void run() {
					notWalking = true;
				}
			}, 0.3f);
		}
		camera.position.set(teseo.x, teseo.y, 0);

	}
/**
 * Mueve (si es posible) al jugador a la posicion X Y dada.
 * @param nextX
 * @param nextY
 */
	private void moveTeseo(float nextX, float nextY) {
		moveHor(nextX);
		moveVer(nextY);
	}
/**
 * Mueve horizontalmente al jugador, tiene en cuenta posibles colisiones
 * @param nextX
 */
	public void moveHor(float nextX) {
		Rectangle hCollider = checkHCollision(nextX);

		if (hCollider == null) {

			teseo.x += nextX;
		} else if (checkHCollision(nextX / nextX * Gdx.graphics.getDeltaTime()) == null) {

			moveHor(nextX / 5);
		}

	}
/**
 * Mueve verticalmente al jugador
 * @param nextY
 */
	public void moveVer(float nextY) {

		Rectangle vCollider = checkVCollision(nextY);

		if (vCollider == null) {
			teseo.y += nextY;
		} else if (checkVCollision(nextY / nextY * Gdx.graphics.getDeltaTime()) == null) {
			moveVer(nextY / 5);
		}
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		batch.dispose();
		teseoSprite.dispose();
		wallSprite.dispose();
		for (Sound f : footsteps)
			f.dispose();
		roar.dispose();
	}

}
