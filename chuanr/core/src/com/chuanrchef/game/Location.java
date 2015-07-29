package com.chuanrchef.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.chuanrchef.game.Purchases.Purchaseable;

public enum Location implements Purchaseable {							
	//						Man		Woman Old Man Old Woman Student Businessman Foreigner	Police	Girl
	Village(	new float[] {1,	 	0,		1,		0,		0.0f,		0,		0.5f,		0.5f,	0	}, "Village", Assets.bgVillage, 0, .6, 0),
	Outskirts(	new float[] {1,		0, 		1,		0, 			0, 		0,			1,		0.05f,	0	}, "Outskirts", Assets.bgOutskirts, 5, .8, 10),
	Suburbs(	new float[] {1, 	0,		1,		0,			0,		0,			1,		0.2f,	0	}, "Suburbs", Assets.bgSuburbs, 10, 1, 50),
	University(	new float[] {1, 	0,		1,		0,			0,		0,			1,		0.2f,	0	}, "University", Assets.bgUniversity, 15, 1.2, 200),
	CBD(		new float[] {1, 	0,		1,		0,			0,		0,			1,		0.2f,	0	}, "Central Business District", Assets.bgCBD, 20, 1.5, 500);
	
	String name;
	float[] customerSpread; 	// once we figure out how many types of customers, assign these.
	double popularity; 			// how many people walk byy
	TextureRegion bg;			// background to use 
	int coinsToUnlock;
	float rentCost; 			// cost to operate in this area each day.
	
	private Location(float[] spread, String name, TextureRegion bg, float rentCost, double popularity, int coinsToUnlock) {
		this.name = name;
		this.bg = bg;
		this.rentCost = rentCost;
		this.customerSpread = spread;
		this.popularity = popularity;
		this.coinsToUnlock = coinsToUnlock;
	}

	@Override
	public String getName() {		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public int coinsToUnlock() {
		return coinsToUnlock;
	}

	@Override
	public float cashToUnlock() {
		return 0;
	}

	@Override
	public float getDailyCost() {
		return rentCost;
	}

	@Override
	public TextureRegion getIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	public TextureRegion getBG() {
		return this.bg;
	}

}
