package com.kebabking.game;

import static com.kebabking.game.Customer.CustomerType.BUSINESSMAN;
import static com.kebabking.game.Customer.CustomerType.FARMER;
import static com.kebabking.game.Customer.CustomerType.FAT_MAN;
import static com.kebabking.game.Customer.CustomerType.FOREIGNER;
import static com.kebabking.game.Customer.CustomerType.GIRL;
import static com.kebabking.game.Customer.CustomerType.JEWELER;
import static com.kebabking.game.Customer.CustomerType.NORMAL;
import static com.kebabking.game.Customer.CustomerType.OLDIE;
import static com.kebabking.game.Customer.CustomerType.POLICE;
import static com.kebabking.game.Customer.CustomerType.SOLDIER;
import static com.kebabking.game.Customer.CustomerType.STUDENT;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kebabking.game.Assets.CustomerTextures;

public class Customer implements Comparable<Customer> {
	// customers arrive at the stand, place an order, and wait until their patience runs out
	// when they get too impatient, they will leave the grill
	// a customer generates a requested "order" when they start waiting.
	// the grill provides them an "order" at some point
	// once they receive everything they've requested, or their patience runs out, they will leave
	// if their order was what they wanted, cooked properly, your reputation increases, otherwise, it decreases
	// if the meat given to them was undercooked, your reputation goes down and your sickness level goes up
	// if they leave before their order is complete, your reputation decreases.
	static final float ALT_PROB = 0.2f;
	
	static final boolean EYES_SEPARATE = false;
	
	static final float SELFIE_WAIT_LENGTH = 1f;

	static final float BASE_HUNGER_FACTOR = 0.1f;
	static final float BASE_WAIT_TIME = 60; // customers will stick around for this time, 
	static final float BASE_WALK_SPEED_X = .25f; // avg customer moves this much of the screen every second
	static final float BASE_WALK_SPEED_Y = .2f;
	static final float SQUISH_FACTOR = .4f; // for the line
	
	static final float SAT_SIZE = 0.35f;
	
	static final float POLICE_SHUTDOWN_BASE = 0.3f;
	static final int JEWELER_COINS = 5;
	
	static final float BORDER_RIGHT = 0.1f; // this times the customer width is border.
//	static final float SCALE_FACTOR = 2f;
	
	static final float END_RIGHT = 1.0f;
	static final float END_LEFT = -.3f;
	
//	static final float TEXTURE_WIDTH = 3f; 	// double the old values before Vic's textures
//	static final float TEXTURE_HEIGHT = 4.5f; 

	static final float TEXTURE_WIDTH = 4f; 	// double the old values before Vic's textures
	static final float TEXTURE_HEIGHT = 4f; 
	
	static final float SELFIE_OFFSET_X = 0.24f;
	static final float SELFIE_OFFSET_Y = 0.08f;
	static final float SELFIE_STICK_HEIGHT = 0.15f;
	
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

	static final float highlightWidth = (int) (TEXTURE_WIDTH * KitchenScreen.UNIT_WIDTH * 0.8);
	static final float highlightHeight = (int) (TEXTURE_HEIGHT * KitchenScreen.UNIT_HEIGHT * 0.85);
//	private static final String TutorialScreen = null;

	CustomerType type;
	CustomerAction action;
	Orient orient; // orientation...?

	boolean tutorialFirst; // only true if special customer, first
	boolean tutorialSecond; // only true if special customer, second

	boolean createdBeforeActive;
	
	Order order; 
	float waitTime; 		// amount of time this customer will wait, counts down
	int currentPatience;	// counts down from 5 to 1
	
	float originalWaitTime;	// original wait time, fixed
	float position_x_range; 		// position along path between 0 and 1
	float position_y_full; 		// position along path between 0 and height
	float path_y; 			// path y for this specific character (randomized)
	int satisfaction; 	// maybe need this
	boolean sick;			// if true, person is sick
	int lineChoice;			// choose a line to go in, weighted towards lines with less people
	
	int lineIndex; 			// which index should this guy be at
	float targetY;			// where is this guy trying to go vertically?

	boolean walkingLeft;	// walking right is default
	
	float width; // to be used for drawing speech bubble when leaving
	float height; 
	
	float initTimeOffset; // to randomize walking start frame

	boolean policeShutdown; // if this is a policeman who is going to shut down stand

	boolean wantsChuanr;
		
	boolean female;
//	boolean altTexture;
//	boolean specialTexture;
	CustomerTextures animations;
	
