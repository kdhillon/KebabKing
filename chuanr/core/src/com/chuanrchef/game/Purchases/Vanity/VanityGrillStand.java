package com.chuanrchef.game.Purchases.Vanity;

import com.badlogic.gdx.utils.Array;
import com.chuanrchef.game.Purchases.Purchaseable;

public class VanityGrillStand extends VanityItem {
	public static Array<VanityGrillStand> stands;
	
	public VanityGrillStand(String name, int priceInCoins, String description, String region) {
		super(name, priceInCoins, description, region);
	}
	
	public static void initialize() {
		stands = new Array<VanityGrillStand>(false, 6, Purchaseable.class);
		// add all possible stands
		stands.add(new VanityGrillStand("None", 0, "", ""));
		stands.add(new VanityGrillStand("Bicycle", 10, "", "whitepixel"));
		stands.add(new VanityGrillStand("Motorcycle", 20, "", "whitepixel"));
		stands.add(new VanityGrillStand("Folding Table", 30, "", "whitepixel"));
		stands.add(new VanityGrillStand("Wooden Table", 40, "", "whitepixel"));
		stands.add(new VanityGrillStand("Metal Stand", 50, "", "whitepixel"));
	}
}
