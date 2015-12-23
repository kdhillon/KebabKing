package com.kebabking.game;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kebabking.game.Customer.CustomerType;


/**
 * This class generates customers, moves customers, removes customers, and manages police.
 * should be created every time a new KitchenScreen is created
 * @author Kyle
 */
public class CustomerManager {
	// make one line with 5 positions

	// could do an array of three "lines", at various x unit positions, that people wait in.
	// start off with just one
//	static final float[] LINE_POSITIONS = new float[] {4.5f};
	static final float[] LINE_POSITIONS = new float[] {0.75f, 7.2f};
//	static final float[] LINE_POSITIONS = new float[] {0, 3.5f, 7f};

	static final float PATH_Y = 10f; // out of 18
	static final float GENERATE_BASE_TIME = 8;
	
	static final float LINES_START_Y = 4.9f; // where should the people wait
	static final int LINE_COUNT = LINE_POSITIONS.length;
	static final int MAX_IN_LINE = 2;
	static boolean SHOULD_ORDER = true; // sorts every frame

	KebabKing master;
	Profile profile;
	Customer mousedOver;
	Customer[][] lines;
	int[] peopleInLine;

	boolean active;
	
	// only used for tutorial
	boolean tutFirstCustomer;
	boolean tutSecondCustomer;
	boolean shouldAddSecondCustomer;

	int maxAtOnce; // number of people in  lines at one time

	boolean generatePoliceNext;
	
	float customerGenerationMaxTime; // generate a customer every 0.5 to this number of seconds?
	float customerPatienceFactor;
	float timeElapsed;

	float expenses;
	float revenue;

	int happyCustomers; 	// customers that were actually served
	int totalCustomers;		// total satisfaction of each customer
	int totalSatisfaction; 	// sum of all satisfactions
	int totalSick; 			// number of customers who got sick

	float lastCustomer; // time since last customer created;
	float nextCustomer; // generated randomly, when next customer should spawn
	
	float customerGenerationRate = 1;

	// going to need to iterate through, add, remove
	//	ArrayList<Customer> customers;

	ArrayList<Customer> customers;

	public CustomerManager(KebabKing master) {
		this.master = master;
		this.profile = master.profile;
		this.customerPatienceFactor = profile.customerPatienceFactor;
		this.customerGenerationMaxTime = getGenerationMaxTime(profile);

		//		customers = new ArrayList<Customer>();
		customers = new ArrayList<Customer>();

		this.maxAtOnce = (profile.daysWorked + 3);

		lines = new Customer[LINE_COUNT][MAX_IN_LINE];
		peopleInLine = new int[LINE_COUNT];

		lastCustomer = 0;
	}

	public void reset() {
		// clear everything
		this.customers.clear();
		lines = new Customer[LINE_COUNT][MAX_IN_LINE];
		peopleInLine = new int[LINE_COUNT];	
		this.lastCustomer = 0;
		this.timeElapsed = 0;

		this.happyCustomers = 0;
		this.totalCustomers = 0;
		this.totalSatisfaction = 0;
		this.totalSick = 0;

		this.activate();
	}

	public void activate() {
		this.active = true;
	}

	public void deactivate() {
		this.active = false;
	}

	public void act(float delta) {
		timeElapsed += delta;

		// decide whether to generate customers
		generateCustomers();

		Object[] array = customers.toArray();

		for (Object o : array) {
			Customer c = (Customer) o;
			c.act(delta);
		}

		lastCustomer += delta;
		
//		if (this.active)
		// This happens at ALL TIME
		// in fact, this should be based on global clock
		// we want players to play as many rounds as possible during the 
		// time that this campaign lasts
		master.profile.inventory.updateAds();
	}

	// draw all customers
	public void draw(SpriteBatch batch) {
		// order isn't working right now, fix later
		//		System.out.println("all customers order");

		// note that this can be optimized to only sort on 
		// frames in which customers actually move
		if (SHOULD_ORDER) {
			Collections.sort(customers);
			SHOULD_ORDER = false;
		}

		for (Customer c : customers) {
			c.draw(batch);
		}

		if (mousedOver != null && mousedOver.action == Customer.CustomerAction.WAIT && active) {
			mousedOver.highlight(batch);
		}
	}

	// called when holding down the mouse
	public void updateMousedOver(int x, int y) {
		if (!active) return;

		// simply check if there is a customer in this range, and only worry about customers in lines
		y = KebabKing.getHeight() - y;
		// first check unit position
		mousedOver = null;

		for (int i = 0; i < LINE_COUNT; i++) {
			for (int j = 0; j < MAX_IN_LINE; j++) {
				Customer c = lines[i][j];
				if (c != null) {
					int xPos = (int) (KebabKing.getGlobalX(c.position_x));
					int yPos = (int) c.position_y;
					if (x > xPos && x < xPos + Customer.TEXTURE_WIDTH * KitchenScreen.UNIT_WIDTH) {
						if (y > yPos && y < yPos + Customer.TEXTURE_HEIGHT * KitchenScreen.UNIT_HEIGHT) {
							mousedOver = c;
							return;
						}
					}
				}
			}
		}
	}

