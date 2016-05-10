package com.kebabking.game;

import java.util.ArrayList;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;

public class ProfileStats {
	@Tag(301) public int daysWorked;		// starts at 0
	
	// acutally start using these
	@Tag(302) public int kebabsServed;
	@Tag(303) public int drinksServed;
	
	@Tag(304) public int customersServed;
	@Tag(305) public int customersSick;
	
	@Tag(306) public float totalRevenue;
	@Tag(307) public float totalExpenses;
	@Tag(308) public int totalJadeEarned;
	
	@Tag(309) public int totalStoreUnlocks;
	@Tag(310) public int adCampaignsLaunched;
	@Tag(311) public int locationsUnlocked;
	@Tag(312) public int timesShutDown;
	@Tag(313) public int jewelerCustomers; // set this when jeweler customer has arrived

	
	// FOR TUTORIAL
	@Tag(314) public boolean spiceCustomerServed;
	@Tag(315) public boolean customerHasOrderedDouble;
	@Tag(316) public boolean burntMeatThrownAway;
	@Tag(317) public boolean servedRaw;
	@Tag(319) public boolean firstCustomerServed;
	@Tag(320) public boolean secondCustomerServed;

	@Tag(321) public int lastShareDay = -5;

	@Tag(322) public int kebabsTrashed;
	@Tag(323) public float moneySpentInStore;
	@Tag(324) public int jadeSpentInStore;

	@Deprecated @Tag(325) public int locationUpgradesPurchased;

	@Tag(326) public int totalConsumablesPurchased;
	
	@Tag(327) public int adsCompleted;
	@Tag(328) public int adsNotAvailable;
	@Tag(329) public int sharesOnFb;
	@Tag(330) public int sharesCrashedButRewarded;
	
	@Tag(331) public ArrayList<Integer> reputationHistory; // stores 2x the reputation for efficiency
	@Tag(332) public ArrayList<Float> revenueHistory;
	@Tag(333) public ArrayList<Float> expensesHistory;
	@Tag(334) public int historyCollectedStart; // day on which history started being collected
	
	@Tag(335) public float profitRecord;
	
	public void initializeAfterLoad(Profile profile) {	
		// initialize any fields that were empty at load.
		if (reputationHistory == null || revenueHistory == null || expensesHistory == null) {
			reputationHistory = new ArrayList<Integer>();
			revenueHistory = new ArrayList<Float>();
			expensesHistory = new ArrayList<Float>();
			historyCollectedStart = daysWorked;
		}
		
		if (KebabKing.DISABLE_TUTORIAL) {
			firstCustomerServed = true;
			secondCustomerServed = true;
			spiceCustomerServed = true;
			servedRaw = true;
			burntMeatThrownAway = true;
			jewelerCustomers = 1;

			// TODO if we want more robust solution, use booleans instead of daysWorked
			daysWorked = 4;
			profile.inventory.forceSecondBoxUpdate();
			profile.inventory.forceThirdBoxUpdate();
		}
		else if (KebabKing.DISABLE_FIRST_DAY) {
			firstCustomerServed = true;
			secondCustomerServed = true;
//			spiceCustomerServed = true;

			// TODO if we want more robust solution, use booleans instead of daysWorked
			if (daysWorked <= 1) daysWorked = 1;
//			profile.inventory.forceSecondBoxUpdate();
		}
		// don't have tutorials if loading an old save:
		if (daysWorked >= 1) {
			firstCustomerServed = true;
			secondCustomerServed = true;
		}
		if (daysWorked >= 2) {
			spiceCustomerServed = true;
		}
	}

	public void dayEnd(float rating, float revenue, float expenses) {
		daysWorked++;
		reputationHistory.add((int) (rating * 2));
		revenueHistory.add(revenue);
		expensesHistory.add(expenses);
	}
	
	public boolean isRecord(float profit) {
		if (profit > profitRecord) {
			profitRecord = profit;
			return true;
		}
		return false;
	}
	
	public boolean tutorialComplete() {
		return secondCustomerServed;
	}
}
