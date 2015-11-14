package com.chuanrchef.game.Purchases;

import java.util.HashSet;
import java.util.jar.JarException;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.chuanrchef.game.Assets;
import com.chuanrchef.game.StoreScreen;


// Allows other purchase types to extend this for simplicity
public class PurchaseType {
	
	// might cause kryo errors
	public transient Inventory inventory;
	protected String name = "Default name";
	protected String description = "Default description";
	protected HashSet<Purchaseable> unlocked; 	// items that have been fully unlocked and can be purchased
	protected HashSet<Purchaseable> roundUnlocked; // items that can be unlocked based on round
	protected Purchaseable currentQuality;
	protected Purchaseable[] values; // contains Purchaseable.values()
	protected TextureRegion icon;
	
	// TODO, actually use this in implementation
	protected boolean allowMultipleSelect; // can multiple purchaseables be "selected" at once?
	protected HashSet<Purchaseable> selected;
	
	// Only used for updating store screen on unlock
	private transient StoreScreen storeScreen;
	private transient StoreScreen.TableType storeTable;
	
	// for kryo
	public PurchaseType() {}
	
	// create a new one
	public PurchaseType(Inventory inventory, String name, String description, String textureRegionName, Purchaseable[] values) {
		this.inventory = inventory;
		unlocked = new HashSet<Purchaseable>();
		roundUnlocked = new HashSet<Purchaseable>();
		this.name = name;
		this.description = description;
		if (textureRegionName != null)
			this.icon = Assets.getTextureRegion(textureRegionName);
		setValues(values);
	}
	
	public void allowMultipleSelect() {
		this.allowMultipleSelect = true;
		selected = new HashSet<Purchaseable>();
	}
	
//	@Override
	public String getName() {
		return name;
	}

//	@Override
	public String getDescription() {
		return description;
	}

//	@Override
	public TextureRegion getIcon() {
		return null;
	}

//	@Override
	public Purchaseable getCurrentSelected() {
		return currentQuality;
	}
	
//	@Override
	public boolean availableForUnlock(Purchaseable purchaseable) {
		return roundUnlocked.contains(purchaseable);
	}

//	@Override
	public boolean unlocked(Purchaseable purchaseable) {
		return unlocked.contains(purchaseable);
	}

//	@Override
	public void setCurrent(Purchaseable newCurrent) {
		if (!unlocked(newCurrent)) throw new java.lang.AssertionError();
		this.currentQuality = newCurrent;
	}
	
	public void addToCurrent(Purchaseable toSelect) {
		if (!unlocked(toSelect)) throw new java.lang.AssertionError();
		this.selected.add(toSelect);
	}
	
	public void removeFromCurrent(Purchaseable toRemove) {
		if (!unlocked(toRemove)) throw new java.lang.AssertionError();
		this.selected.remove(toRemove);
	}
	
//	@Override
	public void unlockByRound(Purchaseable toUnlock) {
		this.roundUnlocked.add(toUnlock);
		
		if (this.storeScreen != null)
			this.storeScreen.resetTable(storeTable);
		// TODO make an announcement!
	}

//	@Override
	public void unlock(Purchaseable toUnlock) {
		if (!availableForUnlock(toUnlock)) throw new java.lang.AssertionError();
		this.unlocked.add(toUnlock);
		this.setCurrent(toUnlock);
	}
	
//	@Override
	public Purchaseable getNext(Purchaseable current, boolean left) {		
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
	
	public boolean checkUnlockedRound(Purchaseable p) {
		if (inventory.hasUnlockedByRound(p)) {
			unlockByRound(p);
			return true;
		}
		return false;
	}
	
	private void setValues(Purchaseable[] values) {
//		System.out.println("set values");
		this.values = values;
		for (Purchaseable p : values) {
			if (p == null) throw new java.lang.NullPointerException();
			if (checkUnlockedRound(p)) {
//				System.out.println(p.getName() + " added to unlockable");
			}
//			else System.out.println("Can't unlock " + p.getName() + " yet, required round is " + p.unlockAtRound());
		}
	}
	
	public void setTable(StoreScreen screen, StoreScreen.TableType table) {
		this.storeScreen = screen;
		this.storeTable = table;
	}
}
