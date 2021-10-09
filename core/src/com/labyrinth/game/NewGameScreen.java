package com.labyrinth.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

public class NewGameScreen extends MenuScreen {
	private mainGame game;
	private Stage stage;
	private Table table, left, right;
	private ButtonGroup<TextButton> radioButtons;
	private TextButton easy, medium, hard;
	private Skin skin;
	private BitmapFont font;
	private Texture menuBkg;
	private TextButtonStyle bStyle;
	private TextFieldStyle tStyle;
	private LabelStyle lStyle;
	private Label namelabel;
	private TextField name;

	public NewGameScreen(final mainGame game) {
		super(game);
		this.game = game;
		stage = new Stage();
		menuBkg = new Texture(Gdx.files.internal("sprites/blankMenu.jpg"));
		skin = new Skin(Gdx.files.internal("data/pixthulhu-ui.json"));
		font = new BitmapFont(Gdx.files.internal("data/bgregular.fnt"));
		
		Gdx.input.setInputProcessor(stage);
		table = new Table();
		left = new Table();
		right = new Table();
		
		Color niceGreen = new Color( 0.33f,  0.4f,  0.14f,  1f);
		tStyle = skin.get("default", TextFieldStyle.class);
		bStyle = new TextButtonStyle();
		lStyle = new LabelStyle();
		lStyle.fontColor = niceGreen;
		bStyle.fontColor = niceGreen;
		tStyle.font = font;
		bStyle.font = font;
		lStyle.font = font;
		
		//		table.setFillParent(true);
		
		stage.addActor(table);
		easy = new TextButton("facil", bStyle);
		medium = new TextButton("medio", bStyle);
		medium.setChecked(true);
		hard = new TextButton("dificil", bStyle);
		radioButtons = new ButtonGroup<TextButton>(easy,medium, hard);
		radioButtons.setMinCheckCount(1);
		radioButtons.setMaxCheckCount(1);
		
		
		
		

		
		namelabel = new Label("Escribe tu nombre", lStyle);
//		namelabel.setColor( 0.9f,  0.7f,  0.63f,  1f);
//		namelabel.setPosition(100, 200);
		name = new TextField("", tStyle);
		name.setColor( 0.9f,  0.8f,  0.73f,  1f);
		
		table.setPosition(400, 180);
		table.add(left,right);
		
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
		game.batch.end();
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void resize (int width, int height) {
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
