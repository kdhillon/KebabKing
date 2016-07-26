package com.kebabking.game;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class JadeWheelTable extends Table {
	public static JadeWheel wheel;
	
	public Table table;
	
	public Table buttonHolder;
	
	Table spinButton;
	boolean canSpinPrev;
	KebabKing master;
	
	// TODO make this into a table like JewelerTable
	public JadeWheelTable(KebabKing master) {
		super();
		this.master = master;

		float size = KebabKing.getGlobalY(0.53f);
        wheel = new JadeWheel((KebabKing.getWidth() - size)/2, KebabKing.getGlobalYFloat(0.485f), size, this);
        
        String top = Assets.strings.get("wheel");
		String mid = Assets.strings.get("of");
		String bot = Assets.strings.get("jade");
		
		Label topL = new Label(top, Assets.generateLabelStyleUIChina(80, top));
		Label midL = new Label(mid, Assets.generateLabelStyleUIChina(40, mid));
		Label botL = new Label(bot, Assets.generateLabelStyleUIChina(80, bot));
				
		table = new Table();
		this.setBackground(new TextureRegionDrawable(Assets.getTextureRegion("market/Jeweler-12")));

//		this.debugAll();
		
//		uiStage.addActor(table);
//        uiStage.addActor(wheel);
//        
        this.setSize(KebabKing.getWidth(), KebabKing.getHeight());
		
		// add title to top
		this.add(topL).top().padTop(KebabKing.getGlobalYFloat(0.06f));
		this.row();
		this.add(midL).top().padTop(-KebabKing.getGlobalY(0.015f));
		this.row();
		this.add(botL).top().expandY().padBottom(size).padTop(-KebabKing.getGlobalY(0.015f));
//		this.debugAll();
		this.row();
		
		this.add(wheel);
		this.row();
		
		buttonHolder = new Table();
		buttonHolder.setTouchable(Touchable.childrenOnly);
		// add blank buffer area to middle
		this.add(buttonHolder).top().expand();
		
		
		addSpinButton();
		// add spin button to bottom
	}
	
//	@Override
//	public void render(float delta) {		
//		super.renderGrayAlpha(delta, DrawUI.GRAY_ALPHA, 1);
//		//		KebabKing.print("rendering " + bgTint);
//		//		uiStage.draw();
//	}
	
//	@Override
	public void update(float delta, boolean ff) {
//		super.update(delta, ff);
		wheel.update(delta, ff);
		
		if (canSpinPrev != master.profile.canSpin) {
			canSpinPrev = master.profile.canSpin;
			if (canSpinPrev) {
				spinButton.remove();
				addSpinButton();
			}
			else {
				KebabKing.print("adding play");
				Image play = new Image(Assets.getTextureRegion("market/Market_menu_element-09"));
				spinButton.add(play).expand().padLeft(-KebabKing.getGlobalX(0.06f)).padRight(KebabKing.getGlobalX(0.11f));
			}
		}
	}

	public void addSpinButton() {
		String text = Assets.strings.get("spin");
		spinButton = DrawUI.getBlueButton(text, 56);
		if (!master.profile.canSpin) {
			KebabKing.print("adding play");
			Image play = new Image(Assets.getTextureRegion("market/Market_menu_element-09"));
			spinButton.add(play).expand().size(KebabKing.getGlobalYFloat(0.045f)).padLeft(-KebabKing.getGlobalX(0.06f)).padRight(KebabKing.getGlobalX(0.11f));
		}
	
		// doesn't work with strict listener for some reason
		spinButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
				return true;
			}
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if (master.profile.canSpin) {
					wheel.startSpin();
					event.getListenerActor().setVisible(false);
					event.getListenerActor().setTouchable(Touchable.disabled);		
				}
				else {
					AdsHandler.showAd();
				}
				
			}
		});

		buttonHolder.clear();
		buttonHolder.add(spinButton).expand();
	}
	
//	public void addContinueButton() {
//		String text = Assets.strings.get("continue");
//		Table continueButton = DrawUI.getBlueButton(text, 44);
//		
//		// doesn't work with strict listener for some reason
//		continueButton.addListener(new InputListener() {
//			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
//				return true;
//			}
//			@Override
//			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//				DrawUI.launchAdSuccessNotification(wheel.getReward());
//				wheel.reset();	
//				master.jadeWheelBack();
//			}
//		});
//		buttonHolder.clear();
//		buttonHolder.add(continueButton).expand();
//	}

	public void handleJadeWheelStopped() {
//		addContinueButton();
		wheelSpinResult(wheel.getReward());
	}
	
//	@Override
	public void show() {
//		super.show();
		master.store.storeScreen.uiStage.addActor(wheel);
	
		wheel.startWaitingForSpin();
		addSpinButton();
	}
	public void hide() {
//		master.store.storeScreen.uiStage.addActor(wheel);
//		wheel.setTouchable(Touchable.disabled);
		KebabKing.print("hiding");
		wheel.remove();
		wheel.reset();
	}

	// make this shoot a bunch in a row, a second after wheel stops spinning
    public void wheelSpinResult(int reward) {
    	KebabKing.print("wheel spin result");
    	DrawUI.createProjectiles(reward, (wheel.x + wheel.width/2) / KebabKing.getWidth(), wheel.y / KebabKing.getHeight(), true);
    	master.profile.giveCoins(reward);
    	master.profile.canSpin = false;
		master.save();
    }
}
