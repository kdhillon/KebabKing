package com.kebabking.game;

import com.kebabking.game.Managers.Manager;

public class StatsHandler {
	static KebabKing master;
	
	public static void init(KebabKing masterIn) {
		master = masterIn;
	}
	
	public static ProfileStats getStats() {
		return master.profile.stats;
	}
	
	public static void updateStatsFor(SummaryScreen summary) {
		getStats().customersSick += summary.sickCount;
		getStats().customersServed += summary.customersServed;
		
		getStats().totalRevenue += summary.revenue;
		getStats().totalExpenses += summary.expenses;
	}
	
	public static void earnJade(int jade) {
		getStats().totalJadeEarned += jade;
	}
	
	public static void spendJade(float jade) {
		getStats().jadeSpentInStore += jade;
	}
	
	public static void spendCash(float cash) {
		getStats().moneySpentInStore += cash;
	}
	
	public static void makePurchase() {
		getStats().totalStoreUnlocks++;
	}
	
	public static void purchaseConsumable() {
		getStats().totalConsumablesPurchased++;
	}
	public static void launchAdCampaign() {
		getStats().adCampaignsLaunched++;
	}
	
	public static void upgradeLocations() {
		getStats().locationsUnlocked++;
	}
	public static void jewelerOrder() {
		getStats().jewelerCustomers++;
	}
	
	public static void serveDrink() {
		getStats().drinksServed++;
	}
	public static void serveKebab() {
		getStats().kebabsServed++;
	}
	public static void trashKebab() {
		getStats().kebabsTrashed++;
	}
	public static void policeShutdown() {
		getStats().timesShutDown++;
	}
	
	public static void completeAd() {
		getStats().adsCompleted++;
	}
	public static void adNotAvailable() {
		getStats().adsNotAvailable++;
	}
	public static void shareOnFb() {
		getStats().sharesOnFb++;
	}
	public static void shareCrashedButRewarded() {
		getStats().sharesCrashedButRewarded++;
	}
	
	public static void onSessionEnd() {
		Manager.analytics.sendEventHit("Ads", "ads watched this session", "", (long) SummaryScreen.adsWatchedThisSession);
		Manager.analytics.sendEventHit("Game", "kebabs trashed this session", "", (long) Grill.kebabsTrashedThisSession);
		Manager.analytics.sendEventHit("Game", "kebabs served this session", "", (long) Grill.kebabsServedThisSession);
		Manager.analytics.sendEventHit("Game", "days played this session", "", (long) MainMenuScreen.daysPlayedThisSession);
		Manager.analytics.sendEventHit("Game", "market clicks this session", "", (long) MainMenuScreen.marketClicksThisSession);
	}
	
	public static void onLevelUp() {
		// send all stats
		// including level
		Manager.analytics.sendEventHit("OnLevelUp", ""+master.profile.getLevel(), "Days Played", getStats().daysWorked);	
		Manager.analytics.sendEventHit("OnLevelUp", ""+master.profile.getLevel(), "Current Cash", (long) master.profile.getCash());	
		Manager.analytics.sendEventHit("OnLevelUp", ""+master.profile.getLevel(), "Current Jade", master.profile.getCoins());	
		Manager.analytics.sendEventHit("OnLevelUp", ""+master.profile.getLevel(), "Kebabs Served", getStats().kebabsServed);	
		Manager.analytics.sendEventHit("OnLevelUp", ""+master.profile.getLevel(), "Drinks Served", getStats().drinksServed);	
		Manager.analytics.sendEventHit("OnLevelUp", ""+master.profile.getLevel(), "Customers Served", getStats().customersServed);	
		Manager.analytics.sendEventHit("OnLevelUp", ""+master.profile.getLevel(), "Customers Sick", getStats().customersSick);	
		Manager.analytics.sendEventHit("OnLevelUp", ""+master.profile.getLevel(), "Total Revenue", (long) getStats().totalRevenue);	
		Manager.analytics.sendEventHit("OnLevelUp", ""+master.profile.getLevel(), "Total Expenses", (long) getStats().totalExpenses);	
		Manager.analytics.sendEventHit("OnLevelUp", ""+master.profile.getLevel(), "Jade Earned", getStats().totalJadeEarned);	
		Manager.analytics.sendEventHit("OnLevelUp", ""+master.profile.getLevel(), "Store Unlocks", getStats().totalStoreUnlocks);	
		Manager.analytics.sendEventHit("OnLevelUp", ""+master.profile.getLevel(), "Locations Unlocked", getStats().locationsUnlocked);	
		Manager.analytics.sendEventHit("OnLevelUp", ""+master.profile.getLevel(), "Consumables Purchased", getStats().totalConsumablesPurchased);	
		Manager.analytics.sendEventHit("OnLevelUp", ""+master.profile.getLevel(), "Ad Campaigns Launched", getStats().adCampaignsLaunched);	
		Manager.analytics.sendEventHit("OnLevelUp", ""+master.profile.getLevel(), "Jeweler Orders", getStats().jewelerCustomers);	
		Manager.analytics.sendEventHit("OnLevelUp", ""+master.profile.getLevel(), "Kebabs Trashed", getStats().kebabsTrashed);	
		Manager.analytics.sendEventHit("OnLevelUp", ""+master.profile.getLevel(), "Money Spent In Store", (long) getStats().moneySpentInStore);	
		Manager.analytics.sendEventHit("OnLevelUp", ""+master.profile.getLevel(), "Jade Spent In Store", getStats().jadeSpentInStore);	
		Manager.analytics.sendEventHit("OnLevelUp", ""+master.profile.getLevel(), "Ads Completed", getStats().adsCompleted);	
		Manager.analytics.sendEventHit("OnLevelUp", ""+master.profile.getLevel(), "Ads Not Available", getStats().adsNotAvailable);	
		Manager.analytics.sendEventHit("OnLevelUp", ""+master.profile.getLevel(), "Shares on FB", getStats().sharesOnFb);	
		Manager.analytics.sendEventHit("OnLevelUp", ""+master.profile.getLevel(), "Shares Crashed But Rewarded", getStats().sharesCrashedButRewarded);	
		Manager.analytics.sendEventHit("OnLevelUp", ""+master.profile.getLevel(), "Last Share Day", getStats().lastShareDay);	
		Manager.analytics.sendEventHit("OnLevelUp", ""+master.profile.getLevel(), "Times Shut Down", getStats().timesShutDown);	
	}
}