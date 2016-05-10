package com.kebabking.game;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kebabking.game.Purchases.LocationType;
import com.kebabking.game.Purchases.Purchaseable;

// make it draw a little bar at the top!

/** Static class for drawing UI elements */
public class DrawUI {
	static final Color WHITE = new Color(1, 1, 1, 1);
	static final Color GRAY = new Color(.2f, .2f, .2f, .5f);
	//	static final Color DARK_GRAY = new Color(.2f, .2f, .2f, .9f);

	//	static final int BRIBE_COST = 10;

	static final Color FONT_COLOR = new Color(0f, .2f, .3f, 1);	

	static final float FLASH_LENGTH = 1f;

	static final float CREATE_CASH_EVERY = 0.025f;
	static final float CREATE_JADE_EVERY = 0.3f;

	//	static final float xStarOffset = 0.06f;
	//	static final float X_STAR_INIT = 0.12f;

	static final float WHITE_ALPHA = 0.75f;
	static final float GRAY_ALPHA = 0.8f;

	//	static final float COINS_X  = 0.02f * ChuanrC.width;
	//	static final float COINS_Y	= 0.982f * ChuanrC.height;
	//	
	//	static final float MONEY_X = 0.70f * ChuanrC.width;
	//	static final float MONEY_Y = 0.982f * ChuanrC.height;

	static final float TIME_X =KebabKing.getGlobalX(0.4f);
	static final float TIME_Y = KebabKing.getGlobalY(0.98f);

	static final int MAX_PROJ = 30;

	static Stage uiStage;
	static KebabKing master;

	static InputProcessor pausedIP; // this is the stage that's happening in the background when a notification is launched

	static Table bigTable;
	static TopBar topBar;

	static Label dayEndLabel;

	static Color dayEndColor;

	static Table notificationTable;
	static Table subTableFull;
	static Table subTable;

	static boolean notificationActive;
	static boolean policeNotificationActive;
	static boolean tutorialNotificationActive;

	static Color tintColor;

	static LinkedList<Queue<Purchaseable>> unlockDisplayQueue = new LinkedList<Queue<Purchaseable>>();

	static String currentString;
	static int prevTime;

	static Label violationTime;
	static int violationTimeMins;
	static int violationTimeSecs;

	static Projectile[] proj;
	static int jadeProjLeft;
	static int cashProjLeft;
	static float jadeGenX, jadeGenY;
	static float cashGenX, cashGenY;
	static float timeToNextJade;
	static float timeToNextCash;


	static float flashCountdown;
	static Color flashColor;

	// for unlock notifications
	//	static SummaryScreen summaryScreen;

	public static void setInput(InputProcessor ip) {
		pausedIP = ip;
		if (ip == null || notificationActive) {
			Gdx.input.setInputProcessor(uiStage);
		}
		else {
			System.out.println("setting input proc");
			InputMultiplexer im = new InputMultiplexer();
			im.addProcessor(uiStage);
			im.addProcessor(ip);
			Gdx.input.setInputProcessor(im);
		}
	}

	public static void initialize(KebabKing master_in, SpriteBatch batch) {
		master = master_in;

		tintColor = new Color(0, 0, 0, 0);

		ScreenViewport viewport = new ScreenViewport();
		uiStage = new Stage(viewport, batch);		

		float barHeight = KebabKing.getGlobalY(TopBar.UI_BAR_HEIGHT);
		float adBarHeight = KebabKing.getGlobalY(TopBar.AD_BAR_HEIGHT);

		bigTable = new Table();
		bigTable.setSize(KebabKing.getWidth(), KebabKing.getHeight());

		topBar = new TopBar(master_in);
		//		uiTablef();

		//		uiTable.background(Assets.getTopBarBG());
		topBar.setPosition(0, KebabKing.getHeight() - barHeight - adBarHeight);
		topBar.setSize(KebabKing.getWidth(), barHeight + 5 + adBarHeight);

		topBar.padTop(KebabKing.getGlobalY(0.01f)).padLeft(KebabKing.getGlobalY(0.005f)).padRight(KebabKing.getGlobalY(0.005f));

		bigTable.add(topBar).top().height(barHeight + 5 + adBarHeight).expandY();
		//		bigTable.debugAll();
		uiStage.addActor(bigTable);

		// screen is divided into 9 regions follows:
		if (KitchenScreen.LAST_CUSTOMER_MODE) {
			String lastCust = Assets.strings.get("last_customer");
			dayEndLabel = new Label(lastCust, Assets.generateLabelStyleUIChina(70, lastCust));
			dayEndLabel.setPosition(0, KebabKing.getGlobalY(0.55f));
		}
		else {
			dayEndLabel = new Label("", Assets.generateLabelStyleUIChina(100, Assets.nums));
			dayEndLabel.setPosition(0, KebabKing.getGlobalY(0.7f));
		}
		dayEndLabel.setWidth(KebabKing.getWidth());
		dayEndLabel.setAlignment(Align.center);

		dayEndColor = new Color(1, 1, 1, 1);

		notificationTable = new Table();
		//		notificationTable.debug();
		//		notificationTable.debugAll();
		//		
		float notificationWidth = KebabKing.getGlobalX(0.9f);
		float notificationHeight = KebabKing.getGlobalY(0.9f);
		//		
		//		bigTable.debug();
		bigTable.row();
		bigTable.add(notificationTable).width(notificationWidth).height(notificationHeight).expandY().center();

		//		launchUnlockNotification(new Purchaseable[] {master.profile.inventory.locationType.values[0], 
		//								master.profile.inventory.drinkQuality.values[1],
		//								master.profile.inventory.grillSpecs.getGrillSize().values[2]
		//		});
		//		launchUnlockNotification(new Purchaseable[] {master.profile.inventory.locationType.values[2], 
		//		});

		//		master.shutdownAt = System.currentTimeMillis();
		//		launchPoliceNotification();

		proj = new Projectile[MAX_PROJ];
	}

	public static void render(float delta, SpriteBatch batch, Profile profile) {
		TopBar.update(delta, profile);

		//		updateCoinCashStrings(delta, profile);

		AdsHandler.checkIfShouldReward();
		SocialMediaHandler.checkIfShouldReward();

		if (policeNotificationActive)
			updateViolationTime();

		// hacky way around trash can
		if (master.kitchen != null && master.kitchen.grill.hoverTrash) 
			notificationTable.setVisible(false);
		else
			notificationTable.setVisible(true);

		batch.end();
		uiStage.act(delta);
		uiStage.draw();
		batch.begin();
		
		
		updateProjectiles(delta);
		drawProjectiles(batch);

		if (flashCountdown > 0) {
			drawFlash(batch, delta);
		}
	}

