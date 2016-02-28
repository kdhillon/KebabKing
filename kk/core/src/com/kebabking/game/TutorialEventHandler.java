package com.kebabking.game;

import com.kebabking.game.Purchases.DrinkQuality;
import com.kebabking.game.Purchases.MeatTypes;

public class TutorialEventHandler {
	public static int FIRST_ORDER_MEAT_COUNT = 2;

	static KebabKing master;
	
	static boolean firstMeatPlaced;
	static boolean secondMeatPlaced;
	
	static boolean firstMeatCooked;
	static boolean secondMeatCooked;
	
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
//		if (master.profile.stats.daysWorked >= 1) {
//			master.profile.stats.firstCustomerServed = true;
//			master.profile.stats.secondCustomerServed = true;
//		}
//		if (master.profile.stats.daysWorked >= 2) {
//			master.profile.stats.spiceCustomerServed = true;
//		}
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
			DrawUI.launchTutorialNotification("Your First Customer!", "", "Drag 2 beef kebabs to the grill to start cooking!", MeatTypes.Type.values[0].coolerClosed);
		}
		else if (!secondCustomerHasOrdered) {
			secondCustomerHasOrdered = true;
			System.out.println("Second customer ordered");
			DrawUI.launchTutorialNotification("Drink Time!", "This customer wants a drink!", "Drag one from the cooler to the customer!", DrinkQuality.Quality.values[0].coolerRegion);
		}
	}
	
	public static void handleMeatPlaced() {
		if (firstMeatPlaced || getStats().firstCustomerServed) {
			if (secondMeatPlaced  || getStats().secondCustomerServed) {
				// both meats have been placed
				if (customerHasOrderedSpice && !meatSpiced && !spiceMeatPlaced) {
					spiceMeatPlaced = true;
					System.out.println("Meat needs to be spiced");
					DrawUI.exitTutorialNotification();
					DrawUI.launchTutorialNotification("The Spice Brush", "This customer ordered a spicy kebab!", "Drag the spice brush over the kebab to spice it!", Assets.getTextureRegion("grill/brush-48"));
				}
							
				return;
			}
			else {
				// handle second meat placed
				System.out.println("Second meat placed");
				secondMeatPlaced = true;
				DrawUI.exitTutorialNotification();
				DrawUI.launchTutorialSuccessNotification("Nice Work!", "", "Now, wait for your kebabs to cook!", Assets.face5);
			}
		}
		else {
			// handle first meat placed
			System.out.println("First meat placed");
			firstMeatPlaced = true;
		}
	}
	
	public static void handleCooked() {
		if (getStats().firstCustomerServed) return;
		if (firstMeatCooked) {
			if (secondMeatCooked) {
				return;
			}
			else {
				System.out.println("Second meat done");
				secondMeatCooked = true;
				DrawUI.launchTutorialNotification("Ready to serve!", "", "Drag the cooked kebabs from the grill to the customer. \n You can select multiple kebabs at once!", Assets.getTextureRegion("screens/tutorial_cooked"));
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
		DrawUI.launchTutorialSuccessNotification("Nice Work!", "", "Tip: You can upgrade the quality of your drinks at the Market!", DrinkQuality.Quality.values[0].getIcon());

		if (fifthMeatServed) {
			handleSecondCustomerDone();
		}
	}
	
	public static void handleSpice() {
		if (getStats().spiceCustomerServed || meatSpiced) return;
		
		DrawUI.exitTutorialNotification();
		DrawUI.launchTutorialSuccessNotification("Nice Work!", "", "You're like a spice artist!", Assets.face5);
		meatSpiced = true;
	}

	public static void handleFirstCustomerDone() {
		if (getStats().firstCustomerServed) return;
		
		DrawUI.exitTutorialNotification();
		DrawUI.launchTutorialSuccessNotification("Congratulations!", "You just made your first sale!", "The faster you serve your customers, the happier they will be. A high reputation attracts more customers!", Assets.face5);

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

		DrawUI.launchTutorialNotification("Burnt Meat", "Oh no! This meat has burned!", "It's ok, we can handle this. Select the meat and hold your finger off the grill to put it in the trash!", Assets.getTextureRegion("screens/tutorial_burnt"));

		System.out.println("First burn");
		meatBurnt = true;
	}
	
	public static void handleTrash() {
		if (getStats().burntMeatThrownAway) return;
		
		DrawUI.exitTutorialNotification();
		DrawUI.launchTutorialSuccessNotification("Great Job!", "", "Throwing away meat wastes money,\nso try not to let your meat burn!", Assets.face5);
		
		System.out.println("Threw away burn");
		getStats().burntMeatThrownAway = true;
	}
	
	public static void handleServeRaw() {
		if (getStats().servedRaw || !getStats().tutorialComplete()) return;
		
		DrawUI.launchTutorialSuccessNotification("Oh no!", "", "Serving raw meat might make your customers sick!\nBe sure to cook your meat fully before serving!", Assets.faceSick);
		getStats().servedRaw = true;
	}

	public static void handleJewelerOrder() {
		if (getStats().jewelerCustomers > 0) return;
		
		DrawUI.launchTutorialSuccessNotification("The Jeweler", "", "Today's your lucky day! \n The Jeweler gives you free Jade if you complete his order!", Assets.getTextureRegion("screens/tutorial_jeweler"));

		System.out.println("First jeweler");
	}
	
	// handle first spice
	public static void handleSpiceOrder(MeatTypes.Type type) {
		if (getStats().spiceCustomerServed || customerHasOrderedSpice) return;
		
		System.out.println("First Spice");
		DrawUI.launchTutorialNotification("The Spice Brush", "This customer ordered a spicy kebab!", "Place a kebab on the grill.", Assets.getTextureRegion("screens/spicy_order"));

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
		DrawUI.launchTutorialSuccessNotification("Lamb Unlocked!", "", "Lamb cooks faster than beef, and is more profitable!", MeatTypes.Type.values[1].coolerOpen);
	}
	
	public static void handleFourthDayBegun() {
		System.out.println("Handling fourth day begun");
		
		// launch notification about lamb
		DrawUI.launchTutorialSuccessNotification("Chicken Unlocked!", "", "Chicken is very profitable! But be careful, it's twice the size of lamb or beef, and it burns quickly!", Assets.getTextureRegion("screens/tutorial_chicken"));
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
		return !getStats().firstCustomerServed || !getStats().secondCustomerServed;
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
		if (getStats().firstCustomerServed) return false;
		return !secondMeatCooked;
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
