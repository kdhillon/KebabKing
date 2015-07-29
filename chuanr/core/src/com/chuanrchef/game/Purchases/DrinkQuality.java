package com.chuanrchef.game.Purchases;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.chuanrchef.game.Assets;


// user has one of these
// later: split this into separate ones for chicken beef, lamb
public class DrinkQuality implements PurchaseType {
	static String meatQualityDescription =
			"Upgrade the quality of your drinks!";

	// Specific types that you might own 
	enum Quality implements Purchaseable{
		// name, initial cost, daily cost, qual factor, description
		LEVEL1 ("Bub Light", 	0,	 0,	 1, "beer_icon",	"The classic"), 
		LEVEL2 ("Heimikan", 	150, 5, 1.1f, "beer_icon",	 "Imported! Wow!"),
		LEVEL3 ("Rolling Log", 	500, 10, 1.2f, "beer_icon",	 "Goes down easy"), 
		LEVEL4 ("Sandshark",	1000, 25, 1.3f, "beer_icon",  "Everybody loves Sandshark"),
		LEVEL5 ("Catfish Ale", 2000, 40, 1.4f, "beer_icon",	 "The best of the best.");

		String name; 
		float initialCost;
		float dailyCost;
		String description;
		float qualityFactor;
		TextureRegion icon;

		private Quality(String name, float initialCost, float dailyCost, float qualityFactor, String iconName, String description) {
			this.name = name;
			this.initialCost = initialCost;
			this.dailyCost = dailyCost;
			this.qualityFactor = qualityFactor;
			this.description = description;
			this.icon = Assets.getTexture(iconName);
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
			return icon;
		}
		@Override
		public String getDescription() {
			return this.description;
		}
	};

	ArrayList<Quality> unlocked;
	Quality currentQuality;
	
	// create a new one
	public DrinkQuality() {
		unlocked = new ArrayList<Quality>();
		unlock(Quality.LEVEL1);
	}
	
	@Override
	public String getName() {
		return "Drink Quality";
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
