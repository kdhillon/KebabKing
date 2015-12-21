package com.kebabking.game.Purchases;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kebabking.game.Assets;

public class SimplePurchaseable implements Purchaseable {
	String name; 
	int coinsToUnlock;
	float cashToUnlock;	
	float dailyCost;
	int unlockAtLevel;
	String description;
	TextureRegion icon;
	
	public SimplePurchaseable(String name, float cashToUnlock, int unlockAtLevel, String description, String iconFull) {
		this(name, cashToUnlock, 0, 0.0f, unlockAtLevel, description, iconFull);
	}
	
	public SimplePurchaseable(String name, float cashToUnlock, int coinsToUnlock, float dailyCost, int unlockAtLevel, String description, String iconFull) {
		this.name = name;
		this.coinsToUnlock = coinsToUnlock;
		this.cashToUnlock = cashToUnlock;
		this.dailyCost = dailyCost;
		this.description = description;
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

	
	
}