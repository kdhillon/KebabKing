package com.kebabking.game;

import java.util.ArrayList;

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
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kebabking.game.Purchases.Purchaseable;

// make it draw a little bar at the top!

/** Static class for drawing UI elements */
public class DrawUI {
	static final Color WHITE = new Color(1, 1, 1, 1);
	static final Color GRAY = new Color(.2f, .2f, .2f, .5f);
//	static final Color DARK_GRAY = new Color(.2f, .2f, .2f, .9f);
	static final int CASH_COINS_SIZE = 18;
	
	static final Color FONT_COLOR = new Color(0f, .2f, .3f, 1);
	static final Color REPUTATION_FONT_COLOR = new Color(0f, .2f, .3f, 0.5f);
	
	static final float UI_BAR_HEIGHT = 0.06f; // 
	static final float AD_BAR_HEIGHT = 0.015f; // 
	
//	static final float xStarOffset = 0.06f;
//	static final float X_STAR_INIT = 0.12f;
	
	static final float STAR_HEIGHT = 0.028f;
	static final float STAR_TABLE_SIZE_FACTOR = 0.9f;
	static final float STAR_PAD = 0.005f;
	static final float STAR_TABLE_OFFSET = 0.01f;
	
//	static final float COINS_X  = 0.02f * ChuanrC.width;
//	static final float COINS_Y	= 0.982f * ChuanrC.height;
//	
//	static final float MONEY_X = 0.70f * ChuanrC.width;
//	static final float MONEY_Y = 0.982f * ChuanrC.height;
	
	static final float TIME_X =KebabKing.getGlobalX(0.4f);
	static final float TIME_Y = KebabKing.getGlobalY(0.98f);
	
	static Stage uiStage;
	static KebabKing master;
	
	static Table bigTable;
	static Table uiTable;
	static Button pauseButton;
	static Button muteButton;
	
	static float barHeight;
	static float adBarHeight;
	
	static Table starTable;
	static Table reputationTable;
	static float currentStarCount;
	
	static Label coins;
	static Label cash;
	
	static Label time;
	
	static Color countdownColor;
	
	static Table adCampaignTable;
	static Table bar;
	
	static Table notificationTable;

	static Color tintColor;

	// each image can only be in one place at a time. So we need 5 star images. 
	static Image[] star;
	static Image halfStar; // only need one of these
	static Image[] grayStar; // need 5 of these (really 4 but whatever);
	
	static ArrayList<Purchaseable> unlockDisplayQueue = new ArrayList<Purchaseable>();
	
	public static void setInput(InputProcessor ip) {
		InputMultiplexer im = new InputMultiplexer();
		im.addProcessor(DrawUI.uiStage);
		im.addProcessor(ip);
		Gdx.input.setInputProcessor(im);
	}
	
