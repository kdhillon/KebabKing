package com.kebabking.game.Managers;

import com.kebabking.game.AdsHandler;

public class AdsManagerMock implements AdsManager {
	@Override
	public void cacheAd() {
		System.out.println("cacheAd() mock");
	}

	/** return true if ad was completed, false otherwise */
	@Override
	public void showAd() {
		System.out.println("showAd() mock");
		AdsHandler.handleAdJustWatched();
//		AdsHandler.handleAdNotAvailable();
	}

	// Stuff to do on pause/resume
	@Override
	public void onResume() {
	}

	@Override
	public void onPause() {
	}

	@Override
	public void onStart() {
	}

	@Override
	public void onStop() {
	}
	@Override
	public void onDestroy() {
	}
}
