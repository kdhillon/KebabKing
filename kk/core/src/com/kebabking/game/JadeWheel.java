package com.kebabking.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class JadeWheel extends Group {
	static final float DECEL = 12f;
	static final float START_SPEED = 100f;
	static final float EPSILON = 0.1f; // if this close to a boundary, push it a bit

	static final int LOW = 1;
	static final int MED = 2;
	static final int HIGH = 3;
	static final int SILVER = 5;
	static final int GOLD = 10;
	static final int JACKPOT = 50;

	static final int[] degrees = {5, 30, 45, 60, 75, 90, 105, 120, 135, 150, 180};
	static final int[] rewards = {JACKPOT, MED, LOW, HIGH, LOW, MED, LOW, MED, GOLD, MED, LOW};
	
//	static final float[] degrees = {2.5f, 7.5f, 15, 22.5f, 30, 37.5f, 45, 52.5f, 60, 67.5f, 75, 82.5f, 90, 
//									97.5f, 105, 112.5f, 120, 127.5f, 135, 142.5f, 150, 157.5f, 165, 172.5f, 177.5f};
//	static final int[] rewards = {JACKPOT, LOW, HIGH, MED, LOW, SILVER, LOW, MED, HIGH, GOLD, HIGH, MED, LOW,
//									MED, HIGH, MED, LOW, SILVER, LOW, MED, HIGH, GOLD, HIGH, MED, LOW};

	
	static Label[] labels;

	
	boolean waitingForSpin;
	boolean spinning;
	float currentSpeed;
	float currentRot;

	float x, y, width, height, jadeWidth, jadeHeight;
	
	int finalIndex;

	Group group;
	JadeWheelScreen wheelScreen;

	public JadeWheel(float x, float y, float size, JadeWheelScreen wheelScreen) {
		this.x = x;
		this.y = y;
		this.width = size;
		this.height = size/2;
		this.wheelScreen = wheelScreen;
		finalIndex = -1;
		
		jadeHeight = KebabKing.getGlobalYFloat(0.06f);
		jadeWidth = jadeHeight * Assets.bigjade.getRegionWidth() / Assets.bigjade.getRegionHeight();
		
		if (rewards.length != degrees.length) throw new java.lang.AssertionError();
		
		labels = new Label[rewards.length * 2];

		group = new Group();
		group.setPosition(x + width/2, y);
		//		text.setSize(width, height);

		//    	float offsetRot = 5;
		float offset = height * 0.9f;
		//    	text.setRotation(50);
		for (int i = 0; i < degrees.length * 2; i++) {
			String toDraw = LanguageManager.localizeNumber(rewards[i % degrees.length]);
			Group smallGroup = new Group();

			float add = 0;
			if (i >= degrees.length) add = 180;

			float firstRot = degrees[(i) % degrees.length];
			int secondIndex = i - 1;
			if (secondIndex < 0) secondIndex = degrees.length - 1;
			float secondRot = degrees[secondIndex % degrees.length];    		
			if (secondRot > firstRot) firstRot += 180;
//			System.out.println("i: " + i + " first: " + firstRot + " second: " + secondRot);
			float rotOffset = (firstRot - secondRot) / 2;

//			System.out.println("rot offset: " + (int) rotOffset);

			Label label;
//			if (rewards[i % degrees.length] == JACKPOT) {
			label = new Label(toDraw, Assets.generateLabelStyleUI((int) rotOffset  + 12, toDraw));
			////				label.setColor(Color.WHITE);
			if (rewards[i % rewards.length] == JACKPOT || rewards[i % rewards.length] == SILVER || rewards[i % rewards.length] == GOLD) 
				label.setColor(Color.WHITE);
			else
				label.setColor(MainStoreScreen.FONT_COLOR);
//
//			}
//			else {
//				label = new Label(toDraw, Assets.generateLabelStyleUILight((int) rotOffset * 2 + 8, toDraw));
//				label.setColor(MainStoreScreen.FONT_COLOR);
//			}
//			
			labels[i] = label;

			smallGroup.setPosition(-label.getPrefWidth()/2, offset + -label.getPrefHeight() / 2);
			smallGroup.setOrigin(label.getPrefWidth()/2, -offset + label.getPrefHeight() / 2);
			smallGroup.addActor(label);
//			System.out.println("pref height: " + label.getPrefWidth());

			smallGroup.rotateBy(-(degrees[secondIndex % degrees.length] + add + rotOffset));

			group.addActor(smallGroup);
		}
		currentRot = (float) (Math.random() * 360);
		
		this.addActor(group);;
		wheelScreen.uiStage.addActor(this);
//		currentRot = 181f;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (waitingForSpin || spinning || finalIndex >= 0) {
			batch.draw(Assets.halfWheel, x, y, width/2, 0, width, height, 1, 1, -currentRot);
			batch.draw(Assets.halfWheel, x, y-1, width/2, 1, width, height, 1, 1, -currentRot + 180);
			group.setRotation(-currentRot);
			group.draw(batch, 1);
			float pointerWidth = KebabKing.getGlobalXFloat(0.05f);
			float pointerHeight = pointerWidth * Assets.wheelPointer.getRegionHeight() / Assets.wheelPointer.getRegionWidth();
			batch.draw(Assets.wheelPointer, x + width/2 - pointerWidth / 2, y + height - pointerHeight * 0.5f, pointerWidth, pointerHeight);
			batch.draw(Assets.bigjade, x + width / 2 -jadeWidth/2, y-jadeHeight/2, jadeWidth/2, jadeHeight/2, jadeWidth, jadeHeight, 1, 1, -currentRot*0);
		}
	}

	public void update(float delta, boolean ff) {
		if (ff) delta *= 8;
		group.act(delta);

		if (spinning) {
			currentSpeed -= delta * DECEL;
			currentRot += currentSpeed * delta;

			currentRot = currentRot % 360;
			//    		System.out.println("rotation: " + currentRot);
			//    		System.out.println("speed: " + currentSpeed);
			if (currentSpeed <= 0) {
				endSpin();
			}
		}
		else if (finalIndex >= 0) {
			if (!labels[finalIndex].hasActions()) {
//				labels[finalIndex].addAction(Actions.forever(Actions.sequence(Actions.fadeIn(1), Actions.fadeOut(1))));
//				if (rewards[finalIndex] == JACKPOT) 
//					labels[finalIndex].addAction(Actions.forever(Actions.sequence(Actions.color(Color.WHITE, 1), Actions.color(MainStoreScreen.FONT_COLOR, 1))));
//				else
					labels[finalIndex].addAction(Actions.forever(Actions.sequence(Actions.color(Color.WHITE, 1), Actions.color(MainStoreScreen.FONT_COLOR, 1))));

//				labels[finalIndex].addAction(Actions.fadeOut(1));
				System.out.println("Added fade action");
			}
		}
	}
	
	public void startWaitingForSpin() {
		this.waitingForSpin = true;
	}

	public void startSpin() {
		System.out.println("starting spin");
		this.waitingForSpin = false;
		this.spinning = true;
		this.currentSpeed = START_SPEED + (float) (Math.random() * (START_SPEED / 2));
	}

	public void endSpin() {
		this.spinning = false;
//		this.currentRot += 180;
//		this.currentRot %= 360;

		// if within some delta of a boundary, push forward by a small amount to remove ambiguity.
		for (int i = 0; i < degrees.length; i++) {
			float diff = this.currentRot % 180 - degrees[i];
			if (diff >= 0 && diff < EPSILON) {
				// push forward a bit
				currentRot += EPSILON;
			}
			else if (diff < 0 && -diff < EPSILON) {
				// push backwards a bit
				currentRot -= EPSILON;
			}
		}
		// do it again for 0
		if (currentRot == 0) {
			// push forward a bit
			currentRot += EPSILON;
		}
		
		if (currentRot < 0) {
			currentRot += 360;
		}
		
		// this helps catch the edge case of jackpot < 5
		finalIndex = 0; 
		for (int i = 0; i < degrees.length; i++) {
			if (180 - (currentRot % 180) < degrees[i]) {
				finalIndex = i;
				if (currentRot < 180) finalIndex += degrees.length;
				break;
			}
		}
		// jackpot case
		if (finalIndex == 0) {
			if (currentRot < 177.5 || currentRot > 357.5) {
				finalIndex = 25;
			}
		}
		
		System.out.println("final index: " + finalIndex);
		
		System.out.println("adjusted current rot: " + currentRot);
		wheelScreen.handleJadeWheelStopped();

//		System.out.println("Expected value: " + calcExpectedValue());
	}

	public int getReward() {
		return rewards[finalIndex % degrees.length];
	}

	// not accurate with jackpot
//	public float calcExpectedValue() {
//		float prevDegrees = 0;
//		float totalValue = 0;
//		for (int i = 0; i < degrees.length; i++) {
//			totalValue += rewards[i] * (degrees[i] - prevDegrees);
//			prevDegrees = degrees[i];
//		}
//		return totalValue / 180;
//	}
	
	public void reset() {
		waitingForSpin = false;
		spinning = false;
//		finalIndex = -1;
		//		for (Label l : labels) {
		labels[finalIndex].clearActions();
		if (rewards[finalIndex % rewards.length] == JACKPOT || rewards[finalIndex % rewards.length] == SILVER || rewards[finalIndex % rewards.length] == GOLD) 
			labels[finalIndex].setColor(Color.WHITE);
		else
			labels[finalIndex].setColor(MainStoreScreen.FONT_COLOR);

		//		}
		finalIndex = -1;
	}
}