	public static void initialize(KebabKing master_in, SpriteBatch batch) {
		master = master_in;
		
		tintColor = new Color(0, 0, 0, 0);
		
		ScreenViewport viewport = new ScreenViewport();
		uiStage = new Stage(viewport, batch);		
		
		barHeight = KebabKing.getGlobalY(UI_BAR_HEIGHT);
		adBarHeight += KebabKing.getGlobalY(AD_BAR_HEIGHT);
		
		bigTable = new Table();
		bigTable.setSize(KebabKing.getWidth(), KebabKing.getHeight());
		
		uiTable = new Table();
//		uiTable.debugAll();
		
//		uiTable.background(Assets.getTopBarBG());
		uiTable.setPosition(0, KebabKing.getHeight() - barHeight - adBarHeight);
		uiTable.setSize(KebabKing.getWidth(), barHeight + 5 + adBarHeight);
		
		uiTable.padTop(KebabKing.getGlobalY(0.01f)).padLeft(KebabKing.getGlobalY(0.005f)).padRight(KebabKing.getGlobalY(0.005f));
		
		bigTable.add(uiTable).top().height(barHeight + 5 + adBarHeight).expandY();
//		bigTable.debugAll();
		uiStage.addActor(bigTable);
			
		// screen is divided into 9 regions follows:
		
		// calculate coins bg size
		float unit = 1/9f;
		float mute_size = KebabKing.getGlobalX(unit);
		float pause_size = KebabKing.getGlobalX(unit);
		float starTableWidth = KebabKing.getGlobalX(2.7f * unit);
		float cash_bg_size = KebabKing.getGlobalX(2.55f * unit);
		float coins_bg_size = KebabKing.getGlobalX(1.75f * unit);
		float bars_pad_top = 0.00f*coins_bg_size;
//		float plusSize = 0.6f*barHeight;
		
		pauseButton = new Button();
		pauseButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
				clickPause();
			}
		});
		pauseButton.setStyle(Assets.getPauseButtonStyle());
		uiTable.add(pauseButton).height(barHeight).width(pause_size).left();
		pauseButton.setVisible(false); // disable on main screen, enable only on pause screen

		muteButton = new Button();
		muteButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
				clickMute(master.profile);
			}
		});
		setProperMuteStyle(master.profile);
		uiTable.add(muteButton).height(barHeight).width(mute_size).left().expandX();

		star = new Image[5];
		for (int i = 0; i < 5; i++) {
			star[i] =  new Image(Assets.getStar());
		}
		halfStar = new Image(Assets.getHalfStar());
		grayStar = new Image[5];
		for (int i = 0; i < 5; i++) {
			grayStar[i] = new Image(Assets.getGrayStar());
		}

		reputationTable = new Table();
		reputationTable.setBackground(new TextureRegionDrawable(Assets.getReputationBG()));

//		int REPUTATION_TEXT_SIZE = (int) (barHeight * 0.20f);
//		System.out.println("REPUTATION TEXT SIZE: " + REPUTATION_TEXT_SIZE);
		Label reputationLabel = new Label("REPUTATION", Assets.generateLabelStyleUIWhite(12, "REPUTATION"));
		reputationLabel.setColor(REPUTATION_FONT_COLOR);
		reputationTable.add(reputationLabel).center();
		reputationTable.row();

		starTable = new Table();
		reputationTable.add(starTable).width(starTableWidth * STAR_TABLE_SIZE_FACTOR).center();
		updateStars(master.profile.getCurrentReputation());
		
//		starTable.setBackground(Assets.getTopBarBG());
		uiTable.add(reputationTable).expandX(); //width(starTableWidth).
		
//		int TEXT_SIZE = (int) (barHeight * 0.35f);

		// make it permanent
		coins = new Label("", Assets.generateLabelStyleUIWhite(CASH_COINS_SIZE, Assets.nums + ".$"));
		cash = new Label("", Assets.generateLabelStyleUIWhite(CASH_COINS_SIZE, Assets.nums + ".$"));
		coins.setColor(FONT_COLOR);
		cash.setColor(FONT_COLOR);

		Table cashTable = new Table();
//		Label cash_l = new Label("$", Assets.generateLabelStyle(TEXT_SIZE));
//		cashTable.add(cash_l);
//		cashTable.debugAll();
		cashTable.setBackground(new TextureRegionDrawable(Assets.getCashBG()));
		cashTable.add(cash).right().expand().padTop(bars_pad_top).padRight(coins_bg_size * .1f);
		cash.setAlignment(Align.right);
		uiTable.add(cashTable).width(cash_bg_size).height(barHeight);
		
		Table coinsTable = new Table();
//		coinsTable.debugAll();
		coinsTable.setBackground(new TextureRegionDrawable(Assets.getCoinsBG()));
//		Label coins_l = new Label("Coins: ", Assets.generateLabelStyle(TEXT_SIZE));
//		coinsTable.add(coins_l);
		coinsTable.add(coins).right().expand().padTop(bars_pad_top).padRight(coins_bg_size * 0.1f);