	public static void updateProjectiles(float delta) {
		for (int i = 0; i < proj.length; i++) {
			if (proj[i] != null) {
				proj[i].update(delta);
				if (proj[i].shouldDestroy) proj[i] = null;
			}
		}
		if (jadeProjLeft > 0) {
			timeToNextJade -= delta;
			if (timeToNextJade <= 0) {
				createOneProjectile(jadeGenX, jadeGenY, true, 1);
				jadeProjLeft--;
				timeToNextJade = CREATE_JADE_EVERY;
			}	
		}
		if (cashProjLeft > 0) {
			timeToNextCash -= delta;
			if (timeToNextCash <= 0) {
				createOneProjectile(cashGenX, cashGenY, false, 1);
				cashProjLeft--;
				timeToNextCash = CREATE_CASH_EVERY;
			}
		}		
	}

	public static void drawProjectiles(SpriteBatch batch) {
		// drawn on top of customers
		for (int i = 0; i < proj.length; i++) {
			if (proj[i] != null) {
				proj[i].draw(batch);
				if (proj[i].shouldDestroy) {
					TopBar.updateFor(proj[i]);
					proj[i] = null;
				}
			}
		}
	}
	
	public static void stopDrawingProjectiles() {
		jadeProjLeft = 0;
		cashProjLeft = 0;
	}

	public static void createOneProjectile(float posX, float posY, boolean jade, float value) {
		for (int i = 0; i < proj.length; i++) {
			if (proj[i] == null) {
				proj[i] = new Projectile(posX, posY, jade, value);
				break;
			}
		}
	}

	public static void createProjectiles(int count, float posX, float posY, boolean jade) {
		System.out.println("creating "  + count + " projectiles " + posX + " " + posY);
		if (jade) {
			jadeProjLeft += count;
			jadeGenX = posX;
			jadeGenY = posY;
		}
		else {
			cashProjLeft += count;
			cashGenX = posX;
			cashGenY = posY;
		}
	}

	public static void clickCoinPlus() {
		System.out.println("Coin Plus");

	}

	//	public static void tintWhite(SpriteBatch batch) {
	//		// draw gray fade over everything
	////		Color c = batch.getColor();
	////		batch.setColor(GRAY);
	//		batch.draw(Assets.whiteAlpha, 0, 0, KebabKing.getWidth(), KebabKing.getHeight());
	////		batch.setColor(c);
	//	}
	//	
	//	public static void tintGray(SpriteBatch batch) {
	//		batch.draw(Assets.grayAlpha, 0, 0, KebabKing.getWidth(), KebabKing.getHeight());
	//	}
	//	public static void tintGrayLight(SpriteBatch batch) {
	//		batch.draw(Assets.grayAlphaLight, 0, 0, KebabKing.getWidth(), KebabKing.getHeight());
	//	}

	public static void startFlash() {
		flashCountdown = FLASH_LENGTH;
	}

	public static void drawFlash(SpriteBatch batch, float delta) {
		if (flashColor == null) {
			flashColor = new Color(1, 1, 1, 1);
		}
		Color o = batch.getColor();
		flashColor.set(1,  1,  1, flashCountdown / FLASH_LENGTH);
		batch.setColor(flashColor);
		batch.draw(Assets.white, 0, 0, KebabKing.getWidth(), KebabKing.getHeight());
		batch.setColor(o);
		flashCountdown -= delta;
	}

	public static void tint(SpriteBatch batch, TextureRegion tint, float alpha) {
		if (alpha < 1) {
			Color o = batch.getColor();
			tintColor.set(1, 1, 1, alpha);
			batch.setColor(tintColor);
			batch.draw(tint, 0, 0, KebabKing.getWidth(), KebabKing.getHeight());
			batch.setColor(o);
		}
		else {
			batch.draw(tint, 0, 0, KebabKing.getWidth(), KebabKing.getHeight());
		}
	}

	public static boolean shouldTint() {
		return DrawUI.notificationActive && !DrawUI.tutorialNotificationActive;
	}

	//	public static void tintGrayAlpha(SpriteBatch batch, float alpha) {
	//		Color o = batch.getColor();
	//		grayDraw.set(1, 1, 1, alpha);
	//		batch.setColor(grayDraw);
	//		batch.draw(Assets.gray, 0, 0, KebabKing.getWidth(), KebabKing.getHeight());
	//		batch.setColor(o);
	//	}
	//	
	//	public static void tintWhiteAlpha(SpriteBatch batch, float alpha) {
	//		Color o = batch.getColor();
	//		grayDraw.set(1, 1, 1, alpha);
	//		batch.setColor(grayDraw);
	//		batch.draw(Assets.white, 0, 0, KebabKing.getWidth(), KebabKing.getHeight());
	//		batch.setColor(o);
	//	}
	//
	//	public static void drawCoins(SpriteBatch batch, Profile profile) {
	//		drawCoins(batch, profile.coins);	
	//	}
	//	
	//	public static void drawMoney(SpriteBatch batch, Profile profile) {
	//		drawMoney(batch, profile.cash);	
	//	}
	//	
	//	public static void drawMoney(SpriteBatch batch, float money) {
	////		Assets.china48o.setColor(batch.getColor());
	////		Assets.china48o.draw(batch, "$" + money, MONEY_X, MONEY_Y);		
	////		Assets.china48o.setColor(WHITE);
	//	}
	//	
	//	public static void drawCoins(SpriteBatch batch, int coins) {
	////		Assets.china48o.setColor(batch.getColor());
	////		Assets.china48o.draw(batch, "Coins:" + coins, COINS_X, COINS_Y);		
	////		Assets.china48o.setColor(WHITE);
	//	}
	//
	public static void countdownTime(SpriteBatch batch, float inputTime) {
		int currentTime = (int) (inputTime + 1);
		if (currentTime != prevTime || currentString  == null) {
			currentString = "" + currentTime;
			prevTime = currentTime;
		}
		dayEndLabel.setText(currentString);

		dayEndColor.set(1,  1,  1, inputTime - (int) inputTime);

		dayEndLabel.setColor(dayEndColor);
		dayEndLabel.draw(batch, 1);	
	}

