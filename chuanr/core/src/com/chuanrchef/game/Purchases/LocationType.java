package com.chuanrchef.game.Purchases;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.chuanrchef.game.Location;

public class LocationType extends PurchaseType {	
	static String description =
			"Upgrade the quality of your meat! Better meat costs more, but sells for "
					+ "more and increases your reputation!. Some customers will only accept"
					+ " high quality meat! ";

	public LocationType(Inventory inventory) {
		super(inventory, "Location", description, null, Location.values());
		unlock(Location.Village);
	}

	// for kyro
	public LocationType() {};
	
	public TextureRegion getBG() {
		return ((Location) this.getCurrentSelected()).getBG();
	}
}
