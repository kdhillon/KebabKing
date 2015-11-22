package com.kebabchef.game.android;

import android.util.Log;

import com.chuanrchef.game.Managers.Manager;
import com.chuanrchef.game.OnlinePurchaseManager;
import com.chuanrchef.game.Managers.IABManager;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.analytics.ecommerce.ProductAction;
import com.kebabchef.game.android.util.IabHelper;
import com.kebabchef.game.android.util.IabResult;
import com.kebabchef.game.android.util.Purchase;

/**
 * Created by Kyle on 11/8/2015.
 */
public class IABManagerAndroid implements IABManager {
    private static final String TAG = "IAB Manager";

    private static final String IAB_REAL_KEY1 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAo8FEKsoT1p1ZdQa/8V3fNmzke7rfP6rUWWwEYC+Bm+Rk2XM2AiBtQUxWxP2a1U668aUYXGE8WnDlXXie4iV1Vd/uIUZtk5/DWOcanK1np7zpO+Ck47bLB7alMUbS0JWstC3fXRuVZsB4IuPhNGjPXWvmYhafnNJuaW+mWgD0STPl4H8iMMtk+3U5MMwi1xqYOsenP6EzDB2Q7I4UKd3jG9DVcz4lboxBmvU";
    private static final String IAB_REAL_KEY2 = "rmGGtTEVtNJ1EtyXU3PPr8tKuAzoTyangtELeCFHR/PLcgWSf8doNaiqGa5xF6PLrDeb+n+rVyav+c0rX1nkzz3kzALv55byjGmjJwyhBVGPXfGt6kQIDAQAB";

    private static final int REQUEST_CODE = 420;
    private static final String DEVELOPER_PAYLOAD = "vKRqq+JTFn4uQZbPiQJo4pf";

    AndroidLauncher androidLauncher;

    IabHelper iabHelper; // In-App Billing helper

    public IABManagerAndroid(AndroidLauncher androidLauncher) {
        this.androidLauncher = androidLauncher;

        this.iabHelper = new IabHelper(androidLauncher, IAB_REAL_KEY1+IAB_REAL_KEY2);
        iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    Log.d(TAG, "Problem setting up In-app Billing: " + result);
                }
                // Hooray, IAB is fully set up!
            }
        });
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

        iabHelper.launchPurchaseFlow(androidLauncher, productID, REQUEST_CODE, mPurchaseFinishedListener, "");
    }

    private void consumePurchase(Purchase purchase) {
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

    private void confirmPurchase(Purchase purchase) {
        androidLauncher.game.confirmPurchase(purchase.getSku());

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

//        Manager.analytics.sendPaymentHit(product, productAction);
    }

    @Override
    public void onDestroy() {
        if (iabHelper != null) iabHelper.dispose();
        iabHelper = null;
    }
}