	public static void drawLastCustomer(SpriteBatch batch, float inputTime) {
		//		dayEndLabel.setText(KitchenScreen.LAST_CUSTOMER_TEXT);
		//		System.out.println("drawing last customer " + inputTime);
		dayEndColor.set(1,  1,  1, Math.max(0, ((inputTime + KitchenScreen.LAST_CUSTOMER_FADE) / KitchenScreen.LAST_CUSTOMER_FADE)));

		dayEndLabel.setColor(dayEndColor);
		dayEndLabel.draw(batch, 1);	
	}

	public static void drawTime(SpriteBatch batch, float time) {
		//		Assets.getTimeFont().draw(batch, (int) (time / 60) + ":" + String.format("%02d", (int) (time % 60)) , TIME_X, TIME_Y);			
	}
	//
	//	public static void drawStars(SpriteBatch batch, Profile profile) {
	//		float rep;
	//
	//		float xStar = X_STAR_INIT;
	//
	//		// draw stars 
	//		for (rep = profile.getCurrentReputation(); rep > 0.5f; rep--) {
	//			batch.draw(Assets.star, ChuanrC.width * xStar, yStar, starWidth, starHeight);
	//			xStar += xStarOffset;
	//		}
	//
	//		// if should draw half star
	//		if (rep == 0.5f) {
	//			batch.draw(Assets.starHalf, ChuanrC.width * xStar, yStar, starWidth, starHeight);				
	//		}
	//	}


	public static void launchPoliceNotification() {
		if (!canLaunchNotification()) return;
		prepareNotification();
		policeNotificationActive = true;
		preparePoliceNotificationTable();
	}

	public static void launchShareSuccessNotification(int coins) {
		if (!canLaunchNotification()) return;
		System.out.println("launching share success notification for " + coins + " coins");
		prepareNotification();
		prepareSuccessNotificationTable(coins);
	}


	public static void launchAdSuccessNotification(int coins) {
		if (!canLaunchNotification()) return;
		//		System.out.println("launching ad success notification for " + coins + " coins");
		prepareNotification();
		if (coins <= 0) {
			preparePoliceSuccessNotificationTable();
		}
		else 
			prepareSuccessNotificationTable(coins);
	}

	public static void launchAdNotAvailableNotification() {
		if (!canLaunchNotification()) return;
		if (notificationActive) {
			exitNotification();
			//			throw new java.lang.AssertionError();
		};
		prepareNotification();
		prepareAdNotAvailableNotificationTable();
	}

	private static void prepareTutorialNotification(float height) {
		notificationActive = true;
		tutorialNotificationActive = true;
		subTableFull = new Table();
		subTable = new Table();

		//		notificationTable.debugAll();
		subTable.setBackground(new TextureRegionDrawable(Assets.white));
		float width = 0.7f;

		//		float padTop = (1 - height) / 2f;
		//		float padBot = padTop;
		//		notificationTable.add(subTable).padLeft(KebabKing.getGlobalX(padX)).padRight(KebabKing.getGlobalX(padX)).padTop(KebabKing.getGlobalY(padTop)).padBottom(KebabKing.getGlobalY(padBot)).expandX().fill();//.top();

		Table topTable = DrawUI.generateTutorialTopTable();
		float topBarPadY = KebabKing.getGlobalY(0.03f);
		subTable.add(topTable).top().expandX().fillX().padBottom(topBarPadY).padTop(topBarPadY);
		subTable.row();

		subTableFull.add(subTable).expand().fill();
		//		notificationTable.add(subTable).width(KebabKing.getGlobalX(width)).height(KebabKing.getGlobalYFloat(height)).expandX().expandY().top().padTop(KebabKing.getGlobalYFloat(-0.03f));
		notificationTable.add(subTableFull).width(KebabKing.getGlobalX(width)).expandX().expandY().top().padTop(KebabKing.getGlobalYFloat(-0.03f));
		//		notificationTable.debugAll();
		//		subTable.debugAll();
	}

	public static Table generateTutorialTopTable() {
		Table topBar = new Table();
		//		topBar.debugAll();
		Table topLeft = new Table();
		Table topRight = new Table();
		topLeft.setBackground(new TextureRegionDrawable(Assets.red));
		topRight.setBackground(new TextureRegionDrawable(Assets.red));

		Table topCenter = new Table();

		TextureRegion topImage = Assets.getTextureRegion("screens/Summary-08");
		float IMAGE_WIDTH = 0.25f;
		float IMAGE_HEIGHT = IMAGE_WIDTH * topImage.getRegionHeight() / topImage.getRegionWidth();
		Image image = new Image(topImage);
		//		image.setWidth(KebabKing.getGlobalX(IMAGE_WIDTH));
		//		image.setHeight(KebabKing.getGlobalY(IMAGE_HEIGHT));
		topCenter.add(image).width(KebabKing.getGlobalX(IMAGE_WIDTH)).height(KebabKing.getGlobalX(IMAGE_HEIGHT));
		topCenter.row();
		Label dailyAccounts = new Label(Assets.strings.get("tutorial"), Assets.generateLabelStyleUI(14, Assets.strings.get("tutorial")));
		dailyAccounts.setColor(MainStoreScreen.FONT_COLOR);

		float BAR_HEIGHT = 0.005f;

		topCenter.add(dailyAccounts);
		topBar.add(topLeft).expandX().fillX().height(KebabKing.getGlobalY(BAR_HEIGHT));

		float imagePadX = KebabKing.getGlobalX(0.03f);
		topBar.add(topCenter).top().padLeft(imagePadX).padRight(imagePadX);;;
		topBar.add(topRight).expandX().fillX().height(KebabKing.getGlobalY(BAR_HEIGHT));;
		return topBar;
	}

	private static void prepareNotification() {
		notificationActive = true;
		Gdx.input.setInputProcessor(uiStage);

		subTableFull = new Table();
		subTable = new Table();

		//		notificationTable.debugAll();
		subTable.setBackground(new TextureRegionDrawable(Assets.white));
		float width = 0.7f;
		//		height = 0.4f;
		//		subTable.debugAll();
		//		float padTop = (1 - height) / 2f;
		//		float padBot = padTop;
		subTableFull.add(subTable).expand().fill();
		notificationTable.add(subTableFull).width(KebabKing.getGlobalX(width)).expandX().expandY().center();//.top();

		//		notificationTable.add(subTable).padLeft(KebabKing.getGlobalX(padX)).padRight(KebabKing.getGlobalX(padX)).padTop(KebabKing.getGlobalY(padTop)).padBottom(KebabKing.getGlobalY(padBot)).expandX().fill();//.top();
		//		notificationTable.add(subTable).width(KebabKing.getGlobalX(width)).expandY().height(KebabKing.getGlobalYFloat(height)).expandX().expandY().center();//.top();
		//		notificationTable.debug();
		Table topTable = SummaryScreen.generateTopTable();
		float topBarPadY = KebabKing.getGlobalY(0.02f);
		subTable.add(topTable).top().expandX().fillX().padBottom(topBarPadY).padTop(topBarPadY);
		subTable.row();

		//		subTable.debugAll();
		//		notificationTable.debugAll();
	}

