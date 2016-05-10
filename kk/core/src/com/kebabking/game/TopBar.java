package com.kebabking.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;


// TODO: combine topbar and projectiles
// 		use the same decelerating algorithm as here, but use it to generate projectiles, not update the top bar.
//		depending on total increase, projectiles might be worth more than actual value.
// 		for example, if difference is 1000, might generate a few with value 20, then 10, then 5, then 2, etc. 
//		in fact, keep constant speed of projectile generation, just change their value
public class TopBar extends Table {
	static final float UI_BAR_HEIGHT = 0.052f; 
	static final float AD_BAR_HEIGHT = 0.015f; 

	static final float STAR_HEIGHT = 0.022f;
	static final float STAR_TABLE_SIZE_FACTOR = 0.8f;
//	static final float STAR_PAD = 0.0015f;
	static final float STAR_TABLE_OFFSET = 0.01f;

	static final int CASH_COINS_SIZE = 16;

	static final boolean COIN_FLASH_ENABLED = true;
	static final float COINS_FLASH_RATE = 3f; // this much alpha per second.
	static final float MIN_ALPHA = 0.3f;

	static final int REPUTATION_FONT_SIZE = 11;
	
	static final float PAD_X_STANDARD = 0.006f;
	
	public static Color blue = new Color(69f/256, 112f/256, 159f/256, 1);
//	static final Color REPUTATION_FONT_COLOR = new Color(0f, .2f, .3f, 0.5f);	
	static final Color REPUTATION_FONT_COLOR = new Color(blue.r, blue.g, blue.b, 0.5f);
	public static KebabKing master;

	static boolean pauseVisible; // if false, settings is visible
	static Button pauseSettingsButton;
	static Button muteButton;

	static float barHeight;
	static float adBarHeight;

	static Table starTable;
	static Table reputationTable;
	static float currentStarCount;

	static Label level;
	static Label coins;
	static Label cash;
	
	static float standardPad;

	static Table adCampaignTable;
	static Table bar;
	static boolean campaignActive;

	// each image can only be in one place at a time. So we need 5 star images. 
	static Image[] star;
	static Image halfStar; // only need one of these
	static Image[] grayStar; // need 5 of these (really 4 but whatever);

	static int levelDisplayed;
	
	static String coinsString;
	static int coinsDisplayed; // this should correspond to the above
	static String cashString;
	static float cashDisplayed; // this should correspond to the above

	static float coinsDisplayedF; // float version of coins displayed
	static float cashDisplayedF;  // float version of cash displayed
	
	static float coinsAlpha = 1;
	static float cashAlpha = 1;
	static boolean coinsAlphaUp; // is the alpha increasing or decreasing?
	static boolean cashAlphaUp;

	static float timeElapsed = 0;

	public TopBar(KebabKing masterIn) {
		master = masterIn;
		
		// calculate coins bg size
		float unit = 1/10f;
//		float mute_size = KebabKing.getGlobalX(unit * 0.9f);
		float pause_size = KebabKing.getGlobalX(unit * 0.9f);
		float level_bg_size = KebabKing.getGlobalX(1.4f * unit);
		float starTableWidth = KebabKing.getGlobalX(2.7f * unit);
		float cash_bg_size = KebabKing.getGlobalX(2.3f * unit);
		float coins_bg_size = KebabKing.getGlobalX(1.75f * unit);
		
//		float totalSize = mute_size + pause_size + level_bg_size + starTableWidth + cash_bg_size + coins_bg_size;
//		float ratio = 1 - (totalSize / KebabKing.getWidth());
//		standardPad = KebabKing.getGlobalXFloat(ratio / 2);
		standardPad = KebabKing.getGlobalXFloat(PAD_X_STANDARD);
		// TODO figure out a better, cleaner way of calculating standardPad.
		
		
		float bars_pad_top = 0.00f*coins_bg_size;
		//		float plusSize = 0.6f*barHeight;

		barHeight = KebabKing.getGlobalY(UI_BAR_HEIGHT);
		adBarHeight += KebabKing.getGlobalY(AD_BAR_HEIGHT);

		pauseSettingsButton = new Button();
		pauseSettingsButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
				System.out.println("PAUSE VISIBLE: " + pauseVisible);
				if (pauseVisible)
					clickPause();
				else 
					clickSettings();
			}
		});
		pauseSettingsButton.setStyle(Assets.getPauseSettingsButtonStyle());
		this.add(pauseSettingsButton).height(barHeight).width(pause_size).left().padLeft(standardPad * 2).padRight(standardPad);
		pauseSettingsButton.setVisible(true);
		pauseVisible = false;
		pauseSettingsButton.setDisabled(true); // main screen, should be settings button
		
		
