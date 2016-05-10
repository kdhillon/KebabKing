package com.kebabking.game;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kebabking.game.Managers.Manager;
import com.kebabking.game.Purchases.PurchaseType;
import com.kebabking.game.Purchases.Purchaseable;

public class SummaryScreen extends ActiveScreen {
	final static Color RED = new Color(193/256.0f, 39/256.0f, 45/256.0f, 1f);
	
	final static int SHARE_REWARD = 3;
	
	// Time to display "Day Complete"
	static float TIME_TO_WAIT = 1.5f;
	
	static float EXP_ANIMATE_TIME = 1.0f;

	static final float BAR_HEIGHT = 0.015f;
	static final float NEG_PAD = 0.002f;
	
	static final float ALPHA_GOAL = DrawUI.GRAY_ALPHA;
	
	static final float PROFIT_TO_EXP_RATE = 1.8f;

	public static int adsWatchedThisSession = 0;

	Table bigTable;
	Table continueButton;
	
	KitchenScreen kitchen;
	
	float timeElapsed;
//	Label dayComplete;
	Table dayComplete;
	
	float revenue;
	float expenses;
	float meatExpenses;
	float rent;
	float profit;
	boolean profitRecord;
	
	int customersServed;
	int sickCount;
	float rating; // between 0.5 and 5.0
	
	int lvlDisplayed;
	int expRemaining; // don't use this anymore.
	int expDisplayed;
	String expString;  // corresponds to above
	
	// in case we want to have stars pop on.
	Table starTable;
	float currentStarCount;
	
	boolean dayCompleteDone = false; // used to switch to info from "day complete"
//	boolean grantingExpDone = false; // used to switch to info from "day complete"

	float alpha = 0;
	
	Table expTable;
	Table expBar;
	Table greenBar;
	Table grayBar;
	Label exp;
	Label levelLeft;
	Label levelRight;
	LabelStyle lvlLs;

	int levelAtStart;
	
	Table share;
	
	float infoWidth;
	
	boolean justUnlockedSomething;
	
	public SummaryScreen(KebabKing master, KitchenScreen kitchen) {
		super(master, true, "Summary");
		this.kitchen = kitchen;

		grill.deactivate(); // deactivate grill
		cm.deactivate();
		bg.deactivate();

		ScreenViewport viewport = new ScreenViewport();
		uiStage = new Stage(viewport, batch);
		
		bigTable = new Table();
		
		bigTable.setSize(KebabKing.getWidth(), KebabKing.getHeight());
		uiStage.addActor(bigTable);
		
		dayComplete = getDayCompleteButton();
//		TextureRegion bg = Assets.getTextureRegion("screens/pause-03");		
//		float dayWidth = KebabKing.getGlobalX(0.9f);
//		float dayHeight = dayWidth * bg.getRegionHeight() / bg.getRegionWidth();
		bigTable.add(dayComplete).center();//.height(KebabKing.getGlobalY(0.1f)).width(KebabKing.getGlobalX(1f));
		
//		dayComplete.setPosition(0, KebabKing.getGlobalY(0.45f));

		this.revenue = kitchen.totalRevenue;
		//		this.expenses = kitchen.moneySpentToday - refund;
//		this.expenses = kitchen.meatExpenses + rent;
		
		// TODO this line is legendary but a bit problematic
		this.rent = kitchen.master.profile.getLocation().getDailyCost();
		
		// TODO make sure to catch negative money case (pay to play)
		this.meatExpenses = kitchen.meatExpenses;
		
		this.expenses = meatExpenses + rent;

		// update profit
		//		this.profit =  master.profile.cash - getProfile().cash - rent;
		this.profit = revenue - expenses;
		System.out.println("Profit: " + profit);
		
		// customers served:
		this.customersServed = kitchen.cm.totalCustomers;

		// sicknesses
		this.sickCount = cm.totalSick;

		this.rating = kitchen.calculateReputation();

		this.levelAtStart = master.profile.getLevel();
		
		updateProfile(master.profile, rating, revenue, expenses);
		
		this.expRemaining = this.calcExpForProfit(profit);
		
		if (KebabKing.EXP_CITY) 
			this.expRemaining += 500;
				
		addExpAndSave(expRemaining, master.profile);
		
		if (kitchen.wasShutDown) {
			DrawUI.launchPoliceNotification();
			SoundManager.playShutdown();
		}
		else {
			SoundManager.playDayComplete();
		}
		
		// Prepare an ad to be watched
		Manager.ads.cacheAd();
		
	}
	
