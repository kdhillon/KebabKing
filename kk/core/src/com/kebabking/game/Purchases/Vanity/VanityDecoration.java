//package com.kebabking.game.Purchases.Vanity;
//
//import com.badlogic.gdx.utils.Array;
//import com.kebabking.game.Purchases.PurchaseType;
//import com.kebabking.game.Purchases.Purchaseable;
//
//public class VanityDecoration extends PurchaseType {
//	public static Array<Purchaseable> decorations;
//	
//	// as fraction of screen
//	public float x;
//	public float y;
//	public float width;
//	public float height;
//	
//	// kryo
//	public VanityDecoration() {
//		super("", 0, "", "");
//	}
//	
//	public VanityDecoration(String name, int priceInCoins, float locationX, float locationY, float width,
//			float height, String description, String region) {
//		super(name, priceInCoins, description, region);
//		this.x = locationX;
//		this.y = locationY;
//		this.width = width;
//		this.height = height;
//	}
//
//	public static void initialize() {
//		decorations = new Array<Purchaseable>(false, 7, Purchaseable.class);
//		// create all possible vanity decorations, like an enum
//		decorations.add(new VanityDecoration("Flowers", 10, 0, .3f, 1, 0.05f, "", "whitepixel"));
//		decorations.add(new VanityDecoration("Radio", 10, 0, 0, 0, 0,"", ""));
//		decorations.add(new VanityDecoration("Red Carpet", 10, 0, 0, 0, 0, "", ""));
//		decorations.add(new VanityDecoration("Lanterns", 10, 0, 0, 0, 0, "", ""));
//		decorations.add(new VanityDecoration("Lights", 10, 0, 0, 0, 0, "", ""));
//		decorations.add(new VanityDecoration("Chairs", 10, 0, 0, 0, 0, "", ""));
//		decorations.add(new VanityDecoration("Table", 10, 0, 0, 0, 0, "", ""));
//}
//}
