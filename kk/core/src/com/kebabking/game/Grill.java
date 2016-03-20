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
import com.kebabking.game.Purchases.KebabTypes;

// contains information for the grill, the "trash", spice box, and ice chests
//
// The following is how the control scheme for meat should work:
// 		BASIC (SWIPE) CONTROL SCHEME:
//		Touching down on a chest drags meat of that type (chest is opened during drag). Mousing over grill area "ghosts" meat where it would be placed.
//		Releasing places meat at that spot.
//		Tapping chest does nothing
//		Beer must be dragged onto customers.

// 		[DEPRECATED] ADVANCED (TAP) CONTROL SCHEME:
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

	// number of spots on the right of grill that are for warming meat.
	public static int WARMING_SIZE = 2;

	static final float TIME_TO_TRASH = 0.4f;
	static final float TIME_TO_MOVE = 0.1f;
	static final float TAP_TIME = 0.2f;
	static final boolean ADVANCED_CONTROLS = false;
	static final boolean DROP_MEAT_UNDER_FINGER = true;

	//	static final boolean DISABLE_DRAG_TO_PLACE = false;

	static final int GRILL_X = 0;
	static final float GRILL_Y = 2f;
	static final int GRILL_WIDTH = 12;
	static final float GRILL_HEIGHT = 3.5f;

	static final float GRILL_GRATE_PERCENT = 0.78f;

	static final int GRILL_PIECE_WIDTH = 1;
	static final float GRILL_PIECE_HEIGHT = GRILL_HEIGHT;
	static final double CHUANR_PER_PIECE = 1;
	static final float CHUANR_HEIGHT = 2.8f;
	static final float LIFTED_MEAT = 0.5f;

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

	static final float FIRE_PAD_X = 0.015f;
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
	public enum SelectedBox {
		NONE, FIRST, SECOND, THIRD, SPICE, BEER
	}
	KebabTypes.Type[] boxes;

	SelectedBox selectedBox;

	boolean tutorialMode;

	/** if false, grill cannot be interacted with */
	boolean active; 
	boolean disableTouch;

	boolean hoverTrash; // are we hovering over a
	boolean hoverMove; // are we hovering over a

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
	//	boolean holdingAfterSelect = false;

	boolean placedWhileHeld = false; // true when meat has been placed during the 'hold'
	int removeOnRelease = -1;
	int justSelected = -1; // for use with remove on release
	boolean firstTouch = true;

	boolean holdSpiceOnNextTouch;

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

	float moveHoldTime = 0;
	float trashHoldTime = 0;
	float holdTime = 0;

	public Grill(Profile profile) {
		//		this.grillType = profile.grillType;
		this.profile = profile;
		this.mousedOver = -1;

		this.selectedSet = new ArrayList<Meat>();
		selectedBox = SelectedBox.NONE;		

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
		this.selectedBox = null;
		SoundManager.stopSizzle();
	}

	public void updateBoxes() {
		boxes = new KebabTypes.Type[3];

		// Order is based on selected order (customizable)
		// another option would be to have it come in fixed order.
		Deque<Integer> boxesDeque = new ArrayDeque<Integer>(profile.inventory.kebabTypes.getSelected());
		if (boxesDeque.size() == 1) {
			boxes[0] = KebabTypes.Type.values[boxesDeque.pop()];
		} 
		else if (boxesDeque.size() == 2) {
			boxes[0] = KebabTypes.Type.values[boxesDeque.pop()];
			boxes[1] = KebabTypes.Type.values[boxesDeque.pop()];
		}
		else if (boxesDeque.size() == 3) {
			boxes[0] = KebabTypes.Type.values[boxesDeque.pop()];
			boxes[1] = KebabTypes.Type.values[boxesDeque.pop()];
			boxes[2] = KebabTypes.Type.values[boxesDeque.pop()];	
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
		this.selectedBox = SelectedBox.NONE;
		this.ks = null;
		this.active = false;
		hoverTrash = true;
		clearMeat();
		deselectAll();
		SoundManager.stopSizzle();
	}

	public void act(float delta) {
		if (!active) {
			return;
		}
		//		System.out.println("just tapped spice: " + justTappedSpice);
		//		System.out.println("hold spice on next touch: " + holdSpiceOnNextTouch);
		//		System.out.println("spice selected: " + (SelectedBox.SPICE == selectedBox));

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
			if (m != null && !(m.type.doubleWidth && m.index2 == i) && !isWarming(i))
				m.act(delta);
		}
		this.time += delta;

		// play sound

	}

	public boolean isWarming(int index) {
		return profile.inventory.grillType.isWarming() && index >= getGrillSize() - WARMING_SIZE;
	}

	public KebabTypes.Type getType(SelectedBox select) {
		if (select == SelectedBox.FIRST)
			return boxes[0];
		if (select == SelectedBox.SECOND)
			return boxes[1];
		if (select == SelectedBox.THIRD)
			return boxes[2];
		return null;
	}

	public SelectedBox getSelectedForType(KebabTypes.Type type) {
		int selectedIndex = -1;
		for (int i = 0; i < boxes.length; i++) {
			if (boxes[i] == type)
				selectedIndex = i;
		}
		if (selectedIndex == 0) return SelectedBox.FIRST;
		if (selectedIndex == 1) return SelectedBox.SECOND;
		if (selectedIndex == 2) return SelectedBox.THIRD;

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
		//		System.out.println("holding after select: " + holdingAfterSelect);
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

		// draw floating meat below finger
		if (drawingFloatingSelected()) {
			drawFloatingSelectedMeat(batch, delta);
		}

		if (selectedBox == SelectedBox.BEER && !ADVANCED_CONTROLS)
			drawFloatingBeer(batch, delta);

		// draw spice paintbrush
		if (this.selectedBox == SelectedBox.SPICE && Gdx.input.isTouched()) {
			drawPaintbrush(batch, delta);
		}

		if (moveHover()) {
			moveHoldTime += delta;
			if (moveHoldTime > TIME_TO_MOVE) {
				hoverMove = true;
			}
		}
		else {
			this.moveHoldTime = 0;
			hoverMove = false;
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

		if (holding) {
			holdTime += delta;
		}
		else holdTime = 0;
	}

	public void drawGrill(SpriteBatch batch) {
		TextureRegion fire = getGrillFire().getKeyFrame(time);
		//		if (!active) {
		//			fire = Assets.gray;
		//		}

		int xCount = grillLeftX;

		int index = 0;

		if (isWarming(index)) fire = getGrillFireWarming().getKeyFrame(time);
		// draw left
		batch.draw(fire, draw_x + KebabKing.getGlobalX(FIRE_PAD_X), draw_y + (1-GRILL_GRATE_PERCENT) * draw_height, draw_width * 2 - KebabKing.getGlobalX(FIRE_PAD_X), draw_height * GRILL_GRATE_PERCENT - KebabKing.getGlobalY(FIRE_PAD_TOP)); 
		index++;
		index++;
		//		if (isWarming(index)) fire = getGrillFireWarming().getKeyFrame(time);
		// draw two fires
		//		batch.draw(fire, draw_x + draw_width, draw_y + (1-GRILL_GRATE_PERCENT) * draw_height, draw_width, draw_height * GRILL_GRATE_PERCENT - KebabKing.getGlobalY(FIRE_PAD_TOP)); 
		//		batch.draw(Assets.grillCoals, draw_x, draw_y, draw_width, draw_height); 
		batch.draw(getGrillLeft(), draw_x, draw_y, draw_width * 2, draw_height); 
		xCount++;
		xCount++;
		if (isWarming(index)) fire = getGrillFireWarming().getKeyFrame(time);

		// draw middle
		while (xCount < GRILL_WIDTH - grillLeftX - 4) {
			draw_x = (xCount + GRILL_X) * KitchenScreen.UNIT_WIDTH;
			if (index % 2 == 0) {
				batch.draw(fire, draw_x, draw_y + (1-GRILL_GRATE_PERCENT) * draw_height, draw_width * 2, draw_height * GRILL_GRATE_PERCENT - KebabKing.getGlobalY(FIRE_PAD_TOP)); 
				batch.draw(getGrillMiddle(), draw_x, draw_y, draw_width * 2, draw_height);
			}	//			batch.draw(Assets.grillCoals, draw_x, draw_y, draw_width, draw_height); 
			xCount++;
			index++;
			if (isWarming(index)) fire = getGrillFireWarming().getKeyFrame(time);
		}

		// draw right		 
		draw_x = (xCount + GRILL_X) * KitchenScreen.UNIT_WIDTH;
		batch.draw(fire, draw_x, draw_y + (1-GRILL_GRATE_PERCENT) * draw_height, draw_width * 2 - KebabKing.getGlobalX(FIRE_PAD_X), draw_height * GRILL_GRATE_PERCENT - KebabKing.getGlobalY(FIRE_PAD_TOP)); 
		index++;
		if (isWarming(index)) fire = getGrillFireWarming().getKeyFrame(time);
		//		batch.draw(fire, draw_x + draw_width, draw_y + (1-GRILL_GRATE_PERCENT) * draw_height, draw_width - KebabKing.getGlobalX(FIRE_PAD_X), draw_height * GRILL_GRATE_PERCENT - KebabKing.getGlobalY(FIRE_PAD_TOP)); 
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
	public Animation getGrillFireWarming() {
		return getGrillType().warmingFire;
	}

	public boolean moveHover() {
		return canMoveSelectedTo(mousedOver);
	}

	public boolean trashHover() {
		if (ks == null) return false;
		if (ks.cm != null && ks.cm.mousedOver != null || !holding) return false;
		if (!this.meatSelected()) return false;
		if (ks.isPaused()) return false;
		if (TutorialEventHandler.shouldDisableTrash()) return false;
		//		if (meatSelectedNotHeld) return false;
		if (this.onGrillAbsolute(Gdx.input.getX(), Gdx.input.getY())) return false;

		// also don't allow trashing if hovering over spice box
		SelectedBox box = touchBox(KitchenScreen.getUnitX(Gdx.input.getX()), KitchenScreen.getUnitY(Gdx.input.getY()));

		// not good style but for clarity
		if (box == SelectedBox.SPICE) return false;
		return true;
	}

	public void drawFloatingMeat(SpriteBatch batch, float delta) {
		TextureRegion toDraw = null;

		KebabTypes.Type type = getType(this.selectedBox);		
		toDraw = Assets.getMeatTexture(type, Meat.State.RAW, false);

		Meat.draw(batch, toDraw, type.doubleWidth, Gdx.input.getX() - Meat.getWidth()/2, (KebabKing.getHeight() - Gdx.input.getY()) - Meat.getHeight() / 2, profile, 0.8f);
	}

	public boolean drawingFloatingSelected() {
		return !ADVANCED_CONTROLS && meatSelected() && !mousedOver() && holding;
	}

	public void drawFloatingSelectedMeat(SpriteBatch batch, float delta) {
		TextureRegion toDraw = null;

		// space meat 
		int meatCount = this.selectedSet.size();
		int totalWidth = meatCount;
		for (Meat m : selectedSet) {
			if (m.type.doubleWidth) totalWidth++;
		}

		float offsetDelta = 1.0f/meatCount;

		float scaleEffect = 0.8f;

		float offset = -totalWidth * offsetDelta / 2;

		for (int i = 0; i < meatCount; i++) {
			KebabTypes.Type type = selectedSet.get(i).type;
			toDraw = Assets.getMeatTexture(selectedSet.get(i));

			Meat.draw(batch, toDraw, type.doubleWidth, Gdx.input.getX() - Meat.getWidth()/2 + (int) (offset * Meat.getWidth()), (KebabKing.getHeight() - Gdx.input.getY()) - Meat.getHeight() / 2, profile, scaleEffect);
			offset += offsetDelta;
			if (type.doubleWidth) offset += offsetDelta;
		}	
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

	public boolean holdingPastTap() {
		return this.holdTime > TAP_TIME;
	}

	// say width is 0.4
	// want to draw at 0.3
	public void drawTrashIcon(SpriteBatch batch) {

		batch.draw(Assets.trashIcon, (KebabKing.getWidth() - trashWidth)/2, (KebabKing.getHeight() - trashHeight)/2, trashWidth, trashHeight);
	}

	public void drawTrashPile(SpriteBatch batch) {
		ks.tp.draw(batch);
	}

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
		KebabTypes.Type currentType;
//		MeatQuality.Quality currentQuality;

		float iconPos = 0.2f;

		// Draw first box
		//		currentLevel = 
		currentType = getType(SelectedBox.FIRST);
//		currentQuality = (MeatQuality.Quality) profile.inventory.meatQuality.getFirstSelected();
		
		TextureRegion coolerOpen = profile.inventory.meatQuality.getCoolerOpen();
		TextureRegion coolerClosed = profile.inventory.meatQuality.getCoolerClosed();
		
		float iconWidth = KebabKing.getGlobalX(0.08f);
		float iconHeight = iconWidth;

		toDraw = coolerClosed;
		if (selectedBox == SelectedBox.FIRST)
			toDraw = coolerOpen;
		batch.draw(toDraw, first_x, bottom_y, box_hor_width, box_hor_height);
		batch.draw(currentType.bigIcon, first_x + (box_hor_width - iconWidth)/2, bottom_y + (box_hor_height - iconHeight)*iconPos, iconWidth, iconHeight);

		// Draw second box
		if (boxes[1] != null) {
			currentType = getType(SelectedBox.SECOND);

			toDraw = coolerClosed;
			if (selectedBox == SelectedBox.SECOND)
				toDraw = coolerOpen;
			batch.draw(toDraw, second_x, bottom_y, box_hor_width, box_hor_height);		
			batch.draw(currentType.bigIcon, second_x + (box_hor_width - iconWidth)/2, bottom_y + (box_hor_height - iconHeight)*iconPos, iconWidth, iconHeight);		
		}

		// Draw third box
		if (boxes[2] != null) {
			currentType = getType(SelectedBox.THIRD);
			toDraw = coolerClosed;
			if (selectedBox == SelectedBox.THIRD)
				toDraw = coolerOpen;
			batch.draw(toDraw, third_x, bottom_y, box_hor_width, box_hor_height);	
			batch.draw(currentType.bigIcon, third_x + (box_hor_width - iconWidth)/2, bottom_y + (box_hor_height - iconHeight)*iconPos, iconWidth, iconHeight);
		}

		batch.draw(profile.inventory.drinkQuality.getCooler(), beer_x, beer_y, box_ver_width, box_ver_height);

		if (selectedBox == SelectedBox.BEER) 
			batch.draw(Assets.coolerLidOpen, beer_x, beer_y, box_ver_width, box_ver_height);
		else
			batch.draw(Assets.coolerLidClosed, beer_x, beer_y, box_ver_width, box_ver_height);


		if (disableSpice()) {
			batch.draw(Assets.spiceBoxDisabled,	spice_x, spice_y, spice_width, spice_height);			
		}
		else {
			batch.draw(getGrillType().spice, spice_x, spice_y, spice_width, spice_height);

			if (selectedBox != SelectedBox.SPICE && !holdSpiceOnNextTouch)
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

	public void select(SelectedBox newSelected) {
		this.selectedBox = newSelected;

		//		System.out.println("ring meat");
		this.deselectAll();
		//		this.boxSelectedNotHeld = false;
		this.meatSelectedNotHeld = false;
	}

	public void selectMeat(int grillIndex) {
		if (grillIndex < 0 || grillIndex > meat.length) throw new java.lang.ArrayIndexOutOfBoundsException();
		this.selectedBox = SelectedBox.NONE;
		this.selectedSet.add(meat[grillIndex]);
		meat[grillIndex].selected = true;
		//		this.boxSelectedNotHeld = false;
		this.meatSelectedNotHeld = false;
		this.justSelected = grillIndex;
	}

	public void deselectMeatNotOrdered(Customer customer) {
		Order o = customer.order;

		double firstSelected = 0;
		double secondSelected = 0;
		double thirdSelected = 0;

		for (Meat m : meat) {
			//			System.out.println("selectedset contains meat? " + selectedSet.contains(m));
			if (selectedSet.contains(m)) {				
				if (!o.hasOrderedType(m.type)) {
					selectedSet.remove(m);
					m.selected = false;
				}
				else {
					// customer has ordered this type, but selected too many.
					if (m.type == this.getType(SelectedBox.FIRST)) {
						if (m.type.doubleWidth) firstSelected += 0.5;
						else firstSelected++;
						if (firstSelected > customer.order.first) {
							selectedSet.remove(m);
							m.selected = false;
						}
					}
					if (m.type == this.getType(SelectedBox.SECOND)) {
						if (m.type.doubleWidth) secondSelected += 0.5;
						else secondSelected++;
						if (secondSelected > customer.order.second) {
							selectedSet.remove(m);
							m.selected = false;
						}
					}
					if (m.type == this.getType(SelectedBox.THIRD)) {
						if (m.type.doubleWidth) thirdSelected += 0.5;
						else thirdSelected++;
						if (thirdSelected > customer.order.third) {
							selectedSet.remove(m);
							m.selected = false;
						}
					}
				}
			}
		}
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
			if (holdSpiceOnNextTouch) {
				select(SelectedBox.SPICE);
				holdSpiceOnNextTouch = false;
			}
			else if (selectedBox == SelectedBox.SPICE) {
				justTappedSpice = false;
			}
			else {
				SelectedBox box = touchBox(KitchenScreen.getUnitX(x), KitchenScreen.getUnitY(y));
				//				System.out.println("first touch is " + box);
				if (box == SelectedBox.SPICE) {
					if (disableSpice()) return;
					this.justTappedSpice = true;
					this.select(box);
				}
				if (!ADVANCED_CONTROLS && box != SelectedBox.NONE) {
					if (TutorialEventHandler.shouldDisableBoxes()) return;
					this.select(box);
				}
			}
			firstTouch = false;
		}

		mousedOver = -1;

		//		if (onGrill(unit_x, unit_y)) {
		if (onGrillAbsolute(x, y)) {
			mousedOver = getGrillIndex(x, y);
			if (mousedOver < 0) System.out.println("GetGrillIndex is broken: moused over is " + mousedOver);
			//			System.out.println("Mousing over grill " + mousedOver + " at " + x + ", " + y);
		}

		if (onGrillArea(x, y) && !ADVANCED_CONTROLS) {
			if (meatBoxSelected())
				indexToPlaceMeatAt = getNextOpenSpot();
			//			else if (meatSelected() && canMoveSelected() && holdingAfterSelect) {
			//				indexToPlaceMeatAt = mousedOver;
			//			}
			else indexToPlaceMeatAt = -1;
		}
		else {
			indexToPlaceMeatAt = -1;
			//			if (selected )
			//			System.out.println("Not mousing over grill at " + x + ", " + y);
		}

		// if moused over grill and spice is selected then spice whatever is there
		if (mousedOver != -1 && meat[mousedOver] != null) {
			if (this.selectedBox == SelectedBox.SPICE) {
				dropSpice();
			}
			else if (this.selectedBox == SelectedBox.NONE || ((this.meatBoxSelected() || this.selectedBox == SelectedBox.BEER) && !placedWhileHeld && this.selectedBox != SelectedBox.BEER)) {
				if ((ADVANCED_CONTROLS || !meatBoxSelected()) && !meat[mousedOver].selected && !disableSelectingMeat()) {
					this.selectedBox = SelectedBox.NONE;
					//					selectedSet.add(meat[mousedOver]);
					//					holdingAfterSelect = true;
					selectMeat(mousedOver);
				}
			}
		}

		// draw ghost meat under mouse when dragging meat off grill
		// only if a meat from the grill is selected
		if ((meatSelected() || selectedBox == SelectedBox.BEER) && ks != null && ks.cm != null)
			ks.cm.updateMousedOver(x, y);
	}

	// For placing new meat. Returns -1 if no spot found
	public int getNextOpenSpot() {
		if (DROP_MEAT_UNDER_FINGER) {
			if (mousedOver >= 0) {
				if (canFitAt(mousedOver, getType(selectedBox))) {
					return mousedOver;
				}
			}
		}

		for (int i = 0; i < meat.length; i++) {
			if (canFitAt(i, getType(selectedBox))) return i;
		}

		return -1;
	}

	// gets next open spot that's not already selected (for moving)
	public int getNextOpenSpotNotSelected() {
		for (int i = 0; i < meat.length; i++) {
			if (meat[i] == null) {
				if (!getType(selectedBox).doubleWidth) return i;
				else if (i < meat.length - 1 && meat[i + 1] == null) return i;
			}
		}
		return -1;
	}

	public void release(int x, int y) {
		this.holding = false;
		//		this.holdingAfterSelect = false;

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

		// moused over grill
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
			else if (meatSelected() && shouldDrawGhostMeatForMove()) {
				moveSelectedTo(mousedOver);
				select(SelectedBox.NONE);
			}
			else if (meatSelected() && !holdingPastTap() && (justSelected < 0 || meat[mousedOver] != meat[justSelected]) && !open(mousedOver) && selectedSet.contains(meat[mousedOver])) {
				// remove it from selected if clicking it twice
				//				if (!canMoveSelectedTo(mousedOver)) {
				System.out.println("removing on release");

				// only remove on release if this isn't the first touch
				removeOnRelease(mousedOver);
				//					if (selectedSet.contains(meat[mousedOver]) && meatSelectedNotHeld) 
				//						selectedSet.remove(meat[mousedOver]);
				//				}
			}
			else if (selectedBox == SelectedBox.SPICE) {
				if (!open(mousedOver)) {
					if (!meat[mousedOver].spiced){
						dropSpice();
					}
					select(SelectedBox.NONE);
				}
			}
			if (ADVANCED_CONTROLS) {
				// drop fresh meat on grill
				if (mousedOver != -1 && this.meatBoxSelected()) {
					if (meat[mousedOver] == null && open(mousedOver)) {
						ks.dropMeatOnGrill(getType(selectedBox), mousedOver);
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
		else if (meatSelected() && ks.cm.mousedOver != null && ks.cm.mousedOver.order != null && !TutorialEventHandler.shouldDisableServe()) {
			ks.serveCustomerAll(ks.cm.mousedOver);
		}
		// drop beer on customers
		else if (selectedBox == SelectedBox.BEER && ks.cm.mousedOver != null && ks.cm.mousedOver.order != null) {
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
			meat[removeOnRelease].selected = false;
			removeOnRelease = -1;
		}


		//		if (hoverMove) {
		//			deselectAll();
		//		}

		// selectButNotHold
		int unit_x = KitchenScreen.getUnitX(x);
		int unit_y = KitchenScreen.getUnitY(y);
		SelectedBox box = touchBox(unit_x, unit_y);

		// no meat box was clicked, maybe deselect
		if (box == SelectedBox.NONE) {
			if (onGrillAbsolute(x, y)) {
				if (selectedSet.contains(meat[getGrillIndex(x, y)])) 
					this.meatSelectedNotHeld = true;
			}
			else {
				// don't deselect if just selected meat (unless trashing)
				if (!gaveBeerToCustomer && (meatSelectedNotHeld || hoverTrash)) {
					select(SelectedBox.NONE);
				}
			}
		}
		// releasing on box
		else {
			if (ADVANCED_CONTROLS) {
				// maybe select spice box, maybe deselect it if already selected
				if (box == SelectedBox.SPICE) {
					// if touched and released on spice box
					if (selectedBox != SelectedBox.SPICE) {
						// don't select Spice box if just deselected meat
						if (meatSelectedNotHeld || hoverTrash)
							select(box);
					}
					else if (!justTappedSpice) 
						select(SelectedBox.NONE);
				}
				else {
					select(box);
				}
			}
			else {
				if (box == SelectedBox.SPICE && selectedBox == SelectedBox.SPICE) {
					holdSpiceOnNextTouch = true;
				}
			}

			//								this.boxSelectedNotHeld = true;
			//			}
		}

		if (!ADVANCED_CONTROLS) {
			//			System.out.println(indexToPlaceMeatAt);
			if (indexToPlaceMeatAt >= 0) {
				if (!meatBoxSelected()) indexToPlaceMeatAt = -1;
				else ks.dropMeatOnGrill(getType(selectedBox), indexToPlaceMeatAt);
			}

			if (!meatSelected())
				select(SelectedBox.NONE);
			else 
				selectedBox = SelectedBox.NONE;
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
	private SelectedBox touchBox(int unit_x, int unit_y) {
		if (unit_x >= FIRST_X && unit_x < FIRST_X + BOX_HOR_WIDTH) {
			if (unit_y >= BOTTOM_Y && unit_y < BOTTOM_Y + BOX_HOR_HEIGHT){
				return SelectedBox.FIRST;
			}
		}
		if (unit_x >= SECOND_X && unit_x < SECOND_X + BOX_HOR_WIDTH) {
			if (unit_y >= BOTTOM_Y && unit_y < BOTTOM_Y + BOX_HOR_HEIGHT){
				if (boxes[1] != null)
					return SelectedBox.SECOND;
			}
		}
		if (unit_x >= THIRD_X && unit_x < THIRD_X + BOX_HOR_WIDTH) {
			if (unit_y >= BOTTOM_Y && unit_y < BOTTOM_Y + BOX_HOR_HEIGHT){
				if (boxes[2] != null)
					return SelectedBox.THIRD;
			}
		}

		if (unit_x >= BEER_X && unit_x < BEER_X + BEER_WIDTH) {
			if (unit_y >= BEER_Y && unit_y < BEER_Y + BEER_HEIGHT){
				return SelectedBox.BEER;
			}
		}

		if (unit_x >= grillRightX && unit_x < grillRightX + SPICE_WIDTH) {
			if (unit_y >= SPICE_Y && unit_y < SPICE_Y + SPICE_HEIGHT){
				return SelectedBox.SPICE;
			}
		}

		return SelectedBox.NONE;
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

	public boolean isMeat(SelectedBox selectedBox) {
		return this.selectedBox == SelectedBox.FIRST || this.selectedBox == SelectedBox.SECOND || this.selectedBox == SelectedBox.THIRD;
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

	private int getLiftedYForIndex(int i) {
		return  (int) (KitchenScreen.UNIT_HEIGHT * (GRILL_Y + grillLeftY + LIFTED_MEAT));
	}

	public void drawMeat(SpriteBatch batch) {
		for (Meat m : meat) {
			if (m == null) continue;
			int x = getXForIndex(m.index1);
			int y = getYForIndex(m.index1);

			if (m.selected) {
				if (drawingFloatingSelected()) continue;
				y = getLiftedYForIndex(m.index1);
			}

			if (selectedSet.contains(m) && shouldDrawGhostMeatForMove()) continue;

			m.draw(batch, x, y, profile);
		}

		// draw ghost meat
		if (meatBoxSelected() && indexToPlaceMeatAt >= 0 && !ADVANCED_CONTROLS) { // !this.selectedSet.contains(meat[mousedOver])) {
			batch.setColor(1, 1, 1, .6f);
			TextureRegion toDraw = null;

			KebabTypes.Type ghostType = getType(this.selectedBox);
			toDraw = Assets.getMeatTexture(ghostType, Meat.State.RAW, false);

			if (toDraw != null) { 
				Meat.draw(batch, toDraw, ghostType.doubleWidth, getXForIndex(indexToPlaceMeatAt), getYForIndex(indexToPlaceMeatAt), profile, 1);
			}
			batch.setColor(1, 1, 1, 1);
		}
		else if (meatSelected() && shouldDrawGhostMeatForMove() && !ADVANCED_CONTROLS) { // !this.selectedSet.contains(meat[mousedOver])) {
			batch.setColor(1, 1, 1, .6f);
			TextureRegion toDraw = null;

			int currentIndex = mousedOver;
			for (Meat m : selectedSet) {
				KebabTypes.Type ghostType = m.type;
				toDraw = Assets.getMeatTexture(ghostType, m.state, false);
				if (toDraw != null) { 
					Meat.draw(batch, toDraw, ghostType.doubleWidth, getXForIndex(currentIndex), getYForIndex(currentIndex), profile, 1);
				}
				currentIndex++;
				if (m.type.doubleWidth) currentIndex++;
			}
			batch.setColor(1, 1, 1, 1);
		}
		//		if (meatSelected() && indexToPlaceMeatAt >= 0 && !ADVANCED_CONTROLS) {
		//			
		//		}
	}

	// drops meat at mousedOver index
	public Meat dropMeat(KebabTypes.Type type, int index) {
		Meat toDrop = new Meat(type, this);

		if (!canFitAt(index, toDrop)) return null;
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

	private boolean canFitAt(int index, KebabTypes.Type type) {
		if (!type.doubleWidth) 
			return meat[index] == null;
		else {
			return index + 1 < meat.length && (open(index)) && open(index + 1);
		}
	}

	// will this type of meat fit at mousedOver?
	//	private boolean canFit(Meat piece, int index) {
	//		return canFitAt(index, piece);
	//	}

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
	//	public void move() {
	//		if (!mousedOver()) throw new java.lang.IllegalArgumentException();
	//
	//		int grillIndex = mousedOver;
	//		int selectedIndex = 0;
	//		while (selectedIndex < selectedSet.size() && grillIndex < this.meat.length) {
	//			Meat current = selectedSet.get(selectedIndex);
	//			if (current == null) {
	//				selectedIndex++;
	//				continue;
	//			}
	//			if (this.canFitAt(grillIndex, current)) {
	//				moveFrom(current.index1, grillIndex);
	//				selectedIndex++;
	//			}
	//			grillIndex++;
	//		}
	//
	//		deselectAll();
	//	}

	public boolean shouldDrawGhostMeatForMove() {
		//		System.out.println("just selected: " + justSelected + " mousedOver: " + mousedOver);
		return canMoveSelected() && mousedOver >= 0 && canMoveSelectedTo(mousedOver);// && (justSelected < 0 || meat[mousedOver] != meat[justSelected]);
	}

	public void moveSelectedTo(int index) {
		// first set all previous locations to null
		for (int i = 0; i < meat.length; i++) {
			if (selectedSet.contains(meat[i])) {
				meat[i] = null;
			}
		}
		System.out.println("moving selected to " + index);

		// now relocate all selected meat
		int i = index;
		for (Meat m : selectedSet) {
			meat[i] = m;
			m.setIndex(i);
			i++;
			if (m.type.doubleWidth) {
				meat[i] = m;
				i++;
			}
		}

		deselectAll();
	}

	public boolean canMoveSelectedTo(int index) {
		//		if (index < 0) System.out.println("can't move to " + index);
		if (index < 0) return false;
		//		return false;
		//		return false;

		int size = 0; 
		for (Meat m : selectedSet) {
			size++;
			if (m.type.doubleWidth) size++;
		}

		if (index + size > meat.length) return false;

		// don't allow moving unless actually moving and maybe even have a timer

		// check each index and make sure unoccupied
		int overLapWithExisting = 0;
		for (int i = index; i < index + size; i++) {
			if (meat[i] != null) {
				if (selectedSet.contains(meat[i])) {
					overLapWithExisting++;
				}
				else return false;
			}
		}
		if (overLapWithExisting == size) return false;

		return true;
	}

	public boolean canMoveSelected() {
		return hoverMove;
	}

	//	public void moveFrom(int origIndex, int newIndex) {
	//		Meat toMove = meat[origIndex];
	//		if (toMove == null) return;
	//		// we null this out early because we don't want to null it at the end,
	//		// when it might actually have the moved meat
	//		meat[origIndex] = null;
	//
	//		if (toMove.type.doubleWidth) {
	//			meat[origIndex + 1] = null;
	//		}
	//
	//		meat[newIndex] = toMove;
	//		meat[newIndex].setIndex(newIndex);
	//
	//		if (toMove.type.doubleWidth) {
	//			meat[newIndex + 1] = meat[newIndex];
	//		}
	//	}

	public void deselectAll() {
		for (Meat m : selectedSet) {
			m.selected = false;
		}
		selectedSet.clear();

	}

	public boolean open(int index) {
		return (meat[index] == null);
	}

	public boolean meatBoxSelected() {
		return isMeat(selectedBox);
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
