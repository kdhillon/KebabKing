package com.kebabking.game.Managers;

import com.kebabking.game.OnlinePurchaseManager;

public class IABManagerMock implements IABManager {

	public  IABManagerMock() {
		System.out.println("initializing iab manager mock");
	}

	@Override
	public void makePurchase(String productID) {
		// TODO Auto-generated method stub
		System.out.println("Iab mock purchasing " + productID);
		OnlinePurchaseManager.handlePurchaseSuccess(productID);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
	}

}
