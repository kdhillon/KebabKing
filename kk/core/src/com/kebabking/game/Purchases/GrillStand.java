package com.kebabking.game.Purchases;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kebabking.game.Assets;
import com.kebabking.game.ProfileInventory;

//this class contains the user's current grill Stand
public class GrillStand extends PurchaseType {	
	static String name = "grill_stand";
	static String grillStandDescription = "grill_stand_desc";
	
	// Specific Stands that you might own 
	// Using a static class allows us to extend simplepurchaseable, but still basically be an enum.
	public static class Stand extends SimplePurchaseable {
		public static final Stand[] values = new Stand[] {
								//    round, coins, desc, dayLengthBoost
				new Stand("metal", 		 0, 1, 0f, 0, 1.0f, "grill_stand-01", "grill_stand_thumb-06"),
				new Stand("wooden",		 17,6, 1200f, 15, 1.15f, "grill_stand-03", "grill_stand_thumb-08"),
				new Stand("metal_table", 25,10, 2500f, 20, 1.3f, "grill_stand-02", "grill_stand_thumb-07"),
				new Stand("bicycle",	 34,13, 4500f, 25, 1.45f, "grill_stand-04", "grill_stand_thumb-09"),
				new Stand("motorcycle",  43,15, 7500f, 30, 1.6f, "grill_stand-05", "grill_stand_thumb-10"),
		};
	
		public TextureRegion back;
		float dayLengthBoost;
		
		// for Kryo
		private Stand() {}
		
		private Stand(String name, int unlockRound, int unlockWithLocation, float priceInCash, int priceInCoins, float dayLengthBoost, String back, String icon) {
//			super(name, priceInCoins, description, "grill/"+icon);
			super(GrillStand.name + "_" + name, priceInCash, priceInCoins, unlockRound, unlockWithLocation, "market/icons/"+icon);
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
	
	
	public float getLengthBoost() {
		return ((GrillStand.Stand) this.getFirstSelected()).dayLengthBoost;
	}
};