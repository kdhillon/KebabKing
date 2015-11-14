package com.chuanrchef.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.chuanrchef.game.Purchases.Vanity.VanityDecoration;

public class Background {
	static Color daySky = new Color(.1f, .3f, 1f, 1);
	static Color nightSky = new Color(.1f, .1f, .2f, 1);
	static float nightStartTime = 15; // when night transition starts
	static int CLOUD_COUNT = 3;

	// background based on where the player is, what time of day, etc.
	float timeElapsed;

//	TextureRegion bg; 
	
	Profile profile;

	Color currentColor;

	float dayPercent;
	
	float nightRate = 0.05f;
	float rDif = nightSky.r - daySky.r;
	float gDif = nightSky.g - daySky.g;
	float bDif = nightSky.b - daySky.b;
	boolean fullNight;
	
	
	boolean active;
	

	float cloudWidth;
	float cloudHeight;
	
	float[] cloudX;
	float[] cloudY;
	
	float[] cloudSpeed;
	float[] cloudScale;
	
	public Background(Profile profile) {
		this.timeElapsed = 0;
		
		// assign bg based on location
//		this.bg = ;
		this.profile = profile;

		this.active = false;
		this.currentColor = daySky.cpy();
		this.dayPercent = 1;
		
		cloudX = new float[CLOUD_COUNT];
		cloudY = new float[CLOUD_COUNT];
		cloudSpeed = new float[CLOUD_COUNT];
		
		cloudScale = new float[CLOUD_COUNT];
	}

	public void setToDay() {
		this.currentColor = daySky.cpy();
		this.dayPercent = 1f;
		this.fullNight = false;
	}
	
	public void reset() {
		this.timeElapsed = 0;
		this.activate();
	}
	
	public void activate() {
		this.active = true;
	}
	
	public void deactivate() {
		this.active = false;
	}

	public void act(float delta) {
		this.timeElapsed += delta;
				
		if (this.active && this.timeElapsed > nightStartTime) {

			if (!fullNight) {
				// fade towards night
				this.currentColor.r += rDif * delta * nightRate;
				this.currentColor.g += gDif * delta * nightRate;
				this.currentColor.b += bDif * delta * nightRate;
				if (this.currentColor.g <= nightSky.g) {
					fullNight = true;
					this.currentColor = nightSky.cpy();
				}
				this.dayPercent = (this.currentColor.g - nightSky.g) / (daySky.g - nightSky.g);
			}
			else this.dayPercent = 0;
		}
		
		for (int i = 0; i < CLOUD_COUNT; i++) {
			cloudX[i] += cloudSpeed[i] * delta;
			if (cloudX[i] >= ChuanrC.getWidth()) generateCloud(i);	
		}
	}

	public void draw(SpriteBatch batch) {
		Gdx.gl.glClearColor(currentColor.r, currentColor.g, currentColor.b, currentColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		Color orig = batch.getColor();
//		
//		float colDif = (daySky.g - nightSky.g) - (currentColor.g - nightSky.g);
//		
		

		batch.setColor(dayPercent * .2f + .8f, dayPercent * .2f + .8f, dayPercent * .2f + .8f, 1);
		
		for (int i = 0; i < CLOUD_COUNT; i++)
			batch.draw(Assets.cloud, cloudX[i], cloudY[i], cloudWidth * cloudScale[i], cloudHeight * cloudScale[i]);

		batch.draw(profile.getLocationBG(), 0, 0, ChuanrC.getWidth(), ChuanrC.getHeight());

		
		batch.setColor(orig);
		// draw the background filling entire screen
		
		drawVanityDecorations(batch);
	}
	
	// draw background vanity decorations
	public void drawVanityDecorations(SpriteBatch batch) {
		
//		for (VanityDecoration d : profile.inventory.decorations.getAll()) {
//			
		VanityDecoration d = (VanityDecoration) profile.inventory.decorations.getCurrentSelected();
		if (d != null)
			batch.draw(d.getTexture(), ChuanrC.getGlobalX(d.x), ChuanrC.getGlobalY(d.y), ChuanrC.getGlobalX(d.width), ChuanrC.getGlobalY(d.height));
//		}
		
		// for now, just draw currently selected
	}
	
	public void initialize() {
		this.cloudWidth = ChuanrC.getGlobalX(0.35f);
		this.cloudHeight = ChuanrC.getGlobalY(0.1f);
		
		generateCloud(0);
		generateCloud(1);
		generateCloud(2);
		
		cloudX[2] = ChuanrC.getGlobalX(1.0f / 4);
		cloudX[1] = ChuanrC.getGlobalY(3.0f / 4);
	}
	
	public void generateCloud(int i) {
		this.cloudX[i] = (float) (-cloudWidth * 1.2) + (float) Math.random() * -100;
//		this.cloudY[i] = ((float) (Math.random() * 0.07f) + 0.81f) * ChuanrC.height;
		this.cloudY[i] = (float) (ChuanrC.getGlobalY(((1.0f * i / (CLOUD_COUNT - 1)) * 0.07f + 0.78f)));
		
		this.cloudSpeed[i] = (float) (Math.random() * 10 + 15);
		this.cloudScale[i] = (float) (Math.random() * 0.5 + 1.);
	}
}
