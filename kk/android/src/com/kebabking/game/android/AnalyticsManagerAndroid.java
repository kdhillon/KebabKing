package com.kebabking.game.android;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.analytics.ecommerce.ProductAction;
import com.kebabking.game.Assets;
import com.kebabking.game.Managers.AnalyticsManager;
import com.google.android.gms.analytics.Tracker;

import android.util.Log;

import im.delight.apprater.AppRater;

/**
 * Created by Kyle on 11/10/2015.
 */
public class AnalyticsManagerAndroid implements AnalyticsManager {
    private static final String TRACKING_ID = "UA-61310957-1";

    AndroidLauncher androidLauncher;
    Tracker tracker;

    public AnalyticsManagerAndroid(AndroidLauncher androidLauncher) {
        this.androidLauncher = androidLauncher;

        GoogleAnalytics analytics = GoogleAnalytics.getInstance(androidLauncher);
        tracker = analytics.newTracker(TRACKING_ID);
        tracker.enableAdvertisingIdCollection(true);
    }

    @Override
    public void promptForRating() {
        System.out.println("promptForRating");
        androidLauncher.runOnUiThread(new Runnable() {
            public void run() {
                AppRater appRater = new AppRater(androidLauncher.getContext());
                appRater.setDaysBeforePrompt(0);
                appRater.setLaunchesBeforePrompt(0);
                appRater.setPhrases(
                        Assets.strings.get("rating_title"),
                        Assets.strings.get("rating_desc"),
                        Assets.strings.get("rate_now"),
                        Assets.strings.get("not_now"),
                        Assets.strings.get("never"));
                appRater.show();
                appRater.demo();
                System.out.println("prompted for rating");
            }
        });
    }

    @Override
    public void sendScreenHit(String screenName) {
        Log.i("SEND_HIT", "screen: " + screenName);

        tracker.setScreenName(screenName);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void sendEventHit(String category, String action) {
        Log.i("SEND_HIT", "event: " + category + "/" + action);

        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .build());
    }

    @Override
    public void sendEventHit(String category, String action, String label) {
        Log.i("SEND_HIT", "event: " + category + "/" + action + "/" + label);

        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setLabel(label)
                .build());
    }

    @Override
    public void sendEventHit(String category, String action, String label, long value) {
        Log.i("SEND_HIT", "event: " + category + "/" + action + "/" + label +"/" + value);

        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setLabel(label)
                .setValue(value)
                .build());
    }

    // This is called from other android methods, so don't have to deal with imports.
    public void sendPaymentHit(Product product, ProductAction productAction) {
        Log.i("SEND_HIT", "product: " + product.toString() + "/" + productAction.toString());

        tracker.send(new HitBuilders.EventBuilder()
                .addProduct(product)
                .setProductAction(productAction)
                .build());
    }

    @Override
    public void sendUserTiming(String eventName, long milliseconds) {
        Log.i("SEND_TIMING", "eventName: " + eventName + " Time: " + milliseconds);

        tracker.send(new HitBuilders.TimingBuilder()
                .setCategory("Timing")
                .setValue(milliseconds)
                .setVariable(eventName)
                .setLabel("")
                .build());
    }
}
