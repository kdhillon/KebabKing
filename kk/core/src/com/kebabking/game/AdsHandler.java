package com.kebabking.game;

import com.kebabking.game.Managers.Manager;

/**
 * Created by Kyle on 1/2/2016.
 */
public class AdsHandler {

    static KebabKing master;

    public static void init(KebabKing masterIn) {
        master = masterIn;
    }

    public static void handleAdWatched() {
//    	int coins = (int) ((Math.random() * 2) + 1);
        int coins = 2;
		master.profile.giveCoins(coins);
    	DrawUI.launchAdSuccessNotification(coins);
        Manager.analytics.sendEventHit("Ads", "ad completed");
        System.out.println("Ad completed!");
    }

    public static void handleAdExited() {
        Manager.analytics.sendEventHit("Ads", "ad not completed");
        System.out.println("Ad not completed!");
    }

    public static void handleAdNotAvailable() {
        Manager.analytics.sendEventHit("Ads", "ad not available");
        DrawUI.launchAdNotAvailableNotification();
        System.out.println("Ad not available!");
    }
}
