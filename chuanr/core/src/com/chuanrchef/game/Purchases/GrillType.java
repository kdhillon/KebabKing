package com.chuanrchef.game.Purchases;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

// this class contains the user's current grill type
public class GrillType implements PurchaseType {	
	static String grillTypeDescription = "Change your grill type!";
	
	// Specific types that you might own 
	enum Type implements Purchaseable{
		// name, initial cost, daily cost, description
		Regular("Standard", 	0,	 0, 	"Just your usual charcoal grill!"), 
		Gas		("Gas", 		150, 5, 	"Cooks kebabs faster than charcoal! Don't burn them!"),
		Storage("Warm", 		500, 10, 	"Adds a warmer section to your grill"), 
		Control("Flame Control", 1000, 25, 	"Gives you total control over grill heat!");
		
		String name; 
		float initialCost;
		float dailyCost;
		String description;
		
		private Type(String name, float initialCost, float dailyCost, String description) {
			this.name = name;
			this.initialCost = initialCost;
			this.dailyCost = dailyCost;
			this.description = description;
		}
		@Override
		public String getName() {
			return this.name;
		}
		@Override
		public int coinsToUnlock() {
			return 0;
		}
		@Override
		public float cashToUnlock() {
			return this.initialCost;
		}
		@Override
		public float getDailyCost() {
			return this.dailyCost;
		}
		@Override
		public TextureRegion getIcon() {
			return null;
		}
		@Override
		public String getDescription() {
			return this.description;
		}
	};

	Type currentType;
	ArrayList<Type> unlocked;
	
	// create default grill type
	public GrillType() {
		this.unlocked = new ArrayList<Type>();
		this.unlock(Type.Regular);
	}

	@Override
	public String getName() {
		return "Grill Type";
	}
	
	@Override
	public String getDescription() {
		return grillTypeDescription;
	}

	@Override
	public TextureRegion getIcon() {
		return null;
	}

	@Override
	public Purchaseable getCurrentSelected() {
		return currentType;
	}

	@Override
	public boolean unlocked(Purchaseable purchaseable) {
		return unlocked.contains(purchaseable);
	}

	@Override
	public void setCurrent(Purchaseable newCurrent) {
		if (!this.unlocked(newCurrent)) throw new java.lang.AssertionError();
		this.currentType = (Type) newCurrent;
	}

	@Override
	public void unlock(Purchaseable toUnlock) {
		this.unlocked.add((Type) toUnlock);
		this.setCurrent(toUnlock);
	}

	@Override
	public Purchaseable getNext(Purchaseable current, boolean left) {
		Type[] values = Type.values();
		
		int currentIndex = -1;
		for (int i = 0; i < values.length; i++) {
			if (values[i] == current) currentIndex = i;
		}
		int nextIndex;
		
		if (left) {
			nextIndex = currentIndex - 1;
			if (nextIndex < 0) nextIndex = values.length - 1;
		}
		else {
			nextIndex = currentIndex + 1;
			if (nextIndex > values.length - 1) nextIndex = 0;
		}
		
		return values[nextIndex];
	}
}