	public void updateProfile(Profile profile, float rating, float revenue, float expenses) {
		profile.stats.dayEnd(rating, revenue, expenses);
		profile.addRepuation(rating);
		this.profitRecord = profile.stats.isRecord(revenue - expenses);
	}
	
	public void addExpAndSave(int exp, Profile profile) {
		// before we do anything with actual profile, store the previous values
		expDisplayed = profile.getExp();
		lvlDisplayed = profile.getLevel();
		
		StatsHandler.updateStatsFor(this);
		
		// now actually save stuff to the profile. All other
		profile.giveExp(exp);
		profile.save();
	}
	
	// only reason to do this after day complete is so that player can see it decreasing
	public void subtractExpenses() {
		master.profile.subtractDailyExpenses();
	}
	
	public Table getDayCompleteButton() {
		Table button = DrawUI.getBlueButton(Assets.strings.get("day_complete"), 44);
		
		button.setColor(new Color(1, 1, 1, 0));
		button.addAction(Actions.fadeIn(0.5f));
	
		button.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
				skipWaiting();
			}
		});	
		
		return button;
	}
	
//	public Table getShutDownButton() {
//		return new Table();
//	}
	
	public void skipWaiting() {
		timeElapsed = TIME_TO_WAIT;
	}
	
	public void initializeTable() {
		System.out.println("initializing table");
		bigTable.clear();
//		bigTable.debugAll();
		float topBarPadY = KebabKing.getGlobalY(0.02f);
		
		Table window = new Table();
		bigTable.add(window).width(KebabKing.getGlobalX(0.95f)).center().expandY().padTop(KebabKing.getGlobalYFloat(0.05f));
		
		Table subTableFull = new Table();
		Table subTable = new Table();
		subTableFull.add(subTable).expand().fill();
		subTable.setBackground(new TextureRegionDrawable(Assets.white));

		float padX = 0.08f;
		float padTop = 0.1f;
		float padBot = 0.09f;
		window.add(subTableFull).padLeft(KebabKing.getGlobalX(padX)).padRight(KebabKing.getGlobalX(padX)).padTop(KebabKing.getGlobalY(padTop)).padBottom(KebabKing.getGlobalY(padBot)).expand().fill();//.top();	
		
		Table topBar = generateTopTable();
		subTable.add(topBar).top().expandX().fillX().padBottom(topBarPadY).padTop(topBarPadY);
		subTable.row();
		
		float padShareY = KebabKing.getGlobalY(0.02f);
		
		if (SocialMediaHandler.checkIfShouldAllowShare()) {
			share = generateShareTable();
			//		share.debugAll();
			subTable.add(share).top().expandX().padBottom(padShareY);
			subTable.row();
		}
		
		float infoPadBetween = KebabKing.getGlobalY(0.005f);
		Table infoTable = new Table();
		Table revenueTable = generateTable("revenue", revenue, true, new String[]{}, new float[]{});
		infoTable.add(revenueTable).expandX().fillX().padBottom(infoPadBetween).colspan(2);
		infoTable.row();
		Table expenseTable = generateTable("expenses", expenses, true, new String[]{"kebabs", "rent"}, new float[]{meatExpenses, rent});
		infoTable.add(expenseTable).expandX().fillX().padBottom(infoPadBetween).colspan(2);
		infoTable.row();
		Table customerTable = generateTable("customers", customersServed, false, new String[]{"sick"}, new float[]{sickCount});
		infoTable.add(customerTable).expandX().fillX().padBottom(infoPadBetween).colspan(2);
		infoTable.row();
		
		Table rep = generateReputationTable();
//		rep.debugAll();
		infoTable.add(rep).expandX().fillX().fillY().padRight(infoPadBetween).padBottom(infoPadBetween*2);
		Table prof = generateProfitTable();
//		prof.debugAll();
		infoTable.add(prof).expandX().fillX().padBottom(infoPadBetween*2);
		
		this.infoWidth = KebabKing.getGlobalX(0.6f);
		
//		infoTable.debugAll();
		
		Table exp = generateLevelTable();
		infoTable.row();
		infoTable.add(exp).width(infoWidth).colspan(2);
		
		subTable.add(infoTable).top().width(infoWidth).expandY().center().padBottom(KebabKing.getGlobalXFloat(0.05f));
		subTable.row();
		
//		Table jadeTable = generateJadeTable();
//		float height = KebabKing.getGlobalY(0.2f);
//		float width = height * Assets.getTextureRegion("market/wheel_solo_stand").getRegionWidth() / Assets.getTextureRegion("market/wheel_solo_stand").getRegionHeight();
//		float jadePad = KebabKing.getGlobalY(0.01f);
//		subTable.add(jadeTable).expandY().width(width).height(height).padTop(0).padBottom(jadePad*1);
	
		
		Table bottom = new Table();
		bottom.setBackground(new TextureRegionDrawable(Assets.notificationBottom));
		subTableFull.row();
		subTableFull.add(bottom).expandX().fillX().height(KebabKing.getGlobalYFloat(0.026f));
		
//		subTableFull.debugAll();
		
		// new lines give this extra volume for clicking (hacky)
//		Label nextRound = new Label("\nnext round >>\n", Assets.generateLabelStyleUIWhite(30, "\nnext round>"));
//		nextRound.setColor(RED);
	}
	
	public static Table generateTopTable() {
		Table topBar = new Table();
//		topBar.debugAll();
		Table topLeft = new Table();
		Table topRight = new Table();
		topLeft.setBackground(new TextureRegionDrawable(Assets.red));
		topRight.setBackground(new TextureRegionDrawable(Assets.red));
		
		Table topCenter = new Table();
		
		TextureRegion topImage = Assets.getTextureRegion("screens/Summary-02");
		float IMAGE_WIDTH = 0.2f;
		float IMAGE_HEIGHT = IMAGE_WIDTH * topImage.getRegionHeight() / topImage.getRegionWidth();
		Image image = new Image(topImage);
//		image.setWidth(KebabKing.getGlobalX(IMAGE_WIDTH));
//		image.setHeight(KebabKing.getGlobalY(IMAGE_HEIGHT));
		topCenter.add(image).width(KebabKing.getGlobalX(IMAGE_WIDTH)).height(KebabKing.getGlobalX(IMAGE_HEIGHT));
		topCenter.row();
		String daily = Assets.strings.get("daily_accounts");
		Label dailyAccounts = new Label(daily, Assets.generateLabelStyleUI(12, daily));
		dailyAccounts.setColor(MainStoreScreen.FONT_COLOR);
	
		topCenter.add(dailyAccounts);
		topBar.add(topLeft).expandX().fillX().height(KebabKing.getGlobalY(BAR_HEIGHT));
		
		float imagePadX = KebabKing.getGlobalX(0.03f);
		topBar.add(topCenter).top().padLeft(imagePadX).padRight(imagePadX);;;
		topBar.add(topRight).expandX().fillX().height(KebabKing.getGlobalY(BAR_HEIGHT));;
		return topBar;
	}
	
	public Table generateShareTable() {
		Table shareMain = new Table();
		
		float height = KebabKing.getGlobalYFloat(0.03f);
//		float height = Assets.facebook.getRegionHeight();
		float width = Assets.facebook.getRegionWidth() * height / Assets.facebook.getRegionHeight();

		Table shareFb = new Table();
		shareFb.setBackground(new TextureRegionDrawable(Assets.facebook));
		shareMain.add(shareFb).width(width).height(height).expandX();
		
		Table rightTable = new Table();
		String text = "+" + LanguageManager.localizeNumber(SHARE_REWARD);
		Label shareValue = new Label(text, Assets.generateLabelStyleUIHeavy(16, text));
		shareValue.setColor(MainStoreScreen.FONT_COLOR_GREEN);
		rightTable.add(shareValue);
		
//		Image minijade = new Image(Assets.bigjade);
		Image minijade = new Image(Assets.minijade);
		rightTable.add(minijade).height(height * 0.8f).width(height * 0.8f).padLeft(height * 0.2f);
		shareMain.add(rightTable).height(height).padLeft(KebabKing.getGlobalX(0.01f));
		
		shareMain.setTouchable(Touchable.enabled);
		shareMain.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				clickShareButton();
			}
		});	
		
		return shareMain;
	}
	
	public void clearShareTable() {
		share.clear();
	}
	
	public Table generateTable(String mainLabel, float mainValue, boolean money, String[] labels, float[] values) {
		if (labels.length != values.length) throw new java.lang.AssertionError();
		
		Table table = new Table();
		table.setBackground(new NinePatchDrawable(Assets.gray9PatchSmallThin));
		
		String main = Assets.strings.get(mainLabel);
		Label title = new Label(main, Assets.generateLabelStyleUIHeavy(14, main));	
		title.setColor(MainStoreScreen.FONT_COLOR);
		String toPrint;
		if (money) toPrint = Assets.getCurrency() + LanguageManager.localizeNumber(mainValue);
		else toPrint = "" + LanguageManager.localizeNumber((int) mainValue);
		Label value = new Label(toPrint, Assets.generateLabelStyleUIHeavy(14, toPrint));
		value.setColor(MainStoreScreen.FONT_COLOR);
		table.add(title).left().expandX();
		table.add(value).right().expandX();
		
		
		float indentPad = KebabKing.getGlobalX(0.1f);
		for (int i = 0; i < labels.length; i++) {
			table.row();
			String subLab = Assets.strings.get(labels[i]);
			Label lab = new Label(subLab, Assets.generateLabelStyleUILight(14, subLab));
			lab.setColor(MainStoreScreen.FONT_COLOR);
			if (money) toPrint = Assets.getCurrency() + LanguageManager.localizeNumber(values[i]);
			else toPrint = "" + LanguageManager.localizeNumber((int) values[i]);
			Label val = new Label(toPrint, Assets.generateLabelStyleUILight(14, Assets.nums));
			val.setColor(MainStoreScreen.FONT_COLOR);
			table.add(lab).left().expandX().padLeft(indentPad);
			table.add(val).right().expandX();
		}
		return table;
	}
	
	public Table generateReputationTable() {
		Table table = new Table();
		table.setBackground(new NinePatchDrawable(Assets.gray9PatchSmallThin));
		
		float negPadY = -KebabKing.getGlobalY(NEG_PAD);
		
		String repString = Assets.strings.get("daily_rating");
		Label reputationLabel = new Label(repString, Assets.generateLabelStyleUI(14, repString));
		reputationLabel.setColor(TopBar.REPUTATION_FONT_COLOR);
		table.add(reputationLabel).center().padTop(negPadY);
		table.row();

		starTable = new Table();
		table.add(starTable).width(KebabKing.getGlobalX(0.27f)).center().padBottom(negPadY);		
		
		updateStarTable(this.rating);
		
		return table;
	}
	
	public void updateStarTable(float starCount) {
		if (currentStarCount != starCount) {
			starTable.clear();
			
			// calculate number of gray stars
			int grayStars = (int) (5 - starCount);
					
			// star width is determined by size of star area
			float starHeight = KebabKing.getGlobalY(0.03f);
			
//			float rightPad = KebabKing.getGlobalX(TopBar.STAR_PAD);
//			float leftPad = KebabKing.getGlobalX(TopBar.STAR_PAD);
			
			float rightPad, leftPad;
			rightPad = leftPad = 0;
			
			while (starCount >= 1) {
				Image star = new Image(Assets.getStar());
				starTable.add(star).height(starHeight).padRight(rightPad).padLeft(leftPad).expandX();
				starCount--;
			}
			
			// draw half star
			if (starCount > 0) {
				Image star = new Image(Assets.getHalfStar());
				starTable.add(star).height(starHeight).padRight(rightPad).padLeft(leftPad).expandX();
			}
			
			// draw gray stars
			for (int i = 0; i < grayStars; i++) {
				Image star = new Image(Assets.getGrayStar());
				starTable.add(star).height(starHeight).padRight(rightPad).padLeft(leftPad).expandX();
			}
			
			currentStarCount = starCount;
		}
	}
	
	public Table generateProfitTable() {
		Table table = new Table();
		
		table.setBackground(new NinePatchDrawable(Assets.red9PatchSmall));
		
		float negPadY = -KebabKing.getGlobalY(NEG_PAD);
		
		String profitText = Assets.strings.get("profit");
		Label profitLabel = new Label(profitText, Assets.generateLabelStyleUI(12, profitText));
		profitLabel.setColor(RED);
		table.add(profitLabel).center().padTop(negPadY);
		table.row();

		Label valueLabel = new Label(Assets.strings.get("currency") + LanguageManager.localizeNumber(this.profit), Assets.generateLabelStyleUI(24, Assets.nums + "-"));
		valueLabel.setColor(RED);
		table.add(valueLabel).center().padBottom(negPadY);
		
		return table;
	}
	
