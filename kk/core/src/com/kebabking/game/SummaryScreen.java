package com.kebabking.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kebabking.game.Managers.Manager;

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
	
	KitchenScreen kitchen;
	
	float timeElapsed;
//	Label dayComplete;
	Table dayComplete;
	
	float revenue;
	float expenses;
	float meatExpenses;
	float rent;
	float profit;
	int customersServed;
	int sickCount;
	float rating; // between 0.5 and 5.0
	
	// not anymore
	int expDisplayed;
	String expString;  // corresponds to above
	
	int expRemaining; // don't use this anymore.
	
	// in case we want to have stars pop on.
	Table starTable;
	float currentStarCount;
	
	boolean dayCompleteDone = false; // used to switch to info from "day complete"
	boolean grantingExpDone = false; // used to switch to info from "day complete"

	float alpha = 0;
	
	Table expTable;
	Table expBar;
	Table greenBar;
	Table grayBar;
	Label exp;
	Label levelLeft;
	Label levelRight;
	LabelStyle lvlLs;
	
	float infoWidth;
	
	public SummaryScreen(KebabKing master, KitchenScreen kitchen) {
		super(master, true);
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
		bigTable.add(dayComplete).center().height(KebabKing.getGlobalY(0.1f)).width(KebabKing.getGlobalX(1f));
		
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
		
		this.expRemaining = this.calcExpForProfit(profit);
	
		if (kitchen.wasShutDown) {
			DrawUI.launchPoliceNotification();
		}


		// Prepare an ad to be watched
		Manager.ads.cacheAd();
		
		if (KebabKing.EXP_CITY) 
			this.expRemaining += 300;
	}
	
	public void updateProfile() {
		// save rating
		kitchen.master.profile.subtractDailyExpenses();
	}
	
	// TODO fix this
	// saving takes a while, so better to do this after a screen is up
	public void saveProfile() {
		// save to getProfile()
		getProfile().addRepuation(kitchen.calculateReputation());		
		getProfile().endDay(kitchen);
	}
	
	public Table getDayCompleteButton() {
		Table button = DrawUI.getBlueButton("DAY COMPLETE", 44);
//		TextureRegion bg = Assets.getTextureRegion("screens/pause-03");
//		button.setBackground(new TextureRegionDrawable(bg));
//		  
//		Label resume = new Label("    DAY COMPLETE    ", Assets.generateLabelStyleUIChinaWhite(48, " DAY COMPLETE"));
//		button.add(resume).center().padRight(KebabKing.getGlobalX(0.1f)).padBottom(KebabKing.getGlobalY(0.004f));
	
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
		bigTable.clear();
		float topBarPadY = KebabKing.getGlobalY(0.02f);
		
		Table window = new Table();
		bigTable.add(window).width(KebabKing.getGlobalX(0.95f)).height(KebabKing.getGlobalY(0.94f)).top().padTop(KebabKing.getGlobalY(0.015f));
		
		Table subTable = new Table();
		subTable.setBackground(new TextureRegionDrawable(Assets.notificationBG));

		float padX = 0.08f;
		float padTop = 0.1f;
		float padBot = 0.09f;
		window.add(subTable).padLeft(KebabKing.getGlobalX(padX)).padRight(KebabKing.getGlobalX(padX)).padTop(KebabKing.getGlobalY(padTop)).padBottom(KebabKing.getGlobalY(padBot)).expand().fill();//.top();	
		
		Table topBar = generateTopTable();
		subTable.add(topBar).top().expandX().fillX().padBottom(topBarPadY).padTop(topBarPadY);
		subTable.row();
		
		float padShareY = KebabKing.getGlobalY(0.02f);
		
		Table share = generateShareTable();
//		share.debugAll();
		subTable.add(share).top().expandX().padBottom(padShareY);
		subTable.row();
		
		float infoPadBetween = KebabKing.getGlobalY(0.005f);
		Table infoTable = new Table();
		Table revenueTable = generateTable("Revenue", revenue, true, new String[]{}, new float[]{});
		infoTable.add(revenueTable).expandX().fillX().padBottom(infoPadBetween).colspan(2);
		infoTable.row();
		Table expenseTable = generateTable("Expenses", expenses, true, new String[]{"Meat", "Rent"}, new float[]{meatExpenses, rent});
		infoTable.add(expenseTable).expandX().fillX().padBottom(infoPadBetween).colspan(2);
		infoTable.row();
		Table customerTable = generateTable("Customers", customersServed, false, new String[]{"Sick"}, new float[]{sickCount});
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
		
		subTable.add(infoTable).top().width(infoWidth).expandY().center();
		subTable.row();
		
		
		Table jadeTable = generateJadeTable();
		float width = KebabKing.getGlobalX(0.7f);
		float height = width * Assets.jadeBox.getRegionHeight() / Assets.jadeBox.getRegionWidth();
		float jadePad = KebabKing.getGlobalY(0.01f);
		subTable.add(jadeTable).expandY().width(width).height(height).padTop(0).padBottom(jadePad*3);
	
		// new lines give this extra volume for clicking (hacky)
//		Label nextRound = new Label("\nnext round >>\n", Assets.generateLabelStyleUIWhite(30, "\nnext round>"));
//		nextRound.setColor(RED);
		Table nextRound = DrawUI.getBlueButton("CONTINUE", 40);
		
		nextRound.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
				clickNextRound();
			}
		});	
		
		bigTable.row();
		TextureRegion bg = Assets.getTextureRegion("screens/pause-03");		
		float nextWidth = KebabKing.getGlobalX(0.6f);
		float nextHeight = nextWidth * bg.getRegionHeight() / bg.getRegionWidth();
		bigTable.add(nextRound).top().expandY().padTop(KebabKing.getGlobalY(-0.08f)).width(nextWidth).height(nextHeight);
	}
	
	public static Table generateTopTable() {
		Table topBar = new Table();
//		topBar.debugAll();
		Table topLeft = new Table();
		Table topRight = new Table();
		topLeft.setBackground(new TextureRegionDrawable(Assets.red));
		topRight.setBackground(new TextureRegionDrawable(Assets.red));
		
		Table topCenter = new Table();
		
		TextureRegion topImage = Assets.getTextureRegion("screens/Summary-02 (2)");
		float IMAGE_WIDTH = 0.2f;
		float IMAGE_HEIGHT = IMAGE_WIDTH * topImage.getRegionHeight() / topImage.getRegionWidth();
		Image image = new Image(topImage);
//		image.setWidth(KebabKing.getGlobalX(IMAGE_WIDTH));
//		image.setHeight(KebabKing.getGlobalY(IMAGE_HEIGHT));
		topCenter.add(image).width(KebabKing.getGlobalX(IMAGE_WIDTH)).height(KebabKing.getGlobalX(IMAGE_HEIGHT));
		topCenter.row();
		Label dailyAccounts = new Label("DAILY ACCOUNTS", Assets.generateLabelStyleUIWhite(14, "DAILY ACCOUNTS"));
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
		String text = "+" + SHARE_REWARD;
		Label shareValue = new Label(text, Assets.generateLabelStyleUIHeavyWhite(16, text));
		shareValue.setColor(MainStoreScreen.FONT_COLOR_GREEN);
		rightTable.add(shareValue);
		
		Image minijade = new Image(Assets.bigjade);
		rightTable.add(minijade).height(height * 0.8f).width(height * 0.8f).padLeft(height * 0.2f);
		shareMain.add(rightTable).height(height).padLeft(KebabKing.getGlobalX(0.01f));
		
		shareMain.setTouchable(Touchable.enabled);
		shareMain.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
				clickShareButton();
			}
		});	
		
		return shareMain;
	}
	
	public Table generateTable(String mainLabel, float mainValue, boolean money, String[] labels, float[] values) {
		if (labels.length != values.length) throw new java.lang.AssertionError();
		
		Table table = new Table();
		table.setBackground(new NinePatchDrawable(Assets.gray9PatchSmall));
		
		Label title = new Label(mainLabel, Assets.generateLabelStyleUIHeavyWhite(14, Assets.alpha));	
		title.setColor(MainStoreScreen.FONT_COLOR);
		String toPrint;
		if (money) toPrint = Assets.currencyChar + mainValue;
		else toPrint = "" + (int) mainValue;
		Label value = new Label(toPrint, Assets.generateLabelStyleUIHeavyWhite(14, Assets.nums));
		value.setColor(MainStoreScreen.FONT_COLOR);
		table.add(title).left().expandX();
		table.add(value).right().expandX();
		
		float indentPad = KebabKing.getGlobalX(0.1f);
		for (int i = 0; i < labels.length; i++) {
			table.row();
			Label lab = new Label(labels[i], Assets.generateLabelStyleUILight(14, Assets.alpha));
			lab.setColor(MainStoreScreen.FONT_COLOR);
			if (money) toPrint = Assets.currencyChar + values[i];
			else toPrint = "" + (int) values[i];
			Label val = new Label(toPrint, Assets.generateLabelStyleUILight(14, Assets.nums));
			val.setColor(MainStoreScreen.FONT_COLOR);
			table.add(lab).left().expandX().padLeft(indentPad);
			table.add(val).right().expandX();
		}
		return table;
	}
	
	public Table generateReputationTable() {
		Table table = new Table();
		table.setBackground(new NinePatchDrawable(Assets.gray9PatchSmall));
		
		float negPadY = -KebabKing.getGlobalY(NEG_PAD);
		
		Label reputationLabel = new Label("DAILY RATING", Assets.generateLabelStyleUIWhite(14, "DAILY RATING"));
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
		
		Label profitLabel = new Label("PROFIT", Assets.generateLabelStyleUIWhite(12, "PROFIT"));
		profitLabel.setColor(RED);
		table.add(profitLabel).center().padTop(negPadY);
		table.row();

		Label valueLabel = new Label(Assets.currencyChar + this.profit, Assets.generateLabelStyleUIWhite(24, Assets.nums + "-"));
		valueLabel.setColor(RED);
		table.add(valueLabel).center().padBottom(negPadY);
		
		return table;
	}
	
	public Table generateJadeTable() {
		Table table = new Table();
		table.setTouchable(Touchable.enabled);
		table.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
				transitionToAd();
			}
		});	
		
		table.setBackground(new TextureRegionDrawable(Assets.jadeBox));
		
