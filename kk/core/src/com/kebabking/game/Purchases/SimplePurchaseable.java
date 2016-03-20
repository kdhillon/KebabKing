package com.kebabking.game.Purchases;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kebabking.game.Assets;

public class SimplePurchaseable implements Purchaseable {
	String name; 
	protected int coinsToUnlock;
	protected float cashToUnlock;	
	float dailyCost;
	int unlockAtLevel;
	int unlockWithLocation;
	String description;
	transient TextureRegion icon;
	String textureName;
	PurchaseType type;
	
	// for Kryo
	public SimplePurchaseable() {
		if (textureName != null && textureName != "") {
			this.icon = Assets.getTextureRegion(textureName);
		}
	}
	public SimplePurchaseable(String name, float cashToUnlock, int coinsToUnlock, int unlockAtLevel, int unlockWithLocation, String iconFull) {
		this(Assets.strings.get(name), cashToUnlock, coinsToUnlock, 0.0f, unlockAtLevel, unlockWithLocation, Assets.strings.get(name+"_d"), iconFull);
	}
	
	public SimplePurchaseable(String name, float cashToUnlock, int coinsToUnlock, int unlockAtLevel, int unlockWithLocation, String description, String iconFull) {
		this(name, cashToUnlock, coinsToUnlock, 0.0f, unlockAtLevel, unlockWithLocation, description, iconFull);
	}
	
	public SimplePurchaseable(String name, float cashToUnlock, int coinsToUnlock, float dailyCost, int unlockAtLevel, int unlockWithLocation, String description, String iconFull) {
//		this.type = type;
		// TYPE MUST BE SET IN PurchaseType.setValues();
		this.name = name;
		this.coinsToUnlock = coinsToUnlock;
		this.cashToUnlock = cashToUnlock;
		this.dailyCost = dailyCost;
		this.description = description;
		this.textureName = iconFull;
		this.unlockAtLevel = unlockAtLevel;
		this.unlockWithLocation = unlockWithLocation;
		System.out.println("setting unlockwithlocation to " + this.unlockWithLocation + " for " + name);
		if (iconFull != null && !iconFull.equals(""))
			this.icon = Assets.getTextureRegion(iconFull);
		else this.icon = null;	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public int coinsToUnlock() {
		return coinsToUnlock;
	}

	@Override
	public float cashToUnlock() {
		return cashToUnlock;
	}

	// this applies for all except location, of course
	@Override
	public int unlockAtLevel() {
		if (unlockWithLocation <= 0) System.out.println(getName());
		if (LocationType.UNLOCKS_ONLY_WITH_LOCATIONS)
			return LocationType.getLocationAt(unlockWithLocation).unlockAtLevel;
		else return unlockAtLevel;
	}
	
	@Override
	public int unlockWithLocation() {
		return unlockWithLocation;
	}

	@Override
	public float getDailyCost() {
		return dailyCost;
	}

	@Override
	public TextureRegion getIcon() {
		return icon;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public PurchaseType getType() {
		return type;
	}
	
	@Override
	public void setType(PurchaseType type) {
		this.type = type;
	}
}
