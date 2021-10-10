package com.labyrinth.game;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.labyrinth.maze.MazeGenerator;

public class GameScreen implements Screen {
	final Game game;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture teseoSprite, wallSprite;
	private Sound footsteps;
	private Rectangle teseo;
	private List<Rectangle> walls;
	private int x;
	private int y;
	private Stage stage;
	private SwapImage arrowUp, arrowDown, arrowLeft, arrowRight;
	public String playerName;
	public static int step = 128;

	public GameScreen(final Game game, String name, int difficulty) {

		this.game = game;
		x = difficulty;
		y = difficulty;
		playerName = name;

		camera = new OrthographicCamera();
		batch = new SpriteBatch();
		camera.setToOrtho(false, 800 * 2, 400 * 2);

		stageSetup();

		teseoSprite = new Texture(Gdx.files.internal("sprites/teseo.png"));
		wallSprite = new Texture(Gdx.files.internal("sprites/wall.png"));
		footsteps = Gdx.audio.newSound(Gdx.files.internal("sounds/footsteps-1.mp3"));

		walls = new ArrayList<>();
		generateLevel(walls, x, y, step);
		teseo = new Rectangle();
		teseo.x = x / 2 * step;
		teseo.y = -y / 2 * step;
		teseo.width = 48;
		teseo.height = 48;
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

	public static void generateLevel(List<Rectangle> walls, int x, int y, int step) {

		MazeGenerator level = new MazeGenerator(x, y);
		int[][] levelMatrix = level.maze;
//		int[][] levelMatrix = { { 6, 10 }, { 5, 9 } };
		boolean addedExit = false;
		// level.maze;

		for (int i = 0; i < y; i++) {
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

			Rectangle wallSouth = new Rectangle(j * step / 2 - 1, -(y - 1) * step - 64, 64, 64);
			walls.add(wallEast);

			if (!addedExit && Math.random() < prob) {
				Rectangle blocked = checkCollision(wallSouth, walls, 0, 64);
				if (blocked != null)
					walls.remove(blocked);
				addedExit = true;

			} else
				walls.add(wallSouth);
			prob += 0.2 / x * 0.8;
		}
		// open an exit

		level.display();
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

		float nextX = 0;
		float nextY = 0;

		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			nextY += 300 * Gdx.graphics.getDeltaTime();
			arrowUp.setCurrent(1);
		}else {
			arrowUp.setCurrent(0);
		}
		
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			nextY -= 300 * Gdx.graphics.getDeltaTime();
			arrowDown.setCurrent(1);
		}else {
			arrowDown.setCurrent(0);
		}
			
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			nextX -= 300 * Gdx.graphics.getDeltaTime();
			arrowLeft.setCurrent(1);
		}else {
			arrowLeft.setCurrent(0);
		}
			
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			nextX += 300 * Gdx.graphics.getDeltaTime();
			arrowRight.setCurrent(1);
		}else {
			arrowRight.setCurrent(0);
		}
			

		moveTeseo(nextX, nextY);

		camera.position.set(teseo.x, teseo.y, 0);

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();

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
		footsteps.dispose();
	}

}
