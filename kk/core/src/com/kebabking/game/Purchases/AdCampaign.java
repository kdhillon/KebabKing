package com.kebabking.game.Purchases;

import static com.kebabking.game.Customer.CustomerType.BUSINESSMAN;
import static com.kebabking.game.Customer.CustomerType.FARMER;
import static com.kebabking.game.Customer.CustomerType.FAT_MAN;
import static com.kebabking.game.Customer.CustomerType.FOREIGNER;
import static com.kebabking.game.Customer.CustomerType.GIRL;
import static com.kebabking.game.Customer.CustomerType.MAN;
import static com.kebabking.game.Customer.CustomerType.OLD_MAN;
import static com.kebabking.game.Customer.CustomerType.OLD_WOMAN;
import static com.kebabking.game.Customer.CustomerType.POLICE;
import static com.kebabking.game.Customer.CustomerType.SOLDIER;
import static com.kebabking.game.Customer.CustomerType.STUDENT;
import static com.kebabking.game.Customer.CustomerType.TOURIST;
import static com.kebabking.game.Customer.CustomerType.WOMAN;

import com.kebabking.game.Customer;
import com.kebabking.game.ProfileInventory;

// user has one of these
// later: split this into separate ones for chicken beef, lamb
public class AdCampaign extends PurchaseTypeConsumable {
	static String name = "advertising";
	static String desc = "ad_desc";
	
	static float TINY_BOOST = 1.5f;
	static float SMALL_BOOST = 4f;
	static float MEDIUM_BOOST = 6.0f;
	static float LARGE_BOOST = 10.0f; // bigger because not universal
	static float HUGE_BOOST = 10.0f;
	static float INSANE_BOOST = 10.0f;
	
	static float TINY_SAT = 1.1f;
	static float SMALL_SAT = 1.2f;
	static float MEDIUM_SAT = 1.4f;
	static float LARGE_SAT = 1.6f;
	static float HUGE_SAT = 2.0f;
	static float INSANE_SAT = 2.0f;
	
	static enum Boost {TINY, SMALL, MEDIUM, LARGE, HUGE, INSANE};
	
	// ad campaigns increase your stand's popularity, and customer's satisfaction. Different campaigns affect different types of customers!
	// neon sign - Gives a small reputation boost for all customers. 
	// Posters - These official looking posters will grab the attention of policemen and soldiers. 
	// Newspapers - Extra, extra! Attracts more businessmen and boosts their satisfaction.
	// Radio - Kebab FM! Old folks will love these cheesy ads.
	// Online - kebab.com! Young customers will obsess over your kebabs.
	// TV - Fat
	// sign: tiny boost for everyone
	
	// posters: small boost
//	POLICE(			.8f, 	2, 	6, 		.4f, 	.75f, "Policeman", Assets.strings.get("police")),
//	SOLDIER(		.8f, 	2,	7, 		.4f, 	.53f, "Soldier", Assets.strings.get("soldiers")),
	
	// newspapers: small boost
//	BUSINESSMAN(	.8f, 	2,	7, 		.5f, 	.9f, "Businessman", Assets.strings.get("businessmen")),
//	FARMER(			.8f, 	2, 	8, 		.3f, 	.7f, "Farmer", Assets.strings.get("farmers")),//

	// radio: medium boost
//	OLD_MAN(		1, 		1, 	5, 		.2f, 	.55f, "OldMan", Assets.strings.get("old_men")),
//	OLD_WOMAN(		1.2f, 	1,	3, 		.05f, 	.55f, "OldWoman", Assets.strings.get("old_women")),
	
	// online: medium boost
//	STUDENT(		.8f, 	2,	8, 		.8f, 	.8f, "Student", Assets.strings.get("students")), //
//	GIRL(			1f, 	2, 	6, 		.05f, 	.75f, "Girl", Assets.strings.get("girls")),

	// tv: large boost
//	FAT_MAN(		1, 		3, 	9, 		.6f,	 .5f, "FatMan", Assets.strings.get("fat_men")), //
	
	// tourist brochure: large boost
//	TOURIST(		1f, 	3, 	9, 		.4f, 	.53f, "FatAmerican", Assets.strings.get("tourists")),
//	FOREIGNER(		.8f, 	3, 	8, 		.5f, 	.75f, "American", Assets.strings.get("foreigners")),//
	
	// t shirts: huge boost
//	WOMAN(			1, 		2,	4, 		.2f, 	.7f, "Woman", Assets.strings.get("women")),
//	MAN(			1, 		2, 	7, 		.5f, 	.55f, "Man", Assets.strings.get("men")),
	
	// selfie sticks: huge boost with all
	// plane: insane boost with all
	
//	JEWELER(		1f, 	1, 	8, 		.2f, 	.6f, "Jeweler", Assets.strings.get("jewelers")),//
	
	private static Customer.CustomerType[] all = null;
	
	public enum CampaignSpecial {
		NONE, TSHIRTS, SELFIE, TWO4ONE, PLANE, SIGN
	}
	
