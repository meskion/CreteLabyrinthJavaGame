package com.labyrinth.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.labyrinth.game.mainGame;
/**
 * Lanza la aplicacion de escritorio, con la resolución especificada.
 * @author manuf
 *
 */
public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.title = "Laberinto de Creta";
		config.width = 800;
		config.height = 400;
	    config.resizable = false;
		
		new LwjglApplication(new mainGame(), config);
	}
}
