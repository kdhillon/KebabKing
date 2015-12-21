package com.kebabking.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// load this screen while rest of game is loading
public class SplashScreen extends ScreenTemplate  {	
	static float FadeInTime = 0f;
	static float TransitionTime = 0f;
	
//	static float FadeInTime = 2f;
//	static float TransitionTime = 2f;
	
	KebabKing master;
	
	SpriteBatch spriteBatch;
	
	float currentTime;
	float fadePercent;
	boolean fadeInComplete;
	
	Texture logo;
	long startTime;

	public SplashScreen(KebabKing master) {
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
		
		float logoWidth = KebabKing.getGlobalX(.9f);
		float logoHeight = logoWidth * (logo.getHeight()*1f/logo.getWidth()); 
		
		// TODO add function to ChuanrC that facilitates subtracting relative widths/heights
		float logoX = (KebabKing.getWidth() - logoWidth)/2;
		float logoY = (KebabKing.getHeight() - logoHeight)/2;
		
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
	public void show() {
		
		spriteBatch = new SpriteBatch();
//        splsh = new Texture(Gdx.files.internal("splash.gif"));
	}
}