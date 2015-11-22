package com.chuanrchef.game.Managers;

/**
 * Created by Kyle on 11/10/2015.
 */
public interface AnalyticsManager {
    public void sendScreenHit(String screenName);

    public void sendEventHit(String category, String action);

    public void sendEventHit(String category, String action, String label);

    public void sendEventHit(String category, String action, String label, long value);

    public void sendUserTiming(String eventName, long milliseconds);
}
