package com.labyrinth.game;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Define el cursor de un menú del juego, de manera abstacta. No sabe que imagen
 * lo representa, ni lo que tiene que hacer. Solo guarda las posiciones entre
 * las que hace la rotación, un punto de referencia para las posiciones y
 * estilización.
 * 
 * @author manuf
 *
 */
public abstract class AbsCursor {

	protected Map<Integer, Vector2> dictMenu;
	private float cursorWave;
	public Rectangle origin;
	public Rectangle rect;
	Sound optionShift = Gdx.audio.newSound(Gdx.files.internal("sounds/click.wav"));
	protected int menuCursor = 0;

	/**
	 * Construye el cursor con 3 vectores, las 3 posiciones entre las que rota. Se
	 * podria generalizar a N posiciones, pero para este prototipo ha sido solo
	 * necesario estas.
	 * 
	 * @param v0
	 * @param v1
	 * @param v2
	 * @param origin
	 */
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

	/**
	 * Cambia la posición actual del cursor a la especificada.
	 * 
	 * @param pos
	 */
	public void setCursor(int pos) {
		menuCursor = pos;
	}

	/**
	 * Actualiza el cursor escuchando input de teclado y cambiando su posicion
	 * ligeramente para hacer que oscile.
	 */
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

	/**
	 * Debe implementarse para definir que acciones realiza el cursor en funcion de
	 * su selección.
	 */
	public abstract void act();

}
