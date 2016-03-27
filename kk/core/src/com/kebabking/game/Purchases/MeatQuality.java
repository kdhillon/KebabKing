package com.kebabking.game.Purchases;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kebabking.game.Assets;
import com.kebabking.game.ProfileInventory;

//later: split this into separate ones for chicken beef, lamb
public class MeatQuality extends PurchaseType {
	static String name = "meat_quality";
	static String desc = "meat_quality_desc";
	
	// Specific types that you might own 
	public static class Quality extends SimplePurchaseable {
		public static final Quality[] values = new Quality[] {
//				// 		name, 	cash,  coins, unlock at, sellboost, qual factor, description		
				new Quality  ("1", 	0,	 0,		0, 	1,	0,		1), 
				new Quality  ("2", 	500, 2, 	8, 	3,	0.5f,	1.1f),
				new Quality  ("3", 	2000, 5, 	17, 5,	1f,		1.2f),
				new Quality  ("4", 	5000, 10, 	27, 9,	2f,		1.3f), 
				new Quality  ("5",	10000, 15, 	39, 13,	3f,		1.5f),
		};
		
//		float qualityFactor;
		float sellBoost; // added to sell price of kebabs
		TextureRegion coolerOpen;
		TextureRegion coolerClosed;

		// for kryo
		private Quality(){}

		private Quality(String name, float cash, int coins, int unlockAt, int unlockWithLocation, float sellBoost, float qualityFactor) {
			super(MeatQuality.name + "_" + name, cash, coins, unlockAt, unlockWithLocation, "kebabs/CoolerOpen" + name);			
//			this.qualityFactor = qualityFactor;
			this.sellBoost = sellBoost;
			
			this.coolerClosed = Assets.getTextureRegion("kebabs/CoolerClosed" + name);
			this.coolerOpen = Assets.getTextureRegion("kebabs/CoolerOpen" + name);
		}
	};

	public MeatQuality(ProfileInventory inventory) {
		super(inventory, name, desc, Quality.values);
		unlock(Quality.values[0]);
	}
	
	// for kryo
	public MeatQuality() {
		super(name, desc, Quality.values);
	};
	
	public float getSellBoost() {
		return ((Quality) this.getFirstSelected()).sellBoost;
	}
	
	public TextureRegion getCoolerOpen() {
		return ((Quality) this.getFirstSelected()).coolerOpen;
	}
	public TextureRegion getCoolerClosed() {
		return ((Quality) this.getFirstSelected()).coolerClosed;
	}
}

