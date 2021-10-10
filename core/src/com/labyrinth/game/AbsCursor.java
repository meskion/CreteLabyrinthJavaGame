package com.labyrinth.game;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class AbsCursor {

	protected Map<Integer, Vector2> dictMenu;
	private float cursorWave;
	
	public Rectangle rect;
	protected int menuCursor = 0;
	public AbsCursor(Vector2 v0, Vector2 v1, Vector2 v2) {

		dictMenu = Map.of(0, v0, 1, v1, 2, v2);
		rect = new Rectangle();
		rect.setPosition(dictMenu.get(0));
		cursorWave = 0;
		
	}
	
	public void update() {
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
			menuCursor = (menuCursor + 1) % 3;
			rect.setPosition(dictMenu.get(menuCursor));
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
			menuCursor = (menuCursor + 2) % 3;
			rect.setPosition(dictMenu.get(menuCursor));
		}
		
		rect.y += 0.3*Math.sin(cursorWave*0.05);

		cursorWave++;
	}
	
	public abstract void act();		
	
	
	
}
