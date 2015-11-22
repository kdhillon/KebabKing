package com.chuanrchef.game;

import static com.chuanrchef.game.Assets.speech;
import static com.chuanrchef.game.Customer.CustomerType.BUSINESSMAN;
import static com.chuanrchef.game.Customer.CustomerType.FAT_AMERICAN;
import static com.chuanrchef.game.Customer.CustomerType.FOREIGNER;
import static com.chuanrchef.game.Customer.CustomerType.GIRL;
import static com.chuanrchef.game.Customer.CustomerType.MAN;
import static com.chuanrchef.game.Customer.CustomerType.OLD_MAN;
import static com.chuanrchef.game.Customer.CustomerType.OLD_WOMAN;
import static com.chuanrchef.game.Customer.CustomerType.POLICE;
import static com.chuanrchef.game.Customer.CustomerType.STUDENT;
import static com.chuanrchef.game.Customer.CustomerType.WOMAN;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.chuanrchef.game.Assets.CustomerTextures;

public class Customer implements Comparable<Customer> {
	// customers arrive at the stand, place an order, and wait until their patience runs out
	// when they get too impatient, they will leave the grill
	// a customer generates a requested "order" when they start waiting.
	// the grill provides them an "order" at some point
	// once they receive everything they've requested, or their patience runs out, they will leave
	// if their order was what they wanted, cooked properly, your reputation increases, otherwise, it decreases
	// if the meat given to them was undercooked, your reputation goes down and your sickness level goes up
	// if they leave before their order is complete, your reputation decreases.

	static final float BASE_WAIT_TIME = 90; // customers will stick around for this time, 
	static final float BASE_WALK_SPEED_X = .2f; // avg customer moves this much of the screen every second
	static final float BASE_WALK_SPEED_Y = .15f;
	static final float SQUISH_FACTOR = .4f; // for the line
	
	static final float BORDER_RIGHT = 0.1f; // this times the customer width is border.
//	static final float SCALE_FACTOR = 2f;
	
//	static final float TEXTURE_WIDTH = 3f; 	// double the old values before Vic's textures
//	static final float TEXTURE_HEIGHT = 4.5f; 

	static final float TEXTURE_WIDTH = 4f; 	// double the old values before Vic's textures
	static final float TEXTURE_HEIGHT = 4f; 
	
//	static final float TEXTURE_WIDTH_BACK = 2.2f; 	
//	static final float TEXTURE_HEIGHT_BACK = 3.8f;

	static final float TEXTURE_WIDTH_BACK = 3.5f; 	
	static final float TEXTURE_HEIGHT_BACK = 3.5f;
	
	static final float ORDER_ROW_WIDTH = 1.25f;
	static final float ORDER_ROW_HEIGHT = .675f;
	static final float ICON_WIDTH  = 0.75f; // times unit height
	static final float ICON_HEIGHT = 0.63f;
	static final float ICON_OFFSET_X = 0.45f;
	static final float ICON_OFFSET_Y = 0.02f;
	static final float FONT_OFFSET_Y = 0.54f;
	
	static final float SPEECH_X_OFFSET = .8f;

	static final CustomerType[] genOrder = 	{MAN, WOMAN, OLD_MAN, OLD_WOMAN, STUDENT, BUSINESSMAN, FOREIGNER, POLICE, GIRL, FAT_AMERICAN};

	static final float highlightWidth = (int) (TEXTURE_WIDTH * KitchenScreen.UNIT_WIDTH);
	static final float highlightHeight = (int) (TEXTURE_HEIGHT * KitchenScreen.UNIT_HEIGHT);
//	private static final String TutorialScreen = null;

	CustomerType type;
	CustomerAction action;
	Orient orient; // orientation...?

	boolean tutorialFirst; // only true if special customer, first
	boolean tutorialSecond; // only true if special customer, second

	Order order; 
	float waitTime; 		// amount of time this customer will wait, counts down
	float originalWaitTime;	// original wait time, fixed
	float position_x; 		// position along path between 0 and 1
	float position_y; 		// position along path between 0 and 1
	float path_y; 			// path y for this specific character (randomized)
	int satisfaction; 	// maybe need this
	boolean sick;			// if true, person is sick
	int lineChoice;			// choose a line to go in, weighted towards lines with less people
	int lineIndex; 			// which index should this guy be at
	float targetY;			// where is this guy trying to go vertically?

