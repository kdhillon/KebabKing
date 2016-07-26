package com.kebabking.game.Managers;

import com.kebabking.game.AdsHandler;
import com.kebabking.game.KebabKing;

public class AdsManagerMock implements AdsManager {
	@Override
	public void cacheAd() {
		KebabKing.print("cacheAd() mock");
	}

	/** return true if ad was completed, false otherwise */
	@Override
	public void showAd() {
		KebabKing.print("showAd() mock");
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
