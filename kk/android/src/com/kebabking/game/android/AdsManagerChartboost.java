package com.kebabking.game.android;

import com.chartboost.sdk.Libraries.CBLogging.Level;
import com.chartboost.sdk.Model.CBError.CBClickError;
import com.chartboost.sdk.Model.CBError.CBImpressionError;
import com.chartboost.sdk.Tracking.CBAnalytics;
import com.chartboost.sdk.CBLocation;
import com.chartboost.sdk.CBImpressionActivity;
import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.ChartboostDelegate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.kebabking.game.AdsHandler;
import com.kebabking.game.Managers.AdsManager;
import com.supersonic.mediationsdk.integration.IntegrationHelper;
import com.supersonic.mediationsdk.sdk.Supersonic;
import com.supersonic.mediationsdk.sdk.SupersonicFactory;

import java.io.IOException;

public class AdsManagerChartboost implements AdsManager {
    static final String TAG = "CHARTBOOST";
    // TODO change for KK
    static final String CHARTBOOST_APP_ID = "56798750f789820e60403d70";
    static final String CHARTBOOST_APP_SIGNATURE = "6a042a52116488af98293227f61f198141682ccd";

    final AndroidLauncher androidLauncher;

    public AdsManagerChartboost(AndroidLauncher androidLauncherIn) {
        androidLauncher = androidLauncherIn;

        //Set the Rewarded Video Listener
        // may be unnecessary, can just call mMediationAgent.isRewardedVideoAvailable();
        // http://developers.supersonic.com/hc/en-us/articles/201321042-Integrating-Rewarded-Video
        Chartboost.startWithAppId(androidLauncher, CHARTBOOST_APP_ID, CHARTBOOST_APP_SIGNATURE);

        Chartboost.setDelegate(delegate);

        Chartboost.onCreate(androidLauncher);
        Chartboost.setLoggingLevel(Level.NONE);
        System.out.println("chartboost initialized");
    }

    @Override
    public void showAd() {
        if (Chartboost.hasRewardedVideo(CBLocation.LOCATION_DEFAULT)) {
            System.out.println("showing chartboost ad");
            Chartboost.showRewardedVideo(CBLocation.LOCATION_DEFAULT);
            AdsHandler.handleAdWatched();
        }
        else {
            Chartboost.cacheRewardedVideo(CBLocation.LOCATION_DEFAULT);
            System.out.println("caching chartboost ad");
            if (Chartboost.hasRewardedVideo(CBLocation.LOCATION_DEFAULT)) {
                Chartboost.showRewardedVideo(CBLocation.LOCATION_DEFAULT);
                AdsHandler.handleAdWatched();
            }
            else {
                System.out.println("no chartboost ad available");
                AdsHandler.handleAdNotAvailable();
                return;
            }
        }
    }

    public void onStart() {
        Chartboost.onStart(androidLauncher);
    }

    public void onStop() {
        Chartboost.onStop(androidLauncher);
    }

    public void onResume() {
        Chartboost.onResume(androidLauncher);
    }

    public void onPause() {
        Chartboost.onPause(androidLauncher);
    }

    public void onDestroy() {
        Chartboost.onDestroy(androidLauncher);
    }

