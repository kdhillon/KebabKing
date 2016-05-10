package com.kebabking.game.Purchases;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kebabking.game.Assets;
import com.kebabking.game.ProfileInventory;

public class MeatTypes extends PurchaseType {
	static String name = "kebab_types";
	static String desc = "kebab_types_desc";
	static int MAX_SELECTABLE = 3;
	
	public static class Type extends SimplePurchaseable {
		public static final Type[] values = new Type[] {
				//			name	unlk, cash, coins,	buy		sell	cook burn	double	veg
				new Type ("beef", 	1,	0,		0,	2f, 	4f,  	8, 	12, 	false, false),
				new Type ("lamb", 	1,	0,		0,	2f, 	4f,  	7, 	10, 	false, false),
				new Type ("chicken",1,	0,		0,	2f, 	4f,  	6, 	9,  	true, false),
				
				new Type ("squid", 	3,	500,	5,	2f, 	4f,  	7, 	12, 	false, false), // squid (or tofu) should unlock very early
				new Type ("fish", 	5,	1000,	10,	2f, 	4f,  	5, 	8,  	true, false),  // add these to the unlock waterfall
//				new Type ("pork", 		2f, 	3.5f,  	8, 	12, 	false, false),
//				new Type ("veggie", 		2f, 	3.5f,  	8, 	12, 	false, false),
//				new Type ("goat", 		2f, 	3.5f,  	8, 	12, 	false, false),
//				new Type ("corn", 		2f, 	3.5f,  	8, 	12, 	false, false),
//				new Type ("tofu", 		2f, 	3.5f,  	8, 	12, 	false, false),
//				new Type ("mushroom", 		2f, 	3.5f,  	8, 	12, 	false, false),
//				new Type ("shrimp", 		2f, 	3.5f,  	8, 	12, 	false, false),
//				new Type ("scallops", 		2f, 	3.5f,  	8, 	12, 	false, false),
//				new Type ("fishballs", 		2f, 	3.5f,  	8, 	12, 	false, false),
		};
		
		public Assets.KebabTextures textures;

		public float sellPrice;
		public float buyPrice;
		
		public float cookTime; // base time to cook
		public float burnTime; // base time to burn after cooked 

		public boolean doubleWidth; // only chicken should be double width
		public boolean vegetarian;
		
//		public TextureRegion coolerOpen;
//		public TextureRegion coolerClosed;
		
		public TextureRegion bigIcon;
		
		public TextureRegion icon;

		public Type(String name, int unlockWith, float cash, int coins, float buyPrice, float sellPrice, float cookTime, float burnTime, boolean doubleWidth, boolean vegetarian) {
			super(name, cash, coins, 1, unlockWith, "", "kebabs/" + name + "_cooked"); // remove the quotes to add description

			this.name = Assets.strings.get(name);
			
			this.buyPrice = buyPrice;
			this.sellPrice = sellPrice;
			this.cookTime = cookTime;
			this.burnTime = burnTime;
			this.doubleWidth = doubleWidth;
			this.vegetarian = vegetarian;
			
//			String tName = (name.charAt(0) + "").toUpperCase() + name.substring(1);
			this.textures = Assets.generateKebabTextures("kebabs/" + name);
//			coolerOpen = Assets.getTextureRegion("kebabs/" + tName + "Open");
//			coolerClosed = Assets.getTextureRegion("kebabs/" + tName + "Closed");
			bigIcon = Assets.getTextureRegion("kebabs/" + name + "IconBig");
			icon = Assets.getTextureRegion("kebabs/" + name + "Icon");
		}
	}

	public MeatTypes(ProfileInventory inventory) {
		super(inventory, name, desc, Type.values);
		unlock(Type.values[0]);

//		unlock(Type.values[1]);
//		unlock(Quality.values[0]);
		System.out.println("just initialized?");
	}
	
	// for kryo
	public MeatTypes() {
		super(name, desc, Type.values);
	};

	public int getMaxSelectable() {
		return MAX_SELECTABLE;
	}
	
	public int getMinSelectable() {
		return MAX_SELECTABLE;
	}
	
	@Override
	public int select(int index) {
		int toReturn = super.select(index);
		if (inventory.profile != null && inventory.profile.master != null && inventory.profile.master.grill != null)
			inventory.profile.master.grill.updateBoxes();
		return toReturn;
	}
}
