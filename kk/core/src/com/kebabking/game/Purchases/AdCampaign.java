package com.kebabking.game.Purchases;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kebabking.game.Assets;
import com.kebabking.game.Customer;
import static com.kebabking.game.Customer.CustomerType.*;

// user has one of these
// later: split this into separate ones for chicken beef, lamb
public class AdCampaign extends PurchaseType {
	// Specific types that you might own 
	public enum Campaign implements Purchaseable{
		// name, 			unlock round	initial cost, daily cost, seconds, hype boost, customer?, 	icon,		description
		LEVEL0 ("Sign", 			1,		20,				0,	 		-1,		1, 			null,		"icon_ads",	"A nice kebab sign!"), 
		POSTER ("Poster Ads", 		1,		10, 			5,			60,		200f,		OLD_MAN, 	"icon_ads",	"Advertise with posters!"), 
		NEWSPAPER ("Newspaper Ads",	1,		10,				5,	 		120,	1.1f, 		OLD_WOMAN, 	"icon_ads",	 "Get in print!"),
		ONLINE ("Online Ads", 		1,		10, 			10, 		120,	1.2f,		null,		"icon_ads",	 "Technology!"), 
		LEVEL4 ("Radio Ads",		1,		30,				25, 		120,	1.3f, 		null,	 	"icon_ads",  "Kebab FM!"),
		LEVEL5 ("TV Ads",			1,		20, 			40, 		120,	1.4f,		null,		 "icon_ads",	 "Billy Mays here!"),
		PRESS_R ("Press Release",	1,		20, 			40, 		120,	1.4f,		BUSINESSMAN, "icon_ads",	 "Billy Mays here!"),
		PLANE ("Plane Banner",		25,		20, 			40, 		120,	1.4f,		null,		 "icon_ads",	 "Billy Mays here!"),
		SHIRTS ("T-Shirts",			25,		2000, 			40, 		120,	1.4f,		STUDENT,	 "icon_ads",	 "Billy Mays here!"),
		SELFIE ("Selfie Sticks",	25,		2000, 			40, 		120,	1.4f,		null,	 "icon_ads",	 "Billy Mays here!"),
		TOURIST ("Tourist Brochure",25,		2000, 			40, 		120,	1.4f,		FOREIGNER, "icon_ads",	 "Billy Mays here!"),
		TWO4ONE ("Two 4 One",		25,		2000, 			40, 		120,	1.7f,		null,	 "icon_ads",	 "Billy Mays here!"),
		;

		String name; 
		float initialCost;
		float dailyCost;
		String description;
		float hypeBoost; // aka hype meter boost
		Customer.CustomerType type;
		long seconds;
		TextureRegion icon;
		int roundToUnlock;
		int coinsToUnlock;

		private Campaign(String name, int roundToUnlock, float initialCost, float dailyCost, long seconds, float hypeBoost, Customer.CustomerType type, String iconName, String description) {
			this.name = name;
			this.initialCost = initialCost;
			this.dailyCost = dailyCost;
			this.hypeBoost = hypeBoost;
			this.seconds = seconds;
			this.description = description;
			this.icon = Assets.getTextureRegion("market/"+iconName);
			this.roundToUnlock = roundToUnlock;
			this.type = type;
			this.coinsToUnlock = 0;
		}
		
		@Override
		public String getName() {
			return this.name;
		}
		@Override
		public int coinsToUnlock() {
			return coinsToUnlock;
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
		@Override
		public int unlockAtLevel() {
			return this.roundToUnlock;
		}
	};
	
	// for Kryo
	public AdCampaign(){};
	
	public AdCampaign(Inventory inventory) {
		super(inventory, "Ads", "Ad campaigns", null, Campaign.values());
		unlock(Campaign.LEVEL0);
	}
}
