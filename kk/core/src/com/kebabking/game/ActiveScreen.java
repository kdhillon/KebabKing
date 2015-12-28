package com.kebabking.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;

// Defines a screen that has a background, a grill, customers.
public class ActiveScreen extends ScreenTemplate {
	KebabKing master;
	Background bg; 
	Grill grill;
	CustomerManager cm;
	SpriteBatch batch;
	float timeElapsed = 0;

	public ActiveScreen(KebabKing master) {
		this.master = master;
		this.bg = master.bg;
		this.grill = master.grill;
		this.cm = master.cm;
		this.batch = master.batch;
	}
	
	public void render(float delta) {
		render(delta, null, 1, null);
	}
	
	public void render(float delta, TextureRegion tint, float alpha, Stage uiStage) {
		batch.begin();
		
		bg.draw(batch);
		cm.draw(batch);
		grill.draw(batch);
		
		if (tint != null)
			DrawUI.tint(batch, tint, alpha);
		
		if (uiStage != null) {
			batch.end();
			uiStage.draw();
			batch.begin();
		}
		
		DrawUI.drawFullUI(delta, batch, master.profile);

		batch.end();
		
		boolean fastForward = false;
		if (Gdx.input.isKeyPressed(Keys.F))
			fastForward = true;
		
		update(delta, fastForward);
	}
	
	public void renderGrayAlpha(float delta, float alpha, Stage uiStage) {
		render(delta, Assets.gray, alpha, uiStage);
	}
	
	public void renderGrayBg(float delta, Stage uiStage) {
		render(delta, Assets.gray, 1, uiStage);
	}
	
	public void renderWhiteAlpha(float delta, float alpha, Stage uiStage) {
		render(delta, Assets.white, alpha, uiStage);
	}
	
	public void update(float delta, boolean ff) {
		bg.act(delta);
		grill.act(delta);
		cm.act(delta);
		timeElapsed += delta;
		
		if (ff) {
			bg.act(delta);
			cm.act(delta);
			grill.act(delta);
			bg.act(delta);
			cm.act(delta);
			grill.act(delta);
			bg.act(delta);
			cm.act(delta);
			grill.act(delta);
			bg.act(delta);
			cm.act(delta);
			grill.act(delta);
			bg.act(delta);
			cm.act(delta);
			grill.act(delta);
			bg.act(delta);
			cm.act(delta);
			grill.act(delta);
			bg.act(delta);
			cm.act(delta);
			grill.act(delta);
			timeElapsed += delta * 7;
		}
	}
}
