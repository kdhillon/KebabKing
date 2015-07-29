package com.chuanrchef.game;

import java.io.FileNotFoundException;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.chuanrchef.game.Purchases.Inventory;


public class Profile {
	static final float STARTING_CASH = 50;
	static final int STARTING_COINS = 5;
	static final float MAX_MONEY = 100000;
	static final float MIN_MONEY = 0;
	static final float START_REP = 2.5f;
		
	transient ChuanrC master;
	
	Settings settings; 		// persistent game settings
	Inventory inventory; 	// items that have been unlocked or purchased!

	boolean tutorialNeeded;
	float cash; 			// current cash
	int coins; 				// special currency!
	int daysWorked;
	float customerPatienceFactor = 1;
	float[] pastFiveDaysReputation = {START_REP, START_REP, START_REP, START_REP, START_REP};
	float currentReputation;
	float sicknessFactor;
	
	public Profile() {
	}
	
	// create a totally new Profile
	public Profile(ChuanrC master) {
		this.master = master;
		this.cash = STARTING_CASH;
		this.coins = STARTING_COINS;
		this.daysWorked = 0;
		this.currentReputation = START_REP;
		this.tutorialNeeded = true;
		
		// for now, disable tutorial
//		this.tutorialNeeded = false;
		
		this.inventory = new Inventory(this);
		this.settings = new Settings();
	}
	
	// only used at the end of the day
	public void giveMoney(float money) {
		validateMoney();
		this.cash += money;
		validateMoney();
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
	}
	
	public float getCurrentReputation() {
		return currentReputation;
	}
	
	public void validateMoney() {
		if (this.cash < MIN_MONEY || this.cash > MAX_MONEY) {
			throw new java.util.InputMismatchException("money out of range");
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
	}
	
	public Location getLocation() {
		return (Location) this.inventory.locationType.getCurrentSelected();
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
	}
	
	public void spendCash(float cash) {
		this.cash -= cash;
		if (cash < 0) throw new java.lang.AssertionError();
	}
	
	public TextureRegion getLocationBG() {
//		return this.inventory.
		return inventory.locationType.getBG();
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
