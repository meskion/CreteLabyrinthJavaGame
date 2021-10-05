package com.labyrinth.game;

import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

public class LabyrinthGame extends ApplicationAdapter {

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture teseoSprite, wallSprite;
	private Sound footsteps;
	private Rectangle teseo;
	private List<Rectangle> walls;

	@Override
	public void create() {

		camera = new OrthographicCamera();
		batch = new SpriteBatch();
		camera.setToOrtho(false, 800, 480);

		teseoSprite = new Texture(Gdx.files.internal("sprites/teseo.png"));
		footsteps = Gdx.audio.newSound(Gdx.files.internal("sounds/footsteps-1.mp3"));

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
		batch.end();

		if (Gdx.input.isKeyPressed(Input.Keys.UP))
			teseo.y += 200 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
			teseo.y -= 200 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
			teseo.x -= 200 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
			teseo.x += 200 * Gdx.graphics.getDeltaTime();

		if (teseo.x < 0)
			teseo.x = 0;
		if (teseo.x > 800 - 32)
			teseo.x = 800 - 32;
		if (teseo.y < 0)
			teseo.y = 0;
		if (teseo.y > 400 - 32)
			teseo.y = 400 - 32;

	}

	@Override
	public void dispose() {
		batch.dispose();
//		img.dispose();
	}
}
