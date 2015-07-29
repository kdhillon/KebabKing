package com.chuanrchef.game;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.chuanrchef.game.Purchases.PurchaseType;
import com.chuanrchef.game.Purchases.Purchaseable;

public class UnlockSelectButton extends TextButton {
	PurchaseType type;
	Purchaseable purchaseable;
	
	public UnlockSelectButton(TextButtonStyle tbs, PurchaseType type, Purchaseable purchaseable) {
		super("default", tbs);
		this.type = type;
		this.purchaseable = purchaseable;
	}
}
