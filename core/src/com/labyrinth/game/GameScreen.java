package com.labyrinth.game;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

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
	private Texture teseoSprite, wallSprite, menuSprite;
	private Sound[] footsteps = new Sound[9];
	private Sound roar;
	private Rectangle teseo;
	private List<Rectangle> walls;
	private Rectangle exit;
	private int difficulty;

	private Image menuImg;
	private Table menu;
	private Stage stage;
	private SwapImage arrowUp, arrowDown, arrowLeft, arrowRight;
	public String playerName;
	private AbsCursor cursor;
	private boolean paused = false;
	private Texture cursorSprite;
	private boolean notWalking = true;
	private float numroars;
	public static int step = 128;

	public GameScreen(final mainGame game, String name, int difficulty) {
		gameSetup(game);

	

		this.difficulty = difficulty;

		playerName = name;
		exit = generateLevel(walls, difficulty, step);

		teseo.x = difficulty / 2 * step;
		teseo.y = -difficulty / 2 * step;
	}

	public GameScreen(final mainGame game) {
		gameSetup(game);
		load();

	}

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
					game.setScreen(new GameScreen(game, playerName, difficulty));
					dispose();
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

	private void load() {
		Path savePath = Path.of("data/maze.sav");
		
		List<String> data = null;
		try {
			data = Files.readAllLines(savePath);
			
			String[] teseoData = data.remove(0).split(" ");
			teseo.x = Float.parseFloat(teseoData[0]);
			teseo.y =  Float.parseFloat(teseoData[1]);
			
			exit = new Rectangle(0, 0, 64, 64);
			String[] exitData = data.remove(0).split(" ");
			exit.x = Float.parseFloat(exitData[0]);
			exit.y =  Float.parseFloat(exitData[1]);
			
			data.stream().forEach(line -> {
				String[] wallData = line.split(" ");
				float x = Float.parseFloat(wallData[0]);
				float y =  Float.parseFloat(wallData[1]);
				walls.add(new Rectangle(x, y, 64, 64));
			});
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected void save() {
		Path savePath = Path.of("data/maze.sav");
		String file = "";
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

	private void loadSteps() {
		for (int i = 1; i <= 9; i++) {
			footsteps[i - 1] = Gdx.audio.newSound(Gdx.files.internal("sounds/step_" + i + ".wav"));
		}
	}

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

	private SwapImage setArrow(String name) {
		Texture sprite = new Texture(Gdx.files.internal("sprites/" + name + "Arrow.png"));
		Texture actSprite = new Texture(Gdx.files.internal("sprites/" + name + "ArrowActive.png"));

		SwapImage arrow = new SwapImage(sprite, actSprite);

		return arrow;
	}

	public static Rectangle checkCollision(Rectangle teseo, List<Rectangle> walls, float x, float y) {
		Rectangle checker = new Rectangle(teseo);
		Rectangle collision = null;
		checker.x += x;
		checker.y += y;
		try {
			collision = walls.parallelStream().filter(r -> r.overlaps(checker)).findFirst().get();
		} catch (NoSuchElementException e) {

		}
//		 (walls.parallelStream().anyMatch((r) -> r.contains(x, y))) 

		return collision;
	}

	public Rectangle checkVCollision(float y) {
		Rectangle checker = new Rectangle(teseo);
		Rectangle collision = null;
		checker.y += y;
		try {
			collision = walls.parallelStream().filter(r -> r.overlaps(checker)).findFirst().get();
		} catch (NoSuchElementException e) {

		}
//		 (walls.parallelStream().anyMatch((r) -> r.contains(x, y))) 

		return collision;
	}

	public Rectangle checkHCollision(float x) {
		Rectangle checker = new Rectangle(teseo);
		Rectangle collision = null;
		checker.x += x;
		try {
			collision = walls.parallelStream().filter(r -> r.overlaps(checker)).findFirst().get();
		} catch (NoSuchElementException e) {

		}
//		 (walls.parallelStream().anyMatch((r) -> r.contains(x, y))) 

		return collision;
	}

	public static Rectangle generateLevel(List<Rectangle> walls, int x, int step) {
		Rectangle exit = null;
		MazeGenerator level = new MazeGenerator(x, x);
		int[][] levelMatrix = level.maze;
//		int[][] levelMatrix = { { 6, 10 }, { 5, 9 } };
		boolean addedExit = false;
		// level.maze;

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
//			System.out.println("+");
			// draw the west edge
			for (int j = 0; j < x; j++) {
				if ((levelMatrix[j][i] & 8) == 0) {
					// "| "
					Rectangle wallWest = new Rectangle(j * step - 64, -i * step, 64, 64);
					walls.add(wallWest);
				}
			}
//			System.out.println("|");

			// draw the east border

		}
		// draw the bottom line
		float prob = 0f;
		for (int j = -1; j < 2 * x; j++) {
//			System.out.print("+---");

			Rectangle wallEast = new Rectangle((x - 1) * step + 64, -j * step / 2 + 1, 64, 64);

			Rectangle wallSouth = new Rectangle(j * step / 2 - 1, -(x - 1) * step - 64, 64, 64);
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

//		level.display();
		return exit;
	}

	@Override
	public void show() {
		// things to do as the screen gains focus

	}

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
			System.out.println(cursor.origin.x);
			System.out.println(cursor.rect.x);
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
		}

	}

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

	private void toggleMenu() {
		menu.setVisible(paused);
		menuImg.setVisible(paused);

	}

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

	private void moveTeseo(float nextX, float nextY) {
		moveHor(nextX);
		moveVer(nextY);
	}

	public void moveHor(float nextX) {
		Rectangle hCollider = checkHCollision(nextX);

		if (hCollider == null) {
			teseo.x += nextX;
		} else if (checkHCollision(nextX / nextX * Gdx.graphics.getDeltaTime()) == null) {
			moveHor(nextX / 5);
		}

	}

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
