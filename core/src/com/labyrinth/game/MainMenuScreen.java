package com.labyrinth.game;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class MainMenuScreen extends MenuScreen {
	private float cursorWave;
	Texture menuSprite, cursorSprite;
	private int menuCursor = 0;
	private Rectangle cursor;
	final Map<Integer, Vector2> dictMenu = Map.of(0, new Vector2(20 * 128 - 280, -20 * 128 + 20), 1,
			new Vector2(20 * 128 - 225, -20 * 128 - 120), 2, new Vector2(20 * 128 - 160, -20 * 128 - 270));

	public MainMenuScreen(mainGame game) {
		super(game);
		menuSprite = new Texture(Gdx.files.internal("sprites/menu.jpg"));
		cursorSprite = new Texture(Gdx.files.internal("sprites/menuArrow.png"));
		cursor = new Rectangle();
		cursor.setPosition(dictMenu.get(0));
		cursorWave = 0;
	}
	@Override
	public void render(float delta) {
		super.render(delta);
		game.batch.begin();
		game.batch.draw(menuSprite, 20 * 128 - 1200 / 2, -20 * 128 - 900 / 2);

		game.batch.draw(cursorSprite, cursor.x, cursor.y);

		game.batch.end();
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {// cambiar a interactuar con los botones
			menuCursor = (menuCursor + 1) % 3;
			cursor.setPosition(dictMenu.get(menuCursor));
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {// cambiar a interactuar con los botones
			menuCursor = (menuCursor + 2) % 3;
			cursor.setPosition(dictMenu.get(menuCursor));
		}
		
		cursor.y += 0.3*Math.sin(cursorWave*0.05);

		cursorWave++;
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
			switch (menuCursor) {
			case 0:
				game.setScreen(new NewGameScreen(game));
				break;
			case 1:
				game.setScreen(new GameScreen(game));
				dispose();
				break;
			case 2:
				// exit;
			default:
				break;
			}

		}
	}
	
	@Override
	public void dispose() {
		super.dispose();
		cursorSprite.dispose();
		menuSprite.dispose();

	}

}
