package com.chuanrchef.game.Purchases;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.chuanrchef.game.Assets;


// user has one of these
// later: split this into separate ones for chicken beef, lamb
public class AdCampaign implements PurchaseType {
	static String meatQualityDescription =
			"Launch an advertising campaign!";

	// Specific types that you might own 
	enum Campaign implements Purchaseable{
		// name, initial cost, daily cost, qual factor, description
		LEVEL1 ("Poster Ads", 	0,	 0,	 1, "icon_ads",	"Advertise with posters!"), 
		LEVEL2 ("Newspaper Ads", 	150, 5, 1.1f, "icon_ads",	 "Get in print!"),
		LEVEL3 ("Online Ads", 	500, 10, 1.2f, "icon_ads",	 "Technology!"), 
		LEVEL4 ("Radio Ads",	1000, 25, 1.3f, "icon_ads",  "Kebab FM!"),
		LEVEL5 ("TV Ads",		 2000, 40, 1.4f, "icon_ads",	 "Billy Mays here!");

		String name; 
		float initialCost;
		float dailyCost;
		String description;
		float qualityFactor;
		TextureRegion icon;

		private Campaign(String name, float initialCost, float dailyCost, float qualityFactor, String iconName, String description) {
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

	ArrayList<Campaign> unlocked;
	Campaign currentQuality;
	
	// create a new one
	public AdCampaign() {
		unlocked = new ArrayList<Campaign>();
		unlock(Campaign.LEVEL1);
	}
	
	@Override
	public String getName() {
		return "Ad Campaigns";
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
		this.currentQuality = (Campaign) newCurrent;
	}

	@Override
	public void unlock(Purchaseable toUnlock) {
		this.unlocked.add((Campaign) toUnlock);
		this.setCurrent((Campaign) toUnlock);
	}
	
	@Override
	public Purchaseable getNext(Purchaseable current, boolean left) {
		Campaign[] values = Campaign.values();
		
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
