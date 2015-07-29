package com.chuanrchef.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

// contains information for the grill, the trash, spice box, and ice chests
public class Grill {
	static final int GRILL_X = 0;
	static final float GRILL_Y = 2.5f;
	static final int GRILL_WIDTH = 10;
	static final float GRILL_HEIGHT = 2.5f;

	static final int GRILL_PIECE_WIDTH = 1;
	static final float GRILL_PIECE_HEIGHT = GRILL_HEIGHT;
	static final double CHUANR_PER_PIECE = 1;

	// bottom values
	static final int BOX_HOR_WIDTH = 3;
	static final int BOX_HOR_HEIGHT = 2;
	static int SPICE_WIDTH = 2;
	static final int SPICE_HEIGHT = 2;
//	static float TRASH_WIDTH = 0.01f;
//	static final int TRASH_HEIGHT = 3;
	static final int BEER_WIDTH = 3;
	static final int BEER_HEIGHT = 2;
	static final int CHICKEN_X = 0;
	static final int BEEF_X = 3;
	static final int LAMB_X = 6;
	static final int BOTTOM_Y = 0;
	static final int BEER_X = 9;
	static final int BEER_Y = 0;
	static int SPICE_X = 10;
	static final int SPICE_Y = 3;
//	static final int TRASH_X = 0;
//	static final int TRASH_Y = 2;
	
	private int draw_width;
	private int draw_height;
	private int draw_x;
	private int draw_y;

	private float time;
	private int xOffset;
	private final int yOffset = 0;

	public enum Selected {
		NONE, CHICKEN, BEEF, LAMB, SPICE, BEER
	}

	Selected selected;
	
	boolean tutorialMode;

	/** if false, grill cannot be interacted with */
	boolean active; 
	boolean disableTouch;
	
	Profile profile;
	KitchenScreen ks; // only non-null when active
	int size; 
	Meat[] meat;
	int mousedOver; // index of moused-over slot on grill?
	//	boolean meatBoxSelected; // for use when highlighting grill

	boolean mousedOverTrash = false;
	//	boolean boxSelectedNotHeld = false; // try to use this to differentiate between holding and clicking
	boolean meatSelectedNotHeld = false;
	boolean placedWhileHeld = false; // true when meat has been placed during the 'hold'
	int removeOnRelease = -1;

	ArrayList<Meat> selectedSet;

//	// think of a good way to keep this flexible if we want to add more grills later on.
//	enum GrillType { 
//		SMALL(8), MEDIUM(10), LARGE(10), HUGE(16);
//		int size;
//		boolean warming;
//
//		private GrillType(int size) {
//			this.size = size;
//		}
//	}

	public Grill(Profile profile) {
//		this.grillType = profile.grillType;
		this.profile = profile;
		this.mousedOver = -1;

		this.selectedSet = new ArrayList<Meat>();
		selected = Selected.NONE;		

		updateSize();

		this.time = 0;
		clearMeat();
	}
	
	public void reset(KitchenScreen ks) {
		this.ks = ks;
		clearMeat();
		this.activate();
		this.updateSize();
		this.selectedSet.clear();
		this.selected = null;
	}
	
	// this updates the values that are used to draw grill, must be updated when grill changes
	public void updateSize() {
		this.size = profile.grillSize();
		if (size == 6) {
			SPICE_X = 8;
		}
		if (size == 8) {
			SPICE_X = 9;
		}
		if (size == 10) {
			SPICE_X = 10;
		}

		xOffset = (GRILL_WIDTH - size) / 2;
		
		draw_width = GRILL_PIECE_WIDTH * KitchenScreen.UNIT_WIDTH;
		draw_height =(int) ( GRILL_PIECE_HEIGHT * KitchenScreen.UNIT_HEIGHT);

		draw_x = (xOffset + GRILL_X) * KitchenScreen.UNIT_WIDTH;
		draw_y = (int) ((yOffset + GRILL_Y) * KitchenScreen.UNIT_HEIGHT);
	}

	public void activate() {
		this.active = true;	
	}

	public void clearMeat() {
		int length = this.getGrillSize();
		this.meat = new Meat[(int) (length * CHUANR_PER_PIECE)];
	}