	boolean drawArrow;
	
	float selfieTimer;
	float selfieWidth;
	float selfieHeight;
	
	float stompTimer;
	
	// reference to the parent cm.
	transient CustomerManager cm;

	enum CustomerAction {
		PASS_START, ARRIVE, WAIT, LEAVE, PASS_END
	}

	enum Orient {
		UP, DOWN, LEFT, RIGHT
	}

	public enum CustomerType {
					// pat, 	min,max, 	beer, 	speed
		OLDIE(		1, 		1, 	5, 		.2f, 	.55f, Gender.EITHER, "Oldie", Assets.strings.get("old_folks")),
		STUDENT(		.8f, 	2,	8, 		.8f, 	.8f, Gender.MALE, "Student", Assets.strings.get("students")), //
		FAT_MAN(		1, 		3, 	9, 		.6f,	 .5f, Gender.MALE, "FatMan", Assets.strings.get("fat_men")), //
		NORMAL(			1, 		2,	4, 		.2f, 	.7f, Gender.EITHER, "Normal", Assets.strings.get("normal_people")),
		BUSINESSMAN(	.8f, 	2,	7, 		.5f, 	.9f, Gender.MALE, "Businessman", Assets.strings.get("businessmen")),
		POLICE(			.8f, 	2, 	6, 		.4f, 	.75f, Gender.MALE, "Policeman", Assets.strings.get("police")),
		SOLDIER(		.8f, 	2,	7, 		.4f, 	.53f, Gender.MALE, "Soldier", Assets.strings.get("soldiers")),
		GIRL(			1f, 	2, 	6, 		.05f, 	.75f, Gender.FEMALE, "Girl", Assets.strings.get("girls")),
		FOREIGNER(		1f, 	3, 	9, 		.4f, 	.53f, Gender.EITHER, "Foreigner", Assets.strings.get("foreigners")),
		FARMER(			.8f, 	2, 	8, 		.3f, 	.7f, Gender.MALE,"Farmer", Assets.strings.get("farmers")),//
		JEWELER(		1f, 	1, 	8, 		.2f, 	.6f, Gender.MALE,"Jeweler", Assets.strings.get("jewelers")),//
;
		enum Gender {MALE, FEMALE, EITHER};

		CustomerTextures male;
		CustomerTextures female;
		
		CustomerTextures male_alt;
		CustomerTextures female_alt;
		
		CustomerTextures male_spc;
		CustomerTextures female_spc;

		float walkSpeed; // just for looks
		float animationTime;
		float patienceFactor;
		int minOrder;
		int maxOrder;
		float beerFactor;
		public String plural;
		Gender gender;
				
