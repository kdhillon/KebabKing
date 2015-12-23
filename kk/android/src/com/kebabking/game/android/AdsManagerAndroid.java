package com.kebabking.game.android;

import android.app.Activity;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.kebabking.game.Managers.AdsManager;
import com.supersonic.mediationsdk.sdk.InterstitialListener;
import com.supersonic.mediationsdk.sdk.RewardedVideoListener;
import com.supersonic.mediationsdk.sdk.Supersonic;
import com.supersonic.mediationsdk.sdk.SupersonicFactory;

import java.io.IOException;

public class AdsManagerAndroid implements AdsManager {
//    // TODO change for KK
//    static final String UNITY_GAME_ID = "74619";
//
    AndroidLauncher androidLauncher;
//    UnityAdsListener adsListener;
    private Supersonic mMediationAgent;

    public AdsManagerAndroid(AndroidLauncher androidLauncher) {
        this.androidLauncher = androidLauncher;
        //Get the mediation publisher instance
        mMediationAgent = SupersonicFactory.getInstance();

        AdsListener listener = new AdsListener(androidLauncher);

        //Set the Rewarded Video Listener
        // may be unnecessary, can just call mMediationAgent.isRewardedVideoAvailable();
        // http://developers.supersonic.com/hc/en-us/articles/201321042-Integrating-Rewarded-Video
        mMediationAgent.setRewardedVideoListener(listener);

        //Set the unique id of your end user.
        // This can't be called from main thread, should be done asynchronously?
        AdvertisingIdClient.Info adInfo = null;
//        try {
//            adInfo = AdvertisingIdClient.getAdvertisingIdInfo(androidLauncher.getContext());
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (GooglePlayServicesNotAvailableException e) {
//            e.printStackTrace();
//        } catch (GooglePlayServicesRepairableException e) {
//            e.printStackTrace();
//        }
//        String mUserId =  adInfo.getId();
        String mUserId =  "1502010";

        //Set the Application Key - can be retrieved from Supersonic platform
        String mAppKey = "3b98e7d1";

        //Init Rewarded Video
        mMediationAgent.initRewardedVideo(androidLauncher, mAppKey, mUserId);
    }

    @Override
    public boolean showAd() {
        if (mMediationAgent.isRewardedVideoAvailable()) {
            mMediationAgent.showRewardedVideo();
            return true;
        }
        else {
            System.out.println("No rewarded video available!");
            return false;
        }
    }

    public void onResume() {
        if (mMediationAgent != null) {
            mMediationAgent.onResume((Activity) androidLauncher);
        }
    }

    public void onPause() {
        mMediationAgent.onPause((Activity) androidLauncher);
    }
}