//		muteButton = new Button();
//		muteButton.addListener(new InputListener() {
//			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
//				return true;
//			}
//			public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
//				clickMute(master.profile);
//			}
//		});
//		setProperMuteStyle(master.profile);
//		this.add(muteButton).height(barHeight).width(mute_size).left().expandX().padLeft(standardPad).padRight(standardPad);

	
		
		// Add coins and cash table
		coins = new Label("", Assets.generateLabelStyleUI(CASH_COINS_SIZE, Assets.nums + "."));
		cash = new Label("", Assets.generateLabelStyleUI(CASH_COINS_SIZE, Assets.nums + "."));
		coins.setColor(blue);
		cash.setColor(blue);
		
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
		String rep = Assets.strings.get("reputation");
		Label reputationLabel = new Label(rep, Assets.generateLabelStyleUI(REPUTATION_FONT_SIZE, rep));
		reputationLabel.setColor(REPUTATION_FONT_COLOR);
		reputationTable.add(reputationLabel).center();
		reputationTable.row();

		starTable = new Table();
		reputationTable.add(starTable).width(starTableWidth * STAR_TABLE_SIZE_FACTOR).center();
		updateStars(master.profile.getCurrentReputation());

		//		starTable.setBackground(Assets.getTopBarBG());
		this.add(reputationTable).expandX().padLeft(standardPad).padRight(standardPad); //width(starTableWidth).

		//		int TEXT_SIZE = (int) (barHeight * 0.35f);
		
		
		// Add level table
		level = new Label("", Assets.generateLabelStyleUI(CASH_COINS_SIZE, Assets.nums));
		level.setColor(blue);
		String lvlText = Assets.strings.get("lvl");
		Label levelConst = new Label(lvlText, Assets.generateLabelStyleUI(REPUTATION_FONT_SIZE, lvlText));
		
		Table levelTable = new Table();
