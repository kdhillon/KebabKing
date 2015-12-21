package com.kebabking.game.Purchases;

// user has one of these
// later: split this into separate ones for chicken beef, lamb
public class DrinkQuality extends PurchaseType {
	// Specific types that you might own 
	static class Quality extends SimplePurchaseable {
		public static final Quality[] values = new Quality[] {
				// name, initial cost, unlockAt, qual factor, description
		new Quality ("Bub Light", 	0,	 0,	 1, "beer_icon",	"The classic"), 
		new Quality ("Heimikan", 	150, 5, 1.1f, "beer_icon",	 "Imported! Wow!"),
		new Quality ("Rolling Log", 500, 13, 1.2f, "beer_icon",	 "Goes down easy"), 
		new Quality ("Sandshark",	1000, 18, 1.3f, "beer_icon",  "Everybody loves Sandshark"),
		new Quality ("Catfish Ale", 2000, 24, 1.4f, "beer_icon",	 "The best of the best.")
		};
		
		float qualityFactor;

		private Quality(String name, float initialCost, int unlockAt, float qualityFactor, String iconName, String description) {
			super(name, initialCost, unlockAt, description, "customers/" + iconName);		
			this.qualityFactor = qualityFactor;
		}
	};

	public DrinkQuality(Inventory inventory) {
		super(inventory, "Drink Quality", "Upgrade the quality of your drinks!", null, Quality.values);
		unlock(Quality.values[0]);
	}
	
	// for kryo
	public DrinkQuality() {};
}
