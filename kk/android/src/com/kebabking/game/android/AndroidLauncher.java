package com.kebabking.game.android;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.kebabking.game.KebabKing;
import com.kebabking.game.Managers.Manager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

public class AndroidLauncher extends AndroidApplication {
	KebabKing game;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.loadLibrary("gdx");
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		ConnectivityManager conMgr = (ConnectivityManager) this.getContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// CHECK NETWORK STATE
		NetworkInfo i = conMgr.getActiveNetworkInfo();
		if (i == null)
			System.out.println("Internet Error: Could not be created!");
		else if (!i.isConnected())
			System.out.println("Internet Error: Is not connected!");
		else if (!i.isAvailable())
			System.out.println("Internet Error: Is not available!");
		else {
			System.out.println("Internet is available!");
		}

		// Initialize all android managers
//		IABManagerAndroid iab = new IABManagerAndroid(this);
		AnalyticsManagerAndroid analytics = new AnalyticsManagerAndroid(this);
		FileManagerAndroid file = new FileManagerAndroid(this);
		AdsManagerAndroid ads = new AdsManagerAndroid(this);
//		FacebookManagerAndroid fb = new FacebookManagerAndroid(this);
		
		Manager.initAndroid(null, file, analytics, ads, null);
		
		game = new KebabKing();

		config.useWakelock = true; // forces screen to stay on, remove later TODO
		initialize(game, config);
	}

//	// required for facebook login
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//	}

	// dispose of IAB for cleanliness
	@Override
	public void onDestroy() {
		super.onDestroy();
		System.out.println("DESTROYING APP");
//		Manager.analytics.sendUserTiming("Total Activity Time", System.currentTimeMillis() - game.activityStartTime);
	}
}
