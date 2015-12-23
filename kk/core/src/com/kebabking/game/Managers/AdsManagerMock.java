package com.kebabking.game.Managers;

public class AdsManagerMock implements AdsManager {
	/** return true if ad was completed, false otherwise */
	@Override
	public boolean showAd() {
		System.out.println("showAd() mock");
		return true;
	}

	// Stuff to do on pause/resume
	@Override
	public void onResume() {
	}

	@Override
	public void onPause() {
	}
}