	public void deactivate() {
		System.out.println("deactivating");
		this.selected = Selected.NONE;
		this.ks = null;
		this.active = false;
	}

	public void act(float delta) {
		if (!active) {
			return;
		}

		//		if (!this.newMeatSelected()) this.boxSelectedNotHeld = false;

		for (int i = 0; i < meat.length; i++) {
			Meat m = meat[i];
			if (m != null && !(m.chicken() && m.index2 == i))
				m.act(delta);
		}
		this.time += delta;
	}

	public void draw(SpriteBatch batch) {
		// this is required for now.
		this.updateSize();
		
		drawBoxes(batch);

		Color original = batch.getColor();
		if (!this.active) {
			batch.setColor(Color.GRAY);
		}
		
		TextureRegion fire = Assets.grillFire.getKeyFrame(time);
		if (!active) {
			fire = Assets.gray;
		}

		int xCount = xOffset;

		// draw left
		batch.draw(fire, draw_x, draw_y, draw_width, draw_height); 
		batch.draw(Assets.grillCoals, draw_x, draw_y, draw_width, draw_height); 
		batch.draw(Assets.grillLeft, draw_x, draw_y, draw_width, draw_height); 
		xCount++;

		// draw middle
		while (xCount < GRILL_WIDTH - xOffset - 1) {
			draw_x = (xCount + GRILL_X) * KitchenScreen.UNIT_WIDTH;
			batch.draw(fire, draw_x, draw_y, draw_width, draw_height); 
			batch.draw(Assets.grillCoals, draw_x, draw_y, draw_width, draw_height); 
			batch.draw(Assets.grillMid, draw_x, draw_y, draw_width, draw_height);
			xCount++;
		}

		// draw right		 
		draw_x = (xCount + GRILL_X) * KitchenScreen.UNIT_WIDTH;
		batch.draw(fire, draw_x, draw_y, draw_width, draw_height); 
		batch.draw(Assets.grillCoals, draw_x, draw_y, draw_width, draw_height); 
		batch.draw(Assets.grillRight, draw_x, draw_y, draw_width, draw_height); 

		if (active)
			drawMeat(batch);
		else batch.setColor(original);
		
		// draw spice paintbrush
		if (this.selected == Selected.SPICE && Gdx.input.isTouched()) {
			float brushWidth = 80;
			float brushHeight = 120;
			
			batch.draw(Assets.paintBrush, Gdx.input.getX() - brushWidth/2, ChuanrC.height-Gdx.input.getY() - brushHeight/8, brushWidth, brushHeight);
		}
	}
	
	
	public void tutDrawLeftGrill(SpriteBatch batch) {
		// draw left
		draw_x = (0 + GRILL_X + xOffset) * KitchenScreen.UNIT_WIDTH;
		batch.draw(Assets.grillFire.getKeyFrame(0), draw_x, draw_y, draw_width, draw_height); 
		batch.draw(Assets.grillCoals, draw_x, draw_y, draw_width, draw_height); 
		batch.draw(Assets.grillLeft, draw_x, draw_y, draw_width, draw_height); 
	}

	public void tutDrawGrill(int index, SpriteBatch batch) {
		// draw middle
		draw_x = (index + GRILL_X + xOffset) * KitchenScreen.UNIT_WIDTH;
		batch.draw(Assets.grillFire.getKeyFrame(0), draw_x, draw_y, draw_width, draw_height); 
		batch.draw(Assets.grillCoals, draw_x, draw_y, draw_width, draw_height); 
		batch.draw(Assets.grillMid, draw_x, draw_y, draw_width, draw_height);
	}
	
	public void tutDrawRightGrill(SpriteBatch batch) {
		draw_x = (5 + GRILL_X + xOffset) * KitchenScreen.UNIT_WIDTH;
		batch.draw(Assets.grillFire.getKeyFrame(0), draw_x, draw_y, draw_width, draw_height); 
		batch.draw(Assets.grillCoals, draw_x, draw_y, draw_width, draw_height); 
		batch.draw(Assets.grillRight, draw_x, draw_y, draw_width, draw_height); 
	}

