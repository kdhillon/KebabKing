package com.chuanrchef.game;

import java.io.FileNotFoundException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.chuanrchef.game.Grill.Selected;
import com.chuanrchef.game.Meat.State;
import com.chuanrchef.game.Meat.Type;


public class TutorialScreen extends KitchenScreen {
	public static final boolean FORCE_TUTORIAL = false;

	static float WELCOME1_WAIT = 3f;
	static float WELCOME2_WAIT = 2f;
	static float TIME_TO_WAIT = 2f;
	boolean tutPause;
	
	Label currentText;

	Color GRAY = new Color(0.2f, 0.2f, 0.2f, 0.5f);
//	int lambCount;
	
	float timeWaited;
	float welcome1Wait;
	float welcome2Wait;
	
	enum Step{
		// woman first
		// make wait, clickchicken, clickgrill, clickLamb, swipegrill, clickspice, swipegrill, wait2, clickmean, clickwoman, wait3
		Welcome1, Welcome2, Wait1, ClickChicken, ClickGrill, ClickLamb, DragGrill, ClickSpice, ClickGrill3, Wait2, ClickMeat, ClickWoman, Wait3,
		
		//ClickLamb2, DragGrill, ClickSpice, DragGrill2, ClickBeer, ClickMan1, Wait4, ClickMeat2, ClickMan2
	}

	Step current;
	
	int meatReady;
//	Meat beef;
	Meat lamb1, lamb2, lamb3;
	Meat chicken;


	// A new Kitchen Screen is created every time the player starts a new day.
	// handles user input and the main render / update loop
	public TutorialScreen(ChuanrC master) {
		super(master);
		
		this.master.platformSpec.sendEventHit("Tutorial", "Start", "");
		
		System.out.println("STARTING TUTORIAL MODE");

		current = Step.Welcome1;
		currentText = new Label("Welcome to Kebab King!", Assets.generateLabelStyle(ChuanrC.getGlobalX(64.0f / 480)));
//		currentText.debug();
		// TODO fix this janky shit
		currentText.setSize(ChuanrC.getWidth(), ChuanrC.getGlobalY(1.0f/4));
		currentText.setPosition(0, (ChuanrC.getHeight() - currentText.getHeight())*0.7f);
		currentText.setAlignment(Align.center);
		currentText.setWrap(true);
		
		grill.tutorialMode = true;
		meatReady = 0;
//		
//		lambCount = 0;
		timeWaited = 0;
	}

