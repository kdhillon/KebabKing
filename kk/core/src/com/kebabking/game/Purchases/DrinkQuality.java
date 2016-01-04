package com.kebabking.game.Purchases;

// user has one of these
// later: split this into separate ones for chicken beef, lamb
public class DrinkQuality extends PurchaseType {
	static String name = "DRINK QUALITY";
	static String desc = "Upgrade the quality of your drinks!";
	
	// Specific types that you might own 
	static class Quality extends SimplePurchaseable {
		public static final Quality[] values = new Quality[] {
				// name, initial cost, unlockAt, qual factor, description
		new Quality ("Qing Mao", 	0,	3,	0,	 1, "beer_icon",	"Yellow water"), 
		new Quality ("Heimikan", 	50,	6,	3, 1.1f, "beer_icon",	 "Premium Lager"),
		new Quality ("Youngling", 	100,10, 11, 1.2f, "beer_icon",	 "Twistoffs! Wow!"), 
		new Quality ("Stellar",		200,15, 18, 1.3f, "beer_icon",  "Only 4 ingredients and 10 preservatives!"),
		new Quality ("Guinnist", 	500,20, 20, 1.5f, "beer_icon",	 "Bonnie Irish Stout"),
		new Quality ("Harpspear", 	1000,30, 24, 1.7f, "beer_icon", "The best of the best."),
		new Quality ("Trappist", 	5000,40, 32, 2f,  "beer_icon",	 "Made by Monks! Holy beer!"),
		};
		
		float qualityFactor;
		
		// for kryo
		public Quality() {}

		private Quality(String name, float cash, int coins, int unlockAt, float qualityFactor, String iconName, String description) {
			super(DrinkQuality.name, name, cash, coins, unlockAt, description, "customers/" + iconName);		
			this.qualityFactor = qualityFactor;
		}
	};

	public DrinkQuality(Inventory inventory) {
		super(inventory, name, desc, null, Quality.values);
		unlock(Quality.values[0]);
	}
	
	// for kryo
	public DrinkQuality() {
		super(name, desc, null, Quality.values);
	};
	
	// TODO implement this
	public float getBuyPrice() {
		return 3;
	}
	public float getSellPrice() {
		return 5;
	}
}