//		Button coinPlus = new Button();
//		coinPlus.setStyle(Assets.getCoinPlusStyle());
//		coinPlus.addListener(new InputListener() {
//			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
//				return true;
//			}
//			public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
//				clickCoinPlus();
//			}
//		});
//		
//		coinsTable.add(coinPlus).right().padTop(bars_pad_top + 0.1f*barHeight).width(plusSize).height(plusSize);
		coins.setAlignment(Align.right);
		uiTable.add(coinsTable).width(coins_bg_size).height(barHeight);
		
		uiTable.row();
		adCampaignTable = new Table();
		adCampaignTable.setVisible(false);
		bar = new Table();
		bar.setBackground(new TextureRegionDrawable(Assets.red));
		uiTable.add(adCampaignTable).colspan(5).height(KebabKing.getGlobalY(AD_BAR_HEIGHT)).left();
				
		time = new Label("", Assets.generateLabelStyleUIChinaWhite(100, Assets.nums));
		time.setPosition(0, KebabKing.getGlobalY(0.7f));
		time.setWidth(KebabKing.getWidth());
		time.setAlignment(Align.center);
		
		countdownColor = new Color(1, 1, 1, 1);
		
		notificationTable = new Table();
		notificationTable.debug();
//		
		float notificationWidth = KebabKing.getGlobalX(0.9f);
		float notificationHeight = KebabKing.getGlobalY(0.8f);
//		
		bigTable.debug();
		bigTable.row();
		bigTable.add(notificationTable).width(notificationWidth).height(notificationHeight).expandY().bottom().padBottom((KebabKing.getHeight() - notificationHeight)/2);;
		
		prepareNotificationTable("Title", "Description", "fuckyou.png");
