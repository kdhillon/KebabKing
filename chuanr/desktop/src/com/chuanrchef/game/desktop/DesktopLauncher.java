package com.chuanrchef.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.chuanrchef.game.ChuanrC;
import com.chuanrchef.game.Managers.Manager;

public class DesktopLauncher {
	ChuanrC game;
//	PlatformSpecficDesktop ps;
	
	// just a static entrypoint into the application
	public static void main (String[] arg) {
		DesktopLauncher dl = new DesktopLauncher();
		dl.toString();
	}
	
	public DesktopLauncher() {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 600;
		config.height = 1000;

        // Initialize the platform specific managers
        Manager.initDesktop();
		
		game = new ChuanrC();
//		ps = new PlatformSpecficDesktop();
//		ps.desktopLauncher = this;
        
		new LwjglApplication(game, config);
	}
}
