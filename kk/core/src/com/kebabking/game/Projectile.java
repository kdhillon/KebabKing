package com.kebabking.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Projectile {
	public final static float G = 0.6f;

	public final static float THRESHOLD = 0.02f;

	public final static float JADE_X = 0.9f;
	public final static float JADE_Y = 0.9f;
	
	public final static float CASH_X = 0.65f;
	public final static float CASH_Y = 0.9f;
	
	public final static float MIN_X_VOL = -.1f;
	public final static float MAX_X_VOL = .3f;
	
	public final static float MIN_Y_VOL = -.1f;
	public final static float MAX_Y_VOL = .3f;
	
	public final static float SIZE_X = 0.05f;
	public final static float SIZE_Y = 0.03f;
		
	float xPos;
	float yPos;
	float xVel;
	float yVel;
	float xAccel;
	float yAccel;
	
	float rot;
	float spin;
	
	float destX;
	float destY;

	TextureRegion reg;
	
	boolean shouldDestroy;
	
	public Projectile(float x, float y, boolean jade) {
		if (jade) {
			reg = Assets.bigjade;
			destX = JADE_X;
			destY = JADE_Y;
		}
		else {
			reg = Assets.getTextureRegion("screens/tutorial_jeweler");
			destX = CASH_X;
			destY = CASH_Y;
		}
		
		this.xPos = x;
		this.yPos = y;
		
		this.xVel = (float) (MIN_X_VOL + Math.random() * (MAX_X_VOL - MIN_X_VOL));
		this.yVel = (float) (MIN_Y_VOL + Math.random() * (MAX_Y_VOL - MIN_Y_VOL));
		
		this.spin = (float) Math.random() * 10 - 5f;
	}
	
	public void update(float delta) {	
		updateAccel();
		updateVel(delta);
		updatePos(delta);
	}
	
	public void updateAccel() {
//		float total = G / getDistSqToDest();
//		float ratio = (destX - xPos) / (destY - yPos);
//		xAccel = total * ratio;
//		yAccel = total / ratio;
		
		double distx = (destX - xPos);
		double disty = (destY - yPos);

		// Calculate total sqared distance from body i to j
		double distsqr = (distx*distx) + (disty*disty);

		if (this.xPos > 1 || this.yPos > 1 || distsqr < THRESHOLD) this.shouldDestroy = true;

		// Calculate total gravitational force from body j to i
		double ftotal = G / distsqr;

		// Calculate x and y components of ftotal
		xAccel = (float) (ftotal*distx/(Math.sqrt(distsqr)));
		yAccel = (float) (ftotal*disty/(Math.sqrt(distsqr)));
	}
	
	public void updateVel(float delta) {
		this.xVel += xAccel * delta;
		this.yVel += yAccel * delta;
	}
	
	public void updatePos(float delta) {
		this.xPos += xVel * delta;
		this.yPos += yVel * delta;
		
		this.rot += spin;
	}
	
//	public float getDistSqToDest() {
//		return (destX - xPos) * (destX - xPos) + (destY - yPos) * (destY - yPos);
//	}
	
	public void draw(SpriteBatch batch) {
//		System.out.println("drawing at " + xPos + ", " + yPos);
		batch.draw(reg, KebabKing.getGlobalXFloat(xPos), KebabKing.getGlobalYFloat(yPos), KebabKing.getGlobalX(SIZE_X)/2, KebabKing.getGlobalY(SIZE_Y)/2, KebabKing.getGlobalX(SIZE_X), KebabKing.getGlobalY(SIZE_Y), 1, 1, rot);
	}
}
