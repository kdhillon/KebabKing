package com.kebabking.game;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kebabking.game.Purchases.GrillStand;
import com.kebabking.game.Purchases.GrillType;
import com.kebabking.game.Purchases.MeatTypes;

// contains information for the grill, the "trash", spice box, and ice chests
//
// The following is how the control scheme for meat should work:
// 		BASIC (SWIPE) CONTROL SCHEME:
//		Touching down on a chest drags meat of that type (chest is opened during drag). Mousing over grill area "ghosts" meat where it would be placed.
//		Releasing places meat at that spot.
//		Tapping chest does nothing
//		Beer must be dragged onto customers.

// 		ADVANCED (TAP) CONTROL SCHEME:
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
	public static int kebabsTrashedThisSession = 0;
	public static int kebabsServedThisSession = 0;

	static final float TIME_TO_TRASH = 0.5f;
	static final boolean ADVANCED_CONTROLS = false;

	//	static final boolean DISABLE_DRAG_TO_PLACE = false;

	static final int GRILL_X = 0;
	static final float GRILL_Y = 2f;
	static final int GRILL_WIDTH = 12;
	static final float GRILL_HEIGHT = 3.5f;

	static final float GRILL_GRATE_PERCENT = 0.67f;

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
	static final int FIRST_X = 0;
	static final int SECOND_X = 3;
	static final int THIRD_X = 6;
	static final int BOTTOM_Y = 0;
	static final int BEER_X = 9;
	static final int BEER_Y = 0;
	static final int SPICE_Y = 3;

	static final boolean DRAW_GRAY_ON_TRASH = true;
	static final float TRASH_GRAY_ALPHA = MainMenuScreen.INIT_BG_ALPHA;
	//	static final int TRASH_X = 0;
	//	static final int TRASH_Y = 2;

	//	static final float STAND_WIDTH;
	//	static final float STAND_X = .25f;
	static final float STAND_Y = .05f;
	static final float STAND_WIDTH = .5f;
	static final float STAND_HEIGHT = .18f;

	static final float FIRE_PAD_X = 0.01f;
	static final float FIRE_PAD_TOP = 0.005f;

	static final float BRUSH_MAX_ROT = 30f;
	static final float BRUSH_MIN_ROT = -30f;
	static final float BRUSH_ROT_RATE = 90f;

	private int draw_width;
	private int draw_height;

	private int draw_x;
	private int draw_y;

	private float trashWidth;
	private float trashHeight;	

	private float time;

	private int grillLeftX;
	private final int grillLeftY = 0;
	private int grillRightX;

	private int indexToPlaceMeatAt = -1;

	// use an array to look up which actual meat that is.
	public enum Selected {
		NONE, FIRST, SECOND, THIRD, SPICE, BEER
	}
	MeatTypes.Type[] boxes;

	Selected selected;

	boolean tutorialMode;

	/** if false, grill cannot be interacted with */
	boolean active; 
	boolean disableTouch;

	boolean hoverTrash; // are we hovering over a

	float brushRot;
	boolean brushRotUp; // increaseing or decreasing?

	Profile profile;
	
	KitchenScreen ks; // only non-null when active
	int size; 
	Meat[] meat;
	int mousedOver; // index of moused-over slot on grill?
	int meatOnGrill = 0;
	//	boolean meatBoxSelected; // for use when highlighting grill

	//	boolean mousedOverTrash = false;
	//	boolean boxSelectedNotHeld = false; // try to use this to differentiate between holding and clicking
	boolean meatSelectedNotHeld = false;
	boolean placedWhileHeld = false; // true when meat has been placed during the 'hold'
	int removeOnRelease = -1;
	int justSelected = -1; // for use with remove on release
	boolean firstTouch = true;
	boolean justTappedSpice;

	boolean holding; // is button being held

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

	float trashHoldTime = 0;

	public Grill(Profile profile) {
		//		this.grillType = profile.grillType;
		this.profile = profile;
		this.mousedOver = -1;

		this.selectedSet = new ArrayList<Meat>();
		selected = Selected.NONE;		

		updateSize();

		this.time = 0;
		clearMeat();

		updateBoxes();
	}

	public void reset(KitchenScreen ks) {
		this.ks = ks;
		clearMeat();
		this.activate();
		this.updateSize();
		this.deselectAll();
		this.selected = null;
		SoundManager.stopSizzle();
	}

	public void updateBoxes() {
		boxes = new MeatTypes.Type[3];

		// Order is based on selected order (customizable)
		// another option would be to have it come in fixed order.
		Deque<Integer> boxesDeque = new ArrayDeque<Integer>(profile.inventory.meatTypes.getSelected());
		if (boxesDeque.size() == 1) {
			boxes[0] = MeatTypes.Type.values[boxesDeque.pop()];
		} 
		else if (boxesDeque.size() == 2) {
			boxes[0] = MeatTypes.Type.values[boxesDeque.pop()];
			boxes[1] = MeatTypes.Type.values[boxesDeque.pop()];
		}
		else if (boxesDeque.size() == 3) {
			boxes[0] = MeatTypes.Type.values[boxesDeque.pop()];
			boxes[1] = MeatTypes.Type.values[boxesDeque.pop()];
			boxes[2] = MeatTypes.Type.values[boxesDeque.pop()];	
		}
		else throw new java.lang.AssertionError("Num selected boxes == " + boxesDeque.size());
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

		trashWidth = KebabKing.getGlobalX(0.3f);
		trashHeight = Assets.trashIcon.getRegionHeight() * trashWidth / Assets.trashIcon.getRegionWidth();
	}

	public void activate() {
		this.active = true;	
	}

	public void clearMeat() {
		int length = this.getGrillSize();
		this.meat = new Meat[(int) (length * CHUANR_PER_PIECE)];
		meatOnGrill = 0;
	}

	public void deactivate() {
		System.out.println("deactivating");
		this.selected = Selected.NONE;
		this.ks = null;
		this.active = false;
		hoverTrash = true;
		clearMeat();
		SoundManager.stopSizzle();
	}

	public void act(float delta) {
		if (!active) {
			return;
		}

		//		if (boxesChanged) {
		//			this.updateBoxes();
		//			this.boxesChanged = false;
		//		}

		if (meatOnGrill > 0) {
			SoundManager.playSizzle();
		}
		if (meatOnGrill <= 0) {
			SoundManager.stopSizzle();
		}

		//		if (!this.newMeatSelected()) this.boxSelectedNotHeld = false;

		for (int i = 0; i < meat.length; i++) {
			Meat m = meat[i];
			if (m != null && !(m.type.doubleWidth && m.index2 == i))
				m.act(delta);
		}
		this.time += delta;

		// play sound

	}

	public MeatTypes.Type getType(Selected select) {
		if (select == Selected.FIRST)
			return boxes[0];
		if (select == Selected.SECOND)
			return boxes[1];
		if (select == Selected.THIRD)
			return boxes[2];
		return null;
	}

	public Selected getSelectedForType(MeatTypes.Type type) {
		int selectedIndex = -1;
		for (int i = 0; i < boxes.length; i++) {
			if (boxes[i] == type)
				selectedIndex = i;
		}
		if (selectedIndex == 0) return Selected.FIRST;
		if (selectedIndex == 1) return Selected.SECOND;
		if (selectedIndex == 2) return Selected.THIRD;

		//		if (select == Selected.FIRST)
		//			return boxes[0];
		//		if (select == Selected.SECOND)
		//			return boxes[1];
		//		if (select == Selected.THIRD)
		//			return boxes[2];
		return null;
	}

	public void draw(SpriteBatch batch, float delta) {
		// this is required for now.
		this.updateSize();

		//		if (ks != null && TrashPile.DRAW_TRASH_PILE) {
		//			drawTrashPile(batch);
		//		}

		// draw grill stand (behind boxes
		drawStandBack(batch);

		//		Color original = batch.getColor();
		//		if (!this.active) {
		//			batch.setColor(Color.GRAY);
		//		}

		drawGrill(batch);

		//		drawStandFront(batch);

		drawBoxes(batch);


		if (active)
			drawMeat(batch);
		//		else batch.setColor(original);


		// draw floating meat below finger
		if (!ADVANCED_CONTROLS && meatBoxSelected() && this.indexToPlaceMeatAt < 0) {
			drawFloatingMeat(batch, delta);
		}

		if (selected == Selected.BEER && !ADVANCED_CONTROLS)
			drawFloatingBeer(batch, delta);

		// draw spice paintbrush
		if (this.selected == Selected.SPICE && Gdx.input.isTouched()) {
			drawPaintbrush(batch, delta);
		}

		// draw trash icon if throwing away
		if (trashHover()) {
			trashHoldTime += delta;
			if (trashHoldTime > TIME_TO_TRASH) {
				hoverTrash = true;
				drawTrashIcon(batch);
			}
		}
		else {
			this.trashHoldTime = 0;
			hoverTrash = false;
		}
	}

	public void drawGrill(SpriteBatch batch) {
		TextureRegion fire = getGrillFire().getKeyFrame(time);
		//		if (!active) {
		//			fire = Assets.gray;
		//		}

		int xCount = grillLeftX;

		// draw left
		batch.draw(fire, draw_x + KebabKing.getGlobalX(FIRE_PAD_X), draw_y + (1-GRILL_GRATE_PERCENT) * draw_height, draw_width - KebabKing.getGlobalX(FIRE_PAD_X), draw_height * GRILL_GRATE_PERCENT - KebabKing.getGlobalY(FIRE_PAD_TOP)); 
		// draw two fires
		batch.draw(fire, draw_x + draw_width, draw_y + (1-GRILL_GRATE_PERCENT) * draw_height, draw_width, draw_height * GRILL_GRATE_PERCENT - KebabKing.getGlobalY(FIRE_PAD_TOP)); 
		//		batch.draw(Assets.grillCoals, draw_x, draw_y, draw_width, draw_height); 
		batch.draw(getGrillLeft(), draw_x, draw_y, draw_width * 2, draw_height); 
		xCount++;
		xCount++;

		// draw middle
		while (xCount < GRILL_WIDTH - grillLeftX - 4) {
			draw_x = (xCount + GRILL_X) * KitchenScreen.UNIT_WIDTH;
			batch.draw(fire, draw_x, draw_y + (1-GRILL_GRATE_PERCENT) * draw_height, draw_width, draw_height * GRILL_GRATE_PERCENT - KebabKing.getGlobalY(FIRE_PAD_TOP)); 
			//			batch.draw(Assets.grillCoals, draw_x, draw_y, draw_width, draw_height); 
			batch.draw(getGrillMiddle(), draw_x, draw_y, draw_width, draw_height);
			xCount++;
		}

		// draw right		 
		draw_x = (xCount + GRILL_X) * KitchenScreen.UNIT_WIDTH;
		batch.draw(fire, draw_x, draw_y + (1-GRILL_GRATE_PERCENT) * draw_height, draw_width, draw_height * GRILL_GRATE_PERCENT - KebabKing.getGlobalY(FIRE_PAD_TOP)); 
		batch.draw(fire, draw_x + draw_width, draw_y + (1-GRILL_GRATE_PERCENT) * draw_height, draw_width - KebabKing.getGlobalX(FIRE_PAD_X), draw_height * GRILL_GRATE_PERCENT - KebabKing.getGlobalY(FIRE_PAD_TOP)); 
		//		batch.draw(Assets.grillCoals, draw_x, draw_y, draw_width, draw_height); 
		batch.draw(getGrillRight(), draw_x, draw_y, draw_width*2, draw_height); 
	}
	
	public TextureRegion getGrillLeft() {
		return getGrillType().left;
	}
	public TextureRegion getGrillRight() {
		return getGrillType().right;
	}
	public TextureRegion getGrillMiddle() {
		return getGrillType().center;
	}
	public Animation getGrillFire() {
		return getGrillType().fire;
	}
	

	public boolean trashHover() {
		if (ks != null && ks.cm != null && ks.cm.mousedOver != null || !holding) return false;
		if (!this.meatSelected()) return false;
		if (ks.isPaused()) return false;
		//		if (meatSelectedNotHeld) return false;
		if (this.onGrillAbsolute(Gdx.input.getX(), Gdx.input.getY())) return false;

		// also don't allow trashing if hovering over spice box
		Selected box = touchBox(KitchenScreen.getUnitX(Gdx.input.getX()), KitchenScreen.getUnitY(Gdx.input.getY()));

		// not good style but for clarity
		if (box == Selected.SPICE) return false;
		return true;
	}

	public void drawFloatingMeat(SpriteBatch batch, float delta) {
		TextureRegion toDraw = null;

		MeatTypes.Type type = getType(this.selected);		
		toDraw = Assets.getMeatTexture(type, Meat.State.RAW, false);

		Meat.draw(batch, toDraw, type.doubleWidth, Gdx.input.getX() - Meat.getWidth()/2, (KebabKing.getHeight() - Gdx.input.getY()) - Meat.getHeight() / 2, profile);
	}

	public void drawFloatingBeer(SpriteBatch batch, float delta) {
		float beerWidth = 80;
		float beerHeight = 202;

		batch.draw(Assets.floatingBeer, Gdx.input.getX() - beerWidth/2, KebabKing.getHeight() - Gdx.input.getY() - beerHeight/2, beerWidth, beerHeight);
	}

	public void drawPaintbrush(SpriteBatch batch, float delta) {
		float brushWidth = 80;
		float brushHeight = 120;

		if (brushRotUp) {
			brushRot += BRUSH_ROT_RATE * delta;
			if (brushRot > BRUSH_MAX_ROT) {
				brushRotUp = false;
				brushRot = BRUSH_MAX_ROT;
			}
		}
		else {
			brushRot -= BRUSH_ROT_RATE * delta;
			if (brushRot < BRUSH_MIN_ROT) {
				brushRotUp = true;
				brushRot = BRUSH_MIN_ROT;
			}
		}
		batch.draw(Assets.paintBrush, Gdx.input.getX() - brushWidth/2, KebabKing.getHeight() - Gdx.input.getY() - brushHeight/8,
				brushWidth / 2, brushHeight / 2, brushWidth, brushHeight, 1, 1, brushRot);
	}

	// say width is 0.4
	// want to draw at 0.3
	public void drawTrashIcon(SpriteBatch batch) {

		batch.draw(Assets.trashIcon, (KebabKing.getWidth() - trashWidth)/2, (KebabKing.getHeight() - trashHeight)/2, trashWidth, trashHeight);
	}
	
	//	public void touchInput(int x, int y) {
	//		if (!active || disableTouch) return;
	//
	//		// KitchenScreen.convert to unit coordinates
	//		int unit_x = KitchenScreen.getUnitX(x);
	//		int unit_y = KitchenScreen.getUnitY(y);
	//
	//		// select spice box for dragging
	//		Selected box = touchBox(unit_x, unit_y);
	//		if (box == Selected.SPICE) select(box);
	//
	//		// if not on grill
	////		Selected box = touchBox(unit_x, unit_y);
	////		if (box != Selected.NONE && !ks.paused) {
	////			select(box);
	////			return;
	////		}
	//
	//		// if on grill
	//		if (onGrillAbsolute(x, y) && (selected == Selected.NONE || meatSelected())) {
	//			int index = getGrillIndex(x, y);
	//			if (meat[index] != null) {
	//				if (selectedSet.contains(meat[index])) {
	//					if (meatSelectedNotHeld) {
	//						//						System.out.println("removing from selected");
	//						removeOnRelease(index);
	//
	//
	//						//						selectedSet.remove(meat[index]);
	//					}
	//				}
	//				else selectMeat(index);
	//			}
	//			return;
	//		}
	//	}

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

	//	public void drawStandFront(SpriteBatch batch) {
	//		VanityGrillStand stand = (VanityGrillStand) profile.inventory.grillStand.getCurrentSelected();
	//		if (stand.front != null) {
	//			batch.draw(stand.front, KitchenScreen.convertWidth(getGrillCenter()) - KebabKing.getGlobalX(STAND_WIDTH/2), KebabKing.getGlobalY(STAND_Y), 
	//					KebabKing.getGlobalX(STAND_WIDTH), KebabKing.getGlobalY(STAND_HEIGHT));
	//		}
	//		//		System.out.println(getGrillCenter());
	//	}
	public void drawStandBack(SpriteBatch batch) {
		GrillStand.Stand stand = (GrillStand.Stand) profile.inventory.grillStand.getFirstSelected();
		if (stand.back != null) {
			batch.draw(stand.back, KitchenScreen.convertWidth(getGrillCenter()) - KebabKing.getGlobalX(STAND_WIDTH/2), KebabKing.getGlobalY(STAND_Y), 
					KebabKing.getGlobalX(STAND_WIDTH), KebabKing.getGlobalY(STAND_HEIGHT));
		}
	}

	public void drawBoxes(SpriteBatch batch) {
		int box_hor_width 	= KitchenScreen.convertWidth(BOX_HOR_WIDTH);
		int box_hor_height 	= KitchenScreen.convertHeight(BOX_HOR_HEIGHT);
		int spice_width 	= KitchenScreen.convertWidth(SPICE_WIDTH);
		int spice_height 	= KitchenScreen.convertHeight(SPICE_HEIGHT);
		int box_ver_width 	= KitchenScreen.convertWidth(BEER_WIDTH);
		int box_ver_height 	= KitchenScreen.convertHeight(BEER_HEIGHT);
		int first_x 	= KitchenScreen.convertXWithBuffer(FIRST_X);
		int second_x 		= KitchenScreen.convertXWithBuffer(SECOND_X);
		int third_x 		= KitchenScreen.convertXWithBuffer(THIRD_X);
		int bottom_y 	= KitchenScreen.convertYWithBuffer(BOTTOM_Y);
		int beer_x 		= KitchenScreen.convertXWithBuffer(BEER_X);
		int beer_y 		= KitchenScreen.convertYWithBuffer(BEER_Y);

		// draw spice directly to right of grill
		int spice_x 	= KitchenScreen.convertXWithBuffer(grillRightX);
		int spice_y 	= KitchenScreen.convertYWithBuffer(SPICE_Y);

		// draw boxes in their proper locations
		//		batch.setColor(1, 1, 1, 1);

		//		Color original = batch.getColor();
		//		if (!active) {
		//			batch.setColor(Color.GRAY);
		//		}

		TextureRegion toDraw;
		MeatTypes.Type currentType;

		// Draw first box
		currentType = getType(Selected.FIRST);
		toDraw = currentType.coolerClosed;
		if (selected == Selected.FIRST)
			toDraw = currentType.coolerOpen;
		batch.draw(toDraw, first_x, bottom_y, box_hor_width, box_hor_height);		


		// Draw second box
		if (boxes[1] != null) {
			currentType = getType(Selected.SECOND);

			toDraw = currentType.coolerClosed;
			if (selected == Selected.SECOND)
				toDraw = currentType.coolerOpen;
			batch.draw(toDraw, second_x, bottom_y, box_hor_width, box_hor_height);		
		}

		// Draw third box
		if (boxes[2] != null) {
			currentType = getType(Selected.THIRD);
			toDraw = currentType.coolerClosed;
			if (selected == Selected.THIRD)
				toDraw = currentType.coolerOpen;
			batch.draw(toDraw, third_x, bottom_y, box_hor_width, box_hor_height);	
		}

		//		if (selected == Selected.CHICKEN) 
		//			batch.draw(Assets.chickenBoxOpen, first_x, bottom_y, box_hor_width, box_hor_height);
		//		else 
		//			batch.draw(Assets.chickenBox, first_x, bottom_y, box_hor_width, box_hor_height);
		//
		//		if (selected == Selected.BEEF) 
		//			batch.draw(Assets.beefBoxOpen, 	second_x, bottom_y, box_hor_width, box_hor_height);
		//		else
		//			batch.draw(Assets.beefBox, 	second_x, bottom_y, box_hor_width, box_hor_height);
		//
		//		if (selected == Selected.LAMB) 
		//			batch.draw(Assets.lambBoxOpen, third_x, bottom_y, box_hor_width, box_hor_height);
		//		else
		//			batch.draw(Assets.lambBox, third_x, bottom_y, box_hor_width, box_hor_height);

		batch.draw(profile.inventory.drinkQuality.getCooler(), beer_x, beer_y, box_ver_width, box_ver_height);

		if (selected == Selected.BEER) 
			batch.draw(Assets.coolerLidOpen, beer_x, beer_y, box_ver_width, box_ver_height);
		else
			batch.draw(Assets.coolerLidClosed, beer_x, beer_y, box_ver_width, box_ver_height);

		
		if (disableSpice()) {
			batch.draw(Assets.spiceBoxDisabled,	spice_x, spice_y, spice_width, spice_height);			
		}
		else {
			batch.draw(Assets.spiceBox,	spice_x, spice_y, spice_width, spice_height);

			if (selected != Selected.SPICE)
				batch.draw(Assets.paintBrushSide,	spice_x, spice_y, spice_width, spice_height);
		}
		//		else 
		//			batch.draw(Assets.spiceBox,	spice_x, spice_y, spice_width, spice_height);

		//		batch.setColor(original);
	}

	// removes meat at given index when touch is released
	public void removeOnRelease(int index) {
		removeOnRelease = index;
	}

	public void select(Selected newSelected) {
		this.selected = newSelected;

		System.out.println("deselecting meat");
		this.deselectAll();
		//		this.boxSelectedNotHeld = false;
		this.meatSelectedNotHeld = false;
	}

	public void selectMeat(int grillIndex) {
		if (grillIndex < 0 || grillIndex > meat.length) throw new java.lang.ArrayIndexOutOfBoundsException();
		this.selected = Selected.NONE;
		this.selectedSet.add(meat[grillIndex]);
		//		this.boxSelectedNotHeld = false;
		this.meatSelectedNotHeld = false;
		this.justSelected = grillIndex;
	}
	
	// use this to control when meat/beer boxes are disabled