	public static void prepareBlankNotification() {
		notificationActive = true;
		Gdx.input.setInputProcessor(uiStage);

		subTableFull = new Table();
		subTable = new Table();

		//		//	notificationTable.debugAll();
		//		subTable.setBackground(new TextureRegionDrawable(Assets.white));
		float width = 0.7f;
		//		//	height = 0.4f;
		//		//	subTable.debugAll();
		//		//	float padTop = (1 - height) / 2f;
		//		//	float padBot = padTop;
		subTableFull.add(subTable).expand().fill();
		notificationTable.add(subTableFull).width(KebabKing.getGlobalX(width)).expandX().expandY().center().fill();//.top();

		//	notificationTable.add(subTable).padLeft(KebabKing.getGlobalX(padX)).padRight(KebabKing.getGlobalX(padX)).padTop(KebabKing.getGlobalY(padTop)).padBottom(KebabKing.getGlobalY(padBot)).expandX().fill();//.top();
		//	notificationTable.add(subTable).width(KebabKing.getGlobalX(width)).expandY().height(KebabKing.getGlobalYFloat(height)).expandX().expandY().center();//.top();
		//	notificationTable.debug();
		//		Table topTable = SummaryScreen.generateTopTable();
		//		float topBarPadY = KebabKing.getGlobalY(0.02f);
		//		subTable.add(topTable).top().expandX().fillX().padBottom(topBarPadY).padTop(topBarPadY);
		//		subTable.row();
	}

	public static boolean canLaunchNotification() {
		return !tutorialNotificationActive;
	}


	public static void launchTutorialNotification(String title, String aboveImage, String belowImage, TextureRegion toDraw) {
		if (!canLaunchNotification()) return;
		if (notificationActive && KebabKing.STRICT_MODE) {
			// TODO change this to just exitNotification();
			throw new java.lang.AssertionError();
		}

		else {
			prepareTutorialNotification(0.4f);
			prepareTutorialNotificationTable(title, aboveImage, belowImage, toDraw);
		}
	}

	public static void launchTutorialSuccessNotification(String title, String aboveImage, String belowImage, TextureRegion toDraw) {
		if (!canLaunchNotification()) return;
		if (notificationActive  && KebabKing.STRICT_MODE) {
			// TODO change this to just exitNotification();
			throw new java.lang.AssertionError();
		}

		else {
			prepareTutorialNotification(0.4f);
			prepareTutorialSuccessNotificationTable(title, aboveImage, belowImage, toDraw);
		}
	}

	public static void launchUnlockNotification(Queue<Purchaseable> available) {
		if (!canLaunchNotification()) return;
		if (notificationActive) enqueueUnlockNotification(available);
		else {
			//			launchNotification(unlocked.getName(), unlocked.getDescription(), unlocked.getIcon(), true, unlocked.unlockAtLevel());

			for (Purchaseable toUnlock : available) {
				if (toUnlock.cashToUnlock() == 0 && toUnlock.coinsToUnlock() == 0) {
//					System.out.println(toUnlock.getName());
//					System.out.println(master.profile.getLevel());
					master.profile.inventory.unlock(toUnlock, toUnlock.getType());
					master.store.storeScreen.updatePurchaseableAfterUnlock(toUnlock);
				}
			}

			prepareNotification();
			prepareUnlockNotificationTable(available);
		}

		// also unlock things that should be unlocked
	}

	public static void exitTutorialNotification() {
		exitNotification();
	}

	public static void handleViolationEnded() {
		DrawUI.exitNotification();
		DrawUI.launchAdSuccessNotification(0);
	}

	private static void exitNotification() {
		policeNotificationActive = false;
		notificationActive = false;
		tutorialNotificationActive = false;

		clearNotificationTableDontCallThis();
		setInput(pausedIP);

		if (unlockDisplayQueue.size() > 0) {
			launchUnlockNotification(unlockDisplayQueue.pop());
		}
	}

	private static void prepareTutorialSuccessNotificationTable(String title, String aboveImage, String belowImage, TextureRegion toDraw) {
		prepareTutorialNotificationTable(title, aboveImage, belowImage, toDraw);
		addContinueButton("CONTINUE");
	}

	private static void prepareTutorialNotificationTable(String title, String aboveImage, String belowImage, TextureRegion toDraw) {
		Label titleLabel = new Label(title, Assets.generateLabelStyleUILight(24, title));
		titleLabel.setAlignment(Align.center);
		titleLabel.setColor(MainStoreScreen.FONT_COLOR);
		titleLabel.setWrap(true);

		float padX = KebabKing.getGlobalXFloat(0.05f);
		Table textTable = new Table();

		textTable.add(titleLabel).center().expandX().fillX().padTop(KebabKing.getGlobalY(0f));
		textTable.row();

		if (aboveImage != null && aboveImage.length() > 0) {
			Label above = new Label(aboveImage, Assets.generateLabelStyleUILight(16, aboveImage));
			above.setAlignment(Align.center);
			above.setColor(MainStoreScreen.FONT_COLOR);
			above.setWrap(true);

			textTable.add(above).center().expandX().fillX().padTop(KebabKing.getGlobalYFloat(0.0f));
			textTable.row();
		}

		if (toDraw != null) {
			float regHeight = KebabKing.getGlobalYFloat(0.07f);
			//			float regHeight = toDraw.getRegionHeight() * regWidth / toDraw.getRegionWidth();
			float regWidth = toDraw.getRegionWidth() * regHeight / toDraw.getRegionHeight();
			Image image = new Image(toDraw);
			textTable.add(image).center().expandX().width(regWidth).height(regHeight).top().padTop(KebabKing.getGlobalYFloat(0.02f));
			textTable.row();
		}

		Label below = new Label(belowImage, Assets.generateLabelStyleUILight(16, belowImage));
		below.setAlignment(Align.center);
		below.setColor(MainStoreScreen.FONT_COLOR);
		below.setWrap(true);
		textTable.add(below).center().expandX().fillX().padTop(KebabKing.getGlobalYFloat(0.02f)).top();
		textTable.row();

		subTable.add(textTable).padLeft(padX).padRight(padX).expandX().fillX().top().expandY();
		//		subTable.debugAll();
		addTaperedBottom();
	}

