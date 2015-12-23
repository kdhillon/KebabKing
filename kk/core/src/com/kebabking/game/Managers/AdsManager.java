package com.kebabking.game.Managers;

public interface AdsManager {
    // Display an ad
    // TODO return info about ad completion.
    public boolean showAd();

    // Stuff to do on pause/resume
    public void onResume();
    public void onPause();
}
