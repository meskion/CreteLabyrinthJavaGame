package com.labyrinth.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;

/**
 * Clase principal que se carga al inciar el juego y que redirige al resto de
 * vistas. Contiene las clases que se encargan del renderizado como 'batch' y
 * los estilos y musica usados en el juego.
 * 
 * @author manuf
 *
 */
public class mainGame extends Game {

	public SpriteBatch batch;
	public TextFieldStyle tStyle;
	public TextButtonStyle bStyle;
	public LabelStyle lStyle;
	public Skin skin;
	public BitmapFont font;
	public Music menuMusic;
	public Music victoryMusicA, victoryMusicB;

	/**
	 * Metodo principal de la clase Game que se ejecuta al instanciar la aplicación.
	 */
	@Override
	public void create() {
		batch = new SpriteBatch();
		menuMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/menu.wav"));
		victoryMusicA = Gdx.audio.newMusic(Gdx.files.internal("sounds/victoryA.wav"));
		victoryMusicA.setLooping(false);
		victoryMusicB = Gdx.audio.newMusic(Gdx.files.internal("sounds/victoryB.wav"));
		victoryMusicB.setLooping(true);

		defineStyles();

		this.setScreen(new MainMenuScreen(this));

	}

	/**
	 * Metodo encargado de dibujar cada instante los graficos del juego. se llama
	 * continuamente.
	 */

	public void render() {
		super.render(); // important!
	}

	/**
	 * Elimina recursos no usados para ahorrar memoria y evitar errores. debe
	 * llamarse manualmente cuando se requiera
	 */
	public void dispose() {
		batch.dispose();

	}
	/**
	 * Definición de los estilos de GUI y texto del juego.
	 */
	private void defineStyles() {

		skin = new Skin(Gdx.files.internal("data/pixthulhu-ui.json"));
		font = new BitmapFont(Gdx.files.internal("data/bgregular.fnt"));
		Color niceGreen = new Color(0.33f, 0.4f, 0.14f, 1f);
		tStyle = skin.get("default", TextFieldStyle.class);
		bStyle = new TextButtonStyle();
		lStyle = new LabelStyle();
		lStyle.fontColor = niceGreen;
		bStyle.fontColor = niceGreen;
		tStyle.font = font;
		bStyle.font = font;
		lStyle.font = font;
	}

}