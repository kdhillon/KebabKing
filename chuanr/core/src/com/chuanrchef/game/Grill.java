package com.chuanrchef.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.chuanrchef.game.Purchases.Vanity.VanityGrillStand;

// contains information for the grill, the "trash", spice box, and ice chests
//
// The following is how the control scheme for meat should work:
//  	Tapping a chest opens the chest. 
//		Trying to drag the chest does nothing. Chest opens on release.
//		Tapping an open grill spot places a meat at that spot. 
//		Tapping anywhere else closes the chest.
//
//		Tapping a meat on the grill selects that meat. You can select multiple meats. (Dragging?)
//		Tapping a customer gives them the meat you selected.
//		Tapping the void deselects the meat.
//
//		Dragging the meat to the void trashes the meat.
//		Dragging the spice brush onto the meat spices it.

public class Grill {
	static final boolean DISABLE_DRAG_TO_PLACE = true;
	
	static final int GRILL_X = 0;
	static final float GRILL_Y = 2f;
	static final int GRILL_WIDTH = 12;
	static final float GRILL_HEIGHT = 3.5f;

	static final int GRILL_PIECE_WIDTH = 1;
	static final float GRILL_PIECE_HEIGHT = GRILL_HEIGHT;
	static final double CHUANR_PER_PIECE = 1;
	static final float CHUANR_HEIGHT = 2.5f;
	
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
	static final int SPICE_Y = 3;
//	static final int TRASH_X = 0;
//	static final int TRASH_Y = 2;
	
	static final float STAND_X = .25f;
	static final float STAND_Y = .11f;
	static final float STAND_WIDTH = .5f;
	static final float STAND_HEIGHT = .1f;
	
	private int draw_width;
	private int draw_height;

	private int draw_x;
	private int draw_y;

	private float time;
	
	private int grillLeftX;
	private final int grillLeftY = 0;
	private int grillRightX;

	public enum Selected {
		NONE, CHICKEN, BEEF, LAMB, SPICE, BEER
	}

	Selected selected;
	
	boolean tutorialMode;

	/** if false, grill cannot be interacted with */
	boolean active; 
	boolean disableTouch;
	
	boolean hoverTrash;
	
	Profile profile;
	KitchenScreen ks; // only non-null when active
	int size; 
	Meat[] meat;
	int mousedOver; // index of moused-over slot on grill?
	//	boolean meatBoxSelected; // for use when highlighting grill

//	boolean mousedOverTrash = false;
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
		
		// is this wrong? we should center the grill?
		// So the left side of the grill should be half the screen size - half the grill size
		grillLeftX = (GRILL_WIDTH)/2 - (size + SPICE_WIDTH)/2;
		
		grillRightX = grillLeftX + size; 
				
		draw_width = GRILL_PIECE_WIDTH * KitchenScreen.UNIT_WIDTH;
		draw_height = (int) ( GRILL_PIECE_HEIGHT * KitchenScreen.UNIT_HEIGHT);

		draw_x = (grillLeftX + GRILL_X) * KitchenScreen.UNIT_WIDTH;
		draw_y = (int) ((grillLeftY + GRILL_Y) * KitchenScreen.UNIT_HEIGHT);
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
		
		if (ks != null && TrashPile.DRAW_TRASH_PILE) {
			drawTrashPile(batch);
		}
		
		// draw grill stand
		drawStand(batch);
		
		drawBoxes(batch);

		Color original = batch.getColor();
		if (!this.active) {
			batch.setColor(Color.GRAY);
		}
		
		TextureRegion fire = Assets.grillFire.getKeyFrame(time);
		if (!active) {
			fire = Assets.gray;
		}

		int xCount = grillLeftX;
		
		// draw left
//		batch.draw(fire, draw_x, draw_y, draw_width, draw_height); 
//		batch.draw(Assets.grillCoals, draw_x, draw_y, draw_width, draw_height); 
		batch.draw(Assets.grillLeft, draw_x, draw_y, draw_width * 2, draw_height); 
		xCount++;
		xCount++;
		
