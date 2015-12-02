package com.chuanrchef.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.chuanrchef.game.Purchases.Vanity.VanityDecoration;

public class Background {
	// c7ebff
	static final Color daySky = new Color(.777f, .917f, 1, 1);
	static final Color nightSky = new Color(.1f, .1f, .2f, 1);
	static final float nightStartTime = 15; // when night transition starts
	static final int CLOUD_COUNT = 3;

	static final float SUN_SIZE = 0.15f;
	static final float SUN_START_X = 0f;
	static final float SUN_START_Y = 0.9f;
	static final float SUN_ROT_SPEED = 5f;
	
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
	
	float cloudWidth;
	float cloudHeight;
	
	boolean active;
	
	Cloud[] clouds;
	
	class Cloud {
		float x;
		float y;
		float speed;
		float scale;
		int type;
	}
	
	// Define a path for sun to follow:
	// let it start in upper left corner and come down behind background.
	// y position = -(x*x) * some factor
	float sunX;
	float sunY;
	float sunSize;
	float sunAngle;
	
	float sunPathA = -.25f;
	float sunPathB = 0.1f;
	float sunPathC = 0.9f;
	
	public Background(Profile profile) {
		this.timeElapsed = 0;
		
		// assign bg based on location
//		this.bg = ;
		this.profile = profile;

		this.active = false;
		setToDay();	
		
		sunSize = ChuanrC.getGlobalX(SUN_SIZE);
	}

	public void setToDay() {
		this.currentColor = daySky.cpy();
		this.dayPercent = 1f;
		this.fullNight = false;
		
		sunX = ChuanrC.getGlobalX(SUN_START_X);
		sunY = ChuanrC.getGlobalY(SUN_START_Y);
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
		
		// update clouds
		for (int i = 0; i < CLOUD_COUNT; i++) {
			clouds[i].x += clouds[i].speed * delta;
			if (clouds[i].x >= ChuanrC.getWidth()) generateCloud(i);
			this.sunAngle += SUN_ROT_SPEED * delta;
		}
		
		if (this.active) {
			// update sun
			sunX = (timeElapsed * 0.04f) - 0.4f;
			sunY = sunX * sunX * sunPathA + sunX * sunPathB + sunPathC;
		}
	}

	public void draw(SpriteBatch batch) {
		Gdx.gl.glClearColor(currentColor.r, currentColor.g, currentColor.b, currentColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		Color orig = batch.getColor();
				
		// draw sun
		// draw one spinning sun behind the other one
		batch.draw(Assets.sun, ChuanrC.getGlobalX(sunX), ChuanrC.getGlobalY(sunY), sunSize/2, sunSize/2, sunSize, sunSize, 1, 1, sunAngle);
		
		batch.draw(Assets.sun, ChuanrC.getGlobalX(sunX), ChuanrC.getGlobalY(sunY), sunSize/2, sunSize/2, sunSize, sunSize, 1, 1, -sunAngle/2);
		
//		float colDif = (daySky.g - nightSky.g) - (currentColor.g - nightSky.g);
//		
		batch.setColor(dayPercent * .2f + .8f, dayPercent * .2f + .8f, dayPercent * .2f + .8f, 1);
		
		// draw clouds
		for (int i = 0; i < CLOUD_COUNT; i++) {
			TextureRegion toDraw = Assets.cloud1;
			if (clouds[i].type == 2) toDraw = Assets.cloud2;
			batch.draw(toDraw, clouds[i].x, clouds[i].y, cloudWidth * clouds[i].scale, cloudHeight * clouds[i].scale);
		}
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
		this.clouds = new Cloud[CLOUD_COUNT];
		
		this.cloudWidth = ChuanrC.getGlobalX(0.35f);
		this.cloudHeight = ChuanrC.getGlobalY(0.1f);
				
		generateCloud(0);
		generateCloud(1);
		generateCloud(2);
		
		clouds[2].x = ChuanrC.getGlobalX(1.0f / 4);
		clouds[1].x = ChuanrC.getGlobalY(3.0f / 4);
	}

	public void generateCloud(int i) {
		if (this.clouds[i] == null) this.clouds[i] = new Cloud();
		this.clouds[i].x = (float) (-cloudWidth * 1.2) + (float) Math.random() * -100;
//		this.cloudY[i] = ((float) (Math.random() * 0.07f) + 0.81f) * ChuanrC.height;
		this.clouds[i].y = (float) (ChuanrC.getGlobalY(((1.0f * i / (CLOUD_COUNT - 1)) * 0.07f + 0.82f)));
		
		this.clouds[i].speed = (float) (Math.random() * 10 + 15);
		this.clouds[i].scale = (float) (Math.random() * 0.5 + 0.8);
		this.clouds[i].type = (int) Math.ceil(Math.random()*2);
	}
}
