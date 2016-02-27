package com.kebabking.game.Purchases;

import com.kebabking.game.ProfileInventory;

// this class contains the user's current grill Size
public class GrillSize extends PurchaseType {	
	static String name = "GRILL SIZE";
	static String grillSizeDescription = "Larger grills will let you cook more meat at once!";
	
	// Specific Sizes that you might own 
	// Using a static class allows us to extend simplepurchaseable, but still basically be an enum.
	static class Size extends SimplePurchaseable {
		public static final Size[] values = new Size[] {
				new Size("Tiny Grill", 			0,		0, 	0, 	4,	"Fits a puny 4 kebabs", "grill-00"),
				new Size("Small Grill", 		100,	2,	2,  6,	"Fits 6 kebabs", "grill-01"), 
				new Size("Medium Grill", 		500,	4,	10, 8,	"Fits 8 whole kebabs!", "grill-02"),
				new Size("Large Grill", 		1000,	10,	20, 10,	"Fits 10 kebabs! Wow!", "grill-03"),
		};
	
		int size;
		
		// for Kryo
		private Size() {}
		
		private Size(String name, float cash, int coins, int unlockAtLevel, int size, String description, String icon) {
			super(name, cash, coins, unlockAtLevel, description, "market/icons/" + icon);
			this.size = size;
		}
	};

	
	public GrillSize(ProfileInventory inventory) {
		super(inventory, name, grillSizeDescription, Size.values);
		unlock(values[0]);
	}
	
	// for kryo
	public GrillSize() {
		super(name, grillSizeDescription, Size.values);
	};
	
	public int getSize() {
		return ((Size) this.getFirstSelected()).size;
	}
}
