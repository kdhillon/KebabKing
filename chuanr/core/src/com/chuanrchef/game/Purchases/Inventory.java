package com.chuanrchef.game.Purchases;

import com.chuanrchef.game.Analytics;
import com.chuanrchef.game.Profile;

public class Inventory {	

	static String beerQualityDescription =
			"Upgrade the quality of your drinks! Better drinks cost more, but sell for "
					+ "more and increase your reputation!. Some customers will only accept"
					+ " high quality drinks! ";


	Profile profile;
	// class that contains all information about purchased items
	// remember to make this expandable, so that there are no problems when updates are released

	public MeatQuality meatQuality;
	public DrinkQuality drinkQuality;

	public GrillSpecs grillSpecs; // not an item but contains two relevant items, can remove later

	public LocationType locationType;
	
	public AdCampaign adCampaign;

	// Grill types:
	//	Regular Small, 
	//	Regular Large, 
	//	Storage Small, 
	//	Storage Big, 
	//	(Control Small), 
	//	(Control Big)


	// Location upgrades
	//	Location location;

	// Advertising plans (increase reputation, go away after a day)
	// advertise in local paper 
	// press release (expensive)
	boolean advertisingToday;
	static int advertisingTodayCooldown = 1;
	int advertisingTodayCountdown = 0;

	// Free chuanr for students, boosts reputation but you don't make money from students
	boolean freeForStudents;
	static int freeForStudentsCooldown = 5;
	int freeForStudentsCountdown = 0;

	//	Invite the health inspector for a visit
	//	expires after one day
	boolean healthInspectorVisit;
	static int healthInspectorVisitCooldown = 5;
	int healthInspectorCountdown = 0;

	// Street vendor permit
	// prevents cops from shutting you down, expires every 7 days
	boolean streetVendorPermit;
	static int streetVendorPermitCooldown = 7;
	int streetVendorCountdown = 0;


	// Stand upgrades
	// chuanr sign
	int signLevel; // increases popularity (?)

	// outdoor lighting
	int lightingLevel; // extends day length

	// aesthetics
	//	boolean flowers; // flowers do nothing


	// Extra line (?)
	boolean extraLine;

	// Vanity items:
	// flowers
	// 

	// Subordinates (?)
	// can manage additional stands once you get good enough?
	// each one has its own reputation and you have to manage them

	// Switch out your meat for other types (eventually)

	//	invite the health inspector for a free meal
	//	Advertise in the local newspaper
	//	upgrade your premises 
	//	extra seating
	//	restaurant
	//	Change location 

	// no-arg constructor for kryo
	public Inventory() {
		
	}
	
	// create a blank purchases
	public Inventory(Profile profile) {
		this.profile = profile;

		meatQuality = new MeatQuality();
		drinkQuality = new DrinkQuality();
		
		locationType = new LocationType();

		grillSpecs = new GrillSpecs();
		
		adCampaign = new AdCampaign();

		lightingLevel = 1;

		extraLine = false;

		advertisingToday = false;
		freeForStudents = false;
		healthInspectorVisit = false;
		streetVendorPermit = false;
	}

	public float getCash() {
		return this.profile.getCash();
	}

	public int getCoins() {
		return this.profile.getCoins();
	}
	
	public void spendCoins(int coins) {
		profile.spendCoins(coins);
	}
	
	public void spendCash(float cash) {
		profile.spendCash(cash);
	}

	// returns true if can afford to upgrade given item
	public boolean canAffordUnlock(Purchaseable item) {
		if (item.coinsToUnlock() > 0) {
			if (this.getCoins() >= item.coinsToUnlock()) return true;
		}
		else{ 
			if (this.getCash() >= item.cashToUnlock()) return true;
		}
		return false;
	}

	public boolean unlock(Purchaseable item, PurchaseType type) {
		if (!canAffordUnlock(item)) return false;
		
		// unlock item
		type.unlock(item);
		
		Analytics.sendEventHit("Inventory", "Unlock", item.getName());
		
		// spend money
		if (item.coinsToUnlock() > 0) {
			spendCoins(item.coinsToUnlock());
		}
		else{ 
			spendCash(item.cashToUnlock());
		}
		
		// save game
		profile.save();
		
		return true;
	}
	
	
	// return false if cannot afford
	//	public void purchaseGrillSize(GrillSize size) {
	//		if (!canAffordGrillSize(size)) throw new java.lang.AssertionError();
	//		this.grillSize = size;
	//	}
	//
	//
	//	public void purchaseMeatUpgrade() {
	//		if (!canAffordGrillSize(size)) throw new java.lang.AssertionError();
	//
	//
	//	}
	//
	//	public void purchaseAdvertisingToday() {
	//		if (!canAffordGrillSize(size)) throw new java.lang.AssertionError();
	//
	//		this.advertisingToday = true;
	//		this.advertisingTodayCountdown = advertisingTodayCooldown;
	//	}
	//
	//	public void purchaseFreeForStudents() {
	//		if (!canAffordGrillSize(size)) throw new java.lang.AssertionError();
	//
	//		this.freeForStudents = true;
	//		this.freeForStudentsCountdown = freeForStudentsCooldown;
	//	}
	//
	//	public void purchaseHealthInspectorVisit() {
	//		if (!canAffordGrillSize(size)) throw new java.lang.AssertionError();
	//
	//		this.healthInspectorVisit = true;
	//		this.healthInspectorCountdown = healthInspectorVisitCooldown;
	//	}
	//
	//	public void purchaseStreetVendorPermit() {
	//		if (!canAffordGrillSize(size)) throw new java.lang.AssertionError();
	//
	//		this.streetVendorPermit = true;
	//		this.advertisingTodayCountdown = advertisingTodayCooldown;
	//	}

	// reset values after one day
	public void endDay() {
		advertisingToday = false;
		freeForStudents = false;
		healthInspectorVisit = false;
		if (advertisingTodayCountdown > 0) advertisingTodayCountdown--;
		if (freeForStudentsCountdown > 0) freeForStudentsCountdown--;
		if (healthInspectorCountdown > 0) healthInspectorCountdown--;
		if (streetVendorCountdown > 0) streetVendorCountdown--;

		if (streetVendorCountdown == 0) streetVendorPermit = false; // expires after 7 days
	}
}
