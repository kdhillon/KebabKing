package com.chuanrchef.game.Purchases;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.chuanrchef.game.Assets;


// user has one of these
// later: split this into separate ones for chicken beef, lamb
public class DrinkQuality extends PurchaseType {
	// Specific types that you might own 
	enum Quality implements Purchaseable{
		// name, initial cost, daily cost, qual factor, description
		LEVEL1 ("Bub Light", 	0,	 0,	 1, "customers/beer_icon",	"The classic"), 
		LEVEL2 ("Heimikan", 	150, 5, 1.1f, "customers/beer_icon",	 "Imported! Wow!"),
		LEVEL3 ("Rolling Log", 	500, 10, 1.2f, "customers/beer_icon",	 "Goes down easy"), 
		LEVEL4 ("Sandshark",	1000, 25, 1.3f, "customers/beer_icon",  "Everybody loves Sandshark"),
		LEVEL5 ("Catfish Ale", 2000, 40, 1.4f, "customers/beer_icon",	 "The best of the best.");

		String name; 
		float initialCost;
		float dailyCost;
		String description;
		float qualityFactor;
		TextureRegion icon;

		private Quality(String name, float initialCost, float dailyCost, float qualityFactor, String iconName, String description) {
			this.name = name;
			this.initialCost = initialCost;
			this.dailyCost = dailyCost;
			this.qualityFactor = qualityFactor;
			this.description = description;
			this.icon = Assets.getTextureRegion(iconName);
		}
		
		@Override
		public String getName() {
			return this.name;
		}
		@Override
		public int coinsToUnlock() {
			return 0;
		}
		@Override
		public float cashToUnlock() {
			return this.initialCost;
		}
		@Override
		public float getDailyCost() {
			return this.dailyCost;
		}
		@Override
		public TextureRegion getIcon() {
			return icon;
		}
		@Override
		public String getDescription() {
			return this.description;
		}
		@Override
		public int unlockAtRound() {
			return 0;
		}
	};

	public DrinkQuality(Inventory inventory) {
		super(inventory, "Drink Quality", "Upgrade the quality of your drinks!", null, Quality.values());
		unlock(Quality.LEVEL1);
	}
	
	// for kryo
	public DrinkQuality() {};
}
