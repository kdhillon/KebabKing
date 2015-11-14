package com.chuanrchef.game.Purchases;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

// represents a particular item that a user can buy and own.
public interface Purchaseable {
	public String getName();
	
	public int coinsToUnlock();
	
	public float cashToUnlock();
	
	public int unlockAtRound();
	
	public float getDailyCost();
	
	public TextureRegion getIcon();
	
	public String getDescription();
}