//	public Table generateJadeTable() {
//		Table table = new Table();
//		table.setTouchable(Touchable.enabled);
//		table.addListener(new InputListener() {
//			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
//				return true;
//			}
//			public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
//				clickGoToWheel();
//			}
//		});	
//		
//		// probably add this as an image
//		table.setBackground(new TextureRegionDrawable(Assets.getTextureRegion("market/wheel_solo_stand")));
//		
////		Image playButton = new Image(Assets.getTextureRegion("market/Market_menu_element-09"));
////		Image playButton = new Image();
////		float playWidth = KebabKing.getGlobalX(0.06f);
////		table.add(playButton).width(playWidth).height(playWidth).bottom().expandY().padBottom(KebabKing.getGlobalY(0.003f));
////		table.row();
//		
//		String earnJadeText = Assets.strings.get("earn_jade");
//		Label earnJade = new Label(earnJadeText, Assets.generateLabelStyleUIChina(22, earnJadeText));
//		table.add(earnJade).center().padBottom(KebabKing.getGlobalY(0.045f)).padRight(KebabKing.getGlobalX(0.02f));
////		table.debugAll();
//		return table;
//	}
	
	public Table generateLevelTable() {
		Table table = new Table();

		float height = KebabKing.getGlobalY(0.03f);

//		float expPercent = expDisplayed *1.0f / master.profile.getExpNeededFor(lvlDisplayed + 1);
////		float expPercent = 0.05f;
//		System.out.println("exp percent:" + expPercent);

		expTable = new Table();
		
		expBar = new Table();
				
		greenBar = new Table();
		greenBar.setBackground(new NinePatchDrawable(Assets.limeGreen9PatchSmallFilled));

		grayBar = new Table();
		grayBar.setBackground(new NinePatchDrawable(Assets.gray9PatchSmallFilledCut));
		
		exp = new Label("", Assets.generateLabelStyleUI(16, Assets.nums + "XP"));
		exp.setColor(MainStoreScreen.FONT_COLOR);
		exp.setAlignment(Align.right);
			
		table.add(expTable).width(infoWidth).height(height);
//		table.debug();
		
		String levelText = Assets.strings.get("lvl");
		lvlLs = Assets.generateLabelStyleUI(12, levelText + Assets.nums);
		levelLeft = new Label("", lvlLs);
		levelLeft.setColor(MainStoreScreen.FONT_COLOR_GRAY);
//		levelLeft.setAlignment(Align.left);
		
		levelRight = new Label("", lvlLs);
		levelRight.setColor(MainStoreScreen.FONT_COLOR_GREEN);
//		levelRight.setAlignment(Align.right);
		
		updateExpTable();

		Table lvlTable = new Table();
		lvlTable.add(levelLeft).left();
		lvlTable.add(levelRight).right().expandX();
		
		table.row();
		table.add(lvlTable).colspan(2).expandX().fillX();
		
		return table;
	}
	
	public void updateExpTable() {
		float expPercent = expDisplayed *1.0f / Profile.getExpNeededFor(lvlDisplayed + 1);
		float height = KebabKing.getGlobalY(0.03f);
		
		expTable.clear();
		expBar.clear();
		
		expBar.add(greenBar).width(infoWidth * expPercent).height(height);
		expBar.add(grayBar).width(infoWidth * (1-expPercent)).height(height);
		
		expTable.add(expBar).width(infoWidth).height(height);
		
		float xpPad = ((infoWidth * (1-expPercent)) + KebabKing.getGlobalX(0.01f));
		xpPad = Math.min(xpPad, (infoWidth - KebabKing.getGlobalX(0.1f)));

		exp.setText(LanguageManager.localizeNumber(expDisplayed) + "XP");
		expTable.row();
		expTable.add(exp).right().padRight(xpPad).padTop(-height);
		
		String levelText = Assets.strings.get("lvl");
		levelLeft.setText(levelText + " " + LanguageManager.localizeNumber(lvlDisplayed));
		levelRight.setText(levelText + " " + LanguageManager.localizeNumber((lvlDisplayed + 1)));
	}
	
	public void clickContinue() {
		if (justUnlockedSomething) {
			TopBar.updateLevel(master.profile.getLevel());
			kitchen.master.summaryToStore();
		}
		else {
			TopBar.updateLevel(master.profile.getLevel());
			kitchen.master.summaryToMain(); // change later
		}
	}
	
	public void clickShareButton() {
		Manager.fb.shareDayComplete(this.profit);
	}
	
