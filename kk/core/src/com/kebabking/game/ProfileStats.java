package com.kebabking.game;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;

public class ProfileStats {
	@Tag(301) public int daysWorked;		// starts at 0
	@Tag(302) public int kebabsServed;
	@Tag(303)	public int drinksServed;
	@Tag(304)	public int customersServed;
	@Tag(305) public int customersSick;
	@Tag(306) public float totalRevenue;
	@Tag(307) public float totalExpenses;
	@Tag(308) public int totalJadeEarned;
	
	@Tag(309) public int totalStorePurchases;
	@Tag(310) public int adCampaignsLaunched;
	
	@Tag(311) public int locationsUnlocked;
	
	@Tag(312) public int timesShutDown;
	
	@Tag(313) public int jewelerCustomers; // set this when jeweler customer has arrived

//	@Tag(52) public int fuckIsThis;
	
	// Could have booleans here, used for launching tutorial notifications.

	
	public void initializeAfterLoad() {	
		// initialize any fields that were empty at load.
	}
}
