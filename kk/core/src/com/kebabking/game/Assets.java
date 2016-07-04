
package com.kebabking.game;

import java.util.HashMap;
import java.util.HashSet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
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
import com.badlogic.gdx.utils.I18NBundle;
import com.kebabking.game.StoreScreen.TableType;
import com.kebabking.game.Purchases.MeatTypes;

public class Assets {	
	static HashMap<String, Language> languages;
	
	public final static float GRILL_ANIMATION_TIME = 1f;
	//	final static int GRILL_ANIMATION_FRAMES = 1;
	final static float CUSTOMER_ANIMATION_TIME = .15f;
	final static int CUSTOMER_ANIMATION_FRAMES = 1;
	
	final static float PLANE_ANIMATION_TIME = .25f;
	final static float SIGN_ANIMATION_TIME = .5f;
	
	final static int WHITE_9PATCH_LEFT = 39;
	final static int WHITE_9PATCH_RIGHT = 40;
	
	// PIXELS from edge
	final static int WHITE_9PATCH_TOP = 1; // 25
	final static int WHITE_9PATCH_BOT = 24; // 24

	final static int PATCH_OFFSET_X = 9;
	final static int PATCH_OFFSET_Y = 9;
	
	final static Color RED = new Color(211/256.0f, 90/256.0f, 68/256.0f, 1f);
	final static Color YELLOW = new Color(235/256.0f, 169/256.0f, 28/256.0f, 1f);
	
//	public static String currencyChar; // this is the char used for in-game money
//	static String realCurrencyChar; // this is used for IAPs
//	final static String lower = "abcdefghijklmnopqrstuvwxyz";
//	final static String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
//	final static String alpha = lower + upper + "-";
	static String nums;
//	final static String allChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ .,!?@#%^&*()/1234567890:;-'\">+=_[]{}<" + currencyChar;
//	final static String chars = "a";

	public static AssetManager manager; // must be disposed
//
	public static BitmapFont arial;

	static Texture peppercornLogo; // must be disposed
	static Texture kebabMain;
	
	static TextureAtlas atlas; // must be disposed after load

	static Skin uiSkin;

	static TextureRegion title;
	
	static public class CustomerTextures {
		Animation idle;
		Animation right;
		Animation left;
		Animation down;
		Animation up;
		
		TextureRegion eyes;
		TextureRegion eyesRight;
	}
	
	static public class KebabTextures {
		TextureRegion raw;
		TextureRegion cooked;
		TextureRegion rawSp;
		TextureRegion cookedSp;
		TextureRegion burnt;
	}
	
	static TextureRegion coolerLidOpen;
	static TextureRegion coolerLidClosed;
	static TextureRegion spiceBoxDisabled;
	
	static TextureRegion beerIcon;
	static TextureRegion floatingBeer;
	
	static TextureRegion face1, face2, face3, face4, face5, faceSick;

	static TextureRegion cloud1;
	static TextureRegion cloud2;
	static TextureRegion sun;
	static TextureRegion skyStar;

	static TextureRegion paintBrush;
	static TextureRegion paintBrushSide;
	static TextureRegion trashIcon;

	static TextureRegion halfWheel;
	static TextureRegion wheelPointer;
	
	static TextureRegionDrawable volOn;
	static TextureRegionDrawable volMute;

	static TextureRegion facebook;
	static TextureRegion cash;
	static TextureRegion smalljade;
	static TextureRegion bigjade;
	static TextureRegion minijade;
	
	static TextureRegion redBill;
	static TextureRegion greenBill;
	static TextureRegion blueBill;

	static TextureRegion speech;

	static TextureRegion purchaseableCheck;

	static TextureRegion gray;
	static TextureRegion grayLight;
	static TextureRegion grayDark;
	static TextureRegion grayBlue;
	static TextureRegion white;
	static TextureRegion whiteAlpha;
	static TextureRegion grayAlpha;
	static TextureRegion grayAlphaLight;
	
	static TextureRegion notificationBottom;

	// I think keeping these around is killing memory consumption. see heapdump.
	static FreeTypeFontParameter p;
	static FreeTypeFontGenerator worksans;
	static FreeTypeFontGenerator worksansLight;
	static FreeTypeFontGenerator worksansHeavy;
	static FreeTypeFontGenerator china;
	
//	static final static
	static FreeTypeFontGenerator mangal;

	static BitmapFont timefont;
	
	static LabelStyle storeTitle;
	static LabelStyle purchaseTitle;
	static LabelStyle purchaseableTitle;
	
