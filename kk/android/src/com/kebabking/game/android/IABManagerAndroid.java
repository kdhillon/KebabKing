// followed these directions to get this to work:
// https://github.com/libgdx/libgdx/wiki/Setting-Up-Google-In-App-Billing
package com.kebabking.game.android;

import com.kebabking.game.OnlinePurchaseHandler;
import com.kebabking.game.Managers.IABManager;
import com.kebabking.game.android.util.IabHelper;
import com.kebabking.game.android.util.IabResult;
import com.kebabking.game.android.util.Inventory;

import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

    com.kebabking.game.android.util.IabHelper iabHelper; // In-App Billing helper

    public IABManagerAndroid(AndroidLauncher androidLauncher) {
        this.androidLauncher = androidLauncher;

        this.iabHelper = new com.kebabking.game.android.util.IabHelper(androidLauncher, IAB_REAL_KEY1+IAB_REAL_KEY2);
        iabHelper.startSetup(new com.kebabking.game.android.util.IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(com.kebabking.game.android.util.IabResult result) {
                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    Log.d(TAG, "Problem setting up In-app Billing: " + result);
                }
                // Hooray, IAB is fully set up!
                System.out.println("IAB helper set up successfully");
            }
        });
    }

    private String getPackageName() {
        return "com.kebabchef.game.android";
    }

    public void makePurchase(String productID) {
        // first step: query purchased items and make sure our item is not there
        String purchaseToken = "inapp:"+getPackageName()+":android.test.purchased";
        try {
            int response = iabHelper.getService().consumePurchase(3, getPackageName(),purchaseToken);
            System.out.println("Consumption response: " + response);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        System.out.println("Attempting to make purchase " + productID);
        com.kebabking.game.android.util.IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
                = new com.kebabking.game.android.util.IabHelper.OnIabPurchaseFinishedListener() {
            public void onIabPurchaseFinished(com.kebabking.game.android.util.IabResult result, com.kebabking.game.android.util.Purchase purchase)
            {
                if (purchase == null ||
                        result.isFailure()) {
                    Log.d(TAG, "Error purchasing: " + result);
                }
                else  {
                    System.out.println("Consuming purchase");
                    // before I skipped the important consume step
                    consumePurchase(purchase);
                }
            }
        };

        if (iabHelper != null) iabHelper.flagEndAsync();
        iabHelper.launchPurchaseFlow(androidLauncher, productID, REQUEST_CODE, mPurchaseFinishedListener, "");
    }
    
    public void checkConsumables() {
        System.out.println("checking consumables");

        // send get purchases request
        //
        Bundle ownedItems;
        try {
            if (iabHelper == null || iabHelper.getService() == null) return;
            ownedItems = iabHelper.getService().getPurchases(3, getPackageName(), "inapp", null);
            if (ownedItems == null) return;

            int responseCode = ownedItems.getInt("RESPONSE_CODE");
            if (responseCode != 0) {
                return;
//                throw new java.lang.AssertionError("error fetching owned items");
            }
         //   Toast.makeText(androidLauncher.getContext(), "getPurchases() - success return Bundle", Toast.LENGTH_SHORT).show();
            Log.i("kk", "getPurchases() - success return Bundle");
        } catch (RemoteException e) {
            e.printStackTrace();

        //    Toast.makeText(androidLauncher.getContext(), "getPurchases - fail!", Toast.LENGTH_SHORT).show();
            Log.w("kk", "getPurchases() - fail!");
            return;
        }
//        ArrayList<String> ownedSkus = ownedItems.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
        ArrayList<String> purchaseDataList =
                ownedItems.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
        if (purchaseDataList == null) return;

        int response;
        for (String data : purchaseDataList) {
            JSONObject o = null;
            try {
                o = new JSONObject(data);
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }
            String purchaseToken = o.optString("token", o.optString("purchaseToken"));
            if (purchaseToken == null) return;
            try {
                response = iabHelper.getService().consumePurchase(3, getPackageName(), purchaseToken);
                if (response == 0) {
                    handlePurchaseSuccess(o.optString("productId"));
                    Log.i("kk", "consumePurchase() - success : " + purchaseToken + " return " + String.valueOf(response));
                }
                else
                    Log.w("kk", "consumePurchase() - response not 0! " + purchaseToken);

//                Toast.makeText(context, "consumePurchase() - success : return " + String.valueOf(response), Toast.LENGTH_SHORT).show();
            } catch (RemoteException e) {
                e.printStackTrace();

//                Toast.makeText(context, "consumePurchase() - fail!", Toast.LENGTH_SHORT).show();
                Log.w("kk", "consumePurchase() - fail! " + purchaseToken);
                return;
            }
//            consumePurchase(OnlinePurchaseHandler.getPurchaseableForID(sku).pro);
        }
    }

//    private void consumeIfAlreadyOwned(final String productID) {
//        IabHelper.QueryInventoryFinishedListener mGotInventoryListener
//                = new IabHelper.QueryInventoryFinishedListener() {
//            public void onQueryInventoryFinished(IabResult result,
//                                                 Inventory inventory) {
//
//                if (result.isFailure()) {
//                    // handle error here
//                    System.out.println("Inventory could not be queried");
//                }
//                else {
//                    if (inventory.hasPurchase(productID)) {
//                        System.out.println("You already own " + productID + ", consuming now");
//                        consumePurchase(inventory.getPurchase(productID));
//                    }
//                }
//            }
//        };
//    }

    private void consumePurchase(com.kebabking.game.android.util.Purchase purchase) {
        com.kebabking.game.android.util.IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
                new com.kebabking.game.android.util.IabHelper.OnConsumeFinishedListener() {
                    public void onConsumeFinished(com.kebabking.game.android.util.Purchase purchase, com.kebabking.game.android.util.IabResult result) {
                        if (result.isSuccess()) {
                            // provision the in-app purchase to the user
                            // (for example, credit 50 gold coins to player's character)
                            System.out.println("Confirming purchase");
                            handlePurchaseSuccess(purchase.getSku());
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

    private void handlePurchaseSuccess(String productId) {
        OnlinePurchaseHandler.handlePurchaseSuccess(productId);
//        System.out.println("Purchase success " + purchase.getSku() + " ");
//        Product product =  new Product()
//                .setId(purchase.getSku())
//                .setName(purchase.getSku())
//                .setCategory(purchase.getItemType())
//	    .setBrand("Google")
//	    .setVariant("black")
//                .setPrice(OnlinePurchaseManager.getPurchaseableForID(purchase.getSku()).price)
//	    .setCouponCode("APPARELSALE")
//                .setQuantity(1);

//        ProductAction productAction = new ProductAction(ProductAction.ACTION_PURCHASE)
//                .setTransactionId(purchase.getOrderId())
//                .setTransactionAffiliation("Google Play");

//        Manager.analytics.sendPaymentHit(product, productAction);
    }

    protected boolean handleActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("attempting to handle activity result");
        boolean b = iabHelper.handleActivityResult(requestCode, resultCode, data);
        System.out.println("handle activity result: " + b);
        return b;
    }

    @Override
    public void onDestroy() {
        if (iabHelper != null) iabHelper.dispose();
        iabHelper = null;
    }
}
