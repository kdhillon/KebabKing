package com.kebabking.game.Purchases;

import com.kebabking.game.ProfileInventory;

// this class contains the user's current grill Size
public class SkewerType extends PurchaseType {	
	static String name = "SKEWERS";
	static String desc = "Better skewers make your customers happier!";

	// Specific Sizes that you might own 
	// Using a static class allows us to extend simplepurchaseable, but still basically be an enum.
	static class Type extends SimplePurchaseable {
		public static final Type[] values = new Type[]{
				new Type("Bamboo Skewers", 		0,	 0,	  0,	100,	  "Kebab_Stick-29", "Simple bamboo skewers"),
				new Type("Wooden Skewers", 		50,	 10,  11,	1.1f, "Kebab_Stick-28", "Customers love wooden skewers"), 
				new Type("Iron Skewers", 		200,  26, 27, 	1.2f, "Kebab_Stick-27", "Even better than wood"),
				new Type("Titanium Skewers", 	500,  26, 37,	1.3f, "Kebab_Stick-27", "State-of-the-art"),
		};
	
		float satBoost;
		
		private Type() {};
		
		private Type(String name, float cash, int coins, int unlockAtLevel, float satBoost, String texture, String description) {
			super(name, cash, coins, unlockAtLevel, description, "kebabs/" + texture);
			this.satBoost = satBoost;
		}
	};
	
	public SkewerType(ProfileInventory inventory) {
		super(inventory, name, desc, Type.values);
		unlock(values[0]);
	}
	
	// for kryo, make sure to initialize
	public SkewerType() {
		super(name, desc, Type.values);
	};
	
	public float getSatBoost() {
		return ((SkewerType.Type) this.getFirstSelected()).satBoost;
	}
}
