package com.kebabking.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

// load this screen while rest of game is loading
public class SplashScreen extends ScreenTemplate  {	
	static float FadeInTime = 0.1f;
	static float TransitionTime = 0.1f;
	
//	static float FadeInTime = 2f;
//	static float TransitionTime = 2f;
	
	KebabKing master;
	
	SpriteBatch spriteBatch;
	
	float currentTime;
	float fadePercent;
	boolean fadeInComplete;
	
	Texture logo;
	Texture kebab;
	long startTime;
	long loadTime;
	
	Stage uiStage;
	Table table;
	Label loadText;
	
	String[] textArray = {"buying coal", "prepping the grill", "marinating meat", "prepping kebabs"};
	float arrPercent;
	float nextSwitch;
	int currentIndex = 0;
	
	public SplashScreen(KebabKing master) {
		this.master = master;
		// start loading assets
		startTime = System.currentTimeMillis();
		Assets.load();
		logo = Assets.peppercornLogo;
		kebab = Assets.kebabMain;
		
		arrPercent = 1f / textArray.length;
		nextSwitch = -1;
		currentIndex = -1;
		for (int i = 0; i < textArray.length; i++) {
			textArray[i] = "( " + textArray[i] + " ... )";
		}
	}
	
	@Override
	public void render(float delta) {
		Assets.update();
		
		if (!fadeInComplete) {
			Gdx.gl.glClearColor(1, 1, 1, 1f);
			// draw logo fading in on top
			spriteBatch.begin();
				
			spriteBatch.setColor(1,  1,  1, fadePercent);
			
			float logoWidth = KebabKing.getGlobalX(.9f);
			float logoHeight = logoWidth * (logo.getHeight()*1f/logo.getWidth()); 
			
			// TODO add function to ChuanrC that facilitates subtracting relative widths/heights
			float logoX = (KebabKing.getWidth() - logoWidth)/2;
			float logoY = (KebabKing.getHeight() - logoHeight)/2;
			
//			System.out.println("width: " + logoWidth + " height: " + logoHeight);
			
			spriteBatch.draw(logo, logoX, logoY, logoWidth, logoHeight);
			spriteBatch.end();

			fadeIn(delta);
			
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		}
		
		// Second screen
		if (fadeInComplete) {
			Gdx.gl.glClearColor((51f/256), 51f/256, 51f/256, 1);

			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			
			uiStage.draw();
			loadText.setText(getLoadText());

			// done with everything
			if (Assets.loadingComplete()) {
				this.loadTime = System.currentTimeMillis() - this.loadTime;
				
				master.initialize(startTime);
				this.dispose();
			}
		}
		
//		 just white for now
//		Gdx.gl.glClearColor(fadePercent, fadePercent, fadePercent, 1f);
		
		Gdx.gl.glClearColor((51f/256), 51f/256, 51f/256, 1);
	}
	
	public String getLoadText() {
		if (Assets.getLoadProgress() > nextSwitch && Assets.getLoadProgress() < 1) {
			currentIndex++;
			nextSwitch += arrPercent;
		}
		return textArray[currentIndex];
	}
	
	public void fadeIn(float delta) {
		this.currentTime += delta;
		
		if (currentTime < FadeInTime) {
			this.fadePercent = currentTime / FadeInTime;
		}
		else {
			fadePercent = 1;
			
			if (currentTime > TransitionTime) {
				initStage();
				this.fadeInComplete = true;
				
			}
		}
	}
	
	public void initStage() {
		ScreenViewport viewport = new ScreenViewport();
		uiStage = new Stage(viewport, spriteBatch);		
		table = new Table();
		table.setSize(KebabKing.getWidth(), KebabKing.getHeight());
		uiStage.addActor(table);
		Label title = new Label("Kebab King", Assets.generateLabelStyleUIChinaWhite(60));
		title.setAlignment(Align.center);
		Image image = new Image(kebab);
		float imageWidth = KebabKing.getGlobalX(0.4f);
		float imageHeight = imageWidth * kebab.getHeight() / kebab.getWidth();
		table.add(title).center().top().padTop(KebabKing.getGlobalY(0.1f));
		table.row();
		table.add(image).center().top().expandY().padTop(KebabKing.getGlobalY(0.1f)).width(imageWidth).height(imageHeight);
		table.row();
		loadText = new Label("", Assets.generateLabelStyleUIWhite(20));
		loadText.setAlignment(Align.center);
		table.add(loadText).center().bottom().padBottom(KebabKing.getGlobalY(0.1f));
	}

	@Override
	public void show() {
		
		spriteBatch = new SpriteBatch();
//        splsh = new Texture(Gdx.files.internal("splash.gif"));
	}
}