//		Image playButton = new Image(Assets.getTextureRegion("market/Market_menu_element-09"));
		Image playButton = new Image();
		float playWidth = KebabKing.getGlobalX(0.06f);
		table.add(playButton).width(playWidth).height(playWidth).bottom().expandY().padBottom(KebabKing.getGlobalY(0.003f));
		table.row();
		
		Label earnJade = new Label("\nEARN JADE  \n", Assets.generateLabelStyleUIChinaWhite(22, "EARN JADE\n"));
		table.add(earnJade).bottom().padBottom(KebabKing.getGlobalY(0.025f));
		return table;
	}
	
	public Table generateLevelTable() {
		Table table = new Table();

		float height = KebabKing.getGlobalY(0.03f);

		float expPercent = master.profile.getExp() *1.0f / master.profile.getNextExp();
//		float expPercent = 0.05f;
		System.out.println("exp percent:" + expPercent);

		expTable = new Table();
		
		expBar = new Table();
				
		greenBar = new Table();
		greenBar.setBackground(new NinePatchDrawable(Assets.limeGreen9PatchSmallFilled));

		grayBar = new Table();
		grayBar.setBackground(new NinePatchDrawable(Assets.gray9PatchSmallFilledCut));
		
		exp = new Label("", Assets.generateLabelStyleUIWhite(16, Assets.nums + "XP"));
		exp.setColor(MainStoreScreen.FONT_COLOR);
		exp.setAlignment(Align.right);
			
		table.add(expTable).width(infoWidth).height(height);
//		table.debug();
		
		lvlLs = Assets.generateLabelStyleUIWhite(12, "LVL" + Assets.nums);
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
		float expPercent = master.profile.getExp() *1.0f / master.profile.getNextExp();
		float height = KebabKing.getGlobalY(0.03f);
		
		expTable.clear();
		expBar.clear();
		
		expBar.add(greenBar).width(infoWidth * expPercent).height(height);
		expBar.add(grayBar).width(infoWidth * (1-expPercent)).height(height);
		
		expTable.add(expBar).width(infoWidth).height(height);
		
		float xpPad = ((infoWidth * (1-expPercent)) + KebabKing.getGlobalX(0.01f));
		xpPad = Math.min(xpPad, (infoWidth - KebabKing.getGlobalX(0.1f)));

		exp.setText(master.profile.getExp() + "XP");
		expTable.row();
		expTable.add(exp).right().padRight(xpPad).padTop(-height);
		
		levelLeft.setText("LVL " + master.profile.getLevel());
		levelRight.setText("LVL " + (master.profile.getLevel() + 1));
	}
	
	public void clickNextRound() {
		System.out.println("Transitioning to next round");

		if (expRemaining > 0) {
			grantExp(expRemaining);
			saveProfile();
		}
		kitchen.master.summaryToMain(); // change later
	}
	
	public void clickShareButton() {
		Manager.fb.shareDayComplete(this.profit);
	}
	
	public void transitionToAd() {
		System.out.println("Transitioning to ads");
		Manager.ads.showAd();
	}
	
	public int calcExpForProfit(float profit) {
		return(int) (profit * PROFIT_TO_EXP_RATE); 
	}

	public void grantExp(int exp) {
		this.getProfile().giveExp(exp); 
	}
	
	public void render(float delta) {
		if (timeElapsed > TIME_TO_WAIT && !dayCompleteDone) {
			dayCompleteDone = true;
			this.updateProfile();
			initializeTable();
		}
		
		super.renderGrayAlpha(delta, alpha);

//		uiStage.draw();
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
				grantExp(1);
				updateExpTable();
			}
			// save profile once done granting exp
			else if (expRemaining == 0 && !grantingExpDone) {
				System.out.println("SAVING PROFILE AFTER EXP LOADED");
				this.grantingExpDone = true;
				this.saveProfile();
			}
			
		}
		uiStage.act();
	}
	
	
	public ProfileRobust getProfile() {
		return kitchen.master.profile;
	}
}
