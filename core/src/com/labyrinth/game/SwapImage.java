package com.labyrinth.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
/**
 * Pequeña clase que extiende Image para permitirme cambiar la imagen mostrada facilmente
 * @author manuf
 *
 */
public class SwapImage extends Image {
	
	private Drawable[] images = new Drawable[2];
	private int current;
	/**
	 * Constructor recibe dos imagenes, el primer parametro es la activa inicialmente.
	 * @param img1
	 * @param img2
	 */
	public SwapImage(Texture img1, Texture img2) {
		super(img1);
		current = 0;
		images[0] = new TextureRegionDrawable(img1);
		
		images[1] = new TextureRegionDrawable(img2);

	}
	/**
	 * Cambia la imagen activa por la otra
	 */
	public void toggle() {
		current = ~current;
		this.setDrawable(images[current]);
	}
	/**
	 * Cambia la imagen mostrada a la especificada
	 * @param current
	 */
	public void setCurrent(int current) {
		this.current = current;
		this.setDrawable(images[current]);
	}
}
