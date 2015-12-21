package com.chuanrchef.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
//import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/* new pause screen created for every kitchen screen */
public class PauseScreen extends ScreenTemplate  {
	ChuanrC master;
	
	Stage uiStage;

	SpriteBatch batch;
	Grill grill;
	CustomerManager cm;
	Background bg;
	
	Table table;
	
	public PauseScreen(ChuanrC master) {
		this.master = master;

		this.batch = master.batch;
		this.bg = master.bg;
		this.cm = master.cm;
		this.grill = master.grill;
				
		ScreenViewport viewport = new ScreenViewport();
		uiStage = new Stage(viewport, batch);	
				
		table = new Table();
		table.setPosition(ChuanrC.getGlobalX(1.0f / 8), ChuanrC.getGlobalY(1.0f / 4));
		table.setSize(ChuanrC.getGlobalX(3.0f / 4), ChuanrC.getGlobalY(1.0f / 2));
//		table.debugAll();
		table.align(Align.top);
		uiStage.addActor(table);
		
		Label paused = new Label("Paused!", Assets.generateLabelStyle(100));
		table.add(paused);
		
//		TextButton settings = new TextButton("Settings", Assets.getMainButtonStyle());
//		table.row();
//		table.add(settings).padTop(ChuanrC.height / 32);
//		settings.addListener(new InputListener() {
//			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
//				return true;
//			}
//			public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
//				System.out.println("Settings");;
//			}
//		});	
		
		TextButton resume = new TextButton("Resume", Assets.getMainButtonStyle());
		table.row();
		table.add(resume).padTop(ChuanrC.getGlobalY(1.0f / 32));
		
		resume.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
				unpause();
			}
		});	
	}
		
	@Override
	public void render(float delta) {
		uiStage.act(delta);

		batch.begin();
		bg.draw(batch);
		cm.draw(batch);
		grill.draw(batch);
		
		DrawUI.drawGray(batch);
//		DrawUI.drawMoney(batch, master.profile);
//		DrawUI.drawStars(batch, master.profile);
//		DrawUI.drawTime(batch, master.kitchen.time);
				
		DrawUI.drawFullUI(delta, batch, master.profile);
		
		batch.end();
		
		uiStage.draw();
	}

	public void unpause() {
		master.kitchenUnpause();
	}
	
	@Override
	public void show() {
		DrawUI.setInput(uiStage);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		this.uiStage.dispose();
	}
}
