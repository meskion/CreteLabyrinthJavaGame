package com.labyrinth.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class NewGameScreen extends MenuScreen {
	private mainGame game;
	private Stage stage;
	private TextField name;
	
	public NewGameScreen(final mainGame game) {
		super(game);
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		name = new TextField("usuario", new TextField.TextFieldStyle() );
	}
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		game.batch.begin();
		
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
		// TODO Auto-generated method stub
		
	}

}
