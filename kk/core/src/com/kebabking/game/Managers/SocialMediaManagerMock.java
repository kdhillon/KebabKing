package com.kebabking.game.Managers;

import com.kebabking.game.SocialMediaHandler;

import java.io.InputStream;

public class SocialMediaManagerMock implements SocialMediaManager {
//    boolean loggedIn;

//    // Launch login panel
//    public void login() {
//        System.out.println("login() mock");
//        loggedIn = true;
//    }
//
//    // Attempt logout
//    public void logout() {
//        System.out.println("inviteFriends() mock");
//        loggedIn = false;
//    }

    // share summary screen
    public void shareScreenshot(InputStream imageStream) {
        System.out.println("share() mock");
        SocialMediaHandler.shareSuccess();
    }
    public void shareDayComplete(float profit) {
        System.out.println("share() mock");
        SocialMediaHandler.handleAboutToLaunchShare();
        SocialMediaHandler.shareSuccess();
    }

    // Launch invite friends panel
    // TODO return # of new invites sent or something
    public void inviteFriends() {
        System.out.println("inviteFriends() mock");
    }

    // Is the user currently logged in?
//    public boolean isLoggedIn() {
//        return loggedIn;
//    }

    // Stuff to do on pause/resume
    public void onResume() {}
    public void onPause() {}
}
