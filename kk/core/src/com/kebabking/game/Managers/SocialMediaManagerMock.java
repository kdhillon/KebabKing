package com.kebabking.game.Managers;

import java.io.InputStream;

import com.kebabking.game.KebabKing;
import com.kebabking.game.SocialMediaHandler;

public class SocialMediaManagerMock implements SocialMediaManager {
//    boolean loggedIn;

//    // Launch login panel
//    public void login() {
//        KebabKing.print("login() mock");
//        loggedIn = true;
//    }
//
//    // Attempt logout
//    public void logout() {
//        KebabKing.print("inviteFriends() mock");
//        loggedIn = false;
//    }

    // share summary screen
    public void shareScreenshot(InputStream imageStream) {
        KebabKing.print("share() mock");
        SocialMediaHandler.shareSuccess();
    }
    public void shareDayComplete(float profit) {
        KebabKing.print("share() mock");
        SocialMediaHandler.handleAboutToLaunchShare();
        SocialMediaHandler.shareSuccess();
    }

    // Launch invite friends panel
    // TODO return # of new invites sent or something
    public void inviteFriends() {
        KebabKing.print("inviteFriends() mock");
    }

    // Is the user currently logged in?
//    public boolean isLoggedIn() {
//        return loggedIn;
//    }

    // Stuff to do on pause/resume
    public void onResume() {}
    public void onPause() {}
}