	public void touchInput(int x, int y) {		
		if (!active || disableTouch) return;

		// KitchenScreen.convert to unit coordinates
		int unit_x = KitchenScreen.getUnitX(x);
		int unit_y = KitchenScreen.getUnitY(y);

		// if not on grill
		Selected box = touchBox(unit_x, unit_y);
		if (box != Selected.NONE && !ks.paused) {
			select(box);
			return;
		}

		// if on grill
		if (onGrill(unit_x, unit_y) && (selected == Selected.NONE || meatSelected())) {
			int index = getGrillIndex(x, y);
			if (meat[index] != null) {	
				if (selectedSet.contains(meat[index])) {
					if (meatSelectedNotHeld) {
						//						System.out.println("removing from selected");
						removeOnRelease(index);
						
						
						//						selectedSet.remove(meat[index]);
					}
				}
				else select(index);
			}
			return;
		}
	}


//	public void highlightTrash(SpriteBatch batch) {
//		int trash_x = KitchenScreen.convertXWithBuffer(TRASH_X);
//		int trash_y = KitchenScreen.convertYWithBuffer(TRASH_Y);
//		int box_sq_width = KitchenScreen.convertWidth(TRASH_WIDTH);
//		int box_sq_height = KitchenScreen.convertHeight(TRASH_HEIGHT);
//
//		// make this see through
//		//		Color myColor = Color.RED;
//		//		myColor.a = .4f;
//		//		batch.setColor(myColor);
//		batch.draw(Assets.white, trash_x, trash_y, box_sq_width, box_sq_height);
//		//		batch.setColor(1, 1, 1, 1);
//	}

	public void drawBoxes(SpriteBatch batch) {
		int box_hor_width 	= KitchenScreen.convertWidth(BOX_HOR_WIDTH);
		int box_hor_height 	= KitchenScreen.convertHeight(BOX_HOR_HEIGHT);
		int spice_width 	= KitchenScreen.convertWidth(SPICE_WIDTH);
		int spice_height 	= KitchenScreen.convertHeight(SPICE_HEIGHT);
		int box_ver_width 	= KitchenScreen.convertWidth(BEER_WIDTH);
		int box_ver_height 	= KitchenScreen.convertHeight(BEER_HEIGHT);
		int chicken_x 	= KitchenScreen.convertXWithBuffer(CHICKEN_X);
		int beef_x 		= KitchenScreen.convertXWithBuffer(BEEF_X);
		int lamb_x 		= KitchenScreen.convertXWithBuffer(LAMB_X);
		int bottom_y 	= KitchenScreen.convertYWithBuffer(BOTTOM_Y);
		int beer_x 		= KitchenScreen.convertXWithBuffer(BEER_X);
		int beer_y 		= KitchenScreen.convertYWithBuffer(BEER_Y);
		int spice_x 	= KitchenScreen.convertXWithBuffer(SPICE_X);
		int spice_y 	= KitchenScreen.convertYWithBuffer(SPICE_Y);
		
		// draw boxes in their proper locations
//		batch.setColor(1, 1, 1, 1);

		Color original = batch.getColor();
		if (!active) {
			batch.setColor(Color.GRAY);
		}
		
		if (selected == Selected.CHICKEN) 
			batch.draw(Assets.chickenBoxOpen, chicken_x, bottom_y, box_hor_width, box_hor_height);
		else 
			batch.draw(Assets.chickenBox, chicken_x, bottom_y, box_hor_width, box_hor_height);
		
		if (selected == Selected.BEEF) 
			batch.draw(Assets.beefBoxOpen, 	beef_x, bottom_y, box_hor_width, box_hor_height);
		else
			batch.draw(Assets.beefBox, 	beef_x, bottom_y, box_hor_width, box_hor_height);
		
		if (selected == Selected.LAMB) 
			batch.draw(Assets.lambBoxOpen, lamb_x, bottom_y, box_hor_width, box_hor_height);
		else
			batch.draw(Assets.lambBox, lamb_x, bottom_y, box_hor_width, box_hor_height);
		
		if (selected == Selected.BEER) 
			batch.draw(Assets.beerBoxOpen, beer_x, beer_y, box_ver_width, box_ver_height);
		else
			batch.draw(Assets.beerBox, beer_x, beer_y, box_ver_width, box_ver_height);
		
		if (selected == Selected.SPICE)
			batch.draw(Assets.spiceBox,	spice_x, spice_y, spice_width, spice_height);
		else 
			batch.draw(Assets.spiceBox,	spice_x, spice_y, spice_width, spice_height);

		batch.setColor(original);
	}
	
