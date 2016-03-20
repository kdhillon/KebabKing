package com.kebabking.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

// Defines a screen that has a background, a grill, customers.
public class ActiveScreen extends ScreenTemplate {
	KebabKing master;
	Background bg; 
	Grill grill;
	CustomerManager cm;
	SpriteBatch batch;
	Stage uiStage; // optional
	float timeElapsed = 0;
	Color grillTintColor;

	public ActiveScreen(KebabKing master, boolean uiStage, String name) {
		super(name);
		this.master = master;
		this.bg = master.bg;
		this.grill = master.grill;
		this.cm = master.cm;
		this.batch = master.batch;
		grillTintColor = new Color(1, 1, 1, 1);
		if (uiStage) {
			ScreenViewport viewport = new ScreenViewport();
			this.uiStage = new Stage(viewport, batch);		
		}
	}

	public void render(float delta) {
		render(delta, null, 1, 1);
	}

	public void render(float delta, TextureRegion tint, float alpha, float grillAlpha) {
		batch.begin();

		bg.draw(batch, delta);
		cm.draw(batch);

		if (grillAlpha != 1) {
			Color o = batch.getColor();
			grillTintColor.set(1, 1, 1, grillAlpha);
			batch.setColor(grillTintColor);
			grill.draw(batch, delta);
			batch.setColor(o);
		}
		else {
			grill.draw(batch, delta);	
		}

		// force tint
		if (DrawUI.shouldTint()) {
			tint = Assets.grayDark;
			alpha = DrawUI.GRAY_ALPHA;
		}

		if (tint != null)
			DrawUI.tint(batch, tint, alpha);

		if (uiStage != null && !DrawUI.shouldTint()) {
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

	public void renderGrayAlpha(float delta, float alpha, float grillAlpha) {
		render(delta, Assets.grayDark, alpha, grillAlpha);
	}

	public void renderGrayAlpha(float delta, float alpha) {
		render(delta, Assets.grayDark, alpha, 1);
	}

	public void renderGrayBg(float delta) {
		render(delta, Assets.grayAlpha, 1, 1);
	}

	public void renderWhiteAlpha(float delta, float alpha) {
		render(delta, Assets.white, alpha, 1);
	}

	public void update(float delta, boolean ff) {
		if (TutorialEventHandler.shouldPause()) return;

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

	@Override
	public void show() {
		super.show();
		DrawUI.setInput(uiStage);
	}
}
