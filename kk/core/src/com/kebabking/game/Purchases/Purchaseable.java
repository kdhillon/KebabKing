package com.kebabking.game.Purchases;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

// represents a particular item that a user can buy and own.
public interface Purchaseable {
	public String getName();
	
	public int coinsToUnlock();
	
	public float cashToUnlock();
	
	public int unlockAtLevel();

	public int unlockWithLocation();
	
	public float getDailyCost();
	
	public TextureRegion getIcon();
	
	public String getDescription();
	
	public PurchaseType getType();
	
	public void setType(PurchaseType type);
}