	// specially for tutorial
	public void tutDrawBeefBox(SpriteBatch batch, boolean open) {
		int box_hor_width 	= KitchenScreen.convertWidth(BOX_HOR_WIDTH);
		int box_hor_height 	= KitchenScreen.convertHeight(BOX_HOR_HEIGHT);
	
		int beef_x 		= KitchenScreen.convertXWithBuffer(BEEF_X);
		int bottom_y 	= KitchenScreen.convertYWithBuffer(BOTTOM_Y);
	
		if (open) {
			batch.draw(Assets.beefBoxOpen, 	beef_x, bottom_y, box_hor_width, box_hor_height);			
		}
		else {
			batch.draw(Assets.beefBox, 	beef_x, bottom_y, box_hor_width, box_hor_height);
		}
		if (this.selected == Selected.BEEF && !open) {
			TutorialScreen ts = (TutorialScreen) ks;
			ts.transitionToNext();
		}
	}

	public void tutDrawChickenBox(SpriteBatch batch, boolean open) {
		int box_hor_width 	= KitchenScreen.convertWidth(BOX_HOR_WIDTH);
		int box_hor_height 	= KitchenScreen.convertHeight(BOX_HOR_HEIGHT);
		int chicken_x 	= KitchenScreen.convertXWithBuffer(CHICKEN_X);
		int bottom_y 	= KitchenScreen.convertYWithBuffer(BOTTOM_Y);
		
		if (open) {
			batch.draw(Assets.chickenBoxOpen, chicken_x, bottom_y, box_hor_width, box_hor_height);
		}
		else {
			batch.draw(Assets.chickenBox, chicken_x, bottom_y, box_hor_width, box_hor_height);
		}
		if (this.selected == Selected.CHICKEN && !open) {
			TutorialScreen ts = (TutorialScreen) ks;
			ts.transitionToNext();
		}
	}
	
	public void tutDrawLambBox(SpriteBatch batch, boolean open) {
		int box_hor_width 	= KitchenScreen.convertWidth(BOX_HOR_WIDTH);
		int box_hor_height 	= KitchenScreen.convertHeight(BOX_HOR_HEIGHT);
		int lamb_x 		= KitchenScreen.convertXWithBuffer(LAMB_X);
		int bottom_y 	= KitchenScreen.convertYWithBuffer(BOTTOM_Y);
		
		if (open) {
			batch.draw(Assets.lambBoxOpen, lamb_x, bottom_y, box_hor_width, box_hor_height);
		}
		else {
			batch.draw(Assets.lambBox, lamb_x, bottom_y, box_hor_width, box_hor_height);
		}
		if (this.selected == Selected.LAMB && !open) {
			TutorialScreen ts = (TutorialScreen) ks;
			ts.transitionToNext();
		}
	}
	
	public void tutDrawSpiceBox(SpriteBatch batch, boolean open) {
		int spice_width 	= KitchenScreen.convertWidth(SPICE_WIDTH);
		int spice_height 	= KitchenScreen.convertHeight(SPICE_HEIGHT);
		int spice_x 	= KitchenScreen.convertXWithBuffer(SPICE_X);
		int spice_y 	= KitchenScreen.convertYWithBuffer(SPICE_Y);
		
		if (open) {
			batch.draw(Assets.spiceBox,	spice_x, spice_y, spice_width, spice_height);
		}
		else {
			batch.draw(Assets.spiceBox,	spice_x, spice_y, spice_width, spice_height);
		}
		if (this.selected == Selected.SPICE && !open) {
			TutorialScreen ts = (TutorialScreen) ks;
			ts.transitionToNext();
		}
	}
	
	// removes meat at given index when touch is released
	public void removeOnRelease(int index) {
		removeOnRelease = index;
	}

	public void select(Selected newSelected) {
		this.selected = newSelected;
		this.selectedSet.clear();
		//		this.boxSelectedNotHeld = false;
		this.meatSelectedNotHeld = false;
	}

