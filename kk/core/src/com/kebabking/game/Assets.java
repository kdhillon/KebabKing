package com.kebabking.game;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
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
import com.kebabking.game.StoreScreen.TableType;

public class Assets {	
	final static float GRILL_ANIMATION_TIME = 1f;
	//	final static int GRILL_ANIMATION_FRAMES = 1;
	final static float CUSTOMER_ANIMATION_TIME = .15f;
	final static int CUSTOMER_ANIMATION_FRAMES = 1;
	
	final static int GREEN_9PATCH_OFFSET_X = 17;
	final static int GREEN_9PATCH_OFFSET_X_2 = GREEN_9PATCH_OFFSET_X + 2;
	final static int GREEN_9PATCH_OFFSET_Y = 17;
	final static int GREEN_9PATCH_OFFSET_Y_2 = GREEN_9PATCH_OFFSET_X + 4;
	
	final static Color RED = new Color(211/256.0f, 90/256.0f, 68/256.0f, 1f);
	
	final static String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ .,!?@#$%^&*()/1234567890:;-'\">+=_[]{}<";
	
	public static AssetManager manager; // must be disposed
//	
//	public static BitmapFont topBarFont; // must be disposed

	static Texture peppercornLogo; // must be disposed
	static Texture kebabMain;
	
	static TextureAtlas atlas; // must be disposed after load
	static TextureAtlas uiAtlas; // must be disposed

	static Skin uiSkin;
//	static Skin purchaseableTableSkin;

	static TextureRegion title;
	
	static TextureRegion grillMid;
	static TextureRegion grillLeft;
	static TextureRegion grillRight;
	
//	static TextureRegion grillBgMid;
//	static TextureRegion grillBgLeft;
//	static TextureRegion grillBgRight;
	
//	static TextureRegion grillCoals;
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
//	static TextureRegion beefSpicyIcon;
//	static TextureRegion chickenSpicyIcon;
//	static TextureRegion lambSpicyIcon;
	static TextureRegion beerIcon;

	static TextureRegion face1, face2, face3, face4, face5, faceSick;

//	static TextureRegion bgVillage;
//	static TextureRegion bgOutskirts;
//	static TextureRegion bgSuburbs;
//	static TextureRegion bgUniversity;
//	static TextureRegion bgCBD;

	static TextureRegion cloud1;
	static TextureRegion cloud2;
	static TextureRegion sun;
	static TextureRegion skyStar;

	static TextureRegion paintBrush;
	static TextureRegion paintBrushSide;
	static TextureRegion trashIcon;

	static TextureRegionDrawable volOn;
	static TextureRegionDrawable volMute;

	static TextureRegionDrawable facebook;

	static TextureRegion speech;

//	static TextureRegion pause;
	
	static TextureRegion purchaseableCheck;

	static TextureRegion gray;
	static TextureRegion white;
	static TextureRegion whiteAlpha;
	static TextureRegion grayAlpha;
	static TextureRegion grayAlphaLight;

	static FreeTypeFontParameter p;
	static FreeTypeFontGenerator worksans;
	static FreeTypeFontGenerator worksansLight;
	static FreeTypeFontGenerator worksansHeavy;
	static FreeTypeFontGenerator china;

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
	static TextButtonStyle purchaseTypeButtonStyle;
	static TextButtonStyle facebookButtonStyle;
	static TextButtonStyle unlockButtonStyle;
	
	static TextureRegion marketShelf;
	static TextureRegion marketTitle;
	static TextureRegion marketLock;
	static TextureRegion marketJade;
	static TextureRegion purchaseableJade;
	static TextureRegion marketGreen;
	static TextureRegion marketDarkGreen;
	static TextureRegion questionMark;
	static TextureRegion jadeBox;
	
	static TextureRegion red;
	static TextureRegion yellow;

	static NinePatchDrawable roundUp;
	
