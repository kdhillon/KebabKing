package com.kebabking.game.Purchases;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kebabking.game.Assets;
import com.kebabking.game.ProfileInventory;

// this class contains the user's current grill type
public class GrillType extends PurchaseType {	
	static String name = "grill_type";
	static String grillTypeDescription = "grill_type_desc";
	
	// Specific types that you might own 
	public static class Type extends SimplePurchaseable {
		public static final Type[] values = new Type[]{
				new Type("basic", 			0,	 	0,  0, 1,1f, 	1, 	 false), 
				new Type("charcoal", 		500, 	0,  3, 3,0.9f, 0.9f, false), 
//				new Type("charcoal", 		500, 	0,  6, 0.9f, 0.9f, false), 
				new Type("gas", 			1000, 	5,  13,5, 0.85f,  0.9f, false),
				new Type("gas_warmer", 		1500, 	10, 21,8, 0.85f, 	0.9f, true),
				new Type("electric", 		2000,	15, 32,11, 0.75f, 	0.8f, false),
				new Type("electric_warmer",	2500, 	20, 41,14, 0.75f, 	0.8f, true),
				new Type("nuclear", 		3500, 	25, 47,16, 0.65f, 	0.7f, false),
		};
		// name, initial cost, daily cost, description
		
		public TextureRegion left;
		public TextureRegion center;
		public TextureRegion right;
		public TextureRegion spice;
		public Animation fire;
		public Animation warmingFire;
		public float cookSpeedFactor;
		public float burnSpeedFactor;
		public boolean warming;
		
		// for Kryo
		private Type() {}
		
		private Type(String name, float cash, int coins, int unlockAtLevel, int unlockWithLocation, float cookSpeed, float burnSpeed, boolean warmer) {
			super(GrillType.name + "_" + name, cash, coins, unlockAtLevel, unlockWithLocation, "");
			left = Assets.getTextureRegion("grill/" + getPrefix(name) + "_left");
			center = Assets.getTextureRegion("grill/" + getPrefix(name) + "_center");
			right = Assets.getTextureRegion("grill/" + getPrefix(name) + "_right");
			spice = Assets.getTextureRegion("grill/" + getPrefix(name) + "_spice");
			fire = Assets.createAnimation("grill/"  + getPrefix(name) + "_fire", Assets.GRILL_ANIMATION_TIME, 3, true);
			this.warming = warmer;
			if (warmer) {
				warmingFire = Assets.createAnimation("grill/"  + getPrefix(name) + "_warming", Assets.GRILL_ANIMATION_TIME, 3, true);
				this.icon = Assets.getTextureRegion("market/icons/" + name + "_icon");
			}
			else 
				this.icon = Assets.getTextureRegion("market/icons/" + getPrefix(name) + "_icon");

			this.cookSpeedFactor = cookSpeed;
			this.burnSpeedFactor = burnSpeed;
			

//			fire = Assets.getTextureRegion(name.toLowerCase() + "_fire");
		}
		
		private static String getPrefix(String in) {
			String name = in.toLowerCase();
//			if (name.equals("charcoal")) {
//				name = "basic";
//			}
//			if (name.equals("gas")) {
//				name = "basic";
//			}
			if (name.equals("gas_warmer")) {
				name = "gas";
			}
//			if (name.equals("electric")) {
//				name = "basic";
//			}
			if (name.equals("electric_warmer")) {
				name = "electric";
			}
//			if (name.equals("nuclear")) {
//				name = "basic";
//			}
			return name;
		}
	};

	public GrillType(ProfileInventory inventory) {
		super(inventory, name, grillTypeDescription, Type.values);
		unlock(Type.values[0]);
	}
	
	// for kryo, for now don't initialize
	public GrillType() {
		super(name, grillTypeDescription, Type.values);
	};
	
	public boolean isWarming() {
		return ((GrillType.Type) this.getFirstSelected()).warming;
	}
}

