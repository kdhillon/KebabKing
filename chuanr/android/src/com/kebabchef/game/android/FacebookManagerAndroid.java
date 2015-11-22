//package com.kebabchef.game.android;
//
//import com.chuanrchef.game.Managers.FacebookManager;
//import com.facebook.AccessToken;
//import com.facebook.CallbackManager;
//import com.facebook.FacebookAuthorizationException;
//import com.facebook.FacebookCallback;
//import com.facebook.FacebookException;
//import com.facebook.FacebookSdk;
//import com.facebook.appevents.AppEventsLogger;
//import com.facebook.login.LoginManager;
//import com.facebook.login.LoginResult;
//import com.facebook.share.model.AppInviteContent;
//import com.facebook.share.model.ShareLinkContent;
//import com.facebook.share.widget.AppInviteDialog;
//import com.facebook.share.widget.ShareDialog;
//
//import android.content.Intent;
//import android.util.Log;
//import android.widget.Toast;
//
//import java.util.ArrayList;
//
//public class FacebookManagerAndroid implements FacebookManager {
//	  // TODO change to chuanr app url
//    private final String appLinkUrl = "https://fb.me/1514946498816794";
//
//    AndroidLauncher androidLauncher;
//    CallbackManager callbackManager;
//
//    public FacebookManagerAndroid(AndroidLauncher androidLauncher) {
//        this.androidLauncher = androidLauncher;
//
//        FacebookSdk.sdkInitialize(androidLauncher.getApplicationContext());
//
//        callbackManager = CallbackManager.Factory.create();
//    }
//
//    public void login() {
//        //Login Callback registration
//        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                Toast.makeText(androidLauncher.getApplicationContext(), "in LoginResult on success", Toast.LENGTH_LONG).show();
//                Log.d("FB", "Logged in successfully");
//
//                // Not sure what the fuck this is?
//                //Login success - process to Post
////                if (ShareDialog.canShow(ShareLinkContent.class)) {
////                    String description = "description";
////                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
////                            .setContentTitle("title")
////                            .setContentDescription(description)
////                            .setContentUrl(Uri.parse("http://google.com"))
////                            .setImageUrl(Uri.parse("http://google.com"))
////                            .build();
////
//////					shareDialog.show(linkContent, ShareDialog.Mode.FEED);
////                }
//            }
//
//            @Override
//            public void onCancel() {
//                // for some reason, when loggin in the first time, automatically cancels the install.
//                Log.d("FB", "Login was cancelled!");
//                Toast.makeText(androidLauncher.getApplicationContext(), "in LoginResult on cancel", Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onError(FacebookException exception) {
//                Log.d("FB", "On error " + exception.toString());
//                Toast.makeText(androidLauncher.getApplicationContext(), "in LoginResult on error", Toast.LENGTH_LONG).show();
//            }
//        });
//
//        if (!isLoggedIn()) {
//            ArrayList<String> permissions = new ArrayList<String>();
//            permissions.add("user_friends");
//            LoginManager.getInstance().logInWithReadPermissions(androidLauncher, permissions); //Log in to FB
//        } else {
//            Log.d("FB", "User already logged in!");
//        }
//    }
//
//    public void logout() {
//
//    }
//
//    // You don't actually have to be logged in to do this!!!
//    // Potentially can avoid needing to login at all.
//    public void inviteFriends() {
//        Log.d("Invite", "Trying to invite friends");
//        AppInviteDialog mInvititeDialog = new AppInviteDialog(androidLauncher);
//        mInvititeDialog.registerCallback(callbackManager,
//                new FacebookCallback<AppInviteDialog.Result>() {
//
//                    // users will only be able to see this on their mobile device
//                    @Override
//                    public void onSuccess(AppInviteDialog.Result result) {
//                        Log.d("Result", "Invite Success");
//                        Toast.makeText(androidLauncher.getApplicationContext(), "Invitation Sent Successfully!", Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onCancel() {
//                        Log.d("Result", "Invite Cancelled");
//                        Toast.makeText(androidLauncher.getApplicationContext(), "Invite cancelled!", Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onError(FacebookException exception) {
//                        Log.d("Result", "Invite error " + exception.toString());
//                        Toast.makeText(androidLauncher.getApplicationContext(), "Invite error " + exception.toString(), Toast.LENGTH_LONG).show();
//
//                        if (exception instanceof FacebookAuthorizationException) {
//                            if (AccessToken.getCurrentAccessToken() != null) {
//                                LoginManager.getInstance().logOut();
//                                if (!isLoggedIn())
//                                    inviteFriends();
//                            }
//                        }
//                    }
//                });
//
//        if (AppInviteDialog.canShow()) {
//            AppInviteContent content = new AppInviteContent.Builder()
//                    .setApplinkUrl(appLinkUrl)
//                    .build();
//            AppInviteDialog.show(androidLauncher, content);
//        } else {
//            Log.d("FB", "Can't show app invite dialog");
//        }
//    }
//
//    public boolean isLoggedIn() {
//        return AccessToken.getCurrentAccessToken() != null;
//    }
//
//    public void onResume() {
//        AppEventsLogger.activateApp(androidLauncher);
//    }
//
//    public void onPause() {
//        AppEventsLogger.deactivateApp(androidLauncher);
//    }
//
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//    }
//
//}
