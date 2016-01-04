package com.kebabking.game;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kebabking.game.Managers.Manager;
import com.kebabking.game.Purchases.Inventory;
import com.kebabking.game.Purchases.LocationType;
import com.kebabking.game.Purchases.PurchaseType;
import com.kebabking.game.Purchases.Purchaseable;


public class Profile {
	static final int FIRST_LEVEL_EXP = 200;
	static final float STARTING_CASH = 50;
	static final int STARTING_JADE = 0;
	static final float MAX_MONEY = Float.POSITIVE_INFINITY;
	static final float MIN_MONEY = 0;
	static final int MIN_COINS = 0;
	static final int MAX_COINS = Integer.MAX_VALUE;
	static final float START_REP = 2.5f;
		
	static final int MAX_LEVEL = 1000;
	
	static int[] EXP_TABLE;

	static final float EXP_RATE = 1f;
		
	transient KebabKing master;
	
	Settings settings; 		// persistent game settings
	Inventory inventory; 	// items that have been unlocked or purchased!

	boolean tutorialNeeded;
	private float cash; 			// current cash
	private int coins; 				// special currency!
	public String cashString;
	public String coinsString;
	
	public int daysWorked;		// starts at 0
	
	int exp = 0;
	public int level = 1;
	
	float customerPatienceFactor = 1;
	float[] pastFiveDaysReputation = {START_REP, START_REP, START_REP, START_REP, START_REP};
	float currentReputation;
	float sicknessFactor;
	
	double[] currentCustomerSpread; 		// modified during ad campaigns
	public float boost;						// what is the percent increase in total people, after normalization?
	Customer.CustomerType[] currentBoosted;
	private float bonus; // should only be used for resettting customer distribution
	
	
	public Profile() {
		init();
	}
	
	// create a totally new Profile
	public Profile(KebabKing master) {
		init();
		this.master = master;
		this.cash = STARTING_CASH;
		this.coins = STARTING_JADE;
		
//		if (KebabKing.TEST_MODE) {
//			this.cash = 999999;
//			this.coins = 9999;
//
//		}
		
		this.updateCashString();
		this.updateCoinsString();
		this.daysWorked = 0;
		this.currentReputation = START_REP;

		// only call this when necessary! after rounds or purchases basically.

//		this.tutorialNeeded = true;
		
		// for now, disable tutorial
		this.tutorialNeeded = false;
		
		this.inventory = new Inventory(this);
		this.settings = new Settings();
				
		resetCustomerDistribution();
		
		// this will crash
		if (KebabKing.TEST_MODE) {
//			this.giveExp(1000000);
		}
	}

	public void updateCoinsString() {
		this.coinsString = coins + "";
	}
	public void updateCashString() {
		this.cashString = cash + "";
	}
	public static void init() {
		EXP_TABLE = new int[MAX_LEVEL];
		EXP_TABLE[0] = FIRST_LEVEL_EXP;
		for (int i = 1; i < MAX_LEVEL; i++) {
			EXP_TABLE[i] = (int) (EXP_TABLE[i-1] * 1.05);
		}
//		int total = 0;
//		for (int i = 0; i < 100; i++) {
//			System.out.println("EXP needed for " + i + ": " + EXP_TABLE[i] + " (" + total +")");
//			total += EXP_TABLE[i];
//		}
	}
	
	// only used at the end of the day
	public void giveMoney(float money) {
		validateMoney();
		this.cash += money;
		this.updateCashString();
		validateMoney();
	}

	public void giveCoins(int coins) {
		validateCoins();
		this.coins += coins;
		this.updateCoinsString();
		this.validateCoins();

		// always save when you receive jade, even if it's in the middle of the round
		// (TODO fix later when we add the jeweler)
		save();
	}
	
	public void endDay() {
		daysWorked++;
		// save here!

		// unlock everything that should be unlocked based on days?
		// two levels of unlock - unlock by round, then unlock by money?
		
		try {
			master.save();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void addRepuation(float dayReputation) {
		// slide everything back
		for (int i = 0; i < pastFiveDaysReputation.length - 1; i++) {
			pastFiveDaysReputation[i] = pastFiveDaysReputation[i+1];
		}
		pastFiveDaysReputation[4] = dayReputation;
		
		// calculate new current reputation
		float sum = 0;
		for (float rep : pastFiveDaysReputation) {
			sum += rep;
		}
		sum /= pastFiveDaysReputation.length;
		
		System.out.println(currentReputation);
		
		currentReputation = sum;
		DrawUI.updateStars(getCurrentReputation());
		System.out.println("NEW REPUTATION: " + currentReputation);
	}
	
	public float getCurrentReputation() {
		return currentReputation;
	}
	
	public int getCurrentRound() {
		return this.daysWorked + 1;
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
		if (inventory.grillSpecs == null) {
			System.out.println("grill is null!!!");
			return 0;
		}
		return inventory.grillSpecs.getSize();
	}
	
	// this reapplies any existing ad campaign
	public void updateCustomerDistribution() {
		if (boost == 1) resetCustomerDistribution();
		else {
			updateCustomerDistribution(currentBoosted, bonus);
		}
	}

	
	// this ends the existing ad campaign
	public void resetCustomerDistribution() {
		this.currentCustomerSpread = Arrays.copyOf(this.getLocation().originalCustomerSpread, this.getLocation().originalCustomerSpread.length);
		this.boost = 1;
		this.bonus = 1;
		this.currentBoosted = null;
	}
	
	public void updateCustomerDistribution(Customer.CustomerType[] types, float bonus) {
		this.currentCustomerSpread = Arrays.copyOf(this.getLocation().originalCustomerSpread, this.getLocation().originalCustomerSpread.length);
//		int nonZeroCustomers = 0;
//		for (int i = 0; i < currentCustomerSpread.length; i++) {
//			if (currentCustomerSpread[i] != 0) nonZeroCustomers++;
//		}
		
		this.currentBoosted = types;
		this.bonus = bonus;
		
		if (types != null) {
			for (int i = 0; i < types.length; i++) {
				int j = Customer.getIndexOf(types[i]);
				currentCustomerSpread[j] *= bonus;
			}
			
//			this.boost = 1 + (bonus - 1) / nonZeroCustomers;
			System.out.println("PROFILE: setting boost: " + boost);
		}
		else {
			for (int i = 0; i < currentCustomerSpread.length; i++) {
				currentCustomerSpread[i] *= bonus;
			}
		}
		this.boost = bonus;
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
		this.updateCashString();
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
		updateCoinsString();
	}
	
	public void spendCash(float cash) {
		this.cash -= cash;
		if (cash < 0) throw new java.lang.AssertionError();
		updateCashString();
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

			Manager.analytics.sendEventHit("Player", "Level " + level + " reached at round", "", (long)this.daysWorked);

			LinkedList<Purchaseable> unlocked = new LinkedList<Purchaseable>();
			for (Purchaseable p : PurchaseType.allPurchaseables) {
				if (p.unlockAtLevel() == this.level) {
					// TODO enable
					unlocked.add(p);
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
		try {
			master.save();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
