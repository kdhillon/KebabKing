package com.kebabking.game.Purchases;

import com.kebabking.game.ProfileInventory;

// this class contains the user's current grill Size
public class StickType extends PurchaseType {	
	static String name = "SKEWERS";
	static String desc = "Change your kebab sticks!";

	// Specific Sizes that you might own 
	// Using a static class allows us to extend simplepurchaseable, but still basically be an enum.
	static class Type extends SimplePurchaseable {
		public static final Type[] values = new Type[]{
				// Doesn't do anything except change icon right now
				new Type("Bamboo Skewers", 		0,	 0, 0,	"Kebab_Stick-29", "Pretty flimsy"),
				new Type("Wooden Skewers", 		50,	 3,  10, "Kebab_Stick-28", "Solid, but they might burn"), 
				new Type("Metal Skewers", 		200,  10, 20, "Kebab_Stick-27", "Indestructible!"),
		};
	
		int size;
		
		private Type() {};
		
		private Type(String name, float cash, int coins, int unlockAtLevel, String texture, String description) {
			super(name, cash, coins, unlockAtLevel, description, "kebabs/" + texture);
		}
	};
	
	public StickType(ProfileInventory inventory) {
		super(inventory, name, desc, null, Type.values);
		unlock(values[0]);
	}
	
	// for kryo, make sure to initialize
	public StickType() {
		super(name, desc, null, Type.values);
	};
}
