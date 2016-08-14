package com.kebabking.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

// load this screen while rest of game is loading
public class SplashScreen extends ScreenTemplate  {	
//	static float FadeInTime = 1.0f;
//	static float TransitionTime = 1.5f;
	static float FadeInTime = 1;
	static float TransitionTime = 1.5f;
	static boolean LoadingStarted = false;
	static boolean LoadingFinished = false;
	
	
	static boolean gate1 = false;
	static boolean gate2 = false;
	static boolean gate3 = false;
	static boolean gate4 = false;

	
//	static float FadeInTime = 2f;
//	static float TransitionTime = 2f;
	
	KebabKing master;
	
	SpriteBatch spriteBatch;
	
	float currentTime;
	boolean firstSwitch;
	boolean secondSwitch;
	float fadePercent;
	boolean fadeInComplete;
	
	Texture logo;
	Texture kebab;
	long startTime;
	long loadTime;
	
	Stage uiStage;
	Table table;
	Label loadText;
	Label title;
	
 	String[] textArray = {"buying_coal", "setting_up_grill", "marinating_meat", "sharpening_skewers","prepping_kebabs", "cooling_drinks"};
 	String allText;
 	//	float arrPercent;
//	float nextSwitch;
	int currentIndex = 0;
	
	static Thread loaderThread;
	
	public SplashScreen(KebabKing master) {
		super("Splash");
		this.master = master;
		// start loading assets
		startTime = System.currentTimeMillis();
		Assets.load();
		logo = Assets.peppercornLogo;
		kebab = Assets.bigMeat;
		
//		arrPercent = 1f / textArray.length;
//		nextSwitch = -1;
		currentIndex = 0;
		for (int i = 0; i < textArray.length; i++) {
			textArray[i] = "( " + Assets.strings.get(textArray[i]) + " ... )";
			allText += textArray[i];
		}
	}
	
	@Override
	public void render(float delta) {
		this.currentTime += delta;

		Assets.update();

		// Peppercorn screen
		if (!fadeInComplete) {
			Gdx.gl.glClearColor((51f/256), 51f/256, 51f/256, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			
//			KebabKing.print("/");

			// draw logo fading in on top
			spriteBatch.begin();
				
			spriteBatch.setColor(1,  1,  1, fadePercent);
			
			float logoWidth = KebabKing.getGlobalX(.9f);
			float logoHeight = logoWidth * (logo.getHeight()*1f/logo.getWidth()); 
			
			// TODO add function to ChuanrC that facilitates subtracting relative widths/heights
			float logoX = (KebabKing.getWidth() - logoWidth)/2;
			float logoY = (KebabKing.getHeight() - logoHeight)/2;
			
//			KebabKing.print("width: " + logoWidth + " height: " + logoHeight);
			
			spriteBatch.draw(logo, logoX, logoY, logoWidth, logoHeight);
			spriteBatch.end();

			fadeIn(delta);	
		}
		
		// Second screen
		else if (fadeInComplete && Assets.loadingComplete()) {
			Gdx.gl.glClearColor((51f/256), 51f/256, 51f/256, 1);

			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			
			uiStage.draw();
			
			if (!gate1) {
				gate1 = true;
				loadText.setText(getLoadText());
			}
			else if (!gate2) {
				master.initializeAssets();
				gate2 = true;
				loadText.setText(getLoadText());
			}
			else if (!gate3) {
				master.initializeProfile();
				gate3 = true;
				loadText.setText(getLoadText());
			}
			else if (!gate4) {
				master.initializeRemaining(startTime);
				loadText.setText(getLoadText());
//				this.dispose();
			}
			
//			// not done with loading textures
//			if (!Assets.loadingComplete()) {
//				// hacky but why not
//				if (!firstSwitch && currentTime > TransitionTime + 0.4) {
//					firstSwitch = true;
//					loadText.setText(getLoadText());
//				}
//				if (!secondSwitch && currentTime > TransitionTime + 1.0) {
//					secondSwitch = true;
//					loadText.setText(getLoadText());
//				}
//			}
//			else {
//				// initialize Loader thread
//				if (!LoadingStarted) {
//					LoadingStarted = true;
//					this.loadTime = System.currentTimeMillis() - this.loadTime;
//					AssetLoader t = new AssetLoader();
//					Thread loaderThread = new Thread(t);
//					try {
//						loaderThread.join();
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					loaderThread.start();
//					KebabKing.print("starting load thread");
//				}
//				else {
//					if (!LoadingFinished) {
//						KebabKing.print("Setting load text: " + loadText.getText());
//						try {
//							Thread.sleep((long) (Math.random() * 500 + 1000));
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//					else {
//						master.initializeProfile();
//						master.initializeRemaining(startTime);
//						this.dispose();
//					}
//				}
//			}
		}

		Gdx.gl.glClearColor((51f/256), 51f/256, 51f/256, 1);
	}
	
//	class AssetLoader implements Runnable{
//		public void run() {
//			master.initializeAssets();
////			try {
////				Thread.sleep(8000);
////			} catch (InterruptedException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
//			LoadingFinished = true;
//			return;
//		}
//	}
	
	public String getLoadText() {
//		if (Assets.getLoadProgress() > nextSwitch && Assets.getLoadProgress() < 1) {
//			currentIndex++;
//			nextSwitch += arrPercent;
//		}
//		if (currentIndex >= textArray.length) currentIndex = textArray.length - 1;
//		return textArray[currentIndex];
		return textArray[currentIndex++ % textArray.length];
	}
	
	public void fadeIn(float delta) {		
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
//		String titleText = Assets.strings.get("title");
//		title = new Label(titleText, Assets.generateLabelStyleUIChina(60, titleText));
//		title.setAlignment(Align.center);
//		Image image = new Image(kebab);
		float imageWidth = KebabKing.getGlobalX(1);
		float imageHeight = imageWidth * kebab.getHeight() / kebab.getWidth();
//		table.add(title).center().top().padTop(KebabKing.getGlobalY(0.1f));
//		table.row();
//		table.add(image).center().top().expandY().padTop(KebabKing.getGlobalY(0.1f)).width(imageWidth).height(imageHeight);
//		table.row();
		
		Table top = MainMenuScreen.getTitle();
		table.add(top).center().top().expandY().padTop(KebabKing.getGlobalY(0.35f)).width(imageWidth).height(imageHeight);
		table.row();
		
		loadText = new Label(getLoadText(), Assets.generateLabelStyleUI(20, allText));
		loadText.setAlignment(Align.center);
		table.add(loadText).center().bottom().padBottom(KebabKing.getGlobalY(0.1f));
	}

	@Override
	public void show() {
		
		spriteBatch = new SpriteBatch();
//        splsh = new Texture(Gdx.files.internal("splash.gif"));
	}
	
	public void specialDispose() {
		KebabKing.print("Disposing");
//		Assets.totalCharsForFonts -= Assets.allChars.length();
		KebabKing.print("total font chars (after disposing splash): " + Assets.totalCharsForFonts);
		// both of these are used later.
//		loadText.getStyle().font.dispose();
//		title.getStyle().font.dispose();
		logo.dispose();
//		kebab.dispose();
	}
}
