package com.kebabking.game;

import java.io.FileNotFoundException;
import java.util.Date;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer;
import com.kebabking.game.Managers.Manager;
import com.kebabking.game.Purchases.Purchaseable;

public class KebabKing extends Game {
	//	public static final boolean TEST_MODE = false;
	public static final String lang = "en";
	public static final boolean DEBUG = true;
	public static final boolean EXP_CITY = false; // get 300 exp after day
	public static final boolean SHORT_DAY = false;
	public static final boolean LVL_50 = false;
	public static final boolean RICH_MODE = false;
	public static final boolean FORCE_NEW = false;
	public static final boolean DISABLE_TUTORIAL = false;
	public static final boolean DISABLE_FIRST_DAY = false;
	
	public static final boolean SAVE_AFTER_NEW = false;
	public static final boolean DONT_SAVE = false;
	public static final boolean VERIFY_SAVE = false;
	public static final boolean START_MUTED = false;
	public static final boolean STRICT_MODE = false; // asserts that save files are in right format, and that only one notification is active at once

	public static final String SAVE_FILENAME = "kk.sav";

	// shut down stand for 5 minutes unless they pay the fine
	public static final long SHUTDOWN_LENGTH_SECONDS = 300;
	//	public static final int SHUTDOWN_LENGTH_SECONDS = 30;

	private static int width;
	private static int height; 

	//	PlatformSpecific platformSpec;

	//	boolean TUTORIAL_MODE;

	Kryo kryo;

	SpriteBatch batch; // master spritebatch used for everything

	public Profile profile; // includes settings, inventory, stats

	// these guys are here so they can be drawn at all times
	Background bg;
	public Grill grill;
	CustomerManager cm;

	// this should be disposed a few seconds after loading mainscreen
	SplashScreen splash;

	// these three screens are permanent
	MainMenuScreen mainMenu;
	MainStoreScreen store;
//	JadeWheelScreen jadeWheelScreen;
	SettingsScreen settingsScreen;

	// these three screens are created each day, and need to be disposed of each day
	CountdownScreen countdown;
	KitchenScreen kitchen;
	PauseScreen pause;
	SummaryScreen summary; 

	// timing variables
	public long activityStartTime;

	ActiveScreen marketFromThis;

	Screen settingsFromThis;
	Screen jadeWheelFromThis;
	// Summary Screens are temporary

	@Override
	public void create () {
		KebabKing.print("local storage path " + Gdx.files.getLocalStoragePath());
		this.activityStartTime = System.currentTimeMillis();

		this.kryo = new Kryo();
		kryo.setDefaultSerializer(TaggedFieldSerializer.class);

		// make sure to serailize in this order
		kryo.register(Profile.class);//, new TaggedFieldSerializer(kryo, ProfileRobust.class));
		kryo.register(ProfileSettings.class);//, new TaggedFieldSerializer(kryo, ProfileSettings.class));
		kryo.register(ProfileStats.class);//, new TaggedFieldSerializer(kryo, ProfileStats.class));
		kryo.register(ProfileInventory.class);//, new TaggedFieldSerializer(kryo, ProfileInventory.class));

		// load splash screen first to load assets!
		KebabKing.print("loading splash");
		splash = new SplashScreen(this);
		this.setScreen(splash);

		// set up iab and ads
		OnlinePurchaseHandler.init(this);
		AdsHandler.init(this);
		SocialMediaHandler.init(this);
		TutorialEventHandler.init(this);
		StatsHandler.init(this);
		SoundManager.init(this);

		// allow catching of back button on Android
		Gdx.input.setCatchBackKey(true);
	}

	public void initializeAssets() {
		long finalAssetStart = System.currentTimeMillis();
		Assets.finalizeLoading();
		Manager.analytics.sendUserTiming("Asset Finalization", System.currentTimeMillis() - finalAssetStart);
	}