		// draw middle
		while (xCount < GRILL_WIDTH - grillLeftX - 4) {
			draw_x = (xCount + GRILL_X) * KitchenScreen.UNIT_WIDTH;
//			batch.draw(fire, draw_x, draw_y, draw_width, draw_height); 
//			batch.draw(Assets.grillCoals, draw_x, draw_y, draw_width, draw_height); 
			batch.draw(Assets.grillMid, draw_x, draw_y, draw_width, draw_height);
			xCount++;
		}

		// draw right		 
		draw_x = (xCount + GRILL_X) * KitchenScreen.UNIT_WIDTH;
//		batch.draw(fire, draw_x, draw_y, draw_width, draw_height); 
//		batch.draw(Assets.grillCoals, draw_x, draw_y, draw_width, draw_height); 
		batch.draw(Assets.grillRight, draw_x, draw_y, draw_width*2, draw_height); 

		if (active)
			drawMeat(batch);
		else batch.setColor(original);
		
		// draw spice paintbrush
		if (this.selected == Selected.SPICE && Gdx.input.isTouched()) {
			drawPaintbrush(batch);
		}
		
		// draw trash icon if throwing away
		if (trashHover()) {
			hoverTrash = true;
			drawTrashIcon(batch);
		}
		else hoverTrash = false;
	}
	
	public boolean trashHover() {
		if (ks != null && ks.cm != null && ks.cm.mousedOver != null) return false;
		return this.meatSelected() && !meatSelectedNotHeld && !this.onGrillAbsolute(Gdx.input.getX(), Gdx.input.getY());
	}
	
	public void drawPaintbrush(SpriteBatch batch) {
		float brushWidth = 80;
		float brushHeight = 120;
		
		batch.draw(Assets.paintBrush, Gdx.input.getX() - brushWidth/2, ChuanrC.getHeight() - Gdx.input.getY() - brushHeight/8, brushWidth, brushHeight);
	}
	
	public void drawTrashIcon(SpriteBatch batch) {
		float trashWidth = 40;
		float trashHeight = 40;
		
		batch.draw(Assets.trashIcon, Gdx.input.getX() - trashWidth/2, ChuanrC.getHeight() - Gdx.input.getY() - trashHeight/8, trashWidth, trashHeight);
	}

	public void tutDrawLeftGrill(SpriteBatch batch) {
		// draw left
		draw_x = (0 + GRILL_X + grillLeftX) * KitchenScreen.UNIT_WIDTH;
		batch.draw(Assets.grillFire.getKeyFrame(0), draw_x, draw_y, draw_width, draw_height); 
		batch.draw(Assets.grillCoals, draw_x, draw_y, draw_width, draw_height); 
		batch.draw(Assets.grillLeft, draw_x, draw_y, draw_width, draw_height); 
	}

	public void tutDrawGrill(int index, SpriteBatch batch) {
		// draw middle
		draw_x = (index + GRILL_X + grillLeftX) * KitchenScreen.UNIT_WIDTH;
		batch.draw(Assets.grillFire.getKeyFrame(0), draw_x, draw_y, draw_width, draw_height); 
		batch.draw(Assets.grillCoals, draw_x, draw_y, draw_width, draw_height); 
		batch.draw(Assets.grillMid, draw_x, draw_y, draw_width, draw_height);
	}
	
	public void tutDrawRightGrill(SpriteBatch batch) {
		draw_x = (5 + GRILL_X + grillLeftX) * KitchenScreen.UNIT_WIDTH;
		batch.draw(Assets.grillFire.getKeyFrame(0), draw_x, draw_y, draw_width, draw_height); 
		batch.draw(Assets.grillCoals, draw_x, draw_y, draw_width, draw_height); 
		batch.draw(Assets.grillRight, draw_x, draw_y, draw_width, draw_height); 
	}

	public void touchInput(int x, int y) {		
		if (!active || disableTouch) return;

		// KitchenScreen.convert to unit coordinates
		int unit_x = KitchenScreen.getUnitX(x);
		int unit_y = KitchenScreen.getUnitY(y);

		// select spice box for dragging
		Selected box = touchBox(unit_x, unit_y); 
		if (box == Selected.SPICE) select(box);
		
		// if not on grill
//		Selected box = touchBox(unit_x, unit_y);
//		if (box != Selected.NONE && !ks.paused) {
//			select(box);
//			return;
//		}

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
				else selectMeat(index);
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

	public void drawTrashPile(SpriteBatch batch) {
		ks.tp.draw(batch);
	}
	
	public void drawStand(SpriteBatch batch) {
		VanityGrillStand stand = (VanityGrillStand) profile.inventory.grillStand.getCurrentSelected();
		if (stand.getTexture() != null) {
			batch.draw(stand.getTexture(), ChuanrC.getGlobalX(STAND_X), ChuanrC.getGlobalY(STAND_Y), 
					ChuanrC.getGlobalX(STAND_WIDTH), ChuanrC.getGlobalY(STAND_HEIGHT));
		}
	}
	
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
		
		// draw spice directly to right of grill
		int spice_x 	= KitchenScreen.convertXWithBuffer(grillRightX);
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
		int spice_x 	= KitchenScreen.convertXWithBuffer(grillRightX);
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

	public void selectMeat(int grillIndex) {
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

//		mousedOverTrash = false;
//		if (meatSelected() && mousedOverTrash(x, unit_y))
//			mousedOverTrash = true;

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

		// draw ghost meat under mouse when dragging meat off grill

		// only if a meat from the grill is selected
		if ((meatSelected() || selected == Selected.BEER) && ks.cm != null)
			ks.cm.updateMousedOver(x, y);
	}

	public void release(int x, int y) {
		if (!this.active || this.disableTouch) return;

		if (hoverTrash) {
			this.trashSelected();
		}
		
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
						selectMeat(mousedOver); // unselect if double-spice
					}
					select(Selected.NONE);
				}
			}
			// drop fresh meat on grill
			if (mousedOver != -1 && this.newMeatSelected()) {
				if (meat[mousedOver] == null && open(mousedOver)) {
					ks.dropMeatOnGrill(toMeat(selected));
					this.placedWhileHeld = true;
					
					if (DISABLE_DRAG_TO_PLACE) {
						removeOnRelease(this.mousedOver);
//						this.select(Selected.NONE);
					}
				}
			}
		}
		// drop stuff into trash
