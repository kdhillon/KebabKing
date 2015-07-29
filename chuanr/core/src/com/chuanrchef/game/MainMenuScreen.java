package com.chuanrchef.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenuScreen extends ScreenTemplate {
	static final float FADEOUT_TIME = 1f; // it should take this many seconds to fade out towards countdown screen
	static final float BUTTON_WIDTH = 0.32f;
	static final float BUTTON_GAP = 0.1f;

	ChuanrC master;
	Background bg; 
	Grill grill;
	CustomerManager cm;

	Stage uiStage;
	Table table;

	TextButton startDay;
	Image mute;
	Image connectToFB;

	SpriteBatch batch;
	
	boolean stageSet;
	
	boolean fadeout;
	float fadeoutTimer; 
	Color currentBatchColor; // alpha will change as fades out

	public MainMenuScreen(ChuanrC master) {	
		this.master = master;
		this.bg = master.bg;
		this.grill = master.grill;
		this.cm = master.cm;

		this.batch = master.batch;

		fadeout = false;
		fadeoutTimer = 0;
		currentBatchColor = new Color(1, 1, 1, 1);
		
		// update everything else after resize has been called
	}

	public void initializeStage() {
		ScreenViewport viewport = new ScreenViewport();
		uiStage = new Stage(viewport, batch);		
//		uiStage.setDebugAll(true);

		table = new Table();
		uiStage.addActor(table);
		table.setSize(ChuanrC.width, ChuanrC.height);
		table.setPosition(0, 0);
		table.align(Align.center);
		table.align(Align.top);

		table.row();
		//		String string1 = "Kebab";
		//		String string2 = "Chef!";
		//		if (!ChuanrC.english) {
		//			string1 = "羊肉串";
		//			string2 = "厨师!";
		//		}

		//		Label title1 = new Label(string1, Assets.ls120);
		float padTop = .2f * ChuanrC.height;
		//		table.add(title1).padTop(padTop).colspan(2);
		//		table.row();
		//		Label title2 = new Label(string2, Assets.ls120);
		//		table.add(title2).padTop(-.05f * ChuanrC.height).colspan(2);
		Image title = new Image(Assets.title);
		
		float titleWidth = ChuanrC.width * 0.6f;
//		float titleHeight = titleWidth * (Assets.title.getRegionHeight()/Assets.title.getRegionWidth()); //(Assets.title.getRegionHeight() / Assets.title.getRegionWidth());
	
		table.add(title).padTop(padTop).colspan(2).size(titleWidth, titleWidth * 1.5f);

		table.row();

		startDay = new TextButton("Start Day!", Assets.getStartButtonStyle());
		table.add(startDay).padTop(.03f * ChuanrC.height).center().width(BUTTON_WIDTH*ChuanrC.width).height(BUTTON_WIDTH*ChuanrC.width).padRight(ChuanrC.width*BUTTON_GAP);

		startDay.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
				System.out.println("touchdown");
				return true;
			}
			public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
//				if (x < event.getTarget().getX() || x > event.getTarget().getX() + event.getTarget().getWidth()
//						|| y < event.getTarget().getY() || y > event.getTarget().getY() + event.getTarget().getHeight()) return;
				// should check if in bounds before releasing TODO
				System.out.println("touched");
				startFadeout();
//				transition();
			}
		});

		TextButton upgrades = new TextButton("Market", Assets.getMarketButtonStyle());
		table.add(upgrades).padTop(.03f * ChuanrC.height).center().width(BUTTON_WIDTH*ChuanrC.width).height(BUTTON_WIDTH*ChuanrC.width);

		upgrades.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
				System.out.println("touchdown");
				return true;
			}
			public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
				// should check if in bounds before releasing TODO
//				if (x < event.getTarget().getX() || x > event.getTarget().getX() + event.getTarget().getWidth()
//						|| y < event.getTarget().getY() || y > event.getTarget().getY() + event.getTarget().getHeight()) return;
				System.out.println("touched");
				master.mainToStore();
			}
		});	

		table.row();
//		connectToFB = new TextButton("FB Connect", Assets.tbs48);

//		float pad = 20;
//
//		addFBButton();
//		connectToFB.setVisible(false);
//
//		
//		mute = new Image(Assets.volOn);
//		if (getProfile().settings.muteMusic) mute.setDrawable(Assets.volMute);
//		
//		float volSize = 1.5f * KitchenScreen.UNIT_HEIGHT;
		
//		table.add(mute).right().expandY().bottom().size(volSize, volSize).padBottom(pad).padRight(pad);

