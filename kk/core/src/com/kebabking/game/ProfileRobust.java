package com.kebabking.game;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.kebabking.game.Managers.Manager;
import com.kebabking.game.Purchases.LocationType;
import com.kebabking.game.Purchases.PurchaseType;
import com.kebabking.game.Purchases.Purchaseable;


public class ProfileRobust {
	static final int FIRST_LEVEL_EXP = 90;
	static final double EXP_GROWTH_RATE = 1.15;
	static final float EXP_RATE = 1f;
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
	KebabKing master;
	
	// Don't tag these, instead kryo load and initialize separately. 
	ProfileSettings settings; 		// persistent game settings
	ProfileInventory inventory; 	// items that have been purchased, and consumables that are active!
	ProfileStats stats;				// stats about the user
	
	@Tag(0) private float cash; 			// current cash
	@Tag(1) private int coins; 				// special currency!
	
	@Tag(2) private int exp = 0;
	@Tag(3) private int level = 1;
	
	@Tag(4) public long shutdownAt;
	
	// This needs to be stored
	@Tag(5) float[] pastFiveDaysReputation;
	
	// updated based on past few days, calculated on the fly based on current location
	float currentReputation;	
	
	// Kryo
	public ProfileRobust() {
		init();
		if (KebabKing.LVL_50) {
			level = 50;
		}		
		// TODO calculate customerPatienceFactor
		// TODO calculate currentReputation from past 5 days
	}
	
	// create a totally new Profile
	public ProfileRobust(KebabKing master) {
		init();
		this.master = master;
		this.cash = STARTING_CASH;
		this.coins = STARTING_JADE;
		
		if (KebabKing.RICH_MODE) {
			this.cash = 999999;
			this.coins = 9999;
		}
		if (KebabKing.LVL_50) {
			level = 50;
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
//			System.out.println("EXP needed for " + i + ": " + EXP_TABLE[i] + " (" + total +")");
//			total += EXP_TABLE[i];
//		}
	}
	
	public void initializeAfterLoad(KebabKing master) {
		this.master = master;
		
		updateReputation();

		// initialize any fields that were recently added.
		
		inventory.initializeAfterLoad();
		settings.initializeAfterLoad();
		stats.initializeAfterLoad();
	}
	
	// only used at the end of the day
	public void giveMoney(float money) {
		validateMoney();
		this.cash += money;
		validateMoney();
	}

	public void giveCoins(int coins) {
		validateCoins();
		this.coins += coins;
		validateCoins();
	}
	
	public long getViolationSecondsRemaining() {
		if (shutdownAt <= 0) return 0;
		System.out.println("Get violations: " + shutdownAt);
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
	
	// the most important method of ProfileRobust
	// at the end of the day, we need to update everything in the profile (including stats/exp) and save it.
	// TODO move everything from summary screen constructor to here.
	// only update stats at end of day, otherwise 
	public void endDay(KitchenScreen screen) {
		stats.daysWorked++;

	
		// unlock everything that should be unlocked based on days?
		// two levels of unlock - unlock by round, then unlock by money?
		
		master.save();		
	}
	
	void updateReputation() {
		// calculate new current reputation
		float sum = 0;
		for (float rep : pastFiveDaysReputation) {
			sum += rep;
		}
		sum /= pastFiveDaysReputation.length;

		System.out.println(currentReputation);

		currentReputation = sum;
		
		System.out.println("NEW REPUTATION: " + currentReputation);
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
	
	public int getCurrentRound() {
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
			System.out.println("grill is null!!!");
			return 0;
		}
		return inventory.grillSize.getSize();
	}
	
//	public void setLocation(Location location) {
//		this.inventory.location = location;
//	}

	public void subtractDailyExpenses() {
		float dailyExpenses = 0;
		dailyExpenses += inventory.locationType.getCurrentSelected().getDailyCost();
		dailyExpenses += inventory.drinkQuality.getCurrentSelected().getDailyCost();
		dailyExpenses += inventory.meatQuality.getCurrentSelected().getDailyCost();
//		dailyExpenses += inventory.
		this.cash -= dailyExpenses;
//		this.updateCashString();
	}
	
	public LocationType.Location getLocation() {
		return (LocationType.Location) this.inventory.locationType.getCurrentSelected();
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
//		updateCoinsString();
	}
	
	public void spendCash(float cash) {
		this.cash -= cash;
		if (cash < 0) throw new java.lang.AssertionError();
//		updateCashString();
	}
	
	public TextureRegion getLocationBG() {
//		return this.inventory.
		return inventory.locationType.getBG();
	}
	
	public void giveExp(int exp) {
//		System.out.println("Granting " + exp + " exp");
		this.exp += exp * EXP_RATE;
		if (this.exp > this.getNextExp()) this.levelUp();
	}
	
	private void levelUp() {
		while (this.exp > this.getNextExp()) {
			this.level++;
			this.exp -= this.getNextExp();
			
			System.out.println("You are now at level " + level + "!");

			Manager.analytics.sendEventHit("Player", "Level " + level + " reached at round", "", (long) this.getCurrentRound());

			LinkedList<Purchaseable> unlocked = new LinkedList<Purchaseable>();
			for (Purchaseable p : PurchaseType.allPurchaseables) {
				if (p.unlockAtLevel() == this.level) {
					// TODO enable
					unlocked.add(p);
					master.store.storeScreen.updateTableFor(p);
//					master.store.storeScreen.initializeTables();
				}
			}
			if (unlocked.size() > 0) 
				DrawUI.launchUnlockNotification(unlocked);
		}
	}
	
	public int getNextExp() {
		return EXP_TABLE[level+1];
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
}
