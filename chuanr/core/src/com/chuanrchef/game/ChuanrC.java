package com.chuanrchef.game;

import java.io.FileNotFoundException;
import java.util.Date;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.chuanrchef.game.Managers.Manager;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class ChuanrC extends Game {
	public static final boolean FORCE_NEW = false;
	public static final String SAVE_FILENAME = "cc.sav";
	public static final boolean ENGLISH = true;

	private static int width;
	private static int height; 

//	PlatformSpecific platformSpec;
	
//	boolean TUTORIAL_MODE;

	Kryo kryo;

	SpriteBatch batch; // master spritebatch used for everything
	Profile profile;

	// these guys are here so they can be drawn at all times
	Background bg;
	Grill grill;
	CustomerManager cm;

	// these three screens are permanent
	MainMenuScreen mainMenu;
	MainStoreScreen store;

	// these three screens are created each day, and need to be disposed of each day
	CountdownScreen countdown;
	KitchenScreen kitchen;
	PauseScreen pause;
	SummaryScreen summary; 
	
	// timing variables
	public long activityStartTime;

	// Summary Screens are temporary

	@Override
	public void create () {
		System.out.println("local storage path " + Gdx.files.getLocalStoragePath());
		this.activityStartTime = System.currentTimeMillis();
		
		this.kryo = new Kryo();

		kryo.register(Profile.class);

		// load splash screen first to load assets!
		System.out.println("loading splash");
		SplashScreen splash = new SplashScreen(this);
		this.setScreen(splash);
		
		// allow catching of back button on Android
        Gdx.input.setCatchBackKey(true);
	}


	public void initialize(long startTime) {
		long finalAssetStart = System.currentTimeMillis();
		Assets.finalizeLoading();
//		this.platformSpec.sendUserTiming("Asset Finalization", System.currentTimeMillis() - finalAssetStart);
		
		// load this from device in the future (only thing that needs to be saved)
		if (saveFileExists()) {
			long loadStartTime = System.currentTimeMillis();
			// load a new game
			if (this.load()) {
//				this.TUTORIAL_MODE = false;
//				this.platformSpec.sendUserTiming("Load", System.currentTimeMillis() - loadStartTime);
			}
			else {
				this.profile = new Profile(this);
//				this.TUTORIAL_MODE = true;
				System.out.println("Loading failed, creating a new profile");
			}
		}
		else {
			System.out.println("No save found, starting new profile");
			// start a new game
			// launch tutorial screen if first launch
			this.profile = new Profile(this);
//			this.TUTORIAL_MODE = true;
		}

		// initialize new spritebatch
		this.batch = new SpriteBatch();

		
		// load background which will always be present  
		bg = new Background(profile); 

		// load grill which will always be present
		grill = new Grill(profile);

		// load customer manager which will always be present
		cm = new CustomerManager(this);

		// if music is supposed to be playing, play it
		if (!profile.settings.muteMusic) Assets.music.play();

		// initialize screen
		mainMenu = new MainMenuScreen(this);
		setScreen(mainMenu); // have to set this immediately, otherwise height and width won't load properly

		store = new MainStoreScreen(this);

		bg.initialize(); // have to do  this after setScreen, so height and width work
		
		DrawUI.initializeUIBar(this, batch);
		
		long totalLoadTime = System.currentTimeMillis() - startTime;
//		this.platformSpec.sendUserTiming("Splash Load", totalLoadTime);
//		this.platformSpec.sendEventHit("App", "Started");
	}

	public void save() throws FileNotFoundException {	
		// open android fileoutputstream in internal storage
		//		FileOutputStream internal = openFileOutput("filename.sav", Context.MODE_PRIVATE);
		
		long startTime = System.currentTimeMillis();
	
		Output output = new Output(Manager.file.getOutputStream());

		Date date = new Date();
		kryo.writeObject(output, date);
		kryo.writeObject(output, this.profile);

		output.close();

		System.out.println("Game saved successfully!");
//		this.platformSpec.sendUserTiming("Save", System.currentTimeMillis() - startTime);
	}

//	public void deleteProfile() {
//		platformSpec.deleteProfile();
//		this.profile = new Profile();
//		System.out.println("deleting profile");
//		this.initialize();
//	}

	public void deleteSave() {
		Manager.file.deleteProfile();
	}
	
	public boolean saveFileExists() {
		if (FORCE_NEW)
			return false;
		else 
			return Manager.file.saveFileExists();
	}

	public boolean load() {
		if (kryo == null) return false;

		Input input = null;
		try {
			input = new Input(Manager.file.getInputStream());
		}
		catch (Exception e) {	
			System.out.println("no save file found");
			return false;
		}

		Profile profile;
		try {
			Date date = kryo.readObject(input, Date.class);
			profile = kryo.readObject(input, Profile.class);
			System.out.println("Loaded save from " + date.toString());
		}
		catch (Exception e) {	
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.out.println("save file in wrong format, creating new profile");
			input.close();
			deleteSave();
			// Create fresh profile
			profile = new Profile(this);
		}
		// update profile 
		this.profile = profile;	
		this.profile.master = this;
		
		if (TutorialScreen.FORCE_TUTORIAL) {
			this.profile.tutorialNeeded = true;
		}
		
		// update main menu screen stuff
//		this.initialize();
		
		input.close();
		return true;
	}

	// shutdown the stand
	public void shutdownStand() {
		this.kitchen.shutdown();
		System.out.println("");
	}

	public void startCountdown() {
		countdown = new CountdownScreen(this);
		this.setScreen(countdown);
	}

	// switch from main to kitchen
	public void startDay() {
		if (countdown != null)
			countdown.dispose();
		if (profile.tutorialNeeded) {
			kitchen = new TutorialScreen(this);
		}
		else {
			kitchen = new KitchenScreen(this);
		}
		pause = new PauseScreen(this);

		this.setScreen(kitchen);		
		
		long muted = 0;
		if (profile.settings.muteMusic) muted = 1;
		
		Manager.analytics.sendEventHit("Day", "Start", "Muted", muted);
	}

	// switch from kitchen screen to summary screen
	public void endDay() {
		kitchen.dispose();
		pause.dispose();
		
		summary = new SummaryScreen(kitchen);
		if (profile.tutorialNeeded) profile.tutorialNeeded = false;
		kitchen = null; // hopefully save some memory here
		this.setScreen(summary);
	}
	
	public void toggleMute() {
		if (!profile.settings.muteMusic) {
			profile.settings.muteMusic();
			Manager.analytics.sendEventHit("Volume", "Mute");
		}
		else {
			profile.settings.unmuteMusic();
			Manager.analytics.sendEventHit("Volume", "Unmute");
		}
	}

	public void kitchenPause() {
		this.setScreen(pause);
	}

	public void kitchenUnpause() {
		this.setScreen(kitchen);
	}

//	public void summaryToStore() {
//		this.setScreen(store);
//	}
	
	public void storeToMain() {
		mainMenu.reset();
		this.setScreen(mainMenu);
		bg.setToDay();
	}

	public void summaryToMain() {		
		if (summary != null) summary.dispose();
		mainMenu.reset();
		this.setScreen(mainMenu);
		bg.setToDay();
	}

	public void mainToStore() {
		this.setScreen(store);
	}

//	public void setPlatformSpecific(PlatformSpecific ps) {
//		this.platformSpec = ps;
//		Analytics.init(ps);
//	}

	public void makePurchase(OnlinePurchaseManager.PurchaseableOnline choice) {
		Manager.iab.makePurchase(choice.productID);
	}
	
	// confirm that user has purchased specified product
	public void confirmPurchase(String productID) {
		OnlinePurchaseManager.PurchaseableOnline purchased = OnlinePurchaseManager.getPurchaseableForID(productID);
		if (purchased == null) {
			System.out.println("Error: purchased invalid");
			return;
		}
		System.out.println("You just purchased " + purchased.coins + " coins!");		
		
		Manager.analytics.sendEventHit("OnlinePurchase", "Coins", productID);

		this.profile.coins += purchased.coins;
	}

	// go to pause screen
	public void pause() {

	}

	// switch to store screen
	public void store() {

	}

	// switch to main menu screen
	public void menu() {

	}
	
	public static void setWidth(int toSet) {
		width = toSet;
	}
	public static void setHeight(int toSet) {
		height = toSet;
	}
	public static int getWidth() {
		return width;
	}
	public static int getHeight() {
		return height;
	}
	
	public static int getGlobalX(float percentX) {
		return (int) (percentX * width);
	}
	public static int getGlobalY(float percentY) {
		return (int) (percentY * height);
	}
}