//		mute.addListener(new InputListener() {
//			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
//				return true;
//			}
//			public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
////				if (x < event.getTarget().getX() || x > event.getTarget().getX() + event.getTarget().getWidth()
////						|| y < event.getTarget().getY() || y > event.getTarget().getY() + event.getTarget().getHeight()) return;
//				// change this to be more flexible change at any time
//				toggleMusic();
//			}
//		});
		
		
		this.show();
	}
	
	public void reset() {
		this.currentBatchColor = new Color(1, 1, 1, 1);
		this.fadeout = false;
		this.fadeoutTimer = 0;
	}

	@Override
	public void render(float delta) {
		// have to do this every frame for some reason?
		if (!stageSet && DrawUI.uiStage != null) {
			DrawUI.setInput(uiStage);
			stageSet = true;
		}

		update(delta);

		batch.begin();
		bg.draw(batch);
		cm.draw(batch);
		grill.draw(batch);
		
		DrawUI.drawFullUI(delta, batch, getProfile());
		
		
		// fix fading
		this.uiStage.getBatch().setColor(currentBatchColor);
//		DrawUI.drawStars(batch, getProfile());
//		DrawUI.drawMoney(batch, getProfile());
		
		batch.end();

		uiStage.draw();
		this.uiStage.getBatch().setColor(currentBatchColor);
		table.setColor(currentBatchColor);
	}

	// actually run a game loop
	public void update(float delta) {
		uiStage.act(delta);

		// just for testing
		boolean fastForward = false;
		if (Gdx.input.isKeyPressed(Keys.F))
			fastForward = true;

		grill.mousedOver = -1;
		grill.mousedOverTrash = false;
		cm.mousedOver = null;

		if (fastForward) {
			bg.act(delta);
			cm.act(delta);
			bg.act(delta);
			cm.act(delta);
			bg.act(delta);
			cm.act(delta);
			bg.act(delta);
			cm.act(delta);
			bg.act(delta);
			cm.act(delta);
			bg.act(delta);
			cm.act(delta);
			bg.act(delta);
			cm.act(delta);
			bg.act(delta);
			cm.act(delta);
		}
		else {
			bg.act(delta);
			cm.act(delta);
		}
		
		if (this.fadeout) this.fadeout(delta);

		// add booleans to make this less intensive
//		if (master.platformSpec != null && master.platformSpec.isLoggedIn()) {
//			connectToFB.setVisible(false);
////			connectToFB.setTouchable(Touchable.disabled);
//		}
//		else {
//			connectToFB.setVisible(true);
////			connectToFB.setTouchable(Touchable.enabled);
//		}
	}

	public void transition() {
		this.master.startCountdown();
	}


	@Override
	public void resize(int width, int height) {
		ChuanrC.width = width;
		ChuanrC.height = height;

		// initialize once
		if (KitchenScreen.UNIT_HEIGHT == 0 && KitchenScreen.UNIT_WIDTH == 0) {
			KitchenScreen.UNIT_WIDTH = (int) (ChuanrC.width / KitchenScreen.WIDTH);
			KitchenScreen.UNIT_HEIGHT = (int) (ChuanrC.height / KitchenScreen.HEIGHT);
		}

		initializeStage();
	}

	@Override
	public void show() {
		if (this.startDay != null) 
			startDay.setText("Start!");
			//			startDay.setText("Start Day " + (getProfile().daysWorked + 1));

		// this doesn't work well for some reason
//		Gdx.input.setInputProcessor(uiStage);
		stageSet = false;
	}
	
	public void startFadeout() {
		this.fadeout = true;
		table.setTouchable(Touchable.disabled);
	}
	
	// changes batch color
	public void fadeout(float delta) {
		this.currentBatchColor.a = 1 - (fadeoutTimer / FADEOUT_TIME);
		this.batch.setColor(currentBatchColor);
		this.fadeoutTimer += delta;
		
		if (fadeoutTimer > FADEOUT_TIME) this.transition();
	}
	
	public void load() {
		master.load();
		
		// update relevent buttons
		this.show();
	}
	
//	public void addFBButton() {
//		float pad = 20;
//		float fbPad = 5;
//		
//		float fbWidth = 8f * KitchenScreen.UNIT_WIDTH;
//		float fbHeight = 1.25f * KitchenScreen.UNIT_HEIGHT;
//		
//		connectToFB = new Image(Assets.facebook);
//		table.add(connectToFB).left().expandX().expandY().bottom().size(fbWidth, fbHeight).padLeft(fbPad).padBottom(pad);
//		
//
//		connectToFB.addListener(new InputListener() {
//			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
//				return true;
//			}
//			
//			public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
//				if (x < event.getTarget().getX() || x > event.getTarget().getX() + event.getTarget().getWidth()
//						|| y < event.getTarget().getY() || y > event.getTarget().getY() + event.getTarget().getHeight()) return;
//				// should check if in bounds before releasing TODO
//				// change this to be more flexible, change at any time.
//				deleteSave();
////				purchaseCoins(PurchaseManager.Purchaseable.FIVE_COINS);
////				clickFacebook();
////				load();
//			}
//		});	
//	}
	
	public void purchaseCoins(OnlinePurchaseManager.PurchaseableOnline choice) {
		this.master.makePurchase(choice);
	}
	
//	public void deleteSave() {
//		master.deleteProfile();
//	}
	
	public void toggleMusic() {
		if (!getProfile().settings.muteMusic) {
			getProfile().settings.muteMusic();
			mute.setDrawable(Assets.volMute);
		}
		else {
			getProfile().settings.unmuteMusic();
			mute.setDrawable(Assets.volOn);
		}
	}
	
	public void clickFacebook() {
		if (master.platformSpec != null && !master.platformSpec.isLoggedIn())
			master.platformSpec.logIn();
		else if (master.platformSpec != null) {
			
			master.platformSpec.inviteFriends();
//			master.facebook.logOut();
		}
	}
	
	private Profile getProfile() {
		return master.profile;
	}
}
