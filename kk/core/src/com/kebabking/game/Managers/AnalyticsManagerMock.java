package com.kebabking.game.Managers;

import com.kebabking.game.KebabKing;

public class AnalyticsManagerMock implements AnalyticsManager {

	@Override
	public void promptForRating() {
		KebabKing.print("prompt for rating mock");
	}
		@Override
	public void sendScreenHit(String screenName) {
		KebabKing.print("Send screen hit mock: " + screenName);
	}

	@Override
	public void sendEventHit(String category, String action) {
		KebabKing.print("Send event hit mock: " + category + "/" + action);

	}

	@Override
	public void sendEventHit(String category, String action, String label) {
		KebabKing.print("Send event hit mock: " + category + "/" + action + "/" + label);

	}

	@Override
	public void sendEventHit(String category, String action, String label, long value) {
		KebabKing.print("Send event hit mock: " + category + "/" + action + "/" + label + "/" + value);
	
	}

	@Override
	public void sendUserTiming(String eventName, long milliseconds) {
		KebabKing.print("Send user hit mock: " + eventName + "/" + milliseconds + "ms");
	
	}
}
