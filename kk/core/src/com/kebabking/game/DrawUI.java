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
import com.kebabking.game.Managers.Manager;
import com.kebabking.game.Purchases.Purchaseable;

// make it draw a little bar at the top!

/** Static class for drawing UI elements */
public class DrawUI {
	static final Color WHITE = new Color(1, 1, 1, 1);
	static final Color GRAY = new Color(.2f, .2f, .2f, .5f);
//	static final Color DARK_GRAY = new Color(.2f, .2f, .2f, .9f);
	
//	static final int BRIBE_COST = 10;
	
	static final Color FONT_COLOR = new Color(0f, .2f, .3f, 1);	
	
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
	
	static Stage uiStage;
	static KebabKing master;

	static InputProcessor pausedIP; // this is the stage that's happening in the background when a notification is launched
	
	static Table bigTable;
	static TopBar topBar;
	
	static Label dayEndLabel;
	
	static Color dayEndColor;
	
	static Table notificationTable;
	static Table subTable;
	static boolean notificationActive;
	static boolean policeNotificationActive;
	static boolean tutorialNotificationActive;

	static Color tintColor;

	static LinkedList<Queue<Purchaseable>> unlockDisplayQueue = new LinkedList<Queue<Purchaseable>>();
	static Purchaseable toSwitchToNext;
	
	static String currentString;
	static int prevTime;
		
	static Label violationTime;
	static int violationTimeMins;
	static int violationTimeSecs;
	
	static Projectile[] proj;
	
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
//		uiTable.debugAll();
		
//		uiTable.background(Assets.getTopBarBG());
		topBar.setPosition(0, KebabKing.getHeight() - barHeight - adBarHeight);
		topBar.setSize(KebabKing.getWidth(), barHeight + 5 + adBarHeight);
		
		topBar.padTop(KebabKing.getGlobalY(0.01f)).padLeft(KebabKing.getGlobalY(0.005f)).padRight(KebabKing.getGlobalY(0.005f));
		
		bigTable.add(topBar).top().height(barHeight + 5 + adBarHeight).expandY();
//		bigTable.debugAll();
		uiStage.addActor(bigTable);
			
		// screen is divided into 9 regions follows:
		if (KitchenScreen.LAST_CUSTOMER_MODE) {
			dayEndLabel = new Label(KitchenScreen.LAST_CUSTOMER_TEXT, Assets.generateLabelStyleUIChinaWhite(70, KitchenScreen.LAST_CUSTOMER_TEXT));
			dayEndLabel.setPosition(0, KebabKing.getGlobalY(0.55f));
		}
		else {
			dayEndLabel = new Label("", Assets.generateLabelStyleUIChinaWhite(100, Assets.nums));
			dayEndLabel.setPosition(0, KebabKing.getGlobalY(0.7f));
		}
		dayEndLabel.setWidth(KebabKing.getWidth());
		dayEndLabel.setAlignment(Align.center);
		
		dayEndColor = new Color(1, 1, 1, 1);
		
		notificationTable = new Table();
//		notificationTable.debug();
//		
		float notificationWidth = KebabKing.getGlobalX(0.9f);
		float notificationHeight = KebabKing.getGlobalY(0.8f);
//		
//		bigTable.debug();
		bigTable.row();
		bigTable.add(notificationTable).width(notificationWidth).height(notificationHeight).expandY().bottom().padBottom((KebabKing.getHeight() - notificationHeight)/2);;
		
//		launchUnlockNotification(new Purchaseable[] {master.profile.inventory.locationType.values[0], 
//								master.profile.inventory.drinkQuality.values[1],
//								master.profile.inventory.grillSpecs.getGrillSize().values[2]
//		});
//		launchUnlockNotification(new Purchaseable[] {master.profile.inventory.locationType.values[2], 
//		});
		
//		master.shutdownAt = System.currentTimeMillis();
//		launchPoliceNotification();
		
