package com.kebabking.game;

import com.kebabking.game.Purchases.DrinkQuality;
import com.kebabking.game.Purchases.MeatTypes;

public class TutorialEventHandler {
	public static int FIRST_ORDER_MEAT_COUNT = 2;

	static KebabKing master;
	
	static boolean firstMeatPlaced;
	static boolean secondMeatPlaced;
	static boolean thirdMeatPlaced;
	static boolean fourthMeatPlaced;
	static boolean fifthMeatPlaced;
	
	static boolean firstMeatCooked;
	static boolean secondMeatCooked;
	static boolean thirdMeatCooked;
	static boolean fourthMeatCooked;
	static boolean fifthMeatCooked;

	static boolean firstMeatServed;
	static boolean secondMeatServed;	
	
	static boolean thirdMeatServed;
	static boolean fourthMeatServed;
	static boolean fifthMeatServed;
	
	static boolean firstCustomerApproached;
	static boolean secondCustomerApproached;
	
	static boolean customerHasOrderedBeer;
	static boolean secondCustomerBeerServed;
	static boolean firstCustomerHasOrdered;
	static boolean secondCustomerHasOrdered;
	
	static boolean customerHasOrderedSpice;
	static boolean spiceMeatPlaced;
	static boolean meatSpiced;
	
	static boolean meatBurnt;
//	static boolean needsToBeThrownAway;
	
//	static int meatSpiced;
	
	// order:
	// 		first order is just beef
	//			place on grill
	//			wait
	//			serve
	//		second customer (beef w/ beer)
	//			drag beer
	//		let play proceed
	//
	//		second day, first customer, force spice, wait until meat placed on grill
	//			drag spice
	
	public static void init(KebabKing masterIn) {
		master = masterIn;
	}
	
	public static void initializeAfterLoad() {
		// TODO remove, just for testing
//		master.profile.stats.daysWorked = 1;		
		
//		// don't have tutorials if loading an old save:
		if (master.profile.stats.daysWorked >= 1 || master.profile.getLevel() > 1) {
			master.profile.stats.firstCustomerServed = true;
			master.profile.stats.secondCustomerServed = true;
		}
		if (master.profile.stats.daysWorked >= 2 || master.profile.getLevel() > 1) {
			master.profile.stats.spiceCustomerServed = true;
		}
//		System.out.println("HERE");
	}
	
	private static ProfileStats getStats() {
		return master.profile.stats;
	}
	
	public static void handleCustomerApproach() {
		if (getStats().secondCustomerServed) return;
		
		if (!firstCustomerApproached) {
			firstCustomerApproached = true;
			
		}
		else if (!secondCustomerApproached) {
			secondCustomerApproached = true;
		}
	}
	
	public static void handleCustomerOrders() {
		if (getStats().secondCustomerServed || secondCustomerHasOrdered) return;
		
		if (!firstCustomerHasOrdered) {
			firstCustomerHasOrdered = true;
			System.out.println("First customer ordered");
			DrawUI.launchTutorialNotification(Assets.strings.get("tut_1_title"), "", Assets.strings.get("tut_1"), MeatTypes.Type.values[0].bigIcon);
		}
		else if (!secondCustomerHasOrdered) {
			secondCustomerHasOrdered = true;
			System.out.println("Second customer ordered");
			DrawUI.launchTutorialNotification(Assets.strings.get("tut_2_title"), "", Assets.strings.get("tut_2"), DrinkQuality.Quality.values[0].coolerRegion);
		}
	}
	
