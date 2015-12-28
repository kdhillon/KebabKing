package com.kebabking.game.Purchases.Vanity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.kebabking.game.Assets;
import com.kebabking.game.Purchases.Purchaseable;

public class VanityGrillStand extends VanityItem {
	public transient static Array<VanityGrillStand> stands;
	public TextureRegion front;
	public TextureRegion back;
//	public String frontName;
//	public String backName;
	
	
	// for Kryo
//	public VanityGrillStand() {
//		if (frontName != null)
//			this.front = Assets.getTextureRegion("grill/"+frontName);
//		if (backName != null)
//			this.back = Assets.getTextureRegion("grill/"+backName);	
//	}
	
	public VanityGrillStand(String name, int priceInCoins, String description, String front, String back) {
		super(name, priceInCoins, description, "grill/"+front);
//		this.frontName = front;
//		this.backName = back;
		this.front = Assets.getTextureRegion("grill/"+front);
		this.back = Assets.getTextureRegion("grill/"+back);
	}
	
	public static void initialize() {
		stands = new Array<VanityGrillStand>(false, 5, Purchaseable.class);
		// add all possible stands
		stands.add(new VanityGrillStand("Metal Stand", 0, "", "legs_front", "legs_back"));
		stands.add(new VanityGrillStand("Folding Table", 100, "", "legs_front", "legs_back"));
		stands.add(new VanityGrillStand("Wooden Table", 200, "", "legs_front", "legs_back"));
		stands.add(new VanityGrillStand("Bicycle", 300, "", "legs_front", "legs_back"));
		stands.add(new VanityGrillStand("Motorcycle", 400, "", "legs_front", "legs_back"));
}
}
