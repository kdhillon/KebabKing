package com.kebabking.game.Managers;

public class FacebookManagerMock implements FacebookManager {
    boolean loggedIn;

    // Launch login panel
    public void login() {
        System.out.println("login() mock");
        loggedIn = true;
    }

    // Attempt logout
    public void logout() {
        System.out.println("inviteFriends() mock");
        loggedIn = false;
    }

    // Launch invite friends panel
    // TODO return # of new invites sent or something
    public void inviteFriends() {
        System.out.println("inviteFriends() mock");
    }

    // Is the user currently logged in?
    public boolean isLoggedIn() {
        return loggedIn;
    }

    // Stuff to do on pause/resume
    public void onResume() {}
    public void onPause() {}
}
