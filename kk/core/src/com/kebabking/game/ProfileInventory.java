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
import com.kebabking.game.Purchases.MeatTypes;
import com.kebabking.game.Purchases.PurchaseType;
import com.kebabking.game.Purchases.PurchaseTypeConsumable;
import com.kebabking.game.Purchases.Purchaseable;
import com.kebabking.game.Purchases.SimpleConsumable;
import com.kebabking.game.Purchases.SkewerType;

public class ProfileInventory {	
	// class that contains all information about purchased items
	// remember to make this expandable, so that there are no problems when updates are released
	public Profile profile;

	@Tag(101) public MeatQuality meatQuality;
	@Tag(102) public DrinkQuality drinkQuality;

	@Tag(103) public SkewerType skewerType;

	@Tag(104) public GrillSize grillSize;

	@Tag(105) public GrillType grillType;

	@Tag(106) public GrillStand grillStand;

	@Tag(107) public LocationType locationType;

	@Tag(108) public AdCampaign adCampaign;
	
	// multiple meat types can be selected
	@Tag(111) public MeatTypes meatTypes;
	
	// fix saving bug by:
	// first trying to get the save off the phone with ddms in android studio. 
	
	// testing tags, won't be saved.
	@Deprecated	@Tag(109) public AdCampaign unused;
	@Deprecated @Tag(110) public String fake;

	// no-arg constructor for kryo
	public ProfileInventory() {
//		VanityItem.initialize();
	}

	// create a blank purchases
	public ProfileInventory(Profile profile) {
//		VanityItem.initialize();

		this.profile = profile;

		meatQuality = new MeatQuality(this);

		meatTypes = new MeatTypes(this);
		
		drinkQuality = new DrinkQuality(this);

		locationType = new LocationType(this);

		grillSize = new GrillSize(this);

		grillType = new GrillType(this);

		adCampaign = new AdCampaign(this);

		//		decorations = new VanityDecorationType(this);

		grillStand = new GrillStand(this);

		skewerType = new SkewerType(this);
	}

	public void initializeAfterLoad(Profile profile) {
		// Anything that wasn't in the first generation of purchasetypes should be checked and initialized here
		if (meatTypes == null) {
			meatTypes = new MeatTypes(this);
		}
		if (grillType == null) {
			grillType = new GrillType(this);
		}

		if (profile.stats.daysWorked >= 2 && !meatTypes.isUnlocked(MeatTypes.Type.values[1])) {
			meatTypes.unlock(MeatTypes.Type.values[1]);
		}
		if (profile.stats.daysWorked >= 3 & !meatTypes.isUnlocked(MeatTypes.Type.values[2])) {
			meatTypes.unlock(MeatTypes.Type.values[2]);
		}
		
		// do this to make sure no lingering ad campaigns are drawn
		if (adCampaign.getActive() != null)
			adCampaign.update();
		
		this.profile = profile;
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

	public void spendCashOnStore(float cash) {
		profile.spendCash(cash);
		StatsHandler.spendCash(cash);
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
		if (type.isUnlocked(item)) return false;
		
		SoundManager.playUnlock();
		
		// unlock item
		type.unlock(item);

		Manager.analytics.sendEventHit("Inventory", "Unlock", item.getName());

		// spend money
		if (item.coinsToUnlock() > 0) {
			spendCoins(item.coinsToUnlock());
		}
		if (item.cashToUnlock() > 0) { 
			spendCashOnStore(item.cashToUnlock());
		}
		
		StatsHandler.makePurchase();
		if (type == locationType) StatsHandler.upgradeLocations();

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
			this.spendCashOnStore(item.cashToActivate());
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

		StatsHandler.purchaseConsumable();
		if (type == adCampaign) StatsHandler.launchAdCampaign();
		
		// save game
		profile.save();

		return true;
	}

//	public void handleConsumableReset(PurchaseTypeConsumable type) {
//		if (type == this.adCampaign) {
//			profile.master.cm.resetCustomerDistribution();
//			profile.master.store.storeScreen.updateAfterConsumableReset(this.adCampaign);
//		}
//	}

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

	// unlock second meat option without asking player
	public void forceSecondBoxUpdate() {
		System.out.println("forcing second box update");
		meatTypes.unlock(MeatTypes.Type.values[1]);
//		profile.master.store.storeScreen.foodTable.updatePurchaseableForUnlock(MeatTypes.Type.values[1]);
//		meatTypes.addToSelected(1);
	}
	
	public void forceThirdBoxUpdate() {
		meatTypes.unlock(MeatTypes.Type.values[2]);
//		profile.master.store.storeScreen.foodTable.updatePurchaseableForUnlock(MeatTypes.Type.values[2]);
//		meatTypes.addToSelected(1);
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
