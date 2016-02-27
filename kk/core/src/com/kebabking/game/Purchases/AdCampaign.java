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
import com.kebabking.game.ProfileInventory;

// user has one of these
// later: split this into separate ones for chicken beef, lamb
public class AdCampaign extends PurchaseTypeConsumable {
	static String name = "AD CAMPAIGN";
	static String desc = "Marketing campaigns bring more customers to your stand. Using them at the right time can be the key to success!";
	
	private static Customer.CustomerType all = null;
	
	public enum CampaignSpecial {
		NONE, TSHIRTS, SELFIE, TWO4ONE, PLANE, SIGN
	}
	
	public static class Campaign extends SimpleConsumable {
		public static final Campaign[] values = new Campaign[] {
					// name,		round, 		jade, 	time, description
//		new Campaign ("Sign", 			1,		10,				0,	 		5,		1.5f, 		OLD_MAN,		"icon_ads",	"A nice kebab sign!"), 
		new Campaign ("Poster Ads", 	4,		1, 		120,	1.25f,		all, 		"poster",	"Advertise with posters!"),
		new Campaign ("Newspaper Ads",	5,		3,	 	160,	1.5f, 		new Customer.CustomerType[] {BUSINESSMAN, OLD_MAN}, 	"poster",	 "Get in print!"), 
		new Campaign ("Radio Ads",		12,		3,		180,	1.5f, 		new Customer.CustomerType[] {TOURIST, OLD_MAN, OLD_WOMAN},	 	"poster",  "Kebab FM!"),
		new Campaign ("Online Ads", 	18,		4,		200,	1.5f,		all,		"poster",	 "Technology!"),
		new Campaign ("Neon Sign",		19,		5,		200,	2.0f,		new Customer.CustomerType[] {STUDENT, FOREIGNER, TOURIST, MAN, WOMAN}, "sign", "Billy Mays here!", CampaignSpecial.SIGN),
		new Campaign ("TV Ads",			20,		6,		240,	2.4f,		all,		 "poster",	 "Billy Mays here!"),
		new Campaign ("T-Shirts",		22,		7,		300,	3.4f,		STUDENT,	 "poster",	 "Billy Mays here!", CampaignSpecial.TSHIRTS),
		new Campaign ("Tourist Brochure",30,	8,		300,	2.5f,		new Customer.CustomerType[] {FOREIGNER, FAT_MAN, TOURIST}, "poster",	 "Billy Mays here!"),
		new Campaign ("Selfie Sticks",	34,		9, 		300,	4.0f,		all,	 "selfie",	 "Billy Mays here!", CampaignSpecial.SELFIE),
		new Campaign ("Two for One",	42,		10, 	300,	4.0f,		all,	 "poster",	 "Billy Mays here!", CampaignSpecial.TWO4ONE),
		new Campaign ("Plane Banner",	48,		15,		300,	5.0f,		all,	 "poster",	"Billy Mays here!", CampaignSpecial.PLANE),
		};
		
		public CampaignSpecial special;
		public float hypeBoost; // aka hype meter boost
		public Customer.CustomerType[] types;

		// for kryo
		public Campaign() {}

		private Campaign(String name, int roundToUnlock, int jadeCost, long seconds, float hypeBoost, Customer.CustomerType[] type, String iconName, String description, CampaignSpecial special) {
			super(name, 0, jadeCost, roundToUnlock, description, "market/icons/ads_" + iconName, seconds);		
			
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
			
			if (type == null) effect = "";
			
			String duration = "Lasts " + seconds + " seconds.";
			this.description = description + " " + effect + " " + duration;
			this.hypeBoost = hypeBoost;
			this.types = type;
			this.special = special;
		}
		
		private Campaign(String name, int roundToUnlock, int jadeCost, long seconds, float hypeBoost, Customer.CustomerType[] type, String iconName, String description) {
			this(name, roundToUnlock, jadeCost, seconds, hypeBoost, type, iconName, description, CampaignSpecial.NONE);
		}		
		private Campaign(String name, int roundToUnlock, int jadeCost, long seconds, float hypeBoost, Customer.CustomerType type, String iconName, String description) {
			this(name, roundToUnlock, jadeCost, seconds, hypeBoost, new Customer.CustomerType[] {type}, iconName, description, CampaignSpecial.NONE);
		}
		private Campaign(String name, int roundToUnlock, int jadeCost, long seconds, float hypeBoost, Customer.CustomerType type, String iconName, String description, CampaignSpecial special) {
			this(name, roundToUnlock, jadeCost, seconds, hypeBoost, new Customer.CustomerType[] {type}, iconName, description, special);
		}
	};
	
	public Campaign getCurrent() {
		return (Campaign) this.getCurrentSelected();
	}
	
	public boolean isPlane() {
//		return true;
		return getCurrent() != null && getCurrent().special == CampaignSpecial.PLANE;
	}
	
	public boolean isTshirts() {
//		return true;
		return getCurrent() != null && getCurrent().special == CampaignSpecial.TSHIRTS;
	}
	
	// for Kryo
	public AdCampaign(){
		super(name, desc, Campaign.values);
		this.consumable = true;
	};
	
	public AdCampaign(ProfileInventory inventory) {
		super(inventory, name, desc, Campaign.values);
//		unlock(Campaign.LEVEL0);
		this.consumable = true;
	}
	
	public void reset() {
		this.unlocked.clear();
	}
}