	public void select(int grillIndex) {
		if (grillIndex < 0 || grillIndex > meat.length) throw new java.lang.ArrayIndexOutOfBoundsException();
		this.selected = Selected.NONE;
		this.selectedSet.add(meat[grillIndex]);
		//		this.boxSelectedNotHeld = false;
		this.meatSelectedNotHeld = false;
	}

	// used for calculating "mouseOver"
	public void holdInput(int x, int y) {
		int unit_x = KitchenScreen.getUnitX(x);
		int unit_y = KitchenScreen.getUnitY(y);

		mousedOverTrash = false;
		if (meatSelected() && mousedOverTrash(x, unit_y))
			mousedOverTrash = true;

		mousedOver = -1;
		if (onGrill(unit_x, unit_y)) 
			mousedOver = getGrillIndex(x, y);

		// if moused over grill and spice is selected then spice whatever is there
		if (mousedOver != -1 && meat[mousedOver] != null) {
			if (this.selected == Selected.SPICE) {
				dropSpice();
			}
			else if (this.selected == Selected.NONE || ((this.newMeatSelected() || this.selected == Selected.BEER) && !placedWhileHeld)) {
				if (!selectedSet.contains(meat[mousedOver])) {
					this.selected = Selected.NONE;
					selectedSet.add(meat[mousedOver]);
				}
			}
		}

		// drop fresh meat on grill
		if (mousedOver != -1 && this.newMeatSelected()) {
			if (meat[mousedOver] == null && open(mousedOver)) {
				ks.dropMeatOnGrill(toMeat(selected));
				this.placedWhileHeld = true;
			}
		}

		// only if a meat from the grill is selected
		if ((meatSelected() || selected == Selected.BEER) && ks.cm != null)
			ks.cm.updateMousedOver(x, y);
	}

	public void release(int x, int y) {
		if (!this.active || this.disableTouch) return;

		boolean gaveBeerToCustomer = false;
		// drop stuff onto grill 
		if (mousedOver()) {
			if (newMeatSelected()) {
				//				// if already meat there, select it, but only if just touched
				//				if (this.meat[this.mousedOver] != null && !placedWhileHeld) {
				//					this.select(mousedOver);
				//				}


				//				if (open(mousedOver)) {
				////					if (!boxSelectedNotHeld) select(Selected.NONE);
				//					if (!boxSelectedNotHeld) {
				//						System.out.println("BAD PLACE");
				//						select(Selected.NONE);
				//					}
				//				}
				//				else {
				//					System.out.println("deselecting");
				//					selected = Selected.NONE;
				//				}
			}
			else if (meatSelected()) {
				// remove it from selected if clicking it twice
				if (!open(mousedOver)) {
					//					if (selectedSet.contains(meat[mousedOver]) && meatSelectedNotHeld) 
					//						selectedSet.remove(meat[mousedOver]);
				}
				else if (open(mousedOver) || 
						(meat[mousedOver] == selectedSet.get(0) && !meat[mousedOver].chicken())) {
					move();
					select(Selected.NONE);
				}
			}
			else if (selected == Selected.SPICE) {
				if (!open(mousedOver)) {
					if (!meat[mousedOver].spiced){
						dropSpice();
					}
					else if (selected != Selected.SPICE) {
						select(mousedOver); // unselect if double-spice
					}
					select(Selected.NONE);
				}
			}
		}
		// drop stuff into trash
		else if (mousedOverTrash && meatSelected()) {
//			trashSelected();
		}
		// drop meat on customers
		else if (meatSelected() && ks.cm.mousedOver != null && ks.cm.mousedOver.order != null) {
			ks.earnMoney(ks.cm.mousedOver.giveMeat(selectedSet));
			removeSelected(); // deletes selected meat from grill;
			select(Selected.NONE);
		}
		// drop beer on customers
		else if (selected == Selected.BEER && ks.cm.mousedOver != null && ks.cm.mousedOver.order != null) {
			if (ks.canAfford(KitchenScreen.BEER_BUY_PRICE)) {
				float moneyEarned = ks.cm.mousedOver.giveBeer(); 
			
				// customer doesn't want beer
				if (moneyEarned == 0) {
					return;
				}
				
				ks.spendMoney(KitchenScreen.BEER_BUY_PRICE);
				ks.earnMoney(moneyEarned);

				gaveBeerToCustomer = true;
			}
		}
		// trash meat if dropping it onto empty space and you were holding down
		else if (meatSelected() && !meatSelectedNotHeld) {
//			trashSelected();
		}

		// remove on release
		if (removeOnRelease >= 0) {
			selectedSet.remove(meat[removeOnRelease]);
			removeOnRelease = -1;
		}

		//		select(Selected.NONE);

		// selectButNotHold
		int unit_x = KitchenScreen.getUnitX(x);
		int unit_y = KitchenScreen.getUnitY(y);
		Selected box = touchBox(unit_x, unit_y);

		// touched and released same box
		if (box == Selected.NONE) {
			if (onGrill(unit_x, unit_y)) {
				if (selectedSet.contains(meat[getGrillIndex(x, y)])) 
					this.meatSelectedNotHeld = true;
			}
			else {
				if (!gaveBeerToCustomer) {
					select(Selected.NONE);
				}
			}
		}
		else {
			if (isMeat(box)) {
				//				this.boxSelectedNotHeld = true;
			}
		}
		placedWhileHeld = false;
	}

