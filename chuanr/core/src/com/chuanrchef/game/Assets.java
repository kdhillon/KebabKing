package com.chuanrchef.game;

import java.util.ArrayList;
import java.util.HashMap;

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
	final static float CUSTOMER_ANIMATION_TIME = .15f;
	final static int CUSTOMER_ANIMATION_FRAMES = 1;

	public static AssetManager manager; // must be disposed

	public static BitmapFont fontOrder; // must be disposed
//	
//	public static BitmapFont topBarFont; // must be disposed

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

	static public class CustomerTextures {
		Animation idle;
		Animation right;
		Animation down;
		Animation up;
	}
	
//	static HashMap<Customer.CustomerType, CustomerTextures> customerMap;
	
	static public class KebabTextures {
		TextureRegion raw;
		TextureRegion cooked;
		TextureRegion rawSp;
		TextureRegion cookedSp;
		TextureRegion burnt;
	}
	
	
	static HashMap<Meat.Type, KebabTextures> meatMap;

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

	static TextureRegion face1, face2, face3, face4, face5, faceSick;

	static TextureRegion bgVillage;
	static TextureRegion bgOutskirts;
	static TextureRegion bgSuburbs;
	static TextureRegion bgUniversity;
	static TextureRegion bgCBD;

	static TextureRegion cloud1;
	static TextureRegion cloud2;
	static TextureRegion sun;
	static TextureRegion skyStar;

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
	
	static FreeTypeFontGenerator worksans;

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
		Animation animation = new Animation(time, textureArray[row - 1]);
		animation.setPlayMode(Animation.PlayMode.LOOP);
		return animation;
	}
	
	public static Animation createAnimationWithRepeatFirst(String region, float time, int row, int rowsColumns) {
		TextureRegion walkSheet = getTextureRegion(region);
		TextureRegion[][] textureArray = walkSheet.split(walkSheet.getRegionWidth()/rowsColumns, walkSheet.getRegionHeight()/rowsColumns);
		TextureRegion[] framesToUse = new TextureRegion[4];
	
		framesToUse[0] = textureArray[row-1][0];
		framesToUse[1] = textureArray[row-1][1];
		framesToUse[2] = textureArray[row-1][0];
		framesToUse[3] = textureArray[row-1][2];
		
		Animation animation = new Animation(time, framesToUse);
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

	public static LabelStyle generateLabelStyleUI(int size, boolean permanent) {
		p.size = size;
		LabelStyle ls = new LabelStyle();
		ls.font = worksans.generateFont(p);
		if (!permanent)
			fonts.add(ls.font);
		return ls;
	}

	// continue loading
	public static void update() {
		manager.update();
	}

	public static boolean loadingComplete() {
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

//		beefBox = getTextureRegion("beef_closed");
//		lambBox = getTextureRegion("lamb_closed");
//		chickenBox = getTextureRegion("chicken_closed");
//		beerBox = getTextureRegion("beer_closed");
//		beefBoxOpen = getTextureRegion("beef_open");
//		lambBoxOpen = getTextureRegion("lamb_open");
//		chickenBoxOpen = getTextureRegion("chicken_open");
//		beerBoxOpen = getTextureRegion("beer_open");
		
		spiceBox = getTextureRegion("Grill-05");
		
		beefBox = getTextureRegion("Cooler-21");
		lambBox = getTextureRegion("Cooler-23");
		chickenBox = getTextureRegion("Cooler-22");
		beerBox = getTextureRegion("Cooler-24");
		beefBoxOpen = getTextureRegion("Cooler_open-29");
		lambBoxOpen = getTextureRegion("Cooler_open-31");
		chickenBoxOpen = getTextureRegion("Cooler_open-30");
		beerBoxOpen = getTextureRegion("Cooler_open-32");

		//		trashBox = getTexture("trashbox");

		meatMap = new HashMap<Meat.Type, KebabTextures>();
		
		meatMap.put(Meat.Type.CHICKEN, generateKebabTextures("ChickenKebab"));
		meatMap.put(Meat.Type.BEEF,  generateKebabTextures("BeefKebab"));
		meatMap.put(Meat.Type.LAMB, generateKebabTextures("LambKebab"));

		grillMid = getTextureRegion("Grill-03");
		grillLeft = getTextureRegion("Grill-02");
		grillRight = getTextureRegion("Grill-04");

		grillCoals = getTextureRegion("coals");
		
		grillFire = createAnimation("fire", GRILL_ANIMATION_TIME, 4);
		
		chickenIcon = getTextureRegion("chicken_icon");
		beefIcon = getTextureRegion("beef_icon");
		lambIcon = getTextureRegion("lamb_icon");
		chickenSpicyIcon = getTextureRegion("chicken_sp_icon");
		beefSpicyIcon = getTextureRegion("beef_sp_icon");
		lambSpicyIcon = getTextureRegion("lamb_sp_icon");
		beerIcon = getTextureRegion("beer_icon");

		face1 = getTextureRegion("face1");
		face2 = getTextureRegion("face2");
		face3 = getTextureRegion("face3");
		face4 = getTextureRegion("face4");
		face5 = getTextureRegion("face5");
		faceSick = getTextureRegion("face_s");

		paintBrush = getTextureRegion("paintbrush");
		trashIcon = getTextureRegion("trash");

		cloud1 = getTextureRegion("SkyElement-02");
		cloud2 = getTextureRegion("SkyElement-03");
		sun = getTextureRegion("SkyElement-04");
		skyStar = getTextureRegion("SkyElement-05");

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
		// permanent font
		fontOrder = gang.generateFont(p);
		
		worksans = new FreeTypeFontGenerator(Gdx.files.internal("data/WorkSans-SemiBold.otf"));
		p.size = 20;
		p.characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ .,!@#$%^&*()/1234567890:-'\"";

		// permanent font
//		topBarFont = worksans.generateFont(p);
		
		// don't add this to the list of fonts because it's permanent
//		fonts.add(fontOrder);
		
		fontOrder.setColor(Color.BLACK);

		createUI();

		loadSound();
	}
	
	public static CustomerTextures generateCustomerTextures(String prefix, float speed) {
		CustomerTextures ct = new CustomerTextures();
		float time = 1/speed * CUSTOMER_ANIMATION_TIME;
		ct.idle = createAnimation(prefix, time, 2, 3, 1);
		ct.right = createAnimationWithRepeatFirst(prefix, time, 1, 3);
		ct.up = createAnimationWithRepeatFirst(prefix, time, 3, 3);
		ct.down = createAnimationWithRepeatFirst(prefix, time, 2, 3);
		if (ct.idle == null || ct.right == null || ct.up == null || ct.down == null)
			throw new java.lang.NullPointerException();
		return ct;
	}
	
	public static KebabTextures generateKebabTextures(String prefix) {
		KebabTextures kt = new KebabTextures();
		kt.raw = getTextureRegion(prefix + "_raw");
		kt.cooked = getTextureRegion(prefix + "_cooked");
		kt.rawSp = getTextureRegion(prefix + "_rawSp");
		kt.cookedSp = getTextureRegion(prefix + "_cookedSp");
		kt.burnt = getTextureRegion(prefix + "_burnt");
		return kt;
	}
	
	public static TextureRegion getStickTexture() {
		return getTextureRegion("stick_brown");
	}
	
	public static TextureRegion getMeatTexture(Meat meat) {
		KebabTextures kt = meatMap.get(meat.type);
		switch (meat.state) {
		case RAW:
			if (!meat.spiced) return kt.raw;
			else return kt.rawSp;
		case COOKED:
			if (!meat.spiced) return kt.cooked;
			else return kt.cookedSp;
		case BURNT:
			return kt.burnt;
		}
		return null;
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
		return getTextureRegion("Top-Bar-Element-06");
	}
	public static TextureRegion getHalfStar() {
		return getTextureRegion("Top-Bar-Element-08");
	}
	public static TextureRegion getGrayStar() {
		return getTextureRegion("Top-Bar-Element-07");
	}
	public static Drawable getTopBarBG() {
		return uiSkin.getDrawable("button_06");
	}
	public static ButtonStyle getStyleFromRegion(String name) {
		ButtonStyle bs = new ButtonStyle();
		bs.up = new TextureRegionDrawable(getTextureRegion(name));
		bs.down = new TextureRegionDrawable(getTextureRegion(name));
		return bs;
	}
	public static ButtonStyle getPauseButtonStyle() {
		return getStyleFromRegion("TopBarElement02");
	}
	public static ButtonStyle getButtonStyleMuted() {
		return getStyleFromRegion("TopBarElement01");
	}
	public static ButtonStyle getButtonStyleUnmuted() {
		return getStyleFromRegion("TopBarElement01");
	}
	public static ButtonStyle getCoinPlusStyle() {
		return getStyleFromRegion("icon_check");
	}
	public static TextureRegion getReputationBG() {
		return getTextureRegion("Top-Bar-Element-09");
	}
	public static TextureRegion getCoinsBG() {
		return getTextureRegion("Top-Bar-Element-05");
	}
	public static TextureRegion getCashBG() {
		return getTextureRegion("Top-Bar-Element-04");
	}
	
	public static void deleteTempResources() {
		for (BitmapFont bf : fonts) {
			System.out.println("deleting font: " + bf.getLineHeight());
			bf.dispose();
		}
		fonts.clear();
	}
}


