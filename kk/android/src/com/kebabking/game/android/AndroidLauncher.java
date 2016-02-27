package com.kebabking.game.android;

import com.applovin.sdk.AppLovinSdk;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.common.GoogleApiAvailability;
import com.kebabking.game.AdsHandler;
import com.kebabking.game.Grill;
import com.kebabking.game.KebabKing;
import com.kebabking.game.MainMenuScreen;
import com.kebabking.game.StatsHandler;
import com.kebabking.game.Managers.AdsManager;
import com.kebabking.game.Managers.AdsManagerMock;
import com.kebabking.game.Managers.Manager;
import com.kebabking.game.SummaryScreen;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.multidex.MultiDex;

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
		GoogleApiAvailability gaa = GoogleApiAvailability.getInstance();
		int playServicesAvailable = gaa.isGooglePlayServicesAvailable(this.getContext());
		System.out.println("Google API availability = " + playServicesAvailable);

		// Initialize all android managers
		IABManagerAndroid iab = new IABManagerAndroid(this);
		AnalyticsManagerAndroid analytics = new AnalyticsManagerAndroid(this);
		FileManagerAndroid file = new FileManagerAndroid(this);
		AdsManager ads = new AdsManagerSupersonic(this);
		SocialMediaFacebook media = new SocialMediaFacebook(this);

		Manager.initAndroid(iab, file, analytics, ads, media);
		
		game = new KebabKing();

		config.useWakelock = true; // forces screen to stay on, remove later TODO
		initialize(game, config);
	}

//	// required for facebook login
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//	}

	@Override
	protected  void onResume() {
		super.onResume();
		System.out.println("ON RESUME!!!");
		// need to pause assets (specifically the stupid fonts)
		Manager.onResume();
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		System.out.println("ON PAUSE!!!");
		Manager.onPause();
		StatsHandler.onSessionEnd();
	}

	// required for iab helper
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		System.out.println("On activity result start");
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("on activity result");
		if (Manager.iab != null) {
			((IABManagerAndroid) Manager.iab).handleActivityResult(requestCode, resultCode, data);
		}
		if (Manager.fb != null) {
			((SocialMediaFacebook) Manager.fb).onActivityResult(requestCode, resultCode, data);
		}
	}

	// dispose of IAB for cleanliness
	@Override
	public void onDestroy() {
		super.onDestroy();
		Manager.onDestroy();
		System.out.println("DESTROYING APP");
//		Manager.analytics.sendUserTiming("Total Activity Time", System.currentTimeMillis() - game.activityStartTime);
	}

	@Override
	public void onStart() {
		super.onStart();
		Manager.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
		Manager.onStop();
	}
}
