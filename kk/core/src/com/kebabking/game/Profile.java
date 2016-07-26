package com.kebabking.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.kebabking.game.Managers.Manager;
import com.kebabking.game.Purchases.LocationType;
import com.kebabking.game.Purchases.PurchaseType;
import com.kebabking.game.Purchases.Purchaseable;


public class Profile {
	static final int FIRST_LEVEL_EXP = 90;
	static final double EXP_GROWTH_RATE = 1.15;
	static final float EXP_RATE = 1.25f; // was 1.33 for v 1.0
	static final int MAX_LEVEL = 1000;

	static final float STARTING_CASH = 50;
	static final int STARTING_JADE = 5;
	static final float MAX_MONEY = Float.POSITIVE_INFINITY;
	static final float MIN_MONEY = 0;
	static final int MIN_COINS = 0;
	static final int MAX_COINS = Integer.MAX_VALUE;
	static final float START_REP = 2.5f;
	
	// created during init
	static int[] EXP_TABLE;
	
	// don't need transient tag anymore
	public KebabKing master;
	
	// Don't tag these, instead kryo load and initialize separately. 
	ProfileSettings settings; 		// persistent game settings
	ProfileInventory inventory; 	// items that have been purchased, and consumables that are active!
	public ProfileStats stats;				// stats about the user
	
	@Tag(0) private float cash; 			// current cash
	@Tag(1) private int coins; 				// special currency!
	
	@Tag(2) private int exp = 0;
	@Tag(3) private int level = 1;
	
	@Tag(4) public long shutdownAt;
	
	// This needs to be stored
	@Tag(5) float[] pastFiveDaysReputation;

	@Tag(6) public boolean gameQuitDuringShare;
	@Tag(7) public boolean gameQuitDuringAd;
	
	// enable this after ad watched, disable after rewarded after spin.
	@Tag(8) public boolean canSpin;
	
	// updated based on past few days, calculated on the fly based on current location
	float currentReputation;	
	
	// Kryo
	public Profile() {
		init();
		if (KebabKing.LVL_50) {
			level = 50;
		}
	}
	
	// create a totally new Profile
	public Profile(KebabKing master) {
		init();
		this.master = master;
		this.cash = STARTING_CASH;
		this.coins = STARTING_JADE;
		
		if (KebabKing.RICH_MODE) {
			this.cash = 1000;
			this.coins = 350;
		}
		if (KebabKing.LVL_50) {
			level = 48;
		}
		
		pastFiveDaysReputation = new float[] {START_REP, START_REP, START_REP, START_REP, START_REP};
		
		updateReputation();
		
		// DON'T initialize these here. 
		// If kryo fails to load this profile, we should try to load inventory, stats, and settings separately.
//		this.stats = new ProfileStats();
//		this.inventory = new ProfileInventory(this);
//		this.settings = new ProfileSettings();
		
//		resetCustomerDistribution();
	}
	
	public static void init() {
		EXP_TABLE = new int[MAX_LEVEL];
		EXP_TABLE[0] = FIRST_LEVEL_EXP;
		for (int i = 1; i < MAX_LEVEL; i++) {
			EXP_TABLE[i] = (int) (EXP_TABLE[i-1] * EXP_GROWTH_RATE);
		}
		
		// figure out how much cash someone will have at a certain level
//		int total = 0;
//		for (int i = 0; i < 100; i++) {
//			KebabKing.print("EXP needed for " + i + ": " + EXP_TABLE[i] + " (" + total +")");
//			total += EXP_TABLE[i];
//		}
	}
	
	public void initializeAfterLoad(KebabKing master) {
		this.master = master;
		
		updateReputation();

		// initialize any fields that were recently added.
		
		inventory.initializeAfterLoad(this);
		settings.initializeAfterLoad();
		stats.initializeAfterLoad(this);

		KebabKing.print("Quit during ad: " + gameQuitDuringAd);
		KebabKing.print("Quit during share: " + gameQuitDuringShare);

		// these booleans are for dealing with cases where game crashes during sharing or ads
		// for now, we just assume the user watched/shared successfully
		if (gameQuitDuringAd) {
			AdsHandler.handleAdJustWatched();
		}
		if (gameQuitDuringShare) {
			SocialMediaHandler.shareSuccess();
			StatsHandler.shareCrashedButRewarded();
		}
		KebabKing.print("can spin: " + canSpin);
		
		if (this.exp < 0) this.exp = 0;
	}
	
	// only used at the end of the day
	public void giveMoney(float money) {
		if (money <= 0) return;
		validateMoney();
		this.cash += money;
		validateMoney();
//		master.store.storeScreen.updateAll();
		SoundManager.playCash();
	}

	public void giveCoins(int coins) {
		if (coins <= 0) return;
		validateCoins();
		this.coins += coins;
		validateCoins();
//		master.store.storeScreen.updateAll();
		StatsHandler.earnJade(coins);
		SoundManager.playCoin();
	}
	
