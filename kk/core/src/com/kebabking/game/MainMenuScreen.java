package com.kebabking.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
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
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kebabking.game.Managers.Manager;

public class MainMenuScreen extends ScreenTemplate {
	static final float INIT_BG_ALPHA = 0.45f;
	static final float FADEOUT_TIME = 1f; // it should take this many seconds to fade out towards countdown screen
	static final float BUTTON_WIDTH = 0.32f;
	static final float BUTTON_GAP = 0.1f;

	KebabKing master;
	Background bg; 
	Grill grill;
	CustomerManager cm;

	Stage uiStage;
	Table table;

	Table startDay;
//	Image mute;
//	Image connectToFB;

	SpriteBatch batch;
	
	boolean stageSet;
	
	boolean fadeout;
	float fadeoutTimer; 
	Color currentBatchColor; // alpha will change as fades out
	float bgTint = INIT_BG_ALPHA;

	public MainMenuScreen(KebabKing master) {	
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
		table.setSize(KebabKing.getWidth(), KebabKing.getHeight());
//		table.setPosition(0, 0);
//		table.align(Align.center);
//		table.align(Align.top);

//		table.row();
		//		String string1 = "Kebab";
		//		String string2 = "Chef!";
		//		if (!ChuanrC.english) {
		//			string1 = "羊肉串";
		//			string2 = "厨师!";
		//		}

		//		Label title1 = new Label(string1, Assets.ls120);
		
		float padTop = KebabKing.getGlobalY(.08f);
		float padBot = KebabKing.getGlobalY(.03f);
		Table topBar = generateTopTable();
		table.add(topBar).padTop(padTop).expandX().fillX().padBottom(padBot);
		table.row();
		
		Image title = new Image(Assets.title);
		float titleWidth = KebabKing.getGlobalX(0.45f);
		float titleHeight = titleWidth * Assets.title.getRegionHeight() /Assets.title.getRegionWidth();
		table.add(title).size(titleWidth, titleHeight).padBottom(KebabKing.getGlobalY(0.04f));

		table.row();

//		startDay = new TextButton("Start Day!", Assets.getStartButtonStyle());
//		table.add(startDay).padTop(KebabKing.getGlobalY(.03f)).center().width(KebabKing.getGlobalX(BUTTON_WIDTH)).height(KebabKing.getGlobalX(BUTTON_WIDTH)).padRight(KebabKing.getGlobalX(BUTTON_GAP));

		startDay = new Table();
		TextureRegion bg = Assets.getTextureRegion("screens/pause-03");
		startDay.setBackground(new TextureRegionDrawable(bg));
		
		Label startLabel = new Label("\nPlay\n", Assets.generateLabelStyleUIChinaWhite(55));
		startDay.add(startLabel).center().padRight(KebabKing.getGlobalX(0.025f)).padBottom(KebabKing.getGlobalY(0.004f));
		
		float width = KebabKing.getGlobalX(0.55f);
		float height = width * bg.getRegionHeight() / bg.getRegionWidth();
		
		table.add(startDay).center().width(width).height(height);
		
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

		table.row();
		
		Table upgrades = new Table();
		upgrades.setBackground(new TextureRegionDrawable(bg));
		
		float width2 = KebabKing.getGlobalX(0.7f);
		float height2 = width * bg.getRegionHeight() / bg.getRegionWidth();
		
		Label marketLabel = new Label("\nMarket\n", Assets.generateLabelStyleUIChinaWhite(55));
		upgrades.add(marketLabel).center().padRight(KebabKing.getGlobalX(0.025f)).padBottom(KebabKing.getGlobalY(0.004f));
		table.add(upgrades).center().width(width2).height(height2).padTop(KebabKing.getGlobalY(.015f)); //.width(KebabKing.getGlobalX(BUTTON_WIDTH)).height(KebabKing.getGlobalX(BUTTON_WIDTH));

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
	
	public Table generateTopTable() {
		Table topBar = new Table();
//		topBar.debugAll();
		Table topLeft = new Table();
		Table topRight = new Table();
		topLeft.setBackground(new TextureRegionDrawable(Assets.white));
		topRight.setBackground(new TextureRegionDrawable(Assets.white));
		
		Label topText = new Label("Kebab King", Assets.generateLabelStyleUIChinaWhite(60));
		
		topBar.add(topLeft).expandX().fillX().height(KebabKing.getGlobalY(SummaryScreen.BAR_HEIGHT));
		
		float imagePadX = KebabKing.getGlobalX(0.1f);
		topBar.add(topText).top().padLeft(imagePadX).padRight(imagePadX);
		topBar.add(topRight).expandX().fillX().height(KebabKing.getGlobalY(SummaryScreen.BAR_HEIGHT));;
		return topBar;
	}
	
	public void reset() {
		this.bgTint = INIT_BG_ALPHA;
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
		
//		Color prev = batch.getColor();
		batch.setColor(currentBatchColor);
		cm.draw(batch);
		batch.setColor(Color.WHITE);
	
		grill.draw(batch);
		
		DrawUI.tintGrayAlpha(batch, bgTint);		
		DrawUI.drawFullUI(delta, batch, getProfile());
		
		// fix fading
//		DrawUI.drawStars(batch, getProfile());
//		DrawUI.drawMoney(batch, getProfile());
		
		batch.end();
		
//		table.setColor(currentBatchColor);
//		this.uiStage.getBatch().setColor(currentBatchColor);	
		uiStage.draw();
//		table.setColor(prev);
//		this.uiStage.getBatch().setColor(prev);		
//		this.uiStage.getBatch().setColor(currentBatchColor);
//		table.setColor(currentBatchColor);
	}

	// actually run a game loop
	public void update(float delta) {
		uiStage.act(delta);

		// just for testing
		boolean fastForward = false;
		if (Gdx.input.isKeyPressed(Keys.F))
			fastForward = true;

		grill.mousedOver = -1;
//		grill.mousedOverTrash = false;
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


	// This is a duplicate method.
	// TODO try to put this only in one parent (create a parent screen for everything to implement)
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		initializeStage();
	}

	@Override
	public void show() {
//		if (this.startDay != null) 
//			startDay.setText("Start!");
			//			startDay.setText("Start Day " + (getProfile().daysWorked + 1));

		// this doesn't work well for some reason
//		Gdx.input.setInputProcessor(uiStage);
		stageSet = false;
	}
	
	public void startFadeout() {
		this.fadeout = true;
		uiStage.getRoot().addAction(Actions.fadeOut(FADEOUT_TIME));

		table.setTouchable(Touchable.disabled);
	}
	
	// changes batch color
	public void fadeout(float delta) {
		this.currentBatchColor.a = 1 - (fadeoutTimer / FADEOUT_TIME);
//		this.batch.setColor(currentBatchColor);
		this.fadeoutTimer += delta;
		
		this.bgTint = INIT_BG_ALPHA * (1-(fadeoutTimer / FADEOUT_TIME));
		
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
	
//	public void toggleMusic() {
//		if (!getProfile().settings.muteMusic) {
//			getProfile().settings.muteMusic();
//			mute.setDrawable(Assets.volMute);
//		}
//		else {
//			getProfile().settings.unmuteMusic();
//			mute.setDrawable(Assets.volOn);
//		}
//	}
	
	public void clickFacebook() {
		if (!Manager.fb.isLoggedIn())
			Manager.fb.login();		
		else 
		Manager.fb.inviteFriends();
	}
	
	private Profile getProfile() {
		return master.profile;
	}
}