	static LabelStyle descriptionLS;
	static LabelStyle costLS;
	static LabelStyle mainLS;
//	static TextButtonStyle backButtonStyle;
//	static TextButtonStyle mainButtonStyle;
//	static TextButtonStyle startButtonStyle;
//	static TextButtonStyle marketButtonStyle;
	static ButtonStyle purchaseTypeButtonStyle;
//	static TextButtonStyle facebookButtonStyle;
//	static TextButtonStyle unlockButtonStyle;
	
	static TextureRegion marketShelf;
	static TextureRegion marketTitle;
	static TextureRegion marketLock;
	static TextureRegion marketJade;
	static TextureRegion purchaseableJade;
	static TextureRegion marketGreen;
	static TextureRegion marketDarkGreen;
	static TextureRegion questionMark;
	static TextureRegion jadeBox;
	static TextureRegion jadeBoxPlay;
	
	static TextureRegionDrawable marketGreenD;
	static TextureRegionDrawable marketDarkGreenD;
	static TextureRegionDrawable grayD;
	static TextureRegionDrawable grayLightD;

	static TextureRegion red;
	static TextureRegion redBright;
	static TextureRegion yellow;
	
	static TextureRegion greenArrow;

	static NinePatchDrawable roundUp;
	
	static Animation neonSign;
	static Animation plane;
	static TextureRegion selfieStick;
	
	// currently allocated fonts, dispose after every screen
//	static ArrayList<BitmapFont> fonts;
	
	static Music mainTheme;
	
	static Sound sizzle;
	static Sound trash;

	static Sound yum;
	static Sound veryYum;
	static Sound sick;

	static Sound sheepSound;
	static Sound cowSound;
	static Sound chickenSound;
	static Sound danSound;
	static Sound fatAmericanSound;
	static Sound policemanSound;
	
	static Sound coinSound;
	static Sound cashSound;
	static Sound dayCompleteSound;
	static Sound buttonClickSound;
	static Sound levelUpSound;
	static Sound unlockSound;
	static Sound dayStartSound;
	
//	static NinePatch green9Patch;
//	static NinePatch white9Patch;

	static NinePatch white9PatchSmall;
	static NinePatch green9PatchSmall;
	static NinePatch limeGreen9PatchSmallFilled;
//	static NinePatch gray9Patch;
	static NinePatch gray9PatchSmall;
	static NinePatch gray9PatchSmallThin;

	static NinePatch gray9PatchSmallFilled;
	static NinePatch gray9PatchSmallFilledCut;
	static NinePatch red9PatchSmall;
	
	// There should only be 4 labelstyles in here
	static HashMap<String, LabelStyle> styles;
	static HashMap<String, HashSet<Character>> charSets;
	
	// for testing
	static int totalCharsForFonts = 0;
	
	
	// languages and internationalization
	public static I18NBundle strings;

	// This loads peppercorn screen and preps stuff for second loader
	public static void preLoad() {
		
// TODO		
	}
	
	// static class containing relevant images, etc
	// should prepare animations and set regions to the appropriate size
	public static void load() {		
		manager = new AssetManager();
		
		// enqueue everything for loading, instead of just straight loading it. does it asynchronously.
		manager.load("atlas1.atlas", TextureAtlas.class);

		arial = new BitmapFont();

		//make sure to load logo first!!
		kebabMain = new Texture(Gdx.files.internal("Main-02.png"));
		peppercornLogo = new Texture(Gdx.files.internal("logo.png"));

		p = new FreeTypeFontParameter();
		worksans = new FreeTypeFontGenerator(Gdx.files.internal("data/WorkSans-Medium.otf"));
		worksansHeavy = new FreeTypeFontGenerator(Gdx.files.internal("data/WorkSans-SemiBold.otf"));
		worksansLight = new FreeTypeFontGenerator(Gdx.files.internal("data/WorkSans-Regular.otf"));
		china = new FreeTypeFontGenerator(Gdx.files.internal("data/CHINA.TTF"));
		mangal = new FreeTypeFontGenerator(Gdx.files.internal("data/mangal.ttf"));
		
		loadLanguages();

		styles = new HashMap<String, LabelStyle>();
		charSets = new HashMap<String, HashSet<Character>>();
	}
	
	public static Language getLanguage() {
		return languages.get(KebabKing.lang);
	}
	
