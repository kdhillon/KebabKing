package com.kebabchef.game.android;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.chuanrchef.game.ChuanrC;
import com.chuanrchef.game.FileManager;

public class AndroidLauncher extends AndroidApplication {
	ChuanrC game;

	IABManagerAndroid iabManager;
	AnalyticsManagerAndroid analyticsManager;
	FileManagerAndroid fileManager;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();


		iabManager = new IABManagerAndroid(this);
		analyticsManager = new AnalyticsManagerAndroid(this);
		fileManager = new FileManagerAndroid(this);

		game = new ChuanrC();
//		game.setPlatformSpecific(ps);

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
		this.analyticsManager.sendUserTiming("Total Activity Time", System.currentTimeMillis() - game.activityStartTime);
	}
}