	private static void preparePoliceNotificationTable() {
		String policeShutYouDown = Assets.strings.get("police_shut_down");
		Label obey = new Label(policeShutYouDown, Assets.generateLabelStyleUILight(24, policeShutYouDown));
		obey.setAlignment(Align.center);
		obey.setWrap(true);
		obey.setColor(MainStoreScreen.FONT_COLOR);
		subTable.add(obey).center().expandX().fillX().padTop(KebabKing.getGlobalY(0.01f));
		subTable.row();
		//		subTable.debug();
		TextureRegion reg = Customer.CustomerType.POLICE.male.idle.getKeyFrame(0);
		float policeWidth = KebabKing.getGlobalX(0.3f);
		float policeHeight = reg.getRegionHeight() * policeWidth / reg.getRegionWidth();
		Image police = new Image(reg);
		subTable.add(police).center().expandX().width(policeWidth).height(policeHeight).top().padTop(KebabKing.getGlobalY(0)).padBottom(KebabKing.getGlobalY(-0.01f));
		subTable.row();

		String customersGettingSick = Assets.strings.get("customers_getting_sick");
		Label sick = new Label(customersGettingSick, Assets.generateLabelStyleUILight(12, customersGettingSick));
		sick.setAlignment(Align.center);
		sick.setColor(MainStoreScreen.FONT_COLOR);
		sick.setWrap(true);
		subTable.add(sick).center().expandX().fillX().padLeft(KebabKing.getGlobalXFloat(0.04f)).padRight(KebabKing.getGlobalXFloat(0.04f));
		subTable.row();

		Table violationTable = new Table();
		String violationText = Assets.strings.get("violation_expires_in");
		Label expires = new Label(violationText + " ", Assets.generateLabelStyleUIHeavy(16, violationText));
		expires.setAlignment(Align.center);
		expires.setColor(MainStoreScreen.FONT_COLOR);

		violationTime = new Label("", Assets.generateLabelStyleUIHeavy(16, Assets.nums + ":"));
		updateViolationTime();
		violationTime.setAlignment(Align.center);
		violationTime.setColor(MainStoreScreen.FONT_COLOR);

		violationTable.add(expires).expandX().right();
		violationTable.add(violationTime).expandX().left();

		subTable.add(violationTable).center().expandX().fillX().padTop(KebabKing.getGlobalY(0.02f));
		subTable.row();

		String orText = "- " + Assets.strings.get("or") + " -";
		Label or = new Label(orText, Assets.generateLabelStyleUIHeavy(20,  orText));
		or.setColor(SummaryScreen.RED);
		float padTop = 0.01f;
		float padBot = 0.015f;
		subTable.add(or).padTop(KebabKing.getGlobalY(padTop)).padBottom(KebabKing.getGlobalY(padBot));
		subTable.row();

		Table bribeButton = new Table();
		bribeButton.setTouchable(Touchable.enabled);
		bribeButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				attemptBribe();
			}
		});
		String bribeText = Assets.strings.get("bribe");
		Label bribe = new Label(bribeText, Assets.generateLabelStyleUIChina(28, bribeText));	
		Table bribeTable = new Table();

		boolean canAfford = true;

		if (canAfford)
			bribeTable.setBackground(Assets.marketGreenD);
		else
			bribeTable.setBackground(Assets.grayLightD);

		bribeTable.add(bribe).padLeft(KebabKing.getGlobalX(0.01f)).padRight(KebabKing.getGlobalX(0.01f));
		bribeButton.add(bribeTable).width(KebabKing.getGlobalX(0.2f)).center().fill();

		Table priceCoinsTable = new Table();
		//		Label priceCoins = new Label("", Assets.generateLabelStyleUIChinaWhite(28, Assets.nums));
		//		priceCoins.setTouchable(Touchable.disabled);

		if (canAfford) {
			priceCoinsTable.setBackground(new TextureRegionDrawable(Assets.marketDarkGreen));
		}
		else {
			priceCoinsTable.setBackground(new TextureRegionDrawable(Assets.gray));			
		}

		float height = KebabKing.getGlobalYFloat(0.05f);

		//		priceCoinsTable.add(new Image(Assets.marketJade)).width(height).fill();
		//		priceCoins.setText("" + BRIBE_COST);
		Image priceCoins = new Image(Assets.getTextureRegion("market/Market_menu_element-09"));
		//		priceCoins.setWidth(height*0.6f);
		//		priceCoins.setHeight(height*0.6f);
		//		priceCoinsTable.add(priceCoins).padLeft(KebabKing.getGlobalX(-0.005f)).padRight(KebabKing.getGlobalX(0.01f)).fill();
		priceCoinsTable.add(priceCoins).width(height*0.8f).height(height*0.8f).expandY();

		bribeButton.add(priceCoinsTable).expandY().width(height).height(height);

		subTable.add(bribeButton).height(height).expandY().top().padBottom(KebabKing.getGlobalY(0.035f));

		addTaperedBottom();
		addContinueButton(Assets.strings.get("wait"));
	}

	private static void attemptBribe() {
		System.out.println("bribe was pressed");
		AdsHandler.showAd();
	}

	private static void updateViolationTime() {
		if (!master.profile.violationActive()) {
			// need to check that this notification is actually police
			exitNotification();
			return;
		}
		if (getViolationRemainingMins() != violationTimeMins) {
			violationTimeMins = getViolationRemainingMins();
			violationTime.setText(getCurrentViolationString());
		}
		if (getViolationRemainingSecs() != violationTimeSecs) {
			violationTimeSecs = getViolationRemainingSecs();
			violationTime.setText(getCurrentViolationString());
		}
	}

	private static String getCurrentViolationString() {
		if (violationTimeSecs < 10) {
			return LanguageManager.localizeNumber(violationTimeMins) + ":" + LanguageManager.localizeNumber(0) + LanguageManager.localizeNumber(violationTimeSecs);
		}
		return LanguageManager.localizeNumber(violationTimeMins) + ":" + LanguageManager.localizeNumber(violationTimeSecs);
	}

	private static int getViolationRemainingMins() {
		return (int) (master.profile.getViolationSecondsRemaining() / 60);
	}

	private static int getViolationRemainingSecs() {
		return (int) (master.profile.getViolationSecondsRemaining() % 60);
	}

	// launch this to launch jade wheel
	//	private static void prepareJadeWheelNotificationTable() {
	//
	//		//		
	//		//		
	//		//		launchAdSuccessNotification();
	//	}

	private static void preparePoliceSuccessNotificationTable() {
		Label labelTop = new Label(Assets.strings.get("let_you_off"), Assets.generateLabelStyleUILight(24, Assets.strings.get("let_you_off")));
		labelTop.setAlignment(Align.center);
		labelTop.setColor(MainStoreScreen.FONT_COLOR);
		labelTop.setWrap(true);
		float padTop = 0.02f;
		subTable.add(labelTop).center().expandX().fillX().padTop(KebabKing.getGlobalY(padTop)).top().padLeft(KebabKing.getGlobalXFloat(0.05f)).padRight(KebabKing.getGlobalXFloat(0.05f));
		subTable.row();

		TextureRegion reg = Customer.CustomerType.POLICE.male.idle.getKeyFrame(0);
		float policeWidth = KebabKing.getGlobalX(0.3f);
		float policeHeight = reg.getRegionHeight() * policeWidth / reg.getRegionWidth();
		Image police = new Image(reg);
		subTable.add(police).center().expandX().width(policeWidth).height(policeHeight).top().padTop(KebabKing.getGlobalY(0.02f)).padBottom(KebabKing.getGlobalY(-0.01f)).expandY().top();
		subTable.row();

		addTaperedBottom();
		addContinueButton();
	}

	// if coins == 0, assume this was for a violation
	// otherwise it was for jade
	private static void prepareSuccessNotificationTable(int coins) {
		if (KebabKing.STRICT_MODE && coins < 0) throw new java.lang.AssertionError();
		Label labelTop = new Label(Assets.strings.get("congratulations"), Assets.generateLabelStyleUILight(24, Assets.strings.get("congratulations")));
		labelTop.setAlignment(Align.center);
		labelTop.setColor(MainStoreScreen.FONT_COLOR);
		labelTop.setWrap(true);
		float padTop = 0.06f;
		subTable.add(labelTop).center().expandX().fillX().padTop(KebabKing.getGlobalY(padTop)).top();
		subTable.row();

		Label labelBot = new Label(Assets.strings.get("you_earned"), Assets.generateLabelStyleUILight(24, Assets.strings.get("you_earned")));
		labelBot.setAlignment(Align.center);
		labelBot.setColor(MainStoreScreen.FONT_COLOR);
		labelBot.setWrap(true);
		subTable.add(labelBot).center().expandX().fillX().padTop(KebabKing.getGlobalY(0.01f)).top();
		subTable.row();

		Table jadeTable = new Table();

		Label count = new Label(LanguageManager.localizeNumber(coins) + "x", Assets.generateLabelStyleUI(44, Assets.nums + "x"));
		count.setColor(MainStoreScreen.FONT_COLOR);

		jadeTable.add(count).expandX().right();

		float jadeWidth = KebabKing.getGlobalX(0.2f);
		float jadeHeight = jadeWidth;
		Image jadeIcon = new Image(Assets.getTextureRegion("market/Jeweler-09"));
		jadeTable.add(jadeIcon).width(jadeWidth).height(jadeHeight).expandX().left().padLeft(-KebabKing.getGlobalX(0.04f));

		subTable.add(jadeTable).top().expandY().padTop(KebabKing.getGlobalY(0.025f)).padBottom(KebabKing.getGlobalYFloat(0.05f));

		addTaperedBottom();
		addContinueButton();
	}

	private static void prepareAdNotAvailableNotificationTable() {
		String noVids = Assets.strings.get("no_videos");
		Label noVideos = new Label(noVids, Assets.generateLabelStyleUILight(24, noVids));
		noVideos.setAlignment(Align.center);
		noVideos.setWrap(true);
		noVideos.setColor(MainStoreScreen.FONT_COLOR);
		subTable.add(noVideos).center().expandX().fillX().padTop(KebabKing.getGlobalY(0.08f)).top().padBottom(KebabKing.getGlobalY(0.08f)).expandY().padLeft(KebabKing.getGlobalXFloat(0.05f)).padRight(KebabKing.getGlobalXFloat(0.05f));

		subTable.row();

		addTaperedBottom();
		addContinueButton();
	}

	private static void prepareUnlockNotificationTable(Queue<Purchaseable> available) {

		//		float subTableWidth = (notificationTable.getWidth() - KebabKing.getGlobalX(2 * padX));
		//		subTable.debugAll();

		// add daily accounts thing.
		float unlocksTableWidth = KebabKing.getGlobalX(0.52f);

		// contains youve unlocked and all unlocks
		Table unlocksTable = new Table();
		unlocksTable.setBackground(new NinePatchDrawable(Assets.gray9PatchSmallThin));

		Table topPart = new Table();

		int newAvailableCount = available.size();
		if (LocationType.UNLOCKS_ONLY_WITH_LOCATIONS) {
			newAvailableCount--;
		}
		String youReachedText = Assets.strings.get("you_reached");
		String newUpgradeAvailableText = Assets.strings.get("new_upgrade_available");
		if (available.size() > 1) {
			newUpgradeAvailableText = LanguageManager.localizeNumber(newAvailableCount) + " " + Assets.strings.get("new_upgrades_available");
		}

		LabelStyle lite = Assets.generateLabelStyleUILight(14, youReachedText + newUpgradeAvailableText + Assets.nums);
		Label youveUnlocked = new Label(youReachedText, lite); 
		youveUnlocked.setAlignment(Align.center);
		youveUnlocked.setColor(MainStoreScreen.FONT_COLOR);
		topPart.add(youveUnlocked).center().expandX().width(unlocksTableWidth).fillX();
		topPart.row();

		//		if (unlock) {
		String levelText = Assets.strings.get("level");
		Label levelLabel = new Label(levelText +" "+ LanguageManager.localizeNumber(available.peek().unlockAtLevel()), Assets.generateLabelStyleUIChina(44, Assets.nums + levelText));
		levelLabel.setColor(MainStoreScreen.FONT_COLOR);
		levelLabel.setAlignment(Align.center);
		topPart.add(levelLabel).center().expandX().width(unlocksTableWidth).fillX();		

		unlocksTable.add(topPart).top();
		unlocksTable.row();

		subTable.add(unlocksTable).center().expandY().expandX().width(unlocksTableWidth);//.fillX();

		float padBelowLine = KebabKing.getGlobalYFloat(0.010f);

		Table topLine = new Table();
		topLine.setBackground(new TextureRegionDrawable(Assets.grayBlue));
		unlocksTable.add(topLine).height(1).width(unlocksTableWidth).expand().padBottom(padBelowLine);
		unlocksTable.row();

		if (LocationType.UNLOCKS_ONLY_WITH_LOCATIONS) {
			String locationText = Assets.strings.get("location_unlocked");

			Table middlePart = new Table();
			Label locationLabel = new Label(locationText, Assets.generateLabelStyleUILight(14, locationText));
			locationLabel.setColor(MainStoreScreen.FONT_COLOR);
			locationLabel.setAlignment(Align.left);	
			middlePart.add(locationLabel).padBottom(KebabKing.getGlobalYFloat(0.005f));
			middlePart.row();
			//		middlePart.debugAll();
			LocationType.Location locationUnlocked = null;
			for (Purchaseable p : available) {
				if (p.getType() == master.profile.inventory.locationType) {
					locationUnlocked = (LocationType.Location) p;
				}
			}

			if (locationUnlocked != null) {
				available.remove(locationUnlocked);
			}

			Table locationTable = generateLocationUnlockTable(locationUnlocked);
			middlePart.add(locationTable).width(unlocksTableWidth).left().padLeft(KebabKing.getGlobalX(0.02f)).padBottom(KebabKing.getGlobalY(0.015f));
			middlePart.row();

			unlocksTable.add(middlePart).top();
			unlocksTable.row();

			Table bottomLine = new Table();
			bottomLine.setBackground(new TextureRegionDrawable(Assets.grayBlue));
			unlocksTable.add(bottomLine).height(1).width(unlocksTableWidth).expand().padBottom(padBelowLine);
			unlocksTable.row();
		}

		Table bottomPart = new Table();
		//		bottomPart.debug();

		Label newAvailable = new Label(newUpgradeAvailableText, lite);
		newAvailable.setColor(MainStoreScreen.FONT_COLOR);
		newAvailable.setAlignment(Align.left);

		bottomPart.add(newAvailable).left().expandX().padBottom(KebabKing.getGlobalYFloat(0.005f));
		bottomPart.row();

		Table unlockedList = new Table();
		ScrollPane sp = new ScrollPane(unlockedList, Assets.getSPS());
		sp.setScrollingDisabled(true, false);

		bottomPart.add(sp).expandX().expandY();

		for (Purchaseable p : available) {
			Table item = generateUnlockItemTable(p);
			unlockedList.add(item).width(unlocksTableWidth).left().padLeft(KebabKing.getGlobalX(0.08f)).padBottom(KebabKing.getGlobalY(0.015f));
			unlockedList.row();
		}

		unlocksTable.add(bottomPart).top().expandY().expandX().fillX();

		Table goToMarket = getBlueButton(Assets.strings.get("go_to_market"), 34);
		subTable.row();
		subTable.add(goToMarket).height(KebabKing.getGlobalY(0.07f)).bottom().padBottom(KebabKing.getGlobalY(0.00f)).padTop(KebabKing.getGlobalYFloat(0.02f));
		goToMarket.setTouchable(Touchable.enabled);
		goToMarket.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				master.toStoreFrom(master.summary);
				DrawUI.exitNotification();
			}
		});

		addTaperedBottom();
		addContinueButton();
	}

	public static void addContinueButton() {
		addContinueButton(Assets.strings.get("continue"));
	}

	public static void addTaperedBottom() {
		// first add the little tapered white thing to the bottom
		Table bottom = new Table();
		bottom.setBackground(new TextureRegionDrawable(Assets.notificationBottom));
		Table buffer = new Table();
		buffer.setBackground(new TextureRegionDrawable(Assets.white));
		subTable.row();
		subTable.add(buffer).expandX().fillX().height(KebabKing.getGlobalYFloat(0.02f));
		subTableFull.row();
		subTableFull.add(bottom).expandX().fillX().height(KebabKing.getGlobalYFloat(0.026f));
	}

	public static void addContinueButton(String text) {
		Table continueButton = getBlueButton(text, 34);
		continueButton.addListener(new StrictInputListener() {
			public void touch(InputEvent event) {
				DrawUI.exitNotification();
			}
		});
		subTableFull.row();
		subTableFull.add(continueButton).top().height(KebabKing.getGlobalYFloat(0.07f)).expandY().padTop(KebabKing.getGlobalYFloat(0.02f));
	}

	public static Table generateLocationUnlockTable(Purchaseable purchaseable) {
		Table table = new Table();
		// prev .15
		int buttonWidth = KebabKing.getGlobalX(0.15f);
		// guarantees a square button
		int buttonHeight = buttonWidth;

		Table button = StorePurchaseableTable.generateIconBox(purchaseable, buttonWidth, buttonHeight, false, false, false);

		table.add(button).width(buttonWidth).height(buttonHeight).left();

		Table buttonCover = new Table();
		StorePurchaseableTable.updateButtonCover(buttonCover, false, false, false, buttonWidth, buttonHeight);
		button.add(buttonCover).fill();

		Table info = new Table();
		//		info.debugAll();

		float wid = KebabKing.getGlobalX(0.34f);

		//		String unlockName = "NEW LOCATION UNLOCKED";
		//		Label type = new Label(unlockName, Assets.generateLabelStyleUI(11, unlockName));
		//		type.setColor(MainStoreScreen.FONT_COLOR);
		//		type.setAlignment(Align.left);
		//		type.setWrap(true);
		//		info.add(type).left().expandX().width(wid);
		//		info.row();

		String nameField = purchaseable.getName();
		Label name = new Label(nameField, Assets.generateLabelStyleUILight(18, nameField));
		name.setColor(MainStoreScreen.FONT_COLOR);
		name.setAlignment(Align.left);
		name.setWrap(true);
		info.add(name).left().expandX().width(wid);
		info.row();

		Label desc = new Label(purchaseable.getDescription(), Assets.generateLabelStyleUILight(12, purchaseable.getDescription()));
		desc.setColor(MainStoreScreen.FONT_COLOR);
		desc.setAlignment(Align.left);
		desc.setWrap(true);
		info.add(desc).left().expandX().width(wid);
		info.row();

		int infoPad = KebabKing.getGlobalX(0.015f);
		table.add(info).expandX().left().padLeft(infoPad).fillX();

		table.setColor(new Color(1, 1, 1, 0));
		table.addAction(Actions.delay(0.5f, Actions.fadeIn(3)));

		//		table.debugAll();
		return table;
	}

	private static class TableWithPurchaseable extends Table {
		Purchaseable p;
	}

	public static Table generateUnlockItemTable(Purchaseable purchaseable) {
		TableWithPurchaseable table = new TableWithPurchaseable();
		table.p = purchaseable;

		// prev .15
		int buttonWidth = KebabKing.getGlobalX(0.125f);
		// guarantees a square button
		int buttonHeight = buttonWidth;

		Table button = StorePurchaseableTable.generateIconBox(purchaseable, buttonWidth, buttonHeight, false, false, false);

		table.add(button).width(buttonWidth).height(buttonHeight).left();

		Table buttonCover = new Table();
		StorePurchaseableTable.updateButtonCover(buttonCover, false, false, false, buttonWidth, buttonHeight);
		button.add(buttonCover).fill();

		Table info = new Table();
		//		info.debugAll();

		float wid = KebabKing.getGlobalX(0.34f);

		Label type = new Label(purchaseable.getType().name, Assets.generateLabelStyleUI(11, purchaseable.getType().name));
		type.setColor(MainStoreScreen.FONT_COLOR);
		type.setAlignment(Align.left);
		type.setWrap(true);
		info.add(type).left().expandX().width(wid);
		info.row();

		Label name = new Label(purchaseable.getName(), Assets.generateLabelStyleUILight(16, purchaseable.getName()));
		name.setColor(MainStoreScreen.FONT_COLOR);
		name.setAlignment(Align.left);
		name.setWrap(true);
		info.add(name).left().expandX().width(wid);
		info.row();

		Label desc = new Label(purchaseable.getDescription(), Assets.generateLabelStyleUILight(12, purchaseable.getDescription()));
		desc.setColor(MainStoreScreen.FONT_COLOR);
		desc.setAlignment(Align.left);
		desc.setWrap(true);
		info.add(desc).left().expandX().width(wid);
		info.row();

		int infoPad = KebabKing.getGlobalX(0.02f);
		table.add(info).expandX().left().padLeft(infoPad).fillX();

		table.setTouchable(Touchable.enabled);
		table.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				TableWithPurchaseable twp = (TableWithPurchaseable) event.getListenerActor();
				master.toStoreFrom(master.summary, twp.p);
				DrawUI.exitNotification();
			}
		});
		return table;
	}

	public static void resize() {
		if (bigTable != null)
			bigTable.setSize(KebabKing.getWidth(), KebabKing.getHeight());
	}

	private static void clearNotificationTableDontCallThis() {
		Drawable d = null;
		notificationTable.setBackground(d);
		notificationTable.clear();
	}

	public static void enqueueUnlockNotification(Queue<Purchaseable> p) {
		unlockDisplayQueue.add(p);
	}

	public static Table getBlueButton(String text, int size) {
		Table button = new Table();
		// THE KEY IS SET BACKGROUND!!!
		// SET BACKGROUND FUCKS UP PREFERRED SIZE!
		button.setTouchable(Touchable.enabled);
		Label resume = new Label(text, Assets.generateLabelStyleUIChina(size, text));

		float padRight = resume.getPrefWidth() * 0.35f;
		float padLeft = resume.getPrefWidth() * 0.25f;
		float padYTop = resume.getPrefHeight() * 0.3f;
		float padYBot = padYTop * 1.5f;
		if (Assets.getLanguage().extraPadding) {
			padYBot *= 0.3f;
			padYTop *= -0.0f;
			padRight *= 1.5f;
			padLeft *= 1.5f;
			System.out.println("adding extra padding: " + padLeft + text);
		}

		button.add(resume).padLeft(padLeft).padRight(padRight).padBottom(padYBot).padTop(padYTop);

		TextureRegion bg;
		if (button.getPrefWidth() > 300 && button.getPrefHeight() > 60) {
			//			System.out.println("USING 300");
			bg = Assets.getTextureRegion("screens/pause-052");
			button.getCell(resume).padLeft(padLeft * 0.6f).padRight(padRight * 0.7f);
		}
		//		else if (button.getPrefWidth() > 267 && button.getPrefHeight() > 90) {
		////			System.out.println("USING 267");
		//			bg = Assets.getTextureRegion("screens/pause-04");
		//		}
		//		else if (button.getPrefWidth() > 267 && button.getPrefHeight() > 80) {
		////			System.out.println("USING 267 80");
		//			bg = Assets.getTextureRegion("screens/pause-042");
		//		}
		else if (button.getPrefWidth() > 270 && button.getPrefHeight() > 60) {
			//			System.out.println("USING 270 60");
			bg = Assets.getTextureRegion("screens/pause-053");
		}
		else if (button.getPrefWidth() > 250 && button.getPrefHeight() > 40) {
			//			System.out.println("USING 250 40");
			bg = Assets.getTextureRegion("screens/pause-054");
		}
		//		else if (button.getPrefWidth() > 229 && button.getPrefHeight() > 60) {
		////			System.out.println("USING SUPER BIGGG");
		//			bg = Assets.getTextureRegion("screens/pause-055");
		//		}
		//		else if (button.getPrefWidth() > 208 && button.getPrefHeight() > 60) {
		////			System.out.println("USING SUPER BIGGG");
		//			bg = Assets.getTextureRegion("screens/pause-056");
		//		}
		//		else if (button.getPrefWidth() > 206 && button.getPrefHeight() > 63) {
		////			System.out.println("USING MEDD");
		//			bg = Assets.getTextureRegion("screens/Main-03");
		//		}
		else if (button.getPrefWidth() > 196 && button.getPrefHeight() > 86) {
			System.out.println("USING 196 86");
			bg = Assets.getTextureRegion("screens/blueSwipe");
		}
		else {
			System.out.println("USING SMALL");
			bg = Assets.getTextureRegion("screens/blueSwipeSm");			
		}
		button.setBackground(new TextureRegionDrawable(bg));

		//		button.debugAll();
		return button;
	}

	//	public static Table getBlueButton(String text, int size, String region) {
	//		Table button = new Table();
	//		TextureRegion bg = Assets.getTextureRegion(region);
	//		button.setBackground(new TextureRegionDrawable(bg));
	//		
	//		button.setTouchable(Touchable.enabled);
	//		Label resume = new Label(text, Assets.generateLabelStyleUIChina(size, text));
	//		button.add(resume).center();
	//		
	//		return button;
	//	}
}
