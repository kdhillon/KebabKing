package com.kebabking.game;

import java.io.FileNotFoundException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.kebabking.game.Managers.Manager;

/**
 * Created by Kyle on 1/21/2016.
 */
public class SocialMediaHandler {
	static final int DAYS_BETWEEN_SHARES = 3;
    static final long MILLIS_TO_WAIT_BEFORE_REWARD = 1000; 
    public static String FILENAME = "temp.png";

    static long sharedAt;
    static boolean justShared;
    static KebabKing master;

    public static void init(KebabKing masterIn) {
        master = masterIn;
    }

    public static void shareSuccess() {
        System.out.println("Share Success in SocialMediaHandler");
        justShared = true;
        sharedAt = TimeUtils.millis();
        if (master.summary != null) {
        	master.summary.clearShareTable();
        }
    }

    public static void reward() {
        int jade = SummaryScreen.SHARE_REWARD;
        master.profile.giveCoins(jade);

        DrawUI.launchShareSuccessNotification(jade);
        Manager.analytics.sendEventHit("Social", "day shared");
        System.out.println("Share rewarded!");

        master.profile.gameQuitDuringShare = false;
        master.save();
    }

    public static void checkIfShouldReward() {
        if (justShared && TimeUtils.millis() - sharedAt > MILLIS_TO_WAIT_BEFORE_REWARD) {
            reward();
            justShared = false;
            sharedAt = 0;
        }
    }
    
    public static boolean checkIfShouldAllowShare() {
    	System.out.println(master.profile.stats.lastShareDay);
    	return (master.profile.stats.daysWorked - master.profile.stats.lastShareDay >= DAYS_BETWEEN_SHARES);
    }

    public static void handleAboutToLaunchShare() {
    	System.out.println("HANDLE ABOUT TO LAUNCH SHARE");
        master.profile.stats.lastShareDay = master.profile.stats.daysWorked;
        master.profile.gameQuitDuringShare = true;
        // need to save here!
        master.save();
    }

    private static void saveScreenshot() {
        byte[] pixels = ScreenUtils.getFrameBufferPixels(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        Pixmap pixmap = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Pixmap.Format.RGBA8888);
        BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);
        try {
            PixmapIO.writePNG(Manager.file.getTempOutputHandle(), pixmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        pixmap.dispose();
        System.out.println("Screenshot saved");
    }

    public static void shareScreenshot() {
        saveScreenshot();
        try {
            Manager.fb.shareScreenshot(Manager.file.getTempInputStream());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