	// converts from selected to Meat.Type
	public Meat.Type toMeat(Selected selected) {
		if (selected == Selected.CHICKEN) return Meat.Type.CHICKEN;
		if (selected == Selected.BEEF) return Meat.Type.BEEF;
		if (selected == Selected.LAMB) return Meat.Type.LAMB;
		return null;
	}

	/** 
	 * Given touched units, return the box that was touched, or none if no box was touched
	 */
	private Selected touchBox(int unit_x, int unit_y) {
		if (unit_x >= CHICKEN_X && unit_x < CHICKEN_X + BOX_HOR_WIDTH) {
			if (unit_y >= BOTTOM_Y && unit_y < BOTTOM_Y + BOX_HOR_HEIGHT){
				return Selected.CHICKEN;
			}
		}
		if (unit_x >= BEEF_X && unit_x < BEEF_X + BOX_HOR_WIDTH) {
			if (unit_y >= BOTTOM_Y && unit_y < BOTTOM_Y + BOX_HOR_HEIGHT){
				return Selected.BEEF;
			}
		}
		if (unit_x >= LAMB_X && unit_x < LAMB_X + BOX_HOR_WIDTH) {
			if (unit_y >= BOTTOM_Y && unit_y < BOTTOM_Y + BOX_HOR_HEIGHT){
				return Selected.LAMB;
			}
		}

		if (unit_x >= BEER_X && unit_x < BEER_X + BEER_WIDTH) {
			if (unit_y >= BEER_Y && unit_y < BEER_Y + BEER_HEIGHT){
				return Selected.BEER;
			}
		}

		if (unit_x >= SPICE_X && unit_x < SPICE_X + SPICE_WIDTH) {
			if (unit_y >= SPICE_Y && unit_y < SPICE_Y + SPICE_HEIGHT){
				return Selected.SPICE;
			}
		}

		return Selected.NONE;
	}

	// not regular x, unit y (for off-screen stuff) 
	public boolean mousedOverTrash(int x, int unit_y) {
		if (x < 0 || x > ChuanrC.width) {
//			if (unit_y >= TRASH_Y && unit_y < TRASH_Y + TRASH_HEIGHT){
				return true;
//			}
		}
		return false;
	}

	public boolean isMeat(Selected selected) {
		return (selected == Selected.CHICKEN || selected == Selected.BEEF || selected == Selected.LAMB);
	}

	private int getGrillSize() {
		return size;
	}

	public boolean onGrill(int unit_x, int unit_y) {
		if (unit_x >= GRILL_X + this.xOffset && unit_x < GRILL_X + GRILL_WIDTH - xOffset) {
			if (unit_y >= GRILL_Y + this.yOffset && unit_y < GRILL_Y + GRILL_HEIGHT - yOffset) {
				return true;
			}
		}
		return false;
	}

	// can add vertical grills later
	public int getGrillIndex(int x, int y) {
		int distanceFromLeft = x - (GRILL_X + xOffset) * KitchenScreen.UNIT_WIDTH; 		
		return (int) (distanceFromLeft * CHUANR_PER_PIECE / (KitchenScreen.UNIT_WIDTH * GRILL_PIECE_WIDTH));
	}

