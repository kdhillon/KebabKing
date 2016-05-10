package com.kebabking.game.Managers;

public class AnalyticsManagerMock implements AnalyticsManager {

	@Override
	public void promptForRating() {
		System.out.println("prompt for rating mock");
	}
		@Override
	public void sendScreenHit(String screenName) {
		System.out.println("Send screen hit mock: " + screenName);
	}

	@Override
	public void sendEventHit(String category, String action) {
		System.out.println("Send event hit mock: " + category + "/" + action);

	}

	@Override
	public void sendEventHit(String category, String action, String label) {
		System.out.println("Send event hit mock: " + category + "/" + action + "/" + label);

	}

	@Override
	public void sendEventHit(String category, String action, String label, long value) {
		System.out.println("Send event hit mock: " + category + "/" + action + "/" + label + "/" + value);
	
	}

	@Override
	public void sendUserTiming(String eventName, long milliseconds) {
		System.out.println("Send user hit mock: " + eventName + "/" + milliseconds + "ms");
	
	}
}
