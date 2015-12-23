package com.kebabking.game;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.kebabking.game.OnlinePurchaseManager.PurchaseableOnline;

public class CoinsTable extends Table {
	KebabKing master;
	
	// create a table in which people can buy coins 
	public CoinsTable(KebabKing master) {
		super();
		System.out.println("RUNNING");
		this.master = master;
		Label title = new Label("Coins!", Assets.getStoreTitleLS());
		title.setAlignment(Align.center);
		this.add(title).height(KebabKing.getGlobalY(StoreScreen.TitleHeight / StoreScreen.UNITS_HEIGHT));
		this.row();
		for (PurchaseableOnline op : OnlinePurchaseManager.PurchaseableOnline.values()) {
			this.add(newTableEntry(op));
			this.row();
		}
	}
	
	private Table newTableEntry(PurchaseableOnline op) {
		Table entry = new Table();
		
		Label title = new Label("" + op.coins + " Coins!", Assets.generateLabelStyleUIChinaWhite(30));
		
		entry.add(title);
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
	
//	private void buy(PurchaseableOnline op) {
//		master.makePurchase(op);
//	}
}
