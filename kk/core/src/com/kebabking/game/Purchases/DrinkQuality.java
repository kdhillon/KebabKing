package com.kebabking.game.Purchases;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kebabking.game.Assets;
import com.kebabking.game.ProfileInventory;

// user has one of these
// later: split this into separate ones for chicken beef, lamb
public class DrinkQuality extends PurchaseType {
	static String name = "drink_quality";
	static String desc = "drink_quality_desc";
	
	// Specific types that you might own 
	public static class Quality extends SimplePurchaseable {
		public static final Quality[] values = new Quality[] {
		// name, initial cost, unlockAt, sell price, description
		new Quality ("1", 	0,		0,	0, 1,	 1, 	"lightgreen"), 
		new Quality ("2", 	200,	3,	3, 2, 1.5f, 	"green"),
		new Quality ("3", 	500,	10, 11,4,  2f, 	"brown"), 
		new Quality ("4",	800,	15, 19,6, 2.5f,	"yellow"),
		new Quality ("5", 	1100,	15, 28,10,  3f,	 	"dark"),
		new Quality ("6", 	2500,	20, 35,12,  4f, 	"red"),
		new Quality ("7", 	5000,	25, 48,16,  5f, 	"tan"),
		};
		
		float sellPrice;
		public TextureRegion coolerRegion; 
		
		// for kryo
		public Quality() {}

		private Quality(String name, float cash, int coins, int unlockAt, int unlockWithLocation, float qualityFactor, String color) {
			super(DrinkQuality.name + "_" + name, cash, coins, unlockAt, unlockWithLocation, "market/icons/" + color + "_thumb");
			this.sellPrice = qualityFactor;
			this.coolerRegion = Assets.getTextureRegion("kebabs/" + color + "_cooler");
		}
	};

	public DrinkQuality(ProfileInventory inventory) {
		super(inventory, name, desc, Quality.values);
		unlock(Quality.values[0]);
	}
	
	// for kryo
	public DrinkQuality() {
		super(name, desc, Quality.values);
	};
	
	// TODO implement this
	public float getBuyPrice() {
		// when you change this, make sure to change the way daily rev is calculated on summary
		return 0;
	}
	public float getSellPrice() {
		return ((Quality) getFirstSelected()).sellPrice;
	}
	
	public TextureRegion getCooler() {
		return ((Quality) getFirstSelected()).coolerRegion;
	}
}
