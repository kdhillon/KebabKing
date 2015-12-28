package com.kebabking.game;

import java.io.FileNotFoundException;
import java.util.Arrays;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kebabking.game.Purchases.Inventory;
import com.kebabking.game.Purchases.LocationType;
import com.kebabking.game.Purchases.PurchaseType;
import com.kebabking.game.Purchases.Purchaseable;


public class Profile {
	static final float STARTING_CASH = 99999;
	static final int STARTING_COINS = 999;
	static final float MAX_MONEY = Float.POSITIVE_INFINITY;
	static final float MIN_MONEY = 0;
	static final int MIN_COINS = 0;
	static final int MAX_COINS = Integer.MAX_VALUE;
	static final float START_REP = 2.5f;
		
	static final int MAX_LEVEL = 1000;
	
	static int[] EXP_TABLE;

	static final float EXP_RATE = 1;
		
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
	int level = 1;
	
	float customerPatienceFactor = 1;
	float[] pastFiveDaysReputation = {START_REP, START_REP, START_REP, START_REP, START_REP};
	float currentReputation;
	float sicknessFactor;
	
	float[] currentCustomerSpread; 		// modified during ad campaigns
	public float boost;						// what is the percent increase in total people, after normalization?
	
	public Profile() {
		init();
	}
	
	// create a totally new Profile
	public Profile(KebabKing master) {
		init();
		this.master = master;
		this.cash = STARTING_CASH;
		this.coins = STARTING_COINS;
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
	}

	public void updateCoinsString() {
		this.coinsString = coins + "";
	}
	public void updateCashString() {
		this.cashString = cash + "";
	}
	public static void init() {
		EXP_TABLE = new int[MAX_LEVEL];
		EXP_TABLE[0] = 100;
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
	
	public void updateRepuation(float dayReputation) {
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

	public void resetCustomerDistribution() {
		this.currentCustomerSpread = Arrays.copyOf(this.getLocation().originalCustomerSpread, this.getLocation().originalCustomerSpread.length);
		this.boost = 1;
	}
	
	public void updateCustomerDistribution(Customer.CustomerType type, float bonus) {
		this.currentCustomerSpread = Arrays.copyOf(this.getLocation().originalCustomerSpread, this.getLocation().originalCustomerSpread.length);
		int nonZeroCustomers = 0;
		for (int i = 0; i < currentCustomerSpread.length; i++) {
			if (currentCustomerSpread[i] != 0) nonZeroCustomers++;
		}
		
		if (type != null) {
			int i = Customer.getIndexOf(type);
			currentCustomerSpread[i] *= bonus;
			this.boost = bonus / nonZeroCustomers;
		}
		else {
			for (int i = 0; i < currentCustomerSpread.length; i++) {
				currentCustomerSpread[i] *= bonus;
			}
			this.boost = bonus;
		}
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
		System.out.println("Granting " + exp + " exp");
		this.exp += exp * EXP_RATE;
		if (this.exp > this.getNextExp()) this.levelUp();
	}
	
	private void levelUp() {
		while (this.exp > this.getNextExp()) {
			this.level++;
			this.exp -= this.getNextExp();
			
			System.out.println("You are now at level " + level + "!");
			for (Purchaseable p : PurchaseType.allPurchaseables) {
				if (p.unlockAtLevel() == this.level) {
					DrawUI.addToUnlockDisplayQueue(p);
				}
			}
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
