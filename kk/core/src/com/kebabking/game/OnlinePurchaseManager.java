package com.kebabking.game;

// for managing online purchases
public class OnlinePurchaseManager {
	public static enum PurchaseableOnline {
		FIVE_COINS("five_coins", 5, 1.99),
		TEN_COINS("ten_coins", 10, 2.99),
		TWENTY_COINS("twenty_coins", 20, 4.99),
		FIFTY_COINS("fifty_coins", 50, 9.99),
		ONE_HUNDRED_COINS("one_hundred_coins", 100, 14.99);
		
		public String productID;
		public int coins;
		public double price;
		
		private PurchaseableOnline(String productID, int coins, double price) {
			this.productID = productID;
			this.coins = coins;
			this.price = price;
		}
	}
	
	public static PurchaseableOnline getPurchaseableForID(String purchaseID) {
		PurchaseableOnline[] array = PurchaseableOnline.values();
		for (PurchaseableOnline p : array) {
			if (p.productID.equals(purchaseID)) {
				return p;
			}
		}
			
		System.out.println("PURCHASE ERROR: PURCHASEID NOT FOUND");
		return null;
	}
}
