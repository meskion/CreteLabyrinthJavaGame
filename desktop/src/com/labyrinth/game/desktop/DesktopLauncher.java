package com.labyrinth.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.labyrinth.game.mainGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.title = "Labyrinth";
		config.width = 800;
		config.height = 400;
	    config.resizable = false;
		
		new LwjglApplication(new mainGame(), config);
	}
}
