package com.kebabking.game;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class JadeWheelScreen extends ActiveScreen {
	public static JadeWheel wheel;
	
	public Table table;
	
	public Table buttonHolder;
	
	public JadeWheelScreen(KebabKing master) {
		super(master, true, "Jade Wheel");

		float size = KebabKing.getGlobalY(0.57f);
        wheel = new JadeWheel((KebabKing.getWidth() - size)/2, KebabKing.getGlobalYFloat(0.43f), size, this);
        
        String top = Assets.strings.get("wheel");
		String mid = Assets.strings.get("of");
		String bot = Assets.strings.get("jade");
		
		Label topL = new Label(top, Assets.generateLabelStyleUIChina(80, top));
		Label midL = new Label(mid, Assets.generateLabelStyleUIChina(40, mid));
		Label botL = new Label(bot, Assets.generateLabelStyleUIChina(80, bot));
				
		table = new Table();
//		table.debugAll();
		uiStage.addActor(table);
		table.setSize(KebabKing.getWidth(), KebabKing.getHeight());
		
		// add title to top
		table.add(topL).top().padTop(KebabKing.getGlobalYFloat(0.08f));
		table.row();
		table.add(midL).top().padTop(-KebabKing.getGlobalY(0.015f));
		table.row();
		table.add(botL).top().expandY().padBottom(size).padTop(-KebabKing.getGlobalY(0.015f));
//		table.debugAll();
		table.row();
		
		buttonHolder = new Table();
		buttonHolder.setTouchable(Touchable.childrenOnly);
		// add blank buffer area to middle
		table.add(buttonHolder).top().expand();
		
		
		addSpinButton();
		// add spin button to bottom
	}
	
	@Override
	public void render(float delta) {		
		super.renderGrayAlpha(delta, DrawUI.GRAY_ALPHA, 1);
		//		System.out.println("rendering " + bgTint);
		//		uiStage.draw();
	}
	
	@Override
	public void update(float delta, boolean ff) {
		super.update(delta, ff);
		wheel.update(delta, ff);
		batch.begin();
//		wheel.draw(batch, 0);
		batch.end();
	}
	

	public void addSpinButton() {
		String text = Assets.strings.get("spin");
		Table spinButton = DrawUI.getBlueButton(text, 60);
		
		// doesn't work with strict listener for some reason
		spinButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
				return true;
			}
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				wheel.startSpin();
				event.getListenerActor().setVisible(false);
				event.getListenerActor().setTouchable(Touchable.disabled);		
			}
		});

		buttonHolder.clear();
		buttonHolder.add(spinButton).expand();
	}
	
	public void addContinueButton() {
		String text = Assets.strings.get("continue");
		Table continueButton = DrawUI.getBlueButton(text, 44);
		
		// doesn't work with strict listener for some reason
		continueButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
				return true;
			}
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				DrawUI.launchAdSuccessNotification(wheel.getReward());
				wheel.reset();	
				master.jadeWheelBack();
			}
		});
		buttonHolder.clear();
		buttonHolder.add(continueButton).expand();
	}

	public void handleJadeWheelStopped() {
		addContinueButton();
		wheelSpinResult(wheel.getReward());
	}
	
	@Override
	public void show() {
		super.show();
		wheel.startWaitingForSpin();
		addSpinButton();
	}

	// make this shoot a bunch in a row, a second after wheel stops spinning
    public void wheelSpinResult(int reward) {
    	DrawUI.createProjectiles(reward, (wheel.x + wheel.width/2) / KebabKing.getWidth(), wheel.y / KebabKing.getHeight(), true);
    	master.profile.giveCoins(reward);
		master.save();
    }
}