//	public void clickGoToWheel() {
//		if (DrawUI.notificationActive) return;
//		
//		System.out.println("Transitioning to ads");
////		AdsHandler.showAd();
//		master.switchToJadeWheelScreen();
//	}
	
	public int calcExpForProfit(float profit) {
		return Math.max((int) (profit * PROFIT_TO_EXP_RATE), 0);
	}

	public void grantExp(int exp) {
		this.getProfile().giveExp(exp); 
	}
	
	public void render(float delta) {
		if (timeElapsed > TIME_TO_WAIT && !dayCompleteDone) {
			dayCompleteDone = true;
			this.subtractExpenses();
			initializeTable();
			if (expRemaining <= 0) {
				addContinueButton();
			}
		}

		super.renderGrayAlpha(delta, alpha);

//		uiStage.draw();
	}

	public boolean shouldDisplayPromptForRating() {
		return false;
//		if (master.profile.getLevel() < 4) return false;
//
//		// every time you gain a level after that with a good rating
//		if (master.profile.getLevel() > levelAtStart && rating >= 4.5) {
//			return true;
//		}
//		return false;
	}
	
	public void increaseDisplayedExp() {
		expDisplayed++;
		if (expDisplayed >= Profile.getExpNeededFor(lvlDisplayed + 1)) {
			lvlDisplayed++;
			SoundManager.playLevelUp();
			TopBar.updateLevel(lvlDisplayed);
			expDisplayed = 0;
			launchUnlockNotificationIfNecessary();
		}
	}
	
	public void launchUnlockNotificationIfNecessary() {
		// "leveling up"
		LinkedList<Purchaseable> unlocked = new LinkedList<Purchaseable>();
		for (Purchaseable p : PurchaseType.allPurchaseables) {
			if (p.unlockAtLevel() == this.lvlDisplayed) {
				// TODO enable
				unlocked.add(p);
			}
		}
		if (unlocked.size() > 0) {
			this.justUnlockedSomething = true;
			DrawUI.launchUnlockNotification(unlocked);
//			for (Purchaseable p : unlocked) {
//				master.store.storeScreen.updatePurchaseableAfterUnlock(p);
//			}
		}
	}

	@Override
	// actually run a game loop
	public void update(float delta, boolean ff) {
		super.update(delta, ff);

		this.timeElapsed += delta;
		if (timeElapsed < TIME_TO_WAIT) {
			alpha = (timeElapsed / TIME_TO_WAIT) * ALPHA_GOAL;
		}
		else {
			alpha = ALPHA_GOAL;
	
			if (expRemaining > 0 && expTable != null && !DrawUI.notificationActive) {
				this.expRemaining--;
				increaseDisplayedExp();
				updateExpTable();
			}
			
			// display the continue table
			if (expRemaining <= 0 && continueButton == null && dayCompleteDone) {
				addContinueButton();

				// maybe prompt here? maybe prompt on home screen
				if (shouldDisplayPromptForRating()) {
					Manager.analytics.promptForRating();
				}
			}
			
			// save profile once done granting exp
//			else if (expRemaining == 0 && !grantingExpDone) {
//				System.out.println("SAVING PROFILE AFTER EXP LOADED");
//				this.grantingExpDone = true;
////				this.saveProfile();
//			}
			
		}
		uiStage.act();
	}
	
	public void addContinueButton() {
		continueButton = DrawUI.getBlueButton(Assets.strings.get("continue"), 40);

		System.out.println("exp remaining: " + expRemaining + " continue button " + (continueButton != null));
		
		continueButton.setColor(new Color(1, 1, 1, 0));
		continueButton.addAction(Actions.fadeIn(1));
		
		continueButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
				clickContinue();
			}
		});
		
		bigTable.row();
//		TextureRegion bg = Assets.getTextureRegion("screens/pause-03");		
//		float nextWidth = KebabKing.getGlobalX(0.6f);
//		float nextHeight = nextWidth * bg.getRegionHeight() / bg.getRegionWidth();
		bigTable.add(continueButton).top().padTop(KebabKing.getGlobalY(-0.11f)).expandY(); //.width(nextWidth).height(nextHeight);
	}
	
	public Profile getProfile() {
		return kitchen.master.profile;
	}
}
