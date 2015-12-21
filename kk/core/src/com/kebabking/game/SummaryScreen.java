package com.kebabking.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class SummaryScreen extends ScreenTemplate {
	final static Color RED = new Color(193/256.0f, 39/256.0f, 45/256.0f, 1f);
	
	// Time to display "Day Complete"
	static float TIME_TO_WAIT = 2f;

	static final float RED_BAR_HEIGHT = 0.015f;
	static final float NEG_PAD = 0.002f;
	
	Stage uiStage;

	KitchenScreen kitchen;

	Background bg; 
	Grill grill;
	CustomerManager cm;

	SpriteBatch batch;

	Table bigTable;

	float timeElapsed;
	Label dayComplete;

	float revenue;
	float expenses;
	float meatExpenses;
	float rent;
	float profit;
	int customersServed;
	int sickCount;
	float rating; // between 0.5 and 5.0
	
	// in case we want to have stars pop on.
	Table starTable;
	float currentStarCount;

	public SummaryScreen(KitchenScreen kitchen) {
		this.batch = kitchen.batch;
		this.bg = kitchen.bg;
		this.cm = kitchen.cm;
		this.kitchen = kitchen;
		this.grill = kitchen.grill;
		grill.deactivate(); // deactivate grill
		cm.deactivate();
		bg.deactivate();

		dayComplete = new Label("Day Complete", Assets.generateLabelStyleUIChina(50));
		if (kitchen.wasShutDown)
			dayComplete.setText("You got shut down! Obey the Health Code!");
		dayComplete.setPosition(0, KebabKing.getGlobalY(0.45f));
		dayComplete.setWidth(KebabKing.getWidth());
		dayComplete.setAlignment(Align.center);
		dayComplete.setWrap(true);

		this.revenue = kitchen.moneyEarnedToday;
		//		this.expenses = kitchen.moneySpentToday - refund;
		this.expenses = kitchen.moneySpentToday;
		
		// TODO this is legendary but a bit problematic
		this.rent = kitchen.master.profile.getLocation().getDailyCost();
		// TODO make sure to catch negative money case (pay to play)
		this.meatExpenses = kitchen.moneySpentToday - rent;

		// update profit
		//		this.profit =  master.profile.cash - getProfile().cash - rent;
		this.profit = revenue - expenses - rent;

		// customers served:
		this.customersServed = kitchen.cm.totalCustomers;

		// sicknesses
		this.sickCount = cm.totalSick;

		// save rating
		this.rating = kitchen.calculateReputation();


		initializeTable();
	}
	
	public void initializeTable() {
		ScreenViewport viewport = new ScreenViewport();
		uiStage = new Stage(viewport, batch);
		bigTable = new Table();
//		bigTable.debugAll();
		bigTable.setSize(KebabKing.getWidth(), KebabKing.getHeight());
		
		uiStage.addActor(bigTable);
		
		float topBarPadY = KebabKing.getGlobalY(0.02f);
		
		Table window = new Table();
		window.setBackground(new TextureRegionDrawable(Assets.getTextureRegion("whitepixel")));
		bigTable.add(window).width(KebabKing.getGlobalX(0.8f)).height(KebabKing.getGlobalY(0.65f));
		
		Table topBar = generateTopTable();
		window.add(topBar).top().expandX().fillX().padBottom(topBarPadY).padTop(topBarPadY);
		window.row();
		
		float infoPadBetween = KebabKing.getGlobalY(0.005f);
		Table infoTable = new Table();
		Table revenueTable = generateTable("Revenue", revenue, true, new String[]{}, new float[]{});
		infoTable.add(revenueTable).expandX().fillX().padBottom(infoPadBetween).colspan(2);
		infoTable.row();
		Table expenseTable = generateTable("Expenses", expenses, true, new String[]{"Meat", "Rent"}, new float[]{meatExpenses, rent});
		infoTable.add(expenseTable).expandX().fillX().padBottom(infoPadBetween).colspan(2);
		infoTable.row();
		Table customerTable = generateTable("Customers", customersServed, false, new String[]{"Sick"}, new float[]{sickCount});
		infoTable.add(customerTable).expandX().fillX().padBottom(infoPadBetween).colspan(2);
		infoTable.row();
		
		Table rep = generateReputationTable();
//		rep.debugAll();
		infoTable.add(rep).expandX().fillX().fillY().padRight(infoPadBetween);
		Table prof = generateProfitTable();
//		prof.debugAll();
		infoTable.add(prof).expandX().fillX();
		
		window.add(infoTable).top().width(KebabKing.getGlobalX(0.6f)).expandY();
		window.row();
		
		Table jadeTable = generateJadeTable();
		float width = KebabKing.getGlobalX(0.7f);
		float height = width * Assets.jadeBox.getRegionHeight() / Assets.jadeBox.getRegionWidth();
		float jadePad = KebabKing.getGlobalY(0.04f);
		window.add(jadeTable).expandY().width(width).height(height).padTop(jadePad).padBottom(jadePad);
	
		// new lines give this extra volume for clicking (hacky)
		Label nextRound = new Label("\nnext round >>\n", Assets.generateLabelStyleUIRed(30));
		nextRound.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
				clickNextRound();
			}
		});	
		
		bigTable.row();
		bigTable.add(nextRound).bottom().padTop(KebabKing.getGlobalY(0.00f));
	}
	
	public Table generateTopTable() {
		Table topBar = new Table();
//		topBar.debugAll();
		Table topLeft = new Table();
		Table topRight = new Table();
		topLeft.setBackground(new TextureRegionDrawable(Assets.red));
		topRight.setBackground(new TextureRegionDrawable(Assets.red));
		
		Table topCenter = new Table();
		
		TextureRegion topImage = Assets.getTextureRegion("screens/Summary-02");
		float IMAGE_WIDTH = 0.2f;
		float IMAGE_HEIGHT = IMAGE_WIDTH * topImage.getRegionHeight() / topImage.getRegionWidth();
		Image image = new Image(topImage);
//		image.setWidth(KebabKing.getGlobalX(IMAGE_WIDTH));
//		image.setHeight(KebabKing.getGlobalY(IMAGE_HEIGHT));
		topCenter.add(image).width(KebabKing.getGlobalX(IMAGE_WIDTH)).height(KebabKing.getGlobalX(IMAGE_HEIGHT));
		topCenter.row();
		Label dailyAccounts = new Label("DAILY ACCOUNTS", Assets.generateLabelStyleUI(14));
	
		topCenter.add(dailyAccounts);
		topBar.add(topLeft).expandX().fillX().height(KebabKing.getGlobalY(RED_BAR_HEIGHT));
		
		float imagePadX = KebabKing.getGlobalX(0.03f);
		topBar.add(topCenter).top().padLeft(imagePadX).padRight(imagePadX);;;
		topBar.add(topRight).expandX().fillX().height(KebabKing.getGlobalY(RED_BAR_HEIGHT));;
		return topBar;
	}
	
	public Table generateTable(String mainLabel, float mainValue, boolean money, String[] labels, float[] values) {
		if (labels.length != values.length) throw new java.lang.AssertionError();
		
		Table table = new Table();
		table.setBackground(new NinePatchDrawable(Assets.gray9PatchSmall));
		
		Label title = new Label(mainLabel, Assets.generateLabelStyleUIHeavy(14));		
		String toPrint;
		if (money) toPrint = "$" + mainValue;
		else toPrint = "" + (int) mainValue;
		Label value = new Label(toPrint, Assets.generateLabelStyleUIHeavy(14));
		table.add(title).left().expandX();
		table.add(value).right().expandX();
		
		float indentPad = KebabKing.getGlobalX(0.1f);
		for (int i = 0; i < labels.length; i++) {
			table.row();
			Label lab = new Label(labels[i], Assets.generateLabelStyleUILight(14));
			if (money) toPrint = "$" + values[i];
			else toPrint = "" + (int) values[i];
			Label val = new Label(toPrint, Assets.generateLabelStyleUILight(14));
			table.add(lab).left().expandX().padLeft(indentPad);
			table.add(val).right().expandX();
		}
		return table;
	}
	
	public Table generateReputationTable() {
		Table table = new Table();
		table.setBackground(new NinePatchDrawable(Assets.gray9PatchSmall));
		
		float negPadY = -KebabKing.getGlobalY(NEG_PAD);
		
		Label reputationLabel = new Label("REPUTATION", Assets.generateLabelStyleUIGray(14));
		table.add(reputationLabel).center().padTop(negPadY);
		table.row();

		starTable = new Table();
		table.add(starTable).width(KebabKing.getGlobalX(0.27f)).center().padBottom(negPadY);		
		
		updateStarTable(this.rating);
		
		return table;
	}
	
	public void updateStarTable(float starCount) {
		if (currentStarCount != starCount) {
			starTable.clear();
			
			// calculate number of gray stars
			int grayStars = (int) (5 - starCount);
					
			// star width is determined by size of star area
			float starHeight = KebabKing.getGlobalY(DrawUI.STAR_HEIGHT);
			
			float rightPad = KebabKing.getGlobalX(DrawUI.STAR_PAD);
			float leftPad = KebabKing.getGlobalX(DrawUI.STAR_PAD);
			
			while (starCount >= 1) {
				Image star = new Image(Assets.getStar());
				starTable.add(star).height(starHeight).padRight(rightPad).padLeft(leftPad).expandX();
				starCount--;
			}
			
			// draw half star
			if (starCount > 0) {
				Image star = new Image(Assets.getHalfStar());
				starTable.add(star).height(starHeight).padRight(rightPad).padLeft(leftPad).expandX();
			}
			
			// draw gray stars
			for (int i = 0; i < grayStars; i++) {
				Image star = new Image(Assets.getGrayStar());
				starTable.add(star).height(starHeight).padRight(rightPad).padLeft(leftPad).expandX();
			}
			
			currentStarCount = starCount;
		}
	}
	
	public Table generateProfitTable() {
		Table table = new Table();
		
		table.setBackground(new NinePatchDrawable(Assets.red9PatchSmall));
		
		float negPadY = -KebabKing.getGlobalY(NEG_PAD);
		
		Label profitLabel = new Label("PROFIT", Assets.generateLabelStyleUIRed(12));
		table.add(profitLabel).center().padTop(negPadY);
		table.row();

		Label valueLabel = new Label("$" + this.profit, Assets.generateLabelStyleUIRed(24));
		table.add(valueLabel).center().padBottom(negPadY);
		
		return table;
	}
	
	public Table generateJadeTable() {
		Table table = new Table();
		table.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
				transitionToAd();
			}
		});	
		
		table.setBackground(new TextureRegionDrawable(Assets.jadeBox));
		
		Image playButton = new Image(Assets.getTextureRegion("market/Market_menu_element-09"));
		float playWidth = KebabKing.getGlobalX(0.06f);
		table.add(playButton).width(playWidth).height(playWidth).bottom().expandY().padBottom(KebabKing.getGlobalY(0.003f));
		table.row();
		
		Label earnJade = new Label("\nEARN JADE\n", Assets.generateLabelStyleUIChinaWhite(28));
		table.add(earnJade).bottom().padBottom(KebabKing.getGlobalY(0.015f));
		return table;
	}
	
	public void clickNextRound() {
		System.out.println("Transitioning to next round");
		kitchen.master.summaryToMain(); // change later
	}
	
	public void transitionToAd() {
		System.out.println("Transitioning to ads");
	}

	public void render(float delta) {
		update(delta);

		batch.begin();
		bg.draw(batch);
		cm.draw(batch);
		grill.draw(batch);
		DrawUI.drawGray(batch);	
		DrawUI.drawFullUI(delta, batch, getProfile());

		if (timeElapsed <= TIME_TO_WAIT) {
			dayComplete.draw(batch, 1);			
		}

		batch.end();

		if (timeElapsed > TIME_TO_WAIT)
			uiStage.draw();
	}


	// actually run a game loop
	public void update(float delta) {	
		this.timeElapsed += delta;
		uiStage.act();
		bg.act(delta);
		cm.act(delta);
	}
	
	
	public Profile getProfile() {
		return kitchen.master.profile;
	}
	

	@Override
	public void show() {
		super.show();
		// TODO Auto-generated method stub
		DrawUI.setInput(uiStage);
	}
}
