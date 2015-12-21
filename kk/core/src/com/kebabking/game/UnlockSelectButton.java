package com.kebabking.game;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.kebabking.game.Purchases.Inventory;
import com.kebabking.game.Purchases.PurchaseType;
import com.kebabking.game.Purchases.Purchaseable;

public class UnlockSelectButton extends Button {
	PurchaseType type;
	Purchaseable purchaseable;
	Inventory inventory;
	
	public UnlockSelectButton(PurchaseType type, Purchaseable purchaseable, Inventory inventory) {
		super(new ButtonStyle());
		this.type = type;
		this.purchaseable = purchaseable;
		this.inventory = inventory;
	}
}
