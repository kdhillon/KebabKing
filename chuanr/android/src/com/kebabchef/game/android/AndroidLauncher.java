package com.kebabchef.game.android;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.chuanrchef.game.ChuanrC;
import com.chuanrchef.game.Managers.FileManager;
import com.chuanrchef.game.Managers.Manager;

public class AndroidLauncher extends AndroidApplication {
	ChuanrC game;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		// Initialize all android managers
		IABManagerAndroid iab = new IABManagerAndroid(this);
		AnalyticsManagerAndroid analytics = new AnalyticsManagerAndroid(this);
		FileManagerAndroid file = new FileManagerAndroid(this);
		AdsManagerAndroid ads = new AdsManagerAndroid(this);
//		FacebookManagerAndroid fb = new FacebookManagerAndroid(this);
		
//		Manager.initAndroid(iab, file, analytics, ads, fb);
		
		game = new ChuanrC();

		config.useWakelock = true; // forces screen to stay on, remove later TODO
		initialize(game, config);
	}

	// required for facebook login
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	// dispose of IAB for cleanliness
	@Override
	public void onDestroy() {
		super.onDestroy();
		System.out.println("DESTROYING APP");
		Manager.analytics.sendUserTiming("Total Activity Time", System.currentTimeMillis() - game.activityStartTime);
	}
}
