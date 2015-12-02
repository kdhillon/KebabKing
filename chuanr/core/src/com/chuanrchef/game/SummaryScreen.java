package com.chuanrchef.game;

import java.io.FileNotFoundException;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.chuanrchef.game.Managers.Manager;

// this screen summarizes your sales for the day
public class SummaryScreen extends ScreenTemplate  {
	static float TIME_TO_WAIT = 3f;
	static float TIME_TO_WAIT_POLICE = 4f;
	
	static float timeBetweenStars = 0.5f;
	static float timeBetweenLines = .4f;
	
	static int numberLines = 7;
	float lines[];
	boolean nots[];
	int counter;
//	static float line1 = 1;
//	static float line2 = line1 + timeBetweenLines;
//	static float line3 = line2 + timeBetweenLines;
//	static float line4 = line3 + timeBetweenLines;

	Stage uiStage;
	
	KitchenScreen kitchen;

	Background bg; 
	Grill grill;
	CustomerManager cm;

	SpriteBatch batch;

	Table table;
	//	float revenue;
	//	float expenses;

	float timeElapsed;
	float revenue;
	float expenses;
	float rent;
	float profit;
	int customersServed;
	int sickCount;
	float rating; // between 0.5 and 5.0
	float forceWait;
	float nextStar;
	float starsToDraw;
	boolean drawHalfStar; 
	
	float labelPad;
	
//	Label revenueL;
//	Label expensesL;
//	Label rentL;
//	Label profitL;
//	Label customersServedL;
//	Label sickCountL;
//	Label ratingL;
	
	Label revenueR;
	Label expensesR;
	Label rentR;
	Label profitR;
	Label customersServedR;
	Label sickCountR;
	Label ratingR;
	Table earnCoinsTable;
	Table starsTable;
	
	Label tapToContinue;
	
//	boolean not1, not2, not3, not4, tap;
	
	float time;
	
	boolean initialized;
	
	Label dayComplete;