//		levelTable.debugAll();
		levelTable.add(levelConst).left().padLeft(KebabKing.getGlobalX(0.015f));
		
		levelTable.setBackground(new TextureRegionDrawable(Assets.getTextureRegion("topbar/level")));
		levelTable.add(level).center().expand();
		level.setAlignment(Align.center);
		this.add(levelTable).width(level_bg_size).height(barHeight).padLeft(standardPad).padRight(standardPad);

		
		
		Table cashTable = new Table();
		//		Label cash_l = new Label("$", Assets.generateLabelStyle(TEXT_SIZE));
		//		cashTable.add(cash_l);
		//		cashTable.debugAll();
		cashTable.setBackground(new TextureRegionDrawable(Assets.getCashBG()));
		cashTable.add(cash).center().expand().padTop(bars_pad_top).padLeft(coins_bg_size * 0.4f);
		cash.setAlignment(Align.center);
		this.add(cashTable).width(cash_bg_size).height(barHeight).padLeft(standardPad).padRight(standardPad);

		Table coinsTable = new Table();
		//		coinsTable.debugAll();
		coinsTable.setBackground(new TextureRegionDrawable(Assets.getCoinsBG()));
		//		Label coins_l = new Label("Coins: ", Assets.generateLabelStyle(TEXT_SIZE));
		//		coinsTable.add(coins_l);
		coinsTable.add(coins).center().expand().padTop(bars_pad_top).padLeft(coins_bg_size * 0.4f);
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
		coins.setAlignment(Align.center);
		this.add(coinsTable).width(coins_bg_size).height(barHeight).padLeft(standardPad).padRight(standardPad * 2);

		this.row();
		adCampaignTable = new Table();
		adCampaignTable.setVisible(false);
		bar = new Table();
		bar.setBackground(new TextureRegionDrawable(Assets.yellow));
		this.add(adCampaignTable).colspan(5).height(KebabKing.getGlobalY(AD_BAR_HEIGHT)).left();

		coinsDisplayed = master.profile.getCoins();
		coinsDisplayedF = coinsDisplayed;
		cashDisplayed = master.profile.getCash();
		cashDisplayedF = cashDisplayed;
		updateCoinsString();
		updateCashString();
		updateLevel(master.profile.getLevel());
	}

	public static void update(float delta, Profile profile) {
		updateCoinCashStrings(delta, profile);
	
		// don't update every frame, just on load and when level-up on summary
//		updateLevel(profile);
		
		// update fields of all relevant UI things
		if (master.getScreen() != master.kitchen && pauseVisible) {
			// set disabled is actually switching to settings box
			switchPauseToSettings();
			pauseVisible = false;

//			pauseSettingsButton.
			//					muteButton.setTouchable(Touchable.disabled);
			//					if (uiTable.getCell(muteButton) != null) {
			//						uiTable.getCell(muteButton).setActor(pauseButton).height(barHeight).width(barHeight).left().expandX();
			//					}
		}
		if (master.getScreen() == master.kitchen && !pauseVisible) {
			switchSettingsToPause();
			pauseVisible = true;

			//					muteButton.setTouchable(Touchable.enabled);
			//					if (uiTable.getCell(pauseButton) != null) {
			//						uiTable.getCell(pauseButton).setActor(muteButton).height(barHeight).width(barHeight).left().expandX();
			//					}
		}
		if (master.getScreen() == master.settingsScreen) {
//			pauseSettingsButton.setVisible(false);
		}
		else 
			pauseSettingsButton.setVisible(true);
		
		if (profile.inventory.adCampaign.getActive() != null) {
			updateAdCampaignBar(profile.inventory.adCampaign.getActive().getName());
			campaignActive = true;
		}
		else if (campaignActive) {
			campaignActive = false;
			updateAdCampaignBar(null);
			
			
			// hacky, but should work.
			master.store.storeScreen.campaignEnded();
		}
	}
	
	public static void switchPauseToSettings() {
		pauseSettingsButton.setDisabled(true);
		pauseSettingsButton.setTouchable(Touchable.enabled);
	}
	
	public static void switchSettingsToPause() {
		pauseSettingsButton.setDisabled(false);
		pauseSettingsButton.setTouchable(Touchable.enabled);
	}
	
	public static void updateLevel(int lvl) {
		if (levelDisplayed != lvl) {
			levelDisplayed = lvl;
			level.setText(LanguageManager.localizeNumber(levelDisplayed));
		}
	}

	// change this to generate projectiles with appropriate values
	// update coins/cash strings only when the projectiles are destroyed
	// called every frame, updates strings and ints as necessary
	public static void updateCoinCashStrings(float delta, Profile profile) {
		timeElapsed += delta;

		float baseCoinsRate = 1f;
		float minCoinsRate = 3f;
		
		// solution: have hidden variable that is a float, increasing every frame by small amount!
		if (coinsDisplayed != profile.getCoins()) {
			float min = minCoinsRate;
			if (profile.getCoins() < coinsDisplayedF) min = -baseCoinsRate;
			float coinsRate = baseCoinsRate * (profile.getCoins() - coinsDisplayedF) + min;
			coinsDisplayedF += coinsRate * delta;
			coinsDisplayed = (int) (coinsDisplayedF + 0.5f);
			updateCoinsString();
			updateCoinsAlpha();
		}
		else {
			coinsAlpha = 1;
			updateCoinsAlpha();
		}
		
		float baseCashRate = 2f;
		float minCashRate = 2f;

		if (cashDisplayed != profile.getCash()) {
			float min = minCashRate;
			if (profile.getCash() < cashDisplayedF) min = -minCashRate;
			float cashRate = baseCashRate * (profile.getCash() - cashDisplayedF) + min;
			cashDisplayedF += cashRate * delta;
			cashDisplayed = ((int) ((cashDisplayedF) * 2) + 1) / 2.0f;
			updateCashString();
			updateCashAlpha();
		}
		else {
			cashAlpha = 1;
			updateCashAlpha();
		}
		
//		if (cashDisplayed != profile.getCash()) {
//			if (!cashUpdateActive) {
//				cashUpdateActive = true;
//				cashUpdateRate = TIME_TO_UPDATE / (profile.getCash() - cashDisplayed);
//				cashUpdateRate = Math.min(cashUpdateRate, MAX_UPDATE_RATE);
//				nextUpdateCash = timeElapsed + cashUpdateRate;
//			}
//			if (timeElapsed > nextUpdateCash) {
//				if (cashDisplayed < profile.getCash()) cashDisplayed += 0.5;
//				else cashDisplayed -= 0.5;
//
//				nextUpdateCash += cashUpdateRate;
//				updateCashString();
//			}
//
//			if (!cashAlphaUp) {
//				cashAlpha -= COINS_FLASH_RATE * delta;
//				if (cashAlpha < MIN_ALPHA) {
//					cashAlpha = MIN_ALPHA; 
//					cashAlphaUp = true;
//				}
//			}
//			else {
//				cashAlpha += COINS_FLASH_RATE * delta;
//				if (cashAlpha > 1) {
//					cashAlpha = 1; 
//					cashAlphaUp = false;
//				}
//			}
//			updateCashAlpha();
//		}
//		else {
//			cashUpdateActive = false;
//			cashAlpha = 1;
//			updateCashAlpha();
//		} 
	}
	
	public static void updateFor(Projectile projectile) {
		if (projectile.jade) {
			
		}
		else {
			
		}
	}


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

