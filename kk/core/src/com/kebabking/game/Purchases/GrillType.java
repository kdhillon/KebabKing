package com.kebabking.game.Purchases;

// this class contains the user's current grill type
public class GrillType extends PurchaseType {	
	// Specific types that you might own 
	static class Type extends SimplePurchaseable {
		public static final Type[] values = new Type[]{
				new Type("Standard", 		0,	 0, 	"Just your usual charcoal grill!"), 
				new Type("Gas", 			150, 11, 	"Cooks kebabs faster than charcoal! Don't burn them!"),
				new Type("Warm", 			500, 21, 	"Adds a warmer section to your grill"), 
				new Type("Flame Control", 	1000, 32, 	"Gives you total control over grill heat!")
		};
		// name, initial cost, daily cost, description
		
		private Type(String name, float initialCost, int unlockAtLevel, String description) {
			super(name, initialCost, unlockAtLevel, description, "");
		}
	};
	static String grillTypeDescription = "Change your grill type!";

	public GrillType(Inventory inventory) {
		super(inventory, "Grill Type", grillTypeDescription, null, Type.values);
		unlock(Type.values[0]);
	}
	
	// for kryo
	public GrillType() {};
}

