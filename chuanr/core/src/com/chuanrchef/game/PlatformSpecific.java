//package com.chuanrchef.game;
//
//import java.io.FileNotFoundException;
//import java.io.InputStream;
//import java.io.OutputStream;
//
//public interface PlatformSpecific {
//	
//    public boolean isLoggedIn();
//    
//    public void logIn();
//    
//    public void logOut();
//    
//    public void inviteFriends();
//    
//    public OutputStream getOutputStream() throws FileNotFoundException;
//    
//    public InputStream getInputStream() throws FileNotFoundException;
//    
//    public boolean saveFileExists();
//    
//    public void deleteProfile();
//    
//	public void makePurchase(String productID);
//	
//	public void sendScreenHit(String screenName);
//	
//	public void sendEventHit(String category, String action);
//	public void sendEventHit(String category, String action, String label);
//	public void sendEventHit(String category, String action, String label, long value);
//	
////	public void sendPaymentHit(Product product, ProductAction productAction);
////
////	public void sendUserTiming(String eventName, long milliseconds);
//}
