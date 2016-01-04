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
    
    // For now, pass everything in already initialized...
    public static void initAndroid(IABManager iabIn, FileManager fileIn, AnalyticsManager analyticsIn, AdsManager adsIn, FacebookManager facebookIn) {
    	if (iabIn == null) iab = new IABManagerMock();
        else iab = iabIn;
    	file = fileIn;
    	analytics = analyticsIn;
    	if (adsIn == null)
            ads = new AdsManagerMock();
        else ads = adsIn;
    	if (facebookIn == null)
            fb = new FacebookManagerMock();
        else fb = facebookIn;
    }

    public static void onStart() {
        ads.onStart();
    }

    public static void onStop() {
        ads.onStop();
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

    public static void onDestroy() {
        ads.onDestroy();
    }
}

