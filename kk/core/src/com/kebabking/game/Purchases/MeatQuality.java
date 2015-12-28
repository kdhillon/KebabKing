package com.kebabking.game.Purchases;

//later: split this into separate ones for chicken beef, lamb
public class MeatQuality extends PurchaseType {
	
	// Specific types that you might own 
	static class Quality extends SimplePurchaseable {
		public static final Quality[] values = new Quality[] {
//				// 					name, 	cash,  coins, unlock at, qual factor, description		
				new Quality ("Grade C Meat", 	0,	 0,	0, 1, 		"Pretty sketchy 'meat'..."), 
				new Quality  ("Grade B Meat", 	100, 2, 8, 1.1f,	 "It probably comes from animals."),
				new Quality  ("Grade A Meat", 	250, 5, 13, 1.2f,	 "Smells great!"),
				new Quality  ("Grade AA Meat", 	500, 10, 21, 1.3f,	 "Almost restaurant quality!"), 
				new Quality  ("Grade AAA Meat",	1000, 15, 37, 1.4f,  "You can't find better!"),
		};
		
		float qualityFactor;
		
		// for kryo
		private Quality(){}

		private Quality(String name, float cash, int coins, int unlockAt, float qualityFactor, String description) {
			super(name, cash, coins, unlockAt, description, "market/Market_subMenus__template_element-06");			
			this.qualityFactor = qualityFactor;
		}
	};

	public MeatQuality(Inventory inventory) {
		super(inventory, "MEAT QUALITY", "Upgrade the quality of your drinks!", null, Quality.values);
		unlock(Quality.values[0]);
	}
	
	// for kryo
	public MeatQuality() {
		super("MEAT QUALITY", "Upgrade the quality of your drinks!", null, Quality.values);
	};
}

