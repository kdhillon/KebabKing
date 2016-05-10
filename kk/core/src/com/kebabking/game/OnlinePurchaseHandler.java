package com.kebabking.game;

import com.kebabking.game.Managers.Manager;

// for managing online purchases
public class OnlinePurchaseHandler {

	public static class PurchaseableOnline {
		public static final PurchaseableOnline[] values = new PurchaseableOnline[] {
				new PurchaseableOnline("PEASANT PACK", "peasant_pack", 10, 0, 0.99f),
				new PurchaseableOnline("VENDOR PACK", "vendor_pack", 25, 300, 1.99f),
				new PurchaseableOnline("MERCHANT PACK", "merchant_pack", 50, 500, 2.99f),
				new PurchaseableOnline("TYCOON PACK", "tycoon_pack", 400, 1000, 9.99f),
				new PurchaseableOnline("KEBAB KING PACK", "kebab_king_pack", 1000, 2500, 19.99f),
		};

		public String name;
		public String productID;
		public int jade;
		public float cash;
		public float price;

		private PurchaseableOnline(String name, String productID, int coins, float cash, float price) {
			this.name = Assets.strings.get(productID);
			this.productID = productID;
			this.jade = coins;
			this.cash = cash;
			this.price = price;
		}
	}

	static KebabKing master;

	public static void init(KebabKing masterIn) {
		master = masterIn;
		
		// attempt to consume any purchases
	}
	
	public static void attemptPurchase(PurchaseableOnline op) {
		Manager.iab.makePurchase(op.productID);
	}

	public static void handlePurchaseSuccess(String purchaseID) {
		PurchaseableOnline purchased = getPurchaseableForID(purchaseID);
		System.out.println("Successfully purchased " + purchaseID + " for " + purchased.price + " giving you " + purchased.cash + " cash and " + purchased.jade + " coins");
		master.profile.giveCoins(purchased.jade);
		master.profile.giveMoney(purchased.cash);
		
		DrawUI.createProjectiles(purchased.jade, JewelerTable.playLocX, JewelerTable.playLocY, true);
		DrawUI.createProjectiles((int) (purchased.cash), JewelerTable.playLocX, JewelerTable.playLocY, false);
		
		// save after purchase success
		master.save();
	}

	public static PurchaseableOnline getPurchaseableForID(String purchaseID) {
		for (PurchaseableOnline p : PurchaseableOnline.values) {
			if (p.productID.equals(purchaseID)) {
				return p;
			}
		}
			
		System.out.println("PURCHASE ERROR: PURCHASEID NOT FOUND");
		return null;
	}
}
