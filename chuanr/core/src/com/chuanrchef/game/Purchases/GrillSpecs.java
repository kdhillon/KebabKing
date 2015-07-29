package com.chuanrchef.game.Purchases;


public class GrillSpecs {
	static float GRILL_SMALL_COST = 0;
	static float GRILL_MED_COST = 300;
	static float GRILL_LG_COST = 1000;
	
	GrillType type;
	GrillSize size;

	public GrillSpecs() {
		this.type = new GrillType();
		this.size = new GrillSize();
	}
	
	public GrillType getType() {
		return type;
	}
	
	public int getSize() {
		return size.currentSize.size;
	}
	
	public GrillSize getGrillSize() {
		return size;
	}
}