	public static void loadLanguages() {
		languages = new HashMap<String, Language>();
		languages.put("en", new Language("en", worksans, worksansHeavy, worksansLight, china, false)); 
		languages.put("hi", new Language("hi", mangal, mangal, mangal, mangal, true)); 
		
		// load languages
		FileHandle baseFileHandle = Gdx.files.internal("data/bundles/strings");
		
//		manager.load("data/bundles/strings", I18NBundle.class);
//		manager.load("data/bundles/strings_hi", I18NBundle.class);
		
		strings = I18NBundle.createBundle(baseFileHandle, languages.get(KebabKing.lang).locale);
		
//		currencyChar = strings.get("currency");
//		realCurrencyChar = strings.get("currency_iaps");
//		System.out.println("currency: " + currencyChar);
		
//strings = manager.get("data/bundles/strings", I18NBundle.class);

		nums = strings.get("nums") + getCurrency() + "." + "-";
	}
	
	public static String getCurrency() {
//		return "¥";
		return strings.get("currency");
	}
	
	public static String getIAPCurrency() {
		return strings.get("currency_iaps");
	}

	public static String getIAPCurrencyAbbrev() {
		return strings.get("currency_abbrev");
	}
	
//	public static void createUI() {
//		uiAtlas = new TextureAtlas(Gdx.files.internal("ui/ui-orange.atlas"));
//
//		uiSkin = new Skin(uiAtlas);	
//	}

	/** loads music and sound */
	public static void loadSound() {
		mainTheme = Gdx.audio.newMusic(Gdx.files.internal("sound/music.ogg"));
		mainTheme.setLooping(true);
		
		// looping sounds should always be ogg
		sizzle = getSound("Sizzle.ogg");
		
		cowSound = getSound("Cow.mp3");
		chickenSound = getSound("Cow.mp3");
		sheepSound = getSound("Sheep.mp3");
		policemanSound = getSound("Policeman.mp3");
		fatAmericanSound = getSound("FatAmerican.mp3");
		danSound = getSound("Character_dan.mp3");
		sick = getSound("Sick.mp3");
		yum = getSound("Yum.mp3");
		veryYum = getSound("Very_satisfied.mp3");
		trash = getSound("trash.mp3");
		
		// TODO convert all wavs to mp3
		coinSound = getSound("coin1.wav");
		cashSound = getSound("cash.mp3");
		dayCompleteSound = getSound("success2.mp3");
		buttonClickSound = getSound("button2.mp3");
		levelUpSound = getSound("success1.mp3");
		unlockSound = getSound("3up.mp3");
		dayStartSound = getSound("success2.mp3");
	}
	