	float width; // to be used for drawing speech bubble when leaving
	float height; 
	
	float initTimeOffset; // to randomize walking start frame

	boolean policeShutdown; // if this is a policeman who is going to shut down stand

	// reference to the parent cm.
	transient CustomerManager cm;

	enum CustomerAction {
		PASS_START, ARRIVE, WAIT, LEAVE, PASS_END
	}

	enum Orient {
		UP, DOWN, LEFT, RIGHT
	}

	public enum CustomerType {
		OLD_MAN(	1, 1, 3, .1f, .5f, "OldMan"),
		OLD_WOMAN(	1.2f, 1, 3, .05f, .5f, "OldWoman"),
		STUDENT(	.8f, 5, 10, .9f, .8f, "OldMan"),
		MAN(		1, 3, 7, .5f, .7f, "OldMan"),
		WOMAN(		1, 3, 7, .5f, .7f, "OldWoman"),
		BUSINESSMAN(.8f, 4, 10, .8f, .75f, "OldMan"),
		POLICE(		.8f, 4, 10, .8f, .75f, "Policeman"),
		FOREIGNER(	.8f, 4, 10, .8f, .75f, "OldMan"),
		GIRL(		1f, 1, 3, 0f, .75f, "OldMan"),
		FAT_AMERICAN(0.8f, 4, 10, 0.5f, .5f, "FatAmerican"),
		;

		Animation walkUp;
		Animation walkDown;
		Animation walkLeft;
		Animation walkRight;
		Animation idle;

		float walkSpeed; // just for looks
		float patienceFactor;
		int minOrder;
		int maxOrder;
		float beerFactor;
		/**
		 * 
		 * @param patience: relative amount of time they're willing to wait, a factor
		 * @param minOrder: minimum number of Chuanr they will order
		 * @param maxOrder: maximum number of Chuanr they will order
		 * @param beerFactor: likelihood of buying beer
		 */
		//		private CustomerType(Animation animation, float patienceFactor, int minOrder, int maxOrder, float beerFactor) {
		//			this.idle = animation;
		//			this.walkSpeed = (float) (Math.random());
		//			this.patienceFactor = patienceFactor;
		//			this.minOrder = minOrder;
		//			this.maxOrder = maxOrder;
		//			this.beerFactor = beerFactor;	
		//		}

		private CustomerType(float patienceFactor, int minOrder, int maxOrder, float beerFactor, float speed, String prefix) {
			CustomerTextures ct = Assets.generateCustomerTextures(prefix, speed);
			this.idle = ct.idle;
			this.walkUp = ct.up;
			this.walkDown = ct.down;
			this.walkLeft = ct.right;
			this.walkRight = ct.right;
			this.walkSpeed = speed;
			this.patienceFactor = patienceFactor;
			this.minOrder = minOrder;
			this.maxOrder = maxOrder;
			this.beerFactor = beerFactor;	
		}
	}


	public Customer(float currentWaitFactor, CustomerManager cm) {
		this.action = CustomerAction.PASS_START;
		this.cm = cm;

		this.position_x = -.3f;
		this.orient = Orient.RIGHT;

		// randomize line_y
		this.position_y = (CustomerManager.PATH_Y + (float) Math.random() - 0.5f) * KitchenScreen.UNIT_HEIGHT;
		//		this.position_y = CustomerManager.PATH_Y * KitchenScreen.UNIT_HEIGHT;

		this.path_y = this.position_y;

		// first decide customer type, then generate customer
		this.type = generateCustomerType();

		// calculate wait time
		this.waitTime = calcWaitTime(currentWaitFactor);
		this.originalWaitTime = waitTime;

		// decide which line to go in
		this.lineChoice = chooseLine();
		//		System.out.println("line choice: " + this.lineChoice);
		this.initTimeOffset = (float) (Math.random() * 10);
	}

	public CustomerType generateCustomerType() {
		// testing for now
//		if (Math.random() < 0.5) 
//			return CustomerType.OLD_MAN;
//		else if (true) {
//			return CustomerType.OLD_WOMAN;
//		}
		
		// do this based off profile
		float[] spread = cm.profile.currentCustomerSpread;

		// first calculate total from Profile
		float sum = 0;
		for (float f : spread) sum += f;

		// calculate probabilities (normalize)
		float[] probs = new float[spread.length];
		probs[0] = spread[0] / sum;
		for (int i = 1; i < probs.length; i++) {
			probs[i] = (spread[i] / sum) + probs[i - 1];
		}

		double random = Math.random();		
		for (int i = 0; i < probs.length; i++) {
			if (random < probs[i])
				return genOrder[i];
		}
		return genOrder[genOrder.length-1];
		//		throw new java.lang.AssertionError(0);
	}

