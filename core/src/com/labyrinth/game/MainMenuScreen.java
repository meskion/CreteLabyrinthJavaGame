package com.labyrinth.game;

import java.nio.file.Files;
import java.nio.file.Path;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class MainMenuScreen extends MenuScreen {

	Texture menuSprite, cursorSprite;
	boolean fileExists;

	AbsCursor cursor;

	public MainMenuScreen(mainGame game) {
		super(game);
		fileExists = !Files.notExists(Path.of("data/maze.sav"));
		
		
		menuSprite = (fileExists) ? new Texture(Gdx.files.internal("sprites/menu.jpg")) : new Texture(Gdx.files.internal("sprites/menu2.jpg"));
		
		cursorSprite = new Texture(Gdx.files.internal("sprites/menuArrow.png"));

		cursor = new AbsCursor(new Vector2(20 * 128 - 280, -20 * 128 + 20),
				new Vector2(20 * 128 - 225, -20 * 128 - 120), new Vector2(20 * 128 - 160, -20 * 128 - 270)) {

			@Override
			public void act() {
				switch (this.menuCursor) {
				case 0:
					game.setScreen(new NewGameScreen(game));
					optionSelect.play();
					dispose();
					break;
				case 1:
					if (fileExists) {
						game.setScreen(new GameScreen(game));
						optionSelect.play();
						dispose();
					}
					break;
				case 2:
					dispose();
					Gdx.app.exit();
				default:
					break;
				}
				

			}
		};

		game.menuMusic.play();
		game.menuMusic.setVolume(0.3f);

	}

	

	@Override
	public void render(float delta) {
		super.render(delta);
		game.batch.begin();
		game.batch.draw(menuSprite, 20 * 128 - 1200 / 2, -20 * 128 - 900 / 2);

		game.batch.draw(cursorSprite, cursor.rect.x, cursor.rect.y);

		game.batch.end();

		cursor.update();

		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
			cursor.act();

//			switch (menuCursor) {
//			case 0:
//				game.setScreen(new NewGameScreen(game));
//				break;
//			case 1:
//				game.setScreen(new GameScreen(game));
//				dispose();
//				break;
//			case 2:
//				// exit;
//			default:
//				break;
//			}

		}
	}

	@Override
	public void dispose() {
		super.dispose();
		cursorSprite.dispose();
		menuSprite.dispose();

	}

}