	public static void handleMeatPlaced() {
		System.out.println("handle meat placed");
		if (firstMeatPlaced || getStats().firstCustomerServed) {
			if (secondMeatPlaced  || getStats().firstCustomerServed) {
				if (thirdMeatPlaced || getStats().secondCustomerServed) {
					if (fourthMeatPlaced || getStats().secondCustomerServed) {
						if (fifthMeatPlaced || getStats().secondCustomerServed) {
							System.out.println("fifth meat already placed");
							// first two customers have been served
							if (customerHasOrderedSpice && !meatSpiced && !spiceMeatPlaced) {
								spiceMeatPlaced = true;
								System.out.println("Meat needs to be spiced");
								DrawUI.exitTutorialNotification();
								DrawUI.launchTutorialNotification(Assets.strings.get("tut_3_title"), "", Assets.strings.get("tut_3"), Assets.getTextureRegion("grill/brush-48"));
							}
						}
						else {
							System.out.println("fifth meat placed");
							fifthMeatPlaced = true;
						}
					}
					else {
						System.out.println("fourth meat placed");
						fourthMeatPlaced = true;
					}
				}
				else {
					System.out.println("third meat placed");
					thirdMeatPlaced = true;
				}
			}
			else {
				// handle second meat placed
				System.out.println("Second meat placed");
				secondMeatPlaced = true;
				DrawUI.exitTutorialNotification();
				DrawUI.launchTutorialSuccessNotification(Assets.strings.get("tut_1_placed_title"), "", Assets.strings.get("tut_1_placed"), Assets.face5);
			}
		}
		else {
			// handle first meat placed
			System.out.println("First meat placed");
			firstMeatPlaced = true;
		}
	}
	
	public static void handleCooked() {
		if (getStats().tutorialComplete()) return;
		if (firstMeatCooked) {
			if (secondMeatCooked) {
				if (thirdMeatCooked) {
					if (fourthMeatCooked) {
						if (fifthMeatCooked) {
							return;
						}
						else fifthMeatCooked = true;
					}
					else fourthMeatCooked = true;
				}
				else thirdMeatCooked = true;
			}
			else {
				System.out.println("Second meat done");
				secondMeatCooked = true;
				DrawUI.launchTutorialNotification(Assets.strings.get("tut_1_cooked_title"), "", Assets.strings.get("tut_1_cooked"), Assets.getTextureRegion("screens/tutorial_cooked"));
			}
		}
		else {
			System.out.println("First meat done");
			firstMeatCooked = true;
		}
	}
	
	public static void handleMeatServed() {
		if (getStats().firstCustomerServed && getStats().secondCustomerServed) return;
		if (firstMeatServed) {
			if (secondMeatServed) {
				if (thirdMeatServed) {
					if (fourthMeatServed) {
						if (fifthMeatServed) {
							if (meatSpiced && !getStats().spiceCustomerServed) {
								handleSpiceCustomerServed();
							}
							return;
						}
						else {
							fifthMeatServed = true;
							if (secondCustomerBeerServed) {
								handleSecondCustomerDone();
							}
						}
					}
					else {
						fourthMeatServed = true;
					}
				}
				else {
					thirdMeatServed = true;
				}
			}
			else {
				System.out.println("Second meat served");
				secondMeatServed = true;
				handleFirstCustomerDone();
			}
		}
		else {
			System.out.println("First meat served");
			firstMeatServed = true;
		}
	}
	
	// handle beer order
	public static void handleBeerOrder() {
		if (getStats().secondCustomerServed || customerHasOrderedBeer) return;

		System.out.println("First drink ordered");
		customerHasOrderedBeer = true;
	}
	

	public static void handleBeerServed() {
		if (getStats().secondCustomerServed || secondCustomerBeerServed) return;
	
		System.out.println("First drink served");
		secondCustomerBeerServed = true;
		DrawUI.exitTutorialNotification();
		DrawUI.launchTutorialSuccessNotification(Assets.strings.get("tut_2_served_title"), "", Assets.strings.get("tut_2_served"), DrinkQuality.Quality.values[0].getIcon());

		if (fifthMeatServed) {
			handleSecondCustomerDone();
		}
	}
	
	public static void handleSpice() {
		if (getStats().spiceCustomerServed || meatSpiced) return;
		
		DrawUI.exitTutorialNotification();
		DrawUI.launchTutorialSuccessNotification(Assets.strings.get("tut_3_spiced_title"), "", Assets.strings.get("tut_3_spiced"), Assets.face5);
		meatSpiced = true;
	}

	public static void handleFirstCustomerDone() {
		if (getStats().firstCustomerServed) return;
		
		DrawUI.exitTutorialNotification();
		DrawUI.launchTutorialSuccessNotification(Assets.strings.get("tut_1_served_title"), "", Assets.strings.get("tut_1_served"), Assets.face5);

		System.out.println("First customer done");
		getStats().firstCustomerServed = true;	
	}
	
