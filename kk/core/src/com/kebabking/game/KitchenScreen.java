package com.kebabking.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class KitchenScreen extends ScreenTemplate {
	static final float COUNTDOWN_TIME = 10;
	
	static final int WIDTH  = 12; // approx. 3:4 ratio
	static final int HEIGHT = 16; // 3:4 ratio approximation
	static final int BUFFER = 0; // distance between each item
	
//	static final float PAUSE_X = 0;
//	static final float PAUSE_Y = .90f;
//	static final float PAUSE_WIDTH = 2.1f; // this times unit width
//	static final float PAUSE_HEIGHT = 1.5f;

//	static final float DAY_LENGTH = 120; // 2 minutes per day
	static final float DAY_LENGTH = 10; 
	
//	static float TIME_TO_WAIT = 2f;
	
	static int UNIT_WIDTH;
	static int UNIT_HEIGHT;

	// probably shouldn't be here but thats ok
	static final float BEER_SELL_PRICE = 5;
	static final float BEER_BUY_PRICE = 3;

	KebabKing master;

	float time;
	SpriteBatch batch;
	Grill grill;
	CustomerManager cm;
	Background bg;
	boolean wasShutDown;

	boolean paused = false;

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
		roundStartTime = System.currentTimeMillis();
		
		this.master = master;

		this.batch = master.batch;
		this.bg = master.bg;
		this.bg.reset();
		this.cm = master.cm;

		this.time = DAY_LENGTH;

//		this.currentMoney = master.profile.cash;

		grill = master.grill;
		grill.reset(this);
		grill.tutorialMode = false;
		
		tp = new TrashPile(this);

		// clear cm for each day
		
		//		bg.activate();

		setInputProcessor();

		// required to have a smooth thing
		this.render(0);
		//		paused = true;
	}

	@Override
	public void render(float delta) {

		// shouldn't even need this boolean TODO delete
		if (!paused) {
			update(delta);
		}

		batch.begin();
		bg.draw(batch);
		cm.draw(batch);
		grill.draw(batch);
		
		if (time < COUNTDOWN_TIME) {
			DrawUI.countdownTime(batch, time);
		}
		
//		DrawUI.drawMoney(batch, currentMoney);
////		DrawUI.drawStars(batch, profile);
//		DrawUI.drawTime(batch, time);
		DrawUI.drawFullUI(delta, batch, master.profile);
//		drawPause();
		batch.end();
	}

	// actually run a game loop
	public void update(float delta) {
		// just for testing
		boolean fastForward = false;
		if (Gdx.input.isKeyPressed(Keys.F))
			fastForward = true;

		if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
			grill.holdInput(Gdx.input.getX(), Gdx.input.getY());
//			this.checkPause(Gdx.input.getX(), Gdx.input.getY());
		}
		else {
			grill.mousedOver = -1;
//			grill.mousedOverTrash = false;
			cm.mousedOver = null;
		}

		if (fastForward) {
			bg.act(delta);
			cm.act(delta);
			grill.act(delta);
			bg.act(delta);
			cm.act(delta);
			grill.act(delta);
			bg.act(delta);
			cm.act(delta);
			grill.act(delta);
			bg.act(delta);
			cm.act(delta);
			grill.act(delta);
			bg.act(delta);
			cm.act(delta);
			grill.act(delta);
			bg.act(delta);
			cm.act(delta);
			grill.act(delta);
			bg.act(delta);
			cm.act(delta);
			grill.act(delta);
			bg.act(delta);
			cm.act(delta);
			grill.act(delta);
			this.time -= 8*delta;
		}
		else {
			bg.act(delta);
			cm.act(delta);

			grill.act(delta);

			// countdown
			this.time -= delta;
		}
		if (this.time < 0) finishDay();
	}
	
//	public void drawPause() {
//		batch.draw(Assets.uiSkin.getRegion("icon_pause"), PAUSE_X * ChuanrC.width, PAUSE_Y * ChuanrC.height, PAUSE_WIDTH * UNIT_WIDTH, PAUSE_HEIGHT * UNIT_HEIGHT);
//	}
	
//	public void checkPause(float x, float y) {
//		y = ChuanrC.height - y;
//		if (x > PAUSE_X * ChuanrC.width && x < PAUSE_X * ChuanrC.width + PAUSE_WIDTH * UNIT_WIDTH &&
//				y > PAUSE_Y * ChuanrC.height && y < PAUSE_Y * ChuanrC.height + PAUSE_WIDTH * UNIT_HEIGHT) {
//			master.kitchenPause();
//		}
//	}
	
//	/** this method happens every frame that ks is paused */
//	public void pausedUpdate(float delta) {
//		
//	}

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
