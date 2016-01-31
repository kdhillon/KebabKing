package com.kebabking.game.Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.ScreenUtils;

import java.io.InputStream;

public interface SocialMediaManager {
    // Launch login panel
//    public void login();

    // Attempt logout
//    public void logout();

    public void shareScreenshot(InputStream imageStream);
    public void shareDayComplete(float profit);

    // Launch invite friends panel
    // TODO return # of new invites sent or something
    public void inviteFriends();

    // Is the user currently logged in?
//    public boolean isLoggedIn();

    // Stuff to do on pause/resume
    public void onResume();
    public void onPause();
}
