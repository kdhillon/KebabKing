package com.kebabking.game.Purchases;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kebabking.game.Assets;
import com.kebabking.game.ProfileInventory;

//this class contains the user's current grill Stand
public class GrillStand extends PurchaseType {	
	static String name = "GRILL STAND";
	static String grillStandDescription = "Change your grill stand!";

	// Specific Stands that you might own 
	// Using a static class allows us to extend simplepurchaseable, but still basically be an enum.
	public static class Stand extends SimplePurchaseable {
		public static final Stand[] values = new Stand[] {
								//    round, coins, desc, dayLengthBoost
				new Stand("Metal Stand", 0,  0f, 0, 1.0f, "Simple metal frame", "grill_stand-01", "grill_stand_thumb-06"),
				new Stand("Wooden Table",17, 0f, 5, 1, "A nice oak table", "grill_stand-03", "grill_stand_thumb-08"),
				new Stand("Metal Table", 25, 0f, 10, 1, "Durable steel table", "grill_stand-02", "grill_stand_thumb-07"),
				new Stand("Bicycle",	 34, 0f, 15, 1, "Light and agile", "grill_stand-04", "grill_stand_thumb-09"),
				new Stand("Motorcycle",  43, 0f, 20, 1, "Makes you look cool!", "grill_stand-05", "grill_stand_thumb-10"),
		};
	
		public TextureRegion back;
		float dayLengthBoost;
		
		// for Kryo
		private Stand() {}
		
		private Stand(String name, int unlockRound, float priceInCash, int priceInCoins, float dayLengthBoost, String description, String back, String icon) {
//			super(name, priceInCoins, description, "grill/"+icon);
			super(name, priceInCash, priceInCoins, unlockRound, description, "grill/"+icon);
			this.back = Assets.getTextureRegion("grill/"+back);
			this.dayLengthBoost = dayLengthBoost;
		}
	};

	
	public GrillStand(ProfileInventory inventory) {
		super(inventory, name, grillStandDescription, Stand.values);
		unlock(values[0]);
	}
	
	// for kryo
	public GrillStand() {
		super(name, grillStandDescription, Stand.values);
	}
};