//		clearNotificationTable();
	}

	public static void drawFullUI(float delta, SpriteBatch batch, Profile profile) {
		// update fields of all relevant UI things
		if (master.getScreen() == master.kitchen) {
			pauseButton.setVisible(true);
			pauseButton.setTouchable(Touchable.enabled);
//			muteButton.setTouchable(Touchable.disabled);
//			if (uiTable.getCell(muteButton) != null) {
//				uiTable.getCell(muteButton).setActor(pauseButton).height(barHeight).width(barHeight).left().expandX();
//			}
		}
		else {
			pauseButton.setVisible(false);
			pauseButton.setTouchable(Touchable.disabled);
//			muteButton.setTouchable(Touchable.enabled);
//			if (uiTable.getCell(pauseButton) != null) {
//				uiTable.getCell(pauseButton).setActor(muteButton).height(barHeight).width(barHeight).left().expandX();
//			}
		}

		// This generates the string every frame, not good.
		// TODO optimize
		coins.setText(profile.coinsString);
		cash.setText(profile.cashString);

		if (profile.inventory.adCampaign != null)
			updateAdCampaignBar(profile.inventory.adCampaign.getCurrentSelected().getName());
		else updateAdCampaignBar(null);
		
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
	
	public static void clickPause() {
		System.out.println("PAUSING");
		master.kitchenPause();
	}
	
	public static void clickMute(Profile profile) {
		System.out.println("Mutings");
		master.toggleMute();
		setProperMuteStyle(profile);
	}
	
	public static void setProperMuteStyle(Profile profile) {
		if (profile.settings.muteMusic)
			muteButton.setStyle(Assets.getButtonStyleMuted());
		else 
			muteButton.setStyle(Assets.getButtonStyleUnmuted());
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
		time.setText("" + (int) (inputTime + 1));
		
		countdownColor.set(1,  1,  1, inputTime - (int) inputTime);
	
		time.setColor(countdownColor);
		time.draw(batch, 1);	
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

	public static void updateStars() {
		updateStars(master.profile.getCurrentReputation());
	}
	
	// round to nearest 0.5
	public static void updateStars(float starCount) {
		System.out.println("star count o : " + starCount);
		starCount = ((int) Math.round(starCount * 2)) / 2f;
		System.out.println("rounded count : " + starCount);
		if (currentStarCount != starCount) {
			starTable.clear();
//			starTable.debug();
			
			// calculate number of gray stars
			int grayStars = (int) (5 - starCount);
			System.out.println(starCount + " stars " + grayStars);
					
			// star width is determined by size of star area
			float starHeight = KebabKing.getGlobalY(STAR_HEIGHT);
			
			float rightPad = KebabKing.getGlobalX(STAR_PAD);
			float leftPad = KebabKing.getGlobalX(STAR_PAD);
			
			// each "image" can only be in one place at a time.
			while (starCount >= 1) {
				starTable.add(star[(int) starCount]).height(starHeight).width(starHeight).padRight(rightPad).padLeft(leftPad).expand();
				starCount--;
			}
			
			// draw half star
			if (starCount > 0) {
				starTable.add(halfStar).height(starHeight).width(starHeight).padRight(rightPad).padLeft(leftPad).expand();
			}
			
			// draw gray stars (necessary)
			for (int i = 0; i < grayStars; i++) {
				starTable.add(grayStar[i]).height(starHeight).width(starHeight).padRight(rightPad).padLeft(leftPad).expand();
			}
			
			currentStarCount = starCount;
		}
	}
		
	public static void updateAdCampaignBar(String labelText) {
		// draw a yellow
		if (master.profile.inventory.campaignPercent() > 0) {
			float textWidth = labelText.length() * 0.02f;
			if (!adCampaignTable.isVisible()) {
				System.out.println("UPDATING AD CAMPAIGN BAR");

				adCampaignTable.setVisible(true);
				Table labelTable = new Table();
				labelTable.setBackground(new TextureRegionDrawable(Assets.gray));
//				labelTable.debugAll();
				//			labelTable.setBackground();
				Label adCampaignLabel = new Label(labelText + ": ", Assets.generateLabelStyleUIChinaWhite(16, Assets.alpha + " :"));
				adCampaignLabel.setAlignment(Align.center);
				labelTable.add(adCampaignLabel).left();
				adCampaignTable.add(labelTable).left().width(KebabKing.getGlobalX(textWidth)).height(KebabKing.getGlobalY(AD_BAR_HEIGHT));
				adCampaignTable.add(bar).left().fillY().expandY();
			}
			// update length of bar
			float barWidth = 1-textWidth - master.profile.inventory.campaignPercent();
			bar.setWidth(KebabKing.getGlobalX(barWidth));
		}
		else if (adCampaignTable.isVisible()) {
			adCampaignTable.clear();
			// visibility is used as a boolean flag
			adCampaignTable.setVisible(false);
		}
	}
	
	public static void prepareNotificationTable(String titleText, String messageText, String iconString) {
		notificationTable.setBackground(new TextureRegionDrawable(Assets.notificationBG));
		Table subTable = new Table();
		float padX = 0.1f;
		float padTop = 0.1f;
		float padBot = 0.15f;
		notificationTable.add(subTable).padLeft(KebabKing.getGlobalX(padX)).padRight(KebabKing.getGlobalX(padX)).padTop(KebabKing.getGlobalY(padTop)).padBottom(KebabKing.getGlobalY(padBot)).expand().fill();//.top();
		Label title = new Label(titleText, Assets.generateLabelStyleUIWhite(40, Assets.alpha));
		title.setColor(MainStoreScreen.FONT_COLOR);
		subTable.add(title).top().expandX();
		
		Label message = new Label(messageText, Assets.generateLabelStyleUIWhite(24, Assets.allChars));
		message.setColor(MainStoreScreen.FONT_COLOR);
		subTable.row();
		message.setAlignment(Align.left);
		message.setWrap(true);
		subTable.add(message).expandX().expandY().top().left();
		
		message.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
				System.out.println("clearing");
				clearNotificationTable();
			}
		});
	}
	
	public static void clearNotificationTable() {
		Drawable d = null;
		notificationTable.setBackground(d);
		notificationTable.clear();
	}
	
	public static void addToUnlockDisplayQueue(Purchaseable p) {
		unlockDisplayQueue.add(p);
	}
	
	public static void drawUnlockWindows() {
		
	}
	
	public static void launchNextUnlockWindow() {
		
	}
}
