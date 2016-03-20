package com.kebabking.game.Purchases;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kebabking.game.Assets;
import com.kebabking.game.ProfileInventory;

public class LocationType extends PurchaseType {
	public static boolean UNLOCKS_ONLY_WITH_LOCATIONS = true;
	static double BASE_POP_RATE = 1.1;
	static String name = "location";
	static String description = "location_desc";
			
	public static class Location extends SimplePurchaseable {
		public transient static final Location[] values = new Location[]{
				
		// Note: an array isn't the best way to do this, but a hashmap is more complicated (especially for static initialization) - already tried

									//MAN, FAT_, WOM O_M, O_W, STUD, GIRL, 	TOUR, FOR, 	POLIC, 		SOLD, BUS, FAR, JEW
		new Location(	new double[] {1,	0.2,	1,	1,	1,	0.1,	0.5,	0,	0,		0.1,	0.1,	.1,	.5,	0.1},1, 0, 0, 0, 0, "village"),
		new Location(	new double[] {1,	0.2,	1,	1,	1,	0.1,	0.5,	0,	0.1,	0.1,	0.1,	.4,	2,	0.1},1, 0, 3, 250, 3, "paddy"),
		
		new Location(	new double[] {1,	0.4,	1,	1,	1,	0.4,	0.5,	0,	0.1,	0.3,	0.3,	.4,	.1,	0.1},2, 5, 6, 0, 10, "suburbs"),
		new Location(	new double[] {1,	0.4,	1,	1,	1,	0.4,	0.5,	0,	0.05,	0.3, 	0.3,	.4,	.1,	0.1},2, 5, 9, 200, 10, "subway"),
		new Location(	new double[] {1,	0.6,	1,	1,	1,	0.4,	0.5,	0,	0.05,	0.2,	0.2,	.4,	.1,	0.1},2, 5, 12, 250, 10, "urbancomplex"),
		
		new Location(	new double[] {1,	0.6,	1,	1,	1,	0.4,	0.5,	0.1,0.1,	0.3,	0.3,	.4,	.1,	0.1},3, 10, 15, 0, 15,  "smallcity"),
		new Location(	new double[] {0.5,	0.7,	0.7,0.6,0.6,2,		0.8,	0.6,0.7,	0.4,	0.2,	.8,	.1,	0.1},3, 10, 18, 0, 20, "university"),
		new Location(	new double[] {1,	0.6,	1,	1,	1,	0.5,	0.4,	0.5,0.5,	0.5,	0.4,	.3,	.3,	0.1},3, 10, 21, 300, 20, "birdsnest"),
		
		new Location(	new double[] {1,	0.6,	1,	1,	1,	0.7,	0.4,	0.5,0.5,	0.5,	0.2,	.5,	.4,	0.1},4, 20, 24, 350, 40, "temple"),
		new Location(	new double[] {0.5,	0.7,	0.7,0.1,0.1,0.8,	1.2,	0.2,0.9,	0.5,	0.2,	.9,	0,	0.1},4, 20, 27, 0, 30, "nightclub"),
		new Location(	new double[] {1,	0.6,	1,	1,	1,	0.5,	0.4,	0.5,0.5,	0.5,	0.3,	.3,	.3,	0.1},4, 20, 30, 400, 40,"drum"),
		
		new Location(	new double[] {1,	0.6,	1,	1,	1,	0.5,	0.4,	2,	0.5,	0.5,	0.5,	.3,	.3,	0.1},5, 30, 33, 0, 40, "forbidden"),
		new Location(	new double[] {1,	0.6,	1,	1,	1,	0.5,	0.4,	2,	0.5,	0.5,	0.5,	.3,	0,	0.1},5, 40, 36, 400, 40, "summerpalace"),
		new Location(	new double[] {1,	0.6,	1,	1,	1,	0.5,	0.4,	0.5,0.5,	0.5,	0.5,	1,	0,	0.1},5, 40, 39, 0, 40, "shanghai"),
		new Location(	new double[] {1,	0.6,	1,	1,	1,	0.5,	0.4,	2,	0.5,	0.5,	0.5,	.3,	0,	0.1},5, 40, 42, 0, 30, "terracotta"),
		new Location(	new double[] {1,	0.6,	1,	1,	1,	0.5,	0.4,	0.5,0.5,	0.5,	0.5,	.3,	0,	0.1},5, 40, 45, 400, 40, "panda"),
		new Location(	new double[] {1,	0.6,	1,	1,	1,	0.5,	0.4,	0.5,0.5,	0.5,	0.5,	.3,	0,	0.1},5, 40, 48, 0, 5, "silkroad"),
		new Location(	new double[] {0,	0,		0,	0,	0,	0.2,	0.4,	0.5,0.5,	0.5,	0.5,	2,	0,	0.1},5, 40, 50, 500, 50, "cbd")
		};
		
		public double[] originalCustomerSpread; 	// once we figure out how many types of customers, assign these.
		public double popularity; 			// how many people walk byy
		public transient TextureRegion fullBG;

//		public String bgName;
		
//		private Location() {
//			if (bgName != null) {
//				fullBG = Assets.getTextureRegion(bgName);
//			}
//		}
		
		private Location(double[] spread, int tier, float rentCost, int unlockAt, float cash, int coins, String name) {
			super(Assets.strings.get(LocationType.name + "_" +name), 0, 0, rentCost, unlockAt, -5,Assets.strings.get(LocationType.name + "_" + name + "_d"), "");
		
//			bgName = "background/" + bg;
			fullBG = Assets.getTextureRegion("background/" + name);
			//(fullBG.getRegionHeight()*0.66f
			this.icon = Assets.getTextureRegion("background/icon/" + name);
//			this.icon = new TextureRegion(fullBG, 0, (int) (fullBG.getRegionHeight()*0.1f), fullBG.getRegionWidth(), (int) (fullBG.getRegionHeight()*0.22f));
			
			this.originalCustomerSpread = spread;		
			
			this.popularity = 0.5 + tier * 0.5;
		}

		public TextureRegion getBG() {
			return this.getIcon();
		}
		
		@Override
		public int unlockAtLevel() {
			return unlockAtLevel;
		}
	}

	public LocationType(ProfileInventory inventory) {
		super(inventory, name, description, Location.values);
		unlock(Location.values[0]);
	}

	// for kyro
	public LocationType() {
		super(name, description, Location.values);
	};

	public TextureRegion getBG() {
		return ((Location) getFirstSelected()).fullBG;
	}

//	public static Location getLocationUnlockedAt(int level) {
////		for (int i = 0; i < Location.values.length; i++) {
////			if (Location.values[i].unlockAtLevel() == level) return (Location) Location.values[i];
////		}
////		return null;
//		return getLocationA
//	}
	
	public static Location getLocationAt(int index) {
		return LocationType.Location.values[index - 1];
	}
}
