package com.kebabking.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Background {
	static final float BG_Y_BOT = 0.77f;
	
	// c7ebff
	static final Color daySky = new Color(.777f, .917f, 1, 1);
//	static final Color duskSky = new Color(255f/256, 225f/256, 135f/256, 1);
	static final Color nightSky = new Color(.1f, .1f, .2f, 1);
	static final float nightStartTime = 15; // when night transition starts
	static final int CLOUD_COUNT = 5;
	static final int STAR_COUNT = 50;
	static final float STAR_ALPHA_RATE = 0.3f;
	
	static final float SUN_SIZE = 0.15f;
	static final float SUN_START_X = 0f;
	static final float SUN_START_Y = 0.9f;
	static final float SUN_ROT_SPEED = 15f;
	static final float SUN_SPEED = 1.25f;
	
	static final float PLANE_Y = 0.87f;
//	static final float PLANE_WIDTH = 0.1f;
//	static final float PLANE_HEIGHT = 0.05f;
	static final float PLANE_SPEED = 0.05f;

	static final float FLASH_LENGTH = 4f;
	
	// background based on where the player is, what time of day, etc.
	float timeElapsed;

//	TextureRegion bg; 
	Profile profile;

	TextureRegion currentBg;
	
	Color currentGoal;
	Color currentColor;

	float dayPercent;

	float nightRate = 0.15f;
//	float rDif = (duskSky.r - daySky.r) / 2f;
//	float gDif = duskSky.g - daySky.g;
//	float bDif = duskSky.b - daySky.b;
	float rDif = (nightSky.r - daySky.r) / 4f;
	float gDif = (nightSky.g - daySky.g) / 2f;
	float bDif = (nightSky.b - daySky.b);
	boolean fullNight;
	boolean dusk;
	
	float cloudWidth;
	float cloudHeight;

	float starWidth;
	float starHeight;
	
	float planeWidth;
	float planeHeight;
	
	boolean active;
	
//	boolean sign; //is neon sign drawn
	
	Cloud[] clouds;
	
	class Cloud {
		float x;
		float y;
		float speed;
		float scale;
		int type;
	}
	
	Star[] stars;
	class Star {
		float x;
		float y;
		float scale;
		float alpha;
		boolean alphaUp;
	}
	
	Plane plane;
	class Plane {
		float x;
		float y;
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

	float signWidth;
	float signHeight;
	float sign_y;
	
	static float flashCountdown;
	static Color flashColor;
	
	public Background(Profile profile) {
		this.timeElapsed = 0;
		
		// assign bg based on location
//		this.bg = ;
		this.profile = profile;

		this.active = false;
		setToDay();	
		
		sunSize = KebabKing.getGlobalX(SUN_SIZE);
		
		signWidth = KebabKing.getWidth();
		signHeight = signWidth *  Assets.neonSign.getKeyFrame(0).getRegionHeight() / Assets.neonSign.getKeyFrame(0).getRegionWidth();
		sign_y = KebabKing.getGlobalYFloat(0.83f);
	}

	public void setToDay() {
		this.currentColor = daySky.cpy();
		this.dayPercent = 1f;
		this.fullNight = false;
		
		sunX = KebabKing.getGlobalX(SUN_START_X);
		sunY = KebabKing.getGlobalY(SUN_START_Y);
	}
	
	public void reset() {
		this.timeElapsed = 0;
		this.activate();
		
		rDif = (nightSky.r - daySky.r) / 4f;
		gDif = (nightSky.g - daySky.g) / 2f;
		bDif = (nightSky.b - daySky.b);
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
//				if (!dusk) {
//					if (this.currentColor.r <= duskSky.r) {
//						System.out.println("updating current");
//						this.currentColor.r += rDif * delta * nightRate;
//					}
//					if (this.currentColor.g >= duskSky.g) {
//						this.currentColor.g += gDif * delta * nightRate;
//					}
//					if (this.currentColor.b >= duskSky.b) {
//						this.currentColor.b += bDif * delta * nightRate;
//					}
//					if (this.currentColor.r <= duskSky.r && this.currentColor.g >= duskSky.g && this.currentColor.b >= duskSky.b) {
//						this.duskReached();
//					}
//				}
				this.currentColor.r += rDif * delta * nightRate;
				this.currentColor.g += gDif * delta * nightRate;
				this.currentColor.b += bDif * delta * nightRate;
				if (this.currentColor.b <= nightSky.b) {
					rDif = (nightSky.r - daySky.r) / 1.5f;
					gDif = (nightSky.g - daySky.g) / 1.2f;
					bDif = 0;
					nightRate = 0.2f;
				}
				if (this.currentColor.g <= nightSky.g) {
					gDif = 0;
				}
				if (this.currentColor.r <= nightSky.r) {
					fullNight = true;
					this.currentColor = nightSky.cpy();
				}
				
				this.dayPercent = (this.currentColor.b - nightSky.b) / (daySky.b - nightSky.b);
				if (dayPercent < 0) this.dayPercent = 0;
			}
			else this.dayPercent = 0;
		}
		
		// update clouds
		for (int i = 0; i < CLOUD_COUNT; i++) {
			clouds[i].x += clouds[i].speed * delta;
			if (clouds[i].x >= KebabKing.getWidth()) generateCloud(i);
		}

		this.sunAngle += SUN_ROT_SPEED * delta;
		
		// update stars
		for (int i = 0; i < STAR_COUNT; i++) {
			if (stars[i].alphaUp) {
				stars[i].alpha += STAR_ALPHA_RATE * delta;
				if (stars[i].alpha > 1) {
					stars[i].alpha = 1;
					stars[i].alphaUp = false;
				}
			}
			else{
				stars[i].alpha -= STAR_ALPHA_RATE * delta;
				if (stars[i].alpha < 0) {
					stars[i].alpha = 0;
					stars[i].alphaUp = true;
				}
			}
		}
		
		// update plane
		if (plane != null) {
			plane.x += KebabKing.getGlobalXFloat(PLANE_SPEED * delta);
			if (plane.x > KebabKing.getGlobalXFloat(1)) {
				if (!profile.inventory.adCampaign.isPlane()) {
					plane = null;
				}
				else {
					plane.x = -planeWidth * 2;
				}
			}
		}
		else {
			if (profile.inventory.adCampaign.isPlane()) {
				plane = new Plane();
				plane.y = KebabKing.getGlobalYFloat(PLANE_Y);
				plane.x = -planeWidth * 2;
			}
		}

		if (this.active) {
			// update sun
			sunX = (timeElapsed * SUN_SPEED * 0.04f) - 0.4f;
			sunY = sunX * sunX * sunPathA + sunX * sunPathB + sunPathC;
		}
	}
	
