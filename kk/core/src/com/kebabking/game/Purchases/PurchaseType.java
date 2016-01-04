package com.kebabking.game.Purchases;

import java.util.HashSet;


// Allows other purchase types to extend this for simplicity
public class PurchaseType {
	// stores rounds at which purchaseables unlock
	public static HashSet<Purchaseable> allPurchaseables = new HashSet<Purchaseable>();
	public transient Purchaseable[] values; // contains Purchaseable.values()
//	protected transient static TextureRegion icon;
//	protected transien
	public String name;
	public String description;
	
	// TODO, actually use this in implementation
	protected boolean allowMultipleSelect; // can multiple purchaseables be "selected" at once?
	protected HashSet<Integer> selected;

	public Inventory inventory;
	protected HashSet<Integer> unlocked; 	// items that have been fully unlocked and can be purchased
	protected HashSet<Integer> levelUnlocked; // items that can be unlocked based on level
//	protected Purchaseable currentQuality;
	
	// just store the index. If consumable, this should be -1 unless something is active
	protected int current;
	
	public  boolean consumable;
	
	// Only used for updating store screen on unlock
//	private transient StoreScreen storeScreen;
//	private transient StoreScreen.TableType storeTable;
	
	// for kryo
	public PurchaseType() {
	}
	
	// create a new one
	public PurchaseType(String name, String description, String textureRegionName, Purchaseable[] values) {
		init(name, description, textureRegionName, values, false);
	}
	
	// create a new one
	public PurchaseType(Inventory inventory, String name, String description, String textureRegionName, Purchaseable[] values) {
		this.inventory = inventory;
		init(name, description, textureRegionName, values, false);
		setValues(values);
	}
	
	// create a new one (with consumable option)
	public PurchaseType(Inventory inventory, String name, String description, String textureRegionName, Purchaseable[] values, boolean consumable) {
		this.inventory = inventory;
		init(name, description, textureRegionName, values, consumable);
		setValues(values);
	}
	
	public void init(String name, String description, String textureRegionName, Purchaseable[] values, boolean consumable) {
		unlocked = new HashSet<Integer>();
		levelUnlocked = new HashSet<Integer>();
		this.name = name;
		this.description = description;
//		if (textureRegionName != null)
//			this.icon = Assets.getTextureRegion(textureRegionName);
		this.values = values;
		for (Purchaseable p : values)
			allPurchaseables.add(p);
		
		this.consumable = consumable;
		current = 0;
		if (consumable) current = -1;
	}
	
	public void allowMultipleSelect() {
		this.allowMultipleSelect = true;
		selected = new HashSet<Integer>();
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
//	public TextureRegion getIcon() {
//		return icon;
//	}

//	@Override
	public Purchaseable getCurrentSelected() {
		if (this.consumable) {
			if (current < 0) return null;
		}
		return values[current];
	}
	
//	@Override
	public boolean availableForUnlock(Purchaseable purchaseable) {
		return levelUnlocked.contains(getIndexOf(purchaseable));
	}

//	@Override
	public boolean isUnlocked(Purchaseable purchaseable) {
		return unlocked.contains(getIndexOf(purchaseable));
	}
	
	public void setCurrent(Purchaseable newCurrent) {
		if (newCurrent == null) {
			throw new java.lang.AssertionError();
		}
		if (!consumable && !isUnlocked(newCurrent)) throw new java.lang.AssertionError();
		if (!availableForUnlock(newCurrent)) throw new java.lang.AssertionError();
		this.current = getIndexOf(newCurrent);
	}
	
	public int getIndexOf(Purchaseable p) {
		for (int i = 0; i < values.length; i++) {
			if (p == values[i])
				return i;
		}
		return -1;
	}
	
	public void addToCurrent(Purchaseable toSelect) {
		if (!isUnlocked(toSelect)) throw new java.lang.AssertionError();
		this.selected.add(getIndexOf(toSelect));
	}
	
	public void removeFromCurrent(Purchaseable toRemove) {
		if (!isUnlocked(toRemove)) throw new java.lang.AssertionError();
		this.selected.remove(toRemove);
	}
	
//	@Override
	public void unlockByLevel(Purchaseable toUnlock) {
		this.levelUnlocked.add(getIndexOf(toUnlock));
		
//		if (this.storeScreen != null)
//			this.storeScreen.resetTable(storeTable);
		// TODO make an announcement!
	}

//	@Override
	public void unlock(Purchaseable toUnlock) {
		if (!availableForUnlock(toUnlock)) throw new java.lang.AssertionError();
		this.unlocked.add(getIndexOf(toUnlock));
		this.setCurrent(toUnlock);
	}
	
	public Purchaseable getActive() {
		if (!this.consumable) throw new java.lang.AssertionError();
		return getCurrentSelected();
	}
	
	public void activateConsumable(Purchaseable p) {
		if (p == null) {
			current = -1;
			return;
		}
		this.setCurrent(p);
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
	
	public boolean unlockIfReady(Purchaseable p) {
		if (inventory.hasUnlockedByLevel(p)) {
			unlockByLevel(p);
			return true;
		}
		return false;
	}
	
	private void setValues(Purchaseable[] values) {
//		System.out.println("set values");
		this.values = values;
		for (Purchaseable p : values) {
			if (p == null) throw new java.lang.NullPointerException();
			unlockIfReady(p);
		}
	}
	
//	public void setTable(StoreScreen screen, StoreScreen.TableType table) {
//		this.storeScreen = screen;
//		this.storeTable = table;
//	}
}
