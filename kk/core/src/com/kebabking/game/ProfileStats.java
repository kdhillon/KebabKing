package com.kebabking.game;

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
	
	@Tag(309) public int totalStorePurchases;
	@Tag(310) public int adCampaignsLaunched;
	
	@Tag(311) public int locationsUnlocked;
	
	@Tag(312) public int timesShutDown;
	
	@Tag(313) public int jewelerCustomers; // set this when jeweler customer has arrived

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
	@Tag(325) public int locationUpgradesPurchased;

	// Could have booleans here, used for launching tutorial notifications.
	
	public void initializeAfterLoad(Profile profile) {	
		// initialize any fields that were empty at load.
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
		// don't have tutorials if loading an old save:
		if (daysWorked >= 1) {
			firstCustomerServed = true;
			secondCustomerServed = true;
		}
		if (daysWorked >= 2) {
			spiceCustomerServed = true;
		}
	}
}
