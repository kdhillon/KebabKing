package com.kebabking.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kebabking.game.Purchases.MeatTypes;

// represents a piece of meat on the grill
public class Meat {
	// TODO move this to separate loader
//	enum MeatTypes.Type {  // buy	   sell     cook    burn
//		BEEF(		2, 		3.5f,	8,		12), 
//		CHICKEN(	2.5f,	4f,		5,		8),  
//		LAMB(		1.5f,	3f, 	7,		10);
//		
//		float buyPrice;
//		float sellPrice;
//		float cookTime;
//		float burnTime;
//		
//		private MeatTypes.Type(float buy, float sell, float cook, float burn) {
//			this.buyPrice = buy;
//			this.sellPrice = sell;
//			this.cookTime = cook;
//			this.burnTime = burn;
//		}
//	}
	enum State {RAW, COOKED, BURNT}
	
	Grill grill;
	MeatTypes.Type type;
	State state;
	
	boolean spiced;
	
	float cookTime; // time on the grill so far
	
	int index1;
	int index2;
	
	boolean selected;

	// create a raw meat on the grill 
	public Meat(MeatTypes.Type type, Grill grill) {
		this.grill = grill;
		this.type = type;
		if (type == null) throw new java.lang.AssertionError();
		this.state = State.RAW;
		this.cookTime = 0;
		this.index1 = -1;
		this.index2 = -1;
	}
	
	public void setIndex(int index) {
		this.index1 = index;
		if (this.type.doubleWidth) this.index2 = index + 1;
	}
	
	// what to do every frame
	public void act(float delta) {
		// simply update cooked-ness
		cookTime += delta;
		if (cookTime > this.getBurnTime()) {
			if (this.state != State.BURNT) {
				this.state = State.BURNT;
				TutorialEventHandler.handleBurn();
			}
		}
		else if (cookTime > this.getCookTime()) {
			if (this.state != State.COOKED) {
				this.state = State.COOKED;
				TutorialEventHandler.handleCooked();
			}
		}
	}
	
	public static int getWidth() {
		return  (int) (Grill.GRILL_PIECE_WIDTH * KitchenScreen.UNIT_WIDTH / 
				Grill.CHUANR_PER_PIECE);
	}
	
	public static int getHeight() {
		return  (int) (KitchenScreen.UNIT_HEIGHT * Grill.CHUANR_HEIGHT);
	}
	
	public static void draw(SpriteBatch batch, TextureRegion meatTexture, boolean chicken, int x, int y, Profile profile, float scale) {
		TextureRegion stickTexture = Assets.getStickTexture(profile);

		// First draw meat
		int width = getWidth();
		int height = getHeight();

		batch.draw(stickTexture, x, y, width * scale, KitchenScreen.UNIT_HEIGHT * Grill.GRILL_PIECE_HEIGHT * scale);

		if (chicken) {
			batch.draw(stickTexture, x + KitchenScreen.UNIT_WIDTH * scale, y, width * scale, KitchenScreen.UNIT_HEIGHT * Grill.GRILL_PIECE_HEIGHT * scale);
			width *= 2;
		}

		batch.draw(meatTexture, x, y + KitchenScreen.UNIT_HEIGHT * (Grill.GRILL_PIECE_HEIGHT - Grill.CHUANR_HEIGHT), width * scale, height * scale);
	}
	
	public void draw(SpriteBatch batch, int x, int y, Profile profile) {
		TextureRegion meatTexture = Assets.getMeatTexture(this);
		if (meatTexture == null) return;
		draw(batch, meatTexture, this.type.doubleWidth, x, y, profile, 1);
	}
	
	public float getBuyPrice() {
		return type.buyPrice;
	}
	
	public float getSellPrice() {
		return type.sellPrice + grill.profile.inventory.meatQuality.getSellBoost();
	}
	
	// cost for player to purchase
//	public float getBuyPrice(MeatTypes.Type type) {
//		return type.buyPrice;
//	}
//	
//	// cost for customer to sell
//	public float getSellPrice(MeatTypes.Type type) {
//		return type.sellPrice + grill.
//	}
	
	// seconds it takes to become well-cooked
	public float getCookTime() {
		return this.type.cookTime * grill.getCookSpeedFactor();
	}
	
	// seconds it takes to burn after being cooked
	public float getBurnTime() {
		return this.type.burnTime * grill.getBurnSpeedFactor() + this.getCookTime();
	}
	
	public void spice() {
		if (this.spiced) return;
		this.spiced = true;
		TutorialEventHandler.handleSpice();
	}
	
//	public boolean chicken() {
//		return this.type == MeatTypes.Type.CHICKEN;
//	}
}
