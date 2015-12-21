package com.kebabking.game.Purchases.Vanity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.kebabking.game.Assets;
import com.kebabking.game.Purchases.Purchaseable;

public class VanityGrillStand extends VanityItem {
	public static Array<VanityGrillStand> stands;
	public TextureRegion front;
	public TextureRegion back;
	
	public VanityGrillStand(String name, int priceInCoins, String description, String front, String back) {
		super(name, priceInCoins, description, "grill/"+front);
		this.front = Assets.getTextureRegion("grill/"+front);
		this.back = Assets.getTextureRegion("grill/"+back);
	}
	
	public static void initialize() {
		stands = new Array<VanityGrillStand>(false, 6, Purchaseable.class);
		// add all possible stands
		stands.add(new VanityGrillStand("None", 0, "", "legs_front", "legs_back"));
		stands.add(new VanityGrillStand("Bicycle", 10, "", "legs_front", "legs_back"));
		stands.add(new VanityGrillStand("Motorcycle", 20, "", "legs_front", "legs_back"));
		stands.add(new VanityGrillStand("Folding Table", 30, "", "legs_front", "legs_back"));
		stands.add(new VanityGrillStand("Wooden Table", 40, "", "legs_front", "legs_back"));
		stands.add(new VanityGrillStand("Metal Stand", 50, "", "legs_front", "legs_back"));
	}
}