//	public boolean disableMeatBoxes() {
////		return TutorialEventHandler.dontAllowCustomer() && ks.cm.totalPeopleInLines() == 0;
//	}
	
	public boolean disableSpice() {
//		if (ks == null || ks.cm == null) return true;
//		return profile.stats.daysWorked == 0;
//		return ks.cm.customerNotServed();
		return TutorialEventHandler.shouldNotOrderSpice();
	}
	
	public boolean disableSelectingMeat() {
		return TutorialEventHandler.shouldDisableGrill();
	}
	
	// used for calculating "mouseOver"
	public void holdInput(int x, int y) {
		// disable everything
		holding = true;

		if (firstTouch) {
			if (selected == Selected.SPICE) {
				justTappedSpice = false;
			}
			else {
				Selected box = touchBox(KitchenScreen.getUnitX(x), KitchenScreen.getUnitY(y));
				System.out.println("first touch is " + box);
				if (box == Selected.SPICE) {
					if (disableSpice()) return;
					this.justTappedSpice = true;
					this.select(box);
				}
				if (!ADVANCED_CONTROLS && box != Selected.NONE) {
					if (TutorialEventHandler.shouldDisableBoxes()) return;
					this.select(box);
				}
			}
			firstTouch = false;
		}

		mousedOver = -1;

		if (onGrillArea(x, y) && !ADVANCED_CONTROLS) {
			if (meatBoxSelected())
				indexToPlaceMeatAt = getNextOpenSpot();
		}
		else {
			indexToPlaceMeatAt = -1;
			//			if (selected )
			//			System.out.println("Not mousing over grill at " + x + ", " + y);
		}

		//		if (onGrill(unit_x, unit_y)) {
		if (onGrillAbsolute(x, y)) {
			mousedOver = getGrillIndex(x, y);
			if (mousedOver < 0) System.out.println("GetGrillIndex is broken: moused over is " + mousedOver);
			//			System.out.println("Mousing over grill " + mousedOver + " at " + x + ", " + y);
		}

		// if moused over grill and spice is selected then spice whatever is there
		if (mousedOver != -1 && meat[mousedOver] != null) {
			if (this.selected == Selected.SPICE) {
				dropSpice();
			}
			else if (this.selected == Selected.NONE || ((this.meatBoxSelected() || this.selected == Selected.BEER) && !placedWhileHeld && this.selected != Selected.BEER)) {
				if ((ADVANCED_CONTROLS || !meatBoxSelected()) && !selectedSet.contains(meat[mousedOver])) {
					if (disableSelectingMeat()) return;
					this.selected = Selected.NONE;
					//					selectedSet.add(meat[mousedOver]);
					selectMeat(mousedOver);
				}
			}
		}

		// draw ghost meat under mouse when dragging meat off grill
		// only if a meat from the grill is selected
		if ((meatSelected() || selected == Selected.BEER) && ks.cm != null)
			ks.cm.updateMousedOver(x, y);
	}

	// Returns -1 if no spot found
	public int getNextOpenSpot() {
		for (int i = 0; i < meat.length; i++) {
			if (meat[i] == null) {
				if (!getType(selected).doubleWidth) return i;
				else if (i < meat.length - 1 && meat[i + 1] == null) return i;
			}
		}
		return -1;
	}

	public void release(int x, int y) {
		this.holding = false;

		if (!this.active || this.disableTouch) {
			System.out.println("GRILL NOT ACTIVE OR TOUCH DISABLED");
			return;
		}
		firstTouch = true;

		if (hoverTrash) {
			this.trashSelected();
		}

		//System.out.println("Releasing. Moused over grill: " + mousedOver() + " " + " new meat selected: " + (newMeatSelected() && mousedOver != 0) + " meat selected: " + meatSelected() + " ");
		//System.out.println("Customer not null :" + (ks.cm.mousedOver != null));
		// if (ks.cm.mousedOver != null) System.out.println(" order not null: " + ks.cm.mousedOver.order != null);

		boolean gaveBeerToCustomer = false;
		// drop stuff onto grill 
		if (mousedOver()) {
			if (meatBoxSelected()) {
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
			else if (meatSelected() && mousedOver != justSelected) {
				// remove it from selected if clicking it twice
				if (!open(mousedOver)) {

					// only remove on release if this isn't the first touch
					removeOnRelease(mousedOver);
					//					if (selectedSet.contains(meat[mousedOver]) && meatSelectedNotHeld) 
					//						selectedSet.remove(meat[mousedOver]);
				}
				else if (open(mousedOver) || 
						(meat[mousedOver] == selectedSet.get(0) && !meat[mousedOver].type.doubleWidth)) {
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
			if (ADVANCED_CONTROLS) {
				// drop fresh meat on grill
				if (mousedOver != -1 && this.meatBoxSelected()) {
					if (meat[mousedOver] == null && open(mousedOver)) {
						ks.dropMeatOnGrill(getType(selected), mousedOver);
						this.placedWhileHeld = true;

						removeOnRelease(this.mousedOver);
						//						this.select(Selected.NONE);
					}
				}
			}
			//			if (onGrillAbsolute(x, y) && (selected == Selected.NONE || meatSelected())) {
			//				if (meat[mousedOver] != null) {
			//					if (selectedSet.contains(meat[index])) {
			//						if (meatSelectedNotHeld) {
			//							//						System.out.println("removing from selected");
			//							removeOnRelease(index);
			//
			//
			//							//						selectedSet.remove(meat[index]);
			//						}
			//					}
			//					else selectMeat(index);
			//				}
			//				return;
			//			}
		}
		// drop stuff into trash
		//		else if (mousedOverTrash && meatSelected()) {
		////			trashSelected();
		//		}
		// drop meat on customers
		else if (meatSelected() && ks.cm.mousedOver != null && ks.cm.mousedOver.order != null) {
			ks.serveCustomerAll(ks.cm.mousedOver);
		}
		// drop beer on customers
		else if (selected == Selected.BEER && ks.cm.mousedOver != null && ks.cm.mousedOver.order != null) {
			gaveBeerToCustomer = ks.serveCustomerBeer(ks.cm.mousedOver);
		}
		// trash meat if dropping it onto empty space and you were holding down
		else if (meatSelected() && !meatSelectedNotHeld) {
			//			trashSelected();
		}

		//		System.out.println("remove on release: " + removeOnRelease + " justSelected " + justSelected);
		// remove on release, don't remove if just selected
		if (removeOnRelease >= 0) {
			selectedSet.remove(meat[removeOnRelease]);
			removeOnRelease = -1;
		}


		// selectButNotHold
		int unit_x = KitchenScreen.getUnitX(x);
		int unit_y = KitchenScreen.getUnitY(y);
		Selected box = touchBox(unit_x, unit_y);

		// no meat box was clicked, maybe deselect
		if (box == Selected.NONE) {
			if (onGrillAbsolute(x, y)) {
				if (selectedSet.contains(meat[getGrillIndex(x, y)])) 
					this.meatSelectedNotHeld = true;
			}
			else {
				// don't deselect if just selected meat (unless trashing)
				if (!gaveBeerToCustomer && (meatSelectedNotHeld || hoverTrash)) {
					select(Selected.NONE);
				}
			}
		}
		// releasing on box
		else {
			if (ADVANCED_CONTROLS) {
				// maybe select spice box, maybe deselect it if already selected
				if (box == Selected.SPICE) {
					if (selected != Selected.SPICE) {
						// don't select Spice box if just deselected meat
						if (meatSelectedNotHeld || hoverTrash)
							select(box);
					}
					else if (!justTappedSpice) 
						select(Selected.NONE);
				}
				else select(box);
			}

			//								this.boxSelectedNotHeld = true;
			//			}
		}

		if (!ADVANCED_CONTROLS) {
			//			System.out.println(indexToPlaceMeatAt);
			if (indexToPlaceMeatAt >= 0) {
				if (!meatBoxSelected()) indexToPlaceMeatAt = -1;
				else ks.dropMeatOnGrill(getType(selected), indexToPlaceMeatAt);
			}

			if (!meatSelected())
				select(Selected.NONE);
			else 
				selected = Selected.NONE;
			indexToPlaceMeatAt = -1;
		}


		placedWhileHeld = false;
		this.justSelected = -1;
		justTappedSpice = false;
	}

	//	// converts from selected to Meat.Type
	//	public Meat.Type toMeat(Selected selected) {
	//		if (selected == Selected.CHICKEN) return Meat.Type.CHICKEN;
	//		if (selected == Selected.BEEF) return Meat.Type.BEEF;
	//		if (selected == Selected.LAMB) return Meat.Type.LAMB;
	//		return null;
	//	}

	/** 
	 * Given touched units, return the box that was touched, or none if no box was touched
	 */
	private Selected touchBox(int unit_x, int unit_y) {
		if (unit_x >= FIRST_X && unit_x < FIRST_X + BOX_HOR_WIDTH) {
			if (unit_y >= BOTTOM_Y && unit_y < BOTTOM_Y + BOX_HOR_HEIGHT){
				return Selected.FIRST;
			}
		}
		if (unit_x >= SECOND_X && unit_x < SECOND_X + BOX_HOR_WIDTH) {
			if (unit_y >= BOTTOM_Y && unit_y < BOTTOM_Y + BOX_HOR_HEIGHT){
				if (boxes[1] != null)
					return Selected.SECOND;
			}
		}
		if (unit_x >= THIRD_X && unit_x < THIRD_X + BOX_HOR_WIDTH) {
			if (unit_y >= BOTTOM_Y && unit_y < BOTTOM_Y + BOX_HOR_HEIGHT){
				if (boxes[2] != null)
					return Selected.THIRD;
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
		return this.selected == Selected.FIRST || this.selected == Selected.SECOND || this.selected == Selected.THIRD;
	}

	private int getGrillSize() {
		return size;
	}

	// returns true if x,y is anywhere where a grill could be (anywhere from right of screen to left of screen, including paintbrush)
	public boolean onGrillArea(int x, int y) {
		//		System.out.println(x + ", " + y);
		// assumption is that halfway across screen will always be on grill
		return onGrillAbsolute(KebabKing.getGlobalX(0.5f), y);
	}

	public boolean onGrillAbsolute(int x, int y) {
		grillLeftX = (GRILL_WIDTH) / 2 - (size + SPICE_WIDTH) / 2;

		grillRightX = grillLeftX + size;

		draw_width = GRILL_PIECE_WIDTH * KitchenScreen.UNIT_WIDTH;
		draw_height = (int) (GRILL_PIECE_HEIGHT * KitchenScreen.UNIT_HEIGHT);

		draw_x = (grillLeftX + GRILL_X) * KitchenScreen.UNIT_WIDTH;
		draw_y = (int) ((grillLeftY + GRILL_Y) * KitchenScreen.UNIT_HEIGHT);

		int left = draw_x;
		int bottom = draw_y;
		int right = draw_x + draw_width * size;
		int top = draw_y + draw_height;


		//		System.out.println(left + " " + bottom + " " + right + " " + top + " " + x + " " + (KebabKing.getHeight()-y));
		boolean onGrill = (x > left && x < right && KebabKing.getHeight() - y > bottom && KebabKing.getHeight() - y < top);
		//		System.out.println("On grill: " + onGrill);
		return (onGrill);
		//		return onGrill(KitchenScreen.getUnitX(x), KitchenScreen.getUnitY(y));
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
			m.draw(batch, x, y, profile);
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
		if (meatBoxSelected() && indexToPlaceMeatAt >= 0 && !ADVANCED_CONTROLS) { // !this.selectedSet.contains(meat[mousedOver])) {

			// TODO fix this, because we're dealing with placing new meat.

			// draw where all the meat will go if dropped!
			//			for now, just draw the first one
			batch.setColor(1, 1, 1, .6f);
			TextureRegion toDraw = null;

			MeatTypes.Type ghostType = getType(this.selected);
			toDraw = Assets.getMeatTexture(ghostType, Meat.State.RAW, false);

			//			if (this.selected == Selected.CHICKEN) { // && this.canFit(Meat.Type.CHICKEN)) {
			//				toDraw = Assets.getMeatTexture(Meat.Type.CHICKEN, Meat.State.RAW, false);
			//				chickenGhost = true;
			//			}
			//			if (this.selected == Selected.BEEF) //  && this.canFit(Meat.Type.BEEF))
			//				toDraw = Assets.getMeatTexture(Meat.Type.BEEF, Meat.State.RAW, false);
			//			if (this.selected == Selected.LAMB) //  && this.canFit(Meat.Type.LAMB))
			//				toDraw = Assets.getMeatTexture(Meat.Type.LAMB, Meat.State.RAW, false);			

			if (toDraw != null) {
				Meat.draw(batch, toDraw, ghostType.doubleWidth, getXForIndex(indexToPlaceMeatAt), getYForIndex(indexToPlaceMeatAt), profile);
			}
			batch.setColor(1, 1, 1, 1);
			//				batch.draw(toDraw, getXForIndex(indexToPlaceMeatAt), getYForIndex(indexToPlaceMeatAt),  (int) (GRILL_PIECE_WIDTH * widthFactor * KitchenScreen.UNIT_WIDTH / 
			//						CHUANR_PER_PIECE), KitchenScreen.UNIT_HEIGHT * GRILL_PIECE_HEIGHT);
		}
		if (this.meatSelected()) {
			//			Color orig = batch.getColor();
			for (Meat m : selectedSet) {
				//				Color myColor = Color.WHITE;
				//				myColor.a = .2f;
				//				batch.setColor(myColor);
				batch.draw(Assets.whiteAlpha, getXForIndex(m.index1), getYForIndex(m.index1) + KitchenScreen.UNIT_HEIGHT * (GRILL_PIECE_HEIGHT - CHUANR_HEIGHT),  (int) (GRILL_PIECE_WIDTH * KitchenScreen.UNIT_WIDTH / 
						CHUANR_PER_PIECE), KitchenScreen.UNIT_HEIGHT * CHUANR_HEIGHT);
				if (m.type.doubleWidth) 
					batch.draw(Assets.whiteAlpha, getXForIndex(m.index2), getYForIndex(m.index2) + KitchenScreen.UNIT_HEIGHT * (GRILL_PIECE_HEIGHT - CHUANR_HEIGHT),  (int) (GRILL_PIECE_WIDTH * KitchenScreen.UNIT_WIDTH / 
							CHUANR_PER_PIECE), KitchenScreen.UNIT_HEIGHT * CHUANR_HEIGHT);
			}
			//			batch.setColor(orig);
		}
	}

	// drops meat at mousedOver index
	public Meat dropMeat(MeatTypes.Type type, int index) {
		Meat toDrop = new Meat(type, this);

		if (!canFit(toDrop, index)) return null;
		meat[index] = toDrop;
		meat[index].setIndex(index);
		meatOnGrill++;
		if (type.doubleWidth) {
			meat[index + 1] = meat[index];
		}

		TutorialEventHandler.handleMeatPlaced();

		return toDrop;
		//		this.boxSelectedNotHeld = false;
	}
	
	public GrillType.Type getGrillType() {
		return (GrillType.Type) profile.inventory.grillType.getFirstSelected();
	}
	
	public float getCookSpeedFactor() {
		return getGrillType().cookSpeedFactor;
	}
	
	public float getBurnSpeedFactor() {
		return getGrillType().burnSpeedFactor;
	}
	
	// trashes meat from grill
	public void trashSelected() {
		int removed = removeSelected();
		ks.kebabsTrashed += removed;
		System.out.println("Kebabs trashed: " + ks.kebabsTrashed);
		kebabsTrashedThisSession += removed;
		
		StatsHandler.trashKebab();
		TutorialEventHandler.handleTrash();
	}

	private boolean canFitAt(int index, Meat piece) {
		if (!piece.type.doubleWidth)
			return meat[index] == null;
		else {
			return index + 1 < meat.length && 
					(open(index) || meat[index] == piece)
					&& (open(index + 1) || meat[index + 1] == piece);
		}
	}

	//	private boolean canFitAt(int index, Meat.Type type) {
	//		if (type != Meat.Type.CHICKEN)
	//			return meat[index] == null;
	//		else {
	//			return index + 1 < meat.length && (open(index)) && open(index + 1);
	//		}
	//	}

	// will this type of meat fit at mousedOver?
	private boolean canFit(Meat piece, int index) {
		return canFitAt(index, piece);
	}

	//	// will this type of meat fit at mousedOver?
	//	private boolean canFit(Meat.Type type) {
	//		return canFitAt(mousedOver, type);
	//	}

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
			meatOnGrill--;
			if (m.type.doubleWidth) meat[m.index2] = null;
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

		deselectAll();
	}

	public void moveFrom(int origIndex, int newIndex) {
		Meat toMove = meat[origIndex];
		if (toMove == null) return;
		// we null this out early because we don't want to null it at the end,
		// when it might actually have the moved meat
		meat[origIndex] = null;

		if (toMove.type.doubleWidth) {
			meat[origIndex + 1] = null;
		}

		meat[newIndex] = toMove;
		meat[newIndex].setIndex(newIndex);

		if (toMove.type.doubleWidth) {
			meat[newIndex + 1] = meat[newIndex];
		}
	}

	public void deselectAll() {
		selectedSet.clear();
	}
	
	public boolean open(int index) {
		return (meat[index] == null);
	}

	public boolean meatBoxSelected() {
		return isMeat(selected);
	}

	// Is meat from grill selected?
	public boolean meatSelected() {
		return this.selectedSet.size() > 0;
	}

	public boolean mousedOver() {
		return this.mousedOver >= 0;
	}

	public int getGrillCenter() {
		//		System.out.println(grillRightX + " " + grillLeftX);
		return (this.grillRightX - this.grillLeftX) / 2 + this.grillLeftX;
	}
}
