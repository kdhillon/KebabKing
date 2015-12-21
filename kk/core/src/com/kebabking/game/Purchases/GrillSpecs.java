package com.kebabking.game.Purchases;


public class GrillSpecs {
	static float GRILL_SMALL_COST = 0;
	static float GRILL_MED_COST = 300;
	static float GRILL_LG_COST = 1000;
	
	GrillType grillType;
	GrillSize grillSize;

	public GrillSpecs(Inventory inventory) {
		this.grillType = new GrillType(inventory);
		this.grillSize = new GrillSize(inventory);
	}
	public GrillSpecs(){};

	public GrillType getType() {
		return grillType;
	}
	
	public int getSize() {
		return ((GrillSize.Size) grillSize.currentQuality).size;
	}
	
	public GrillSize getGrillSize() {
		return grillSize;
	}
}
