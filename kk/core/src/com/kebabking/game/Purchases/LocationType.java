package com.kebabking.game.Purchases;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kebabking.game.Assets;
import com.kebabking.game.ProfileInventory;

public class LocationType extends PurchaseType {
	static double BASE_POP_RATE = 1.1;
	static String name = "LOCATION";
	static String description =
			"Upgrade the quality of your meat! Better meat costs more, but sells for "
					+ "more and increases your reputation!. Some customers will only accept"
					+ " high quality meat! ";

	public static class Location extends SimplePurchaseable {
		public transient static final Location[] values = new Location[]{
				
		// Note: an array isn't the best way to do this, but a hashmap is more complicated (especially for static initialization) - already tried

									//MAN, FAT_, WOM O_M, O_W, STUD, GIRL, 	TOUR, FOR, 	POLIC, 		SOLD, BUS, FAR, JEW
		new Location(	new double[] {1,	0.2,	1,	1,	1,	0.1,	0.5,	0,	0,		0.1,	0.1,	.1,	.5,	0.05},1, 0, 0, 0, 	0, "Village", "village"),
		new Location(	new double[] {1,	0.2,	1,	1,	1,	0.1,	0.5,	0,	0.1,	0.1,	0.1,	.4,	2,	0.05},1, 0, 3, 250, 3, "Rice Paddies", "paddy"),
		
		new Location(	new double[] {1,	0.4,	1,	1,	1,	0.4,	0.5,	0,	0.1,	0.3,	0.3,	.4,	.1,	0.05},2, 5, 7, 0, 10, "Subway Stop", "subway"),
		new Location(	new double[] {1,	0.4,	1,	1,	1,	0.4,	0.5,	0,	0.05,	0.3, 	0.3,	.4,	.1,	0.05},2, 5, 12, 200, 10, "Suburbs", "suburbs"),
		new Location(	new double[] {1,	0.6,	1,	1,	1,	0.4,	0.5,	0,	0.05,	0.2,	0.2,	.4,	.1,	0.05},2, 5, 15, 250, 10, "Urban Complex", "urbancomplex"),
		
		new Location(	new double[] {1,	0.6,	1,	1,	1,	0.4,	0.5,	0.1,0.1,	0.3,	0.3,	.4,	.1,	0.05},3, 10, 19, 0, 15, "Small City", "smallcity"),
		new Location(	new double[] {0.5,	0.7,	0.7,0.6,0.6,1,		0.8,	0.6,0.7,	0.4,	0.2,	.8,	.1,	0.05},3, 10, 23, 0, 20, "University", "university"),
		new Location(	new double[] {1,	0.6,	1,	1,	1,	0.5,	0.4,	0.5,0.5,	0.5,	0.4,	.3,	.3,	0.05},3, 10, 25, 300, 20, "Bird's Nest", "birdsnest"),
		
		new Location(	new double[] {1,	0.6,	1,	1,	1,	0.7,	0.4,	0.5,0.5,	0.5,	0.2,	.5,	.4,	0.05},4, 20, 27, 350, 40, "Temple", "temple"),
		new Location(	new double[] {0.5,	0.7,	0.7,0.1,0.1,1,		0.9,	0.2,0.9,	0.5,	0.2,	.9,	0,	0.05},4, 20, 29, 0, 30, "Nightclub District", "nightclub"),
		new Location(	new double[] {1,	0.6,	1,	1,	1,	0.5,	0.4,	0.5,0.5,	0.5,	0.3,	.3,	.3,	0.05},4, 20, 31, 400, 40, "Drum Tower", "drum"),
		
		new Location(	new double[] {1,	0.6,	1,	1,	1,	0.5,	0.4,	0.5,0.5,	0.5,	0.5,	.3,	.3,	0.05},5, 30, 36, 0, 40, "Forbidden City", "forbidden"),
		new Location(	new double[] {1,	0.6,	1,	1,	1,	0.5,	0.4,	0.5,0.5,	0.5,	0.5,	.3,	0,	0.05},5, 40, 38, 400, 40, "Summer Palace", "summerpalace"),
		new Location(	new double[] {1,	0.6,	1,	1,	1,	0.5,	0.4,	0.5,0.5,	0.5,	0.5,	1,	0,	0.05},5, 40, 41, 0, 40, "Shanghai Skyline", "shanghai"),
		new Location(	new double[] {1,	0.6,	1,	1,	1,	0.5,	0.4,	0.5,0.5,	0.5,	0.5,	.3,	0,	0.05},5, 40, 44, 0, 30, "Terra Cotta Warriors", "terracotta"),
		new Location(	new double[] {1,	0.6,	1,	1,	1,	0.5,	0.4,	0.5,0.5,	0.5,	0.5,	.3,	0,	0.05},5, 40, 47, 400, 40, "Panda Sanctuary", "panda"),
		new Location(	new double[] {1,	0.6,	1,	1,	1,	0.5,	0.4,	0.5,0.5,	0.5,	0.5,	.3,	0,	0.05},5, 40, 49, 0, 5, "Silk Road", "silkroad"),
		new Location(	new double[] {0,	0,		0,	0,	0,	0.2,	0.4,	0.5,0.5,	0.5,	0.5,	2,	0,	0.05},5, 40, 50, 500, 50, "Business District", "cbd")
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
		
		private Location(double[] spread, int tier, float rentCost, int unlockAt, float cash, int coins,  String name, String bg) {
			super(name, cash, coins, rentCost, unlockAt, "", "");
		
//			bgName = "background/" + bg;
			fullBG = Assets.getTextureRegion("background/" + bg);
			//(fullBG.getRegionHeight()*0.66f
			this.icon = Assets.getTextureRegion("background/icon/" + bg);
//			this.icon = new TextureRegion(fullBG, 0, (int) (fullBG.getRegionHeight()*0.1f), fullBG.getRegionWidth(), (int) (fullBG.getRegionHeight()*0.22f));
			
			this.originalCustomerSpread = spread;		
			
			this.popularity = Math.pow(BASE_POP_RATE, tier-1);
			System.out.println("Tier " + tier + " popularity " + popularity);
		}

		public TextureRegion getBG() {
			return this.getIcon();
		}
	}

	public LocationType(ProfileInventory inventory) {
		super(inventory, name, description, null, Location.values);
		unlock(Location.values[0]);
	}

	// for kyro
	public LocationType() {
		super(name, description, null, Location.values);
	};

	public TextureRegion getBG() {
		return ((Location) getCurrentSelected()).fullBG;
	}
}
