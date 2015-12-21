package com.kebabking.game;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kebabking.game.Purchases.PurchaseType;
import com.kebabking.game.Purchases.Purchaseable;

public class StoreScrollButton extends Button {
	Table scroll;
	PurchaseType type;
	Purchaseable current;
	public StoreScrollButton other;
	
	public StoreScrollButton(Table scroll, PurchaseType type, Purchaseable current) {
		super();
		this.scroll = scroll;
		this.type = type;
		this.current = current;
	}
}
