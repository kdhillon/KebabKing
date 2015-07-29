package com.chuanrchef.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.chuanrchef.game.ChuanrC;

public class DesktopLauncher {
	ChuanrC game;
	PlatformSpecficDesktop ps;
	
	// just a static entrypoint into the application
	public static void main (String[] arg) {
		DesktopLauncher dl = new DesktopLauncher();
		dl.toString();
	}
	
	public DesktopLauncher() {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 600;
		config.height = 1000;
		
		game = new ChuanrC();
		ps = new PlatformSpecficDesktop();
		ps.desktopLauncher = this;
		
		game.setPlatformSpecific(ps);
		
		new LwjglApplication(game, config);
	}
}
