package com.chuanrchef.game.Purchases;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.TextureRegion;


// user has one of these
// later: split this into separate ones for chicken beef, lamb
public class MeatQuality implements PurchaseType {
	static String meatQualityDescription =
			"Upgrade the quality of your meat! Better meat costs more, but sells for "
					+ "more and increases your reputation!. Some customers will only accept"
					+ " high quality meat! ";

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
	};

	ArrayList<Quality> unlocked;
	Quality currentQuality;
	
	// create a new one
	public MeatQuality() {
		unlocked = new ArrayList<Quality>();
		unlock(Quality.LEVEL1);
	}
	
	@Override
	public String getName() {
		return "Meat Quality";
	}

	@Override
	public String getDescription() {
		return meatQualityDescription;
	}

	@Override
	public TextureRegion getIcon() {
		return null;
	}

	@Override
	public Purchaseable getCurrentSelected() {
		return currentQuality;
	}

	@Override
	public boolean unlocked(Purchaseable purchaseable) {
		return unlocked.contains(purchaseable);
	}

	@Override
	public void setCurrent(Purchaseable newCurrent) {
		if (!unlocked(newCurrent)) throw new java.lang.AssertionError();
		this.currentQuality = (Quality) newCurrent;
	}

	@Override
	public void unlock(Purchaseable toUnlock) {
		this.unlocked.add((Quality) toUnlock);
		this.setCurrent((Quality) toUnlock);
	}
	
	@Override
	public Purchaseable getNext(Purchaseable current, boolean left) {
		Quality[] values = Quality.values();
		
		int currentIndex = -1;
		for (int i = 0; i < values.length; i++) {
			if (values[i] == current) currentIndex = i;
		}
		int nextIndex;
		
		if (left) {
			nextIndex = currentIndex - 1;
			if (nextIndex < 0) nextIndex = values.length - 1;
		}
		else {
			nextIndex = currentIndex + 1;
			if (nextIndex > values.length - 1) nextIndex = 0;
		}
		
		return values[nextIndex];
	}
}
