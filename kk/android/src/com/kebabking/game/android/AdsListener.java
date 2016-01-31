package com.kebabking.game.android;

import com.kebabking.game.AdsHandler;
import com.supersonic.mediationsdk.logger.SupersonicError;
import com.supersonic.mediationsdk.model.Placement;
import com.supersonic.mediationsdk.sdk.RewardedVideoListener;

/**
 * Created by Kyle on 12/22/2015.
 */
public class AdsListener implements RewardedVideoListener {

    private AndroidLauncher androidLauncher;
    public AdsListener(AndroidLauncher androidLauncher) {
        this.androidLauncher = androidLauncher;
    }

    @Override
    public void onRewardedVideoInitSuccess() {

    }

    @Override
    public void onRewardedVideoInitFail(SupersonicError supersonicError) {
        System.out.println("SUPERSONIC INIT ERROR: " + supersonicError.getErrorMessage());
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoAdClosed() {

    }

    @Override
    public void onVideoAvailabilityChanged(boolean available) {
        System.out.println("SUPERSONIC VIDEO AVAILABILITY: " + available);
    }

    @Override
    public void onVideoStart() {

    }

    @Override
    public void onVideoEnd() {

    }

    @Override
    public void onRewardedVideoAdRewarded(Placement placement) {
        System.out.println("onRewardedVideoAdRewarded!");
        AdsHandler.handleAdJustWatched();
    }

    @Override
    public void onRewardedVideoShowFail(SupersonicError supersonicError) {
        System.out.println("SUPERSONIC SHOW ERROR: " + supersonicError.getErrorMessage());
    }
}
