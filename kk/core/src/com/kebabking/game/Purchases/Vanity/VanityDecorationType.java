//package com.kebabking.game.Purchases.Vanity;
//
//import com.kebabking.game.ProfileInventory;
//import com.kebabking.game.Purchases.PurchaseType;
//
//public class VanityDecorationType extends PurchaseType {
//	static String description =
//			"Upgrade the quality of your meat! Better meat costs more, but sells for "
//					+ "more and increases your reputation!. Some customers will only accept"
//					+ " high quality meat! ";
//	
//	// TODO add a boolean to purchasetype that makes it so you can toggle all items, instead of having "current"
//	public VanityDecorationType(ProfileInventory inventory) {
//		super(inventory, "Decorations", description, null, VanityDecoration.decorations.items);
//		this.allowMultipleSelect();
//		// allow null purchaseable to be current.
//	}
//	
//	// for Kyro
//	public VanityDecorationType(){
//		super("Decorations", description, null, VanityDecoration.decorations.items);
//	};
//}