	public void generateCustomers() {
		// tutorial mode
		if (profile.tutorialNeeded && master.getScreen() == master.kitchen) {
			if (!tutFirstCustomer) {
				this.reset();
				addFirstTutorialCustomer();
				this.tutFirstCustomer = true;
			}
//			if (!tutSecondCustomer && shouldAddSecondCustomer) {
//				addSecondTutorialCustomer();
//				this.tutSecondCustomer = true;
//				this.shouldAddSecondCustomer = false;
////				TutorialScreen ts = (TutorialScreen) this.master.kitchen;
////				ts.transitionToNext();
//			}
//			System.out.println("tutorial needed");
			return;
		}
		
		// decide whether to create a customer this frame
		if (customers.size() == 0) {
			addCustomer();
			return;
		}
		
		if (lastCustomer > nextCustomer) addCustomer();

		//TODO make this based on location you're in.
	}

	public void addCustomer() {
		//		System.out.println("adding customer");
		Customer customer = new Customer(customerPatienceFactor, this);
	
		if (this.generatePoliceNext) {
			customer.type = Customer.CustomerType.POLICE;
			customer.policeShutdown = true;
			generatePoliceNext = false;
		}
		
		if (!customers.add(customer)) {
			System.out.println("CANT ADD, ALREADY EXISTS");
		}
		lastCustomer = 0;
		calcNextCustomer();
		
		SHOULD_ORDER = true;
	}
	
	// add the first customer
	public void addFirstTutorialCustomer() {
		Customer customer = new Customer(customerPatienceFactor, this);
//		customer.type = CustomerType.OLD_WOMAN;
		customer.type = CustomerType.OLD_MAN;
		customer.tutorialFirst = true;
		if (!customers.add(customer)) {
			System.out.println("CANT ADD, ALREADY EXISTS");
		}
		
		lastCustomer = 0;
		this.nextCustomer = (float) (Math.random() * (customerGenerationMaxTime - 0.5f) + 0.5f);
		
		SHOULD_ORDER = true;
	}
	
	public void addSecondTutorialCustomer() {
		Customer customer = new Customer(customerPatienceFactor, this);
		customer.type = CustomerType.FAT_MAN;
		customer.tutorialSecond = true;
		
		if (!customers.add(customer)) {
			System.out.println("CANT ADD, ALREADY EXISTS");
		}
		
		lastCustomer = 0;
		this.nextCustomer = (float) (Math.random() * (customerGenerationMaxTime - 0.5f) + 0.5f);
		
		SHOULD_ORDER = true;
	}
	
	public void calcNextCustomer() {
		this.customerGenerationMaxTime = getGenerationMaxTime(this.profile);
		this.nextCustomer = (float) (Math.random() * (customerGenerationMaxTime - 0.5f) + 0.5f);
	}

	// call this if someone gets sick and the stand should be shutdown
	public void generatePoliceNext() {
		this.generatePoliceNext = true;
	}
	
	public float getGenerationMaxTime(Profile profile) {
		return GENERATE_BASE_TIME * (1/(float) (profile.getLocation().popularity * profile.boost));
	}

	public void removeCustomers() {
		// TODO: improve efficiency?
		for (Customer c : customers) {
			if (c.shouldRemove()) removeCustomer(c);
		}
	}

	public void removeCustomer(Customer customer) {
		//		System.out.println("removing customer");
		// calculate sickness if sick
		// modify reputation

		this.customers.remove(customer);
	}

	public void addToLine(Customer customer, int lineChoice) {
		customer.lineIndex = getBackOfLine(lineChoice);
		lines[lineChoice][customer.lineIndex] = customer;
		peopleInLine[lineChoice]++;
	}

	public void moveUpLine(int lineChoice, int lineIndex) {
		for (int i = lineIndex; i < MAX_IN_LINE; i++) {
			Customer c = lines[lineChoice][i];
			if (c == null) continue;
			c.lineIndex--; // should never be below 0;
			lines[lineChoice][i] = null;
			lines[lineChoice][i-1] = c;
			if (c.lineIndex < 0) throw new java.lang.ArrayIndexOutOfBoundsException();
			c.updateTargetY();
		}
	}

	public void removeFromLine(int lineChoice, int lineIndex) {
		peopleInLine[lineChoice]--;
		lines[lineChoice][lineIndex] = null;
	}

	public int peopleInLine(int index) {
		return peopleInLine[index];
	}

	public int totalPeopleInLines() {
		int sum = 0;
		for (int i = 0; i < LINE_COUNT; i++) {
			sum += peopleInLine(i);
		}
		return sum;
	}

	public int getBackOfLine(int index) {
		return peopleInLine(index);
	}

	public float calculateProfit() {
		return revenue - expenses;
	}

}
