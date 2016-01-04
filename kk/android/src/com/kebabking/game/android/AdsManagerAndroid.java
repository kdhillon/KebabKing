package com.kebabking.game.android;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.kebabking.game.AdsHandler;
import com.kebabking.game.Managers.AdsManager;
import com.supersonic.mediationsdk.integration.IntegrationHelper;
import com.supersonic.mediationsdk.sdk.InterstitialListener;
import com.supersonic.mediationsdk.sdk.RewardedVideoListener;
import com.supersonic.mediationsdk.sdk.Supersonic;
import com.supersonic.mediationsdk.sdk.SupersonicFactory;

import java.io.IOException;

public class AdsManagerAndroid implements AdsManager {
//    // TODO change for KK
//    static final String UNITY_GAME_ID = "74619";

    final AndroidLauncher androidLauncher;
//    UnityAdsListener adsListener;
    private Supersonic mMediationAgent;
    String mUserId;


    public AdsManagerAndroid(AndroidLauncher androidLauncherIn) {
        androidLauncher = androidLauncherIn;
        //Get the mediation publisher instance
        mMediationAgent = SupersonicFactory.getInstance();

        AdsListener listener = new AdsListener(androidLauncher);

        //Set the Rewarded Video Listener
        // may be unnecessary, can just call mMediationAgent.isRewardedVideoAvailable();
        // http://developers.supersonic.com/hc/en-us/articles/201321042-Integrating-Rewarded-Video
        mMediationAgent.setRewardedVideoListener(listener);

        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                //Set the unique id of your end user.
                // This can't be called from main thread, should be done asynchronously?
                AdvertisingIdClient.Info adInfo = null;
                try {
                    adInfo = AdvertisingIdClient.getAdvertisingIdInfo(androidLauncher.getContext());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                }

                setUserId(adInfo.getId());
                return adInfo.getId();
            }

            @Override
            protected void onPostExecute(String token) {
//                Log.i(TAG, "Access token retrieved:" + token);
                System.out.println("token retrieved: " + token);
            }

        };
        task.execute();

    }

    public void setUserId(String id) {
        mUserId = id;

//        String mUserId =  "1502010";

        //Set the Application Key - can be retrieved from Supersonic platform
        String mAppKey = "3b98e7d1";
        System.out.println("Supersonic userid is: " + mUserId);

        //Init Rewarded Video
        mMediationAgent.initRewardedVideo(androidLauncher, mAppKey, mUserId);

        IntegrationHelper.validateIntegration(androidLauncher);
    }

    @Override
    /** Calls AdsHandler.handleAdCompleted on success
     *  calls nothing on failure
     */
    public void showAd() {
        if (mMediationAgent.isRewardedVideoAvailable()) {
            mMediationAgent.showRewardedVideo();
            AdsHandler.handleAdWatched();
        }
        else {
            System.out.println("No rewarded video available!");
            AdsHandler.handleAdNotAvailable();
            return;
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

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
    }

    @Override
    public void onDestroy() {

    }
}