	//	public void highlightIndex(SpriteBatch batch, int i) {
	//		int x = (int) (i * (KitchenScreen.UNIT_WIDTH * GRILL_PIECE_WIDTH / CHUANR_PER_PIECE) + KitchenScreen.UNIT_WIDTH * (xOffset + GRILL_X));
	//		int y = KitchenScreen.UNIT_HEIGHT * (GRILL_Y + yOffset);
	//		
	//		batch.draw(Assets.white, x, y,  (int) (GRILL_PIECE_WIDTH * KitchenScreen.UNIT_WIDTH / 
	//				CHUANR_PER_PIECE), KitchenScreen.UNIT_HEIGHT * GRILL_PIECE_HEIGHT);
	//	}

	private int getXForIndex(int i) {
		return  (int) (i * (KitchenScreen.UNIT_WIDTH * GRILL_PIECE_WIDTH / CHUANR_PER_PIECE) + KitchenScreen.UNIT_WIDTH * (xOffset + GRILL_X));
	}

	private int getYForIndex(int i) {
		return  (int) (KitchenScreen.UNIT_HEIGHT * (GRILL_Y + yOffset));
	}

	public void drawMeat(SpriteBatch batch) {
		for (Meat m : meat) {
			if (m == null) continue;
			int x = getXForIndex(m.index1);
			int y = getYForIndex(m.index1);
			m.draw(batch, x, y);
		}
		//		System.out.println(meatBoxSelected);


		// if placing meat on grill or moving meat
		//		if (this.mousedOver() ) {

		//			batch.setColor(1, 1, 1, .6f);
		//			TextureRegion toDraw = null;
		//			int widthFactor = 1;
		//			if ((selected == Selected.CHICKEN) && this.canFit(Meat.Type.CHICKEN)) {
		//				toDraw = Assets.chuanrChickenRaw;
		//				widthFactor = 2;
		//			}
		//			if (selected == Selected.BEEF && this.canFit(Meat.Type.BEEF))
		//				toDraw = Assets.chuanrBeefRaw;
		//			if (selected == Selected.LAMB && this.canFit(Meat.Type.LAMB))
		//				toDraw = Assets.chuanrLambRaw;			
		//			
		//			if (toDraw != null)
		//				batch.draw(toDraw, getXForIndex(mousedOver), getYForIndex(mousedOver),  (int) (GRILL_PIECE_WIDTH * widthFactor * KitchenScreen.UNIT_WIDTH / 
		//					CHUANR_PER_PIECE), KitchenScreen.UNIT_HEIGHT * GRILL_PIECE_HEIGHT);
		//		}
		if (this.mousedOver() && this.meatSelected() && !this.selectedSet.contains(meat[mousedOver])) {
			// draw where all the meat will go if dropped!
			// for now, just draw the first one
			batch.setColor(1, 1, 1, .6f);
			TextureRegion toDraw = null;
			int widthFactor = 1;
			if (this.selectedSet.get(0).type == Meat.Type.CHICKEN && this.canFit(Meat.Type.CHICKEN)) {
				toDraw = Assets.chuanrChickenRaw;
				widthFactor = 2;
			}
			if (this.selectedSet.get(0).type == Meat.Type.BEEF  && this.canFit(Meat.Type.BEEF))
				toDraw = Assets.chuanrBeefRaw;
			if (this.selectedSet.get(0).type == Meat.Type.LAMB  && this.canFit(Meat.Type.LAMB))
				toDraw = Assets.chuanrLambRaw;			

			if (toDraw != null)
				batch.draw(toDraw, getXForIndex(mousedOver), getYForIndex(mousedOver),  (int) (GRILL_PIECE_WIDTH * widthFactor * KitchenScreen.UNIT_WIDTH / 
						CHUANR_PER_PIECE), KitchenScreen.UNIT_HEIGHT * GRILL_PIECE_HEIGHT);
		}
		if (this.meatSelected()) {
			for (Meat m : selectedSet) {
				Color myColor = Color.WHITE;
				myColor.a = .2f;
				batch.setColor(myColor);
				batch.draw(Assets.white, getXForIndex(m.index1), getYForIndex(m.index1),  (int) (GRILL_PIECE_WIDTH * KitchenScreen.UNIT_WIDTH / 
						CHUANR_PER_PIECE), KitchenScreen.UNIT_HEIGHT * GRILL_PIECE_HEIGHT);
				if (m.chicken()) 
					batch.draw(Assets.white, getXForIndex(m.index2), getYForIndex(m.index2),  (int) (GRILL_PIECE_WIDTH * KitchenScreen.UNIT_WIDTH / 
							CHUANR_PER_PIECE), KitchenScreen.UNIT_HEIGHT * GRILL_PIECE_HEIGHT);
			}
			batch.setColor(1, 1, 1, 1);
		}
	}

