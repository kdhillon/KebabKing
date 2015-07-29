package com.chuanrchef.game;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.chuanrchef.game.OnlinePurchaseManager.PurchaseableOnline;

public class CoinsButton extends TextButton{
	PurchaseableOnline op;
	
	public CoinsButton(String name, TextButtonStyle tbs, PurchaseableOnline op) {
		super(name, tbs);
		this.op = op;
	}

}
