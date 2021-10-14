package com.labyrinth.game;

import java.nio.file.Files;
import java.nio.file.Path;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/**
 * controla la pantalla del menu principal.
 * 
 * @author manuf
 *
 */
public class MainMenuScreen extends MenuScreen {

	Texture menuSprite, cursorSprite;
	boolean fileExists;

	AbsCursor cursor;

	/**
	 * Constructor, recibe la clase game principal y inicializa texturas y sonidos y
	 * lógica del menu principal
	 * 
	 * @param game
	 */
	public MainMenuScreen(mainGame game) {
		super(game);
		fileExists = !Files.notExists(Path.of("data/maze.sav"));

		menuSprite = (fileExists) ? new Texture(Gdx.files.internal("sprites/menu.jpg"))
				: new Texture(Gdx.files.internal("sprites/menu2.jpg"));

		cursorSprite = new Texture(Gdx.files.internal("sprites/menuArrow.png"));

		cursor = new AbsCursor(new Vector2(ref - 280, -ref + 20), new Vector2(ref - 225, -ref - 120),
				new Vector2(ref - 160, -ref - 270)) {
			/**
			 * Implementación del cursor del menu para que redirija al resto de pantallas.
			 */
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
						game.menuMusic.stop();
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

	/**
	 * Dibuja el cursor y el menu de la pantalla. Como se llama cada frame, tambien
	 * controla los eventos de teclado.
	 */
	@Override
	public void render(float delta) {
		super.render(delta);
		game.batch.begin();
		game.batch.draw(menuSprite, ref - 1200 / 2, -ref - 900 / 2);

		game.batch.draw(cursorSprite, cursor.rect.x, cursor.rect.y);

		game.batch.end();

		cursor.update();

		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
			cursor.act();

		}
	}

	/**
	 * Llamado para limpiar memoria de recursos ya no necesarios.
	 */
	@Override
	public void dispose() {
		super.dispose();
		cursorSprite.dispose();
		menuSprite.dispose();

	}

}
