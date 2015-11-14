package com.chuanrchef.game.Purchases;

import com.badlogic.gdx.graphics.g2d.TextureRegion;


// user has one of these
// later: split this into separate ones for chicken beef, lamb
public class MeatQuality extends PurchaseType {
	// Specific types that you might own 
	enum Quality implements Purchaseable{
		// name, initial cost, daily cost, qual factor, description
		LEVEL1 ("Grade B Meat", 	0,	 0,	 1, 	"Pretty crappy meat!"), 
		LEVEL2 ("Grade A Meat", 	150, 5, 1.1f,	 "Not too great; probably comes from animals!"),
		LEVEL3 ("Grade AA Meat", 	500, 10, 1.2f,	 "A bit better; still tastes kinda fishy."), 
		LEVEL4 ("Grade AAA Meat",	1000, 25, 1.3f,  "Almost restaurant quality!"),
		LEVEL5 ("Top Quality Meat", 2000, 40, 1.4f,	 "You can't find better!");

		String name; 
		float initialCost;
		float dailyCost;
		String description;
		float qualityFactor;

		private Quality(String name, float initialCost, float dailyCost, float qualityFactor, String description) {
			this.name = name;
			this.initialCost = initialCost;
			this.dailyCost = dailyCost;
			this.qualityFactor = qualityFactor;
			this.description = description;
		}
		@Override
		public String getName() {
			return this.name;
		}
		@Override
		public int coinsToUnlock() {
			return 0;
		}
		@Override
		public float cashToUnlock() {
			return this.initialCost;
		}
		@Override
		public float getDailyCost() {
			return this.dailyCost;
		}
		@Override
		public TextureRegion getIcon() {
			return null;
		}
		@Override
		public String getDescription() {
			return this.description;
		}
		@Override
		public int unlockAtRound() {
			return 0;
		}
	};
	
	static String meatQualityDescription =
			"Upgrade the quality of your meat! Better meat costs more, but sells for "
					+ "more and increases your reputation!. Some customers will only accept"
					+ " high quality meat! ";

	public MeatQuality(Inventory inventory) {
		super(inventory, "Meat Quality", meatQualityDescription, null, Quality.values());
		unlock(Quality.LEVEL1);
	}
	
	// for Kyro
	public MeatQuality(){};
}
