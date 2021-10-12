package com.labyrinth.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;

public class NewGameScreen extends MenuScreen {
	private mainGame game;
	private Stage stage;
	private Table table, left, right;
	private ButtonGroup<TextButton> radioButtons;
	private TextButton easy, medium, hard;
	private Texture menuBkg, cursorSprite;
	private Label namelabel;
	private TextField name;
	private AbsCursor cursor;
	private int difficulty;

	public NewGameScreen(final mainGame game) {
		super(game);
		this.game = game;
		stage = new Stage();
		menuBkg = new Texture(Gdx.files.internal("sprites/blankMenu.jpg"));
		cursorSprite = new Texture(Gdx.files.internal("sprites/menuArrow.png"));

		Gdx.input.setInputProcessor(stage);
		table = new Table();
		left = new Table();
		right = new Table();
		

		// table.setFillParent(true);

		stage.addActor(table);
		easy = new TextButton("facil", game.bStyle);
		medium = new TextButton("medio", game.bStyle);
		hard = new TextButton("dificil", game.bStyle);
		

		namelabel = new Label("Escribe tu nombre", game.lStyle);
//		namelabel.setColor( 0.9f,  0.7f,  0.63f,  1f);
//		namelabel.setPosition(100, 200);
		name = new TextField("", game.tStyle);
		name.setColor(0.9f, 0.8f, 0.73f, 1f);
		name.setText("Teseo");

		table.setPosition(400, 180);
		table.add(left, right);

		left.pad(15);

		right.pad(15);

		left.add(easy).pad(13);
		left.row();
		left.add(medium).pad(13);
		left.row();
		left.add(hard).pad(13);

		right.add(namelabel).pad(20);
		right.row();
		right.add(name);

		stage.setKeyboardFocus(name);
//		table.align(Align.bottom);

//		table.add( new Label("Escribe tu nombre", skin));

		cursor = new AbsCursor(new Vector2(20 * 128 - 250, -20 * 128 + 70), new Vector2(20 * 128 - 250, -20 * 128 - 85),
				new Vector2(20 * 128 - 250, -20 * 128 - 240)) {

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

	@Override
	public void render(float delta) {
		super.render(delta);

//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		game.batch.begin();
		game.batch.draw(menuBkg, 20 * 128 - 1200 / 2, -20 * 128 - 900 / 2);
		game.batch.draw(cursorSprite, cursor.rect.x, cursor.rect.y, cursorSprite.getWidth(), cursorSprite.getHeight(),
				0, 0, cursorSprite.getWidth(), cursorSprite.getHeight(), true, false);
		game.batch.end();
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		cursor.update();

		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
			cursor.act();
			game.setScreen(new GameScreen(game, name.getText(), difficulty));
			optionSelect.play();
			game.menuMusic.stop();
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
		// TODO Auto-generated method stub

	}

}
