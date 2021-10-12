package com.labyrinth.game;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class AbsCursor {

	protected Map<Integer, Vector2> dictMenu;
	private float cursorWave;
	public Rectangle origin;
	public Rectangle rect;
	Sound optionShift =  Gdx.audio.newSound(Gdx.files.internal("sounds/click.wav"));
	protected int menuCursor = 0;

	public AbsCursor(Vector2 v0, Vector2 v1, Vector2 v2, Rectangle origin) {
		this.origin = origin;
		dictMenu = Map.of(0, v0, 1, v1, 2, v2);
		rect = new Rectangle();
		rect.setPosition(dictMenu.get(0));
		cursorWave = 0;

	}

	public AbsCursor(Vector2 v0, Vector2 v1, Vector2 v2) {
		this(v0, v1, v2, new Rectangle());
	}

	public void setCursor(int pos) {
		menuCursor = pos;
	}

	public void update() {

		if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
			optionShift.play();
			menuCursor = (menuCursor + 1) % 3;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
			optionShift.play();
			menuCursor = (menuCursor + 2) % 3;
		}

		float x = origin.x + dictMenu.get(menuCursor).x;
		float y = origin.y + dictMenu.get(menuCursor).y;
		rect.setPosition(x, y);

		rect.y += 5 * Math.sin(cursorWave * 0.05);

		cursorWave++;
	}

	public abstract void act();

}
