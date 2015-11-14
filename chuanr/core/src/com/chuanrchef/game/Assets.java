package com.chuanrchef.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Assets {	
	final static float GRILL_ANIMATION_TIME = 1f;
	//	final static int GRILL_ANIMATION_FRAMES = 1;
	final static float CUSTOMER_ANIMATION_TIME = .25f;
	final static int CUSTOMER_ANIMATION_FRAMES = 1;

	public static AssetManager manager; // must be disposed

	public static BitmapFont fontOrder; // must be disposed

	static Texture peppercornLogo; // must be disposed

	static TextureAtlas atlas; // must be disposed after load
	static TextureAtlas uiAtlas; // must be disposed

	static Skin uiSkin;
//	static Skin purchaseableTableSkin;

	static TextureRegion title;
	
	static TextureRegion grillMid;
	static TextureRegion grillLeft;
	static TextureRegion grillRight;
	static TextureRegion grillCoals;
	static Animation grillFire;

	static Animation customerIdle;
	static Animation customerWalking;

	static Animation teenIdle;
	static Animation teenWalkLeft;
	static Animation teenWalkRight;
	static Animation teenWalkDown;
	static Animation teenWalkUp;
	
	static Animation girlIdle;
	static Animation girlWalkLeft;
	static Animation girlWalkRight;
	static Animation girlWalkDown;
	static Animation girlWalkUp;

	static Animation manIdle;
	static Animation manWalkLeft;
	static Animation manWalkRight;
	static Animation manWalkDown;
	static Animation manWalkUp;

	static Animation touristIdle;
	static Animation touristWalkLeft;
	static Animation touristWalkRight;
	static Animation touristWalkDown;
	static Animation touristWalkUp;

	static Animation oldWomanIdle;
	static Animation oldWomanWalkLeft;
	static Animation oldWomanWalkRight;
	static Animation oldWomanWalkDown;
	static Animation oldWomanWalkUp;

	static Animation oldManStingyIdle;
	static Animation oldManStingyWalkLeft;
	static Animation oldManStingyWalkRight;
	static Animation oldManStingyWalkDown;
	static Animation oldManStingyWalkUp;

	static Animation policeIdle;
	static Animation policeWalkRight;
	static Animation policeWalkDown;
	static Animation policeWalkUp;	
	
	static Animation fatAmericanIdle;
	static Animation fatAmericanRight;
	static Animation fatAmericanDown;
	static Animation fatAmericanUp;

	static TextureRegion chuanrChickenRaw;
	static TextureRegion chuanrChickenCooked;
	static TextureRegion chuanrChickenRawSpice;
	static TextureRegion chuanrChickenCookedSpice;
	static TextureRegion chuanrChickenBurnt;
	static TextureRegion chuanrChickenGhost;

	static TextureRegion chuanrBeefRaw;
	static TextureRegion chuanrBeefCooked;
	static TextureRegion chuanrBeefRawSpice;
	static TextureRegion chuanrBeefCookedSpice;
	static TextureRegion chuanrBeefBurnt;
	static TextureRegion chuanrBeefGhost;

	static TextureRegion chuanrLambRaw;
	static TextureRegion chuanrLambCooked;
	static TextureRegion chuanrLambRawSpice;
	static TextureRegion chuanrLambCookedSpice;
	static TextureRegion chuanrLambBurnt;
	static TextureRegion chuanrLambGhost;

	static TextureRegion beefBox;
	static TextureRegion chickenBox;
	static TextureRegion lambBox;
	static TextureRegion beerBox;
	static TextureRegion beefBoxOpen;
	static TextureRegion chickenBoxOpen;
	static TextureRegion lambBoxOpen;
	static TextureRegion beerBoxOpen;
	static TextureRegion spiceBox;

	static TextureRegion beefIcon;
	static TextureRegion chickenIcon;
	static TextureRegion lambIcon;
	static TextureRegion beefSpicyIcon;
	static TextureRegion chickenSpicyIcon;
	static TextureRegion lambSpicyIcon;
	static TextureRegion beerIcon;

	static TextureRegion star;
	static TextureRegion starHalf;

	static TextureRegion face1, face2, face3, face4, face5, faceSick;

	
	// make these textures, maybe that will save some memory?
	static TextureRegion bgVillage;
	static TextureRegion bgOutskirts;
	static TextureRegion bgSuburbs;
	static TextureRegion bgUniversity;
	static TextureRegion bgCBD;

	static TextureRegion cloud;

	static TextureRegion paintBrush;
	static TextureRegion trashIcon;

	static TextureRegionDrawable volOn;
	static TextureRegionDrawable volMute;

	static TextureRegionDrawable facebook;

	static TextureRegion speech;

	static TextureRegion pause;

	static TextureRegion gray;
	static TextureRegion white;

	static FreeTypeFontGenerator gang;
	static FreeTypeFontParameter p;

	static FreeTypeFontGenerator c1;
	static FreeTypeFontGenerator c1p;

	static BitmapFont timefont;
	
	static LabelStyle storeTitle;
	static LabelStyle purchaseTitle;
	static LabelStyle purchaseableTitle;
	
	static LabelStyle descriptionLS;
	static LabelStyle costLS;
	static LabelStyle mainLS;
	static TextButtonStyle backButtonStyle;
	static TextButtonStyle mainButtonStyle;
	static TextButtonStyle startButtonStyle;
	static TextButtonStyle marketButtonStyle;
	static TextButtonStyle facebookButtonStyle;
	static TextButtonStyle unlockButtonStyle;

//	static LabelStyle ls16;
//	static LabelStyle ls20;
//	static LabelStyle ls24;
//	static LabelStyle ls32;
//	static LabelStyle ls48;
//	static LabelStyle ls60;
//	static LabelStyle ls80;
//	static LabelStyle ls100;
//	static LabelStyle ls120;
//	static LabelStyle ls160;

	//	static TextButtonStyle tbs32;
	//	static TextButtonStyle tbs48;
	//	static TextButtonStyle tbs60;
	//	static TextButtonStyle tbs80;

	static NinePatchDrawable roundUp;
	
	// currently allocated fonts, dispose after every screen
	static ArrayList<BitmapFont> fonts;


	static Music music;

	//	static Animation

	// static class containing relevant images, etc
	// should prepare animations and set regions to the appropriate size
	public static void load() {		
		manager = new AssetManager();

		// enqueue everything for loading, instead of just straight loading it. does it asynchronously.
		manager.load("atlas1.atlas", TextureAtlas.class);

		//make sure to load logo first!!
		peppercornLogo = new Texture(Gdx.files.internal("logo.png"));
		
		fonts = new ArrayList<BitmapFont>();

		//		manager.load("uiAtlas.atlas", TextureAtlas.class);

		//		fontOrder = new BitmapFont(Gdx.files.internal("data/gang24.fnt"), false);
		//		fontOrder.setColor(Color.BLACK);

		// Loading jsons while using Kryo causes problem
		//		uiSkin = new Skin(Gdx.files.internal("data/uiskin.json"));
		//		uiSkin = new Skin();


		//		FreeTypeFontGenerator msgothic = new FreeTypeFontGenerator(Gdx.files.internal("data/msgothic.ttc"));
		//		FreeTypeFontParameter params1 = new FreeTypeFontParameter();
		//		params1.size = 70;
		//		params1.characters = "厨师开始一天 1234567890";
		//		chinese70 = msgothic.generateFont(params1);
		//		
		//		FreeTypeFontParameter params2 = new FreeTypeFontParameter();
		//		params2.size = 32;
		//		params2.characters = "¥: 1234567890.";
		//		chinese32 = msgothic.generateFont(params2);
	}

	public static void createUI() {
		uiAtlas = new TextureAtlas(Gdx.files.internal("ui/ui-orange.atlas"));

		uiSkin = new Skin(uiAtlas);
		//		purchaseableTableSkin = new Skin(Gdx.files.internal("ui/ptable.json"));	
//		purchaseableTableSkin = new Skin(new TextureAtlas(Gdx.files.internal("ui/ui-red.atlas")));	


//		final CheckBoxStyle t = new CheckBoxStyle();
//		t.font = china32;
//		t.fontColor =new Color(0, 0, 0, 1f);
//		t.disabledFontColor = new Color(0, 0, 0, 0.4f);
//		t.checkboxOff = uiSkin.getDrawable( "checkbox_off");
//		t.checkboxOn = uiSkin.getDrawable( "checkbox_on");
//		uiSkin.add("default", t);

		// Make these 9 patches yo!
//		Drawable roundDown = uiSkin.getDrawable("button_03");
//		Drawable roundUp = uiSkin.getDrawable("button_04");

//		TextButtonStyle tbs32;
//		TextButtonStyle tbs48;
//		TextButtonStyle tbs60;
//		TextButtonStyle tbs80;
//
//		// set up style for buttons
//		tbs32 = new TextButtonStyle();
//		tbs32.down = roundDown;
//		tbs32.up = roundUp;
//		tbs32.font = Assets.china32o;
//
//		tbs48 = new TextButtonStyle();
//		tbs48.down = roundDown;
//		tbs48.up = roundUp;
//		tbs48.font = Assets.china48o;
//
//		tbs60 = new TextButtonStyle();
//		tbs60.down = roundDown;
//		tbs60.up = roundUp;
//		tbs60.font = Assets.china60;
//		//
//		tbs80 = new TextButtonStyle();
//		tbs80.down = roundDown;
//		tbs80.up = roundUp;
//		tbs80.font = Assets.china80;
//
//		uiSkin.add("small_button", tbs48);
//		uiSkin.add("button_medium", tbs48);
//		uiSkin.add("default", tbs80);

		//		tbs80.fontColor = new Color(1, .8f, 0, 1);
		
	}

	/** loads music and sound */
	public static void loadSound() {
		music = Gdx.audio.newMusic(Gdx.files.internal("sound/music.ogg"));
		music.setLooping(true);
	}
	
	// create a four-frame animation from textures
	public static Animation createAnimationFromTextures(String region1, String region2,
														String region3, String region4, float time) {
		TextureRegion texture1 = getTextureRegion(region1);
		TextureRegion texture2 = getTextureRegion(region2);
		TextureRegion texture3 = getTextureRegion(region3);
		TextureRegion texture4 = getTextureRegion(region4);

		TextureRegion[][] textureArray = new TextureRegion[1][4];
		textureArray[0][0] = texture1;
		textureArray[0][1] = texture2;
		textureArray[0][2] = texture3;
		textureArray[0][3] = texture4;
		
		Animation animation = new Animation(time, textureArray[0]);
		animation.setPlayMode(Animation.PlayMode.LOOP);
		return animation;
	}
	
	// create a two-frame animation from textures
	public static Animation createAnimationFromTextures(String region1, String region2, float time) {
		TextureRegion texture1 = getTextureRegion(region1);
		TextureRegion texture2 = getTextureRegion(region2);
		
		TextureRegion[][] textureArray = new TextureRegion[1][2];
		textureArray[0][0] = texture1;
		textureArray[0][1] = texture2;
		Animation animation = new Animation(time, textureArray[0]);
		animation.setPlayMode(Animation.PlayMode.LOOP);
		return animation;
	}

	public static Animation createAnimation(String region, float time, int columns) {
		TextureRegion walkSheet = getTextureRegion(region);
		TextureRegion[][] textureArray = walkSheet.split(walkSheet.getRegionWidth()/columns, walkSheet.getRegionHeight()/1);
		Animation animation = new Animation(time, textureArray[0]);
		animation.setPlayMode(Animation.PlayMode.LOOP);
		return animation;
	}
	
	public static Animation createAnimation(String region, float time, int row, int rowsColumns) {
		TextureRegion walkSheet = getTextureRegion(region);
		TextureRegion[][] textureArray = walkSheet.split(walkSheet.getRegionWidth()/rowsColumns, walkSheet.getRegionHeight()/rowsColumns);
		System.out.println(textureArray.length + " " + textureArray[0].length);
		Animation animation = new Animation(time, textureArray[row - 1]);
		animation.setPlayMode(Animation.PlayMode.LOOP);
		return animation;
	}
	
	// access a single frame
	public static Animation createAnimation(String region, float time, int row, int rowsColumns, int column) {
		TextureRegion walkSheet = getTextureRegion(region);
		TextureRegion[][] textureArray = walkSheet.split(walkSheet.getRegionWidth()/rowsColumns, walkSheet.getRegionHeight()/rowsColumns);
		Animation animation = new Animation(time, textureArray[row - 1][column-1]);
		animation.setPlayMode(Animation.PlayMode.LOOP);
		return animation;
	}

	public static TextureRegion getTextureRegion(String region) {
		TextureRegion toReturn = atlas.findRegion(region);
		if (toReturn == null) throw new NullPointerException("cant find '" + region +"'");
		return toReturn;
	}

	public static void dispose() {
		music.dispose();
	}
	
	/**
	 *  this automatically sets permanent to false for any temporary screens that call this
	 * @param size
	 * @return
	 */
	public static LabelStyle generateLabelStyle(int size) {
		return generateLabelStyle(size, false);
	}

	public static LabelStyle generateLabelStyle(int size, boolean permanent) {
		p.size = size;
		LabelStyle ls = new LabelStyle();
		ls.font = gang.generateFont(p);
		if (!permanent)
			fonts.add(ls.font);
		return ls;
	}

	// continue loading
	public static void update() {
		manager.update();
	}

	public static boolean loadingComplete() {
		//		System.out.println(manager.getProgress());
		if (manager.getProgress() >= 1) {
			return true;
		}
		return false;
	}

	// get shit out of the asset manager
	public static void finalizeLoading() {
		atlas = manager.get("atlas1.atlas", TextureAtlas.class);
				
		speech = getTextureRegion("speech");

		gray = getTextureRegion("graypixel");
		white = getTextureRegion("whitepixel");

		title = getTextureRegion("kebabking");
		//		start = getTexture("start");
		//		store = getTexture("store");
		//		quit = getTexture("quit");

		beefBox = getTextureRegion("beef_closed");
		lambBox = getTextureRegion("lamb_closed");
		chickenBox = getTextureRegion("chicken_closed");
		beerBox = getTextureRegion("beer_closed");
		beefBoxOpen = getTextureRegion("beef_open");
		lambBoxOpen = getTextureRegion("lamb_open");
		chickenBoxOpen = getTextureRegion("chicken_open");
		beerBoxOpen = getTextureRegion("beer_open");
		spiceBox = getTextureRegion("spicebox");
//		trashBox = getTexture("trashbox");

		chuanrChickenRaw = getTextureRegion("chicken_raw");
		chuanrChickenCooked = getTextureRegion("chicken_cooked");
		chuanrChickenRawSpice = getTextureRegion("chicken_raw_spice");
		chuanrChickenCookedSpice = getTextureRegion("chicken_cooked_spice");
		chuanrChickenBurnt = getTextureRegion("chicken_burnt");
		//		chuanrChickenGhost = getTexture("chicken_raw_ghost");

		chuanrBeefRaw = getTextureRegion("beef_raw");
		chuanrBeefCooked = getTextureRegion("beef_cooked");
		chuanrBeefRawSpice = getTextureRegion("beef_raw_spice");
		chuanrBeefCookedSpice = getTextureRegion("beef_cooked_spice");
		chuanrBeefBurnt = getTextureRegion("beef_burnt");
		//		chuanrBeefGhost = getTexture("beef_raw_ghost");

		chuanrLambRaw = getTextureRegion("lamb_raw");
		chuanrLambCooked = getTextureRegion("lamb_cooked");
		chuanrLambRawSpice = getTextureRegion("lamb_raw_spice");
		chuanrLambCookedSpice = getTextureRegion("lamb_cooked_spice");
		chuanrLambBurnt = getTextureRegion("lamb_burnt");
		//		chuanrLambGhost = getTexture("lamb_raw_ghost");

		grillMid = getTextureRegion("grillcenter4");
		grillLeft = getTextureRegion("grillleft4");
		grillRight = getTextureRegion("grillright4");

		grillCoals = getTextureRegion("coals");
		
		grillFire = createAnimation("fire", GRILL_ANIMATION_TIME, 4);

		teenIdle = createAnimation("teen_down", CUSTOMER_ANIMATION_TIME, 1);
		teenWalkLeft = createAnimation("teen_walk_left", CUSTOMER_ANIMATION_TIME, 2);
		teenWalkRight = createAnimation("teen_walk_right", CUSTOMER_ANIMATION_TIME, 2);
		teenWalkDown = createAnimation("teen_walk_down", CUSTOMER_ANIMATION_TIME, 2);
		teenWalkUp = createAnimation("teen_walk_up", CUSTOMER_ANIMATION_TIME, 2);

		girlIdle = createAnimation("girl", CUSTOMER_ANIMATION_TIME, 1);
		girlWalkRight = createAnimationFromTextures("girlsideright1", 
	  			"girlsideright", CUSTOMER_ANIMATION_TIME);
		girlWalkUp = createAnimationFromTextures("girlbackstepright",
				"girlbackstepleft", CUSTOMER_ANIMATION_TIME);
		girlWalkDown = createAnimationFromTextures("girlstepleft", "girlstepright", CUSTOMER_ANIMATION_TIME);
	
		
//		manIdle = createAnimation("man_down", CUSTOMER_ANIMATION_TIME, 1);
//		manWalkLeft = createAnimation("man_walk_left", CUSTOMER_ANIMATION_TIME, 2);
//		manWalkRight = createAnimation("man_walk_right", CUSTOMER_ANIMATION_TIME, 2);
//		manWalkDown = createAnimation("man_walk_down", CUSTOMER_ANIMATION_TIME, 2);
//		manWalkUp = createAnimation("man_walk_up", CUSTOMER_ANIMATION_TIME, 2);

		manIdle = createAnimation("man", CUSTOMER_ANIMATION_TIME, 1);
		manWalkRight = createAnimationFromTextures("mansideright1", 
	  			"mansideright2", CUSTOMER_ANIMATION_TIME);
		manWalkUp = createAnimationFromTextures("manbackstepright",
				"manbackstepleft", CUSTOMER_ANIMATION_TIME);
		manWalkDown = createAnimationFromTextures("manstepleft", "manstepright", CUSTOMER_ANIMATION_TIME);
		
//		oldWomanIdle = createAnimation("old_woman_down", CUSTOMER_ANIMATION_TIME, 1);
//		oldWomanWalkLeft = createAnimation("old_woman_walk_left", CUSTOMER_ANIMATION_TIME, 2);
//		oldWomanWalkRight = createAnimation("old_woman_walk_right", CUSTOMER_ANIMATION_TIME, 2);
//		oldWomanWalkUp = createAnimation("old_woman_walk_up", CUSTOMER_ANIMATION_TIME, 2);
//		oldWomanWalkDown = createAnimation("old_woman_walk_down", CUSTOMER_ANIMATION_TIME, 2);

		oldWomanIdle = createAnimation("OldWoman", CUSTOMER_ANIMATION_TIME, 2, 3, 1);
		oldWomanWalkRight = createAnimation("OldWoman", CUSTOMER_ANIMATION_TIME, 1, 3);
		oldWomanWalkUp = createAnimation("OldWoman", CUSTOMER_ANIMATION_TIME, 3, 3);
		oldWomanWalkDown = createAnimation("OldWoman", CUSTOMER_ANIMATION_TIME, 2, 3);
	
//		oldManStingyIdle = createAnimation("old_man_stingy_down", CUSTOMER_ANIMATION_TIME, 1);
//		oldManStingyWalkLeft = createAnimation("old_man_stingy_walk_left", CUSTOMER_ANIMATION_TIME, 2);
//		oldManStingyWalkRight = createAnimation("old_man_stingy_walk_right", CUSTOMER_ANIMATION_TIME, 2);
//		oldManStingyWalkUp = createAnimation("old_man_stingy_walk_up", CUSTOMER_ANIMATION_TIME, 2);
//		oldManStingyWalkDown = createAnimation("old_man_stingy_walk_down", CUSTOMER_ANIMATION_TIME, 2);
		
		oldManStingyIdle = createAnimation("OldMan", CUSTOMER_ANIMATION_TIME, 2, 3, 1);
		oldManStingyWalkRight = createAnimation("OldMan", CUSTOMER_ANIMATION_TIME, 1, 3);
		oldManStingyWalkUp = createAnimation("OldMan", CUSTOMER_ANIMATION_TIME, 3, 3);
		oldManStingyWalkDown = createAnimation("OldMan", CUSTOMER_ANIMATION_TIME, 2, 3);
		
		fatAmericanIdle = createAnimation("FatAmerican", CUSTOMER_ANIMATION_TIME, 2, 3, 1);
		fatAmericanRight = createAnimation("FatAmerican", CUSTOMER_ANIMATION_TIME, 1, 3);
		fatAmericanUp = createAnimation("FatAmerican", CUSTOMER_ANIMATION_TIME, 3, 3);
		fatAmericanDown = createAnimation("FatAmerican", CUSTOMER_ANIMATION_TIME, 2, 3);
		
//		policeIdle = createAnimation("police_down", CUSTOMER_ANIMATION_TIME, 1);
//		policeWalkRight = createAnimation("police_walk_right", CUSTOMER_ANIMATION_TIME, 2);
//		policeWalkUp = createAnimation("police_walk_up", CUSTOMER_ANIMATION_TIME, 2);
//		policeWalkDown = createAnimation("police_walk_down", CUSTOMER_ANIMATION_TIME, 2);
		
		policeIdle = createAnimation("policemman", CUSTOMER_ANIMATION_TIME, 1);
		policeWalkRight = createAnimationFromTextures("policemmanwalkingright1", 
										  			"policemmanwalkingright2-1", 
										  			"policemmanwalkingright2",	
										  			"policemmanwalkingright1-1", CUSTOMER_ANIMATION_TIME);
		policeWalkUp = createAnimationFromTextures("policemmanbackstepleft",
													"policemmanbackstepright", CUSTOMER_ANIMATION_TIME);
		policeWalkDown = createAnimationFromTextures("policemmanstepleft",
												"policemmanstepright", CUSTOMER_ANIMATION_TIME);

//		touristIdle = createAnimation("tourist_down", CUSTOMER_ANIMATION_TIME, 1);
//		touristWalkLeft = createAnimation("tourist_walk_left", CUSTOMER_ANIMATION_TIME, 2);
//		touristWalkRight = createAnimation("tourist_walk_right", CUSTOMER_ANIMATION_TIME, 2);
//		touristWalkDown = createAnimation("tourist_walk_down", CUSTOMER_ANIMATION_TIME, 2);
//		touristWalkUp = createAnimation("tourist_walk_up", CUSTOMER_ANIMATION_TIME, 2);

		touristIdle = createAnimation("foreignerfrontleft", CUSTOMER_ANIMATION_TIME, 1);
		touristWalkDown = createAnimationFromTextures("foreignerfrontleft", "foreignerfrontright", CUSTOMER_ANIMATION_TIME);
		touristWalkUp = createAnimationFromTextures("foreignerbackright", "foreignerbackright", CUSTOMER_ANIMATION_TIME);
		touristWalkRight = createAnimationFromTextures("foreignersideright1", "foreignersideright2", CUSTOMER_ANIMATION_TIME);
		touristWalkLeft = createAnimationFromTextures("foreignersideleft1", "foreignersideleft2", CUSTOMER_ANIMATION_TIME);
		
		chickenIcon = getTextureRegion("chicken_icon");
		beefIcon = getTextureRegion("beef_icon");
		lambIcon = getTextureRegion("lamb_icon");
		chickenSpicyIcon = getTextureRegion("chicken_sp_icon");
		beefSpicyIcon = getTextureRegion("beef_sp_icon");
		lambSpicyIcon = getTextureRegion("lamb_sp_icon");
		beerIcon = getTextureRegion("beer_icon");

//		volOn = new TextureRegionDrawable(getTexture("vol_on"));
//		volMute = new TextureRegionDrawable(getTexture("vol_mute"));

//		facebook = new TextureRegionDrawable(getTexture("facebook"));

		star = getTextureRegion("star");
		starHalf = getTextureRegion("starhalf");

		face1 = getTextureRegion("face1");
		face2 = getTextureRegion("face2");
		face3 = getTextureRegion("face3");
		face4 = getTextureRegion("face4");
		face5 = getTextureRegion("face5");
		faceSick = getTextureRegion("face_s");

		paintBrush = getTextureRegion("paintbrush");
		trashIcon = getTextureRegion("trash");

		cloud = getTextureRegion("cloud");

		bgVillage = getTextureRegion("village2");
		bgOutskirts = getTextureRegion("outskirts");
		bgSuburbs = getTextureRegion("suburbs");
		bgUniversity = getTextureRegion("university");
		bgCBD = getTextureRegion("cbd");

		pause = getTextureRegion("pause");

		gang = new FreeTypeFontGenerator(Gdx.files.internal("data/CarterOne.ttf"));
		p = new FreeTypeFontParameter();
		p.characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ .,!@#$%^&*()/1234567890:-'\"";

		p.size = 24;
		fontOrder = gang.generateFont(p);
		
		// don't add this to the list of fonts because it's permanent
//		fonts.add(fontOrder);
		
		fontOrder.setColor(Color.BLACK);

//		china24 = gang.generateFont(p);
//
//		p.size = 32;
//		china32 = gang.generateFont(p);
//
//		p.size = 48;
//		china48 = gang.generateFont(p);
//
//		p.size = 60; 
//		china60 = gang.generateFont(p);
//
//		p.size = 80; 
//		china80 = gang.generateFont(p);
//
//		p.size = 100; 
//		china100 = gang.generateFont(p);
//
//		p.size = 120; 
//		china120 = gang.generateFont(p);
//
//		p.size = 240;
//		china160 = gang.generateFont(p);
//
//
//		String fontClass = "gang";
//
//		//		china24 = new BitmapFont(Gdx.files.internal("data/" + fontClass + "24.fnt"), false);
//		//
//		china32o = new BitmapFont(Gdx.files.internal("data/" + fontClass + "32.fnt"), false);
//		china48o = new BitmapFont(Gdx.files.internal("data/" + fontClass + "48.fnt"), false);
//		china60o = new BitmapFont(Gdx.files.internal("data/" + fontClass + "64.fnt"), false);
//		//		china80o = new BitmapFont(Gdx.files.internal("data/" + fontClass + "80.fnt"), false);
//		china100o = new BitmapFont(Gdx.files.internal("data/" + fontClass + "100.fnt"), false);
//		china120o = new BitmapFont(Gdx.files.internal("data/" + fontClass + "120.fnt"), false);
//
//		china240o = new BitmapFont(Gdx.files.internal("data/" + fontClass + "240.fnt"), false);




//		ls24 = new LabelStyle();
//		ls24.font = china24;
//
//		ls32 = new LabelStyle();
//		ls32.font = china32;
//
//		ls48 = new LabelStyle();
//		ls48.font = china48;
//
//		ls60 = new LabelStyle();
//		ls60.font = china60o;
//
//		ls80 = new LabelStyle();
//		ls80.font = china100o;
//
//		ls100 = new LabelStyle();
//		ls100.font = china100;
//
//		ls120 = new LabelStyle();
//		ls120.font = china120;
//
//		ls160 = new LabelStyle();
//		ls160.font = china240o;

		createUI();

		loadSound();
	}

	
	// ui methods for (hopefully) simplicity
	public static Drawable getStoreBackground() {
		return uiSkin.getDrawable("textbox_01");
	}
	public static Drawable getPurchaseableBackground() {
		return uiSkin.getDrawable("button_01");
	}
	public static Drawable getPurchaseTypeBackground() {
		return uiSkin.getDrawable("textbox_02");
	}
	public static Drawable getArrowLeftUp() {
		return uiSkin.getDrawable("knob_04");
	}
	public static Drawable getArrowLeftDown() {
		return uiSkin.getDrawable("knob_04");
	}
	public static Drawable getArrowRightUp() {
		return uiSkin.getDrawable("knob_02");
	}
	public static Drawable getArrowRightDown() {
		return uiSkin.getDrawable("knob_02");
	}
	
	public static LabelStyle getStoreTitleLS() {
		if (storeTitle != null) return storeTitle;
		
		LabelStyle titleStyle = new LabelStyle();
		titleStyle.background = uiSkin.getDrawable("button_05");
		titleStyle.font = Assets.generateUIFont(60, true);
		storeTitle = titleStyle;
		return titleStyle;
	}
	public static LabelStyle getMarketLS() {
//		LabelStyle titleStyle = new LabelStyle();
//		titleStyle.background = uiSkin.getDrawable("textbox_01");
//		titleStyle.font = Assets.getUIFont(50);
//		return titleStyle;
		return getStoreTitleLS();
	}
	public static LabelStyle getPurchaseTypeTitleLS() {
		if (purchaseTitle != null) return purchaseTitle;
		LabelStyle titleStyle = new LabelStyle();
		titleStyle.background = uiSkin.getDrawable("button_05");
		titleStyle.font = Assets.generateUIFont(40, true);
		purchaseTitle = titleStyle;
		return titleStyle;
	}
	public static LabelStyle getPurchaseableTitleLS() {
		if (purchaseableTitle != null) return purchaseableTitle;
		
		LabelStyle titleStyle = new LabelStyle();
//		titleStyle.background = uiSkin.getDrawable("textbox_01");
		titleStyle.font = Assets.generateUIFont(26, true);
		purchaseableTitle = titleStyle;
		return titleStyle;
//		return ls32;
	}
	public static LabelStyle getDescriptionLS() {
		if (descriptionLS != null) return descriptionLS;
		
		descriptionLS = generateLabelStyle(18, true);
		return descriptionLS;
	}
	public static LabelStyle getCostLS() {
		if (costLS != null) return costLS;
		costLS = generateLabelStyle(24, true);
		return costLS;
	}
	public static LabelStyle getMainLS() {
		if (mainLS != null) return mainLS;
		mainLS = generateLabelStyle(40, true);
		return mainLS;
	}
	public static TextButtonStyle getMainButtonStyle() {
		if (mainButtonStyle != null) return mainButtonStyle;
		
		TextButtonStyle tbs = new TextButtonStyle();
		tbs.down = uiSkin.getDrawable("button_01");
		tbs.up = uiSkin.getDrawable("button_03");
		tbs.font = Assets.generateUIFont(48, true);
//		tbs.font = Assets.china48o;
		mainButtonStyle = tbs;
		return tbs;
	}
	public static TextButtonStyle getBackButtonStyle() {
		if (backButtonStyle != null) return backButtonStyle;
		
		TextButtonStyle tbs = new TextButtonStyle();
		tbs.down = uiSkin.getDrawable("button_01");
		tbs.up = uiSkin.getDrawable("button_02");
		tbs.font = Assets.generateUIFont(32, true);
//		tbs.font = Assets.china32;
		backButtonStyle = tbs;
		return tbs;
	}
	public static TextButtonStyle getStartButtonStyle() {
		if (startButtonStyle != null) return startButtonStyle;

		TextButtonStyle tbs = new TextButtonStyle();
		tbs.down = uiSkin.getDrawable("button_01");
		tbs.up = uiSkin.getDrawable("button_02");
		tbs.font = Assets.generateUIFont(ChuanrC.getGlobalX(36.0f / 480), true);
//		tbs.font = Assets.china32;
		startButtonStyle = tbs;
		return tbs;
	}
	public static TextButtonStyle getMarketButtonStyle() {
		if (marketButtonStyle != null) return marketButtonStyle;

		TextButtonStyle tbs = new TextButtonStyle();
		tbs.down = uiSkin.getDrawable("button_01");
		tbs.up = uiSkin.getDrawable("button_02");
		tbs.font = Assets.generateUIFont(ChuanrC.getGlobalX(36.0f / 480), true);
//		tbs.font = Assets.china32;
		marketButtonStyle = tbs;
		return tbs;
	}
	public static TextButtonStyle getFacebookButtonStyle() {
		if (facebookButtonStyle != null) return facebookButtonStyle;

		TextButtonStyle tbs = new TextButtonStyle();
		tbs.down = uiSkin.getDrawable("button_01");
		tbs.up = uiSkin.getDrawable("button_02");
		tbs.font = Assets.generateUIFont(ChuanrC.getGlobalX(48.0f / 480), true);
//		tbs.font = Assets.china32;
		facebookButtonStyle = tbs;
		return tbs;
	}
	public static TextButtonStyle getUnlockButtonStyle() {
		if (unlockButtonStyle != null) return unlockButtonStyle;
		TextButtonStyle tbs = new TextButtonStyle();
//		tbs.font = Assets.china32;
		tbs.font = Assets.generateUIFont(32, true);
		tbs.down = Assets.uiSkin.getDrawable("button_01");
		tbs.up = Assets.uiSkin.getDrawable("button_02");
		unlockButtonStyle = tbs;
		return tbs;
	}
	public static ScrollPaneStyle getSPS() {
		ScrollPaneStyle sps = new ScrollPaneStyle();
//		sps.vScroll = uiSkin.getDrawable("scroll_back_ver");
//		sps.hScroll = uiSkin.getDrawable("scroll_back_hor");
//		sps.vScrollKnob = uiSkin.getDrawable("slider_back_ver");
		sps.background = uiSkin.getDrawable("textbox_01");
		return sps;
	}
	
	public static BitmapFont generateUIFont(int size, boolean permanent) {
		// these are leaking hard!
		p.size = size;
		System.out.println(size + " size font");
		BitmapFont toReturn = gang.generateFont(p);
		if (!permanent)
			fonts.add(toReturn);
		return toReturn;
	}

	public static NinePatchDrawable getDefaultIcon() {
		return (NinePatchDrawable) uiSkin.getDrawable("textbox_02");
	}
	
	public static TextureRegion getFoodIcon() {
		return getTextureRegion("icon_food");
	}
	public static TextureRegion getGrillIcon() {
		return getTextureRegion("icon_grill");
	}
	public static TextureRegion getMapIcon() {
		return getTextureRegion("icon_map");
	}
	public static TextureRegion getAdsIcon() {
		return getTextureRegion("icon_ads");
	}
	public static TextureRegion getCoinsIcon() {
		return getTextureRegion("icon_coin");
	}
	public static TextureRegion getCoin() {
		return getTextureRegion("coin");
	}
	public static TextureRegion getStar() {
		return getTextureRegion("star");
	}
	public static TextureRegion getHalfStar() {
		return getTextureRegion("starhalf");
	}
	public static TextureRegion getGrayStar() {
		return getTextureRegion("stargray");
	}
	public static Drawable getTopBarBG() {
		return uiSkin.getDrawable("button_06");
	}
	public static ButtonStyle getPauseButtonStyle() {
		ButtonStyle bs = new ButtonStyle();
		bs.up = uiSkin.getDrawable("icon_pause");
		bs.down = uiSkin.getDrawable("icon_pause");
		return bs;
	}
	public static ButtonStyle getButtonStyleMuted() {
		ButtonStyle bs = new ButtonStyle();
		bs.up = uiSkin.getDrawable("icon_sound_off");
		bs.down = uiSkin.getDrawable("icon_sound_off");
		return bs;
	}
	public static ButtonStyle getButtonStyleUnmuted() {
		ButtonStyle bs = new ButtonStyle();
		bs.up = uiSkin.getDrawable("icon_sound_on");
		bs.down = uiSkin.getDrawable("icon_sound_on");
		return bs;
	}
	public static ButtonStyle getCoinPlusStyle() {
		ButtonStyle bs = new ButtonStyle();
		bs.up = uiSkin.getDrawable("icon_check");
		bs.down = uiSkin.getDrawable("icon_check");
		return bs;
	}
	public static TextureRegion getCoinsBG() {
		return getTextureRegion("coins_bg");
	}
	public static TextureRegion getCashBG() {
		return getTextureRegion("cash_bg");
	}
	
	public static void deleteTempResources() {
		for (BitmapFont bf : fonts) {
			System.out.println("deleting font: " + bf.getLineHeight());
			bf.dispose();
		}
		fonts.clear();
	}
}