	public void initializeProfile() {
		// load this from device in the future (only thing that needs to be saved)
		if (saveFileExists()) {
			long loadStartTime = System.currentTimeMillis();
			// load a new game
			if (this.load()) {
				//				this.TUTORIAL_MODE = false;
				Manager.analytics.sendUserTiming("Load", System.currentTimeMillis() - loadStartTime);
			}
			else {
				// if absolutely no save file found, launch a completely new profile.	
				//				this.TUTORIAL_MODE = true;

				// save exists, at least try loading parts of it?
				// TODO
				KebabKing.print("Loading failed because Kryo is null, creating a new profile");
				if (STRICT_MODE) 
					throw new java.lang.AssertionError();
				createFreshProfile();
			}
		}
		else {
			KebabKing.print("No save found, starting completely new profile");
			if (STRICT_MODE)
				throw new java.lang.AssertionError();

			// start a new game
			// launch tutorial screen if first launch
			createFreshProfile();
			profile.initializeAfterLoad(this);	

			//			TUTORIAL_MODE = true;
		}
		TutorialEventHandler.initializeAfterLoad();
	}

	public void createFreshProfile() {
		this.profile = new Profile(this);
		this.profile.settings = new ProfileSettings();
		this.profile.stats = new ProfileStats();
		this.profile.inventory = new ProfileInventory(profile);

		// test just for now
		if (SAVE_AFTER_NEW) {
			try {
				save();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void initializeRemaining(long startTime) {
		long finalRemainingStart = System.currentTimeMillis();

		// initialize new spritebatch
		this.batch = new SpriteBatch();

		DrawUI.initialize(this, batch);

		// load background which will always be present
		bg = new Background(profile);

		// load grill which will always be present
		grill = new Grill(profile);

		// load customer manager which will always be present
		cm = new CustomerManager(this);

		// initialize screen
		mainMenu = new MainMenuScreen(this);

		store = new MainStoreScreen(this);

		settingsScreen = new SettingsScreen(this);

//		jadeWheelScreen = new JadeWheelScreen(this);
		
		bg.initialize(); // have to do  this after setScreen, so height and width work

		Manager.analytics.sendUserTiming("Remaining Finalization", System.currentTimeMillis() - finalRemainingStart);

		long totalLoadTime = System.currentTimeMillis() - startTime;
		Manager.analytics.sendUserTiming("Splash Load", totalLoadTime);
		Manager.analytics.sendEventHit("App", "Started");
		
		setScreen(mainMenu); // have to set this immediately, otherwise height and width won't load properly

		Manager.iab.checkConsumables();
	}

	//	public void initialize(long startTime) {
	//		initializeAssets();
	//		initializeProfile();
	//		initializeRemaining(startTime);
	//	}

	public void disposeSplash() {
		splash.specialDispose();
	}

	public void save() {
		if (DONT_SAVE) return;

		KebabKing.print("Trying to save");
		// open android fileoutputstream in internal storage
		//		FileOutputStream internal = openFileOutput("filename.sav", Context.MODE_PRIVATE);

		long startTime = System.currentTimeMillis();

		Output output = null;
		try {
			output = new Output(Manager.file.getOutputStream());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		// Order is important here
		Date date = new Date();
		kryo.writeObject(output, date);
		kryo.writeObject(output, this.profile);
		kryo.writeObject(output, this.profile.settings);
		kryo.writeObject(output, this.profile.stats);
		kryo.writeObject(output, this.profile.inventory);

		output.close();

		if (VERIFY_SAVE) {
			Profile p = getSavedProfile();
			if (p == null) throw new java.lang.AssertionError();
		}

		KebabKing.print("Game saved successfully!");
		Manager.analytics.sendUserTiming("Save", System.currentTimeMillis() - startTime);
	}

	//	public void deleteProfile() {
	//		platformSpec.deleteProfile();
	//		this.profile = new Profile();
	//		KebabKing.print("deleting profile");
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

	// useful for testing whether save is actually saving properly
	private Profile getSavedProfile() {		
		Input input = null;
		try {
			input = new Input(Manager.file.getInputStream());
		}
		catch (Exception e) {	
			KebabKing.print("no save file found");
			return null;
		}

		// the order of loading/saving these is important.
		Date date;
		// profile is most important, so load first.
		Profile profile;
		ProfileSettings settings;
		ProfileStats stats;
		ProfileInventory inventory;

		// Try loading all things separately:
		try {
			date = kryo.readObject(input, Date.class);
			KebabKing.print("Loading save from " + date.toString());			
		}
		catch (Exception e) {
			KebabKing.print(e.getMessage());
			e.printStackTrace();
			KebabKing.print("Date in wrong format");
		}

		// Load profile
		try {
			profile = kryo.readObject(input, Profile.class);
		}
		catch (Exception e) {
			KebabKing.print(e.getMessage());
			e.printStackTrace();
			KebabKing.print("Profile in wrong format, creating new!");
			profile = new Profile();
			
			if (STRICT_MODE) throw new java.lang.AssertionError();
		}

		// Load settings
		try {
			settings = kryo.readObject(input, ProfileSettings.class);
		}
		catch (Exception e) {
			KebabKing.print(e.getMessage());
			e.printStackTrace();
			KebabKing.print("Settings in wrong format, creating new!");
			settings = new ProfileSettings();
			
			if (STRICT_MODE) throw new java.lang.AssertionError();
		}

		// Load stats
		try {
			stats = kryo.readObject(input, ProfileStats.class);
		}
		catch (Exception e) {
			KebabKing.print(e.getMessage());
			e.printStackTrace();
			KebabKing.print("Stats in wrong format, creating new!");
			stats = new ProfileStats();
			
			if (STRICT_MODE) throw new java.lang.AssertionError();
		}

		// Load inventory
		try {
			inventory = kryo.readObject(input, ProfileInventory.class);
		}
		catch (Exception e) {
			KebabKing.print(e.getMessage());
			e.printStackTrace();
			KebabKing.print("Inventory in wrong format, creating new!");
			inventory = new ProfileInventory(profile);
			
			if (STRICT_MODE) throw new java.lang.AssertionError();
		}

		input.close();

		// everything has been initialized by now
		profile.settings = settings;
		profile.stats = stats;
		profile.inventory = inventory;
		inventory.profile = profile;

		return profile;
	}

	public boolean load() {
		if (kryo == null) return false;
		Profile profile = getSavedProfile();
		this.profile = profile;
		profile.initializeAfterLoad(this);


		KebabKing.print("save loaded successfully");

		return true;

		//		try {
		//			profile = kryo.readObject(input, ProfileSettings.class);
		//		}
		//		catch (Exception e) {	
		//			
		//			KebabKing.print("save file in wrong format, creating new profile");
		//			input.close();
		//			deleteSave();
		//			// Create fresh profile
		//			profile = new Profile(this);
		//		}
		// update profile 
		//		this.profile = profile;	
		//		this.profile.master = this;
		////		
		//		if (TutorialScreen.FORCE_TUTORIAL) {
		//			this.profile.tutorialNeeded = true;
		//		}

		// update main menu screen stuff
		//		this.initialize();

	}

	// shutdown the stand
	public void shutdownStand() {
		this.kitchen.shutdown();
		KebabKing.print("");
	}

	public void startCountdown() {
		countdown = new CountdownScreen(this);
		this.setScreen(countdown);
	}

	// switch from main to kitchen
	public void startDay() {
		if (countdown != null)
			countdown.dispose();
		//		if (profile.tutorialNeeded) {
		//			kitchen = new TutorialScreen(this);
		//		}
		//		else {
		kitchen = new KitchenScreen(this);
		//		}
		pause = new PauseScreen(this);

		this.setScreen(kitchen);		

		long muted = 0;
		if (profile.settings.muteMusic) muted = 1;

		if (profile.stats.daysWorked == 1) TutorialEventHandler.handleSecondDayBegun();
		else if (profile.stats.daysWorked == 2) TutorialEventHandler.handleThirdDayBegun();
		else if (profile.stats.daysWorked == 3) TutorialEventHandler.handleFourthDayBegun();

		Manager.analytics.sendEventHit("Day", "Start", "Muted", muted);
	}

	// switch from kitchen screen to summary screen
	public void endDay() {
		kitchen.dispose();
		pause.dispose();

		// saving here should be redundant with creating summary screen, which saves after exp is loaded.
		//
		//		try {
		//			save();
		//		} catch (FileNotFoundException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
		if (profile.stats.daysWorked == 0) TutorialEventHandler.handleFirstDayComplete();
		else if (profile.stats.daysWorked == 1) TutorialEventHandler.handleSecondDayComplete();
		else if (profile.stats.daysWorked == 2) TutorialEventHandler.handleThirdDayComplete();

		summary = new SummaryScreen(this, kitchen);
		//		summary = new SummaryScreen(kitchen);
		//		if (profile.tutorialNeeded) profile.tutorialNeeded = false;
		kitchen = null; // hopefully save some memory here
		this.setScreen(summary);
	}

	public void kitchenPause() {
		if (grill != null) {
			grill.pause();
		}
		this.setScreen(pause);
		SoundManager.pauseAll();
	}

	public void kitchenUnpause() {
		this.setScreen(kitchen);
		SoundManager.resumeAll();
	}

	public void switchToSettings() {
		// DON'T ALLOW SWITCH FROM SETTINGS TO SETTINGS
		if (this.getScreen() == settingsScreen) return;

		this.settingsFromThis = this.getScreen();
		if (settingsScreen == null) throw new java.lang.AssertionError();
		this.setScreen(settingsScreen);
		KebabKing.print("Just switched to settings");
	}

	public void settingsBack() {
		if (settingsFromThis == null) throw new java.lang.AssertionError();
		this.setScreen(settingsFromThis);
		this.settingsFromThis = null;
	}
	
//	public void switchToJadeWheelScreen() {
//		if (this.getScreen() == jadeWheelScreen) return;
//		
//		this.jadeWheelFromThis = this.getScreen();
//		this.setScreen(jadeWheelScreen);
//	}
	
	public void jadeWheelBack() {
		if (jadeWheelFromThis == null) throw new java.lang.AssertionError();
		this.setScreen(jadeWheelFromThis);
		this.jadeWheelFromThis = null;
	}

	//	public void summaryToStore() {
	//		this.setScreen(store);
	//	}

	public void leaveStore() {
		// go to summary screen
		if (marketFromThis != null) {
			this.setScreen(marketFromThis);
			marketFromThis = null;
		}
		else {
			mainMenu.reset();
			this.setScreen(mainMenu);
			bg.setToDay();
		}
	}

	//	public void storeToMain() {
	//		mainMenu.reset();
	//		this.setScreen(mainMenu);
	//		bg.setToDay();
	//	}

	public void summaryToStore() {		
		if (summary != null) {
			summary.dispose();
			this.summary = null;
		}
		KebabKing.print("summary to store");
		mainMenu.reset();
		this.setScreen(store);
		bg.setToDay();
	}

	
	public void summaryToMain() {		
		if (summary != null) {
			summary.dispose();
			this.summary = null;
		}
		KebabKing.print("summary to main");
		mainMenu.reset();
		this.setScreen(mainMenu);
		bg.setToDay();
	}

	public void toStoreFrom(ActiveScreen screen, Purchaseable p) {
		toStoreFrom(screen);
		store.switchTo(p);
	}
	
	// go to main store, return to this.
	public void toStoreFrom(ActiveScreen screen) {
		this.marketFromThis = screen;
		this.setScreen(store);
	}

	public void mainToStore() {
		this.setScreen(store);
	}
	
	// this doesn't really do anything, but could be useful later.
	public static void print(Object string) {
		// if debug, print all.
		// if release, don't
		if (DEBUG) {
			System.out.println(string);
		}
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
	public static float getGlobalXFloat(float percentX) {
		return (percentX * width);
	}
	public static int getGlobalY(float percentY) {
		return (int) (percentY * height);
	}
	public static float getGlobalYFloat(float percentY) {
		return (percentY * height);
	}
}
