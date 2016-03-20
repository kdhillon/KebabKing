package com.kebabking.game.Purchases;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.kebabking.game.Assets;
import com.kebabking.game.ProfileInventory;
import com.kebabking.game.Purchases.GrillType.Type;


// Allows other purchase types to extend this for simplicity
// These really should be Static classes EXCEPT for unlocked
public class PurchaseType {
	// stores rounds at which purchaseables unlock
	public static HashSet<Purchaseable> allPurchaseables = new HashSet<Purchaseable>();
	public transient Purchaseable[] values; // contains Purchaseable.values()
	//	protected transient static TextureRegion icon;
	//	protected transien
	// doesn't need to be saved
	public String name;
	public String description;
	public TextureRegion icon;

	// doesn't need to be serialized

	// needs to be serialized
	@Tag(401) protected HashSet<Integer> unlocked; 	// items that have been fully unlocked and can be purchased

	// TODO do we really need to save this???
	@Tag(402) protected HashSet<Integer> levelUnlocked; // items that can be unlocked based on level, 
	//	protected Purchaseable currentQuality;

	// just store the index. If consumable, this should be -1 unless something is active
	@Deprecated @Tag(403) protected int currentSelected;

	// I think we do need to save this.
	@Tag(404) public ProfileInventory inventory;

	// TODO, actually use this in implementation
	//	@Deprecated @Tag(405) public int maxSelectable;
	@Tag(406) protected Deque<Integer> selected;

	public boolean consumable; // should only be used by PurchaseTypeConsumable

	// for kryo (note, init should be called on kryo initialization in extending classes)
	public PurchaseType() {
	}

	// create a new one
	public PurchaseType(String name, String description, Purchaseable[] values) {
		init(name, description, values);
	}

	// create a new one
	public PurchaseType(ProfileInventory inventory, String name, String description, Purchaseable[] values) {
		this.inventory = inventory;
		init(name, description, values);
	}

	public void init(String name, String description, Purchaseable[] values) { //, boolean consumable) {
		unlocked = new HashSet<Integer>();
		levelUnlocked = new HashSet<Integer>();
		this.name = Assets.strings.get(name);
		this.description = Assets.strings.get(description);
		this.values = values;
		for (Purchaseable p : values) {
			allPurchaseables.add(p);
			// way too hacky
			if (this.getName().equals(Assets.strings.get("location"))) continue;
			if (p.unlockAtLevel() <= 0) System.out.println(p.getName());
			if (LocationType.UNLOCKS_ONLY_WITH_LOCATIONS && LocationType.getLocationAt(p.unlockWithLocation()) == null) throw new java.lang.AssertionError("purchaseable needs to be unlocked with a location");
		}
		
		// this is code for checking unlock levels
//		int[] levelUnlockCount = new int[51];
//		for (Purchaseable p : allPurchaseables) {
//			if (p.unlockAtLevel() > 0) levelUnlockCount[p.unlockAtLevel()]++;
//		}
//		for (int i = 2; i < 51; i++) {
//			System.out.println(i + ": " + levelUnlockCount[i]);
//		}
		
		String regName = "market/" + name + "_icon";
		if (Assets.regionExists(regName))
			this.icon = Assets.getTextureRegion(regName);
		
		// default value is 1
		//		maxSelectable = 1;
		selected = new ArrayDeque<Integer>();
		//		this.consumable = consumable;
		//		current = 0;
		//		if (consumable) current = -1;
		setValues(values);
	}

	//	public boolean allowsMultipleSelect() {
	//		return maxSelectable > 1;
	//	}

	//	public void allowMultipleSelect() {
	//		this.allowMultipleSelect = true;
	//		selected = new HashSet<Integer>();
	//	}

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
		return icon;
	}

	//	@Override
	//	public Purchaseable getCurrentSelected() {
	//		if (this.consumable) {
	//			if (selected.size() <= 0) return null;
	//		}
	//		if (maxSelectable == 1) return values[selected.getFirst()];
	////		return values[currentSelected];
	//		return null;
	//	}

	//	@Override
	public Purchaseable getFirstSelected() {
		if (this.consumable) {
			if (selected.size() <= 0) return null;
		}
		// if loading from a file that had "currentselected" as an int
		else if (this.getMaxSelectable() == 1 && selected.size() == 0) {
			unlock(Type.values[0]);
		}
		if (selected.size() == 0) {
			selected.add(0);
		}
		
		if (selected.getFirst() > values.length - 1) {
			selected.remove(selected.getFirst());
		}
		
		return values[selected.getFirst()];
		//		return values[currentSelected];
		//		return null;
	}
	
	public Deque<Integer> getSelected() {
		return selected;
	}

	//	@Override
	public boolean availableForUnlock(Purchaseable purchaseable) {
		return levelUnlocked.contains(getIndexOf(purchaseable));
	}

	//	@Override
	public boolean isUnlocked(Purchaseable purchaseable) {
		return unlocked.contains(getIndexOf(purchaseable));
	}

	// for multi select
	public void setCurrent(Deque<Integer> deque) {
		this.selected.clear();
		this.selected.addAll(deque);
	}

	public int getIndexOf(Purchaseable p) {
		for (int i = 0; i < values.length; i++) {
			if (p == values[i])
				return i;
		}
		return -1;
	}

	//	@Override
	public void unlockByLevel(Purchaseable toUnlock) {
		this.levelUnlocked.add(getIndexOf(toUnlock));
	}

	//	@Override
	public void unlock(Purchaseable toUnlock) {
		if (!availableForUnlock(toUnlock)) throw new java.lang.AssertionError();
		this.unlocked.add(getIndexOf(toUnlock));
		this.addToSelected(toUnlock);
	}

	public int addToSelected(Purchaseable toAdd) {
		return addToSelected(getIndexOf(toAdd));
	}

	// returns the index that was removed, or -1 if none removed
	public int addToSelected(int index) {
		// if already selected, try to remove
		if (isSelected(index)) {
			// don't remove if less 1
			if (this.selected.size() > 1) {
				this.selected.remove(index);
				return index;
			}
			return -1;
		}
		else {
			System.out.println("Adding " + index + " to selected");
			int ret = -1;
			if (selected.size() >= this.getMaxSelectable()) {
				System.out.println(selected.size() + " >= " + this.getMaxSelectable());
				ret = selected.removeFirst();
			}
			this.selected.add(index);
			return ret;
		}
	}

	public boolean isSelected(int index) {
		for (Integer i : selected) {
			if (i == index) {
				return true;
			}
		}
		return false;
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
		if (inventory == null) {
//			System.out.println("INVENTORY IS NULL IN PURCHASETYPE");
			return false;
		}
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
			p.setType(this);
		}
	}

	public int getMaxSelectable() {
		return 1;
	}
	//	public void setTable(StoreScreen screen, StoreScreen.TableType table) {
	//		this.storeScreen = screen;
	//		this.storeTable = table;
	//	}
}
