package com.chuanrchef.game.Purchases;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public interface PurchaseType {

	public String getName();
	public String getDescription();
	public TextureRegion getIcon();
	public Purchaseable getCurrentSelected();
	public boolean unlocked(Purchaseable purchaseable);
	public void setCurrent(Purchaseable newCurrent);
	public void unlock(Purchaseable toUnlock);
	public Purchaseable getNext(Purchaseable current, boolean left);
}
