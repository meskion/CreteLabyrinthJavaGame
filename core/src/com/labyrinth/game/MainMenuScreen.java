package com.labyrinth.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {

	final int width = 800;
	final int height = 400;
	Texture wallSprite, menuSprite, cursorSprite;
	final mainGame game;
	List<Rectangle> walls = new ArrayList<>();
	private int menuCursor = 0;
	Rectangle cursor;
	final Map<Integer,Vector2> dictMenu = Map.of(
			0,new Vector2(20*128 - 280, -20*128 + 30),
			1,new Vector2(20*128 -225, -20*128 - 120),
			2, new Vector2(20*128 -160, -20*128 - 270));
	OrthographicCamera camera;

	public MainMenuScreen(final mainGame game) {
		this.game = game;
		wallSprite = new Texture(Gdx.files.internal("sprites/wall.png"));
		menuSprite = new Texture(Gdx.files.internal("sprites/menu.jpg"));
		cursorSprite = new Texture(Gdx.files.internal("sprites/teseo.png"));
		GameScreen.generateLevel(walls, 40, 40, GameScreen.step);
		camera = new OrthographicCamera();
		camera.setToOrtho(false, width*3, height*3);
		camera.position.set( 20*128, -20*128,0);
		cursor = new Rectangle();
		cursor.setPosition(dictMenu.get(0));

	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(250f / 255f, 237f / 255f, 205f / 255f, 1f);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		
		
		game.batch.begin();
//		game.font.draw(game.batch, "Crete Labyrinth",  width/2, height/2, width/3, 0, false);
//		game.font.draw(game.batch, "Crete Labyrinth" , width/2 - 30, height/2);
//		game.font.draw(game.batch, "Start", width/2, height/2 - 40);
//		game.batch.draw(t, width/2 -t.getWidth()/2, height/2 - t.getHeight()/2, t.getWidth(), t.getHeight());
	
		for (Rectangle wall : walls) {
			game.batch.draw(wallSprite, wall.x, wall.y);
		}
		game.batch.draw(menuSprite, 20*128 - 1200/2,-20*128 - 900/2);
		game.batch.draw(cursorSprite,cursor.x,cursor.y);
		game.batch.end();

		if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {//cambiar a interactuar con los botones
			menuCursor = (menuCursor +1) % 3;
			cursor.setPosition(dictMenu.get(menuCursor));
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {//cambiar a interactuar con los botones
			menuCursor = (menuCursor  + 2) % 3;
			cursor.setPosition(dictMenu.get(menuCursor));
		}
		
		
		
		if  (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
			switch (menuCursor) {
			case 0:
				// al formulario
				break;
			case 1:
				game.setScreen(new GameScreen(game));
				dispose();
				break;
			case 2:
				//exit;
			default:
				break;
			}
			

		}

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		wallSprite.dispose();
		

	}

}
