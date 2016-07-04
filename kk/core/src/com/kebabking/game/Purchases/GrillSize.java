package com.kebabking.game.Purchases;

import com.kebabking.game.ProfileInventory;

// this class contains the user's current grill Size
public class GrillSize extends PurchaseType {	
	static String name = "grill_size";
	static String grillSizeDescription = "grill_size_desc";
			
	// Specific Sizes that you might own 
	// Using a static class allows us to extend simplepurchaseable, but still basically be an enum.
	static class Size extends SimplePurchaseable {
		public static final Size[] values = new Size[] {
				new Size("tiny", 		0,		0, 	0, 	1, 4,"grill-00"),
//				new Size("small", 		100,	2,	4,  5, "grill-01"), 
				new Size("med", 		200,	3,	4,  2, 6,"grill-02"), 
//				new Size("big", 		500,	4,	10, 7,  "grill-03"),
				new Size("large", 		1000,	20,	12, 7, 8, "grill-04"),
//				new Size("huge", 		1000,	10,	24, 9, "grill-05"),
				new Size("enormous", 	9500,	40,	33, 11, 10,"grill-07"),
		};
	
		int size;
		
		// for Kryo
		private Size() {}
		
		private Size(String name, float cash, int coins, int unlockAtLevel, int unlockWithLocation, int size, String icon) {
			super(GrillSize.name + "_" + name, cash, coins, unlockAtLevel, unlockWithLocation, "market/icons/" + icon);
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
