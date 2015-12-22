package com.kebabking.game.Purchases;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kebabking.game.Assets;

public class LocationType extends PurchaseType {	
	static String description =
			"Upgrade the quality of your meat! Better meat costs more, but sells for "
					+ "more and increases your reputation!. Some customers will only accept"
					+ " high quality meat! ";

	public static class Location extends SimplePurchaseable {
		public static final Location[] values = new Location[]{
				
		// Note: an array isn't the best way to do this, but a hashmap is more complicated (especially for static initialization) - already tried
				
		//					 {FAT_MAN, WOMAN, OLD_MAN, OLD_WOMAN, STUDENT, BUSINESSMAN, FOREIGNER, POLICE, GIRL, FAT_AMERICAN};
		//						Man		Woman Old Man Old Woman Student Businessman Foreigner	Police	Girl Fat	Soldier
		new Location(	new float[] {1,	 	1,		1,		1,			1f,		1,		1f,		1f,		1,	1f,		1}, 0, 0, 2, 0, "Village", "village"),
		new Location(	new float[] {1,	 	1,		1,		1,			1f,		1,		1f,		1f,		1,	1f,		1}, 0, 0, 2, 0, "Rice Paddies", "paddy"),
		new Location(	new float[] {1,	 	1,		1,		1,			1f,		1,		1f,		1f,		1,	1f,		1}, 0, 0, 2, 0, "Suburbs", "suburbs"),
		new Location(	new float[] {1,	 	1,		1,		1,			1f,		1,		1f,		1f,		1,	1f,		1}, 0, 0, 2, 0, "Urban Complex", "urbancomplex"),
		new Location(	new float[] {1,	 	1,		1,		1,			1f,		1,		1f,		1f,		1,	1f,		1}, 5, 0, .8, 10, "Subway Stop", "subway"),
		new Location(	new float[] {1,	 	1,		1,		1,			1f,		1,		1f,		1f,		1,	1f,		1}, 10, 0, 1, 50, "Drum Tower", "drum"),
		new Location(	new float[] {1,	 	1,		1,		1,			1f,		1,		1f,		1f,		1,	1f,		1}, 15, 0, 1.2, 200, "Forbidden City", "forbidden"),
		new Location(	new float[] {1,	 	1,		1,		1,			1f,		1,		1f,		1f,		1,	1f,		1}, 20, 0, 1.5, 500, "Business District", "cbd")
		};
		
		public float[] originalCustomerSpread; 	// once we figure out how many types of customers, assign these.
		public double popularity; 			// how many people walk byy
		public TextureRegion fullBG;
		
		private Location(float[] spread, float rentCost, int unlockAt, double popularity, int coinsToUnlock,  String name, String bg) {
			super(name, 0, coinsToUnlock, rentCost, unlockAt, " ", "");
			
			fullBG = Assets.getTextureRegion("background/" + bg);
			//(fullBG.getRegionHeight()*0.66f
			this.icon = new TextureRegion(fullBG, 0, (int) (fullBG.getRegionHeight()*0.1f), fullBG.getRegionWidth(), (int) (fullBG.getRegionHeight()*0.22f));
			
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
		return ((Location) getCurrentSelected()).fullBG;
	}
}
