package com.kebabking.game.Purchases;

// this class contains the user's current grill Size
public class StickType extends PurchaseType {	

	// Specific Sizes that you might own 
	// Using a static class allows us to extend simplepurchaseable, but still basically be an enum.
	static class Type extends SimplePurchaseable {
		public static final Type[] values = new Type[]{
				// Doesn't do anything except change icon right now
				new Type("Bamboo", 		0,	 0, 0,	"Kebab_Stick-29", "Pretty flimsy"),
				new Type("Wood", 		50,	 3,  10, "Kebab_Stick-28", "Solid, but they might burn"), 
				new Type("Metal", 		200,  10, 20, "Kebab_Stick-27", "Indestructible!"),
		};
	
		int size;
		
		private Type() {};
		
		private Type(String name, float cash, int coins, int unlockAtLevel, String texture, String description) {
			super(name, cash, coins, unlockAtLevel, description, "kebabs/" + texture);
		}
	};

	static String desc = "Change your kebab sticks!";
	
	public StickType(Inventory inventory) {
		super(inventory, "KEBAB STICK", desc, null, Type.values);
		unlock(values[0]);
	}
	
	// for kryo
	public StickType() {
		super("KEBAB STICK", desc, null, Type.values);
	};
}