//		else if (mousedOverTrash && meatSelected()) {
////			trashSelected();
//		}
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

		// touched and released same meat
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
			if (box != Selected.SPICE) {
				select(box);
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

		if (unit_x >= grillRightX && unit_x < grillRightX + SPICE_WIDTH) {
			if (unit_y >= SPICE_Y && unit_y < SPICE_Y + SPICE_HEIGHT){
				return Selected.SPICE;
			}
		}

		return Selected.NONE;
	}

	// not regular x, unit y (for off-screen stuff) 
//	public boolean mousedOverTrash(int x, int unit_y) {
//		if (x < 0 || x > ChuanrC.width) {
////			if (unit_y >= TRASH_Y && unit_y < TRASH_Y + TRASH_HEIGHT){
//				return true;
////			}
//		}
//		return false;
//	}

	public boolean isMeat(Selected selected) {
		return (selected == Selected.CHICKEN || selected == Selected.BEEF || selected == Selected.LAMB);
	}

	private int getGrillSize() {
		return size;
	}

	public boolean onGrillAbsolute(int x, int y) {
		return onGrill(KitchenScreen.getUnitX(x), KitchenScreen.getUnitY(y));
	}
	
	public boolean onGrill(int unit_x, int unit_y) {
		if (unit_x >= GRILL_X + this.grillLeftX && unit_x < GRILL_X + grillRightX) {
			if (unit_y >= GRILL_Y + this.grillLeftY && unit_y < GRILL_Y + GRILL_HEIGHT - grillLeftY) {
				return true;
			}
		}
		return false;
	}

	// can add vertical grills later
	public int getGrillIndex(int x, int y) {
		int distanceFromLeft = x - (GRILL_X + grillLeftX) * KitchenScreen.UNIT_WIDTH; 		
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
		return  (int) (i * (KitchenScreen.UNIT_WIDTH * GRILL_PIECE_WIDTH / CHUANR_PER_PIECE) + KitchenScreen.UNIT_WIDTH * (grillLeftX + GRILL_X));
	}

	private int getYForIndex(int i) {
		return  (int) (KitchenScreen.UNIT_HEIGHT * (GRILL_Y + grillLeftY));
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
			
			// TODO fix this, because we're dealing with placing new meat.
			
			// draw where all the meat will go if dropped!
			// for now, just draw the first one
//			batch.setColor(1, 1, 1, .6f);
//			TextureRegion toDraw = null;
//			int widthFactor = 1;
//			if (this.selectedSet.get(0).type == Meat.Type.CHICKEN && this.canFit(Meat.Type.CHICKEN)) {
//				toDraw = Assets.chuanrChickenRaw;
//				widthFactor = 2;
//			}
//			if (this.selectedSet.get(0).type == Meat.Type.BEEF  && this.canFit(Meat.Type.BEEF))
//				toDraw = Assets.chuanrBeefRaw;
//			if (this.selectedSet.get(0).type == Meat.Type.LAMB  && this.canFit(Meat.Type.LAMB))
//				toDraw = Assets.chuanrLambRaw;			
//
//			if (toDraw != null)
//				batch.draw(toDraw, getXForIndex(mousedOver), getYForIndex(mousedOver),  (int) (GRILL_PIECE_WIDTH * widthFactor * KitchenScreen.UNIT_WIDTH / 
//						CHUANR_PER_PIECE), KitchenScreen.UNIT_HEIGHT * GRILL_PIECE_HEIGHT);
		}
		if (this.meatSelected()) {
			for (Meat m : selectedSet) {
				Color myColor = Color.WHITE;
				myColor.a = .2f;
				batch.setColor(myColor);
				batch.draw(Assets.white, getXForIndex(m.index1), getYForIndex(m.index1) + KitchenScreen.UNIT_HEIGHT * (GRILL_PIECE_HEIGHT - CHUANR_HEIGHT),  (int) (GRILL_PIECE_WIDTH * KitchenScreen.UNIT_WIDTH / 
						CHUANR_PER_PIECE), KitchenScreen.UNIT_HEIGHT * CHUANR_HEIGHT);
				if (m.chicken()) 
					batch.draw(Assets.white, getXForIndex(m.index2), getYForIndex(m.index2) + KitchenScreen.UNIT_HEIGHT * (GRILL_PIECE_HEIGHT - CHUANR_HEIGHT),  (int) (GRILL_PIECE_WIDTH * KitchenScreen.UNIT_WIDTH / 
							CHUANR_PER_PIECE), KitchenScreen.UNIT_HEIGHT * CHUANR_HEIGHT);
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

	// trashes meat from grill
	public void trashSelected() {
		ks.kebabsTrashed += removeSelected(); 
		System.out.println("Kebabs trashed: " + ks.kebabsTrashed);		
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

	// returns number of kebabs trashed
	public int removeSelected() {
		if (!meatSelected()) throw new java.lang.IllegalArgumentException();
		int count = 0;
		for (Meat m : selectedSet) {
			meat[m.index1] = null;
			if (m.type == Meat.Type.CHICKEN) meat[m.index2] = null;
			count++;
		}
		return count;
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

	// Is meat from grill selected?
	public boolean meatSelected() {
		return this.selectedSet.size() > 0;
	}

	public boolean mousedOver() {
		return this.mousedOver >= 0;
	}
}
