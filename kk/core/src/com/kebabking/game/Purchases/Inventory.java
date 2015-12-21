package com.kebabking.game.Purchases;

import com.badlogic.gdx.utils.TimeUtils;
import com.kebabking.game.Profile;
import com.kebabking.game.Managers.Manager;
import com.kebabking.game.Purchases.AdCampaign.Campaign;
import com.kebabking.game.Purchases.Vanity.VanityDecorationType;
import com.kebabking.game.Purchases.Vanity.VanityGrillStandType;
import com.kebabking.game.Purchases.Vanity.VanityItem;

public class Inventory {	
	// class that contains all information about purchased items
	// remember to make this expandable, so that there are no problems when updates are released
	Profile profile;

	public MeatQuality meatQuality;
	public DrinkQuality drinkQuality;

	// Info about grill
	public GrillSpecs grillSpecs; // contains two relevant items, can remove later

	// Current location
	public LocationType locationType;

	// Ad campaigns
	public AdCampaign adCampaign;
	public AdCampaign.Campaign currentCampaign;
	public long campaignStartedAtNanos;
	public long campaignEndsAtNanos;

	// List of Vanity items player might have
	// decorations includes things that don' get in the way of other things (flowers, etc)
	public VanityDecorationType decorations;
	public VanityGrillStandType grillStand;

	// Location upgrades
	// Subordinates (?)
	// can manage additional stands once you get good enough?
	// each one has its own reputation and you have to manage them

	// no-arg constructor for kryo
	public Inventory() {
		VanityItem.initialize();
	}

	// create a blank purchases
	public Inventory(Profile profile) {
		VanityItem.initialize();

		this.profile = profile;

		meatQuality = new MeatQuality(this);

		drinkQuality = new DrinkQuality(this);

		locationType = new LocationType(this);

		grillSpecs = new GrillSpecs(this);

		adCampaign = new AdCampaign(this);

		decorations = new VanityDecorationType(this);
		
		grillStand = new VanityGrillStandType(this);
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

	public boolean hasUnlockedByLevel(Purchaseable item) {
		if (this.profile == null) return false;
		return this.profile.getCurrentRound() >= item.unlockAtLevel();
	}

	// Does the current round exactly equal the unlock round?
	public boolean atExactRoundForUnlock(Purchaseable item) {
		if (this.profile == null) return false;
		return this.profile.getCurrentRound() == item.unlockAtLevel();
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
		if (!hasUnlockedByLevel(item)) throw new java.lang.AssertionError();
		if (!canAffordUnlock(item)) return false;

		// unlock item
		type.unlock(item);

		Manager.analytics.sendEventHit("Inventory", "Unlock", item.getName());

		// spend money
		if (item.coinsToUnlock() > 0) {
			spendCoins(item.coinsToUnlock());
		}
		else{ 
			spendCash(item.cashToUnlock());
		}
		
		// TODO remove, move to better place
		if (type == this.adCampaign) {
			purchaseConsumable(item, type);
		}

		// save game
		profile.save();

		return true;
	}

	public boolean purchaseConsumable(Purchaseable item, PurchaseType type) {
		if (!type.isUnlocked(item)) {
			System.out.println("Item not unlocked!");
			return false;
		}
		if (!canAffordConsumable(item)) {
			System.out.println("Can't afford item!");
			return false;
		}
		if (alreadyActive(item)) {
			System.out.println("Item already active!");
			return false;
		}

		Manager.analytics.sendEventHit("Inventory", "Purchase Consumable", item.getName());

		// spend money
		spendCash(item.getDailyCost());

		if (type == adCampaign) {
			System.out.println("purchased ad campaign");
			Campaign campaign = (Campaign) item;
			this.currentCampaign = campaign;
			this.campaignEndsAtNanos = TimeUtils.nanoTime() + 1000000000*campaign.seconds;
			this.campaignStartedAtNanos = TimeUtils.nanoTime();
			// this needs to happen on load...
			profile.updateCustomerDistribution(campaign.type, campaign.hypeBoost);
		}

		// save game
		profile.save();

		return true;
	}

	public boolean alreadyActive(Purchaseable item) {
		if (this.currentCampaign == item) return true;
		return false;
	}

	// returns true if can afford to purchase given consumable
	public boolean canAffordConsumable(Purchaseable item) {
		if (this.getCash() >= item.getDailyCost()) return true;
		return false;
	}

	// Updates current ad campaign. Should be called every frame that kitchen screen is active.
	public void updateAds() {
//		System.out.println("Boost: " + profile.boost);
//		System.out.println("Campaign percent: " + campaignPercent());

		if (currentCampaign != null &&
				TimeUtils.nanoTime() > this.campaignEndsAtNanos) {
			this.currentCampaign = null;
			profile.resetCustomerDistribution();
		}
	}

	// can be used for Hype meter
	public float campaignPercent() {
		if (this.currentCampaign == null) return 0;
		return 1.0f * TimeUtils.timeSinceNanos(campaignStartedAtNanos) / 
				(campaignEndsAtNanos - campaignStartedAtNanos);
	}

	// reset values after one day
	public void endDay() {

	}
}
