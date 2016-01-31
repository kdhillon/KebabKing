package com.kebabking.game.Purchases;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kebabking.game.Assets;

public class SimplePurchaseable implements Purchaseable {
	String name; 
	protected int coinsToUnlock;
	protected float cashToUnlock;	
	float dailyCost;
	int unlockAtLevel;
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
	
	public SimplePurchaseable(String name, float cashToUnlock, int coinsToUnlock, int unlockAtLevel, String description, String iconFull) {
		this(name, cashToUnlock, coinsToUnlock, 0.0f, unlockAtLevel, description, iconFull);
	}
	
	public SimplePurchaseable(String name, float cashToUnlock, int coinsToUnlock, float dailyCost, int unlockAtLevel, String description, String iconFull) {
//		this.type = type;
		// TYPE MUST BE SET IN PurchaseType.setValues();
		this.name = name;
		this.coinsToUnlock = coinsToUnlock;
		this.cashToUnlock = cashToUnlock;
		this.dailyCost = dailyCost;
		this.description = description;
		this.textureName = iconFull;
		this.unlockAtLevel = unlockAtLevel;
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

	@Override
	public int unlockAtLevel() {
		return unlockAtLevel;
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
