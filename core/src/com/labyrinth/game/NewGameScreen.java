package com.labyrinth.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class NewGameScreen extends MenuScreen {
	private mainGame game;
	private Stage stage;
	private Table table, left, right;

	private TextButton easy, medium, hard;
	private Texture menuBkg, cursorSprite;
	private Label namelabel;

	private TextField name;
	private AbsCursor cursor;
	private int difficulty;
	private Label nameError;

	/**
	 * Crea el menu con todos sus elementos de GUI
	 * 
	 * @param game
	 */
	public NewGameScreen(final mainGame game) {
		super(game);
		this.game = game;

		stage = new Stage();
		menuBkg = new Texture(Gdx.files.internal("sprites/blankMenu.jpg"));
		cursorSprite = new Texture(Gdx.files.internal("sprites/menuArrow.png"));
		Gdx.input.setInputProcessor(stage);

		/*
		 * Clases contenedoras que definen el layout
		 */
		table = new Table();
		Container<Label> valBox = new Container<>();
		left = new Table();
		right = new Table();

		stage.addActor(table);
		table.add(left, right);
		stage.addActor(valBox);

		/*
		 * Display de opciones de dificultad
		 */
		easy = new TextButton("facil", game.bStyle);
		medium = new TextButton("medio", game.bStyle);
		hard = new TextButton("dificil", game.bStyle);

		/*
		 * etiquetas y entrada de texto para el nombre del jugador
		 */
		namelabel = new Label("Escribe tu nombre", game.lStyle);
		nameError = new Label("necesitas nombre!", new LabelStyle(game.lStyle));
		nameError.setVisible(false);
		nameError.getStyle().fontColor = Color.RED;
		name = new TextField("", game.tStyle);
		name.setColor(0.9f, 0.8f, 0.73f, 1f);
		name.setText("Teseo");

		/* Se añaden los elementos a los contenedores y se reposicionan y ajustan */
		valBox.setActor(nameError);
		valBox.setPosition(480, 110);
		table.setPosition(400, 180);

		left.pad(15);

		right.pad(15);

		left.add(easy).pad(13);
		left.row();
		left.add(medium).pad(13);
		left.row();
		left.add(hard).pad(13);

		right.add(namelabel).padBottom(10);
		right.row();
		right.add(name);

		/*
		 * damos el foco a la entrada de texto del nombre para que pueda escribirlo
		 * directamente
		 */
		stage.setKeyboardFocus(name);

		/* Instanciamos e implementamos un Cursor para el menu */
		cursor = new AbsCursor(new Vector2(ref - 200, -ref + 70), new Vector2(ref - 200, -ref - 85),
				new Vector2(ref - 200, -ref - 240)) {
			/**
			 * define que hace el cursor en este menu, cambiar la dificultad
			 */
			@Override
			public void act() {
				difficulty = 15 * (this.menuCursor + 1);

			}
		};

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	/**
	 * Dibuja la escena y controla los inputs.
	 */
	@Override
	public void render(float delta) {
		super.render(delta);

		game.batch.begin();
		game.batch.draw(menuBkg, ref - 1200 / 2, -ref - 900 / 2);
		game.batch.draw(cursorSprite, cursor.rect.x, cursor.rect.y, cursorSprite.getWidth(), cursorSprite.getHeight(),
				0, 0, cursorSprite.getWidth(), cursorSprite.getHeight(), true, false);
		game.batch.end();
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		cursor.update();

		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
			String nameTxt = name.getText();
			if (nameTxt.length() < 1) {
				nameError.setVisible(true);
			} else {
				cursor.act();
				game.setScreen(new GameScreen(game, nameTxt, difficulty));
				optionSelect.play();
				game.menuMusic.stop();
				dispose();
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
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
		super.dispose();
		menuBkg.dispose();
		cursorSprite.dispose();

	}

}