	// currently allocated fonts, dispose after every screen
	static ArrayList<BitmapFont> fonts;
	
	static Music music;
	
//	static NinePatch green9Patch;
	static NinePatch green9PatchSmall;
//	static NinePatch gray9Patch;
	static NinePatch gray9PatchSmall;
	static NinePatch gray9PatchSmallFilled;
	static NinePatch red9PatchSmall;
	
	static HashMap<String, LabelStyle> styles;

	// This loads peppercorn screen and preps stuff for second loader
	public static void preLoad() {
		
//TODO		
	}
	
	
	// static class containing relevant images, etc
	// should prepare animations and set regions to the appropriate size
	public static void load() {		
		manager = new AssetManager();

		// enqueue everything for loading, instead of just straight loading it. does it asynchronously.
		manager.load("atlas1.atlas", TextureAtlas.class);

		//make sure to load logo first!!
		kebabMain = new Texture(Gdx.files.internal("textures/screens/Main-02.png"));
		peppercornLogo = new Texture(Gdx.files.internal("logo.png"));

		p = new FreeTypeFontParameter();
		p.characters = chars;
		
		worksans = new FreeTypeFontGenerator(Gdx.files.internal("data/WorkSans-Medium.otf"));
		worksansHeavy = new FreeTypeFontGenerator(Gdx.files.internal("data/WorkSans-SemiBold.otf"));
		worksansLight = new FreeTypeFontGenerator(Gdx.files.internal("data/WorkSans-Regular.otf"));
		china = new FreeTypeFontGenerator(Gdx.files.internal("data/CHINA.TTF"));

		styles = new HashMap<String, LabelStyle>();
		fonts = new ArrayList<BitmapFont>();
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
	
	public static Animation createAnimation(String region, float time, int columns, boolean oneTwoThreeTwo) {
		if (oneTwoThreeTwo) {
			TextureRegion walkSheet = getTextureRegion(region);
			TextureRegion[][] textureArray = walkSheet.split(walkSheet.getRegionWidth()/columns, walkSheet.getRegionHeight()/1);
			Animation animation = new Animation(time, textureArray[0]);
			animation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
			return animation;		
		}
		else return createAnimation(region, time, columns);
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
//	public static LabelStyle generateLabelStyle(int size) {
//		return generateLabelStyle(size, false);
//	}

	public static LabelStyle generateLabelStyleUI(int size) {
		return generateLabelStyle(worksans, MainStoreScreen.FONT_COLOR, size);
	}
	
	public static LabelStyle generateLabelStyleUIGray(int size) {
		return generateLabelStyle(worksans, DrawUI.REPUTATION_FONT_COLOR, size);		
	}
	public static LabelStyle generateLabelStyleUIRed(int size) {
		return generateLabelStyle(worksans, SummaryScreen.RED, size);		
	}
	public static LabelStyle generateLabelStyleUIWhite(int size) {
		return generateLabelStyle(worksans, Color.WHITE, size);		
	}
//	public static LabelStyle generateLabelStyleUIDark(int size, boolean permanent) {
//		p.size = size;
//		LabelStyle ls = new LabelStyle();
//		ls.font = worksans.generateFont(p);
//		ls.fontColor = FONT_COLOR;
//		if (!permanent)
//			fonts.add(ls.font);
//		return ls;
//	}
	
	public static LabelStyle generateLabelStyleUILight(int size) {
		return generateLabelStyle(worksansLight, MainStoreScreen.FONT_COLOR, size);
	}
	
	public static LabelStyle generateLabelStyleUIHeavy(int size) {
		return generateLabelStyle(worksansHeavy, MainStoreScreen.FONT_COLOR, size);
	}
	
	public static LabelStyle generateLabelStyleUIHeavyWhite(int size) {
		return generateLabelStyle(worksansHeavy, Color.WHITE, size);
	}
	
	public static LabelStyle generateLabelStyleUIHeavyGreen(int size) {
		return generateLabelStyle(worksansHeavy, MainStoreScreen.FONT_COLOR_GREEN, size);
	}
	public static LabelStyle generateLabelStyleUIChina(int size) {
		return generateLabelStyle(china, Color.BLACK, size);
	}	
	public static LabelStyle generateLabelStyleUIChinaWhite(int size) {
		return generateLabelStyle(china, Color.WHITE, size);
	}
	
	public static LabelStyle generateLabelStyleUIChinaRed(int size) {
		return generateLabelStyle(china, RED, size);
	}	
	
	public static LabelStyle generateLabelStyle(FreeTypeFontGenerator gen, Color color, int size) {
		String name = "" + gen.hashCode() + size + color.hashCode();
		if (styles.containsKey(name)) return styles.get(name);
		
		p.size = getFontSize(size);
		LabelStyle ls = new LabelStyle();
		ls.font = gen.generateFont(p);
		ls.fontColor = color;

		styles.put(name, ls);
		System.out.println("label styles: " + styles.size());
		return ls;
	}
	
	// continue loading
	public static void update() {
		manager.update();
	}

	public static float getLoadProgress() {
		return manager.getProgress();
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
				
		speech = getTextureRegion("customers/Play_speechbubble_element-38");

		gray = getTextureRegion("graypixel");
		white = getTextureRegion("whitepixel");
		whiteAlpha = getTextureRegion("white_alpha");
		grayAlpha = getTextureRegion("gray_alpha");
		grayAlphaLight = getTextureRegion("gray_alpha_light");

		title = getTextureRegion("screens/Main-02");
		//		start = getTexture("start");
		//		store = getTexture("store");
		//		quit = getTexture("quit");
		
		spiceBox = getTextureRegion("grill/grill-05");
		
		beefBox = getTextureRegion("grill/Cooler-21");
		lambBox = getTextureRegion("grill/Cooler-23");
		chickenBox = getTextureRegion("grill/Cooler-22");
		beerBox = getTextureRegion("grill/Cooler-24");
		beefBoxOpen = getTextureRegion("grill/Cooler_open-29");
		lambBoxOpen = getTextureRegion("grill/Cooler_open-31");
		chickenBoxOpen = getTextureRegion("grill/Cooler_open-30");
		beerBoxOpen = getTextureRegion("grill/Cooler_open-32");

		//		trashBox = getTexture("trashbox");

		meatMap = new HashMap<Meat.Type, KebabTextures>();
		
		meatMap.put(Meat.Type.CHICKEN, generateKebabTextures("kebabs/ChickenKebab"));
		meatMap.put(Meat.Type.BEEF,  generateKebabTextures("kebabs/BeefKebab"));
		meatMap.put(Meat.Type.LAMB, generateKebabTextures("kebabs/LambKebab"));

		grillMid = getTextureRegion("grill/grill-03");
		grillLeft = getTextureRegion("grill/grill-02");
		grillRight = getTextureRegion("grill/grill-04");

//		grillBgMid = getTextureRegion("grill/mid_bg");
//		grillBgLeft = getTextureRegion("grill/left_bg");
//		grillBgRight = getTextureRegion("grill/right_bg");
//		grillCoals = getTextureRegion("coals");
//		
		grillFire = createAnimation("grill/fire", GRILL_ANIMATION_TIME, 3, true);
		
		beerIcon = getTextureRegion("customers/beer_icon");
		chickenIcon = getTextureRegion("customers/chicken_icon");
		beefIcon = getTextureRegion("customers/beef_icon");
		lambIcon = getTextureRegion("customers/lamb_icon");
//		chickenSpicyIcon = getTextureRegion("chicken_sp_icon");
//		beefSpicyIcon = getTextureRegion("beef_sp_icon");
//		lambSpicyIcon = getTextureRegion("lamb_sp_icon");

		face1 = getTextureRegion("customers/face1");
		face2 = getTextureRegion("customers/face2");
		face3 = getTextureRegion("customers/face3");
		face4 = getTextureRegion("customers/face4");
		face5 = getTextureRegion("customers/face5");
		faceSick = getTextureRegion("customers/face_s");

		paintBrush = getTextureRegion("grill/brush-48");
		paintBrushSide = getTextureRegion("grill/brush-50");

		trashIcon = getTextureRegion("trash");

		cloud1 = getTextureRegion("background/SkyElement-02");
		cloud2 = getTextureRegion("background/SkyElement-03");
		sun = getTextureRegion("background/SkyElement-04");
		skyStar = getTextureRegion("background/SkyElement-05");

//		bgVillage = getTextureRegion("background/village2");
//		bgOutskirts = getTextureRegion("background/outskirts");
//		bgSuburbs = getTextureRegion("background/suburbs");
//		bgUniversity = getTextureRegion("background/university");
//		bgCBD = getTextureRegion("background/cbd");

//		pause = getTextureRegion("pause");

		
		marketShelf = getTextureRegion("market/Market_menu_element-02");
		marketTitle = getTextureRegion("market/Market_menu_element-08");
		marketGreen = getTextureRegion("market/green");
		marketDarkGreen = getTextureRegion("market/darkGreen");
		marketJade = getTextureRegion("market/jade");
		marketLock = getTextureRegion("market/Market_subMenus__template_element-02");
		purchaseableCheck = getTextureRegion("market/Market_subMenus__template_element-05");
		purchaseableJade = getTextureRegion("market/Market_subMenus__template_element-04");
		questionMark = getTextureRegion("market/question");
		jadeBox = getTextureRegion("market/Market_menu_element-07");

		red = getTextureRegion("screens/red");
		yellow = getTextureRegion("topbar/yellow");
		
//		green9Patch = new NinePatch(getTextureRegion("market/green9patch"), GREEN_9PATCH_OFFSET_X, GREEN_9PATCH_OFFSET_X_2, GREEN_9PATCH_OFFSET_Y, GREEN_9PATCH_OFFSET_Y_2);
		green9PatchSmall = new NinePatch(getTextureRegion("market/green9patchSmall"), GREEN_9PATCH_OFFSET_X/2, GREEN_9PATCH_OFFSET_X_2/2, GREEN_9PATCH_OFFSET_Y/2, GREEN_9PATCH_OFFSET_Y_2/2);
//		gray9Patch = new NinePatch(getTextureRegion("market/gray9patch"), GREEN_9PATCH_OFFSET_X, GREEN_9PATCH_OFFSET_X_2, GREEN_9PATCH_OFFSET_Y, GREEN_9PATCH_OFFSET_Y_2);
		gray9PatchSmall = new NinePatch(getTextureRegion("market/gray9patchSmall"), GREEN_9PATCH_OFFSET_X/2, GREEN_9PATCH_OFFSET_X_2/2, GREEN_9PATCH_OFFSET_Y/2, GREEN_9PATCH_OFFSET_Y_2/2);
		gray9PatchSmallFilled = new NinePatch(getTextureRegion("market/gray9patchSmallFilled"), GREEN_9PATCH_OFFSET_X/2, GREEN_9PATCH_OFFSET_X_2/2, GREEN_9PATCH_OFFSET_Y/2, GREEN_9PATCH_OFFSET_Y_2/2);
		red9PatchSmall = new NinePatch(getTextureRegion("market/red9patchSmall"), GREEN_9PATCH_OFFSET_X/2, GREEN_9PATCH_OFFSET_X_2/2, GREEN_9PATCH_OFFSET_Y/2, GREEN_9PATCH_OFFSET_Y_2/2);
	
		createUI();

		loadSound();
	}
	
	public static CustomerTextures generateCustomerTextures(String prefix, float speed) {
		prefix = "customers/" + prefix;
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
		return getTextureRegion("kebabs/Kebab_Stick-28");
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
//	public static LabelStyle getDescriptionLS() {
//		if (descriptionLS != null) return descriptionLS;
//		
//		descriptionLS = generateLabelStyleGang(18);
//		return descriptionLS;
//	}
//	public static LabelStyle getCostLS() {
//		if (costLS != null) return costLS;
//		costLS = generateLabelStyleGang(24);
//		return costLS;
//	}
//	public static LabelStyle getMainLS() {
//		if (mainLS != null) return mainLS;
//		mainLS = generateLabelStyleGang(40);
//		return mainLS;
//	}
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
	public static TextButtonStyle getBackButtonStyle(int fontSize) {
//		if (backButtonStyle != null) return backButtonStyle;
//		
//		TextButtonStyle tbs = new TextButtonStyle();
//		tbs.down = uiSkin.getDrawable("button_01");
//		tbs.up = uiSkin.getDrawable("button_02");
//		tbs.font = Assets.generateUIFont(32, true);
////		tbs.font = Assets.china32;
//		backButtonStyle = tbs;
//		return tbs;
		return getTextStyleFromRegion("market/back_button", fontSize);
	}
	
	public static TextButtonStyle getStartButtonStyle() {
		if (startButtonStyle != null) return startButtonStyle;

		TextButtonStyle tbs = new TextButtonStyle();
		tbs.down = uiSkin.getDrawable("button_01");
		tbs.up = uiSkin.getDrawable("button_02");
		tbs.font = Assets.generateUIFont(36, true);
//		tbs.font = Assets.china32;
		startButtonStyle = tbs;
		return tbs;
	}
	public static TextButtonStyle getMarketButtonStyle() {
		return getStartButtonStyle();
	}
	
	public static ButtonStyle getSpecificMarketButtonStyle(TableType type) {
//		if (marketButtonStyle != null) return marketButtonStyle;
//
//		TextButtonStyle tbs = new TextButtonStyle();
//		tbs.down = uiSkin.getDrawable("button_01");
//		tbs.up = uiSkin.getDrawable("button_02");
//		tbs.font = Assets.generateUIFont(ChuanrC.getGlobalX(36.0f / 480), true);
////		tbs.font = Assets.china32;
//		marketButtonStyle = tbs;
//		return tbs;
		switch(type) {
		case food:
			return getStyleFromRegion("market/Market_menu_element-03");
		case grill:
			return getStyleFromRegion("market/Market_menu_element-04");
		case map:
			return getStyleFromRegion("market/Market_menu_element-05");
		case ads:
			return getStyleFromRegion("market/Market_menu_element-06");
		case coins:
			return getStyleFromRegion("market/Market_menu_element-07");
		case vanity:
			return null;
		default:
			return null;
		}
		
	}
	
	public static TextureRegion getIcon(Meat.Type type) {
		switch(type) {
		case BEEF: return beefIcon;
		case CHICKEN: return chickenIcon;
		case LAMB: return lambIcon;
		}
		return null;
	}
	
	// basically normalizes all font sizes, converts from standard 480 width to current width
	public static int getFontSize(int originalFont) {
		return KebabKing.getGlobalX(originalFont / 480.0f);
	}
	
	public static TextButtonStyle getPurchaseTypeButtonStyle(int fontSize) {
		if (purchaseTypeButtonStyle != null) return purchaseTypeButtonStyle;

		TextButtonStyle tbs = new TextButtonStyle();
		tbs.down = new TextureRegionDrawable(getTextureRegion("whitepixel"));
		tbs.up =  new TextureRegionDrawable(getTextureRegion("whitepixel"));
		tbs.font = Assets.generateChinaFont(fontSize, true);
		tbs.fontColor = new Color(0, 0, 0, 1);
		tbs.disabledFontColor = new Color(1, 1, 1, 1);
		tbs.disabled = new TextureRegionDrawable(getTextureRegion("market/bluePixel"));
//		tbs.font = Assets.china32;
		purchaseTypeButtonStyle = tbs;
		return tbs;
	}
	public static TextButtonStyle getFacebookButtonStyle() {
		if (purchaseTypeButtonStyle != null) return purchaseTypeButtonStyle;

		TextButtonStyle tbs = new TextButtonStyle();
		tbs.down = uiSkin.getDrawable("button_01");
		tbs.up = uiSkin.getDrawable("button_02");
		tbs.font = Assets.generateUIFont(48, true);
//		tbs.font = Assets.china32;
		purchaseTypeButtonStyle = tbs;
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
//		sps.background = uiSkin.getDrawable("textbox_01");
		return sps;
	}
	
	public static BitmapFont generateUIFont(int size, boolean permanent) {
		// these are leaking hard!
		p.size = getFontSize(size);
		System.out.println(size + " size font");
		BitmapFont toReturn = worksansLight.generateFont(p);
		if (!permanent)
			fonts.add(toReturn);
		return toReturn;
	}
	
	public static BitmapFont generateChinaFont(int size, boolean permanent) {
		// these are leaking hard!
		p.size = getFontSize(size);
		System.out.println(size + " size font");
		BitmapFont toReturn = china.generateFont(p);
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
		return getTextureRegion("market/icon_ads");
	}
	public static TextureRegion getCoinsIcon() {
		return getTextureRegion("icon_coin");
	}
	public static TextureRegion getCoin() {
		return getTextureRegion("coin");
	}
	public static TextureRegion getStar() {
		return getTextureRegion("topbar/Top-Bar-Element-06");
	}
	public static TextureRegion getHalfStar() {
		return getTextureRegion("topbar/Top-Bar-Element-08");
	}
	public static TextureRegion getGrayStar() {
		return getTextureRegion("topbar/Top-Bar-Element-07");
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
	public static TextButtonStyle getTextStyleFromRegion(String name, int fontSize) {
		TextButtonStyle bs = new TextButtonStyle();
		bs.font = Assets.generateUIFont(fontSize, true);
		bs.up = new TextureRegionDrawable(getTextureRegion(name));
		bs.down = new TextureRegionDrawable(getTextureRegion(name));
		return bs;
	}
	public static ButtonStyle getPauseButtonStyle() {
		return getStyleFromRegion("topbar/TopBarElement02");
	}
	public static ButtonStyle getButtonStyleMuted() {
		return getStyleFromRegion("topbar/TopBarElement01");
	}
	public static ButtonStyle getButtonStyleUnmuted() {
		return getStyleFromRegion("topbar/soundon");
	}
	public static ButtonStyle getCoinPlusStyle() {
		return getStyleFromRegion("icon_check");
	}
	public static TextureRegion getReputationBG() {
		return getTextureRegion("topbar/Top-Bar-Element-09");
	}
	public static TextureRegion getCoinsBG() {
		return getTextureRegion("topbar/Top-Bar-Element-05");
	}
	public static TextureRegion getCashBG() {
		return getTextureRegion("topbar/Top-Bar-Element-04");
	}
	
	public static ButtonStyle getButtonStylePurchaseableGreen() {
		ButtonStyle bs = new ButtonStyle();
		NinePatchDrawable np = new NinePatchDrawable(Assets.green9PatchSmall);
		bs.up = np;
		bs.down = np;
		return bs;
	}
	
	public static ButtonStyle getButtonStylePurchaseableGray() {
		ButtonStyle bs = new ButtonStyle();
		NinePatchDrawable np = new NinePatchDrawable(Assets.gray9PatchSmall);
		bs.up = np;
		bs.down = np;
		return bs;
	}
	
	public static void deleteTempResources() {
		for (BitmapFont bf : fonts) {
			System.out.println("deleting font: " + bf.getLineHeight());
			bf.dispose();
		}
		fonts.clear();
	}
}


