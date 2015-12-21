package com.kebabking.game;

import com.kebabking.game.Customer.CustomerType;

public class DailyGoal {
	// TODO determine how meat goals should scale
	static int MAX_MEAT_TOTAL = 40;
	static int MIN_MEAT_TOTAL = 30;
	
	static int MAX_BEER = 15;
	static int MIN_BEER = 5;
	
	static int MAX_CUSTOMERS = 10;
	static int MIN_CUSTOMERS = 5;
	
	static int MAX_REWARD = 30;
	static int MIN_REWARD = 10;
	
	// example goals:
	
	// serve 10 customers
	// serve 5 spicy beef chuanr
	// serve 20 total chuanr
	// serve 5 beer
	
	// TODO later
	// maintain an average customer rating of 4.
	// don't have any customer less than 4
	// fully satisfy 3 customesr

	
	enum GoalType{
		NUM_CUSTOMERS,
//		AVG_RATING,
		NUM_MEAT_SPECIFIC,
		NUM_MEAT_TOTAL,
		NUM_BEER,
//		CUSTOMER_TYPE,
	}
	
	GoalType thisGoalType;
	
	int progressToGoal;
	int goal;
	Meat.Type meatType;
	CustomerType customerToServe;
	
	float reward;		// what is the reward for meeting this goal
	boolean satisfied; 	// has the goal been met
	
	String string;
	
	public DailyGoal() {
		GoalType[] array = GoalType.values();
		int length = array.length;
		
		int index = (int) (Math.random() * length);
		thisGoalType = array[index];
		
		switch(thisGoalType) {
		case NUM_CUSTOMERS:
			goal = (int) (MIN_CUSTOMERS + Math.random()*(MAX_CUSTOMERS - MIN_CUSTOMERS));
			this.string = "Serve " + goal +" customers!";
			break;
		case NUM_MEAT_SPECIFIC:
			Meat.Type[] typeArray = Meat.Type.values();
			int meatIndex = (int) (Math.random() * typeArray.length);
			this.meatType = typeArray[meatIndex];
			this.goal = generateMeatTotalGoal() / 3;
			this.string = "Serve " + this.goal + " " + this.meatType + " kebabs!";
			break;
		case NUM_MEAT_TOTAL:
			this.goal = generateMeatTotalGoal();
			this.string = "Serve " + this.goal + " kebabs!";
			break;
		case NUM_BEER:
			this.goal = generateBeerGoal();
			this.string = "Serve " + this.goal + " beer!";
			break;
//		case CUSTOMER_TYPE:
//			this.goal = generateBeerGoal();
//			// generate customer type based on location
//			
////			CustomerType[] ctOptions =;
////			int customerIndex = (int) (Math.random() * ctOptions.length);
//			
//			
//			this.string = "Serve " + this.goal + " customers!";
//			break;
		}
		
		this.generateReward();
	}
	
	public void updateGoal(KitchenScreen kitchen) {
		
	}
	
	public String toString() {
		return string;
	}
	
	private int generateMeatTotalGoal() {
		return (int) ((Math.random() * (MAX_MEAT_TOTAL - MIN_MEAT_TOTAL)) + MIN_MEAT_TOTAL);
	}
	
	private int generateBeerGoal() {
		return (int) ((Math.random() * (MAX_BEER - MIN_BEER)) + MIN_BEER);
	}
	
	private void generateReward() {
		reward = (int) (Math.random()*(MAX_REWARD - MIN_REWARD)) + MIN_REWARD;
	}
}
