package com.labyrinth.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

public class MenuScreen implements Screen {

	final int width = 800;
	final int height = 400;
	final int step = 128;
	final int mazeSize = 40;
	protected Map<Integer, Vector2> dictMenu;
	Texture wallSprite;
	Sound  optionSelect;
	final mainGame game;
	List<Rectangle> walls = new ArrayList<>();

	OrthographicCamera camera;

	public MenuScreen(final mainGame game) {
		this.game = game;
		wallSprite = new Texture(Gdx.files.internal("sprites/wall.png"));

		GameScreen.generateLevel(walls, mazeSize, GameScreen.step);
		camera = new OrthographicCamera();
		camera.setToOrtho(false, width * 3, height * 3);
		camera.position.set(20 * step, -20 * step, 0);
		
		optionSelect = Gdx.audio.newSound(Gdx.files.internal("sounds/Accept.mp3"));

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		// background color
		ScreenUtils.clear(250f / 255f, 237f / 255f, 205f / 255f, 1f);
		// set camera render
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();

		for (Rectangle wall : walls) {
			game.batch.draw(wallSprite, wall.x, wall.y);
		}
		game.batch.end();

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

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
		wallSprite.dispose();

	}

}
