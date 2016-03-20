package com.kebabking.game.Purchases;

public class SimpleConsumable extends SimplePurchaseable {
	public long duration;

	// for kryo
	public SimpleConsumable() {
		super();
	}
		
	public SimpleConsumable(String name, float cashToActivate, int coinsToActivate, int unlockAtLevel, int unlockWithLocation, String iconFull, long duration) {
		super(name, cashToActivate, coinsToActivate, unlockAtLevel, unlockWithLocation, iconFull);
		this.duration = duration;
	}
	
	// these are just so you never call the wrong method of this guy
	public int coinsToActivate() {
		return this.coinsToUnlock;
	}
	
	public float cashToActivate() {
		return this.cashToUnlock;
	}
	
	@Override
	public float cashToUnlock() {
//		throw new java.lang.AssertionError();		
		return cashToActivate();
	}
	
	@Override
	public int coinsToUnlock() {
//		throw new java.lang.AssertionError();
		return coinsToActivate();
	}
	
	// ASSUME DAILY COST IS 0 IF CONSUMABLE
	@Override
	public float getDailyCost() {
		throw new java.lang.AssertionError();
	}
}
