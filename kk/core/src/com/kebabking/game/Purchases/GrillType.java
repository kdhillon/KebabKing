package com.kebabking.game.Purchases;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kebabking.game.Assets;
import com.kebabking.game.ProfileInventory;

// this class contains the user's current grill type
public class GrillType extends PurchaseType {	
	static String name = "GRILL TYPE";
	static String grillTypeDescription = "Change your grill type!";
	
	// Specific types that you might own 
	public static class Type extends SimplePurchaseable {
		public static final Type[] values = new Type[]{
				new Type("Basic", 			0,	 	0,  0, 1f, 	1, "Runs on twigs and firewood."), 
				new Type("Charcoal", 		500, 	5,  6, 0.9f, 0.9f, "Cooks and burns faster than basic."), 
				new Type("Gas", 			1000, 	8,  13, 0.85f, 	1, "Cooks faster and burns slower than charcoal!"),
				new Type("Gas Warmer", 		1500, 	10, 21, 0.85f, 	1, "Cooks faster and burns slower than charcoal!"),
				new Type("Electric", 		500,	2,  32, 0.8f, 	1,	"Adds a warmer section to your grill"), 
				new Type("Electric Warmer",	1000, 	15, 41, 0.8f, 	1, 	"Gives you total control over grill heat!"),
				new Type("Nuclear", 		1000, 	15, 47, 0.75f, 	1, 	"Gives you total control over grill heat!"),
		};
		// name, initial cost, daily cost, description
		
		public TextureRegion left;
		public TextureRegion center;
		public TextureRegion right;
		public Animation fire;
		public float cookSpeedFactor;
		public float burnSpeedFactor;
		
		// for Kryo
		private Type() {}
		
		private Type(String name, float cash, int coins, int unlockAtLevel, float cookSpeed, float burnSpeed, String description) {
			super(name, cash, coins, unlockAtLevel, description, "");
			left = Assets.getTextureRegion("grill/" + getPrefix(name) + "_left");
			center = Assets.getTextureRegion("grill/" + getPrefix(name) + "_center");
			right = Assets.getTextureRegion("grill/" + getPrefix(name) + "_right");
			fire = Assets.createAnimation("grill/"  + getPrefix(name) + "_fire", Assets.GRILL_ANIMATION_TIME, 3, true);
			
			this.cookSpeedFactor = cookSpeed;
			this.burnSpeedFactor = burnSpeed;

//			fire = Assets.getTextureRegion(name.toLowerCase() + "_fire");
		}
		
		private static String getPrefix(String in) {
			String name = in.toLowerCase();
			if (name.equals("gas")) {
				name = "charcoal";
			}
			if (name.equals("gas warmer")) {
				name = "charcoal";
			}
			if (name.equals("electric")) {
				name = "charcoal";
			}
			if (name.equals("electric warmer")) {
				name = "charcoal";
			}
			if (name.equals("nuclear")) {
				name = "charcoal";
			}
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
}

