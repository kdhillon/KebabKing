package com.kebabking.game.Managers;

/**
 * Created by Kyle on 11/8/2015.
 */
public interface IABManager {
    // returns true on success
    public void makePurchase(String productID);
    public void checkConsumables();
    
    public void onDestroy();
}
