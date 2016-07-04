package com.kebabking.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.kebabking.game.KebabKing;
import com.kebabking.game.Managers.Manager;

public class KKLauncher {
	KebabKing game;
//	PlatformSpecficDesktop ps;
	
	// just a static entrypoint into the application
	public static void main (String[] arg) {
		KKLauncher dl = new KKLauncher();
		dl.toString();
	}
	
	public KKLauncher() {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 600;
		config.height = 1000;

//		config.width = 550;
//		config.height = 1000;
		
		
        // Initialize the platform specific managers
        Manager.initDesktop();
		
		game = new KebabKing();
//		ps = new PlatformSpecficDesktop();
//		ps.desktopLauncher = this;
        
		new LwjglApplication(game, config);
	}
}
