package com.kebabking.game.Purchases;

import com.kebabking.game.ProfileInventory;

//later: split this into separate ones for chicken beef, lamb
public class MeatQuality extends PurchaseType {
	static String name = "MEAT";
	static String desc = "Better quality meat means more profits per kebab!";
	
	// Specific types that you might own 
	static class Quality extends SimplePurchaseable {
		public static final Quality[] values = new Quality[] {
//				// 				name, 	cash,  coins, unlock at, sellboost, qual factor, description		
				new Quality  ("Grade C", 	0,	 0,		0, 		0,		1, 		"Pretty fishy smell to it"), 
				new Quality  ("Grade B", 	500, 2, 	8, 		0.5f,	1.1f,	"I think it's meat"),
				new Quality  ("Grade A", 	1000, 5, 	17, 	1f,		1.2f,	"Definitely meat"),
				new Quality  ("Grade AA", 	2000, 10, 	27, 	1.5f,	1.3f,	"Great organic meat"), 
				new Quality  ("Grade AAA",	5000, 15, 	39, 	2f,		1.5f,  	"Mouth-watering meat"),
		};
		
		float qualityFactor;
		float sellBoost; // added to sell price of kebabs
		
		// for kryo
		private Quality(){}

		private Quality(String name, float cash, int coins, int unlockAt, float sellBoost, float qualityFactor, String description) {
			super(name, cash, coins, unlockAt, description, "market/Market_subMenus__template_element-06");			
			this.qualityFactor = qualityFactor;
			this.sellBoost = sellBoost;
		}
	};

	public MeatQuality(ProfileInventory inventory) {
		super(inventory, name, desc, Quality.values);
		unlock(Quality.values[0]);
	}
	
	// for kryo
	public MeatQuality() {
		super(name, desc, Quality.values);
	};
	
	public float getSellBoost() {
		return ((Quality) this.getFirstSelected()).sellBoost;
	}
}

