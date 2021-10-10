package com.labyrinth.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class SwapImage extends Image {
	
	private Drawable[] images = new Drawable[2];
	private int current;
	
	public SwapImage(Texture img1, Texture img2) {
		super(img1);
		current = 0;
		images[0] = new TextureRegionDrawable(img1);
		
		images[1] = new TextureRegionDrawable(img2);

	}
	
	public void toggle() {
		current = ~current;
		this.setDrawable(images[current]);
	}
	
	public void setCurrent(int current) {
		this.current = current;
		this.setDrawable(images[current]);
	}
}
