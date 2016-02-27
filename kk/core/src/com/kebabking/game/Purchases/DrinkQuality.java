package com.kebabking.game.Purchases;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kebabking.game.Assets;
import com.kebabking.game.ProfileInventory;

// user has one of these
// later: split this into separate ones for chicken beef, lamb
public class DrinkQuality extends PurchaseType {
	static String name = "DRINKS";
	static String desc = "Better quality beer means more profit!";
	
	// Specific types that you might own 
	public static class Quality extends SimplePurchaseable {
		public static final Quality[] values = new Quality[] {
		// name, initial cost, unlockAt, sell price, description
		new Quality ("Qing Mao", 	0,		3,	0,	 1, "lightgreen",	"Yellow water"), 
		new Quality ("Heimikan", 	200,	6,	3, 1.5f, "green",	 "Premium Lager"),
		new Quality ("Youngling", 	500,	10, 11, 2f, "brown",	 "Twistoffs! Wow!"), 
		new Quality ("Stellar",		800,	15, 19, 2.5f, "yellow",  "Only 4 ingredients and 10 preservatives!"),
		new Quality ("Guinnist", 	1100,	20, 27, 3f, "dark",	 "Bonnie Irish Stout"),
		new Quality ("Harpspear", 	1500,	30, 35, 3.5f, "red", 	"Delicious, fisherman's favorite."),
		new Quality ("Trappish", 	2000,	40, 48, 4f,  	"tan",	 "Made by Monks! Holy beer!"),
		};
		
		float sellPrice;
		public TextureRegion coolerRegion; 
		
		// for kryo
		public Quality() {}

		private Quality(String name, float cash, int coins, int unlockAt, float qualityFactor, String color, String description) {
			super(name, cash, coins, unlockAt, description, "market/icons/" + color + "_thumb");
			this.sellPrice = qualityFactor;
			this.coolerRegion = Assets.getTextureRegion("market/icons/" + color + "_cooler");
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