	public static Sound getSound(String file) {
		return Gdx.audio.newSound(Gdx.files.internal("sound/" + file));
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
	
	public static Animation createAnimationVertical(String region, float time, int rows) {
		TextureRegion walkSheet = getTextureRegion(region);
		TextureRegion[][] textureArray = walkSheet.split(walkSheet.getRegionWidth(), walkSheet.getRegionHeight()/rows);
		TextureRegion[] animationArray = new TextureRegion[rows];
		for (int i = 0; i < rows; i++) {
			animationArray[i] = textureArray[i][0];
		}
		Animation animation = new Animation(time, animationArray);
		animation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
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
	
	public static Animation createAnimationWithRepeatFirstFlipped(String region, float time, int row, int rowsColumns) {
		TextureRegion walkSheet = getTextureRegion(region);
		TextureRegion[][] textureArray = walkSheet.split(walkSheet.getRegionWidth()/rowsColumns, walkSheet.getRegionHeight()/rowsColumns);
		TextureRegion[] framesToUse = new TextureRegion[4];
	
		framesToUse[0] = textureArray[row-1][0];
		framesToUse[1] = textureArray[row-1][1];
		framesToUse[2] = textureArray[row-1][0];
		framesToUse[3] = textureArray[row-1][2];
		
		framesToUse[0].flip(true, false);
		framesToUse[1].flip(true, false);
		framesToUse[3].flip(true, false);

		Animation animation = new Animation(time, framesToUse);
		animation.setPlayMode(Animation.PlayMode.LOOP);
		
		return animation;
	}
	
	// access a single frame
	public static Animation createAnimation(String region, float time, int row, int rowsColumns, int column) {
		TextureRegion walkSheet = getTextureRegion(region);
		if (walkSheet == null) return null;
		TextureRegion[][] textureArray = walkSheet.split(walkSheet.getRegionWidth()/rowsColumns, walkSheet.getRegionHeight()/rowsColumns);
		Animation animation = new Animation(time, textureArray[row - 1][column-1]);
		animation.setPlayMode(Animation.PlayMode.LOOP);
		return animation;
	}

	public static boolean regionExists(String region) {
		return atlas.findRegion(region) != null;
	}
	
	public static TextureRegion getTextureRegion(String region) {
		TextureRegion toReturn = atlas.findRegion(region);
		if (toReturn == null) throw new NullPointerException("cant find '" + region +"'");
		return toReturn;
	}

	public static void dispose() {
		worksans.dispose();
		worksansHeavy.dispose();
		worksansLight.dispose();
		china.dispose();
		mainTheme.dispose();
	}
	
	/**
	 *  this automatically sets permanent to false for any temporary screens that call this
	 * @param size
	 * @return
	 */
//	public static LabelStyle generateLabelStyle(int size) {
//		return generateLabelStyle(size, false);
//	}

	// force all to be white
//	public static LabelStyle generateLabelStyleUI(int size) {
//		return generateLabelStyle(worksans, MainStoreScreen.FONT_COLOR, size);
//	}
	
//	public static LabelStyle generateLabelStyleUIGray(int size) {
//		return generateLabelStyle(worksans, DrawUI.REPUTATION_FONT_COLOR, size);		
//	}
//	public static LabelStyle generateLabelStyleUIRed(int size) {
//		return generateLabelStyle(worksans, SummaryScreen.RED, size);		
//	}
	public static LabelStyle generateLabelStyleUI(int size, String chars) {
		return generateLabelStyle(getLanguage().regular, size, chars);		
	}

	public static LabelStyle generateLabelStyleUILight(int size, String chars) {
		return generateLabelStyle(getLanguage().light, size, chars);
//		return generateLabelStyle(worksansLight, MainStoreScreen.FONT_COLOR, size);
	}
	
//	public static LabelStyle generateLabelStyleUIHeavy(int size) {
//		return generateLabelStyle(worksansHeavy, MainStoreScreen.FONT_COLOR, size);
//	}
	
	public static LabelStyle generateLabelStyleUIHeavy(int size, String chars) {
		return generateLabelStyle(getLanguage().heavy, size, chars);
	}
	
//	public static LabelStyle generateLabelStyleUIHeavyGreen(int size) {
//		return generateLabelStyle(worksansHeavy, MainStoreScreen.FONT_COLOR_GREEN, size);
//	}
//	public static LabelStyle generateLabelStyleUIChina(int size) {
//		return generateLabelStyle(china, Color.BLACK, size);
//	}	
	public static LabelStyle generateLabelStyleUIChina(int size, String chars) {
		return generateLabelStyle(getLanguage().chinaFont, size, chars);
	}
	
//	public static LabelStyle generateLabelStyleUIChinaRed(int size) {
//		return generateLabelStyle(china, RED, size);
//	}	

	public static void registerStyle(FreeTypeFontGenerator gen, int size, String chars) {
		// lets see how big it is if you only do one bitmap font
		String name = "" + gen.hashCode() + size;// + color.hashCode();
		
		p.size = getFontSize(size);
//		System.out.println("generating size " + p.size);
		
//		System.out.println(p.size);
		
		p.characters = chars;
		LabelStyle ls = new LabelStyle();
		ls.font = gen.generateFont(p);
		ls.fontColor = Color.WHITE;

		// if replacing
//		ls.font = arial;
//		ls.fontColor = color;

//		if (!styles.containsKey(name))
		styles.put(name, ls);
		
		HashSet<Character> charset = charSets.get(name);
		if (charset == null) {
			charset = new HashSet<Character>();
		}
		
		for (int i = 0; i < chars.length(); i++) {
			charset.add(chars.charAt(i));
		}
		
		charSets.put(name, charset);
		
//		System.out.println("total font chars: " + totalCharsForFonts);
	}

	/**Generate label style with given font family, size, and characters. if chars is null, use all chars by default.
	 * 
	 * @param gen
	 * @param size
	 * @param chars
	 * @return
	 */
	public static LabelStyle generateLabelStyle(FreeTypeFontGenerator gen, int size, String chars) {
//		chars = Assets.allChars; //TODO remove this test
		// best before was 28 label styles and 2548 font chars after summary
		
		// lets see how big it is if you only do one bitmap font
		String name = "" + gen.hashCode() + size;// + color.hashCode();
		if (styles.containsKey(name)) {
//			System.out.println("styles already contains this size");
//			LabelStyle copy = new LabelStyle(styles.get(name));
//			copy.fontColor = color;
			
			HashSet<Character> existingCharset = charSets.get(name);
			if (existingCharset == null) {
				throw new java.lang.AssertionError();
			}
			
			HashSet<Character> newCharset = new HashSet<Character>();
			for (int i = 0; i < chars.length(); i++) {
				newCharset.add(chars.charAt(i));
			}
			
			if (existingCharset.equals(newCharset)) {
//				System.out.println("Found identical existing charset");
				return styles.get(name);				
			}
			else {
				String newChars = "";
				newCharset.addAll(existingCharset);
				for (char c : newCharset) 
					newChars += c;
				totalCharsForFonts -= existingCharset.size();
				registerStyle(gen, size, newChars);
				totalCharsForFonts += newCharset.size();
//				System.out.println("Updating " + gen.toString() + " " + size + " for " + newChars);
				return styles.get(name);
			}
		}
		else {
//			System.out.println("styles doesn't contain this size");
//			else throw new java.lang.AssertionError("You need to register " + gen.toString() + " " + color.toString() + " " + size);
//			System.out.println("Registering " + gen.toString() + " " + size + " for " + chars);
			if (chars == null) {
//				registerStyle(gen, size, allChars);
//				totalCharsForFonts += allChars.length();
				throw new java.lang.AssertionError();
			}
			else {
				registerStyle(gen, size, chars);
				totalCharsForFonts += chars.length();
			}
			return styles.get(name);
		}
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
		facebook = getTextureRegion("screens/facebook-share-button");
		minijade = getTextureRegion("screens/minijade");
		smalljade = getTextureRegion("market/smallJade");
		bigjade = getTextureRegion("screens/pause-02");
		redBill = getTextureRegion("screens/bill_red");
		greenBill = getTextureRegion("screens/bill_green");
		blueBill = getTextureRegion("screens/bill_blue");

		grayDark = getTextureRegion("graypixel");
		gray = getTextureRegion("graypixel2");
		grayLight = getTextureRegion("lightgraypixel2");
		grayBlue = getTextureRegion("gray_blue");
		white = getTextureRegion("whitepixel");
		whiteAlpha = getTextureRegion("white_alpha");
		grayAlpha = getTextureRegion("gray_alpha");
		grayAlphaLight = getTextureRegion("gray_alpha_light");
		
		notificationBottom = getTextureRegion("screens/summary_bottom");

		title = getTextureRegion("screens/Main-02");
		
		spiceBoxDisabled = getTextureRegion("grill/grill-06");

		coolerLidOpen = getTextureRegion("kebabs/cover-active");
		coolerLidClosed = getTextureRegion("kebabs/cover-inactive");
//		coolerOpen = getTextureRegion("kebabs/CoolerOpen");
//		coolerClosed = getTextureRegion("kebabs/CoolerClosed");
		
		
		selfieStick = getTextureRegion("screens/selfie");
		plane = createAnimationVertical("screens/Airplane-01", PLANE_ANIMATION_TIME, 3);
		neonSign = createAnimationVertical("screens/neonsign", SIGN_ANIMATION_TIME, 3);
		
		floatingBeer = getTextureRegion("customers/BeerIcon-53");
		
		beerIcon = getTextureRegion("customers/beer_icon");
		
		face1 = getTextureRegion("customers/face1");
		face2 = getTextureRegion("customers/face2");
		face3 = getTextureRegion("customers/face3");
		face4 = getTextureRegion("customers/face4");
		face5 = getTextureRegion("customers/face5");
		faceSick = getTextureRegion("customers/face_s");

		paintBrush = getTextureRegion("grill/brush-48");
		paintBrushSide = getTextureRegion("grill/brush-50");

		trashIcon = getTextureRegion("grill/trashcan-52");

		cloud1 = getTextureRegion("background/SkyElement-02");
		cloud2 = getTextureRegion("background/SkyElement-03");
		sun = getTextureRegion("background/SkyElement-04");
		skyStar = getTextureRegion("background/SkyElement-05");

		halfWheel = getTextureRegion("screens/wheel7");
		wheelPointer = getTextureRegion("screens/triangle");

		marketShelf = getTextureRegion("market/shelf");
		marketTitle = getTextureRegion("market/Market_menu_element-08");
		marketGreen = getTextureRegion("lightGreen");
		marketDarkGreen = getTextureRegion("green");
		marketJade = getTextureRegion("market/jade");
		marketLock = getTextureRegion("market/Market_subMenus__template_element-02");
		purchaseableCheck = getTextureRegion("market/Market_subMenus__template_element-05");
		purchaseableJade = getTextureRegion("market/Market_subMenus__template_element-04");
		questionMark = getTextureRegion("market/question");
		jadeBox = getTextureRegion("market/jadeBox");
		jadeBox = getTextureRegion("screens/jadeBox");

		red = getTextureRegion("screens/red");
		redBright = getTextureRegion("red");
		yellow = getTextureRegion("topbar/yellow");
		
		greenArrow = getTextureRegion("screens/green_arrow");
		
		marketGreenD = new TextureRegionDrawable(marketGreen);
		marketDarkGreenD = new TextureRegionDrawable(marketDarkGreen);
		grayD = new TextureRegionDrawable(gray);
		grayLightD = new TextureRegionDrawable(grayLight);
		
		
//		white9Patch = new NinePatch(getTextureRegion("screens/white9patch"), WHITE_9PATCH_LEFT, WHITE_9PATCH_RIGHT, WHITE_9PATCH_TOP, WHITE_9PATCH_BOT);
//		green9Patch = new NinePatch(getTextureRegion("market/green9patch"), GREEN_9PATCH_OFFSET_X, GREEN_9PATCH_OFFSET_X_2, GREEN_9PATCH_OFFSET_Y, GREEN_9PATCH_OFFSET_Y_2);
		white9PatchSmall = new NinePatch(getTextureRegion("market/white9patchSmall"), PATCH_OFFSET_X, PATCH_OFFSET_X, PATCH_OFFSET_Y, PATCH_OFFSET_Y);
		green9PatchSmall = new NinePatch(getTextureRegion("market/green9patchSmallHollow"), PATCH_OFFSET_X, PATCH_OFFSET_X, PATCH_OFFSET_Y, PATCH_OFFSET_Y);
		
		
		limeGreen9PatchSmallFilled = new NinePatch(getTextureRegion("market/limeGreen9patchSmallFilled2"), 8, 4, 8, 8);
		
		//		gray9Patch = new NinePatch(getTextureRegion("market/gray9patch"), GREEN_9PATCH_OFFSET_X, GREEN_9PATCH_OFFSET_X_2, GREEN_9PATCH_OFFSET_Y, GREEN_9PATCH_OFFSET_Y_2);
		gray9PatchSmall = new NinePatch(getTextureRegion("market/gray9patchSmallHollow2"), PATCH_OFFSET_X, PATCH_OFFSET_X, PATCH_OFFSET_Y, PATCH_OFFSET_Y);
		gray9PatchSmallThin = new NinePatch(getTextureRegion("market/gray9patchSmallHollow"), PATCH_OFFSET_X, PATCH_OFFSET_X, PATCH_OFFSET_Y, PATCH_OFFSET_Y);
		gray9PatchSmallFilled = new NinePatch(getTextureRegion("market/gray9patchSmallFilled"), PATCH_OFFSET_X, PATCH_OFFSET_X, PATCH_OFFSET_Y, PATCH_OFFSET_Y);
		gray9PatchSmallFilledCut = new NinePatch(getTextureRegion("market/gray9patchSmallFilledCut"), 2,9,8, 8);
		red9PatchSmall = new NinePatch(getTextureRegion("market/red9patchSmall"), PATCH_OFFSET_X, PATCH_OFFSET_X, PATCH_OFFSET_Y, PATCH_OFFSET_Y);
	
		
//		createUI();

		loadSound();
	}
	
	public static CustomerTextures generateCustomerTextures(String prefix, float time) {
		prefix = "customers/" + prefix;
		CustomerTextures ct = new CustomerTextures();
		if (!regionExists(prefix)) return null;
		
		ct.idle = createAnimation(prefix, time, 2, 3, 1);
		if (ct.idle == null) return null;
		ct.right = createAnimationWithRepeatFirst(prefix, time, 1, 3);
		ct.left = createAnimationWithRepeatFirstFlipped(prefix, time, 1, 3);
		ct.up = createAnimationWithRepeatFirst(prefix, time, 3, 3);
		ct.down = createAnimationWithRepeatFirst(prefix, time, 2, 3);
		if (ct.idle == null || ct.right == null || ct.left == null || ct.up == null || ct.down == null)
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
	
	public static TextureRegion getStickTexture(Profile profile) {
		return profile.inventory.skewerType.getStick();
	}
	
	public static TextureRegion getMeatTexture(MeatTypes.Type type, Meat.State state, boolean spiced) {
		KebabTextures kt = type.textures;
		if (kt == null) return null;
		switch (state) {
		case RAW:
			if (!spiced) return kt.raw;
			else return kt.rawSp;
		case COOKED:
			if (!spiced) return kt.cooked;
			else return kt.cookedSp;
		case BURNT:
			return kt.burnt;
		}
		return null;
	}
	
	public static TextureRegion getMeatTexture(Meat meat) {
		return getMeatTexture(meat.type, meat.state, meat.spiced);
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
	
//	public static LabelStyle getStoreTitleLS() {
//		if (storeTitle != null) return storeTitle;
//		
//		LabelStyle titleStyle = new LabelStyle();
//		titleStyle.background = uiSkin.getDrawable("button_05");
//		titleStyle.font = Assets.generateUIFont(60, true);
//		storeTitle = titleStyle;
//		return titleStyle;
//	}
//	public static LabelStyle getMarketLS() {
////		LabelStyle titleStyle = new LabelStyle();
////		titleStyle.background = uiSkin.getDrawable("textbox_01");
////		titleStyle.font = Assets.getUIFont(50);
////		return titleStyle;
//		return getStoreTitleLS();
//	}
//	public static LabelStyle getPurchaseTypeTitleLS() {
//		if (purchaseTitle != null) return purchaseTitle;
//		LabelStyle titleStyle = new LabelStyle();
//		titleStyle.background = uiSkin.getDrawable("button_05");
//		titleStyle.font = Assets.generateUIFont(40, true);
//		purchaseTitle = titleStyle;
//		return titleStyle;
//	}
//	public static LabelStyle getPurchaseableTitleLS() {
//		if (purchaseableTitle != null) return purchaseableTitle;
//		
//		LabelStyle titleStyle = new LabelStyle();
////		titleStyle.background = uiSkin.getDrawable("textbox_01");
//		titleStyle.font = Assets.generateUIFont(26, true);
//		purchaseableTitle = titleStyle;
//		return titleStyle;
////		return ls32;
//	}
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
//	public static TextButtonStyle getMainButtonStyle() {
//		if (mainButtonStyle != null) return mainButtonStyle;
//		
//		TextButtonStyle tbs = new TextButtonStyle();
//		tbs.down = uiSkin.getDrawable("button_01");
//		tbs.up = uiSkin.getDrawable("button_03");
//		tbs.font = Assets.generateUIFont(48, true);
////		tbs.font = Assets.china48o;
//		mainButtonStyle = tbs;
//		return tbs;
//	}
//	public static TextButtonStyle getBackButtonStyle(int fontSize) {
////		if (backButtonStyle != null) return backButtonStyle;
////		
////		TextButtonStyle tbs = new TextButtonStyle();
////		tbs.down = uiSkin.getDrawable("button_01");
////		tbs.up = uiSkin.getDrawable("button_02");
////		tbs.font = Assets.generateUIFont(32, true);
//////		tbs.font = Assets.china32;
////		backButtonStyle = tbs;
////		return tbs;
//		return getTextStyleFromRegion("market/back_button", fontSize);
//	}
	
//	public static TextButtonStyle getStartButtonStyle() {
//		if (startButtonStyle != null) return startButtonStyle;
//
//		TextButtonStyle tbs = new TextButtonStyle();
//		tbs.down = uiSkin.getDrawable("button_01");
//		tbs.up = uiSkin.getDrawable("button_02");
//		tbs.font = Assets.generateUIFont(36, true);
////		tbs.font = Assets.china32;
//		startButtonStyle = tbs;
//		return tbs;
//	}
//	public static TextButtonStyle getMarketButtonStyle() {
//		return getStartButtonStyle();
//	}
	
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
				return getStyleFromRegion("market/food_drink");
			case grill:
				return getStyleFromRegion("market/grill");
			case map:
				return getStyleFromRegion("market/real_estate");
			case ads:
				return getStyleFromRegion("market/promo");
			case jeweler:
				return getStyleFromRegion("market/Jeweler_bo");
			case wheel:
				return getStyleFromRegion("market/wheel_solo_stand");
			case vanity:
				return null;
			default:
				return null;
		}
	}
	
//	public static TextureRegion getIcon(Meat.Type type) {
//		switch(type) {
//		case BEEF: return beefIcon;
//		case CHICKEN: return chickenIcon;
//		case LAMB: return lambIcon;
//		}
//		return null;
//	}
	
	// basically normalizes all font sizes, converts from standard 480 width to current width
	public static int getFontSize(int originalFont) {
		// note that we use Gdx.graphics instead of KebabKing.getGlobalX
		// because there's no guarantee that width will be set by this point.
		return (int) (originalFont * Gdx.graphics.getWidth() / 480.0f);
	}
	
	public static ButtonStyle getPurchaseTypeButtonStyle() {
		if (purchaseTypeButtonStyle != null) return purchaseTypeButtonStyle;

		ButtonStyle tbs = new TextButtonStyle();
		tbs.down = new TextureRegionDrawable(getTextureRegion("whitepixel"));
		tbs.up =  new TextureRegionDrawable(getTextureRegion("whitepixel"));
		tbs.disabled = new TextureRegionDrawable(getTextureRegion("market/bluePixel"));
//		tbs.font = Assets.china32;
		return tbs;
	}
//	public static TextButtonStyle getFacebookButtonStyle() {
//		if (purchaseTypeButtonStyle != null) return purchaseTypeButtonStyle;
//
//		TextButtonStyle tbs = new TextButtonStyle();
//		tbs.down = uiSkin.getDrawable("button_01");
//		tbs.up = uiSkin.getDrawable("button_02");
//		tbs.font = Assets.generateUIFont(48, true);
////		tbs.font = Assets.china32;
//		purchaseTypeButtonStyle = tbs;
//		return tbs;
//	}
//	public static TextButtonStyle getUnlockButtonStyle() {
//		if (unlockButtonStyle != null) return unlockButtonStyle;
//		TextButtonStyle tbs = new TextButtonStyle();
////		tbs.font = Assets.china32;
//		tbs.font = Assets.generateUIFont(32, true);
//		tbs.down = Assets.uiSkin.getDrawable("button_01");
//		tbs.up = Assets.uiSkin.getDrawable("button_02");
//		unlockButtonStyle = tbs;
//		return tbs;
//	}
	public static ScrollPaneStyle getSPS() {
		ScrollPaneStyle sps = new ScrollPaneStyle();
//		sps.vScroll = uiSkin.getDrawable("scroll_back_ver");
//		sps.hScroll = uiSkin.getDrawable("scroll_back_hor");
//		sps.vScrollKnob = uiSkin.getDrawable("slider_back_ver");
//		sps.background = uiSkin.getDrawable("textbox_01");
		return sps;
	}
	
//	public static BitmapFont generateUIFont(int size, boolean permanent) {
//		return arial;
//		// these are leaking hard!
////		p.size = getFontSize(size);
////		System.out.println(size + " size font");
////		BitmapFont toReturn = worksansLight.generateFont(p);
////		if (!permanent)
////			fonts.add(toReturn);
////		return toReturn;
//	}
	
//	public static BitmapFont generateChinaFont(int size, boolean permanent) {
//		return arial;
//		// these are leaking hard!
////		p.size = getFontSize(size);
////		System.out.println(size + " size font");
////		BitmapFont toReturn = china.generateFont(p);
////		if (!permanent)
////			fonts.add(toReturn);
////		return toReturn;
//	}

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
	public static ButtonStyle getStyleFromRegions(String name1, String name2) {
		ButtonStyle bs = new ButtonStyle();
		bs.up = new TextureRegionDrawable(getTextureRegion(name1));
		bs.down = new TextureRegionDrawable(getTextureRegion(name1));
		bs.disabled = new TextureRegionDrawable(getTextureRegion(name2));
		return bs;
	}
//	public static TextButtonStyle getTextStyleFromRegion(String name, int fontSize) {
//		TextButtonStyle bs = new TextButtonStyle();
//		bs.font = Assets.generateUIFont(fontSize, true);
//		bs.up = new TextureRegionDrawable(getTextureRegion(name));
//		bs.down = new TextureRegionDrawable(getTextureRegion(name));
//		return bs;
//	}
	public static ButtonStyle getPauseSettingsButtonStyle() {
		return getStyleFromRegions("topbar/TopBarElement02", "topbar/gear2");
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
	
	public static ButtonStyle getButtonStylePurchaseableWhite() {
		ButtonStyle bs = new ButtonStyle();
		NinePatchDrawable np = new NinePatchDrawable(Assets.white9PatchSmall);
		bs.up = np;
		bs.down = np;
		return bs;
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
	
	public static ButtonStyle getButtonStyleMusic() {
		ButtonStyle bs = new ButtonStyle();
		bs.up = new TextureRegionDrawable(getTextureRegion("screens/music_on"));
		bs.down = new TextureRegionDrawable(getTextureRegion("screens/music_on"));
		bs.disabled = new TextureRegionDrawable(getTextureRegion("screens/music_off"));
		return bs;
	}
	
	public static ButtonStyle getButtonStyleSound() {
		ButtonStyle bs = new ButtonStyle();
		bs.up = new TextureRegionDrawable(getTextureRegion("screens/sound_on"));
		bs.down = new TextureRegionDrawable(getTextureRegion("screens/sound_on"));
		bs.disabled = new TextureRegionDrawable(getTextureRegion("screens/sound_off"));
		return bs;
	}
	
	public static void deleteTempResources() {
//		for (BitmapFont bf : fonts) {
//			System.out.println("deleting font: " + bf.getLineHeight());
//			bf.dispose();
//		}
//		fonts.clear();
	}
}