	public static class Campaign extends SimpleConsumable {
		public static final Campaign[] values = new Campaign[] {
					// name,		round,	jade, 	time, description
		new Campaign ("sign",		3,	2,	3,		300,	Boost.TINY,		all, "sign", CampaignSpecial.SIGN),
		new Campaign ("posters", 	7,	3,	5, 		300,	Boost.SMALL,		new Customer.CustomerType[] {POLICE, SOLDIER}, "poster"),
		new Campaign ("newspapers",	9,	4,	5,	 	300,	Boost.SMALL, 		new Customer.CustomerType[] {BUSINESSMAN, FARMER}, "newspaper"), 
		new Campaign ("radio",		12,	5,	10,		300,	Boost.MEDIUM, 		new Customer.CustomerType[] {OLD_MAN, OLD_WOMAN}, "radio"),
		new Campaign ("online", 	16,	7,	10,		300,	Boost.MEDIUM,		new Customer.CustomerType[] {STUDENT, GIRL}, "online"),
		new Campaign ("tv",			20,	8,	15,		300,	Boost.LARGE,		new Customer.CustomerType[] {FAT_MAN},		 "tv"),
		new Campaign ("tourist",	22,	10,	15,		300,	Boost.LARGE,		new Customer.CustomerType[] {FOREIGNER, TOURIST}, "tourist"),
		new Campaign ("tshirts",	30,	12,	25,		300,	Boost.HUGE,			new Customer.CustomerType[] {MAN, WOMAN},	"tshirt", CampaignSpecial.TSHIRTS),
		new Campaign ("selfie",		34,	14,	35,		300,	Boost.HUGE,		all,	 "selfie", CampaignSpecial.SELFIE),
		new Campaign ("plane",		48,	17,	50,		300,	Boost.INSANE,		all,	 "plane", CampaignSpecial.PLANE),
		};
		
		public CampaignSpecial special;
		public float hypeBoost; //
		public float satBoost;  // applied to all customers equally.
		public Customer.CustomerType[] types;

		// for kryo
		public Campaign() {}

		private Campaign(String name, int roundToUnlock, int unlockWithLocation, int jadeCost, long seconds, Boost boost, Customer.CustomerType[] type, String iconName, CampaignSpecial special) {
			super(AdCampaign.name+"_"+name, 0, jadeCost, roundToUnlock, unlockWithLocation, "market/icons/ads_" + iconName, seconds);		
			
			if (type != null && type[0] == null) type = null;
			
//			String effect = "Attracts more ";
//			if (type == null) effect += "customers.";
//			else if (type.length == 1) effect += type[0].plural + " ";
//			else {
//				for (int i = 0; i < type.length - 1; i++) {
//					effect += type[i].plural;
//					if (i != type.length - 2) {
//						 effect += ",";
//					}
//					effect += " ";
//				}
//				effect += "and " + type[type.length-1].plural + ".";
//			}
//			effect += "to your stand.";
//			effect += ".";
			
//			if (type == null) effect = "";
			
//			String duration = "Lasts " + seconds + " seconds.";
//			this.description = effect;//  + " " + duration;
//			this.description = Assets.strings.get(AdCampaign.name +"_"+name+"_d");
			switch(boost) {
			case TINY:
				hypeBoost = TINY_BOOST;
				satBoost = TINY_SAT;
				break;
			case SMALL:
				hypeBoost = SMALL_BOOST;
				satBoost = SMALL_SAT;
				break;
			case MEDIUM:
				hypeBoost = MEDIUM_BOOST;
				satBoost = MEDIUM_SAT;
				break;
			case LARGE:
				hypeBoost = LARGE_BOOST;
				satBoost = LARGE_SAT;
				break;
			case HUGE:
				hypeBoost = HUGE_BOOST;
				satBoost = HUGE_SAT;
				break;
			case INSANE:
				hypeBoost = INSANE_BOOST;
				satBoost = INSANE_SAT;
				break;
			}
			
			this.types = type;
			this.special = special;
		}
		
		private Campaign(String name, int roundToUnlock, int unlockWithLocation, int jadeCost, long seconds, Boost boost, Customer.CustomerType[] type, String iconName) {
			this(name, roundToUnlock, unlockWithLocation, jadeCost, seconds, boost, type, iconName, CampaignSpecial.NONE);
		}		
//		private Campaign(String name, int roundToUnlock, int unlockWithLocation, int jadeCost, long seconds, float hypeBoost, Customer.CustomerType type, String iconName) {
//			this(name, roundToUnlock, unlockWithLocation, jadeCost, seconds, hypeBoost, new Customer.CustomerType[] {type}, iconName, CampaignSpecial.NONE);
//		}
//		private Campaign(String name, int roundToUnlock, int unlockWithLocation, int jadeCost, long seconds, float hypeBoost, Customer.CustomerType type, String iconName, CampaignSpecial special) {
//			this(name, roundToUnlock, unlockWithLocation, jadeCost, seconds, hypeBoost, new Customer.CustomerType[] {type}, iconName, special);
//		}
	};
	
	public Campaign getCurrent() {
		return (Campaign) this.getCurrentSelected();
	}
	
	public boolean isPlane() {
//		return true;
		return getCurrent() != null && getCurrent().special == CampaignSpecial.PLANE;
	}
	
	public boolean isSign() {
//		return true;
		return getCurrent() != null && getCurrent().special == CampaignSpecial.SIGN;
	}
	
	public boolean isTshirts() {
//		return true;
		return getCurrent() != null && getCurrent().special == CampaignSpecial.TSHIRTS;
	}
	
	public boolean isSelfie() {
//		return true;
		return getCurrent() != null && getCurrent().special == CampaignSpecial.SELFIE;
	}
	
	public float getSatBoost(Customer.CustomerType type) {
		float boost = 1;
		
		if (getCurrent() != null) {
			Customer.CustomerType[] types = getCurrent().types;
			if (types == all) return getCurrent().satBoost;
			
			for (Customer.CustomerType t : types) {
				if (type == t) {
					boost = getCurrent().satBoost;
				}
			}
		}
		
		return boost;
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
