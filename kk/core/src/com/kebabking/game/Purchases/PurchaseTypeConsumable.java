package com.kebabking.game.Purchases;

import com.badlogic.gdx.utils.TimeUtils;
import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.kebabking.game.ProfileInventory;

public class PurchaseTypeConsumable extends PurchaseType {

	// might be null, 
	@Tag(501) private int active;
	
	@Tag(502) public long startedAtMillis;
	@Tag(503) public long endsAtMillis;

	// kryo
	public PurchaseTypeConsumable(){}
	
	public PurchaseTypeConsumable(String name, String description, Purchaseable[] values) {
		super(name, description, values);
		this.consumable = true;
		this.active = -1;
	}
	
	public PurchaseTypeConsumable(ProfileInventory inventory, String name, String description, Purchaseable[] values) {
		super(inventory, name, description, values);
		this.consumable = true;
		this.active = -1;
	}

	public void update() {
		if (getActive() != null &&
				TimeUtils.millis() > this.endsAtMillis) {
			reset();
		}
	}
	
	public void activateConsumable(Purchaseable p) {
		if (p == null) {
			active = -1;
			return;
		}
		active = getIndexOf(p);
		this.startedAtMillis = TimeUtils.millis();
		
		// TODO create Consumable type that extends purchaseable
		this.endsAtMillis = startedAtMillis + 1000 * ((SimpleConsumable) p).duration;
//		this.setCurrent(p);
	}
	
	private void reset() {
		activateConsumable(null);
		this.active = -1;
		this.startedAtMillis = 0;
		this.endsAtMillis = 0;
		
		this.inventory.handleConsumableReset(this);
	}
	
	public Purchaseable getActive() {
		if (active < 0) {
//			System.out.println("ad campaign active is null");
			return null;
		}
		return values[active];
	}
	
	// TODO remove
	public Purchaseable getCurrentSelected() {
		return getActive();
//		throw new java.lang.AssertionError();
	}
	
	public float getPercent() {
		if (getActive() == null) return 0;
		return 1.0f * TimeUtils.timeSinceMillis(startedAtMillis) / 
				(endsAtMillis - startedAtMillis);
	}
	
}
