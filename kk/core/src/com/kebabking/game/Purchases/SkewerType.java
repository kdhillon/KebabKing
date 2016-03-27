package com.kebabking.game.Purchases;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kebabking.game.Assets;
import com.kebabking.game.ProfileInventory;

// this class contains the user's current grill Size
public class SkewerType extends PurchaseType {	
	static String name = "skewers";
	static String desc = "skewers_desc";

	// Specific Sizes that you might own 
	// Using a static class allows us to extend simplepurchaseable, but still basically be an enum.
	static class Type extends SimplePurchaseable {
		public static final Type[] values = new Type[]{
				new Type("bamboo", 		0,	 0,	  0,	1,	1.0f),
				new Type("wood", 		400,  10,  10,	4,	1.1f), 
				new Type("iron", 		2000,  15, 26, 	9,	1.2f),
				new Type("steel", 		5000,  20, 36, 	12,	1.3f),
				new Type("titanium", 	15000,  25, 46,	16,	1.4f),
		};
	
		float satBoost;
		TextureRegion stick;
		
		private Type() {};
		
		private Type(String name, float cash, int coins, int unlockAtLevel, int unlockWithLocation, float satBoost) {
			super(SkewerType.name + "_" + name, cash, coins, unlockAtLevel, unlockWithLocation, "kebabs/" + name);
			this.icon = Assets.getTextureRegion("market/icons/skewers_" + name + "_icon");
			this.stick = Assets.getTextureRegion("kebabs/" + name);
			this.satBoost = satBoost;
		}
	};
	
	public SkewerType(ProfileInventory inventory) {
		super(inventory, name, desc, Type.values);
		unlock(values[0]);
	}
	
	// for kryo, make sure to initialize
	public SkewerType() {
		super(name, desc, Type.values);
	};
	
	public float getSatBoost() {
		return ((SkewerType.Type) this.getFirstSelected()).satBoost;
	}
	
	public TextureRegion getStick() {
		return ((SkewerType.Type) this.getFirstSelected()).stick;
	}
}
