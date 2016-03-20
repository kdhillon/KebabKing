package com.kebabking.game;

public class LanguageManager {

	// use this to draw localized number (ie, hindu numerals ० १ २ ३ ४ etc)
	// do not call this every frame
	public static String localizeNumber(int number) {
		String english = number + "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < english.length(); i++) {
			sb.append(Assets.strings.get(""+english.charAt(i)));
		}
		return sb.toString();
	}
	
	public static String localizeNumber(float number) {
		String english = number + "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < english.length(); i++) {
			sb.append(Assets.strings.get(""+english.charAt(i)));
		}
		return sb.toString();
	}
}
