package com.chuanrchef.game;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.chuanrchef.game.Purchases.Inventory;
import com.chuanrchef.game.Purchases.PurchaseType;
import com.chuanrchef.game.Purchases.Purchaseable;

public class UnlockSelectButton extends TextButton {
	PurchaseType type;
	Purchaseable purchaseable;
	Inventory inventory;
	
	public UnlockSelectButton(TextButtonStyle tbs, PurchaseType type, Purchaseable purchaseable, Inventory inventory) {
		super("default", tbs);
		this.type = type;
		this.purchaseable = purchaseable;
		this.inventory = inventory;
	}
}