	@Override
	public void render(float delta) {	
		super.render(delta);
		// prevent countdown
		this.time = 9999;

		// draw appropriate overlays
		// draw gray overlay
		batch.begin();

		if (tutPause) {
			
			Color c = batch.getColor();
			this.batch.setColor(GRAY);
			batch.draw(Assets.white, 0, 0, ChuanrC.getWidth(), ChuanrC.getHeight());
			this.batch.setColor(c);
			
			if (current == Step.ClickChicken)
				grill.tutDrawChickenBox(batch, false);
			else if (current == Step.ClickGrill) {
				grill.tutDrawChickenBox(batch, true);
				grill.tutDrawGrill(1, batch);
				grill.tutDrawGrill(2, batch);
				if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
					grill.holdInput(Gdx.input.getX(), Gdx.input.getY());
				}
				else {
					grill.mousedOver = -1;
//					grill.mousedOverTrash = false;
				}
			}
			else if (current == Step.ClickLamb) {
				grill.tutDrawLambBox(batch, false);
			}
			else if (current == Step.DragGrill) {
				grill.tutDrawLambBox(batch, true);
				grill.tutDrawGrill(3, batch);
				grill.tutDrawGrill(4, batch);
				grill.tutDrawRightGrill(batch);
				grill.drawMeat(batch);
				if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
					grill.holdInput(Gdx.input.getX(), Gdx.input.getY());
				}
				else {
					grill.mousedOver = -1;
//					grill.mousedOverTrash = false;
				}
			}
			else if (current == Step.ClickSpice) {
				grill.tutDrawSpiceBox(batch, false);
				
				if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
					grill.holdInput(Gdx.input.getX(), Gdx.input.getY());
				}
				else {
					grill.mousedOver = -1;
//					grill.mousedOverTrash = false;
				}
			}
			else if (current == Step.ClickGrill3) {
				grill.tutDrawGrill(3, batch);
				grill.tutDrawGrill(4, batch);
				grill.tutDrawRightGrill(batch);
				grill.drawMeat(batch);
				
				if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
					grill.holdInput(Gdx.input.getX(), Gdx.input.getY());
				}
				else {
					grill.mousedOver = -1;
//					grill.mousedOverTrash = false;
				}
				
				if (lamb1.spiced && lamb2.spiced && lamb3.spiced) {
					transitionToNext();
				}
				
			}
			else if (current == Step.ClickMeat) {
				grill.tutDrawGrill(1, batch);
				grill.tutDrawGrill(2, batch);
				grill.tutDrawGrill(3, batch);
				grill.tutDrawGrill(4, batch);
				grill.drawMeat(batch);

				if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
					grill.holdInput(Gdx.input.getX(), Gdx.input.getY());
				}
				else {
					grill.mousedOver = -1;
//					grill.mousedOverTrash = false;
				}
				
				if (grill.selectedSet.contains(lamb1) && grill.selectedSet.contains(lamb2) && grill.selectedSet.contains(lamb3) && grill.selectedSet.contains(chicken)) {
					transitionToNext();
				}
			}
			else if (current == Step.ClickWoman) {
				cm.draw(batch);
				cm.updateMousedOver(Gdx.input.getX(), Gdx.input.getY());
				if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
					grill.holdInput(Gdx.input.getX(), Gdx.input.getY());
				}
				else {
					grill.mousedOver = -1;
//					grill.mousedOverTrash = false;
				}
			}
//			else if (current == Step.ClickLamb2) {
//				grill.tutDrawLambBox(batch, false);
//			}
//			else if (current == Step.DragGrill) {
////				grill.tutDrawLeftGrill(batch);
//				grill.tutDrawGrill(1, batch);
//				grill.tutDrawGrill(2, batch);
//				grill.tutDrawGrill(3, batch);
//				grill.tutDrawGrill(4, batch);
//				grill.tutDrawRightGrill(batch);
				
//				if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
//					grill.holdInput(Gdx.input.getX(), Gdx.input.getY());
//				}
//				else {
//					grill.mousedOver = -1;
//					grill.mousedOverTrash = false;
//				}
//			}
		}
		if (current == Step.Welcome1) {
			welcome1Wait += delta;
			if (welcome1Wait > WELCOME1_WAIT) {
				this.transitionToNext();
			}
		}
		if (current == Step.Welcome2) {
			welcome2Wait += delta;
			if (welcome2Wait > WELCOME2_WAIT) {
				this.transitionToNext();
			}
		}
		if (current == Step.Wait2) {
			if (lamb1.state == State.COOKED && lamb2.state == State.COOKED && lamb3.state == State.COOKED && chicken.state == State.COOKED) {
				this.transitionToNext();
			}
		}
		if (current == Step.Wait3) {
			this.timeWaited += delta;
			if (timeWaited > TIME_TO_WAIT) {
				this.transitionToNext();
			}
		}
		
		
		// always draw text