	public void act(float delta) {
		if (!cm.active) {
			this.waitTime = -1; // make everyone start leaving if they're not already
			if (this.action == CustomerAction.ARRIVE) {
				this.action = CustomerAction.WAIT;
			}
		}

		// if on path
		if (this.action == CustomerAction.PASS_START || this.action == CustomerAction.PASS_END) {
			this.position_x += BASE_WALK_SPEED_X * this.type.walkSpeed * delta;
			if (this.position_x * KitchenScreen.WIDTH >= CustomerManager.LINE_POSITIONS[lineChoice] && this.action == CustomerAction.PASS_START) makeDecision();
		}
		// if not on path, walk to stand. but for now, don't worry about this

		// move down
		if (this.action == CustomerAction.ARRIVE) {			
			this.position_y -= ChuanrC.getGlobalY(BASE_WALK_SPEED_Y * this.type.walkSpeed * delta);
			if (this.position_y <= targetY) this.placeOrder();
			CustomerManager.SHOULD_ORDER = true;
		}

		if (this.action == CustomerAction.WAIT) {
			this.waitTime -= delta;
			if (this.waitTime < 0) startLeaving();
			else if (this.position_y > targetY){
				this.position_y -= ChuanrC.getGlobalY(BASE_WALK_SPEED_Y * this.type.walkSpeed * delta);
			}
		}
		else this.order = null;

		// move up
		if (this.action == CustomerAction.LEAVE) {			
			this.position_y += ChuanrC.getGlobalY(BASE_WALK_SPEED_Y * this.type.walkSpeed * delta);
			//			System.out.println("leaving");
			//			System.out.println(position_y + " " + targetY);
			if (this.position_y >= targetY) this.finishLeaving();
			CustomerManager.SHOULD_ORDER = true; // only time we need to reorder
		}

		if (this.action == CustomerAction.PASS_END) {
			if (this.shouldRemove()) cm.removeCustomer(this);
		}
	}

	public void draw(SpriteBatch batch) {
		// two possible animations, depending on the state and direction
		TextureRegion toDraw;
		float time = cm.timeElapsed + initTimeOffset;
		// default
		if (type.walkDown == null) toDraw = type.idle.getKeyFrame(0);
		else {
			switch (orient) {
			case UP:
				toDraw = type.walkUp.getKeyFrame(time);
				break;
			case DOWN:
				if (Math.abs(targetY - position_y) < 4) toDraw = type.idle.getKeyFrame(time);
				else toDraw = type.walkDown.getKeyFrame(time);
				break;
			case LEFT:
				toDraw = type.walkLeft.getKeyFrame(time);
				break;
			default:
				toDraw = type.walkRight.getKeyFrame(time);
			}
		}

		// calculate global position
		int x_pos = (int) (ChuanrC.getGlobalX(this.position_x));
		int y_pos = (int) (this.position_y);

		width =  (int) (TEXTURE_WIDTH * KitchenScreen.UNIT_WIDTH);
		height = (int) (TEXTURE_HEIGHT * KitchenScreen.UNIT_HEIGHT);

		int LINE_TOP = (int) (((CustomerManager.MAX_IN_LINE - 1) * Customer.TEXTURE_HEIGHT * Customer.SQUISH_FACTOR + CustomerManager.LINES_START_Y) * KitchenScreen.UNIT_HEIGHT);


		if (this.action == CustomerAction.PASS_START || this.action == CustomerAction.PASS_END) {
			width =  (TEXTURE_WIDTH_BACK * KitchenScreen.UNIT_WIDTH);
			height = (TEXTURE_HEIGHT_BACK * KitchenScreen.UNIT_HEIGHT);
		}
		// otherwise interpolate size linearly
		else if (y_pos > LINE_TOP && y_pos < this.path_y) {
			// height of top of line (assuming max 3 per line)
			//			System.out.println(y_pos);
			double interpolate = (1.0 * this.path_y - y_pos) / (1.0 * this.path_y - LINE_TOP);

			width = (float) ((TEXTURE_WIDTH - TEXTURE_WIDTH_BACK) * interpolate + TEXTURE_WIDTH_BACK) * KitchenScreen.UNIT_WIDTH;
			height = (float) ((TEXTURE_HEIGHT - TEXTURE_HEIGHT_BACK) * interpolate + TEXTURE_HEIGHT_BACK) * KitchenScreen.UNIT_HEIGHT;

			// also move slightly right to account for awkward shifting
//			x_pos -= TEXTURE_WIDTH_BACK * interpolate * BORDER_RIGHT * KitchenScreen.UNIT_WIDTH;
			// TODO fix the character offest here
			// 
			x_pos += (int) (((1 - interpolate) * (TEXTURE_WIDTH_BACK)  * KitchenScreen.UNIT_WIDTH * BORDER_RIGHT));
		}

		// draw proper animation, in proper location
		batch.draw(toDraw, x_pos, y_pos, width, height);

		// draw order
		if (this.order != null) {
			drawOrder(batch, x_pos, y_pos);
		}

		// just for testing
		if (this.action == CustomerAction.LEAVE && this.satisfaction > 0) {
			//			this.satisfaction = 2;
			this.drawHappyness(batch, x_pos, y_pos);
		}
	}

