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

public class TopBar extends Table {
	static final float UI_BAR_HEIGHT = 0.054f; 
	static final float AD_BAR_HEIGHT = 0.015f; 

	static final float STAR_HEIGHT = 0.022f;
	static final float STAR_TABLE_SIZE_FACTOR = 0.8f;
//	static final float STAR_PAD = 0.0015f;
	static final float STAR_TABLE_OFFSET = 0.01f;

	static final int CASH_COINS_SIZE = 16;

	static final float TIME_TO_UPDATE = 0.5f; // it should take this long to update the cash/coins every time there is a change	
	static final float MAX_UPDATE_RATE = 0.1f; // don't let update rate be higher, otherwise it looks slow

	static final boolean COIN_FLASH_ENABLED = true;
	static final float COINS_FLASH_RATE = 3f; // this much alpha per second.
	static final float MIN_ALPHA = 0.3f;

	static final int REPUTATION_FONT_SIZE = 11;
	public static Color blue = new Color(69f/256, 112f/256, 159f/256, 1);
//	static final Color REPUTATION_FONT_COLOR = new Color(0f, .2f, .3f, 0.5f);	
	static final Color REPUTATION_FONT_COLOR = new Color(blue.r, blue.g, blue.b, 0.5f);
	public static KebabKing master;

	static Button pauseButton;
	static Button muteButton;

	static float barHeight;
	static float adBarHeight;

	static Table starTable;
	static Table reputationTable;
	static float currentStarCount;

	static Label level;
	static Label coins;
	static Label cash;

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

	static boolean coinsUpdateActive;
	static boolean cashUpdateActive;

	static float coinsUpdateRate; // update one coin every x seconds
	static float cashUpdateRate; // update one cash every x seconds

	static float nextUpdateCoins;
	static float nextUpdateCash;

	static float coinsAlpha = 1;
	static float cashAlpha = 1;
	static boolean coinsAlphaUp; // is the alpha increasing or decreasing?
	static boolean cashAlphaUp;

	static float timeElapsed = 0;

	public TopBar(KebabKing masterIn) {
		master = masterIn;

		// calculate coins bg size
		float unit = 1/10f;
		float mute_size = KebabKing.getGlobalX(unit);
		float pause_size = KebabKing.getGlobalX(unit);
		float level_bg_size = KebabKing.getGlobalX(1.4f * unit);
		float starTableWidth = KebabKing.getGlobalX(2.7f * unit);
		float cash_bg_size = KebabKing.getGlobalX(2.3f * unit);
		float coins_bg_size = KebabKing.getGlobalX(1.75f * unit);
		float bars_pad_top = 0.00f*coins_bg_size;
		//		float plusSize = 0.6f*barHeight;

		barHeight = KebabKing.getGlobalY(UI_BAR_HEIGHT);
		adBarHeight += KebabKing.getGlobalY(AD_BAR_HEIGHT);

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
		this.add(pauseButton).height(barHeight).width(pause_size).left();
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
		this.add(muteButton).height(barHeight).width(mute_size).left().expandX();

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
		Label reputationLabel = new Label("REPUTATION", Assets.generateLabelStyleUIWhite(REPUTATION_FONT_SIZE, "REPUTATION"));
		reputationLabel.setColor(REPUTATION_FONT_COLOR);
		reputationTable.add(reputationLabel).center();
		reputationTable.row();

		starTable = new Table();
		reputationTable.add(starTable).width(starTableWidth * STAR_TABLE_SIZE_FACTOR).center();
		updateStars(master.profile.getCurrentReputation());

		//		starTable.setBackground(Assets.getTopBarBG());
		this.add(reputationTable).expandX(); //width(starTableWidth).

		//		int TEXT_SIZE = (int) (barHeight * 0.35f);

		// Add level table
		level = new Label("", Assets.generateLabelStyleUIWhite(CASH_COINS_SIZE, Assets.nums));
		level.setColor(blue);
		Label levelLabel = new Label("LVL", Assets.generateLabelStyleUIWhite(REPUTATION_FONT_SIZE, "LVL"));
		
		Table levelTable = new Table();
//		levelTable.debugAll();
		levelTable.add(levelLabel).left().padLeft(KebabKing.getGlobalX(0.015f));
		
		levelTable.setBackground(new TextureRegionDrawable(Assets.getTextureRegion("topbar/level")));
		levelTable.add(level).center().expand();
		level.setAlignment(Align.center);
		this.add(levelTable).width(level_bg_size).height(barHeight);
		
		// Add coins and cash table
		coins = new Label("", Assets.generateLabelStyleUIWhite(CASH_COINS_SIZE, Assets.nums + "." + Assets.currencyChar));
		cash = new Label("", Assets.generateLabelStyleUIWhite(CASH_COINS_SIZE, Assets.nums + "." + Assets.currencyChar));
		coins.setColor(blue);
		cash.setColor(blue);

		Table cashTable = new Table();
		//		Label cash_l = new Label("$", Assets.generateLabelStyle(TEXT_SIZE));
		//		cashTable.add(cash_l);
		//		cashTable.debugAll();
		cashTable.setBackground(new TextureRegionDrawable(Assets.getCashBG()));
		cashTable.add(cash).center().expand().padTop(bars_pad_top).padLeft(coins_bg_size * 0.4f);
		cash.setAlignment(Align.center);
		this.add(cashTable).width(cash_bg_size).height(barHeight);

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
		this.add(coinsTable).width(coins_bg_size).height(barHeight);

		this.row();
		adCampaignTable = new Table();
		adCampaignTable.setVisible(false);
		bar = new Table();
		bar.setBackground(new TextureRegionDrawable(Assets.red));
		this.add(adCampaignTable).colspan(5).height(KebabKing.getGlobalY(AD_BAR_HEIGHT)).left();

		coinsDisplayed = master.profile.getCoins();
		cashDisplayed = master.profile.getCash();
		updateCoinsString();
		updateCashString();
	}

