package com.labyrinth.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.labyrinth.maze.Maze;
import com.labyrinth.maze.MazeGenerator;

public class LabyrinthGame extends ApplicationAdapter {

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture teseoSprite, wallSprite;
	private Sound footsteps;
	private Rectangle teseo;
	private Array<Rectangle> walls;

	@Override
	public void create() {

		camera = new OrthographicCamera();
		batch = new SpriteBatch();
		camera.setToOrtho(false, 800*2, 480*2);

		teseoSprite = new Texture(Gdx.files.internal("sprites/teseo.png"));
		wallSprite = new Texture(Gdx.files.internal("sprites/wall.png"));
		footsteps = Gdx.audio.newSound(Gdx.files.internal("sounds/footsteps-1.mp3"));

		walls = new Array<>();
		generateLevel();
		teseo = new Rectangle();
		teseo.x = 800 / 2 - 32 / 2;
		teseo.y = 20;
		teseo.width = 32;
		teseo.height = 32;
	}

	@Override
	public void render() {
		ScreenUtils.clear(1, 1, 1, 1);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(teseoSprite, teseo.x, teseo.y);
		for (Rectangle wall : walls) {
			batch.draw(wallSprite, wall.x, wall.y);
		}
		batch.end();

		if (Gdx.input.isKeyPressed(Input.Keys.UP))
			teseo.y += 200 * Gdx.graphics.getDeltaTime();

		if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
			teseo.y -= 200 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
			teseo.x -= 200 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
			teseo.x += 200 * Gdx.graphics.getDeltaTime();

		camera.position.set(teseo.x, teseo.y, 0);
//		if (teseo.x < 0)
//			teseo.x = 0;
//		if (teseo.x > 800 - 32)
//			teseo.x = 800 - 32;
//		if (teseo.y < 0)
//			teseo.y = 0;
//		if (teseo.y > 480 - 32)
//			teseo.y = 480 - 32;

	}

	public void generateLevel() {
		int x = 10;
		int y = 10;
		MazeGenerator level = new MazeGenerator(x, y);
		int[][] levelMatrix = level.maze;

		for (int i = 0; i < y; i++) {
			// draw the north edge
			for (int j = 0; j < x; j++) {

				if ((levelMatrix[j][i] & 1) == 0) {
					// "+---"
					Rectangle wallCorner = new Rectangle(j * 128 - 64, i * 128 + 64, 64, 64);
					Rectangle wallNorth = new Rectangle(j * 128, i * 128 + 64, 64, 64);
					walls.add(wallCorner);
					walls.add(wallNorth);

				} else {
					Rectangle wallCorner = new Rectangle(j * 128 - 64, i * 128 + 64, 64, 64);
					// "+ "
					walls.add(wallCorner);
				}
			}
			System.out.println("+");
			// draw the west edge
			for (int j = 0; j < x; j++) {
				if ((levelMatrix[j][i] & 8) == 0) {
					// "| "
					Rectangle wallCorner = new Rectangle(i * 128 - 64, j * 128, 64, 64);
					walls.add(wallCorner);
				}
			}
//			System.out.println("|");
		}
		// draw the bottom line
		for (int j = 0; j < x; j++) {
//			System.out.print("+---");
		}
//		System.out.println("+");
		level.display();
	}

	@Override
	public void dispose() {
		batch.dispose();
		teseoSprite.dispose();
		wallSprite.dispose();
		footsteps.dispose();
	}
}
