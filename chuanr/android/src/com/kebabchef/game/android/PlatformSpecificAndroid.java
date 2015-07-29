package com.kebabchef.game.android;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.chuanrchef.game.ChuanrC;
import com.chuanrchef.game.PlatformSpecific;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.analytics.ecommerce.ProductAction;

public class PlatformSpecificAndroid implements PlatformSpecific {
	UiLifecycleHelper uiHelper;
	AndroidLauncher androidLauncher;

	public PlatformSpecificAndroid(AndroidLauncher androidLauncher) {
		this.androidLauncher = androidLauncher;

		this.uiHelper = new UiLifecycleHelper(androidLauncher, new Session.StatusCallback() {
			@Override
			public void call(Session session, SessionState sessionState, Exception e) {
				onSessionStateChange(session, sessionState, e);
			}
		});
	}

	@Override
	public void logIn() {
		List<String> permissions = new ArrayList<String>();
		permissions.add("user_friends");

		Session.openActiveSession(androidLauncher, true, permissions, new Session.StatusCallback() {
			// callback when session changes state
			@Override
			public void call(Session session, SessionState state, Exception exception) {
				onSessionStateChange(session, state, exception);

				if (session.isOpened()) {
					if (!session.getPermissions().contains("user_friends")) {
						Session.NewPermissionsRequest newPermissionsRequest = new Session
								.NewPermissionsRequest(androidLauncher, Arrays.asList("user_friends"));
						session.requestNewReadPermissions(newPermissionsRequest);
					} else {
						System.out.println("line 40");
					}
				}
			}
		}
				);
	}

	@Override
	public void inviteFriends() {
		if (isLoggedIn()) {
			final Bundle params = new Bundle();
			params.putString("message", "Come play Kebab Chef!");
			params.putString("filters", "app_non_users");
			params.putString("title", "Invite Friends!");

//			//This part is optional
            params.putString("action_type", "send");		   
            params.putString("object_id", "536948939741609");
			
			androidLauncher.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					WebDialog requestsDialog = (
							new WebDialog.RequestsDialogBuilder(androidLauncher,
						            Session.getActiveSession(),
						            params))
						            .setOnCompleteListener(new OnCompleteListener() {

						                public void onComplete(Bundle values,
						                    FacebookException error) {
						                    if (error != null) {
						                        if (error instanceof FacebookOperationCanceledException) {
						                            Toast.makeText(androidLauncher.getApplicationContext(), 
						                                "Request cancelled", 
						                                Toast.LENGTH_SHORT).show();
						                        } else {
						                            Toast.makeText(androidLauncher.getApplicationContext(), 
						                                "Network Error", 
						                                Toast.LENGTH_SHORT).show();
						                        }
						                    } else {
						                        final String requestId = values.getString("request");
						                        if (requestId != null) {
						                            Toast.makeText(androidLauncher.getApplicationContext(), 
						                                "Request sent",  
						                                Toast.LENGTH_SHORT).show();
						                        } else {
						                            Toast.makeText(androidLauncher.getApplicationContext(), 
						                                "Request cancelled", 
						                                Toast.LENGTH_SHORT).show();
						                        }
						                    }   
						                }

						            })
						            .build();
						    requestsDialog.show();
				}
			});

		}
	}

	// useful for facebook changes
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		if (state.isOpened()) {
			Log.i("RANDOM", "Logged in...");
		} else if (state.isClosed()) {
			Log.i("RANDOM", "Logged out...");
		}
	}

	@Override
	public boolean isLoggedIn() {
		//		Log.i("MAIN_ACTIVITY", "first half: " + (Session.getActiveSession() != null));
		//		if (Session.getActiveSession() != null) Log.i("MAIN_ACTIVITY", "second half: " + Session.getActiveSession().isOpened());
		return Session.getActiveSession() != null && Session.getActiveSession().isOpened();
	}

	@Override
	public void logOut() {
		if (isLoggedIn()) {
			Session.getActiveSession().closeAndClearTokenInformation();
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public OutputStream getOutputStream() throws FileNotFoundException {
		Context ctx = androidLauncher.getContext();
		return ctx.openFileOutput(ChuanrC.SAVE_FILENAME, Context.MODE_PRIVATE);
	}    
	
	public InputStream getInputStream() throws FileNotFoundException {
		Context ctx = androidLauncher.getContext();
		return ctx.openFileInput(ChuanrC.SAVE_FILENAME);
	}

	@Override
	public boolean saveFileExists() {
		Context ctx = androidLauncher.getContext();
		File file = ctx.getFileStreamPath(ChuanrC.SAVE_FILENAME);
		if (file == null || !file.exists()) return false;
		return true;
	}

	@Override
	public void deleteProfile() {
		Context ctx = androidLauncher.getContext();
		ctx.deleteFile(ChuanrC.SAVE_FILENAME);
	}
	
	// all products are immediately consumed
	@Override
	public void makePurchase(String productID) {
//		androidLauncher.makePurchase(productID);
		
		// for now, just force a purchase
		this.androidLauncher.game.confirmPurchase(productID);
	}
	
	@Override
	public void sendScreenHit(String screenName) {
		Log.i("SEND_HIT", "screen: " + screenName);
		
		androidLauncher.tracker.setScreenName(screenName);
		androidLauncher.tracker.send(new HitBuilders.ScreenViewBuilder().build());
	}
	
	@Override
	public void sendEventHit(String category, String action) {
		Log.i("SEND_HIT", "event: " + category + "/" + action);
		
		androidLauncher.tracker.send(new HitBuilders.EventBuilder()
	    .setCategory(category)
	    .setAction(action)
	    .build());
	}
	
	@Override
	public void sendEventHit(String category, String action, String label) {
		Log.i("SEND_HIT", "event: " + category + "/" + action + "/" + label);
		
		androidLauncher.tracker.send(new HitBuilders.EventBuilder()
	    .setCategory(category)
	    .setAction(action)
	    .setLabel(label)
	    .build());
	}
	
	public void sendEventHit(String category, String action, String label, long value) {
		Log.i("SEND_HIT", "event: " + category + "/" + action + "/" + label +"/" + value);

		androidLauncher.tracker.send(new HitBuilders.EventBuilder()
	    .setCategory(category)
	    .setAction(action)
	    .setLabel(label)
	    .setValue(value)
	    .build());
	}
	
	@Override
	public void sendPaymentHit(Product product, ProductAction productAction) {
		Log.i("SEND_HIT", "product: " + product.toString() + "/" + productAction.toString());
		
		androidLauncher.tracker.send(new HitBuilders.EventBuilder()
	    .addProduct(product)
	    .setProductAction(productAction)
	    .build());
	}
	
	@Override
	public void sendUserTiming(String eventName, long milliseconds) {
		Log.i("SEND_TIMING", "eventName: " + eventName + " Time: " + milliseconds);
				
		androidLauncher.tracker.send(new HitBuilders.TimingBuilder()
	    .setCategory("Timing")
	    .setValue(milliseconds)
	    .setVariable(eventName)
	    .setLabel("")
	    .build());	
	}
}
