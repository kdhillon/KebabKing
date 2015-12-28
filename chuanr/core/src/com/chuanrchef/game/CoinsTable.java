package com.chuanrchef.game;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.chuanrchef.game.OnlinePurchaseManager.PurchaseableOnline;

public class CoinsTable extends Table {
	ChuanrC master;
	
	// create a table in which people can buy coins 
	public CoinsTable(ChuanrC master) {
		super();
		this.master = master;
		Label title = new Label("Coins!", Assets.getStoreTitleLS());
		title.setAlignment(Align.center);
		this.add(title).height(ChuanrC.getGlobalY(StoreScreen.TitleHeight / StoreScreen.UNITS_HEIGHT));
		this.row();
		for (PurchaseableOnline op : OnlinePurchaseManager.PurchaseableOnline.values()) {
			this.add(newTableEntry(op));
			this.row();
		}
	}
	
	
	private Table newTableEntry(PurchaseableOnline op) {
		Table entry = new Table();
		
//		Label title = new Label("" + op.coins + " Coins!", Assets.generateLabelStyle(30, true));
		
//		entry.add(title);
		entry.row();
		
//		CoinsButton buy = new CoinsButton("Get Coins!", Assets.getBackButtonStyle(), op);
//		buy.getLabel().setTouchable(Touchable.disabled);
//		buy.addListener(new InputListener() {
//			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
//				return true;
//			}
//			public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
//				PurchaseableOnline coinOp = ((CoinsButton) event.getTarget()).op;
//				buy(coinOp);
//			}
//		});
//		
//		entry.add(buy);
		return entry;
	}
	
	private void buy(PurchaseableOnline op) {
		master.makePurchase(op);
	}
}
