package com.kebabking.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Projectile {
//	public final static float G = 0.6f;
	public final static float G = 1f;
	
	public final static float CASH_SPEED = 1.0f;
	
	public final static float JADE_SPEED = 0.75f;

//	public final static float THRESHOLD = 0.015f;
//	public final static float THRESHOLD = 0.003f;
	public final static float THRESHOLD_Y = 1 - TopBar.UI_BAR_HEIGHT * 2.5f;

	public final static float JADE_X = 0.9f;
	public final static float JADE_Y = 0.9f;
	
	public final static float CASH_X = 0.65f;
	public final static float CASH_Y = 0.9f;
	
	public final static float MIN_X_VOL = -.2f;
	public final static float MAX_X_VOL = .1f;
	
	public final static float MIN_Y_VOL = -.2f;
	public final static float MAX_Y_VOL = .1f;
	
	public final static float SIZE_X_JADE = 0.10f;
	public final static float SIZE_Y_JADE = 0.06f;

	public final static float SIZE_X_CASH = 0.066f;
	public final static float SIZE_Y_CASH = 0.03f;
	
	float size_x;
	float size_y;
	
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

	float speed;
	
	TextureRegion reg;
	
	boolean shouldDestroy;
	
	public Projectile(float x, float y, boolean jade) {
		if (jade) {
			reg = Assets.bigjade;
			destX = JADE_X;
			destY = JADE_Y;
			size_x = SIZE_X_JADE;
			size_y = SIZE_Y_JADE;
			speed = JADE_SPEED;
		}
		else {
			double rand = Math.random();
			if (rand < 0.33) {
				reg = Assets.blueBill;
			}
			else if (rand < 0.66) {
				reg = Assets.redBill;
			}
			else {
				reg = Assets.greenBill;
			}
			destX = CASH_X;
			destY = CASH_Y;
			size_x = SIZE_X_CASH;
			size_y = SIZE_Y_CASH;
			speed = CASH_SPEED;
		}
		
	
		this.xPos = x - (size_x / 2);
		this.yPos = y - size_y / 2;
		
		this.xVel = (float) (MIN_X_VOL + Math.random() * (MAX_X_VOL - MIN_X_VOL));
		this.yVel = (float) (MIN_Y_VOL + Math.random() * (MAX_Y_VOL - MIN_Y_VOL));
		
		this.spin = (float) Math.random() * 10 - 5f;
	}
	
	public void update(float delta) {	
		delta *= speed;
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

		if (this.yPos >= THRESHOLD_Y) this.shouldDestroy = true;

		// Calculate total gravitational force from body j to i
//		double ftotal = G / distsqr;
		double ftotal = G / Math.sqrt(distsqr);

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
		batch.draw(reg, KebabKing.getGlobalXFloat(xPos), KebabKing.getGlobalYFloat(yPos), KebabKing.getGlobalX(size_x)/2, KebabKing.getGlobalY(size_y)/2, KebabKing.getGlobalX(size_x), KebabKing.getGlobalY(size_y), 1, 1, rot);
	}
}
