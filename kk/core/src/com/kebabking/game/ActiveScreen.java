package com.kebabking.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// Defines a screen that has a background, a grill, customers.
public class ActiveScreen extends ScreenTemplate {
	KebabKing master;
	Background bg; 
	Grill grill;
	CustomerManager cm;
	SpriteBatch batch;

	public ActiveScreen(KebabKing master) {
		this.master = master;
		this.bg = master.bg;
		this.grill = master.grill;
		this.cm = master.cm;
		this.batch = master.batch;
	}
	
	
	public void render(float delta) {
		batch.begin();
		
		bg.draw(batch);
		cm.draw(batch);
		grill.draw(batch);
		DrawUI.drawFullUI(delta, batch, master.profile);

		batch.end();
		
		boolean fastForward = false;
		if (Gdx.input.isKeyPressed(Keys.F))
			fastForward = true;
		
		update(delta, fastForward);
	}
	
	public void renderGrayBg(float delta, float alpha) {
		batch.begin();
		
		bg.draw(batch);
		cm.draw(batch);
		grill.draw(batch);
		
		DrawUI.tintGrayAlpha(batch, alpha);		
		DrawUI.drawFullUI(delta, batch, master.profile);

		batch.end();
		
		boolean fastForward = false;
		if (Gdx.input.isKeyPressed(Keys.F))
			fastForward = true;
		
		update(delta, fastForward);	
	}
	
	public void renderGrayBg(float delta) {
		batch.begin();
		
		bg.draw(batch);
		cm.draw(batch);
		grill.draw(batch);
		
		DrawUI.tintGray(batch);		
		DrawUI.drawFullUI(delta, batch, master.profile);

		batch.end();
		
		boolean fastForward = false;
		if (Gdx.input.isKeyPressed(Keys.F))
			fastForward = true;
		
		update(delta, fastForward);	
	}
	
	public void renderWhiteBg(float delta, float alpha) {
		batch.begin();
		
		bg.draw(batch);
		cm.draw(batch);
		grill.draw(batch);
		
		DrawUI.tintWhiteAlpha(batch, alpha);		
		DrawUI.drawFullUI(delta, batch, master.profile);

		batch.end();
		
		boolean fastForward = false;
		if (Gdx.input.isKeyPressed(Keys.F))
			fastForward = true;
		
		update(delta, fastForward);
	}
	
	public void update(float delta, boolean ff) {
		bg.act(delta);
		cm.act(delta);
		grill.act(delta);
		
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
			bg.act(delta);
			cm.act(delta);
		}
	}
}
