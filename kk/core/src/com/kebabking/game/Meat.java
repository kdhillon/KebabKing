package com.kebabking.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

// represents a piece of meat on the grill
public class Meat {
	enum Type {  // buy	   sell     cook    burn
		BEEF(		2, 		3.5f,		8,		8), 
		CHICKEN(	2.5f,	4f,		5,		6),  
		LAMB(		1.5f,	3f, 	7,		7);
		
		float buyPrice;
		float sellPrice;
		float cookTime;
		float burnTime;
		
		private Type(float buy, float sell, float cook, float burn) {
			this.buyPrice = buy;
			this.sellPrice = sell;
			this.cookTime = cook;
			this.burnTime = burn;
		}
	}
	enum State {RAW, COOKED, BURNT}
	
	Type type;
	State state;
	
	boolean spiced;
	
	float cookTime; // time on the grill so far
	
	int index1;
	int index2;

	// create a raw meat on the grill 
	public Meat(Type type) {
		this.type = type;
		this.state = State.RAW;
		this.cookTime = 0;
		this.index1 = -1;
		this.index2 = -1;
	}
	
	public void setIndex(int index) {
		this.index1 = index;
		if (this.type == Type.CHICKEN) this.index2 = index + 1;
	}
	
	// what to do every frame
	public void act(float delta) {
		// simply update cooked-ness
		cookTime += delta;
		if (cookTime > this.getBurnTime())
			this.state = State.BURNT;
		else if (cookTime > this.getCookTime())
			this.state = State.COOKED;
	}
	
	public void draw(SpriteBatch batch, int x, int y) {
		TextureRegion stickTexture = Assets.getStickTexture();
		TextureRegion meatTexture = Assets.getMeatTexture(this);
//		switch (state) {
//		case RAW: 
//			switch (type) {
//			case CHICKEN:
//				if (!this.spiced)
//					toDraw = Assets.chuanrChickenRaw;	
//				else
//					toDraw = Assets.chuanrChickenRawSpice;
//				break;
//			case BEEF:
//				if (!this.spiced)
//					toDraw = Assets.chuanrBeefRaw;	
//				else 
//					toDraw = Assets.chuanrBeefRawSpice;
//				break;
//			case LAMB:
//				if (!this.spiced)
//					toDraw = Assets.chuanrLambRaw;	
//				else 
//					toDraw = Assets.chuanrLambRawSpice;
//				break;
//			}
//			break;
//		case COOKED:
//			switch (type) {
//			case CHICKEN:
//				if (!this.spiced)
//					toDraw = Assets.chuanrChickenCooked;
//				else 
//					toDraw = Assets.chuanrChickenCookedSpice;
//				break;
//			case BEEF:
//				if (!this.spiced)
//					toDraw = Assets.chuanrBeefCooked;
//				else 
//					toDraw = Assets.chuanrBeefCookedSpice;
//				break;
//			case LAMB:
//				if (!this.spiced)
//					toDraw = Assets.chuanrLambCooked;
//				else 
//					toDraw = Assets.chuanrLambCookedSpice;
//				break;
//			}
//			break;
//		case BURNT:
//			switch (type) {
//			case CHICKEN:
//				toDraw = Assets.chuanrChickenBurnt;
//				break;
//			case BEEF:
//				toDraw = Assets.chuanrBeefBurnt;
//				break;
//			case LAMB:
//				toDraw = Assets.chuanrLambBurnt;
//				break;
//			}
//			break;
//		}
		
		// First draw meat
		int width =  (int) (Grill.GRILL_PIECE_WIDTH * KitchenScreen.UNIT_WIDTH / 
				Grill.CHUANR_PER_PIECE);
		int height = (int) (KitchenScreen.UNIT_HEIGHT * Grill.CHUANR_HEIGHT);
		
		batch.draw(stickTexture, x, y, width, KitchenScreen.UNIT_HEIGHT * Grill.GRILL_PIECE_HEIGHT);

		if (this.chicken()) {
			batch.draw(stickTexture, x + KitchenScreen.UNIT_WIDTH, y, width, KitchenScreen.UNIT_HEIGHT * Grill.GRILL_PIECE_HEIGHT);
			width *= 2;
		}
		
		batch.draw(meatTexture, x, y + KitchenScreen.UNIT_HEIGHT * (Grill.GRILL_PIECE_HEIGHT - Grill.CHUANR_HEIGHT), width, height);
	}
	
	// cost for player to purchase
	public static float getBuyPrice(Meat.Type type) {
		return type.buyPrice;
	}
	
	// cost for customer to sell
	public static float getSellPrice(Meat.Type type) {
		return type.sellPrice;
	}
	
	// seconds it takes to become well-cooked
	public float getCookTime() {
		return this.type.cookTime;
	}
	
	// seconds it takes to burn after being cooked
	public float getBurnTime() {
		return this.type.burnTime + this.type.cookTime;
	}
	
	public void spice() {
		this.spiced = true;
	}
	
	public boolean chicken() {
		return this.type == Type.CHICKEN;
	}
}