	public static void handleSecondCustomerDone() {
		if (getStats().secondCustomerServed) return;
		
		DrawUI.launchTutorialSuccessNotification("Tutorial Complete!", "", "There are still many hungry customers out there! Go get 'em!", Assets.face5);

		System.out.println("Second customer done");
		getStats().secondCustomerServed = true;	
	}
	
	public static void handleSpiceCustomerServed() {
		if (getStats().spiceCustomerServed) return;
				
		getStats().spiceCustomerServed = true;
	}
	
	// first burn
	public static void handleBurn() {
		if (getStats().burntMeatThrownAway || meatBurnt || !getStats().tutorialComplete()) return;

		DrawUI.launchTutorialNotification(Assets.strings.get("tut_burnt_title"), "", Assets.strings.get("tut_burnt"), Assets.getTextureRegion("screens/tutorial_burnt"));

		System.out.println("First burn");
		meatBurnt = true;
	}
	
	public static void handleTrash() {
		if (getStats().burntMeatThrownAway) return;
		if (!meatBurnt) return;
		
		DrawUI.exitTutorialNotification();
		DrawUI.launchTutorialSuccessNotification(Assets.strings.get("tut_trashed_title"), "", Assets.strings.get("tut_trashed"), Assets.face5);
		
		System.out.println("Threw away burn");
		getStats().burntMeatThrownAway = true;
	}
	
	public static void handleServeRaw() {
		if (getStats().servedRaw || !getStats().tutorialComplete()) return;
		
		DrawUI.launchTutorialSuccessNotification(Assets.strings.get("tut_raw_title"), "", Assets.strings.get("tut_raw"), Assets.faceSick);
		getStats().servedRaw = true;
	}

	public static void handleJewelerOrder() {
		if (getStats().jewelerCustomers > 0) return;
		
		DrawUI.launchTutorialSuccessNotification(Assets.strings.get("tut_jeweler_title"), "", Assets.strings.get("tut_jeweler"), Assets.getTextureRegion("screens/tutorial_jeweler"));

		System.out.println("First jeweler");
	}
	
	// handle first spice
	public static void handleSpiceOrder(MeatTypes.Type type) {
		if (getStats().spiceCustomerServed || customerHasOrderedSpice) return;
		
		System.out.println("First Spice");
		DrawUI.launchTutorialNotification(Assets.strings.get("tut_3_order_title"), "", Assets.strings.get("tut_3_order"), Assets.getTextureRegion("screens/spicy_order"));

		customerHasOrderedSpice = true;
	}

	public static void handleDoubleOrder(MeatTypes.Type type) {
		if (getStats().customerHasOrderedDouble) return;

		System.out.println("First double");
		getStats().customerHasOrderedDouble = true;
	}
	
	public static void handleFirstDayComplete() {
		System.out.println("Handling first day complete");		
		// just in case, set booleans
		handleFirstCustomerDone();
		handleSecondCustomerDone();
	}
	
	public static void handleSecondDayComplete() {
		System.out.println("Handling second day complete");
		master.profile.inventory.forceSecondBoxUpdate();
		
		// just in case, also set booleans
		handleFirstCustomerDone();
		handleSecondCustomerDone();
		handleSpiceCustomerServed();
	}
	
	public static void handleThirdDayComplete() {
		System.out.println("Handling third day complete");
		master.profile.inventory.forceThirdBoxUpdate();
		
		// just in case, also set booleans
		handleFirstCustomerDone();
		handleSecondCustomerDone();
		handleSpiceCustomerServed();

	}
	
	public static void handleSecondDayBegun() {
		System.out.println("Handling second day begun");
	}
	
	public static void handleThirdDayBegun() {
		System.out.println("Handling third day begun");
		
		// launch notification about lamb
		DrawUI.launchTutorialSuccessNotification(Assets.strings.get("tut_lamb_title"), "", Assets.strings.get("tut_lamb"), MeatTypes.Type.values[1].bigIcon);
	}
	
	public static void handleFourthDayBegun() {
		System.out.println("Handling fourth day begun");
		
		// launch notification about lamb
		DrawUI.launchTutorialSuccessNotification(Assets.strings.get("tut_chicken_title"), "", Assets.strings.get("tut_chicken"), Assets.getTextureRegion("screens/tutorial_chicken"));
	}
	
