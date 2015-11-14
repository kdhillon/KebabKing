package com.chuanrchef.game;

import java.util.ArrayList;

import com.chuanrchef.game.Customer.CustomerType;

public class Order {
	// represents a food order 
	// too keep things simpler, allow either only spicy or not, and max two types 
	int chicken;
	int beef;
	int lamb;
	
	boolean chickenSpicy;
	boolean beefSpicy;
	boolean lambSpicy;
	
	int beer;
	int total;
	
	int remaining;
	int raw; 			 // how many raw meats were given to this person
	int burnt; 			 // how many chuanrs were burnt
	int incorrect;		 // how many wrong chuanrs/beers were given to this person	
	
	// generate a totally random order based on the given type
	public Order(CustomerType type) {
		chickenSpicy = (Math.random() < .5);
		beefSpicy = (Math.random() < .5);
		lambSpicy = (Math.random() < .5);
		
		int min = type.minOrder;
		int max = type.maxOrder;
		
		// just the meat
		total = (int) (Math.random() * (max-min) + min);
		

		int count1 = (int) (Math.random() * total);
		int count2 = total-count1;
				
		double random = Math.random();
		// no chicken
		if (random < .33) {
			beef = count1;
			lamb = count2;
		}
		// no beef
		else if (random < .66) {
			chicken = count1;
			lamb = count2;
		}
		// no lamb
		else {
			chicken = count1;
			beef = count2;
		}
		
		// because Val wanted it.
		chicken /= 2;
		
		double beerRandom = Math.random();
		if (beerRandom < type.beerFactor) beer = 1;
		if (beerRandom * 2 < type.beerFactor) beer = 2;
		if (beerRandom * 3 < type.beerFactor) beer = 3;
		
		total = chicken + beef + lamb + beer;
		remaining = total;
	}
	
	// returns total money received from this 
	public float giveMeat(ArrayList<Meat> set) {
		float revenue = 0;
		for (Meat m : set) revenue += giveMeat(m);
		System.out.println(incorrect);
		return revenue;
	}
	
	public float giveMeat(Meat meat) {
		switch (meat.type) {
		case CHICKEN:
			if (meat.spiced != chickenSpicy || chicken == 0) {
				System.out.println("wrong chicken");
				incorrect++;
				return 0;
			}
			break;
		case BEEF:
			if (meat.spiced != beefSpicy || beef == 0) {
				System.out.println("wrong beef");
				incorrect++;
				return 0;
			}
			break;
		case LAMB:
			if (meat.spiced != lambSpicy || lamb == 0) {
				System.out.println("wrong lamb");
				incorrect++;
				return 0;
			}
		}
		
		Meat.Type type = meat.type;
		if (type == Meat.Type.CHICKEN)
			chicken--;
		else if (type == Meat.Type.BEEF)
			beef--;
		else if (type == Meat.Type.LAMB)
			lamb--;
		
		remaining--;
		if (meat.state == Meat.State.BURNT) burnt++;
		if (meat.state == Meat.State.RAW) raw++;
		
		return Meat.getSellPrice(meat.type) - Meat.getBuyPrice(meat.type);
	}
	
//	public float giveBeer(int count) {
//		float price = 0;
//		for (int i = 0; i < count; i++) {
//			price += giveBeer();
//		}
//		return price;
//	}
	
	public float giveBeer() {
		System.out.println("giving beer!");
		if (beer == 0) {
			incorrect++;
			return 0;
		}
		beer--;
		remaining--;
		return KitchenScreen.BEER_SELL_PRICE;
	}
	
	/** 
	 * Print the order in an easy to read format
	 */
	public String toString() {
		StringBuilder str = new StringBuilder("");
		
		if (chicken > 0) {
			if (chickenSpicy)
				str.append(chicken + " sp chicken, \n");
			else
				str.append(chicken + " chicken, \n");
		}
		if (beef > 0) {
			if (beefSpicy)
				str.append(beef + " sp beef, \n");
			else
				str.append(beef + " beef, \n");
		}
		if (lamb > 0) {
			if (lambSpicy)
				str.append(lamb + " sp lamb \n");
			else
				str.append(lamb + " lamb \n");
		}
		
		if (beer > 0)
			str.append(beer + " beer.");
		return str.toString();
	}
	
	// returns number of different types of items remaining (1-3)
	public int getTotalTypes() {
		int types = 0;
		if (chicken > 0) types++;
		if (beef > 0) types++;
		if (lamb > 0) types++;
		
		if (beer > 0) types++;
		return types;
	}
}
