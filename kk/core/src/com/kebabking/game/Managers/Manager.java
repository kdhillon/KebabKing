package com.kebabking.game.Managers;

/**
 * Created by Kyle on 11/10/2015.
 */
public class Manager {
    public static IABManager iab;
    public static FileManager file;
    public static AnalyticsManager analytics;
    public static AdsManager ads;
    public static FacebookManager fb;

    public static void initDesktop() {
    	iab = new IABManagerMock();
    	file = new FileManagerDesktop();
    	analytics = new AnalyticsManagerMock();
    	ads = new AdsManagerMock();
    	fb = new FacebookManagerMock();
    }
    
    // How to do this cleanly? TODO
    // For now, pass everything in already initialized...
    public static void initAndroid(IABManager iabIn, FileManager fileIn, AnalyticsManager analyticsIn, AdsManager adsIn, FacebookManager facebookIn) {
    	iab = new IABManagerMock();
    	file = fileIn;
    	analytics = analyticsIn;
    	ads = adsIn;
    	fb = new FacebookManagerMock();;
    }

    public static void onResume() {
//        iab.onResume();
//        file.onResume();
//        analytics.onResume();
        fb.onResume();
        ads.onResume();
    }

    public static void onPause() {
        fb.onPause();
        ads.onPause();
    }
}

