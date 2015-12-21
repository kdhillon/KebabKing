package com.kebabking.game.Purchases;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class LocationType extends PurchaseType {	
	static String description =
			"Upgrade the quality of your meat! Better meat costs more, but sells for "
					+ "more and increases your reputation!. Some customers will only accept"
					+ " high quality meat! ";

	public static class Location extends SimplePurchaseable {
		public static final Location[] values = new Location[]{
				
		// Note: an array isn't the best way to do this, but a hashmap is more complicated (especially for static initialization) - already tried
				
		//					 {FAT_MAN, WOMAN, OLD_MAN, OLD_WOMAN, STUDENT, BUSINESSMAN, FOREIGNER, POLICE, GIRL, FAT_AMERICAN};
		//						Man		Woman Old Man Old Woman Student Businessman Foreigner	Police	Girl Fat
		new Location(	new float[] {1,	 	1,		1,		1,			1f,		1,			1f,		1f,		1,	1f}, "Village", "village2", 0, 0, 2, 0),
		new Location(	new float[] {1,		0, 		1,		0, 			0, 		0,			1,		0.05f,	0,	0.2f}, "Outskirts", "outskirts", 5, 0, .8, 10),
		new Location(	new float[] {1, 	0,		1,		0,			0,		0,			1,		0.2f,	0,	0.2f}, "Drum Tower", "drum", 10, 0, 1, 50),
		new Location(	new float[] {1, 	0,		1,		0,			0,		0,			1,		0.2f,	0,	0.2f}, "Forbidden City", "forbidden", 15, 0, 1.2, 200),
		new Location(	new float[] {1, 	0,		1,		0,			0,		0,			1,		0.2f,	0,	0.2f}, "Central Business District", "cbd", 20, 0, 1.5, 500)
		};
		
		public float[] originalCustomerSpread; 	// once we figure out how many types of customers, assign these.
		public double popularity; 			// how many people walk byy

		private Location(float[] spread, String name, String bg, float rentCost, int unlockAt, double popularity, int coinsToUnlock) {
			super(name, 0, coinsToUnlock, rentCost, unlockAt, "", "background/" + bg);
		
			this.originalCustomerSpread = spread;		
			this.popularity = popularity;
		}

		public TextureRegion getBG() {
			return this.getIcon();
		}
	}

	public LocationType(Inventory inventory) {
		super(inventory, "Location", description, null, Location.values);
		unlock(Location.values[0]);
	}

	// for kyro
	public LocationType() {};

	public TextureRegion getBG() {
		return ((Location) this.getCurrentSelected()).getBG();
	}
}
