package com.kebabking.game.Purchases;

//later: split this into separate ones for chicken beef, lamb
public class MeatQuality extends PurchaseType {
	
	// Specific types that you might own 
	static class Quality extends SimplePurchaseable {
		public static final Quality[] values = new Quality[] {
//				// name, initial cost, daily cost, qual factor, description		
				new Quality ("Grade B Meat", 	0,	 0,	 1, 	"Pretty crappy meat!"), 
				new Quality  ("Grade A Meat", 	150, 4, 1.1f,	 "Delicious stuff. Still not sure where it came from, but tastes great. Animals."),
				new Quality  ("Grade AA Meat", 	500, 9, 1.2f,	 "A bit better; still tastes kinda fishy."), 
				new Quality  ("Grade AAA Meat",	1000, 18, 1.3f,  "Almost restaurant quality!"),
				new Quality  ("Top Quality Meat", 2000, 24, 1.4f,	 "You can't find better!"),
				new Quality  ("God Meat", 		2000, 100, 1.4f,	 "Divine!")
		};
		
		float qualityFactor;

		private Quality(String name, float initialCost, int unlockAt, float qualityFactor, String description) {
			super(name, initialCost, unlockAt, description, "market/Market_subMenus__template_element-06");			
			this.qualityFactor = qualityFactor;
		}
	};

	public MeatQuality(Inventory inventory) {
		super(inventory, "Drink Quality", "Upgrade the quality of your drinks!", null, Quality.values);
		unlock(Quality.values[0]);
	}
	
	// for kryo
	public MeatQuality() {};
}