	public void drawHappyness(SpriteBatch batch, int x_pos, int y_pos) {
		int order_x_pos = x_pos - 10;

		//		float speechHeight = 0.6f * ORDER_ROW_HEIGHT;
		//
		//		float speechXOffset = width - 8;
		//		float speechYOffset = 0.98f;

		// draw speech bubble with height 2
		//		batch.draw(speech, order_x_pos + speechXOffset, (int) (y_pos + 0.8 * speechYOffset * KitchenScreen.UNIT_HEIGHT), (int) (speechWidth * 0.9 * KitchenScreen.UNIT_WIDTH), (int) (1.7f * speechHeight * KitchenScreen.UNIT_HEIGHT));

		int y_position = (int) (y_pos + height * 0.35);

		TextureRegion icon;
		if (this.satisfaction == 1) {
			icon = Assets.face1;
		} else if (this.satisfaction == 2) {
			icon = Assets.face2;
		} else if (this.satisfaction == 3) {
			icon = Assets.face3;
		} else if (this.satisfaction == 4) {
			icon = Assets.face4;
		} else if (this.satisfaction == 5) {
			icon = Assets.face5;
		} 
		else {
			throw new java.lang.AssertionError();
		}
		if (this.sick) icon = Assets.faceSick;

		batch.draw(icon, order_x_pos + width, y_position, (int) (width * 0.5), (int) (width * 0.5));
	}