//	public void duskReached() {
//		dusk = true;
//		rDif = (nightSky.r - duskSky.r) / 2;
//		gDif = nightSky.g - duskSky.g;
//		bDif = nightSky.b - duskSky.b;
//		nightRate = 0.1f;
//	}

	public void draw(SpriteBatch batch, float delta) {
		Gdx.gl.glClearColor(currentColor.r, currentColor.g, currentColor.b, currentColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		Color orig = batch.getColor();
				
		// draw sun
		// draw one spinning sun behind the other one
		batch.draw(Assets.sun, KebabKing.getGlobalX(sunX), KebabKing.getGlobalY(sunY), sunSize/2, sunSize/2, sunSize, sunSize, 1, 1, sunAngle);
		
		batch.draw(Assets.sun, KebabKing.getGlobalX(sunX), KebabKing.getGlobalY(sunY), sunSize/2, sunSize/2, sunSize, sunSize, 1, 1, -sunAngle/2);
		
//		float colDif = (daySky.g - nightSky.g) - (currentColor.g - nightSky.g);
//		
		
		// draw clouds
		for (int i = 0; i < CLOUD_COUNT; i++) {
			TextureRegion toDraw = Assets.cloud1;
			if (clouds[i].type == 2) toDraw = Assets.cloud2;
			batch.draw(toDraw, clouds[i].x, clouds[i].y, cloudWidth * clouds[i].scale, cloudHeight * clouds[i].scale);
		}
		
		// when dayPercent is 0.5, should be 0
		// when dayPercent is 0, should be 1

		// draw stars
		for (int i = 0; i < STAR_COUNT; i++) {
			batch.setColor(1, 1, 1, stars[i].alpha * Math.min(Math.max((((sunX) - 0.9f)*10), 0), 1));
			TextureRegion toDraw = Assets.skyStar;
	
			// TODO make this screen size dependent.
			batch.draw(toDraw, stars[i].x, stars[i].y, starWidth * stars[i].scale, starHeight * stars[i].scale);
		}
	
		batch.setColor(orig);

		// draw plane
		if (plane != null) {
//			System.out.println("Drawing plane");
			TextureRegion toDraw = Assets.plane.getKeyFrame(timeElapsed);
//			System.out.println(plane.x);
			batch.draw(toDraw, plane.x, plane.y, planeWidth, planeHeight);
		}
		
		batch.setColor(dayPercent * .2f + .8f, dayPercent * .2f + .8f, dayPercent * .2f + .8f, 1);
		
		if (profile.getLocationBG() != currentBg) {
			updateBg();
		}
		
		batch.draw(currentBg, 0, 0, KebabKing.getWidth(), KebabKing.getHeight());
				
		batch.setColor(orig);
		
		if (profile.inventory.adCampaign.isSign()) {				
			batch.draw(Assets.neonSign.getKeyFrame(timeElapsed), 0, sign_y, signWidth, signHeight);
		}
		// draw the background filling entire screen
		
//		drawVanityDecorations(batch);
		if (flashCountdown > 0) {
			drawFlash(batch, delta);
		}
	}
	
	public void updateBg() {
		// don't draw flash on start
		if (currentBg != null) startFlash();
		currentBg = profile.getLocationBG();
	}
	
	public static void startFlash() {
		flashCountdown = FLASH_LENGTH;
	}
	
	public static void drawFlash(SpriteBatch batch, float delta) {
		if (flashColor == null) {
			flashColor = new Color(1, 1, 1, 1);
		}
		Color o = batch.getColor();
		flashColor.set(1,  1,  1, flashCountdown / FLASH_LENGTH);
		batch.setColor(flashColor);
		batch.draw(Assets.white, 0, 0, KebabKing.getWidth(), KebabKing.getHeight());
		batch.setColor(o);
		flashCountdown -= delta;
	}
	
	// draw background vanity decorations
//	public void drawVanityDecorations(SpriteBatch batch) {
//		
////		for (VanityDecoration d : profile.inventory.decorations.getAll()) {
////			
//		VanityDecoration d = (VanityDecoration) profile.inventory.decorations.getCurrentSelected();
//		if (d != null)
//			batch.draw(d.getTexture(), KebabKing.getGlobalX(d.x), KebabKing.getGlobalY(d.y), KebabKing.getGlobalX(d.width), KebabKing.getGlobalY(d.height));
////		}
//		
//		// for now, just draw currently selected
//	}
	
	public void initialize() {		
		this.cloudWidth = KebabKing.getGlobalX(0.262f);
		this.cloudHeight = KebabKing.getGlobalY(0.075f);
				
		this.starWidth = KebabKing.getGlobalX(0.02f);
		this.starHeight = KebabKing.getGlobalY(0.01f);
	
		this.planeWidth = KebabKing.getGlobalX(0.7f);
		this.planeHeight = KebabKing.getGlobalY(0.07f);	
		
		generateClouds(CLOUD_COUNT);
		
		clouds[2].x = KebabKing.getGlobalX(1.0f / 4);
		clouds[1].x = KebabKing.getGlobalY(3.0f / 4);
		
		generateRandomStars(STAR_COUNT);
	}

	public void generateClouds(int count) {
		this.clouds = new Cloud[count];
		for (int i = 0; i < count; i++) {
			generateCloud(i);
			clouds[i].x = (float) (Math.random() * KebabKing.getWidth());
		}
	}
	
	public void generateCloud(int i) {
		if (this.clouds[i] == null) this.clouds[i] = new Cloud();
		this.clouds[i].x = (float) (-cloudWidth * 1.2) + (float) Math.random() * -100;
//		this.cloudY[i] = ((float) (Math.random() * 0.07f) + 0.81f) * ChuanrC.height;
		this.clouds[i].y = (float) (KebabKing.getGlobalY(((1.0f * i / (CLOUD_COUNT - 1)) * 0.07f + 0.82f)));
		
		this.clouds[i].speed = (float) (Math.random() * 10 + 15);
		this.clouds[i].scale = (float) (Math.random() * 0.5 + 0.8);
		this.clouds[i].type = (int) Math.ceil(Math.random()*2);
	}
	
	public void generateRandomStars(int count) {
		stars = new Star[count];
		for (int i = 0; i < count; i++) {
			float xPos = (float) Math.random();
			float yPos = (float) (Math.random()) * (1 - BG_Y_BOT) + BG_Y_BOT;
			float size = ((float) Math.random()) + 0.6f;
			Star star = new Star();
			star.x = KebabKing.getGlobalXFloat(xPos);
			star.y = KebabKing.getGlobalYFloat(yPos);
			star.scale = size;
			star.alpha = (float) Math.random();
			star.alphaUp = Math.random() < 0.5;
			stars[i] = star;
		}
	}
}