    private ChartboostDelegate delegate = new ChartboostDelegate() {

        @Override
        public boolean shouldRequestInterstitial(String location) {
            Log.i(TAG, "SHOULD REQUEST INTERSTITIAL '"+ (location != null ? location : "null"));
            return true;
        }

        @Override
        public boolean shouldDisplayInterstitial(String location) {
            Log.i(TAG, "SHOULD DISPLAY INTERSTITIAL '"+ (location != null ? location : "null"));
            return true;
        }

        @Override
        public void didCacheInterstitial(String location) {
            Log.i(TAG, "DID CACHE INTERSTITIAL '"+ (location != null ? location : "null"));
        }

        @Override
        public void didPauseClickForConfirmation(Activity activity) {
            super.didPauseClickForConfirmation(activity);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
            alertDialogBuilder.setMessage("Are 18 or older?");
            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            Chartboost.didPassAgeGate(true);
                        }
                    });
            alertDialogBuilder.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            Chartboost.didPassAgeGate(false);
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        @Override
        public void didFailToLoadInterstitial(String location, CBImpressionError error) {
            Log.i(TAG, "DID FAIL TO LOAD INTERSTITIAL '"+ (location != null ? location : "null")+ " Error: " + error.name());
//            Toast.makeText(androidLauncher.getContext(), "INTERSTITIAL '"+location+"' REQUEST FAILED - " + error.name(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void didDismissInterstitial(String location) {
            Log.i(TAG, "DID DISMISS INTERSTITIAL: "+ (location != null ? location : "null"));
        }

        @Override
        public void didCloseInterstitial(String location) {
            Log.i(TAG, "DID CLOSE INTERSTITIAL: "+ (location != null ? location : "null"));
        }

        @Override
        public void didClickInterstitial(String location) {
            Log.i(TAG, "DID CLICK INTERSTITIAL: "+ (location != null ? location : "null"));
        }

        @Override
        public void didDisplayInterstitial(String location) {
            Log.i(TAG, "DID DISPLAY INTERSTITIAL: " +  (location != null ? location : "null"));
        }

        @Override
        public boolean shouldRequestMoreApps(String location) {
            Log.i(TAG, "SHOULD REQUEST MORE APPS: " +  (location != null ? location : "null"));
            return true;
        }

        @Override
        public boolean shouldDisplayMoreApps(String location) {
            Log.i(TAG, "SHOULD DISPLAY MORE APPS: " + (location != null ? location : "null"));
            return true;
        }

        @Override
        public void didFailToLoadMoreApps(String location, CBImpressionError error) {
            Log.i(TAG, "DID FAIL TO LOAD MOREAPPS " +  (location != null ? location : "null")+ " Error: "+ error.name());
//            Toast.makeText(androidLauncher.getContext(), "MORE APPS REQUEST FAILED - " + error.name(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void didCacheMoreApps(String location) {
            Log.i(TAG, "DID CACHE MORE APPS: " +  (location != null ? location : "null"));
        }

        @Override
        public void didDismissMoreApps(String location) {
            Log.i(TAG, "DID DISMISS MORE APPS " +  (location != null ? location : "null"));
        }

        @Override
        public void didCloseMoreApps(String location) {
            Log.i(TAG, "DID CLOSE MORE APPS: "+  (location != null ? location : "null"));
        }

        @Override
        public void didClickMoreApps(String location) {
            Log.i(TAG, "DID CLICK MORE APPS: "+  (location != null ? location : "null"));
        }

        @Override
        public void didDisplayMoreApps(String location) {
            Log.i(TAG, "DID DISPLAY MORE APPS: " +  (location != null ? location : "null"));
        }

        @Override
        public void didFailToRecordClick(String uri, CBClickError error) {
            Log.i(TAG, "DID FAILED TO RECORD CLICK " + (uri != null ? uri : "null") + ", error: " + error.name());
//            Toast.makeText(androidLauncher.getContext(), "FAILED TO RECORD CLICK " + (uri != null ? uri : "null") + ", error: " + error.name(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public boolean shouldDisplayRewardedVideo(String location) {
            Log.i(TAG, String.format("SHOULD DISPLAY REWARDED VIDEO: '%s'",  (location != null ? location : "null")));
            return true;
        }

        @Override
        public void didCacheRewardedVideo(String location) {
            Log.i(TAG, String.format("DID CACHE REWARDED VIDEO: '%s'",  (location != null ? location : "null")));
        }

        @Override
        public void didFailToLoadRewardedVideo(String location,
                                               CBImpressionError error) {
//            Log.i(TAG, String.format("DID FAIL TO LOAD REWARDED VIDEO: '%s', Error:  %s",  (location != null ? location : "null"), error.name()));
//            Toast.makeText(androidLauncher.getContext(), String.format("DID FAIL TO LOAD REWARDED VIDEO '%s' because %s", location, error.name()), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void didDismissRewardedVideo(String location) {
            Log.i(TAG, String.format("DID DISMISS REWARDED VIDEO '%s'",  (location != null ? location : "null")));
        }

        @Override
        public void didCloseRewardedVideo(String location) {
            Log.i(TAG, String.format("DID CLOSE REWARDED VIDEO '%s'",  (location != null ? location : "null")));
        }

        @Override
        public void didClickRewardedVideo(String location) {
            Log.i(TAG, String.format("DID CLICK REWARDED VIDEO '%s'",  (location != null ? location : "null")));
        }

        @Override
        public void didCompleteRewardedVideo(String location, int reward) {
            Log.i(TAG, String.format("DID COMPLETE REWARDED VIDEO '%s' FOR REWARD %d",  (location != null ? location : "null"), reward));
        }

        @Override
        public void didDisplayRewardedVideo(String location) {
            Log.i(TAG, String.format("DID DISPLAY REWARDED VIDEO '%s' FOR REWARD", location));
        }

        @Override
        public void willDisplayVideo(String location) {
            Log.i(TAG, String.format("WILL DISPLAY VIDEO '%s", location));
        }

    };
}
