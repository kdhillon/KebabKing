package com.chuanrchef.game.Purchases.Vanity;

import com.chuanrchef.game.Purchases.Inventory;
import com.chuanrchef.game.Purchases.PurchaseType;

public class VanityGrillStandType extends PurchaseType {
	static String description =
			"Upgrade the quality of your meat! Better meat costs more, but sells for "
					+ "more and increases your reputation!. Some customers will only accept"
					+ " high quality meat! ";
	
	// TODO add a boolean to purchasetype that makes it so you can toggle all items, instead of having "current"
	public VanityGrillStandType(Inventory inventory) {
		super(inventory, "Grill Stand", description, null, VanityGrillStand.stands.items);
		// allow null purchaseable to be current.
		this.unlock(VanityGrillStand.stands.first());
	}
	
	// for Kyro
	public VanityGrillStandType(){};

}