	public static boolean forceFirstOrder() {
		return !getStats().firstCustomerServed;
	}
	
	public static boolean forceSecondOrder() {
		return !getStats().secondCustomerServed && getStats().firstCustomerServed;
	}
	
	public static boolean forceSpiceOrder() {
		if (getStats().spiceCustomerServed) return false;
		return !shouldNotOrderSpice() && !customerHasOrderedSpice;
	}
	
	public static boolean hasSecondCustomerOrdered() {
		return getStats().secondCustomerServed || secondCustomerHasOrdered;
	}	
	
	public static boolean hasFirstCustomerOrdered() {
		return getStats().firstCustomerServed || firstCustomerHasOrdered;
	}
	
	public static boolean shouldNotOrderSpice() {
		return getStats().daysWorked == 0;
	}
	
	public static boolean shouldNotOrderBeer() {
		return !getStats().firstCustomerServed;
	}
	
	// shouldn't allow customer to approach stand while:
		// first customer has started walking up but not been served
		// second customer has started wlaking up but not been served
	public static boolean dontAllowCustomer() {
		if (getStats().secondCustomerServed) return false;
		
		if (firstCustomerApproached && !getStats().firstCustomerServed) return true;
		if (secondCustomerApproached && !getStats().secondCustomerServed) return true;
		return false;
	}
	
	// don't allow jeweler for first or second customers
	public static boolean dontAllowJeweler() {
		return !getStats().tutorialComplete() || getStats().adsCompleted < 2;
	}
	
	// should not allow touching meat boxes if:
	//		first customer has not ordered
	//		all first customer meat has been placed, but customer not served
	// 		
	//		first customer served, second customer has not ordered
	public static boolean shouldDisableBoxes() {
		if (getStats().secondCustomerServed) return false;
		
		if (!firstCustomerHasOrdered) return true;
		
		if (secondMeatPlaced && !getStats().firstCustomerServed) return true;
		
		if (getStats().firstCustomerServed && !secondCustomerHasOrdered) return true;
		
		return false;
	}
	
	public static boolean shouldDisableGrill() {
		if (getStats().tutorialComplete()) return false;
		return !secondMeatCooked || (secondCustomerHasOrdered && !thirdMeatCooked);
	}
	
	public static boolean shouldDisableTrash() {
		return !getStats().tutorialComplete();
	}
	
	// don't allow serving customers when prompted to throw away
	public static boolean shouldDisableServe() {
		return meatBurnt && !getStats().burntMeatThrownAway;
	}
	
	public static boolean shouldDisableRageLeave() {
		return !getStats().secondCustomerServed;
	}
	
	// need to pause when:
	//		first customer has orderd, but second meat not placed
	//		meat is cooked, but customer has not been served
	// 		second customer has ordered, beer has not been served
	// 
	// 		TODO:
	// 		spice ordered, but meat has not been spiced
	//		jeweler approaches, but continue not pressed?
	public static boolean shouldPause() {
		if (showingFirstTut()) return true;
		if (showingSecondTut()) return true;
		if (showingThirdTut()) return true;
		
		if (showingMustSpice()) return true;
		
		if (showingBurnt()) return true;
		
		if (DrawUI.tutorialNotificationActive) return true;
		
		return false;
	}
	
	public static boolean showingFirstTut() {
		if (getStats().firstCustomerServed) return false;
		return firstCustomerHasOrdered && !secondMeatPlaced;
	}
	
	public static boolean showingSecondTut() {
		if (getStats().firstCustomerServed) return false;
		return secondMeatCooked && !getStats().firstCustomerServed;
	}
	
	public static boolean showingThirdTut() {
		if (getStats().secondCustomerServed) return false;
		return secondCustomerHasOrdered && !secondCustomerBeerServed;
	}
	
	public static boolean showingMustSpice() {
		if (getStats().spiceCustomerServed) return false;
		return customerHasOrderedSpice && !meatSpiced;
	}
	
	public static boolean showingBurnt() {
		if (getStats().burntMeatThrownAway) return false;
		return meatBurnt;
	}
}
