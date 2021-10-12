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

public class mainGame extends Game {

		public SpriteBatch batch;
		public TextFieldStyle tStyle;
		public TextButtonStyle bStyle;
		public LabelStyle lStyle;
		public Skin skin;
		public BitmapFont font;
		public Music menuMusic;
		
		public void create() {
			batch = new SpriteBatch();
			menuMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/menu.wav"));
			defineStyles();

			this.setScreen(new MainMenuScreen(this));
			
			
			
		}

		public void render() {
			super.render(); // important!
		}

		public void dispose() {
			batch.dispose();
			
		}
		
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