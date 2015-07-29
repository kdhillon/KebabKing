package com.chuanrchef.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// load this screen while rest of game is loading
public class SplashScreen extends ScreenTemplate  {
	
	
//	static float FadeInTime = 2f;
//	static float TransitionTime = 4f;
	
	static float FadeInTime = 2f;
	static float TransitionTime = 2f;
	
	ChuanrC master;
	
	SpriteBatch spriteBatch;
	
	float currentTime;
	float fadePercent;
	boolean fadeInComplete;
	
	Texture logo;
	long startTime;

	public SplashScreen(ChuanrC master) {
		this.master = master;
		// start loading assets
		startTime = System.currentTimeMillis();
		Assets.load();
		logo = Assets.peppercornLogo;
	}
	
	@Override
	public void render(float delta) {
		Assets.update();
		if (Assets.loadingComplete() && fadeInComplete) {
//			this.loadTime = System.currentTimeMillis() - this.loadTime;
			master.initialize(startTime);
//			logo.dispose(); //  don't do this right now, it's going black
			this.dispose();
		}
		
//		 just white for now
		Gdx.gl.glClearColor(fadePercent, fadePercent, fadePercent, 1f);
		Gdx.gl.glClearColor(1, 1, 1, 1f);
		
		Gdx.gl.glClearColor((26f/256), 84f/256, 76f/256, 1);
		

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// draw logo fading in on top
		spriteBatch.begin();
			
		spriteBatch.setColor(1,  1,  1, fadePercent);
		
		float logoWidth = ChuanrC.width*.9f;
		float logoHeight = logoWidth * (logo.getHeight()*1f/logo.getWidth()); 
		
		float logoX = (ChuanrC.width - logoWidth)/2;
		float logoY = (ChuanrC.height - logoHeight)/2;
		
//		System.out.println("width: " + logoWidth + " height: " + logoHeight);
		
		spriteBatch.draw(logo, logoX, logoY, logoWidth, logoHeight);
		
		spriteBatch.end();
		
		
		fadeIn(delta);
	}
	
	public void fadeIn(float delta) {
		this.currentTime += delta;
		
		if (currentTime < FadeInTime) {
			this.fadePercent = currentTime / FadeInTime;
		}
		else {
			fadePercent = 1;
			
			if (currentTime > TransitionTime)
				this.fadeInComplete = true;
		}
	}

	@Override
	public void resize(int width, int height) {
		ChuanrC.width = width;
		ChuanrC.height = height;

		// initialize once
		if (KitchenScreen.UNIT_HEIGHT == 0 && KitchenScreen.UNIT_WIDTH == 0) {
			KitchenScreen.UNIT_WIDTH = (int) (ChuanrC.width / KitchenScreen.WIDTH);
			KitchenScreen.UNIT_HEIGHT = (int) (ChuanrC.height / KitchenScreen.HEIGHT);
		}
	}

	@Override
	public void show() {
		
		spriteBatch = new SpriteBatch();
//        splsh = new Texture(Gdx.files.internal("splash.gif"));
	}
}