	public long getViolationSecondsRemaining() {
		if (shutdownAt <= 0) return 0;
		return (long) (shutdownAt - System.currentTimeMillis()) / 1000 + KebabKing.SHUTDOWN_LENGTH_SECONDS;
	}
	
	public boolean violationActive() {
		// hacky fix
		if (getViolationSecondsRemaining() > KebabKing.SHUTDOWN_LENGTH_SECONDS + 1000) {
			shutdownAt = 0;
		}
		return (getViolationSecondsRemaining() > 0);
	}
	
	public void endViolation() {
		// TODO show notification that ad was watched successfully
//		master.profile.spendCoins(BRIBE_COST);
		shutdownAt = Long.MAX_VALUE;
		DrawUI.handleViolationEnded();
	}
	
	void updateReputation() {
		// calculate new current reputation
		float sum = 0;
		for (float rep : pastFiveDaysReputation) {
			sum += rep;
		}
		sum /= pastFiveDaysReputation.length;

		KebabKing.print(currentReputation);

		currentReputation = sum;
		
		KebabKing.print("NEW REPUTATION: " + currentReputation);
	}
	
	private void updateReputationAndStars() {
		updateReputation();
		TopBar.updateStars(getCurrentReputation());
	}
	
	public void addRepuation(float dayReputation) {
		// slide everything back
		for (int i = 0; i < pastFiveDaysReputation.length - 1; i++) {
			pastFiveDaysReputation[i] = pastFiveDaysReputation[i+1];
		}
		pastFiveDaysReputation[4] = dayReputation;
		
		updateReputationAndStars();
	}
	
	public float getCurrentReputation() {
		return currentReputation;
	}
	
	public int getCurrentDay() {
		return this.stats.daysWorked + 1;
	}
	
	public void validateMoney() {
		if (this.cash < MIN_MONEY || this.cash > MAX_MONEY) {
			throw new java.util.InputMismatchException("money out of range");
		}
	}
	public void validateCoins() {
		if (this.coins < MIN_COINS || this.cash > MAX_COINS) {
			throw new java.util.InputMismatchException("cash out of range");
		}
	}
	
//	 Just big and small for now
	public int grillSize() {
		if (inventory.grillSize == null) {
			KebabKing.print("grill is null!!!");
			return 0;
		}
		return inventory.grillSize.getSize();
	}
	
//	public void setLocation(Location location) {
//		this.inventory.location = location;
//	}

	public void subtractDailyExpenses() {
		float dailyExpenses = 0;
		dailyExpenses += inventory.locationType.getFirstSelected().getDailyCost();
		dailyExpenses += inventory.drinkQuality.getFirstSelected().getDailyCost();
		dailyExpenses += inventory.meatQuality.getFirstSelected().getDailyCost();
//		dailyExpenses += inventory.
		this.cash -= dailyExpenses;
//		this.updateCashString();
	}
	
	public LocationType.Location getLocation() {
		return (LocationType.Location) this.inventory.locationType.getFirstSelected();
	}
	
	public float getCash() {
		return cash;
	}
	
	public int getCoins() {
		return coins;
	}
	
	public void spendCoins(int coins) {
		this.coins -= coins;
		if (coins < 0) throw new java.lang.AssertionError();
//		master.store.storeScreen.updateAll();
		StatsHandler.spendJade(coins);
//		updateCoinsString();
	}
	
	public void spendCash(float cash) {
		this.cash -= cash;
		if (cash < 0) throw new java.lang.AssertionError();
//		master.store.storeScreen.updateAll();
//		updateCashString();
	}
	
	public TextureRegion getLocationBG() {
//		return this.inventory.
		return inventory.locationType.getBG();
	}
	
	public void giveExp(int exp) {
		KebabKing.print("giving exp");
		if (this.exp < 0) this.exp = 0;
		if (exp <= 0) return;
//		KebabKing.print("Granting " + exp + " exp");
		this.exp += exp * EXP_RATE;
		if (this.exp > this.getNextExp()) this.levelUp();
	}
	
	private void levelUp() {
		while (this.exp > this.getNextExp()) {
			this.level++;
			this.exp -= this.getNextExp();
			if (this.exp < 0) this.exp = 0;
			
			KebabKing.print("You are now at level " + level + "!");

			Manager.analytics.sendEventHit("Player", "Level " + level + " reached at round", "", (long) this.getCurrentDay());

			for (Purchaseable p : PurchaseType.allPurchaseables) {
				if (p.unlockAtLevel() == this.level) {
					p.getType().unlockIfReady(p);
					master.store.storeScreen.updatePurchaseableAfterUnlock(p);
				}
			}
		}
		if (this.exp < 0) this.exp = 0;
		StatsHandler.onLevelUp();
	}
	
	public int getNextExp() {
		return EXP_TABLE[level+1];
	}
	
	public static int getExpNeededFor(int i) {
		return EXP_TABLE[i];
	}
	
	public int getExp() {
		return exp;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void save() {
		master.save();
	}
	
	public void letSpin() {
		canSpin = true;
	}
}
