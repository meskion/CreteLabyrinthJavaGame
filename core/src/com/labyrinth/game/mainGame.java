package com.labyrinth.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class mainGame extends Game {

		public SpriteBatch batch;
		public BitmapFont font;

		public void create() {
			batch = new SpriteBatch();
			font = new BitmapFont(); // use libGDX's default Arial font
			font.setColor(Color.BLACK);
			font.getData().setScale(2);
			this.setScreen(new MainMenuScreen(this));
		}

		public void render() {
			super.render(); // important!
		}

		public void dispose() {
			batch.dispose();
			font.dispose();
		}

	}