	// TODO THIS IS SHITTY CODE FIX THIS 
	public void drawOrder(SpriteBatch batch, int x_pos, int y_pos) {
		int order_x_pos = x_pos - 10;

		//		Color temp = batch.getColor();
		//		batch.setColor(Color.WHITE); 

		float speechWidth = 1.25f * ORDER_ROW_WIDTH;
		float speechHeight = 1.05f * ORDER_ROW_HEIGHT;

		float speechXOffset = Customer.TEXTURE_WIDTH * SPEECH_X_OFFSET;
		float speechYOffset = 0.98f;

		if (order.getTotalTypes() == 1) {
			batch.draw(speech, order_x_pos + speechXOffset*KitchenScreen.UNIT_WIDTH, (int) (y_pos + speechYOffset * KitchenScreen.UNIT_HEIGHT), (int) (speechWidth * KitchenScreen.UNIT_WIDTH), (int) (speechHeight * KitchenScreen.UNIT_HEIGHT));
		}
		else if (order.getTotalTypes() == 2) {
			batch.draw(speech, order_x_pos + speechXOffset*KitchenScreen.UNIT_WIDTH, (int) (y_pos + 0.75 * speechYOffset * KitchenScreen.UNIT_HEIGHT), (int) (speechWidth * KitchenScreen.UNIT_WIDTH), (int) (2 * speechHeight * KitchenScreen.UNIT_HEIGHT));

		}
		else if (order.getTotalTypes() == 3) {
			batch.draw(speech, order_x_pos + speechXOffset*KitchenScreen.UNIT_WIDTH, (int) (y_pos + 0.5 * speechYOffset * KitchenScreen.UNIT_HEIGHT), (int) (speechWidth * KitchenScreen.UNIT_WIDTH), (int) (3 * speechHeight * KitchenScreen.UNIT_HEIGHT));
		}
		//		batch.setColor(temp);

		boolean chickenDone = false;
		boolean beefDone = false;
		boolean lambDone = false;
		for (int i = 0; i < order.getTotalTypes(); i++) {
			TextureRegion icon;
			int count;
			boolean spicy = false;
			// draw the item
			if (order.chicken > 0 && !chickenDone) {
				if (order.chickenSpicy) {
					spicy = true;
					icon = Assets.chickenSpicyIcon;
				}
				else icon = Assets.chickenIcon;
				count = order.chicken;
				chickenDone = true;
			}
			else if (order.beef > 0 && !beefDone) {
				if (order.beefSpicy) {
					icon = Assets.beefSpicyIcon;
					spicy = true;
				}
				else icon = Assets.beefIcon;
				count = order.beef;
				beefDone = true;
			}
			else if (order.lamb > 0 && !lambDone) {
				if (order.lambSpicy) {
					icon = Assets.lambSpicyIcon;
					spicy = true;
				}
				else icon = Assets.lambIcon;
				count = order.lamb;
				lambDone = true;
			}
			else {
				icon = Assets.beerIcon;
				count = order.beer;
			}
			int y_position;
			if (order.getTotalTypes() == 1) 
				y_position = (int) (y_pos + KitchenScreen.UNIT_HEIGHT + ICON_OFFSET_Y * KitchenScreen.UNIT_HEIGHT);
			else if (order.getTotalTypes() == 2) {
				if (i == 0) 
					y_position = (int) (y_pos + KitchenScreen.UNIT_HEIGHT * 1.5 + ICON_OFFSET_Y * KitchenScreen.UNIT_HEIGHT);
				else 
					y_position = (int) (y_pos + KitchenScreen.UNIT_HEIGHT* 0.75 + ICON_OFFSET_Y  * KitchenScreen.UNIT_HEIGHT);
			}
			else {
				if (i == 0) 
					y_position = (int) (y_pos + KitchenScreen.UNIT_HEIGHT * 0.5 + ICON_OFFSET_Y * KitchenScreen.UNIT_HEIGHT);
				else if (i == 1)
					y_position = (int) (y_pos + KitchenScreen.UNIT_HEIGHT * 1.2 + ICON_OFFSET_Y * KitchenScreen.UNIT_HEIGHT);
				else 
					y_position = (int) (y_pos + KitchenScreen.UNIT_HEIGHT * 1.9 + ICON_OFFSET_Y * KitchenScreen.UNIT_HEIGHT);
			}
			batch.draw(icon, order_x_pos + (Customer.TEXTURE_WIDTH + ICON_OFFSET_X)*KitchenScreen.UNIT_WIDTH, y_position, (int) (ICON_WIDTH * KitchenScreen.UNIT_WIDTH), (int) (ICON_HEIGHT * KitchenScreen.UNIT_HEIGHT));

			if (spicy) Assets.fontOrder.setColor(Color.RED);
			Assets.fontOrder.draw(batch, ""+count, (int) (order_x_pos + 1.02* KitchenScreen.UNIT_WIDTH * TEXTURE_WIDTH), (int) (y_position + FONT_OFFSET_Y * KitchenScreen.UNIT_HEIGHT));
			Assets.fontOrder.setColor(Color.BLACK);
		}
	}

	public void highlight(SpriteBatch batch) {
		// calculate global position
		int x_pos = ChuanrC.getGlobalX(this.position_x);
		int y_pos = (int) (this.position_y);

		Color orig = batch.getColor();
		Color myColor = Color.WHITE;
		myColor.a = .3f;
		batch.setColor(myColor);
		batch.draw(Assets.white, x_pos, y_pos, highlightWidth, highlightHeight);
		myColor.a = 1f; // necessary for some reason
		batch.setColor(orig);
	}

