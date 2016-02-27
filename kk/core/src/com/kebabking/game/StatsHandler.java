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
	}
}