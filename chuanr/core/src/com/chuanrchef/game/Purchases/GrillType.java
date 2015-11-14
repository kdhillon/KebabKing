package com.chuanrchef.game.Purchases;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

// this class contains the user's current grill type
public class GrillType extends PurchaseType {	
	// Specific types that you might own 
	enum Type implements Purchaseable{
		// name, initial cost, daily cost, description
		Regular("Standard", 	0,	 0, 	"Just your usual charcoal grill!"), 
		Gas		("Gas", 		150, 5, 	"Cooks kebabs faster than charcoal! Don't burn them!"),
		Storage("Warm", 		500, 10, 	"Adds a warmer section to your grill"), 
		Control("Flame Control", 1000, 25, 	"Gives you total control over grill heat!");
		
		String name; 
		float initialCost;
		float dailyCost;
		String description;
		
		private Type(String name, float initialCost, float dailyCost, String description) {
			this.name = name;
			this.initialCost = initialCost;
			this.dailyCost = dailyCost;
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
	static String grillTypeDescription = "Change your grill type!";

	public GrillType(Inventory inventory) {
		super(inventory, "Grill Type", grillTypeDescription, null, Type.values());
		unlock(Type.Regular);
	}
	
	// for kryo
	public GrillType() {};
}

