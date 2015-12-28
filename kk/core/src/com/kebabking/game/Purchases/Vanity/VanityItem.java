package com.kebabking.game.Purchases.Vanity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kebabking.game.Assets;
import com.kebabking.game.Purchases.Purchaseable;

// Represents an item that doesn't do anything, but changes appearance
// there are different types of vanity items:
//		grill stand
// 		customer skins
//		customer hats
// 		spice box skins
//		music (radio)
//		ui colors
//		
// 		decorations (generic and can have infinite)
public class VanityItem implements Purchaseable {	
	String name;
	int priceInCoins;
	String description;
	transient TextureRegion region;
	String regionName;
		
	// for Kryo
	public VanityItem() {
		if (regionName != null) 
			region = Assets.getTextureRegion(regionName);
	}
	
	public VanityItem(String name, int priceInCoins, String description, String region) {
		this.name = name;
		this.priceInCoins = priceInCoins;
		this.description = description;
		this.regionName = region;
		
		if (region != null && !region.isEmpty())
			this.region = Assets.getTextureRegion(region);
	}
	
	public static void initialize() {
		VanityDecoration.initialize();
		VanityGrillStand.initialize();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int coinsToUnlock() {
		return priceInCoins;
	}

	@Override
	public float cashToUnlock() {
		return 0;
	}

	@Override
	public int unlockAtLevel() {
		return 1;
	}

	@Override
	public float getDailyCost() {
		return 0;
	}

	@Override
	public TextureRegion getIcon() {
		return region;
	}

	@Override
	public String getDescription() {
		return description;
	}
	
	public TextureRegion getTexture() {
		return region;
	}
	
//	// should be implemented by inheriting classes
//	public void draw() {
//		System.out.println("You shouldn't call this draw function in VanityItem");
//	}

}
