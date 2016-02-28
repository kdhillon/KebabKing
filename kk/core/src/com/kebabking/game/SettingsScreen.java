package com.kebabking.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/* new pause screen created for every kitchen screen */
public class SettingsScreen extends ActiveScreen  {
	//	static String defaultDesc = "People are getting sick from your nasty ass meat! Luckily, you've got more options. Want to upgrade?";
	static String defaultDesc = "This is the settings screen";


	Table bigTable;
	
	Button musicButton;
	Button soundButton;

	public SettingsScreen(KebabKing master) {
		super(master, true);	

		bigTable = new Table();
		bigTable.setSize(KebabKing.getWidth(), KebabKing.getHeight());
		uiStage.addActor(bigTable);

		Table topBar = generateTopTable();
		bigTable.add(topBar).top().expandX().fillX().padTop(KebabKing.getGlobalY(0.25f));
		bigTable.row();

		Table textTable = generateTextTable();
		bigTable.add(textTable).padTop(KebabKing.getGlobalY(0.1f)).top();

		TextureRegion bg = Assets.getTextureRegion("screens/pause-03");		
		float width = KebabKing.getGlobalX(0.5f);
		float height = width * bg.getRegionHeight() / bg.getRegionWidth();
		bigTable.row();
		Table resumeTable = generateResumeButton();
		bigTable.add(resumeTable).width(width).height(height).top().expandY().padTop(KebabKing.getGlobalY(0.1f));
	}

	public Table generateTopTable() {
		Table topBar = new Table();
		//		topBar.debugAll();
		Table topLeft = new Table();
		Table topRight = new Table();
		topLeft.setBackground(new TextureRegionDrawable(Assets.white));
		topRight.setBackground(new TextureRegionDrawable(Assets.white));

		Label topText = new Label("SETTINGS", Assets.generateLabelStyleUIChinaWhite(50, "SETTINGS"));

		topBar.add(topLeft).expandX().fillX().height(KebabKing.getGlobalY(SummaryScreen.BAR_HEIGHT));

		float imagePadX = KebabKing.getGlobalX(0.1f);
		topBar.add(topText).top().padLeft(imagePadX).padRight(imagePadX);
		topBar.add(topRight).expandX().fillX().height(KebabKing.getGlobalY(SummaryScreen.BAR_HEIGHT));;
		return topBar;
	}

	public Table generateTextTable() {
		Table textTable = new Table();

		soundButton = new Button(Assets.getButtonStyleSound());
		soundButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
				Button b  = (Button) event.getListenerActor();
				if (b.isDisabled()) {
					SoundManager.unmuteSound();
					b.setDisabled(false);
				}
				else {
					SoundManager.muteSound();
					b.setDisabled(true);
				}
			}
		});

		float size = KebabKing.getGlobalXFloat(0.2f);

		textTable.add(soundButton).width(size).height(size).center().padRight(KebabKing.getGlobalXFloat(0.05f));

		musicButton = new Button(Assets.getButtonStyleMusic());		
		musicButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
				Button b  = (Button) event.getListenerActor();
				if (b.isDisabled()) {
					SoundManager.unmuteMusic();
					b.setDisabled(false);
				}
				else {
					SoundManager.muteMusic();
					b.setDisabled(true);
				}
			}
		});
		textTable.add(musicButton).width(size).height(size).center();
		return textTable;
	}

	public Table generateResumeButton() {
		Table button = DrawUI.getBlueButton("BACK" , 44);

		button.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
				back();
			}
		});	

		return button;
	}

	public void goToMarket() {
		System.out.println("Go to market!");
	}

	@Override
	public void render(float delta) {
		super.renderGrayBg(delta);
		if (master.profile.settings.muteMusic) {
			musicButton.setDisabled(true);
		}
		if (master.profile.settings.muteSound) {
			soundButton.setDisabled(true);
		}
	}

	@Override
	public void update(float delta, boolean ff) {
		// do nothing, we are overriding active screen so nothing happens on pause
		// TODO allow stuff to happen if not already paused?
	}

	public void back() {
		master.settingsBack();
	}

	@Override
	public void dispose() {
		super.dispose();
		this.uiStage.dispose();
	}
}
