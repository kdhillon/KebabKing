package com.kebabking.game.Purchases;

// this class contains the user's current grill Size
public class GrillSize extends PurchaseType {	

	// Specific Sizes that you might own 
	// Using a static class allows us to extend simplepurchaseable, but still basically be an enum.
	static class Size extends SimplePurchaseable {
		public static final Size[] values = new Size[] {
				new Size("Tiny", 		0,		0, 	0, 	4,	"Fits a puny 4 kebabs"),
				new Size("Small", 		100,	0,	0,  6,	"Fits 6 kebabs"), 
				new Size("Medium", 		500,	4,	10, 8,	"Fits 8 whole kebabs!"),
				new Size("Large", 		1000,	10,	20, 10,	"Fits 10 kebabs! Wow!"),
		};
	
		int size;
		
		// for Kryo
		private Size() {}
		
		private Size(String name, float cash, int coins, int unlockAtLevel, int size, String description) {
			super(name, cash, coins, unlockAtLevel, description, "");
			this.size = size;
		}
	};

	static String grillSizeDescription = "Change your grill Size!";
	
	public GrillSize(Inventory inventory) {
		super(inventory, "GRILL SIZE", grillSizeDescription, null, Size.values);
		unlock(values[0]);
	}
	
	// for kryo
	public GrillSize() {
		super("GRILL SIZE", grillSizeDescription, null, Size.values);
	};
}
