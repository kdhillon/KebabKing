package com.kebabking.game.android;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.ScreenUtils;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.kebabking.game.Assets;
import com.kebabking.game.Managers.FileManager;
import com.kebabking.game.Managers.SocialMediaManager;
import com.facebook.FacebookSdk;
import com.kebabking.game.SocialMediaHandler;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Kyle on 1/20/2016.

 https://developers.facebook.com/docs/android/getting-started
 */
public class SocialMediaFacebook implements SocialMediaManager {
    AndroidLauncher master;
    CallbackManager callbackManager;
    ShareDialog shareDialog;

    public SocialMediaFacebook(AndroidLauncher master) {
        this.master = master;
        FacebookSdk.sdkInitialize(master.getContext());

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(master);

        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                System.out.println("Sharing succeeded");
                SocialMediaHandler.shareSuccess();
            }

            @Override
            public void onCancel() {
                // do nothing
                System.out.println("Sharing cancelled");
            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        System.out.println("fb development hash key: " + FacebookSdk.getApplicationSignature(master.getContext()));
    }

    // Lucas was here, 1/24/16, 6:51 PM

    public void shareDayComplete(float profit) {
        System.out.println("Sharing day complete with " + profit + " profit");
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse("https://kebabking.io"))
                    .setContentTitle("I earned " + Assets.currencyChar + profit + " in Kebab King!")
                    .setContentDescription("Cook kebabs and serve your hungry customers in this awesome Android game!")
                    .setImageUrl(Uri.parse("https://lh3.googleusercontent.com/mqZXECxUdd8vEznthMNAU_sZBENxhDc4lVbzWE-P4jvYcPo0_GrGUoGK0oTJJl-NpGg=w300-rw"))
                    .build();

            ShareDialog.show(master, content);
        }
        else {
            System.out.println("Can't show link content");
        }
    }


    @Override
    public void shareScreenshot(InputStream imageStream) {
        System.out.println("Trying to share");
        // multiple steps:
        // first, check
        Bitmap image = BitmapFactory.decodeStream(imageStream);

        if (ShareDialog.canShow(SharePhotoContent.class)) {
            SharePhoto.Builder builder = new SharePhoto.Builder();
            builder.setBitmap(image);
            // the facebook api doesn't let us share captions.
            // might be better to share a link to the website or the app store listing instead of
            // a screenshot

            SharePhoto photo = builder.build();
            SharePhotoContent content = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .build();

            System.out.println("show share dialogue");


            ShareDialog.show(master, content);
        }
        else {
            System.out.println("Can't show share photo content");
        }
    }

//    @Override
//    public void login() {
//
//    }
//
//    @Override
//    public void logout() {
//
//    }

    @Override
    public void inviteFriends() {

    }

//    @Override
//    public boolean isLoggedIn() {
//        return false;
//    }

    // necessary for callbackmanager to work
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }
}
