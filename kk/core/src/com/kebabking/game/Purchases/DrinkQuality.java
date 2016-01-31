package com.kebabking.game.Purchases;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kebabking.game.Assets;
import com.kebabking.game.ProfileInventory;

// user has one of these
// later: split this into separate ones for chicken beef, lamb
public class DrinkQuality extends PurchaseType {
	static String name = "DRINK QUALITY";
	static String desc = "Upgrade the quality of your drinks!";
	
	// Specific types that you might own 
	static class Quality extends SimplePurchaseable {
		public static final Quality[] values = new Quality[] {
				// name, initial cost, unlockAt, qual factor, description
		new Quality ("Qing Mao", 	0,	3,	0,	 1, "lightgreen",	"Yellow water"), 
		new Quality ("Heimikan", 	50,	6,	3, 1.1f, "green",	 "Premium Lager"),
		new Quality ("Youngling", 	100,10, 11, 1.2f, "brown",	 "Twistoffs! Wow!"), 
		new Quality ("Stellar",		200,15, 18, 1.3f, "yellow",  "Only 4 ingredients and 10 preservatives!"),
		new Quality ("Guinnist", 	500,20, 20, 1.5f, "dark",	 "Bonnie Irish Stout"),
		new Quality ("Harpspear", 	1000,30, 24, 1.7f, "red", 	"Delicious, fisherman's favorite."),
		new Quality ("Trappish", 	5000,40, 32, 2f,  	"tan",	 "Made by Monks! Holy beer!"),
		};
		
		float qualityFactor;
		public TextureRegion coolerRegion; 
		
		// for kryo
		public Quality() {}

		private Quality(String name, float cash, int coins, int unlockAt, float qualityFactor, String color, String description) {
			super(name, cash, coins, unlockAt, description, "market/icons/" + color + "_thumb");
			this.qualityFactor = qualityFactor;
			this.coolerRegion = Assets.getTextureRegion("market/icons/" + color + "_cooler");
		}
	};

	public DrinkQuality(ProfileInventory inventory) {
		super(inventory, name, desc, null, Quality.values);
		unlock(Quality.values[0]);
	}
	
	// for kryo
	public DrinkQuality() {
		super(name, desc, null, Quality.values);
	};
	
	// TODO implement this
	public float getBuyPrice() {
		// when you change this, make sure to change the way daily rev is calculated on summary
		return 0;
	}
	public float getSellPrice() {
		return 2;
	}
	
	public TextureRegion getCooler() {
		return ((Quality) getCurrentSelected()).coolerRegion;
	}
}
