package com.chuanrchef.game;

public class Analytics {

	static PlatformSpecific ps;
	
	public static void init(PlatformSpecific in) {
		ps = in;
	}
	
	public static void sendEventHit(String category, String action, String label) {
		ps.sendEventHit(category, action, label);
	}
	
}
