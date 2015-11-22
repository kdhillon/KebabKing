package com.chuanrchef.game.Managers;

public class AdsManagerMock implements AdsManager {
	// TODO return info about ad completion.
	@Override
	public void showAd() {
		System.out.println("showAd() mock");
	}

	// Stuff to do on pause/resume
	@Override
	public void onResume() {
	}

	@Override
	public void onPause() {
	}
}
