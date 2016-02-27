package com.kebabking.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.kebabking.game.Purchases.MeatTypes;

public class KitchenScreen extends ActiveScreen {
	static final boolean LAST_CUSTOMER_MODE = true;
	
	static final String LAST_CUSTOMER_TEXT = "Last customer";
	static final float LAST_CUSTOMER_FADE = 2;
	static final float COUNTDOWN_TIME = 10;

	static final int WIDTH  = 12; // approx. 3:4 ratio
	static final int HEIGHT = 16; // 3:4 ratio approximation
	static final int BUFFER = 0; // distance between each item

	//	static final float PAUSE_X = 0;
	//	static final float PAUSE_Y = .90f;
	//	static final float PAUSE_WIDTH = 2.1f; // this times unit width
	//	static final float PAUSE_HEIGHT = 1.5f;

	static float LAST_CUSTOMER_AFTER = 100; // 2 minutes per day
	static float DAY_LENGTH = 100; // 2 minutes per day
	//	static final float DAY_LENGTH = 10;

	//	static float TIME_TO_WAIT = 2f;

	static int UNIT_WIDTH;
	static int UNIT_HEIGHT;

	// probably shouldn't be here but thats ok

	float time;
	boolean wasShutDown;

	//	boolean paused = false;

	float initialMoney; // set to initial money for calculating profits?

	//	float currentMoney;

	//	float moneyEarnedToday;

	float totalRevenue;

	float drinkExpenses;
	float meatExpenses;

	float happyCustomers;

	long roundStartTime;

	int kebabsTrashed = 0;
	
	TrashPile tp;

	boolean stillHolding;

	boolean lastCustomer;
//	boolean lastCustomerDrawn;
	
	// A new Kitchen Screen is created every time the player starts a new day.
	// handles user input and the main render / update loop
	public KitchenScreen(KebabKing master) {
		super(master, false);
		roundStartTime = System.currentTimeMillis();

		this.bg.reset();
		this.grill.reset(this);
		this.grill.tutorialMode = false;

		if (KebabKing.SHORT_DAY) {
			System.out.println("setting day length to 10");
			DAY_LENGTH = 10;
			LAST_CUSTOMER_AFTER = 3;
		}

		if (LAST_CUSTOMER_MODE) 
			this.time = LAST_CUSTOMER_AFTER;
		else 
			this.time = DAY_LENGTH;

		tp = new TrashPile(this);

		//		setInputProcessor();

		// required to have a smooth thing
		this.render(0);
	}

	@Override
	public void render(float delta) {
		// normally just regular render
		if (!grill.hoverTrash || !Grill.DRAW_GRAY_ON_TRASH) {
			super.render(delta);
		}
		else {
			super.renderGrayAlpha(delta, Grill.TRASH_GRAY_ALPHA);
		}

		batch.begin();
		
		if (LAST_CUSTOMER_MODE) {
			if (lastCustomer) {
				DrawUI.drawLastCustomer(batch, time);
			}
		}
		else {
			if (time < COUNTDOWN_TIME)
				DrawUI.countdownTime(batch, time);
		}
		
		
		batch.end();
	}

	@Override
	// actually run a game loop
	public void update(float delta, boolean ff) {		
		super.update(delta, ff);
		// just for testing

		if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
			stillHolding = true;
			grill.holdInput(Gdx.input.getX(), Gdx.input.getY());
		}
		else {
			// this is breaking everything.
			// this is being called too early.
			// hack solution: force a release if one wasn't registered already
			if (stillHolding) {
				grill.release(Gdx.input.getX(), Gdx.input.getY());
				stillHolding = false;
			}
			grill.mousedOver = -1;
			cm.mousedOver = null;
		}

