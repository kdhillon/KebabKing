package com.kebabking.game;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.kebabking.game.Managers.Manager;
import com.kebabking.game.Purchases.AdCampaign;
import com.kebabking.game.Purchases.DrinkQuality;
import com.kebabking.game.Purchases.GrillSize;
import com.kebabking.game.Purchases.GrillStand;
import com.kebabking.game.Purchases.GrillType;
import com.kebabking.game.Purchases.LocationType;
import com.kebabking.game.Purchases.MeatQuality;
import com.kebabking.game.Purchases.PurchaseType;
import com.kebabking.game.Purchases.PurchaseTypeConsumable;
import com.kebabking.game.Purchases.Purchaseable;
import com.kebabking.game.Purchases.SimpleConsumable;
import com.kebabking.game.Purchases.StickType;

public class ProfileInventory {	
	// class that contains all information about purchased items
	// remember to make this expandable, so that there are no problems when updates are released
	ProfileRobust profile;

	@Tag(101) public MeatQuality meatQuality;
	@Tag(102) public DrinkQuality drinkQuality;

	@Tag(103) public StickType stickType;

	// Info about grill
	//	@Tag(0) public GrillSpecs grillSpecs; // contains two relevant items, can remove later

	@Tag(104) public GrillSize grillSize;

	@Tag(105) public GrillType grillType;

	@Tag(106) public GrillStand grillStand;

	// Current location
	@Tag(107) public LocationType locationType;

	// Ad campaigns
	//	public AdCampaign adCampaign;

	@Tag(108) public AdCampaign adCampaign;

	// testing tags, won't be saved.
	@Deprecated	@Tag(109) public AdCampaign unused;
	@Deprecated @Tag(110) public String fake;

	// no-arg constructor for kryo
	public ProfileInventory() {
//		VanityItem.initialize();
	}

	// create a blank purchases
	public ProfileInventory(ProfileRobust profile) {
//		VanityItem.initialize();

		this.profile = profile;

		meatQuality = new MeatQuality(this);

		drinkQuality = new DrinkQuality(this);

		locationType = new LocationType(this);

		grillSize = new GrillSize(this);

//		grillType = new GrillType(this);

		adCampaign = new AdCampaign(this);

		//		decorations = new VanityDecorationType(this);

		grillStand = new GrillStand(this);

		stickType = new StickType(this);
	}

	public void initializeAfterLoad() {

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
		if (this.profile == null) {
			System.out.println("PROFILE IS NULL");
			throw new java.lang.AssertionError();
			//			return false;
		}
		return this.profile.getLevel() >= item.unlockAtLevel();
	}

	// Does the current round exactly equal the unlock round?
	//	public boolean atExactRoundForUnlock(Purchaseable item) {
	//		if (this.profile == null) return false;
	//		return this.profile.getCurrentRound() == item.unlockAtLevel();
	//	}

	// returns true if can afford to upgrade OR activate given item
	public boolean canAffordPurchase(PurchaseType type, Purchaseable item) {
		if (type.consumable) {
			SimpleConsumable consumable = (SimpleConsumable) item;
			if (consumable.coinsToActivate() > 0) {
				if (this.getCoins() < consumable.coinsToActivate()) return false;
			}
			if (consumable.cashToActivate() > 0) { 
				if (this.getCash() < consumable.cashToActivate()) return false;
			}
		}
		else {
			if (item.coinsToUnlock() > 0) {
				if (this.getCoins() < item.coinsToUnlock()) return false;
			}
			if (item.cashToUnlock() > 0) { 
				if (this.getCash() < item.cashToUnlock()) return false;
			}
		}
		return true;
	}

	public boolean unlock(Purchaseable item, PurchaseType type) {
		if (!hasUnlockedByLevel(item)) throw new java.lang.AssertionError();
		if (type.consumable) throw new java.lang.AssertionError();
		if (!canAffordPurchase(type, item)) return false;

		// unlock item
		type.unlock(item);

		Manager.analytics.sendEventHit("Inventory", "Unlock", item.getName());

		// spend money
		if (item.coinsToUnlock() > 0) {
			spendCoins(item.coinsToUnlock());
		}
		if (item.cashToUnlock() > 0) { 
			spendCash(item.cashToUnlock());
		}

		// save game
		profile.save();

		return true;
	}

	public boolean purchaseConsumable(SimpleConsumable item, PurchaseTypeConsumable type) {
		if (!type.availableForUnlock(item)) {
			System.out.println("Consumable not unlocked!");
			return false;
		}
		if (!canAffordConsumable(item)) {
			System.out.println("Can't afford consumable!");
			return false;
		}
		if (type.getActive() != null) {
			System.out.println("A consumable of this type is already active!");
			return false;
		}

		Manager.analytics.sendEventHit("Inventory", "Purchase Consumable", item.getName());

		// spend money
		if (item.cashToActivate() > 0) {
			this.spendCash(item.cashToActivate());
		}
		if (item.coinsToActivate() > 0) {
			this.spendCoins(item.coinsToActivate());
		}		
		
		if (type == adCampaign) {
			System.out.println("purchased ad campaign");
			//			Campaign campaign = (Campaign) item;
			this.adCampaign.activateConsumable(item);
			//			this.campaignEndsAtNanos = TimeUtils.nanoTime() + 1000000000*campaign.seconds;
			//			this.campaignStartedAtNanos = TimeUtils.nanoTime();
			// this needs to happen on load...
			//			profile.updateCustomerDistribution(campaign.types, campaign.hypeBoost);
			profile.master.cm.updateCustomerDistribution(this.adCampaign);
		}

		// save game
		profile.save();

		return true;
	}

	public void handleConsumableReset(PurchaseTypeConsumable type) {
		if (type == this.adCampaign)
			profile.master.cm.resetCustomerDistribution();
	}

	public boolean campaignAlreadyActive(Purchaseable item) {
		if (this.adCampaign.getActive() == item) return true;
		return false;
	}

	// returns true if can afford to purchase given consumable
	public boolean canAffordConsumable(SimpleConsumable item) {
		if (item.cashToActivate() > 0) {
			if (this.getCash() < item.cashToActivate()) 
				return false;
		}
		if (item.coinsToActivate() > 0) {
			if (this.getCoins() < item.coinsToActivate())
				return false;
		}
		return true;
	}


	// update all consumable purchasetypes
	public void updateConsumables() {
		updateAds();
	}

	//	// Updates current ad campaign. Should be called every frame that kitchen screen is active.
	private void updateAds() {
		//		System.out.println("Boost: " + profile.boost);
		//		System.out.println("Campaign percent: " + campaignPercent());
		adCampaign.update();
	}

	// can be used for Hype meter
	public float campaignPercent() {
		return adCampaign.getPercent();
	}

	//	// reset values after one day
	//	public void endDay() {
	//
	//	}
}
