package com.kebabchef.game.android;

import android.util.Log;

import com.chuanrchef.game.Managers.AdsManager;
//import com.unity3d.ads.android.UnityAds;

import java.util.ArrayList;

public class AdsManagerAndroid implements AdsManager {
//    // TODO change for KK
//    static final String UNITY_GAME_ID = "74619";
//
//    AndroidLauncher androidLauncher;
//    UnityAdsListener adsListener;

    public AdsManagerAndroid(AndroidLauncher androidLauncher) {
//        this.androidLauncher = androidLauncher;
//        adsListener = new UnityAdsListener();
//        UnityAds.init(androidLauncher, UNITY_GAME_ID, adsListener);
    }

    @Override
    public void showAd() {
//        if(UnityAds.canShow()) {
//            UnityAds.show();
//        }
//        else {
//            Log.d("FB", "Can't show ad for some reason!");
//        }
    }

    public void onResume() {
//        UnityAds.changeActivity(androidLauncher);
//        UnityAds.setListener(adsListener);
    }

    public void onPause() {
    }
}
