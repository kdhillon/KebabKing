package com.kebabking.game.Managers;

public interface AdsManager {
    // prepare an ad
    public void cacheAd();

    // Display an ad
    public void showAd();

    // Stuff to do on pause/resume
    public void onResume();
    public void onPause();
    public void onStart();
    public void onStop();
    public void onDestroy();
}
