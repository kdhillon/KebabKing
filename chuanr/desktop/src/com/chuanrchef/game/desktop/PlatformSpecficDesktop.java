package com.chuanrchef.game.desktop;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.chuanrchef.game.ChuanrC;
import com.chuanrchef.game.PlatformSpecific;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.analytics.ecommerce.ProductAction;


public class PlatformSpecficDesktop implements PlatformSpecific {
	DesktopLauncher desktopLauncher;
	
	@Override
	public boolean isLoggedIn() {
		// TODO Auto-generated method stub
//		System.out.println("desktop can't be logged into facebook yo");
		return false;
	}

	@Override
	public void logIn() {
		// TODO Auto-generated method stub
		System.out.println("can't log in to facebook from desktop");
	}

	@Override
	public void logOut() {
		// TODO Auto-generated method stub
		System.out.println("can't log out of facebook from desktop");
		
	}

	@Override
	public void inviteFriends() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public OutputStream getOutputStream() throws FileNotFoundException {
		// get a dummy filestream from desktop
		FileHandle file = Gdx.files.local(ChuanrC.SAVE_FILENAME);
		return file.write(false);
	}
	
	public InputStream getInputStream() throws FileNotFoundException {
		FileHandle file = Gdx.files.local(ChuanrC.SAVE_FILENAME);
		return file.read();
	}

	public boolean saveFileExists() {
		return Gdx.files.local(ChuanrC.SAVE_FILENAME).exists();
	}

	@Override
	public void deleteProfile() {
		System.out.println("exists: " + saveFileExists());
		System.out.println("deleting file: " + Gdx.files.local(ChuanrC.SAVE_FILENAME).delete());
	}
	
	@Override
	public void makePurchase(String productID) {
		System.out.println("Making \"purchase\" on desktop");
		this.desktopLauncher.game.confirmPurchase(productID);
	}
	
	@Override
	public void sendScreenHit(String screenName) {
		System.out.println("SEND HIT: screen: " + screenName);
	}
	
	@Override
	public void sendEventHit(String category, String action) {
		System.out.println("SEND HIT: event: " + category + "/" + action);
	}
	
	@Override
	public void sendEventHit(String category, String action, String label) {
		System.out.println("SEND HIT: event: " + category + "/" + action + "/" + label);
	}

	@Override
	public void sendEventHit(String category, String action, String label, long value) {
		System.out.println("SEND HIT: event: " + category + "/" + action + "/" + label + "/" + value);		
	}
	
}
