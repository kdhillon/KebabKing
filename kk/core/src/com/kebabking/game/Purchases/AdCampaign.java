package com.kebabking.game.Purchases;

import static com.kebabking.game.Customer.CustomerType.BUSINESSMAN;
import static com.kebabking.game.Customer.CustomerType.FAT_MAN;
import static com.kebabking.game.Customer.CustomerType.FOREIGNER;
import static com.kebabking.game.Customer.CustomerType.MAN;
import static com.kebabking.game.Customer.CustomerType.OLD_MAN;
import static com.kebabking.game.Customer.CustomerType.OLD_WOMAN;
import static com.kebabking.game.Customer.CustomerType.STUDENT;
import static com.kebabking.game.Customer.CustomerType.TOURIST;
import static com.kebabking.game.Customer.CustomerType.WOMAN;

import com.kebabking.game.Customer;

// user has one of these
// later: split this into separate ones for chicken beef, lamb
public class AdCampaign extends PurchaseType {
	static String name = "AD CAMPAIGN";
	static String desc = "Marketing campaigns can be the key to a successful kebab stand!";
	
	private static Customer.CustomerType all = null;
	
	private enum CampaignSpecial {
		NONE, TSHIRTS, SELFIE, TWO4ONE, PLANE
	}
	
	static class Campaign extends SimplePurchaseable {
		public static final Campaign[] values = new Campaign[] {
					// name,		round, 		jade, 	time, description
//		new Campaign ("Sign", 			1,		10,				0,	 		5,		1.5f, 		OLD_MAN,		"icon_ads",	"A nice kebab sign!"), 
		new Campaign ("Poster Ads", 	4,		1, 		120,	1.25f,		all, 		"icon_ads",	"Advertise with posters!"),
		new Campaign ("Newspaper Ads",	5,		3,	 	160,	1.5f, 		new Customer.CustomerType[] {OLD_MAN, BUSINESSMAN}, 	"icon_ads",	 "Get in print!"), 
		new Campaign ("Radio Ads",		12,		3,		180,	1.5f, 		new Customer.CustomerType[] {TOURIST, OLD_MAN, OLD_WOMAN},	 	"icon_ads",  "Kebab FM!"),
		new Campaign ("Online Ads", 	18,		4,		200,	1.5f,		all,		"icon_ads",	 "Technology!"),
		new Campaign ("Press Release",	19,		5,		200,	2.0f,		new Customer.CustomerType[] {STUDENT, FOREIGNER, TOURIST, MAN, WOMAN}, "icon_ads",	 "Billy Mays here!"),
		new Campaign ("TV Ads",			20,		6,		240,	2.4f,		all,		 "icon_ads",	 "Billy Mays here!"),
		new Campaign ("T-Shirts",		22,		7,		300,	3.4f,		STUDENT,	 "icon_ads",	 "Billy Mays here!"),
		new Campaign ("Tourist Brochure",30,	8,		300,	2.5f,		new Customer.CustomerType[] {FOREIGNER, FAT_MAN, TOURIST}, "icon_ads",	 "Billy Mays here!"),
		new Campaign ("Selfie Sticks",	34,		9, 		300,	4.0f,		all,	 "icon_ads",	 "Billy Mays here!", CampaignSpecial.SELFIE),
		new Campaign ("Two 4 One",		42,		10, 	300,	4.0f,		all,	 "icon_ads",	 "Billy Mays here!", CampaignSpecial.TWO4ONE),
		new Campaign ("Plane Banner",	48,		15,		300,	5.0f,		all,	 "icon_ads",	"Billy Mays here!", CampaignSpecial.PLANE),
		};
		
		CampaignSpecial special;
		float hypeBoost; // aka hype meter boost
		Customer.CustomerType[] types;
		long seconds;

		// for kryo
		public Campaign() {}

		private Campaign(String name, int roundToUnlock, float initialCost, long seconds, float hypeBoost, Customer.CustomerType[] type, String iconName, String description, CampaignSpecial special) {
			super(AdCampaign.name, name, initialCost, 0, roundToUnlock, description, "market/" + iconName);		
			
			if (type != null && type[0] == null) type = null;
			
			String effect = "Attracts more ";
			if (type == null) effect += "customers ";
			else if (type.length == 1) effect += type[0].plural + " ";
			else {
				for (int i = 0; i < type.length - 1; i++) {
					effect += type[i].plural + ", ";
				}
				effect += "and " + type[type.length-1].plural + " ";
			}
			effect += "to your stand.";
			
			String duration = " Lasts " + seconds + " seconds.";
			this.description = description + " " + effect + " " + duration;
			this.hypeBoost = hypeBoost;
			this.types = type;
			this.seconds = seconds;
			this.special = special;
		}
		
		private Campaign(String name, int roundToUnlock, float initialCost, long seconds, float hypeBoost, Customer.CustomerType[] type, String iconName, String description) {
			this(name, roundToUnlock, initialCost, seconds, hypeBoost, type, iconName, description, CampaignSpecial.NONE);
		}		
		private Campaign(String name, int roundToUnlock, float initialCost, long seconds, float hypeBoost, Customer.CustomerType type, String iconName, String description) {
			this(name, roundToUnlock, initialCost, seconds, hypeBoost, new Customer.CustomerType[] {type}, iconName, description, CampaignSpecial.NONE);
		}
		private Campaign(String name, int roundToUnlock, float initialCost, long seconds, float hypeBoost, Customer.CustomerType type, String iconName, String description, CampaignSpecial special) {
			this(name, roundToUnlock, initialCost, seconds, hypeBoost, new Customer.CustomerType[] {type}, iconName, description, special);
		}
	};
	
	// for Kryo
	public AdCampaign(){
		super(name, desc, null, Campaign.values);
		this.consumable = true;
	};
	
	public AdCampaign(Inventory inventory) {
		super(inventory, name, desc, null, Campaign.values);
//		unlock(Campaign.LEVEL0);
		this.consumable = true;
	}
	
	public void reset() {
		this.unlocked.clear();
	}
}
