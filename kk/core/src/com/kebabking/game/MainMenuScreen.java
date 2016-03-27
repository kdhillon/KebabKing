package com.kebabking.game;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class MainMenuScreen extends ActiveScreen {
	static final boolean FADE_IN_GRILL = false;
	public static int daysPlayedThisSession = 0;
	public static int marketClicksThisSession = 0;

	static final float INIT_BG_ALPHA = 0.45f;
	static final float FADEOUT_TIME = 1f; // it should take this many seconds to fade out towards countdown screen
	static final float BUTTON_WIDTH = 0.32f;
	static final float BUTTON_GAP = 0.1f;
	static final float DISPOSE_SPLASH_TIME = 1;

	Table table;

	Table startDay;
	//	Image mute;
	//	Image connectToFB;

	boolean stageSet;

	boolean fadeout;
	float fadeoutTimer; 
	//	Color currentBatchColor; // alpha will change as fades out
	float bgTint = INIT_BG_ALPHA;

	// dispose the splash screen after a few seconds
	public boolean disposedSplash = false;

	public MainMenuScreen(KebabKing master) {	
		super(master, true, "Main Menu");

		fadeout = false;
		fadeoutTimer = 0;
		//		currentBatchColor = new Color(1, 1, 1, 1);

		// update everything else after resize has been called
		initializeStage();

		SoundManager.playMusic();
	}

	public void initializeStage() {	
		//		uiStage.setDebugAll(true);

		table = new Table();
		uiStage.addActor(table);
		table.setSize(KebabKing.getWidth(), KebabKing.getHeight());

		addTopTable();

		Image title = new Image(Assets.title);
		float titleWidth = KebabKing.getGlobalX(0.45f);
		float titleHeight = titleWidth * Assets.title.getRegionHeight() /Assets.title.getRegionWidth();
		table.add(title).size(titleWidth, titleHeight).padBottom(KebabKing.getGlobalY(0.04f)).padTop(KebabKing.getGlobalYFloat(0.03f));

		table.row();

		startDay = DrawUI.getBlueButton(Assets.strings.get("play"), 60);

		//		TextureRegion bg = Assets.getTextureRegion("screens/pause-03");		
		//		float width = KebabKing.getGlobalX(0.55f);
		//		float height = width * bg.getRegionHeight() / bg.getRegionWidth();

		table.add(startDay).center();//.width(width).height(height);

		startDay.addListener(new StrictInputListener() {
			public void touch(InputEvent event) {
				startFadeout();
			}
		});

		table.row();

		Table marketButton = DrawUI.getBlueButton(Assets.strings.get("market"), 60);

		//		float width2 = KebabKing.getGlobalX(0.7f);
		//		float height2 = width * bg.getRegionHeight() / bg.getRegionWidth();

		table.add(marketButton).center().padTop(KebabKing.getGlobalY(.015f)); //.width(width2).height(height2);//.width(KebabKing.getGlobalX(BUTTON_WIDTH)).height(KebabKing.getGlobalX(BUTTON_WIDTH));

		marketButton.addListener(new StrictInputListener() {
			public void touch(InputEvent event) {
				System.out.println("touched");
				master.mainToStore();
				marketClicksThisSession++;
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

	public void addTopTable() {
		Table topBar = new Table();
		//		TextureRegion kebab = Assets.getTextureRegion("screens/tutorial_cooked");
		//		topBar.setBackground(new TextureRegionDrawable(kebab));
		//		topBar.debugAll();
		Table topLeft = new Table();
		Table topRight = new Table();
		topLeft.setBackground(new TextureRegionDrawable(Assets.white));
		topRight.setBackground(new TextureRegionDrawable(Assets.white));

		Label topText = new Label(Assets.strings.get("title"), Assets.generateLabelStyleUIChina(75, Assets.strings.get("title")));

		topBar.add(topLeft).expandX().fillX().height(KebabKing.getGlobalY(SummaryScreen.BAR_HEIGHT));

		float imagePadX = KebabKing.getGlobalX(0.05f);
		topBar.add(topText).top().padLeft(imagePadX).padRight(imagePadX);
		topBar.add(topRight).expandX().fillX().height(KebabKing.getGlobalY(SummaryScreen.BAR_HEIGHT));;

		float padTop = KebabKing.getGlobalY(.05f);
		float padBot = KebabKing.getGlobalY(.03f);

		//		KebabKing.getWidth() * 1f * kebab.getRegionHeight() / kebab.getRegionWidth()
		table.add(topBar).padTop(padTop).expandX().fillX().padBottom(padBot);
		//		topBar.debugAll();
		table.row();
	}

	public void reset() {
		this.bgTint = INIT_BG_ALPHA;
		//		this.currentBatchColor = new Color(1, 1, 1, 1);
		this.fadeout = false;
		this.fadeoutTimer = 0;
		table.setTouchable(Touchable.enabled);

		// later on, TODO, don't fade out grill
		uiStage.addAction(Actions.fadeIn(0));

		DrawUI.setInput(uiStage);
	}

	@Override
	public void render(float delta) {		
		float alpha = 1;
		if (FADE_IN_GRILL) alpha = fadeoutTimer / FADEOUT_TIME; 
		super.renderGrayAlpha(delta, bgTint, alpha);
		//		System.out.println("rendering " + bgTint);
		//		uiStage.draw();
	}

	@Override
	// actually run a game loop
	public void update(float delta, boolean ff) {
		super.update(delta, ff);
		uiStage.act(delta);

		grill.mousedOver = -1;
		//		grill.mousedOverTrash = false;
		cm.mousedOver = null;

		if (!this.disposedSplash && timeElapsed > DISPOSE_SPLASH_TIME) {
			master.disposeSplash();
			disposedSplash = true;
			System.out.println("Disposing splash " + timeElapsed);
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

	public void startDay() {

		this.master.startCountdown();
		//		this.master.startDay();
		daysPlayedThisSession++;
	}

	// This is a duplicate method.
	// TODO try to put this only in one parent (create a parent screen for everything to implement)
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void show() {
		super.show();

		//		if (this.startDay != null) 
		//			startDay.setText("Start!");
		//			startDay.setText("Start Day " + (getProfile().daysWorked + 1));
		// initialie uiStage here
		//		Gdx.input.setInputProcessor(uiStage);
		stageSet = false;
	}

	public void startFadeout() {
		if (master.profile.violationActive() && !DrawUI.notificationActive) {
			DrawUI.launchPoliceNotification();
			return;
		}

		this.fadeout = true;
		uiStage.addAction(Actions.fadeOut(FADEOUT_TIME));

		table.setTouchable(Touchable.disabled);
	}

	// changes batch color
	public void fadeout(float delta) {
		//		this.currentBatchColor.a = 1 - (fadeoutTimer / FADEOUT_TIME);
		//		this.batch.setColor(currentBatchColor);
		this.fadeoutTimer += delta;

		this.bgTint = INIT_BG_ALPHA * (1-(fadeoutTimer / FADEOUT_TIME));

		if (fadeoutTimer > FADEOUT_TIME) this.startDay();
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

	//	public void clickFacebook() {
	//		if (!Manager.fb.isLoggedIn())
	//			Manager.fb.login();
	//		else
	//		Manager.fb.inviteFriends();
	//	}

	//	private Profile getProfile() {
	//		return master.profile;
	//	}
}
