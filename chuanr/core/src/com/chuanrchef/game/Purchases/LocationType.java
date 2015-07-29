package com.chuanrchef.game.Purchases;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.chuanrchef.game.Location;

public class LocationType implements PurchaseType {
	Location currentLocation; // this is the enum with relevant information
	
	ArrayList<Location> unlocked;
	
	public LocationType() {
		unlocked = new ArrayList<Location>();
		this.unlock(Location.Village);
	}
	
	@Override
	public String getName() {
		return "Location";
	}
	
	@Override
	public String getDescription() {
		return "Choose where you want to sell kebabs!";
	}

	@Override
	public TextureRegion getIcon() {		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Purchaseable getCurrentSelected() {
		return currentLocation;
	}


	@Override
	public boolean unlocked(Purchaseable purchaseable) {
		return this.unlocked.contains(purchaseable);
	}


	@Override
	public void setCurrent(Purchaseable newCurrent) {
		if (!this.unlocked((Location) newCurrent)) throw new java.lang.AssertionError();
		this.currentLocation = (Location) newCurrent;
	}

	@Override
	public void unlock(Purchaseable toUnlock) {
		this.unlocked.add((Location) toUnlock);
		this.setCurrent((Location) toUnlock);
	}

	
	public TextureRegion getBG() {
		return this.currentLocation.getBG();
	}
	
	@Override
	public Purchaseable getNext(Purchaseable current, boolean left) {
		Location[] values = Location.values();
		
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