//			float rightPad = KebabKing.getGlobalX(STAR_PAD);
//			float leftPad = KebabKing.getGlobalX(STAR_PAD);
			float rightPad = 0;
			float leftPad = 0;
			
			// each "image" can only be in one place at a time.
			while (starCount >= 1) {
				starTable.add(star[(int) starCount - 1]).height(starHeight).width(starHeight).padRight(rightPad).padLeft(leftPad).expand();
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
//				System.out.println("UPDATING AD CAMPAIGN BAR");

				adCampaignTable.setVisible(true);

				Table labelTable = new Table();
				labelTable.setBackground(new TextureRegionDrawable(Assets.red));
				//				labelTable.debugAll();
				//			labelTable.setBackground();
				Label adCampaignLabel = new Label(labelText + ": ", Assets.generateLabelStyleUIChina(16, labelText + " :"));
//				adCampaignLabel.setColor(Assets.YELLOW);
				adCampaignLabel.setAlignment(Align.center);
				labelTable.add(adCampaignLabel).left();
				adCampaignTable.add(labelTable).left().width(KebabKing.getGlobalX(textWidth)).height(KebabKing.getGlobalY(AD_BAR_HEIGHT));
				adCampaignTable.add(bar).left().fillY().expandY();

			}
			// update length of bar
			float barWidth = (1 - master.profile.inventory.campaignPercent()) * (1 - textWidth);			
			bar.setWidth(KebabKing.getGlobalX(barWidth));
		}
		else if (adCampaignTable.isVisible()) {
			adCampaignTable.clear();
			// visibility is used as a boolean flag
			adCampaignTable.setVisible(false);
		}
	}

	public static void updateCoinsString() {
		//		coinsString = "" + coinsDisplayed;
		String text = LanguageManager.localizeNumber(coinsDisplayed);
//		System.out.println("UPDATE COINS STRING: " + text);
		coins.setText(text);
//		coins.setText(""+coinsDisplayed);
	}

	public static void updateCashString() {
		cash.setText(LanguageManager.localizeNumber(cashDisplayed));
	}

	public static void updateCoinsAlpha() {
		if (!COIN_FLASH_ENABLED) return;
		coins.setColor(coins.getColor().r, coins.getColor().g,coins.getColor().b, coinsAlpha);
	}
	public static void updateCashAlpha() {
		if (!COIN_FLASH_ENABLED) return;
		cash.setColor(cash.getColor().r, cash.getColor().g,cash.getColor().b, cashAlpha);
	}

	public static void clickPause() {
		System.out.println("PAUSING");
		master.kitchenPause();
	}
	
	public static void clickSettings() {
		System.out.println("SETTINGS");
		master.switchToSettings();
	}
}
