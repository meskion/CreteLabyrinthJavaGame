package com.labyrinth.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class WinScreen extends MenuScreen {

	private Stage stage;
	private Texture menuBkg, cursorSprite;
	private Table table;
	private TextButton volver, salir;
	private Label namelabel;
	private AbsCursor cursor;
//	private Music victoryMusicA, victoryMusicB;
	private TextButton otraVez;

	public WinScreen(mainGame game, String name) {
		super(game);
		stage = new Stage();
//		victoryMusicA = Gdx.audio.newMusic(Gdx.files.internal("sounds/victoryA.wav"));
//		victoryMusicB = Gdx.audio.newMusic(Gdx.files.internal("sounds/victoryB.wav"));
		menuBkg = new Texture(Gdx.files.internal("sprites/blankMenu.jpg"));
		Gdx.input.setInputProcessor(stage);
		cursorSprite = new Texture(Gdx.files.internal("sprites/menuArrow.png"));
		Gdx.input.setInputProcessor(stage);
		table = new Table();
		stage.addActor(table);
		LabelStyle vStyle = new LabelStyle(game.lStyle);
		vStyle.fontColor = new Color(111f / 255f, 29f / 255f, 27f / 255f, 1);
		namelabel = new Label("¡HAS GANADO, " + name + "!", vStyle);
		
		// 111 29 27
		namelabel.sizeBy(100);
		volver = new TextButton("volver al menu", game.bStyle);
		otraVez = new TextButton("jugar otra vez", game.bStyle);
		salir = new TextButton("salir", game.bStyle);
		table.setPosition(400, 180);

		table.add(namelabel).pad(7).padBottom(13);
		table.row();
		table.add(volver).pad(7);
		table.row();
		table.add(otraVez).pad(7);
		table.row();
		table.add(salir).pad(7);

		cursor = new AbsCursor(new Vector2(20 * 128 - 280, -20 * 128 + -30),
				new Vector2(20 * 128 - 260, -20 * 128 - 150), new Vector2(20 * 128 - 140, -20 * 128 - 270)) {

			@Override
			public void act() {
				switch (this.menuCursor) {
				case 0:
					game.setScreen(new MainMenuScreen(game));
					dispose();
					break;
				case 1:
					game.setScreen(new NewGameScreen(game));
					dispose();
					break;
				case 2:
					dispose();
					Gdx.app.exit();
				default:
					break;
				}
				optionSelect.play();

			}
		};
		
		
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		game.batch.begin();
		game.batch.draw(menuBkg, 20 * 128 - 1200 / 2, -20 * 128 - 900 / 2);
		game.batch.draw(cursorSprite, cursor.rect.x, cursor.rect.y, cursorSprite.getWidth(), cursorSprite.getHeight(),
				0, 0, cursorSprite.getWidth(), cursorSprite.getHeight(), false, false);
		game.batch.end();
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		cursor.update();
		game.victoryMusicA.play();
		if (!game.victoryMusicA.isPlaying())
			game.victoryMusicB.play();

		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
			cursor.act();

		}
	}
	
	@Override
	public void dispose() {
		super.dispose();
		game.victoryMusicA.stop();
		game.victoryMusicB.stop();
	
		menuBkg.dispose();
		cursorSprite.dispose();
	}

}