	public static void update(float delta, ProfileRobust profile) {
		updateCoinCashStrings(delta, profile);
		updateLevel(profile);
		
		// update fields of all relevant UI things
		if (master.getScreen() == master.kitchen) {
			pauseButton.setVisible(true);
			pauseButton.setTouchable(Touchable.enabled);
			//					muteButton.setTouchable(Touchable.disabled);
			//					if (uiTable.getCell(muteButton) != null) {
			//						uiTable.getCell(muteButton).setActor(pauseButton).height(barHeight).width(barHeight).left().expandX();
			//					}
		}
		else {
			pauseButton.setVisible(false);
			pauseButton.setTouchable(Touchable.disabled);
			//					muteButton.setTouchable(Touchable.enabled);
			//					if (uiTable.getCell(pauseButton) != null) {
			//						uiTable.getCell(pauseButton).setActor(muteButton).height(barHeight).width(barHeight).left().expandX();
			//					}
		}
		
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
	
	public static void updateLevel(ProfileRobust profile) {
		if (levelDisplayed != profile.getLevel()) {
			levelDisplayed = profile.getLevel();
			level.setText(""+levelDisplayed);
		}
	}

	// called every frame, updates strings and ints as necessary
	public static void updateCoinCashStrings(float delta, ProfileRobust profile) {
		timeElapsed += delta;

		if (coinsDisplayed != profile.getCoins()) {
			if (!coinsUpdateActive) {
				coinsUpdateActive = true;
				// TODO make this rate dependent on the difference between displayed and actual
				coinsUpdateRate = TIME_TO_UPDATE / (profile.getCoins() - coinsDisplayed);
				coinsUpdateRate = Math.min(coinsUpdateRate, MAX_UPDATE_RATE);
				nextUpdateCoins = timeElapsed + coinsUpdateRate;
			}
			if (timeElapsed > nextUpdateCoins) {
				if (coinsDisplayed < profile.getCoins()) coinsDisplayed++;
				else coinsDisplayed--;

				nextUpdateCoins += coinsUpdateRate;
				updateCoinsString();

				if (!coinsAlphaUp) {
					coinsAlpha -= COINS_FLASH_RATE * delta;
					if (coinsAlpha < MIN_ALPHA) {
						coinsAlpha = MIN_ALPHA; 
						coinsAlphaUp = true;
					}
				}
				else {
					coinsAlpha += COINS_FLASH_RATE * delta;
					if (coinsAlpha > 1) {
						coinsAlpha = 1; 
						coinsAlphaUp = false;
					}
				}
				updateCoinsAlpha();
			}
		}
		else {
			coinsUpdateActive = false;
			coinsAlpha = 1;
			updateCoinsAlpha();
		}

		if (cashDisplayed != profile.getCash()) {
			if (!cashUpdateActive) {
				cashUpdateActive = true;
				cashUpdateRate = TIME_TO_UPDATE / (profile.getCash() - cashDisplayed);
				cashUpdateRate = Math.min(cashUpdateRate, MAX_UPDATE_RATE);
				nextUpdateCash = timeElapsed + cashUpdateRate;
			}
			if (timeElapsed > nextUpdateCash) {
				if (cashDisplayed < profile.getCash()) cashDisplayed += 0.5;
				else cashDisplayed -= 0.5;

				nextUpdateCash += cashUpdateRate;
				updateCashString();
			}

			if (!cashAlphaUp) {
				cashAlpha -= COINS_FLASH_RATE * delta;
				if (cashAlpha < MIN_ALPHA) {
					cashAlpha = MIN_ALPHA; 
					cashAlphaUp = true;
				}
			}
			else {
				cashAlpha += COINS_FLASH_RATE * delta;
				if (cashAlpha > 1) {
					cashAlpha = 1; 
					cashAlphaUp = false;
				}
			}
			updateCashAlpha();
		}
		else {
			cashUpdateActive = false;
			cashAlpha = 1;
			updateCashAlpha();
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
		coins.setText( "" + coinsDisplayed);
	}

	public static void updateCashString() {
		cash.setText("" + cashDisplayed);
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

	public static void clickMute(ProfileRobust profile) {
		System.out.println("Mutings");
		master.toggleMute();
		setProperMuteStyle(profile);
	}

	public static void setProperMuteStyle(ProfileRobust profile) {
		if (profile.settings.muteMusic)
			muteButton.setStyle(Assets.getButtonStyleMuted());
		else 
			muteButton.setStyle(Assets.getButtonStyleUnmuted());
	}

}
