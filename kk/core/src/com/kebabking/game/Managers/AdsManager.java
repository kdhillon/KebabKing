package com.kebabking.game.Managers;

public interface AdsManager {
    // Display an ad
    // TODO return info about ad completion.
    public void showAd();

    // Stuff to do on pause/resume
    public void onResume();
    public void onPause();
    public void onStart();
    public void onStop();
    public void onDestroy();
}