//		currentText.drawDebug(shapes);
		if (currentText != null) {
			currentText.draw(batch, 1);
		}
		else {
			System.out.println("current text is null");
		}
		batch.end();
	}

	@Override
	// actually run a game loop
	public void update(float delta) {	
		// force pause if you want

		if (!tutPause) {
			super.update(delta);
		}
	}
	
	@Override
	public Meat dropMeatOnGrill(Type type) {
		Meat m = super.dropMeatOnGrill(type);
//		if (m != null && this.current == Step.ClickGrill && type == Type.BEEF) {
//			this.transitionToNext();
//			beef = m;
//		}
		if (m != null && this.current == Step.ClickGrill && type == Type.CHICKEN) {
			this.transitionToNext();
			chicken = m;
		}
		if (m != null && this.current == Step.DragGrill && type == Type.LAMB) {
			if (lamb1 == null) {
				lamb1 = m;
			}
			else if (lamb2 == null) {
				lamb2 = m;
			}
			else if (lamb3 == null) {
				lamb3 = m;
			}
//			this.transitionToNext();
//			lamb1 = m;
		}
		if (lamb3 != null) {
			this.transitionToNext();
		}
		return m;
	}

	public void transitionToNext() {
		
		switch(current) {
		case Welcome1:
			currentText.setText("Welcome to Kebab King!");
			break;
		case Welcome2:
			currentText.setText("Let's learn how to cook!");
			break;
		case Wait1:
			tutPause = true;
			currentText.setText("Tap the chest to grab some Chicken!");
			break;
		case ClickChicken:
			
			currentText.setText("Tap the grill to start cooking!");
			break;
		case ClickGrill:
			grill.selected = Selected.NONE;
			currentText.setText("Now grab some Lamb!");
			break;
		case ClickLamb: 

			currentText.setText("Tap or slide your finger on the grill!");
			break;
		case DragGrill:
			grill.selected = Selected.NONE;

			currentText.setText("Red means spicy! Grab some Spice!");
			grill.selectedSet.clear();

			break;
		case ClickSpice:
			
			currentText.setText("Tap or slide the Lamb to spice them!");
			break;
		case ClickGrill3:
			grill.selected = Selected.NONE;

			currentText.setText("Awesome! Now we wait...");
			this.tutPause = false;
			grill.disableTouch = true;
			
			grill.selectedSet.clear();
			break;
		case Wait2:
			currentText.setText("Select the kebabs!");
			this.tutPause = true;
			grill.disableTouch = false;

			break;
		case ClickMeat:
			currentText.setText("Now tap the hungry customer!");
			break;
		case ClickWoman:
			// for now, just finish day.
			
			currentText.setText("Great job!");
			this.tutPause = false;
			break;
		case Wait3:
			this.finishDay();
			return;
//			currentText.setText");
//			this.tutPause = true;
//			break;
//		case ClickLamb2: 
//
//			currentText.setText("Drag your finger to drop multiple kebabs!");
//			break;
//		case DragGrill:
//
//			currentText.setText("The red number means he wants them spicy. Grab the spice brush!");
//			break;
//		case ClickSpice: 
//
//			currentText.setText("Slide over the meat to brush on some spice!");
//			break;
//		case DragGrill2: 
//
//			currentText.setText("Awesome! Now, while we wait for the meat to cook, grab a beer from the cooler!");
//			break;
//		case ClickBeer: 
//
//			currentText.setText("And give it to the thirsty customer!");		
//			break;
//		case ClickMan1:
//
//			currentText.setText("");
//			this.tutPause = false;
//			break;
//		case Wait4:
//
//			currentText.setText("Swipe all the kebabs to grab them!");			
//			this.tutPause = true;
//			break;
//		case ClickMeat2:
//			
//			currentText.setText("Now serve the customer and make some cash!");
//			break;
//		case ClickMan2:
//
//			finishDay();
//			break;
		}

		// increment step
		int currentIndex = -1;
		for (int i = 0; i < Step.values().length; i++) {
			if (Step.values()[i] == current) currentIndex = i;
		}
		this.current = Step.values()[currentIndex+1];
		System.out.println("Switching to step: " + current.toString());
	}

	public void tutPause() {
		this.tutPause = true;
	}

	public void tutUnpause() {
		this.tutPause = false;
	}

	@Override
	public void finishDay() {			
		super.finishDay();
		this.master.platformSpec.sendEventHit("Tutorial", "End", "");

		master.profile.tutorialNeeded = false;
		try {
			master.save();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
