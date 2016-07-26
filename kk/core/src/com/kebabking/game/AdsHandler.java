package com.kebabking.game;

import com.badlogic.gdx.utils.TimeUtils;
import com.kebabking.game.Managers.Manager;

/**
 * Created by Kyle on 1/2/2016.
 */
public class AdsHandler {
//    static final int ADS_REWARD = 3;
    static final long MILLIS_TO_WAIT_BEFORE_REWARD = 1000; //
    
    static long adWatchedAt;
    static boolean adJustFinished;
    static KebabKing master;
    
//    static JadeWheel wheel;
      
    public static void init(KebabKing masterIn) {
        master = masterIn;
    }
    
    public static void showAd() {
		Manager.ads.showAd();
		Manager.analytics.sendScreenHit("Watching Ad");
    }

    // call this in onResume
    public static void reward() {
//    	int coins = (int) ((Math.random() * 2) + 1);

    	// assume ad was watched to end violation (can't assume this!)
    	if (DrawUI.policeNotificationActive) {
    		master.profile.endViolation();
    	}
    	else {
    		// launch wheel table  
    		master.profile.letSpin();
    	}

    	// the problem is that this is called before onResume, simple fix is use a boolean flag.
    	Manager.analytics.sendEventHit("Ads", "ad completed");
		KebabKing.print("Ad completed!");
		
		StatsHandler.completeAd();
		// save after ad was watched
		master.save();
    }

    public static void checkIfShouldReward() {
        if (adJustFinished && TimeUtils.millis() - adWatchedAt > MILLIS_TO_WAIT_BEFORE_REWARD) {
            adJustFinished = false;
            adWatchedAt = 0;
            reward();
        }
    }

    public static void handleAdJustWatched() {
//        if (adJustFinished) throw new java.lang.AssertionError();
        // this should be allowed to be set true twice, if person clicks vid and then complete is called as well
        adJustFinished = true;
        adWatchedAt = TimeUtils.millis();
        master.profile.gameQuitDuringAd = false;
    }

    public static void handleAdExited() {
        Manager.analytics.sendEventHit("Ads", "ad not completed");
        KebabKing.print("Ad not completed!");
    }

    public static void handleAdNotAvailable() {
        Manager.analytics.sendEventHit("Ads", "ad not available");
        DrawUI.launchAdNotAvailableNotification();
        StatsHandler.adNotAvailable();
        KebabKing.print("Ad not available!");
    }

    public static void handleAboutToLaunchAd() {
        master.profile.gameQuitDuringAd = true;
    }
}