	// move to Customer Manager
	public void makeDecision() {
		if (!cm.active) {
			action = CustomerAction.PASS_END;
			return;
		}
		
		//tutorial, or force
		if (this.tutorialFirst || this.tutorialSecond  || cm.totalPeopleInLines() == 0) {
			startArriving();
			return;
		}

		// for now, angry police always shut you down
		float policeShutdownProb = 1;
		if (this.policeShutdown && Math.random() < policeShutdownProb) {
			
			if (cm.totalPeopleInLines() == CustomerManager.MAX_IN_LINE) {
				cm.master.shutdownStand();
			}
			else {
				startArriving();
			}
			return;
		}

		// if 5 star reputation, base 75% chance of wanting food
		// if 4 star reputation, base 60% chance of wanting food
		// if 3 star, base 45%
		// if 2 star, base 30%
		// if 1 star, base 15%

		// this should be between 0 and 1
		float probability = cm.profile.currentReputation * 0.20f;
		double random = Math.random() ;
		boolean wantsChuanr = random < probability;

		//		System.out.println("random: " + random + ", probability: " + probability + ", decision: " + wantsChuanr)
		
		// TODO remove isPolice check here
		if (cm.peopleInLine(lineChoice) >= CustomerManager.MAX_IN_LINE ||
				cm.totalPeopleInLines() >= cm.maxAtOnce ||
				!wantsChuanr) {
			action = CustomerAction.PASS_END;
		}
		else {
			startArriving();
		}
	}

	public void startArriving() {
		this.orient = Orient.DOWN;
		cm.addToLine(this, lineChoice);
		action = CustomerAction.ARRIVE;
		updateTargetY();
	}

	// walk away from the stand
	public void startLeaving() {
		
		if (this.tutorialFirst) {
			cm.shouldAddSecondCustomer = true;
			
			TutorialScreen ts = (TutorialScreen) cm.master.kitchen;
			ts.transitionToNext();	
			if (ts.current == TutorialScreen.Step.ClickWoman) ts.transitionToNext();
				
		}
		else if (this.tutorialSecond) {
			((TutorialScreen) cm.master.kitchen).transitionToNext();	
		}
		
		cm.removeFromLine(lineChoice, lineIndex);
		cm.moveUpLine(lineChoice, lineIndex);

		this.orient = Orient.UP;
		this.action = CustomerAction.LEAVE;
		this.targetY = this.path_y;
		this.lineIndex = -1;

		if (!cm.active) {
			this.satisfaction = -1;
			return;
		}

		// calculate order satisfaction (0 to 5)
		// positives: low wait time, no burnt meat
		// negatives: high wait time, burnt meat

		// wait time can be between 0 and 1
		// better than half of the original wait time is a perfect 1
		// 0 wait time is a 0
		// better than 1/3 of the original wait time is a perfect 1

		float difficulty = 8.5f/10;

		float waitTimeFactor = Math.min(1, this.waitTime / (this.originalWaitTime * difficulty));
		//		System.out.println("wait time factor: " + waitTimeFactor);

		// accuracy factor can be between 1 and 0
		// perfect order (everything satisfied, no burnt) is a perfect 1

		// factor = (quantity given / total quantity) * (total well cooked / total quantity)
		float accuracyFactor = ((order.total - order.remaining) / (float) order.total) * ((order.total - (order.burnt + order.raw)) / (float) order.total);
		//		System.out.println("accuracy factor: " + accuracyFactor);

		// satisfaction is between 1 and 5 (don't round for now)
		satisfaction = (int) (((waitTimeFactor + accuracyFactor) * 4 / 2)) + 1;
		//		System.out.println("total satisfaction: " + satisfaction);

		// calculate if the person gets sick or not
		double sickChance = .8;

		this.sick = false;
		if (Math.random() * order.raw  > 1 - sickChance) this.sick = true;

		// save this stuff to the total
		if (this.waitTime > 0) cm.happyCustomers++;
		cm.totalCustomers++;
		cm.totalSatisfaction += satisfaction;

		if (this.sick) {
			// TODO change with difficulty 
			float generatePoliceNextProb = 1;
			if (Math.random() < generatePoliceNextProb) {
				cm.generatePoliceNext();
				cm.totalSick++;
			}
		}
	}

	public void finishLeaving() {
		this.orient = Orient.RIGHT;
		this.action = CustomerAction.PASS_END;
	}

	// is this person done with their path?
	public boolean shouldRemove() {
		return (this.position_x > 1.0);
	}


