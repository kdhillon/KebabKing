package com.chuanrchef.game.Managers;

public interface FacebookManager {
    // Launch login panel
    public void login();

    // Attempt logout
    public void logout();

    // Launch invite friends panel
    // TODO return # of new invites sent or something
    public void inviteFriends();

    // Is the user currently logged in?
    public boolean isLoggedIn();

    // Stuff to do on pause/resume
    public void onResume();
    public void onPause();
}