		if (!TutorialEventHandler.shouldPause()) {
			if (ff) {
				this.time -= 8*delta;
			}
			else {
				// countdown
				this.time -= delta;
			}
			if (this.time < 0) {
				if (LAST_CUSTOMER_MODE) {
					if (lastCustomer) {
						if (cm.totalPeopleInLines() == 0) finishDay();
					}
				}
				else {
					finishDay();
				}
			}
		}
	}
	
	public void handleCustomerApproaching() {
		if (LAST_CUSTOMER_MODE) {
			if (this.time < 0 && !lastCustomer) {
				if (KebabKing.SHORT_DAY) {
					finishDay();
					return;
				}
				lastCustomer = true;
				// must reset time so drawing of "last customer" will work
				this.time = 0;
			}
		}
	}
	
	public boolean forceArrive() {
		return !lastCustomer && LAST_CUSTOMER_MODE && time < 0;
	}
	
	public float getDrinkBuyPrice() {
		return master.profile.inventory.drinkQuality.getBuyPrice();
	}
	public float getDrinkSellPrice() {
		return master.profile.inventory.drinkQuality.getSellPrice();
	}

	// converts 
	public static int convertWidth(float width) {
		return (int) (width * UNIT_WIDTH - 2*BUFFER);
	}

	public static int convertHeight(int height) {
		return height * UNIT_HEIGHT - 2*BUFFER;
	}

	public static int convertXWithBuffer(int unit_x) {
		return convertX(unit_x) + BUFFER;
	}

	public static int convertYWithBuffer(int unit_y) {
		return convertY(unit_y) + BUFFER;
	}

	public static int convertX(int unit_x) {
		return unit_x * UNIT_WIDTH;
	}

	public static int convertY(int unit_y) {
		return unit_y * UNIT_HEIGHT;
	}

	public static int convertY(float unit_y) {
		return (int) (unit_y * UNIT_HEIGHT);
	}

	public void serveCustomerAll(Customer customer) {
		float[] revCost = customer.giveMeat(grill.selectedSet);
		earnMoney(revCost[0] - revCost[1]);
		totalRevenue += revCost[0];
		meatExpenses += revCost[1];

		int served = grill.selectedSet.size();
		Grill.kebabsServedThisSession += served;
		grill.removeSelected(); // deletes selected meat from grill;
		grill.select(Grill.Selected.NONE);
	}

	public boolean serveCustomerBeer(Customer customer) {
		//		if (canAfford(getDrinkBuyPrice())) {
		float moneyEarned = cm.mousedOver.giveBeer();
		totalRevenue += moneyEarned;

		// customer doesn't want beer
		if (moneyEarned == 0) {
			return false;
		}

		spendMoney(getDrinkBuyPrice());

		earnMoney(moneyEarned);

		return true;
		//		}
	}

	public Meat dropMeatOnGrill(MeatTypes.Type type, int index) {
		//		if (!canAfford(Meat.getBuyPrice(type))
		//				|| !grill.open(grill.mousedOver)) return null;
		System.out.println("Dropping meat on grill");
		Meat meat = grill.dropMeat(type, index);
		//		if (meat != null) 
		//			spendMoney(Meat.getBuyPrice(type));
		return meat;
	}

	public boolean canAfford(float price) {
		return  master.profile.getCash() >= price;
	}

	public void earnMoney(float money) {
		this.master.profile.giveMoney(money);
	}

	public void spendMoney(float money) {
		master.profile.spendCash(money);
	}

	public static int getUnitX(int x) {
		return x / UNIT_WIDTH;
	}

	public static int getUnitY(int y) {
		return ((KebabKing.getHeight() - y) / UNIT_HEIGHT);
	}

	// As soon as the policeman walks onto the screen, save the fact that the player was shut down and save.
	// this prevents people from quitting the app before it saves.
	// SAVAGE but necessary to prevent bitch boys from cheating
	public void preShutDown() {
		this.wasShutDown = true;
		StatsHandler.policeShutdown();
		master.profile.shutdownAt = System.currentTimeMillis();
		master.save();
	}

	// this is called when the police actually arrives at your stand
	public void shutdown() {
		//		this.wasShutDown = true;
		//		// also set shutdown start time
		//		master.profile.shutdownAt = System.currentTimeMillis();

		finishDay();
	}

	public void finishDay() {	
		// switch to summary screen
		//		master.platformSpec.sendUserTiming("Round", System.currentTimeMillis() - roundStartTime);
		master.endDay();
	}

	/** returns reputation for day, between 0.5 and 5.0 */
	public float calculateReputation() {		
		// simply average the reputations and round)
		// out of 10
		int reputation = (int) (2.0 * cm.totalSatisfaction / cm.totalCustomers + 0.5);		

		//		if (cm.totalCustomers == 0) return master.profile.currentReputation;
		if (cm.totalCustomers == 0) return 0.5f;

		// out of 5
		float rep = reputation / 2.0f;

		// boost up if too low
		if (rep == 0) rep = 0.5f;

		return rep;
	}

	// this overrides the "onPause" method
	@Override
	public void pause() {
		// this check prevents problems during tutorial
		if (!DrawUI.notificationActive) {
			grill.deselectAll();
			master.kitchenPause();
		}
	}
	
	public boolean isPaused() {
		return master.getScreen() == master.pause;
	}

	//	// this is really untrustworthy if we have a UIStage on top.
	//	public void setInputProcessor() {
	//		DrawUI.setInput(new InputAdapter () {
	//			public boolean touchDown (int x, int y, int pointer, int button) {
	////				super.touchDown(x, y, pointer, button);
	//				grill.touchInput(x, y); // handle all types of clicks the same.
	//				return true; // return true to indicate the event was handled
	//			}
	//			public boolean touchUp (int x, int y, int pointer, int button) {
	////				super.touchUp(x, y, pointer, button);
	/////				grill.release(x, y);
	//				// don't trust this for now. finicky
	//				return true; // return true to indicate the event was handled
	//			};
	//		});
	//	}

	//	@Override
	//	public void show() {
	//		super.show();
	////		this.setInputProcessor();
	//	}

}