	// given this profile's wait factor, calculate wait time
	public float calcWaitTime(float customerPatienceFactor) {
		// TODO add randomness

		// difficulty increases as days go on.
		// after 10 days, customers wait 5% less
		// after 30 days, customers wait 10% less
		// after 70 days, customers wait 15% less
		// after 150 days, customers wait 20% less

		// can increase this to make the game harder, or decrease it to make it easier.
		// scales logarithmically
		float DIFFICULTY = 0.05f;

		float difficultyFactor = (float) (Math.log((cm.profile.daysWorked + 10.0) / 10.0) / Math.log(2.0));
		difficultyFactor *= DIFFICULTY;
		difficultyFactor = 1 -difficultyFactor;

		float maxTime = customerPatienceFactor * BASE_WAIT_TIME * this.type.patienceFactor;

		//		System.out.println("max time: " + maxTime + " difficultyFactor: " + difficultyFactor + ", together: " + maxTime*difficultyFactor);
		return maxTime * difficultyFactor;
	}

	public void updateTargetY() {
		this.targetY = (int) (KitchenScreen.convertY((CustomerManager.LINES_START_Y + lineIndex * (TEXTURE_HEIGHT * SQUISH_FACTOR))));
		//		System.out.println(CustomerManager.LINES_START_Y  + " " + lineIndex * TEXTURE_HEIGHT);
		//		System.out.println("targetY = " + targetY);
		//		System.out.println("y_pos = " + position_y);
	}

	public int chooseLine() {
		// do a weighted decision, variable amount of lines
		int index = 0;
		if (Math.random() < .5) {
			index = (int) (Math.random() * CustomerManager.LINE_POSITIONS.length);
		}
		else {
			// alternatively, choose the line with lowest
			int minCount = Integer.MAX_VALUE;
			for (int i = 0; i < CustomerManager.LINE_COUNT; i++) {
				if (cm.peopleInLine(i) <= minCount) {
					if (cm.peopleInLine(i) == minCount && Math.random() < .5) {
						minCount = cm.peopleInLine(i);
						index = i;
					}
					else {
						minCount = cm.peopleInLine(i);
						index = i;
					}
				}
			}
		}
		return index;
	}

	public void placeOrder() {

		if (this.policeShutdown) {
			this.action = CustomerAction.WAIT;
			// bring up option for paying. Otherwise, shut down
			cm.master.shutdownStand();
			return;
		}

		this.order = new Order(this.type);
		//		order.print();
		this.action = CustomerAction.WAIT;
		
		if (this.tutorialFirst) {
			this.order.beef = 0;
			this.order.chicken = 1;
			this.order.lamb = 3;
			this.order.beer = 0;
			this.order.beefSpicy = false;
			this.order.lambSpicy = true;
			this.order.chickenSpicy = false;
			this.order.remaining = 4;
			
			TutorialScreen ts = (TutorialScreen) this.cm.master.kitchen;
			ts.transitionToNext();
//			ts.firstOrder();
		} 
		else if (this.tutorialSecond) {
			this.order.beef = 0;
			this.order.chicken = 0;
			this.order.lamb = 4;
			this.order.beer = 1;
			this.order.beefSpicy = false;
			this.order.lambSpicy = true;
			this.order.chickenSpicy = false;
			this.order.remaining = 5;
			
			TutorialScreen ts = (TutorialScreen) this.cm.master.kitchen;
			ts.transitionToNext();
//			ts.secondOrder();
		}
	}

	public float giveMeat(ArrayList<Meat> meat) {
		float moneyPaid = this.order.giveMeat(meat);
		//		order.print();
		if (order.remaining == 0) {
			this.startLeaving();
		}
		return moneyPaid;
	}

	
	public float giveBeer() {
		float toReturn = this.order.giveBeer();
		if (order.remaining == 0) {
			this.startLeaving();
		}
		//		order.print();
		return toReturn;
	}

	@Override
	public int compareTo(Customer that) {
		float diff = (that.position_y - this.position_y);
		//		System.out.println(diff);
		if (diff == 0) {
			float diff2 = (this.position_x - that.position_x);
			if (diff2 == 0) return 0;
			else if (diff2 > 0) return -1;
			else return 1;
		}
		else {
			if (diff > 0) return 1;
			else return -1;
		}
	}

	public boolean isPolice() {
		return this.type == CustomerType.POLICE;
	}
	
	public static int getIndexOf(CustomerType type) {
		for (int i = 0; i < genOrder.length; i++) {
			if (genOrder[i] == type) return i;
		}
		return -1;
	}

	//	@Override
	//	public boolean equals(Object o) {
	//		return false;
	//	}
}