		proj = new Projectile[10];
	}

	
	public static void drawFullUI(float delta, SpriteBatch batch, Profile profile) {
		TopBar.update(delta, profile);
		
//		for (int i = 0; i < proj.length; i++) {
//			if (proj[i] == null) {
//				proj[i] = new Projectile(0, 0, false);
////				break;
//			}
//			if (proj[i] != null) {
//				proj[i].update(delta);
//				proj[i].draw(batch);
//				if (proj[i].shouldDestroy) proj[i] = null;
//			}
//		}
	
//		updateCoinCashStrings(delta, profile);

		AdsHandler.checkIfShouldReward();
		SocialMediaHandler.checkIfShouldReward();

		if (policeNotificationActive)
			updateViolationTime();
		
		batch.end();
		uiStage.act(delta);
		uiStage.draw();
		batch.begin();
//		Color c = batch.getColor();
//		batch.setColor(DARK_GRAY);
//		batch.draw(Assets.white, 0, ChuanrC.height - barHeight, ChuanrC.width, barHeight);
//		batch.setColor(c);
//		drawStars(batch, profile); // draw your reputation
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
		prepareNotification(0.7f);
		policeNotificationActive = true;
		preparePoliceNotificationTable();
	}

	public static void launchShareSuccessNotification(int coins) {
		System.out.println("launching share success notification for " + coins + " coins");
		prepareNotification(0.5f);
		prepareSuccessNotificationTable(coins, "Thanks for sharing!\n You've earned:");
	}

	public static void launchAdSuccessNotification(int coins) {
		// TODO
		System.out.println("launching ad success notification for " + coins + " coins");
		prepareNotification(0.5f);
		String text = "Thanks for watching!\n You've earned:";
		if (coins == 0) {
			text = "We'll let you off\n this time... Just don't\n let it happen again!";
		}
		prepareSuccessNotificationTable(coins, text);
	}

	public static void launchAdNotAvailableNotification() {
		if (notificationActive) {
			exitNotification();
//			throw new java.lang.AssertionError();
		};
		prepareNotification(0.6f);
		prepareAdNotAvailableNotificationTable();
	}
	
	private static void prepareTutorialNotification(float height) {
		notificationActive = true;
		tutorialNotificationActive = true;
		subTable = new Table();
		
//		notificationTable.debugAll();
		subTable.setBackground(new TextureRegionDrawable(Assets.notificationBG));
		float width = 0.7f;
		
//		float padTop = (1 - height) / 2f;
//		float padBot = padTop;
//		notificationTable.add(subTable).padLeft(KebabKing.getGlobalX(padX)).padRight(KebabKing.getGlobalX(padX)).padTop(KebabKing.getGlobalY(padTop)).padBottom(KebabKing.getGlobalY(padBot)).expandX().fill();//.top();

		Table topTable = DrawUI.generateTutorialTopTable();
		float topBarPadY = KebabKing.getGlobalY(0.03f);
		subTable.add(topTable).top().expandX().fillX().padBottom(topBarPadY).padTop(topBarPadY);
		subTable.row();
		
		notificationTable.add(subTable).width(KebabKing.getGlobalX(width)).height(KebabKing.getGlobalYFloat(height)).expandX().expandY().top().padTop(KebabKing.getGlobalYFloat(-0.03f));

	}
	
	public static Table generateTutorialTopTable() {
		Table topBar = new Table();
//		topBar.debugAll();
		Table topLeft = new Table();
		Table topRight = new Table();
		topLeft.setBackground(new TextureRegionDrawable(Assets.red));
		topRight.setBackground(new TextureRegionDrawable(Assets.red));
		
		Table topCenter = new Table();
		
		TextureRegion topImage = Assets.getTextureRegion("screens/Summary-02 (2)");
		float IMAGE_WIDTH = 0.15f;
		float IMAGE_HEIGHT = IMAGE_WIDTH * topImage.getRegionHeight() / topImage.getRegionWidth();
		Image image = new Image(topImage);
//		image.setWidth(KebabKing.getGlobalX(IMAGE_WIDTH));
//		image.setHeight(KebabKing.getGlobalY(IMAGE_HEIGHT));
		topCenter.add(image).width(KebabKing.getGlobalX(IMAGE_WIDTH)).height(KebabKing.getGlobalX(IMAGE_HEIGHT));
		topCenter.row();
		Label dailyAccounts = new Label("TUTORIAL", Assets.generateLabelStyleUIWhite(12, "TUTORIAL"));
		dailyAccounts.setColor(MainStoreScreen.FONT_COLOR);
	
		float BAR_HEIGHT = 0.005f;
		
		topCenter.add(dailyAccounts);
		topBar.add(topLeft).expandX().fillX().height(KebabKing.getGlobalY(BAR_HEIGHT));
		
		float imagePadX = KebabKing.getGlobalX(0.03f);
		topBar.add(topCenter).top().padLeft(imagePadX).padRight(imagePadX);;;
		topBar.add(topRight).expandX().fillX().height(KebabKing.getGlobalY(BAR_HEIGHT));;
		return topBar;
	}
	
	private static void prepareNotification(float height) {
		notificationActive = true;
		Gdx.input.setInputProcessor(uiStage);

		subTable = new Table();
		
//		notificationTable.debugAll();
		subTable.setBackground(new TextureRegionDrawable(Assets.notificationBG));
		float width = 0.7f;
		
//		float padTop = (1 - height) / 2f;
//		float padBot = padTop;
//		notificationTable.add(subTable).padLeft(KebabKing.getGlobalX(padX)).padRight(KebabKing.getGlobalX(padX)).padTop(KebabKing.getGlobalY(padTop)).padBottom(KebabKing.getGlobalY(padBot)).expandX().fill();//.top();
		notificationTable.add(subTable).width(KebabKing.getGlobalX(width)).height(KebabKing.getGlobalYFloat(height)).expandX().expandY();//.top();

		Table topTable = SummaryScreen.generateTopTable();
		float topBarPadY = KebabKing.getGlobalY(0.01f);
		subTable.add(topTable).top().expandX().fillX().padBottom(topBarPadY).padTop(topBarPadY);
		subTable.row();
	}
	
	
	public static void launchTutorialNotification(String title, String aboveImage, String belowImage, TextureRegion toDraw) {
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
		if (notificationActive) enqueueUnlockNotification(available);
		else {
//			launchNotification(unlocked.getName(), unlocked.getDescription(), unlocked.getIcon(), true, unlocked.unlockAtLevel());
			prepareNotification(0.6f);
			prepareUnlockNotificationTable(available);
		}
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

		subTable.add(titleLabel).center().expandX().fillX().padTop(KebabKing.getGlobalY(0f));
		subTable.row();
		
		if (aboveImage != null && aboveImage.length() > 0) {
			Label above = new Label(aboveImage, Assets.generateLabelStyleUILight(16, aboveImage));
			above.setAlignment(Align.center);
			above.setColor(MainStoreScreen.FONT_COLOR);
			above.setWrap(true);

			subTable.add(above).center().expandX().fillX().padTop(KebabKing.getGlobalYFloat(0.0f));
			subTable.row();
		}
		
		if (toDraw != null) {
			float regHeight = KebabKing.getGlobalYFloat(0.07f);
//			float regHeight = toDraw.getRegionHeight() * regWidth / toDraw.getRegionWidth();
			float regWidth = toDraw.getRegionWidth() * regHeight / toDraw.getRegionHeight();
			Image image = new Image(toDraw);
			subTable.add(image).center().expandX().width(regWidth).height(regHeight).top().padTop(KebabKing.getGlobalYFloat(0.02f));
			subTable.row();
		}
		
		Label below = new Label(belowImage, Assets.generateLabelStyleUILight(16, belowImage));
		below.setAlignment(Align.center);
		below.setColor(MainStoreScreen.FONT_COLOR);
		below.setWrap(true);
		subTable.add(below).center().expandX().fillX().padTop(KebabKing.getGlobalYFloat(0.02f)).expandY().top();
		subTable.row();
	}
	
	private static void preparePoliceNotificationTable() {
		Label obey = new Label("The police have \n shut you down!", Assets.generateLabelStyleUILight(24, "The police have \n shut you down!"));
		obey.setAlignment(Align.center);
		obey.setColor(MainStoreScreen.FONT_COLOR);
		subTable.add(obey).center().expandX().fillX().padTop(KebabKing.getGlobalY(0.03f));
		subTable.row();
//		subTable.debug();
		TextureRegion reg = Customer.CustomerType.POLICE.walkDown.getKeyFrame(0);
		float policeWidth = KebabKing.getGlobalX(0.3f);
		float policeHeight = reg.getRegionHeight() * policeWidth / reg.getRegionWidth();
		Image police = new Image(reg);
		subTable.add(police).center().expandX().width(policeWidth).height(policeHeight).top().padTop(KebabKing.getGlobalY(0)).padBottom(KebabKing.getGlobalY(-0.01f));
		subTable.row();
		
		Label sick = new Label("Customers are getting sick from your kebabs!\nMake sure to cook your meat thoroughly, \n and consider investing in better quality meat!", Assets.generateLabelStyleUILight(12, "Customers are getting sick from your kebabs!\nMake sure to cook your meat thoroughly, \n and consider investing in better quality meat!"));
		sick.setAlignment(Align.center);
		sick.setColor(MainStoreScreen.FONT_COLOR);
		subTable.add(sick).center().expandX().fillX();
		subTable.row();
		
		Table violationTable = new Table();
		Label expires = new Label("Violation expires in: ", Assets.generateLabelStyleUIHeavyWhite(16, "Violation expires in: "));
		expires.setAlignment(Align.center);
		expires.setColor(MainStoreScreen.FONT_COLOR);
		
		violationTime = new Label("", Assets.generateLabelStyleUIHeavyWhite(16, Assets.nums + ":"));
		updateViolationTime();
		violationTime.setAlignment(Align.center);
		violationTime.setColor(MainStoreScreen.FONT_COLOR);
		
		violationTable.add(expires).expandX().right();
		violationTable.add(violationTime).expandX().left();
		
		subTable.add(violationTable).center().expandX().fillX().padTop(KebabKing.getGlobalY(0.05f));
		subTable.row();
		
		Label or = new Label("- OR -", Assets.generateLabelStyleUIHeavyWhite(20,  "- OR -"));
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
		
		Label bribe = new Label("BRIBE", Assets.generateLabelStyleUIChinaWhite(28, "BRIBE"));	
		Table bribeTable = new Table();
		
		boolean canAfford = true;
		
		if (canAfford)
			bribeTable.setBackground(new TextureRegionDrawable(Assets.marketGreen));
		else
			bribeTable.setBackground(new TextureRegionDrawable(Assets.grayLight));
			
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
		
//		priceCoinsTable.add(priceCoins).padLeft(KebabKing.getGlobalX(-0.005f)).padRight(KebabKing.getGlobalX(0.01f)).fill();
		priceCoinsTable.add(priceCoins).width(height*0.8f).height(height*0.8f);

		bribeButton.add(priceCoinsTable);
		
		subTable.add(bribeButton).height(height).expandY().top();
		
		addContinueButton("WAIT");
	}
	
	private static void attemptBribe() {
		System.out.println("bribe was pressed");
//		if (master.profile.getCoins() >= BRIBE_COST) {
//		if (Manager.ads.showAd())
//			master.profile.spendCoins(BRIBE_COST);
//			master.shutdownAt = Long.MAX_VALUE;
//			DrawUI.exitNotification();
//		}
//		else {
//			System.out.println("Not enough money");
//		}
		Manager.ads.showAd();
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
			return violationTimeMins + ":" +"0" + violationTimeSecs;
		}
		return violationTimeMins + ":" + violationTimeSecs;
	}
	
	private static int getViolationRemainingMins() {
		return (int) (master.profile.getViolationSecondsRemaining() / 60);
	}
	
	private static int getViolationRemainingSecs() {
		return (int) (master.profile.getViolationSecondsRemaining() % 60);
	}

	// if coins == 0, assume this was for a violation
	// otherwise it was for jade
	private static void prepareSuccessNotificationTable(int coins, String text) {
		Label youveEarned = new Label(text, Assets.generateLabelStyleUILight(24, text));
		youveEarned.setAlignment(Align.center);
		youveEarned.setColor(MainStoreScreen.FONT_COLOR);
		float padTop = 0.06f;
		if (coins == 0) padTop = 0.02f;
		subTable.add(youveEarned).center().expandX().fillX().padTop(KebabKing.getGlobalY(padTop)).top();
		
		subTable.row();
		
		if (coins > 0) {
			Table jadeTable = new Table();

			Label count = new Label(coins + "x", Assets.generateLabelStyleUIWhite(44, Assets.nums + "x"));
			count.setColor(MainStoreScreen.FONT_COLOR);

			jadeTable.add(count).expandX().right();

			float jadeWidth = KebabKing.getGlobalX(0.2f);
			float jadeHeight = jadeWidth;
			Image jadeIcon = new Image(Assets.getTextureRegion("market/Jeweler-09"));
			jadeTable.add(jadeIcon).width(jadeWidth).height(jadeHeight).expandX().left().padLeft(-KebabKing.getGlobalX(0.04f));

			subTable.add(jadeTable).top().expandY().padTop(KebabKing.getGlobalY(0.025f));
		}
		else {
			TextureRegion reg = Customer.CustomerType.POLICE.walkDown.getKeyFrame(0);
			float policeWidth = KebabKing.getGlobalX(0.3f);
			float policeHeight = reg.getRegionHeight() * policeWidth / reg.getRegionWidth();
			Image police = new Image(reg);
			subTable.add(police).center().expandX().width(policeWidth).height(policeHeight).top().padTop(KebabKing.getGlobalY(0.02f)).padBottom(KebabKing.getGlobalY(-0.01f)).expandY().top();
			subTable.row();
		}
		
		addContinueButton();
	}

	private static void prepareAdNotAvailableNotificationTable() {
		Label noVideos = new Label("No videos available!\n Check your internet \nconnection.", Assets.generateLabelStyleUILight(24, "No videos available!\n Check your internet connection."));
		noVideos.setAlignment(Align.center);
		noVideos.setColor(MainStoreScreen.FONT_COLOR);
		subTable.add(noVideos).center().expandX().fillX().padTop(KebabKing.getGlobalY(0.08f)).top().expandY();
		
		subTable.row();
		
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
		
		LabelStyle lite = Assets.generateLabelStyleUILight(16, "You reached new available upgrades:" + Assets.nums);
		Label youveUnlocked = new Label("You reached", lite); 
		youveUnlocked.setAlignment(Align.center);
		youveUnlocked.setColor(MainStoreScreen.FONT_COLOR);
		topPart.add(youveUnlocked).center().expandX().width(unlocksTableWidth).fillX();
		topPart.row();
		
//		if (unlock) {
		Label levelLabel = new Label("LEVEL " + available.peek().unlockAtLevel(), Assets.generateLabelStyleUIChinaWhite(44, Assets.nums + "LEVEL"));
		levelLabel.setColor(MainStoreScreen.FONT_COLOR);
		levelLabel.setAlignment(Align.center);
		topPart.add(levelLabel).center().expandX().width(unlocksTableWidth).fillX();		
		
		unlocksTable.add(topPart).top();
		unlocksTable.row();
		
		subTable.add(unlocksTable).center().expandY().expandX().width(unlocksTableWidth);//.fillX();
	
		Table bottomPart = new Table();
//		bottomPart.debug();
		
		
		String toPrint = "New upgrade available:";
		if (available.size() > 1) {
			toPrint = available.size() + " new upgrades available:";
		}
		Label newAvailable = new Label(toPrint, lite);
		newAvailable.setColor(MainStoreScreen.FONT_COLOR);
		newAvailable.setAlignment(Align.left);
		
		float padY = KebabKing.getGlobalYFloat(0.02f);
		bottomPart.add(newAvailable).left().expandX().padTop(padY).padBottom(padY);
		bottomPart.row();
		
		Table unlockedList = new Table();
		ScrollPane sp = new ScrollPane(unlockedList, Assets.getSPS());
		sp.setScrollingDisabled(true, false);
		
		bottomPart.add(sp).expandX().expandY();
		
		for (Purchaseable p : available) {
			Table item = generateUnlockItemTable(p);
			unlockedList.add(item).width(unlocksTableWidth).left().padLeft(KebabKing.getGlobalX(0.08f)).padBottom(KebabKing.getGlobalY(0.015f));
			unlockedList.row();
			
			// this is a hacky but kinda fun way to solve the problem that buttons don't have access to external info
			toSwitchToNext = p;
		}
		
		unlocksTable.add(bottomPart).top().expandY().expandX().fillX();

		Table goToMarket = getBlueButton("MARKET", 34);
		subTable.row();
		subTable.add(goToMarket).height(KebabKing.getGlobalY(0.08f)).bottom().width(KebabKing.getGlobalX(0.5f)).padBottom(KebabKing.getGlobalY(0.05f));
		goToMarket.setTouchable(Touchable.enabled);
		goToMarket.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				System.out.println("goToMarket");
				master.toStoreFrom(master.summary, toSwitchToNext);
				DrawUI.exitNotification();
			}
		});
		
		addContinueButton();
	}

	public static void addContinueButton() {
		addContinueButton("CONTINUE");
	}
	
	public static void addContinueButton(String text) {
		Table continueButton = getBlueButton(text, 34, "screens/pause-04");
		continueButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
//				event.handle();
//				event.stop();
				return true;
			}
			public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
				DrawUI.exitNotification();
			}
		});

