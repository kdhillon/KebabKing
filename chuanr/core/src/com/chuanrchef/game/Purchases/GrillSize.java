package com.chuanrchef.game.Purchases;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

// this class contains the user's current grill Size
public class GrillSize implements PurchaseType {	
	static String grillSizeDescription = "Change your grill Size!";
	
	// Specific Sizes that you might own 
	enum Size implements Purchaseable{
		// name, initial cost, daily cost, int size, description
		Small("Small", 		0,	 0, 6,	"Fits just 6 kebabs!"), 
		Medium("Medium", 	20, 0, 8,	"Fits 8 whole kebabs!"),
		Large("Large", 		500, 0, 10,	"Fits 10 kebabs! Wow!");
//		Huge("Huge", 		1000, 0, 12,	"Gives you total control over grill heat!");
		
		String name; 
		float initialCost;
		float dailyCost;
		int size;
		String description;
		
		private Size(String name, float initialCost, float dailyCost, int size, String description) {
			this.name = name;
			this.initialCost = initialCost;
			this.dailyCost = dailyCost;
			this.size = size;
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

	Size currentSize;
	ArrayList<Size> unlocked;
	
	// create default grill Size
	public GrillSize() {
		this.unlocked = new ArrayList<Size>();
		this.unlock(Size.Small);
	}
	
	@Override
	public String getName() {
		return "Grill Size";
	}

	@Override
	public String getDescription() {
		return grillSizeDescription;
	}

	@Override
	public TextureRegion getIcon() {
		return null;
	}

	@Override
	public Purchaseable getCurrentSelected() {
		return currentSize;
	}

	@Override
	public boolean unlocked(Purchaseable purchaseable) {
		return unlocked.contains(purchaseable);
	}

	@Override
	public void setCurrent(Purchaseable newCurrent) {
		if (!this.unlocked(newCurrent)) throw new java.lang.AssertionError();
		this.currentSize = (Size) newCurrent;
	}
	
	@Override
	public Purchaseable getNext(Purchaseable current, boolean left) {
		Size[] values = Size.values();
		
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

	@Override
	public void unlock(Purchaseable toUnlock) {
		this.unlocked.add((Size) toUnlock);
		this.setCurrent(toUnlock);
	}
}