	// drops meat at mousedOver index
	public Meat dropMeat(Meat.Type type) {
		Meat toDrop = new Meat(type);

		if (!canFit(toDrop)) return null;
		meat[mousedOver] = toDrop;
		meat[mousedOver].setIndex(mousedOver);
		if (type == Meat.Type.CHICKEN) {
			meat[mousedOver + 1] = meat[mousedOver];
		}

		return toDrop;
		//		this.boxSelectedNotHeld = false;
	}

	// removes selected meat from grill
	public void removeSelected() {
		trashSelected(); // for now
	}

	private boolean canFitAt(int index, Meat piece) {
		if (piece.type != Meat.Type.CHICKEN)
			return meat[index] == null;
		else {
			return index + 1 < meat.length && 
					(open(index) || meat[index] == piece)
					&& (open(index + 1) || meat[index + 1] == piece);
		}
	}

	private boolean canFitAt(int index, Meat.Type type) {
		if (type != Meat.Type.CHICKEN)
			return meat[index] == null;
		else {
			return index + 1 < meat.length && (open(index)) && open(index + 1);
		}
	}

	// will this type of meat fit at mousedOver?
	private boolean canFit(Meat piece) {
		return canFitAt(mousedOver, piece);
	}

	// will this type of meat fit at mousedOver?
	private boolean canFit(Meat.Type type) {
		return canFitAt(mousedOver, type);
	}

	// drops spice at mousedOver index
	public void dropSpice() {
		if (meat[mousedOver] == null) throw new java.lang.IllegalArgumentException();
		meat[mousedOver].spice();
	}

	// drops selected meat into trash
	public void trashSelected() {
		if (!meatSelected()) throw new java.lang.IllegalArgumentException();

		for (Meat m : selectedSet) {
			meat[m.index1] = null;
			if (m.type == Meat.Type.CHICKEN) meat[m.index2] = null;
		}
	}

	// moves meat from selected to mousedOver
	public void move() {
		if (!mousedOver()) throw new java.lang.IllegalArgumentException();

		int grillIndex = mousedOver;
		int selectedIndex = 0;
		while (selectedIndex < selectedSet.size() && grillIndex < this.meat.length) {
			Meat current = selectedSet.get(selectedIndex);
			if (current == null) {
				selectedIndex++;
				continue;
			}
			if (this.canFitAt(grillIndex, current)) {
				moveFrom(current.index1, grillIndex);
				selectedIndex++;
			}
			grillIndex++;
		}

		selectedSet.clear();
	}

	public void moveFrom(int origIndex, int newIndex) {
		Meat toMove = meat[origIndex];
		if (toMove == null) return;
		// we null this out early because we don't want to null it at the end,
		// when it might actually have the moved meat
		meat[origIndex] = null;

		if (toMove.chicken()) {
			meat[origIndex + 1] = null;
		}

		meat[newIndex] = toMove;
		meat[newIndex].setIndex(newIndex);

		if (toMove.chicken()) {
			meat[newIndex + 1] = meat[newIndex];
		}
	}

	public boolean open(int index) {
		return (meat[index] == null);
	}

	public boolean newMeatSelected() {
		return this.selected == Selected.BEEF || this.selected == Selected.CHICKEN || this.selected == Selected.LAMB;
	}

	public boolean meatSelected() {
		return this.selectedSet.size() > 0;
	}

	public boolean mousedOver() {
		return this.mousedOver >= 0;
	}
}
