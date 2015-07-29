package com.chuanrchef.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

// make it draw a little bar at the top!

/** Static class for drawing UI elements */
public class DrawUI {
	static final Color WHITE = new Color(1, 1, 1, 1);
	static final Color GRAY = new Color(.2f, .2f, .2f, .5f);
//	static final Color DARK_GRAY = new Color(.2f, .2f, .2f, .9f);
	
	static final float UI_BAR_HEIGHT = 0.06f; // 
	
//	static final float xStarOffset = 0.06f;
//	static final float X_STAR_INIT = 0.12f;
	
	static final float STAR_WIDTH = 0.26f;
	static final float STAR_HEIGHT = 0.8f;
	
//	static final float COINS_X  = 0.02f * ChuanrC.width;
//	static final float COINS_Y	= 0.982f * ChuanrC.height;
//	
//	static final float MONEY_X = 0.70f * ChuanrC.width;
//	static final float MONEY_Y = 0.982f * ChuanrC.height;
	
	static final float TIME_X = 0.4f * ChuanrC.width;
	static final float TIME_Y = 0.98f * ChuanrC.height;
	
	static Stage uiStage;
	static ChuanrC master;
	static Table uiTable;
	static Button pauseButton;
	static Button muteButton;
	
	static float barHeight;
	
	static Table starTable;
	static float starWidth;
	static float currentStarCount;
	
	static Label coins;
	static Label cash;
	
	static Label time;
	
	static Color color;
	
	public static void setInput(InputProcessor ip) {
		InputMultiplexer im = new InputMultiplexer();
		im.addProcessor(DrawUI.uiStage);
		im.addProcessor(ip);
		Gdx.input.setInputProcessor(im);
	}
	
	public static void initializeUIBar(ChuanrC master_in, SpriteBatch batch) {
		master = master_in;
		
		ScreenViewport viewport = new ScreenViewport();
		uiStage = new Stage(viewport, batch);		
		
		barHeight = UI_BAR_HEIGHT * ChuanrC.height;
		
		uiTable = new Table();
//		uiTable.debugAll();
		
		uiTable.background(Assets.getTopBarBG());
		uiTable.setPosition(0, ChuanrC.height - barHeight);
		uiTable.setSize(ChuanrC.width, barHeight+5);
		
		uiStage.addActor(uiTable);
			
		// calculate coins bg size
		float coins_bg_size = barHeight * 2.8f;
		float cash_bg_size = barHeight * 3.2f;
		float bars_pad_top = 0.00f*coins_bg_size;
		float plusSize = 0.6f*barHeight;
		
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
		uiTable.add(pauseButton).height(barHeight).width(barHeight).left();
		
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
//		uiTable.add(muteButton).height(barHeight).width(barHeight).left().expandX();
		
		
		float starTableWidth = STAR_WIDTH * ChuanrC.width;
		starWidth = starTableWidth / 5;
		
		starTable = new Table();
//		starTable.setBackground(Assets.getTopBarBG());
		uiTable.add(starTable).expandX(); //width(starTableWidth).
		
		int TEXT_SIZE = (int) (barHeight * 0.45f);

//		// eventually make text size related to height of the bar!
		// make it permanent
		coins = new Label("", Assets.generateLabelStyle(TEXT_SIZE, true));
		cash = new Label("", Assets.generateLabelStyle(TEXT_SIZE, true));
	
	
		Table cashTable = new Table();
//		Label cash_l = new Label("$", Assets.generateLabelStyle(TEXT_SIZE));
//		cashTable.add(cash_l);
//		cashTable.debugAll();
		cashTable.setBackground(new TextureRegionDrawable(Assets.getCashBG()));
		cashTable.add(cash).right().expand().padTop(bars_pad_top).padRight(coins_bg_size * .08f);
		cash.setAlignment(Align.right);
		uiTable.add(cashTable).width(cash_bg_size).height(barHeight*1.1f);
		
		Table coinsTable = new Table();
//		coinsTable.debugAll();
		coinsTable.setBackground(new TextureRegionDrawable(Assets.getCoinsBG()));
//		Label coins_l = new Label("Coins: ", Assets.generateLabelStyle(TEXT_SIZE));
//		coinsTable.add(coins_l);
		coinsTable.add(coins).right().expand().padTop(bars_pad_top).padRight(coins_bg_size * 0.04f);
		Button coinPlus = new Button();
		coinPlus.setStyle(Assets.getCoinPlusStyle());
		coinPlus.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
				clickCoinPlus();
			}
		});
		
		coinsTable.add(coinPlus).right().padTop(bars_pad_top + 0.1f*barHeight).width(plusSize).height(plusSize);
		coins.setAlignment(Align.right);
		uiTable.add(coinsTable).width(coins_bg_size).height(barHeight);
		
		time = new Label("", Assets.generateLabelStyle(200 * ChuanrC.width / 480, true));
		time.setPosition(0, ChuanrC.height - 4*barHeight);
		time.setWidth(ChuanrC.width);
		time.setAlignment(Align.center);
		
		color = new Color(1, 1, 1, 1);
	}

	public static void drawFullUI(float delta, SpriteBatch batch, Profile profile) {
		// update fields of all relevant UI things
		if (master.getScreen() == master.kitchen) {
			pauseButton.setVisible(true);
			pauseButton.setTouchable(Touchable.enabled);
			muteButton.setVisible(false);
			muteButton.setTouchable(Touchable.disabled);
			if (uiTable.getCell(muteButton) != null) {
				uiTable.getCell(muteButton).setActor(pauseButton).height(barHeight).width(barHeight).left().expandX();
			}
		}
		else {
			pauseButton.setVisible(false);
			pauseButton.setTouchable(Touchable.disabled);
			muteButton.setVisible(true);
			muteButton.setTouchable(Touchable.enabled);
			if (uiTable.getCell(pauseButton) != null) {
				uiTable.getCell(pauseButton).setActor(muteButton).height(barHeight).width(barHeight).left().expandX();
			}
		}
		
		coins.setText(""+profile.coins);
		cash.setText(""+profile.cash);
		
		// only call this when necessary! after rounds or purchases basically.
		updateStars(profile.getCurrentReputation());
		
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
	
	public static void drawGray(SpriteBatch batch) {
		// draw gray fade over everything
		Color c = batch.getColor();
		batch.setColor(GRAY);
		batch.draw(Assets.white, 0, 0, ChuanrC.width, ChuanrC.height);
		batch.setColor(c);
	}
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
		
		color.set(1,  1,  1, inputTime - (int) inputTime);
	
		time.setColor(color);
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
	public static void updateStars(float starCount) {
		if (currentStarCount != starCount) {
			starTable.clear();
			
			// calculate number of gray stars
			int grayStars = (int) (5 - starCount);
					
			float starHeight = starWidth * 1.1f;
			float rightPad = 0;
			
			while (starCount >= 1) {
				Image star = new Image(Assets.getStar());
				starTable.add(star).height(starHeight).padRight(rightPad).expand();
				starCount--;
			}
			
			// draw half star
			if (starCount > 0) {
				Image star = new Image(Assets.getHalfStar());
				starTable.add(star).height(starHeight).padRight(rightPad).expand();
			}
			
			// draw gray stars
			for (int i = 0; i < grayStars; i++) {
				Image star = new Image(Assets.getGrayStar());
				starTable.add(star).height(starHeight).padRight(rightPad).expand();
			}
			
			currentStarCount = starCount;
		}
	}
}
