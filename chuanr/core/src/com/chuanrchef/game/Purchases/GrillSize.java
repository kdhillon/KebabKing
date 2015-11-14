package com.chuanrchef.game.Purchases;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

// this class contains the user's current grill Size
public class GrillSize extends PurchaseType {		
	// Specific Sizes that you might own 
	enum Size implements Purchaseable{
		// name, initial cost, daily cost, int size, description
		Tiny("Small", 		0,	 0, 4,	"Fits just 4 kebabs!"), 
		Small("Small", 		0,	 0, 6,	"Fits just 6 kebabs!"), 
		Medium("Medium", 	20, 0, 8,	"Fits 8 whole kebabs!"),
		Large("Large", 		500, 0, 10,	"Fits 10 kebabs! Wow!");
//		Huge("Huge", 		1000, 0, 12,	"Gives you total control over grill heat!");
		
		String name; 
		float initialCost;
		float dailyCost;
		int size;
		String description;
		
		private Size(String name, float initialCost, float dailyCost, int size, String description) {
			this.name = name;
			this.initialCost = initialCost;
			this.dailyCost = dailyCost;
			this.size = size;
			this.description = description;
		}
		@Override
		public String getName() {
			return this.name;
		}
		@Override
		public int coinsToUnlock() {
			return 0;
		}
		@Override
		public float cashToUnlock() {
			return this.initialCost;
		}
		@Override
		public float getDailyCost() {
			return this.dailyCost;
		}
		@Override
		public TextureRegion getIcon() {
			return null;
		}
		@Override
		public String getDescription() {
			return this.description;
		}
		@Override
		public int unlockAtRound() {
			return 0;
		}
	};

	static String grillSizeDescription = "Change your grill Size!";
	
	public GrillSize(Inventory inventory) {
		super(inventory, "Grill Size", grillSizeDescription, null, Size.values());
		unlock(Size.values()[0]);
	}
	
	// for kryo
	public GrillSize() {};
}
