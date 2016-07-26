package com.kebabking.game.Managers;

import com.kebabking.game.KebabKing;
import com.kebabking.game.OnlinePurchaseHandler;

public class IABManagerMock implements IABManager {

	public  IABManagerMock() {
		KebabKing.print("initializing iab manager mock");
	}

	@Override
	public void makePurchase(String productID) {
		// TODO Auto-generated method stub
		KebabKing.print("Iab mock purchasing " + productID);
		OnlinePurchaseHandler.handlePurchaseSuccess(productID);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void checkConsumables() {
		KebabKing.print("checking purchased consumables");
	}

}