	public SummaryScreen(KitchenScreen kitchen) {
		this.batch = kitchen.batch;
		this.bg = kitchen.bg;
		this.cm = kitchen.cm;
		this.kitchen = kitchen;

		timeElapsed = 0;
		
		dayComplete = new Label("Day Complete!", Assets.generateLabelStyle(ChuanrC.getGlobalX(60.f / 480)));
		if (kitchen.wasShutDown)
			dayComplete.setText("You got shut down! Obey the Health Code!");
		dayComplete.setPosition(0, ChuanrC.getGlobalY(0.45f));
		dayComplete.setWidth(ChuanrC.getWidth());
		dayComplete.setAlignment(Align.center);
		dayComplete.setWrap(true);
		
		// initialize variables used for displaying lines
		lines = new float[numberLines];
		lines[0] = TIME_TO_WAIT + 1;
		for (int i = 1; i < numberLines; i++) {
			lines[i] = lines[i-1] + timeBetweenLines;
		}
		nots = new boolean[numberLines + 1];
		
		ScreenViewport viewport = new ScreenViewport();
		uiStage = new Stage(viewport, batch);

		// No need to refund anymore. 
//		// refund any meat that's on the grill:
//		float refund = 0;
//		for (int i = 0; i < kitchen.grill.meat.length; i++) {
//			Meat meat = kitchen.grill.meat[i];
//			if (meat == null) continue;
//			if (meat.state == Meat.State.BURNT) continue;
//
//			refund += Meat.getBuyPrice(meat.type);
//		}
//		kitchen.master.profile.cash += refund;
//		System.out.println("refund: " + refund);
		
		// update revenue and expenses
		this.revenue = kitchen.moneyEarnedToday;
//		this.expenses = kitchen.moneySpentToday - refund;
		this.expenses = kitchen.moneySpentToday;
		
		// TODO this is legendary but a bit problematic
		this.rent = kitchen.master.profile.getLocation().rentCost;
		// TODO make sure to catch negative money case (pay to play)
		
		// update profit
//		this.profit =  master.profile.cash - getProfile().cash - rent;
		this.profit = revenue - expenses - rent;

		// customers served:
		this.customersServed = kitchen.cm.totalCustomers;

		// sicknesses
		this.sickCount = cm.totalSick;

		// save rating
		this.rating = kitchen.calculateReputation();
		
		this.forceWait = lines[numberLines-1] + (int) (rating + 0.5f) * timeBetweenStars + timeBetweenLines;
		this.nextStar = lines[numberLines-1] + timeBetweenStars;
		
		kitchen.master.profile.subtractDailyExpenses();
				
		// save to getProfile()
		getProfile().endDay();
//		getProfile().updateRepuation(kitchen.calculateReputation());
//		getProfile().cash += profit;
		getProfile().updateRepuation(kitchen.calculateReputation());

		// save here!
		try {
			kitchen.master.save();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.grill = kitchen.grill;
		grill.deactivate(); // deactivate grill
		this.cm = kitchen.cm;
		
		System.out.println("deactivating cm and bg");
		cm.deactivate();
		bg.deactivate();
		
		
		System.out.println("adding listener");
		uiStage.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
				transitionToMain();
			}
		});		
		
		Manager.analytics.sendEventHit("Day", "End", "Twice_Rating", (long) (2*rating));
		Manager.analytics.sendEventHit("Day", "End", "Profit_Rounded", (long) (profit));
		Manager.analytics.sendEventHit("Day", "End", "Expenses", (long) (expenses));
		Manager.analytics.sendEventHit("Day", "End", kitchen.master.profile.getLocation().name);
		
		initializeTable();
	}

	@Override
	public void render(float delta) {
		update(delta);

		batch.begin();
		bg.draw(batch);
		cm.draw(batch);
		grill.draw(batch);
		DrawUI.drawGray(batch);
//		DrawUI.drawStars(batch, getProfile());
//		DrawUI.drawMoney(batch, getProfile());
		DrawUI.drawFullUI(delta, batch, getProfile());

//		drawText();
		if (initialized)
			updateTable();

		if (time <= TIME_TO_WAIT) {
			dayComplete.draw(batch, 1);			
		}
		
		batch.end();
		
		if (time > TIME_TO_WAIT)
			uiStage.draw();
	
		
		time += delta;
	}

	// actually run a game loop
	public void update(float delta) {	
		this.timeElapsed += delta;
		uiStage.act();
		bg.act(delta);
		cm.act(delta);
	}

	
	public void initializeTable() {
		System.out.println("initializing summary table");
		
		this.table = new Table();
//		table.debugAll();
		
		
		table.setSize(ChuanrC.getGlobalX(3.0f / 4), ChuanrC.getGlobalY(3.0f / 4));
		table.setPosition((ChuanrC.getWidth() - table.getWidth())/2, (ChuanrC.getHeight() - table.getHeight())/2);
		table.setBackground(Assets.uiSkin.getDrawable("textbox_01"));
		table.top();
		
		uiStage.addActor(table);
		
		Label title = new Label("Day " + getProfile().daysWorked + ":", Assets.generateLabelStyle(60));
		table.add(title).colspan(2);
		
		labelPad = ChuanrC.getGlobalY(0.01f);
				
		addLabelLeft("Revenue:");
		revenueR = new Label("", Assets.generateLabelStyle(32));
		table.add(revenueR).padTop(labelPad).expandX().right();
		addLabelLeft("Expenses:");
		expensesR = new Label("", Assets.generateLabelStyle(32));
		table.add(expensesR).padTop(labelPad).expandX().right();
		addLabelLeft("Rent:");
		rentR = new Label("", Assets.generateLabelStyle(32));
		table.add(rentR).padTop(labelPad).expandX().right();
		
		
		addLabelLeft("Profit:");
		profitR = new Label("", Assets.generateLabelStyle(32));
		table.add(profitR).padTop(labelPad).expandX().right();
		addLabelLeft("Customers:");
		customersServedR = new Label("", Assets.generateLabelStyle(32));
		table.add(customersServedR).padTop(labelPad).expandX().right();
		addLabelLeft("Sick:");
		sickCountR = new Label("", Assets.generateLabelStyle(32));
		table.add(sickCountR).padTop(labelPad).expandX().right();
		addLabelLeft("Rating:");
//		ratingR = new Label("", Assets.generateLabelStyle(32));
//		table.add(ratingR).padTop(labelPad).expandX().right();
		starsTable = new Table();
		generateBlankStarsTable();
		table.add(starsTable).padTop(labelPad).expandX().right();
		
//		float coinSize = labelPad*4;
//		earnCoinsTable = new Table();
//		Image coin = new Image(Assets.getCoin());
//		Image coin2 = new Image(Assets.getCoin());
//		earnCoinsTable.add(coin).height(coinSize).width(coinSize).left();
//		Label earnCoins = new Label("  Earn Coins!  ", Assets.generateLabelStyle(32));
//		earnCoins.setAlignment(Align.center);
//		earnCoinsTable.add(earnCoins).center();
//		earnCoinsTable.add(coin2).height(coinSize).width(coinSize).right();
//		table.row();
//		table.add(earnCoinsTable).colspan(2);
		
		tapToContinue = new Label("", Assets.generateLabelStyle(32));
		table.row();
		table.add(tapToContinue).colspan(2).expandX().center();
		
		this.initialized = true;
		
		System.out.println("Table initialized");
	}
	
	private void regenerateStarsTable(float starCount) {
		starsTable.clear();
		
		// calculate number of gray stars
		int grayStars = (int) (5 - starCount);

		float starWidth = labelPad * 4;

		float starHeight = starWidth*1.1f;
		float rightPad = 0;

		while (starCount >= 1) {
			Image star = new Image(Assets.getStar());
			starsTable.add(star).height(starHeight).padRight(rightPad).expand();
			starCount--;
		}

		// draw half star
		if (starCount > 0) {
			Image star = new Image(Assets.getHalfStar());
			starsTable.add(star).height(starHeight).padRight(rightPad).expand();
		}

		// draw gray stars
		for (int i = 0; i < grayStars; i++) {
			Image star = new Image(Assets.getGrayStar());
			starsTable.add(star).height(starHeight).padRight(rightPad).expand();
		}
	}
	
	private void generateBlankStarsTable() {
//		starsTable.clear();
//		float starSize = labelPad * 4;
//		for (int i = 0; i < 5; i++) {
//			Image star1 = new Image(Assets.getGrayStar());
//			starsTable.add(star1).width(starSize).height(starSize);
//		}
		regenerateStarsTable(0);
	}
	
	private void addLabelLeft(String s) {
		table.row();
		Label l = new Label(s, Assets.generateLabelStyle(32));
		table.add(l).padTop(labelPad).expandX().left();
	}
	
	public void updateTable() {
		if (timeElapsed > lines[0] && !nots[0]) {
			revenueR.setText("$" + revenue);
			nots[0] = true;
//			counter++;
		}
		if (timeElapsed > lines[1] && !nots[1]) {
			expensesR.setText("$" + expenses);
			nots[1] = true;
		}
		if (timeElapsed > lines[2] && !nots[2]) {
			rentR.setText("$" + rent);
			nots[2] = true;
		}
		if (timeElapsed > lines[3] && !nots[3]) {
			profitR.setText("$" + profit);
			nots[3] = true;
		}
		if (timeElapsed > lines[4] && !nots[4]) {
			customersServedR.setText("" + customersServed);
			nots[4] = true;
		}
		if (timeElapsed > lines[5] && !nots[5]) {
			sickCountR.setText("" + sickCount);
			nots[5] = true;
		}
		if (timeElapsed > lines[6] && !nots[6]) {
			if (timeElapsed > nextStar && starsToDraw + 1 <= rating) {
				starsToDraw++;
				nextStar = nextStar + timeBetweenStars;
			}
					
			// if should draw half star
			if (timeElapsed > nextStar && (rating * 2) % 2 == 1) {
				starsToDraw += .5f;
			}
			
			regenerateStarsTable(starsToDraw);
//
//			String toDraw = "";
//			for (int i = 0; i < starsToDraw; i++) toDraw = toDraw + "X";
//			if (drawHalfStar) toDraw += "Y";
						
//			ratingR.setText(toDraw);
			
			
			if (starsToDraw + 1 > rating && timeElapsed > nextStar) {
				// this part is breaking this.
				nots[6] = true;
			}
		}
		if (timeElapsed > forceWait && !nots[7]) {
			tapToContinue.setText("Tap to Continue!");
			nots[7] = true;
		}
	}

	public void transitionToMain() {
		if (timeElapsed > forceWait) {
			kitchen.master.summaryToMain(); // change later
		}
	}

	@Override
	public void show() {
		super.show();
		// TODO Auto-generated method stub
		DrawUI.setInput(uiStage);
	}
	
	public Profile getProfile() {
		return kitchen.master.profile;
	}
	
	@Override
	public void dispose() {
		this.uiStage.dispose();
		super.dispose();
	}

}
