package com.kebabking.game.Purchases;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kebabking.game.Assets;
import com.kebabking.game.ProfileInventory;

public class MeatTypes extends PurchaseType {
	static String name = "MEAT TYPES";
	static String desc = "Choose your meat type!";
	static int MAX_SELECTABLE = 3;
	
	public static class Type extends SimplePurchaseable {
		public static final Type[] values = new Type[] {
				//			name		buy		sell	cook burn	double	veg
				new Type ("Beef", 		2f, 	3.5f,  	8, 	12, 	false, false),
				new Type ("Lamb", 		1.5f, 	3f,  	7, 	10, 	false, false),
				new Type ("Chicken",	 	2.5f, 	4f,  	5, 	8,  	true, false),
				new Type ("Squid", 		2f, 	3.5f,  	8, 	12, 	false, false),
				new Type ("Fish", 	2.5f, 	4f,  	5, 	8,  	true, false),
//				new Type ("Lamb", 		2f, 	3.5f,  	8, 	12, 	false, false),
		};
		
		public Assets.KebabTextures textures;

		public float sellPrice;
		public float buyPrice;
		
		public float cookTime; // base time to cook
		public float burnTime; // base time to burn after cooked 

		public boolean doubleWidth; // only chicken should be double width
		public boolean vegetarian;
		
		public TextureRegion coolerOpen;
		public TextureRegion coolerClosed;
		
		public TextureRegion icon;

		public Type(String name, float buyPrice, float sellPrice, float cookTime, float burnTime, boolean doubleWidth, boolean vegetarian) {
			this.name = name;
			this.textures = Assets.generateKebabTextures("kebabs/" + name + "Kebab");
			
			this.buyPrice = buyPrice;
			this.sellPrice = sellPrice;
			this.cookTime = cookTime;
			this.burnTime = burnTime;
			this.doubleWidth = doubleWidth;
			this.vegetarian = vegetarian;
			
			coolerOpen = Assets.getTextureRegion("kebabs/" + name + "Open");
			coolerClosed = Assets.getTextureRegion("kebabs/" + name + "Closed");
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
	
	@Override
	public int addToSelected(int index) {
		int toReturn = super.addToSelected(index);
		if (inventory.profile != null && inventory.profile.master != null && inventory.profile.master.grill != null)
			inventory.profile.master.grill.updateBoxes();
		return toReturn;
	}
}