//		notificationTable.debugAll();
		notificationTable.row();
		notificationTable.add(continueButton).top().height(KebabKing.getGlobalYFloat(0.07f)).expandY();
		
		notificationTable.getCell(subTable).expand(true, false);
	}
	
	public static Table generateUnlockItemTable(Purchaseable purchaseable) {
//		int imagePadX, imagePadY;
		Table table = new Table();
//		Table button = new Table();
//		button.setBackground(new NinePatchDrawable(Assets.gray9PatchSmall));
//
//		int buttonHeight = KebabKing.getGlobalX(0.15f);
//		int buttonWidth = buttonHeight;
//		
//		imagePadX = Assets.PATCH_OFFSET_X * 2 / 3;
//		imagePadY = (int) (Assets.PATCH_OFFSET_Y * 2.5f);
//
//		TextureRegion full = purchaseable.getIcon();
//		if (full == null) {
//			full = Assets.questionMark;
//		}
//
//		int iconWidth = buttonWidth - imagePadX;
//		int iconHeight = buttonHeight - imagePadY;
//
//		// if the icon is wider than long, crop out appropriate part of image
//
//		// crop to that aspect ratio
//		int regWidth = full.getRegionWidth();
//		int regHeight = full.getRegionHeight(); 
//		float aspectButton = (iconWidth) * 1.0f / (iconHeight); 
//
//		TextureRegion half;
//		if (regWidth / regHeight > aspectButton) {
//			//			System.out.println("reg Width > regHeight" + regWidth + " , " + regHeight);
//			float cropWidth = (aspectButton * regHeight);
//			half = new TextureRegion(full, (int) (regWidth/2 - cropWidth/2), 0, (int) cropWidth, full.getRegionHeight());
//		}
//		// TODO when region is taller than wide, make this cleverly allow less vertical padding so it fits well inside the box
//		else {
//			float cropHeight = regHeight/aspectButton;
//			half = new TextureRegion(full, 0, (int) (regHeight/2 - cropHeight/2), full.getRegionWidth(), (int) cropHeight);
//		}
//		Image icon = new Image(half);		
//		button.add(icon).center().width(iconWidth).height(iconHeight);

		// prev .15
		int buttonWidth = KebabKing.getGlobalX(0.15f);
		// guarantees a square button
		int buttonHeight = buttonWidth;
		Table button = StorePurchaseTypeSubtable.generateIconBox(purchaseable, buttonWidth, buttonHeight, false, false, false);
		
		table.add(button).width(buttonWidth).height(buttonHeight).left();

		Table info = new Table();
//		info.debugAll();
		
		float wid = KebabKing.getGlobalX(0.3f);
		
		Label type = new Label(purchaseable.getType().name, Assets.generateLabelStyleUIWhite(12, Assets.upper));
		type.setColor(MainStoreScreen.FONT_COLOR);
		type.setAlignment(Align.left);
		type.setWrap(true);
		info.add(type).left().expandX().width(wid);
		info.row();
		
		Label name = new Label(purchaseable.getName(), Assets.generateLabelStyleUILight(18, Assets.alpha));
		name.setColor(MainStoreScreen.FONT_COLOR);
		name.setAlignment(Align.left);
		name.setWrap(true);
		info.add(name).left().expandX().width(wid);
		info.row();

		Label desc = new Label(purchaseable.getDescription(), Assets.generateLabelStyleUILight(14, Assets.allChars));
		desc.setColor(MainStoreScreen.FONT_COLOR);
		desc.setAlignment(Align.left);
		desc.setWrap(true);
		info.add(desc).left().expandX().width(wid);
		info.row();
		
		int infoPad = KebabKing.getGlobalX(0.03f);
		table.add(info).expandX().left().padLeft(infoPad).fillX();
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
		TextureRegion bg = Assets.getTextureRegion("screens/pause-03");
		button.setBackground(new TextureRegionDrawable(bg));
		
		button.setTouchable(Touchable.enabled);
		Label resume = new Label(text, Assets.generateLabelStyleUIChinaWhite(size, text));
		button.add(resume).center().padRight(KebabKing.getGlobalX(0.025f * size / 24)).padBottom(KebabKing.getGlobalY(0.004f * size / 24));
		
		return button;
	}
	
	public static Table getBlueButton(String text, int size, String region) {
		Table button = new Table();
		TextureRegion bg = Assets.getTextureRegion(region);
		button.setBackground(new TextureRegionDrawable(bg));
		
		button.setTouchable(Touchable.enabled);
		Label resume = new Label(text, Assets.generateLabelStyleUIChinaWhite(size, text));
		button.add(resume).center();
		
		return button;
	}
}