		/**
		 * @param patienceFactor: relative amount of time they're willing to wait, a factor
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

		private CustomerType(float patienceFactor, int minOrder, int maxOrder, float beerFactor, float speed, Gender gender, String prefix, String plural) {
			this.animationTime = 1/speed * Assets.CUSTOMER_ANIMATION_TIME;
			if (gender == Gender.EITHER) {
				this.male = Assets.generateCustomerTextures(prefix + "_m", animationTime);
				this.male_alt = Assets.generateCustomerTextures(prefix + "_m_alt", animationTime);
				this.male_spc = Assets.generateCustomerTextures(prefix + "_m_spc", animationTime);
				this.female = Assets.generateCustomerTextures(prefix + "_f", animationTime);
				this.female_alt = Assets.generateCustomerTextures(prefix + "_f_alt", animationTime);
				this.female_spc = Assets.generateCustomerTextures(prefix + "_f_spc", animationTime);
			}
			else if (gender == Gender.MALE) {
				this.male = Assets.generateCustomerTextures(prefix, animationTime);
				this.male_alt = Assets.generateCustomerTextures(prefix + "_alt", animationTime);
				this.male_spc = Assets.generateCustomerTextures(prefix + "_spc", animationTime);
			}
			else if (gender == Gender.FEMALE) {
				this.female = Assets.generateCustomerTextures(prefix, animationTime);
				this.female_alt = Assets.generateCustomerTextures(prefix + "_alt", animationTime);
				this.female_spc = Assets.generateCustomerTextures(prefix + "_spc", animationTime);
			}
	
			this.walkSpeed = speed;

			this.patienceFactor = patienceFactor;
			this.minOrder = minOrder;
			this.maxOrder = maxOrder;
			this.beerFactor = beerFactor;	
			this.plural = plural;
			
			this.gender = gender;
		}
		
		public boolean hasAlt(boolean female) {
			if (female) return this.female_alt != null;
			else return this.male_alt != null;
		}
		public boolean hasSpc(boolean female) {
			if (female) return this.female_spc != null;
			else return this.male_spc != null;
		}
	}
	static final CustomerType[] genOrder = 	{NORMAL, FAT_MAN, OLDIE, STUDENT, GIRL, FOREIGNER, POLICE, SOLDIER, BUSINESSMAN, FARMER, JEWELER};
	
	public Customer(float currentWaitFactor, CustomerManager cm, boolean active) {
		this.action = CustomerAction.PASS_START;
		this.cm = cm;
		
		this.orient = Orient.RIGHT;
		this.position_x_range = END_LEFT;
		if (Math.random() < 0.5) {
			orient = Orient.LEFT;
			walkingLeft = true;
			this.position_x_range = END_RIGHT;
		}

		// randomize line_y
		this.position_y_full = (CustomerManager.PATH_Y + (float) Math.random() - 0.5f) * KitchenScreen.UNIT_HEIGHT;
//				this.position_y = CustomerManager.PATH_Y * KitchenScreen.UNIT_HEIGHT;

		this.path_y = this.position_y_full;

		// first decide customer type, then generate customer
		this.type = generateCustomerType();

		// calculate wait time
		this.waitTime = calcWaitTime(currentWaitFactor);
		this.originalWaitTime = waitTime;
		
		// decide which line to go in
		this.lineChoice = chooseLine();
		
		selfieHeight = KebabKing.getGlobalYFloat(SELFIE_STICK_HEIGHT);
		selfieWidth = selfieHeight * Assets.selfieStick.getRegionWidth() / Assets.selfieStick.getRegionHeight();	
		
		//		KebabKing.print("line choice: " + this.lineChoice);
		this.initTimeOffset = (float) (Math.random() * 10);
		
//			KebabKing.print("customer created before active");
		createdBeforeActive = !active;
		
		if (active) {
			// this should be between 0 and 1
			float probability = cm.profile.currentReputation * BASE_HUNGER_FACTOR;
			double random = Math.random() ;
			wantsChuanr = random < probability;
			
			if (TutorialEventHandler.dontAllowJeweler()) {
				while (this.type == Customer.CustomerType.JEWELER) {
					this.type = generateCustomerType();
				}
			}
		}
		

		switch(type.gender) {
		case MALE: 
			this.female = false;
			break;
		case FEMALE:
			this.female = true;
			break;
		case EITHER: 
			this.female = Math.random() < 0.5;
			break;
		}
		
//		if (type.idleAlt != null) {
//			if (type.walkLeftAlt == null) throw new java.lang.AssertionError();
//		}

		if (this.type.hasSpc(female) && cm.profile.inventory.adCampaign.isTshirts()) {
			if (female) this.animations = type.female_spc;
			else this.animations = type.male_spc;
		}
		if (Math.random() < ALT_PROB && type.hasAlt(female)) {
			if (female) this.animations = type.female_alt;
			else this.animations = type.male_alt;

		}
		else {
			if (female) this.animations = type.female;
			else this.animations = type.male;
		}
		if (animations == null) {
			KebabKing.print(type);
			KebabKing.print("female: " + female);
		
			throw new java.lang.AssertionError();	
		}
	}

	public CustomerType generateCustomerType() {
//		 testing for now
//		if (Math.random() < .01) ;
//			return CustomerType.SOLDIER;
//		else if (Math.random() < 0.5) 
//			return CustomerType.FOREIGNER;
//		else if (true) return CustomerType.JEWELER;
		
		// do this based off profile
		double[] spread = cm.currentCustomerSpread;

		// first calculate total from Profile
		float sum = 0;
		for (double f : spread) sum += f;

		// calculate probabilities (normalize)
		double[] probs = new double[spread.length];
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
			
			if (walkingLeft) {
				this.position_x_range -= BASE_WALK_SPEED_X * this.type.walkSpeed * delta;				
			}
			else {
				this.position_x_range += BASE_WALK_SPEED_X * this.type.walkSpeed * delta;
			}
			if (!walkingLeft && this.position_x_range * KitchenScreen.WIDTH >= CustomerManager.LINE_POSITIONS[lineChoice] && this.action == CustomerAction.PASS_START) {
				makeDecision();
			}
			if (walkingLeft && this.position_x_range * KitchenScreen.WIDTH <= CustomerManager.LINE_POSITIONS[lineChoice] && this.action == CustomerAction.PASS_START) makeDecision();
		}
		
		// if not on path, walk to stand. but for now, don't worry about this

		// move down
		if (this.action == CustomerAction.ARRIVE) {
			this.position_x_range = CustomerManager.LINE_POSITIONS[lineChoice] / KitchenScreen.WIDTH;
			this.position_y_full -= KebabKing.getGlobalYFloat(BASE_WALK_SPEED_Y * this.type.walkSpeed * delta);
			if (this.position_y_full <= targetY) {
				this.placeOrder();
				this.position_y_full = targetY;
			}
			CustomerManager.SHOULD_ORDER = true;
		}

		if (this.action == CustomerAction.WAIT) {
			updatePatience(delta);
			if (this.waitTime < 0 && !TutorialEventHandler.shouldDisableRageLeave()) startLeaving();
			else if (this.position_y_full > targetY){
				this.position_y_full -= KebabKing.getGlobalYFloat(BASE_WALK_SPEED_Y * this.type.walkSpeed * delta);
			}
			else if (this.position_y_full < targetY) {
				this.position_y_full = targetY;
			}
			if (stompTimer > 0) {
				this.stompTimer -= delta;
			}
		}
		else this.order = null;

		// move up
		if (this.action == CustomerAction.LEAVE) {	
			if (selfieTimer > 0) {
				updateSelfie(delta);
			}
			else {
				this.position_y_full += KebabKing.getGlobalYFloat(BASE_WALK_SPEED_Y * this.type.walkSpeed * delta);
				//			KebabKing.print("leaving");
				//			KebabKing.print(position_y + " " + targetY);
				if (this.position_y_full >= targetY) {
					this.finishLeaving();
					this.position_y_full = targetY;
				}
				CustomerManager.SHOULD_ORDER = true; // only time we need to reorder
			}
		}

		if (this.action == CustomerAction.PASS_END) {
			if (this.shouldRemove()) cm.removeCustomer(this);
		}
	}

	public void draw(SpriteBatch batch, boolean highlight) {
		// if should draw selfie stick
		if (selfieTimer > 0) {
			batch.draw(Assets.selfieStick, KebabKing.getGlobalXFloat(position_x_range + SELFIE_OFFSET_X), position_y_full + KebabKing.getGlobalY(SELFIE_OFFSET_Y), selfieWidth, selfieHeight);
		}
		
		// two possible animations, depending on the state and direction
		TextureRegion toDraw;
		float time = cm.timeElapsed + initTimeOffset;
		
		// don't be moving if taking selfie
		if (selfieTimer > 0) {
			time = 0;
		}
		
		switch (orient) {
		case UP:
			toDraw = animations.up.getKeyFrame(time);
			break;
		case DOWN:
			if ((Math.abs(targetY - position_y_full) < 4 || this.action == CustomerAction.WAIT)) {
				if (stompTimer > 0) {					
					toDraw =  animations.down.getKeyFrame(stompTimer);
				}
				else 
					toDraw =  animations.idle.getKeyFrame(time);
			}
			else {
				toDraw =  animations.down.getKeyFrame(time);
			}
			break;
		case LEFT:
			toDraw =  animations.left.getKeyFrame(time);
			break;
		case RIGHT:
			toDraw =  animations.right.getKeyFrame(time);
			break;
		default:
			toDraw = animations.right.getKeyFrame(time);
		}
		

		int x_pos_orig = (int) (KebabKing.getGlobalX(this.position_x_range));
		int y_pos_orig = (int) (this.position_y_full);
		
		// calculate global position
		int x_pos = x_pos_orig;
		int y_pos = y_pos_orig;

		width =  (int) (TEXTURE_WIDTH * KitchenScreen.UNIT_WIDTH);
		height = (int) (TEXTURE_HEIGHT * KitchenScreen.UNIT_HEIGHT);		

		float highlightScale = 1.1f;
		
		if (highlight) {
			x_pos -= ((width * highlightScale) - width)/2;
			y_pos -= ((height * highlightScale) - height)/2;
			width *= highlightScale;
			height *= highlightScale;
		}
		
		int LINE_TOP = (int) (((CustomerManager.MAX_IN_LINE - 1) * Customer.TEXTURE_HEIGHT * Customer.SQUISH_FACTOR + CustomerManager.LINES_START_Y) * KitchenScreen.UNIT_HEIGHT);


		if (this.action == CustomerAction.PASS_START || this.action == CustomerAction.PASS_END) {
			width =  (TEXTURE_WIDTH_BACK * KitchenScreen.UNIT_WIDTH);
			height = (TEXTURE_HEIGHT_BACK * KitchenScreen.UNIT_HEIGHT);
		}
		// otherwise interpolate size linearly
		else if (y_pos > LINE_TOP && y_pos < this.path_y) {
			// height of top of line (assuming max 3 per line)
			//			KebabKing.print(y_pos);
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
//			drawOrder(batch, x_pos, y_pos);
			batch.end();
			// each character will have their own stage. Probably bad practice but definitely helps.
			this.order.draw(x_pos_orig, y_pos_orig);
			batch.begin();
		}

		if (this.action == CustomerAction.LEAVE) {
//			if (this.satisfaction <= 0) KebabKing.print("SAT LESS THAN 0");
			//			this.satisfaction = 2;
			if (this.satisfaction > 0) this.drawHappyness(batch, x_pos_orig, y_pos_orig);
		}
		
//		if (highlight) {
//			highlight(batch);
//		}
	}
	
	public float getStrictLeft() {
		int x_pos = KebabKing.getGlobalX(this.position_x_range);
		return x_pos + (TEXTURE_WIDTH * KitchenScreen.UNIT_WIDTH - highlightWidth) / 2;
	}
	
	public float getStrictRight() {
		return getStrictLeft() + highlightWidth;
	}

	public float getStrictBottom() {
		int y_pos = (int) (this.position_y_full);
		return y_pos + (TEXTURE_HEIGHT * KitchenScreen.UNIT_HEIGHT - highlightHeight)/2;
	}
	
	public float getStrictTop() {
		return getStrictBottom() + highlightHeight;
	}
	
	public void updatePatience(float delta) {
		this.waitTime -= delta;
		if (waitTime < 0) return;
		if (order == null) return;
		
		float adjustedMax = originalWaitTime - order.calcMinCookTime();
	
		float waitTimeFactor = waitTime / adjustedMax;
		waitTimeFactor = Math.min(1, waitTimeFactor);
//		KebabKing.print(waitTimeFactor + " " + (int) (waitTimeFactor * 5 + 0.49));
		
		if (this.currentPatience != (int) (waitTimeFactor * 5 + 0.49)) {
			this.currentPatience = (int) (waitTimeFactor * 5 + 0.49);			
			stompFeet(5-currentPatience);
		}
	}
	
	public void stompFeet(int times) {
		KebabKing.print("STOMPING FEET");
		this.stompTimer = 2 * times * this.type.animationTime;
	}
	
	public void drawHappyness(SpriteBatch batch, int x_pos, int y_pos) {
		int happiness_x = x_pos - KebabKing.getGlobalX(0.05f);

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
			KebabKing.print(satisfaction);
			throw new java.lang.AssertionError();
		}
		if (this.sick) icon = Assets.faceSick;

		float arrowHeight = width * SAT_SIZE * 0.4f;
		float arrowWidth = arrowHeight * Assets.greenArrow.getRegionWidth() / Assets.greenArrow.getRegionHeight();
		                
		float arrowOffsetY = (width*SAT_SIZE - arrowHeight) / 2;
		arrowOffsetY = width * SAT_SIZE * 0.1f;
		
		batch.draw(icon, happiness_x + width, y_position, (int) (width * SAT_SIZE), (int) (width * SAT_SIZE));

		if (drawArrow && satisfaction > 1 && !sick) {
			float percent = cm.timeElapsed - (int) cm.timeElapsed;
			float alpha = 0;
			if (percent < 0.5) {
				alpha = percent * 2;
			}
			else {
				alpha = (1 - percent) * 2;
			}
			Color o = batch.getColor();
			cm.arrowTint.set(1,  1,  1, alpha);
			batch.setColor(cm.arrowTint);
			batch.draw(Assets.greenArrow, happiness_x + width, y_position + arrowOffsetY, arrowWidth, arrowHeight);
			batch.setColor(o);
		}
	}

	public void highlight(SpriteBatch batch) {
		// calculate global position
//		int x_pos = KebabKing.getGlobalX(this.position_x);
//		int y_pos = (int) (this.position_y);

//		Color orig = batch.getColor();
//		Color myColor = new Color(1, 1, 1, 0.3f);
//		batch.setColor(myColor);
		batch.draw(Assets.whiteAlpha, getStrictLeft(), 
										getStrictBottom(), highlightWidth, highlightHeight);
//		myColor.a = 1f; // necessary for some reason
//		batch.setColor(orig);
	}

	// move to Customer Manager
	public void makeDecision() {
		if (!cm.active || this.createdBeforeActive) {
			action = CustomerAction.PASS_END;
			return;
		}
		
		if (cm.totalPeopleInLines() >= cm.master.profile.getLocation().getMaxCustomers()) {
			action = CustomerAction.PASS_END;
			return;
		}
		
		if (cm.peopleInLine(lineChoice) >= maxPerLine()) {
			action = CustomerAction.PASS_END;
			return;
		}
		
		if (cm.master.kitchen != null && cm.master.kitchen.lastCustomer) {
			action = CustomerAction.PASS_END;
			return;
		}
		
		if (TutorialEventHandler.dontAllowCustomer()) {
			KebabKing.print("Don't allow customer");
			action = CustomerAction.PASS_END;
			return;
		}
		
		//tutorial, or force
		if (cm.totalPeopleInLines() == 0) {
			startArriving();
			return;
		}

		
		if (this.policeShutdown) {
			
			if (cm.peopleInLine[this.lineChoice] >= CustomerManager.MAX_IN_LINE) {
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

		//		KebabKing.print("random: " + random + ", probability: " + probability + ", decision: " + wantsChuanr)
		
		float maxAtOnce = cm.master.profile.getCurrentReputation();
		
		if (cm.totalPeopleInLines() >= maxAtOnce - 1) {
			if (cm.master.kitchen != null && cm.master.kitchen.forceArrive()) {
				return;
			}
			else if (!wantsChuanr) {
				action = CustomerAction.PASS_END;
			
			}
		}
		else {
			startArriving();
		}
	}
	
	public int maxPerLine() {
		if (cm.master.profile.getLocation().getMaxCustomers() <= 2) return 1;
		return CustomerManager.MAX_IN_LINE;
	}

	public void startArriving() {
		this.orient = Orient.DOWN;
		cm.addToLine(this, lineChoice);
		action = CustomerAction.ARRIVE;
		updateTargetY();
		if (cm.master.kitchen != null) cm.master.kitchen.handleCustomerApproaching();
		
		TutorialEventHandler.handleCustomerApproach();
	}

	// walk away from the stand
	public void startLeaving() {		
//		if (this.tutorialFirst) {
//			cm.shouldAddSecondCustomer = true;
//			
//			TutorialScreen ts = (TutorialScreen) cm.master.kitchen;
//			ts.transitionToNext();	
//			if (ts.current == TutorialScreen.Step.ClickWoman) ts.transitionToNext();
//				
//		}
//		else if (this.tutorialSecond) {
//			((TutorialScreen) cm.master.kitchen).transitionToNext();	
//		}
		
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

		// satisfaction is between 1 and 5 (don't round for now)
		satisfaction = calculateSatisfaction();

		sick = calculateSick();
		if (sick) {
			cm.totalSick++;
			satisfaction = 1;
		}
		
		// save this stuff to the total
		if (this.waitTime > 0) cm.happyCustomers++;
		cm.totalCustomers++;
		cm.totalSatisfaction += satisfaction;

		if (this.sick && canShutDown()) {
			float generatePoliceNextProb = POLICE_SHUTDOWN_BASE;
			
			// for now, if they've played enough make it harder
			if (cm.profile.getLevel() > 3) generatePoliceNextProb = 0.8f;
			
			// if this is true, a policeman will DEFINITELY come and shut you down
			// what about if its the end of the day?
			// should STILL be shut down
			if (Math.random() < generatePoliceNextProb) {
				cm.generatePoliceNext();
				cm.master.kitchen.preShutDown();
			}
		}
		
		if (cm.profile.inventory.adCampaign.isSelfie() && satisfaction >= 4 && !sick) {
			this.startSelfie();
		}
		
		KebabKing.print("Playing leaving sounds");
		SoundManager.playLeavingSound(type, satisfaction, sick);
	}
	
	public int calculateSatisfaction() {
		// increase leniency by increasing this
		float SAT_BOOST = 1.03f;
		
		// or by increasing base customer wait time
		
		float rawSat = currentPatience;
		
		// boost raw sat so even if you take forever, they wont be furious
		KebabKing.print("raw sat pre adjust: " + rawSat);
		rawSat *= 4f/5;
		rawSat += 1;
		KebabKing.print("raw sat post adjust: " + rawSat);
		
		// accuracy factor can be between 1 and 0
		// perfect order (everything satisfied, no burnt) is a perfect 1

		float accuracyFactor = ((order.total - order.remaining) / (float) order.total) * ((order.total - (order.burnt + order.raw + order.wrongSpiciness)) / (float) order.total);
		if (accuracyFactor < 0) accuracyFactor = 0;
		
//		accuracy factor can only hurt you.
		rawSat *= accuracyFactor;
		rawSat *= cm.profile.inventory.skewerType.getSatBoost() * SAT_BOOST;
		
		float adBoost = cm.profile.inventory.adCampaign.getSatBoost(this.type);
		if (adBoost != 1) {
			rawSat *= adBoost;
			this.drawArrow = true;
			KebabKing.print("Boosting satisfaction by " + adBoost);
		}
		
		KebabKing.print("raw sat post boost: " + rawSat);

		// round to nearest score
		// so 4.5 is perfect, 3.5-4.5 is weaker
		int satisfaction = (int) (rawSat + 0.5);
		
		// only a 4.5 or better will get you 5 stars
		// 3.5-4.5 will get you 4 stars

		satisfaction = Math.min(5, satisfaction);
		satisfaction = Math.max(1, satisfaction);
		if (satisfaction < 0) throw new java.lang.AssertionError();
		KebabKing.print("final sat: " + rawSat);
		
		return satisfaction;
	}
	
	public boolean calculateSick() {
		// calculate if the person gets sick or not
		double sickChance = .8;

		boolean toReturn = false;
		if (Math.random() * order.raw  > 1 - sickChance) toReturn = true;
		
		return toReturn;
	}
	
	public boolean canShutDown() {
		return cm.profile.stats.tutorialComplete() && cm.profile.getLevel() >= 3;
	}

	public void finishLeaving() {
		if (this.walkingLeft) {
			this.orient = Orient.LEFT;
		}
		else {
			this.orient = Orient.RIGHT;
		}
		this.action = CustomerAction.PASS_END;
	}

	// is this person done with their path?
	public boolean shouldRemove() {
		if (!walkingLeft && this.position_x_range > END_RIGHT) return true;
		if (walkingLeft && this.position_x_range < END_LEFT) return true;
		return false;
	}
	
	public void startSelfie() {
		selfieTimer = SELFIE_WAIT_LENGTH;
	}

	public void updateSelfie(float delta) {
		selfieTimer -= delta;
		if (selfieTimer <= 0) {
			DrawUI.startFlash();
		}
	}
	

	// given this profile's wait factor, calculate wait time
	public float calcWaitTime(float customerPatienceFactor) {
		// TODO add randomness

		// difficulty increases as days go on.
		// after 10 days, customers wait 5% less
		// after 30 days, customers wait 10% less
		// after 70 days, customers wait 15% less
		// after 150 days, customers wait 20% less
		
		
		// do this based on location...

		// can increase this to make the game harder, or decrease it to make it easier.
		// scales logarithmically
//		float DIFFICULTY = 0.05f;

//		float difficultyFactor = (float) (Math.log((cm.profile.getDaysWorked + 10.0) / 10.0) / Math.log(2.0));
//		difficultyFactor = 1;
//		difficultyFactor *= DIFFICULTY;
//		difficultyFactor = 1 -difficultyFactor;
		
		// TODO make higher locations more difficult?
		float difficultyFactor = 1;
		
		float maxTime = customerPatienceFactor * BASE_WAIT_TIME * this.type.patienceFactor;

//		KebabKing.print("max time: " + maxTime + " difficultyFactor: " + difficultyFactor + ", together: " + maxTime*difficultyFactor);
		return maxTime * difficultyFactor;
	}

	public void updateTargetY() {
		this.targetY = (int) (KitchenScreen.convertY((CustomerManager.LINES_START_Y + lineIndex * (TEXTURE_HEIGHT * SQUISH_FACTOR))));
		//		KebabKing.print(CustomerManager.LINES_START_Y  + " " + lineIndex * TEXTURE_HEIGHT);
		//		KebabKing.print("targetY = " + targetY);
		//		KebabKing.print("y_pos = " + position_y);
	}

	public int chooseLine() {
		// just pick the first line
		if (walkingLeft) return 1;
		else return 0;

		// do a weighted decision, variable amount of lines
//		int index = 0;
//		if (Math.random() < .5 || cm.totalPeopleInLines() == 0) {
//			index = (int) (Math.random() * CustomerManager.LINE_POSITIONS.length);
//		}
//		else {
//			// alternatively, choose the line with lowest
//			int minCount = Integer.MAX_VALUE;
//			for (int i = 0; i < CustomerManager.LINE_COUNT; i++) {
//				if (cm.peopleInLine(i) <= minCount) {
//					if (cm.peopleInLine(i) == minCount && Math.random() < .5) {
//						minCount = cm.peopleInLine(i);
//						index = i;
//					}
//					else {
//						minCount = cm.peopleInLine(i);
//						index = i;
//					}
//				}
//			}
//		}
//		return index;
	}

	public void placeOrder() {
		if (this.policeShutdown) {
			this.action = CustomerAction.WAIT;
			// bring up option for paying. Otherwise, shut down
			cm.master.shutdownStand();
			return;
		}
		
		this.currentPatience = 5;
		SoundManager.playOrderSound(this.type);

		this.order = new Order(this.type, this.cm.master.kitchen);
		TutorialEventHandler.handleCustomerOrders();
		
		if (order.hasSpicy()) {
//			if (TutorialEventHandler.shouldNotOrderSpice()) order.removeSpice();
			TutorialEventHandler.handleSpiceOrder(order.getSpicyType());
		}
		if (order.hasDouble()) {
			TutorialEventHandler.handleDoubleOrder(order.getDoubleType());
		}
		if (order.hasBeer()) {
//			if (TutorialEventHandler.shouldNotOrderBeer()) order.removeBeer();
			TutorialEventHandler.handleBeerOrder();
		}
		if (this.type == CustomerType.JEWELER) {
			cm.registerJewelerOrdering();
		}
			
		//		order.print();
		this.action = CustomerAction.WAIT;
		
//		if (this.tutorialFirst) {
//			this.order.beef = 0;
//			this.order.chicken = 1;
//			this.order.lamb = 3;
//			this.order.beer = 0;
//			this.order.beefSpicy = false;
//			this.order.lambSpicy = true;
//			this.order.chickenSpicy = false;
//			this.order.remaining = 4;
//			
//			TutorialScreen ts = (TutorialScreen) this.cm.master.kitchen;
//			ts.transitionToNext();
////			ts.firstOrder();
//		} 
//		else if (this.tutorialSecond) {
//			this.order.beef = 0;
//			this.order.chicken = 0;
//			this.order.lamb = 4;
//			this.order.beer = 1;
//			this.order.beefSpicy = false;
//			this.order.lambSpicy = true;
//			this.order.chickenSpicy = false;
//			this.order.remaining = 5;
//			
//			TutorialScreen ts = (TutorialScreen) this.cm.master.kitchen;
//			ts.transitionToNext();
////			ts.secondOrder();
//		}
	}

	public void handleMoneyEarned(float moneyPaid) {
		if (moneyPaid == 0) return;
		int roundUp = (int) Math.ceil(moneyPaid);
		createProjectiles(roundUp, false);
	}
	
	public void orderComplete() {
		this.startLeaving();
		if (this.type == CustomerType.JEWELER) {
			handleJewelerComplete();
		}
	}
	
	public void handleJewelerComplete() {
		// give coins based on satisfaction
		int coinReward = Math.max(0, this.satisfaction - 2);
		if (coinReward <= 0) {
			return;
		}
		
		cm.master.profile.giveCoins(coinReward);
		createProjectiles(coinReward, true);
		// launch a window?
	}
	
	public void createProjectiles(int count, boolean jade) {
		DrawUI.createProjectiles(count, this.position_x_range + this.width / 2 / KebabKing.getWidth(), (this.position_y_full + this.height/2) / KebabKing.getHeight(), jade);		
	}

	// returns revcost array (first element is rev, second is cost)
	public float[] giveMeat(ArrayList<Meat> meat) {
		float[] moneyPaid = this.order.giveMeat(meat);
		//		order.print();
		if (order.remaining == 0) {
			this.orderComplete();
		}
		handleMoneyEarned(moneyPaid[0] - moneyPaid[1]);
		return moneyPaid;
	}

	
	public float giveBeer() {
		float toReturn = this.order.giveBeer();
		if (order.remaining == 0) {
			this.orderComplete();
		}
		handleMoneyEarned(toReturn);
	//		order.print();
		return toReturn;
	}

	@Override
	public int compareTo(Customer that) {
		float diff = (that.position_y_full - this.position_y_full);
		//		KebabKing.print(diff);
		if (diff == 0) {
			float diff2 = (this.position_x_range - that.position_x_range);
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
