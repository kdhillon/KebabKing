package com.kebabking.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputAdapter;

public class KitchenScreen extends ActiveScreen {
	static final float COUNTDOWN_TIME = 10;
	
	static final int WIDTH  = 12; // approx. 3:4 ratio
	static final int HEIGHT = 16; // 3:4 ratio approximation
	static final int BUFFER = 0; // distance between each item
	
//	static final float PAUSE_X = 0;
//	static final float PAUSE_Y = .90f;
//	static final float PAUSE_WIDTH = 2.1f; // this times unit width
//	static final float PAUSE_HEIGHT = 1.5f;

	static final float DAY_LENGTH = 120; // 2 minutes per day
//	static final float DAY_LENGTH = 10; 
	
//	static float TIME_TO_WAIT = 2f;
	
	static int UNIT_WIDTH;
	static int UNIT_HEIGHT;

	// probably shouldn't be here but thats ok
	static final float BEER_SELL_PRICE = 5;
	static final float BEER_BUY_PRICE = 3;

	float time;
	boolean wasShutDown;

//	boolean paused = false;

	float initialMoney; // set to initial money for calculating profits?
	
//	float currentMoney;

	float moneyEarnedToday;
	float moneySpentToday;

	float happyCustomers;

	long roundStartTime;
	
	int kebabsTrashed = 0;
	
	TrashPile tp;
	
	// A new Kitchen Screen is created every time the player starts a new day.
	// handles user input and the main render / update loop
	public KitchenScreen(KebabKing master) {
		super(master);
		roundStartTime = System.currentTimeMillis();
		
		this.bg.reset();
		this.grill.reset(this);
		this.grill.tutorialMode = false;
		
		this.time = DAY_LENGTH;

		tp = new TrashPile(this);

		setInputProcessor();

		// required to have a smooth thing
		this.render(0);
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		
		batch.begin();
		if (time < COUNTDOWN_TIME)
			DrawUI.countdownTime(batch, time);
		batch.end();
	}

	@Override
	// actually run a game loop
	public void update(float delta, boolean ff) {		
		super.update(delta, ff);
		// just for testing

		if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
			grill.holdInput(Gdx.input.getX(), Gdx.input.getY());
		}
		else {
			grill.mousedOver = -1;
			cm.mousedOver = null;
		}

		if (ff) {
			this.time -= 8*delta;
		}
		else {
			// countdown
			this.time -= delta;
		}
		if (this.time < 0) finishDay();
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
	
	public Meat dropMeatOnGrill(Meat.Type type) {
//		if (!canAfford(Meat.getBuyPrice(type))
//				|| !grill.open(grill.mousedOver)) return null;
		System.out.println("Dropping meat on grill");
		Meat meat = grill.dropMeat(type);
//		if (meat != null) 
//			spendMoney(Meat.getBuyPrice(type));
		return meat;
	}

	public boolean canAfford(float price) {
		return  master.profile.cash >= price;
	}

	public void earnMoney(float money) {
		this.moneyEarnedToday += money;
		this.master.profile.cash += money;
		//		System.out.println("money: " + currentMoney);
	}

	public void spendMoney(float money) {
		System.out.println("Spending " + money);
		master.profile.cash -= money;
		moneySpentToday += money;
		if (master.profile.cash < 0) throw new java.lang.AssertionError("Money < 0!");
		//		System.out.println("money: " + currentMoney);
	}

	public static int getUnitX(int x) {
		return x / UNIT_WIDTH;
	}

	public static int getUnitY(int y) {
		return ((KebabKing.getHeight() - y) / UNIT_HEIGHT);
	}

	// when you get shut down by police
	public void shutdown() {
		this.wasShutDown = true;
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

		if (cm.totalCustomers == 0) return master.profile.currentReputation;
		
		// out of 5
		float rep = reputation / 2.0f;
		
		// boost up if too low
		if (rep == 0) rep = 0.5f;

		return rep;
	}
	
	public void setInputProcessor() {
		DrawUI.setInput(new InputAdapter () {
			public boolean touchDown (int x, int y, int pointer, int button) {
//				super.touchDown(x, y, pointer, button);
				grill.touchInput(x, y); // handle all types of clicks the same.
				return true; // return true to indicate the event was handled
			}
			public boolean touchUp (int x, int y, int pointer, int button) {
//				super.touchUp(x, y, pointer, button);
				grill.release(x, y);
				return true; // return true to indicate the event was handled
			};
		});
	}
	
	@Override
	public void show() {
		super.show();
		this.setInputProcessor();
	}
	
}
