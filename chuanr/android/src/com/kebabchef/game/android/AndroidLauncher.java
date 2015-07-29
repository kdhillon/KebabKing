package com.kebabchef.game.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.chuanrchef.game.ChuanrC;
import com.chuanrchef.game.OnlinePurchaseManager;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.analytics.ecommerce.ProductAction;
import com.kebabchef.game.android.util.IabHelper;
import com.kebabchef.game.android.util.IabResult;
import com.kebabchef.game.android.util.Purchase;

public class AndroidLauncher extends AndroidApplication {
	private static final String TRACKING_ID = "UA-61310957-1";
	private static final String TAG = "KC";
//	private static final String RSAKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiBQTDgLUNAIhK0EzK0zYt+Sk2xBG/myOWmnT5jctL6OHHe8vA0mEz0L1fHE6PDhN/oab5emC4pIGohfJPYAo5Ou5yH+0WMtOihq/55Mx9JeCqh4bZy04pzqg8vpMHJjQbHJBy1vKYdLnZAFXLr3Sojn/vcTQzZ9/MJt+of4Q3VJguaJHfN4De5DV1yHjsyTofGbDMkV1ROPNlOtPFyW/uDGw1fuIWB0roQNtJCB4LmYbNTD";
//	private static final String RSAKey2 = "6ZqlcGm+jBYLzvWXWCNAd+0jj6/BsqW4v+2NVDwf7zQe4Z7mDy1LlFW148SK5xKd7IeA0WbtSvOb666GZu8pn76GyS3fLjwbg4dDDnQIDAQAB";
	
	private static final String REAL_KEY1 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAo8FEKsoT1p1ZdQa/8V3fNmzke7rfP6rUWWwEYC+Bm+Rk2XM2AiBtQUxWxP2a1U668aUYXGE8WnDlXXie4iV1Vd/uIUZtk5/DWOcanK1np7zpO+Ck47bLB7alMUbS0JWstC3fXRuVZsB4IuPhNGjPXWvmYhafnNJuaW+mWgD0STPl4H8iMMtk+3U5MMwi1xqYOsenP6EzDB2Q7I4UKd3jG9DVcz4lboxBmvU";
	private static final String REAL_KEY2 = "rmGGtTEVtNJ1EtyXU3PPr8tKuAzoTyangtELeCFHR/PLcgWSf8doNaiqGa5xF6PLrDeb+n+rVyav+c0rX1nkzz3kzALv55byjGmjJwyhBVGPXfGt6kQIDAQAB";

	private static final int REQUEST_CODE = 420;
	private static final String DEVELOPER_PAYLOAD = "vKRqq+JTFn4uQZbPiQJo4pf";
	
	Tracker tracker;
	ChuanrC game;
	PlatformSpecificAndroid ps; // needed for facebook login
	IabHelper iabHelper; // In-App Billing helper

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		ps = new PlatformSpecificAndroid(this);
		game = new ChuanrC();
		game.setPlatformSpecific(ps);

		config.useWakelock = true; // forces screen to stay on, remove later TODO
		initialize(game, config);

		this.iabHelper = new IabHelper(this, REAL_KEY1+REAL_KEY2);
		iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				if (!result.isSuccess()) {
					// Oh noes, there was a problem.
					Log.d(TAG, "Problem setting up In-app Billing: " + result);
				}
				// Hooray, IAB is fully set up!
			}
		});
		
		GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
		tracker = analytics.newTracker(TRACKING_ID);
		tracker.enableAdvertisingIdCollection(true);
	}

	public void makePurchase(String productID) {
		IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener 
		= new IabHelper.OnIabPurchaseFinishedListener() {
			public void onIabPurchaseFinished(IabResult result, Purchase purchase) 
			{
				if (purchase == null || purchase.getDeveloperPayload() != DEVELOPER_PAYLOAD || 
						result.isFailure()) {
					Log.d(TAG, "Error purchasing: " + result);
					return;
				}      
				else  {
					// before I skipped the important consume step
					consumePurchase(purchase);
				}
			}
		};

	    if (iabHelper != null) iabHelper.flagEndAsync();
		
	    iabHelper.launchPurchaseFlow(this, productID, REQUEST_CODE, mPurchaseFinishedListener, "");
	}
	
	public void consumePurchase(Purchase purchase) {
		IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
				new IabHelper.OnConsumeFinishedListener() {
			public void onConsumeFinished(Purchase purchase, IabResult result) {
				if (result.isSuccess()) {
					// provision the in-app purchase to the user
					// (for example, credit 50 gold coins to player's character)
					confirmPurchase(purchase);
				}
				else {
					// handle error
					Log.d(TAG, "Error consuming: " + result);
					return;
				}
			}
		};

	    if (iabHelper != null) iabHelper.flagEndAsync();
		iabHelper.consumeAsync(purchase, 
				mConsumeFinishedListener);
	}
	
	public void confirmPurchase(Purchase purchase) {
		this.game.confirmPurchase(purchase.getSku());
		
		Product product =  new Product()
	    .setId(purchase.getSku())
	    .setName(purchase.getSku())
	    .setCategory(purchase.getItemType())
//	    .setBrand("Google")
//	    .setVariant("black")
	    .setPrice(OnlinePurchaseManager.getPurchaseableForID(purchase.getSku()).price)
//	    .setCouponCode("APPARELSALE")
	    .setQuantity(1);
		
		ProductAction productAction = new ProductAction(ProductAction.ACTION_PURCHASE)
	    .setTransactionId(purchase.getOrderId())
	    .setTransactionAffiliation("Google Play");
		
		ps.sendPaymentHit(product, productAction);
	}

	// required for facebook login
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ps.onActivityResult(requestCode, resultCode, data);       
	}

	// dispose of IAB for cleanliness
	@Override
	public void onDestroy() {
		System.out.println("DESTROYING APP");
		this.ps.sendUserTiming("Total Activity Time", System.currentTimeMillis() - game.activityStartTime);

		super.onDestroy();
		if (iabHelper != null) iabHelper.dispose();
		iabHelper = null;
	}
}
