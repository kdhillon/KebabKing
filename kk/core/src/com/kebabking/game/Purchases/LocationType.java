package com.kebabking.game.Purchases;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kebabking.game.Assets;

public class LocationType extends PurchaseType {	
	static String description =
			"Upgrade the quality of your meat! Better meat costs more, but sells for "
					+ "more and increases your reputation!. Some customers will only accept"
					+ " high quality meat! ";

	public static class Location extends SimplePurchaseable {
		public transient static final Location[] values = new Location[]{
				
		// Note: an array isn't the best way to do this, but a hashmap is more complicated (especially for static initialization) - already tried
				
		//					 {FAT_MAN, WOMAN, OLD_MAN, OLD_WOMAN, STUDENT, BUSINESSMAN, FOREIGNER, POLICE, GIRL, FAT_AMERICAN};
		//						Man		Woman Old Man Old Woman Student Businessman Foreigner	Police	Girl Fat	Soldier
		new Location(	new float[] {1,	 	1,		1,		1,			1f,		1,		1f,		1f,		1,	1f,		1},1, 0, 0, 0, 	0, "Village", "village"),
		new Location(	new float[] {1,	 	1,		1,		1,			1f,		1,		1f,		1f,		1,	1f,		1},1, 0, 3, 250, 3, "Rice Paddies", "paddy"),
		
		new Location(	new float[] {1,	 	1,		1,		1,			1f,		1,		1f,		1f,		1,	1f,		1},2, 5, 7, 0, 10, "Subway Stop", "subway"),
		new Location(	new float[] {1,	 	1,		1,		1,			1f,		1,		1f,		1f,		1,	1f,		1},2, 5, 12, 200, 10, "Suburbs", "suburbs"),
		new Location(	new float[] {1,	 	1,		1,		1,			1f,		1,		1f,		1f,		1,	1f,		1},2, 5, 15, 250, 10, "Urban Complex", "urbancomplex"),
		
		new Location(	new float[] {1,	 	1,		1,		1,			1f,		1,		1f,		1f,		1,	1f,		1},3, 10, 23, 0, 20, "University", "university"),
		new Location(	new float[] {1,	 	1,		1,		1,			1f,		1,		1f,		1f,		1,	1f,		1},3, 10, 25, 300, 20, "Bird's Nest", "birdsnest"),
		
		new Location(	new float[] {1,	 	1,		1,		1,			1f,		1,		1f,		1f,		1,	1f,		1},4, 20, 29, 0, 30, "Nightclub District", "nightclub"),
		new Location(	new float[] {1,	 	1,		1,		1,			1f,		1,		1f,		1f,		1,	1f,		1},4, 20, 31, 400, 40, "Drum Tower", "drum"),
		
		new Location(	new float[] {1,	 	1,		1,		1,			1f,		1,		1f,		1f,		1,	1f,		1},5, 30, 36, 0, 40, "Forbidden City", "forbidden"),
		new Location(	new float[] {1,	 	1,		1,		1,			1f,		1,		1f,		1f,		1,	1f,		1},5, 40, 50, 500, 50, "Business District", "cbd")
		};
		
		public float[] originalCustomerSpread; 	// once we figure out how many types of customers, assign these.
		public double popularity; 			// how many people walk byy
		public transient TextureRegion fullBG;
//		public String bgName;
		
//		private Location() {
//			if (bgName != null) {
//				fullBG = Assets.getTextureRegion(bgName);
//			}
//		}
		
		private Location(float[] spread, int tier, float rentCost, int unlockAt, float cash, int coins,  String name, String bg) {
			super(name, cash, coins, rentCost, unlockAt, " ", "");
		
//			bgName = "background/" + bg;
			fullBG = Assets.getTextureRegion("background/" + bg);
			//(fullBG.getRegionHeight()*0.66f
			this.icon = new TextureRegion(fullBG, 0, (int) (fullBG.getRegionHeight()*0.1f), fullBG.getRegionWidth(), (int) (fullBG.getRegionHeight()*0.22f));
			
			this.originalCustomerSpread = spread;		
			
			this.popularity = Math.pow(1.1, tier-1);
			System.out.println("Tier " + tier + " popularity " + popularity);
		}

		public TextureRegion getBG() {
			return this.getIcon();
		}
	}

	public LocationType(Inventory inventory) {
		super(inventory, "LOCATION", description, null, Location.values);
		unlock(Location.values[0]);
	}

	// for kyro
	public LocationType() {
		super("LOCATION", description, null, Location.values);
	};

	public TextureRegion getBG() {
		return ((Location) getCurrentSelected()).fullBG;
	}
}
