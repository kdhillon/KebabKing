package com.kebabking.game.Purchases;

// this class contains the user's current grill type
public class GrillType extends PurchaseType {	
	// Specific types that you might own 
	static class Type extends SimplePurchaseable {
		public static final Type[] values = new Type[]{
				new Type("Standard", 		0,	 0,  0, 	"Just your usual charcoal grill!"), 
				new Type("Gas", 			200, 5,  6, 	"Cooks faster, burn slower!"),
				new Type("Warm", 			500, 2,  35, 	"Adds a warmer section to your grill"), 
				new Type("Flame Control", 	1000, 15, 43, 	"Gives you total control over grill heat!")
		};
		// name, initial cost, daily cost, description
		
		// for Kryo
		private Type() {}
		
		private Type(String name, float cash, int coins, int unlockAtLevel, String description) {
			super(name, cash, coins, unlockAtLevel, description, "");
		}
	};
	static String grillTypeDescription = "Change your grill type!";

	public GrillType(Inventory inventory) {
		super(inventory, "GRILL TYPE", grillTypeDescription, null, Type.values);
		unlock(Type.values[0]);
	}
	
	// for kryo
	public GrillType() {
		super("GRILL TYPE", grillTypeDescription, null, Type.values);
	};
}

