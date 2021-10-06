package com.labyrinth.game;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.labyrinth.maze.MazeGenerator;

public class LabyrinthGame extends ApplicationAdapter {

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture teseoSprite, wallSprite;
	private Sound footsteps;
	private Rectangle teseo;
	private List<Rectangle> walls;
	private int x = 30;
	private int y = 30;
	private int step = 128;

	@Override
	public void create() {

		camera = new OrthographicCamera();
		batch = new SpriteBatch();
		camera.setToOrtho(false, 800 * 2, 400 * 2);

		teseoSprite = new Texture(Gdx.files.internal("sprites/teseo.png"));
		wallSprite = new Texture(Gdx.files.internal("sprites/wall.png"));
		footsteps = Gdx.audio.newSound(Gdx.files.internal("sounds/footsteps-1.mp3"));

		walls = new ArrayList<>();
		generateLevel();
		teseo = new Rectangle();
		teseo.x = x / 2 * step;
		teseo.y = -y / 2 * step;
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

		float nextX = 0;
		float nextY = 0;

		if (Gdx.input.isKeyPressed(Input.Keys.UP))
//			teseo.y += 200 * Gdx.graphics.getDeltaTime();
			nextY += 200 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
			nextY -= 200 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
			nextX -= 200 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
			nextX += 200 * Gdx.graphics.getDeltaTime();

		Rectangle collider = checkCollision(nextX, nextY);
		Rectangle hCollider = checkHCollision(nextX);
		Rectangle vCollider = checkVCollision(nextY);
		
		if (collider != null) {
			if (hCollider == null) {
				teseo.x += nextX;	
			} else if (vCollider == null){
				teseo.y += nextY;
			}
			
		} else {
			teseo.x += nextX;
			teseo.y += nextY;
		}

		camera.position.set(teseo.x, teseo.y, 0);


	}

	public Rectangle checkCollision(float x, float y) {
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

	public void generateLevel() {

		MazeGenerator level = new MazeGenerator(x, y);
		int[][] levelMatrix = level.maze;
//		int[][] levelMatrix = { { 6, 10 }, { 5, 9 } };

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
		for (int j = -1; j < 2 * x; j++) {
//			System.out.print("+---");
			Rectangle wallEast = new Rectangle((x - 1) * step + 64, -j * step / 2 + 1, 64, 64);
			Rectangle wallSouth = new Rectangle(j * step / 2 - 1, -(y - 1) * step - 64, 64, 64);
			walls.add(wallEast);
			walls.add(wallSouth);
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
