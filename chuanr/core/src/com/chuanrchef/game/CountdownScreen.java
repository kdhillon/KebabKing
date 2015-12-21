package com.chuanrchef.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
//import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class CountdownScreen extends ScreenTemplate  {
	static int COUNTDOWN_TIME = 0;
	static float GOAL_TIME = 0f;
	
	ChuanrC master;
	Background bg; 
	Grill grill;
	CustomerManager cm;
	
	DailyGoal goal;

	Stage uiStage;
	Table table;
	Label countdownLabel;
	Label goalTitle;
	Label goalLabel;
	Label rewardLabel;

	SpriteBatch batch;
	
	float goalCountdown;
	
	boolean countingDown;
	float countdown;
	int countdownInt;
	
	Color color;
	
	// first displays daily goal for a few seconds, then counts down to start round
	public CountdownScreen(ChuanrC master) {	
		this.master = master;
		this.bg = master.bg;
		this.grill = master.grill;
		this.cm = master.cm;

		this.batch = master.batch;
		
		
		
		ScreenViewport viewport = new ScreenViewport();
		uiStage = new Stage(viewport, batch);		
//		uiStage.setDebugAll(true);

		table = new Table();
		uiStage.addActor(table);
		table.setSize(ChuanrC.getWidth(), ChuanrC.getHeight());
		table.setPosition(0, 0);
		table.align(Align.center);
		table.align(Align.top);

		table.row();
		
		goal = new DailyGoal();
		
		this.countdownLabel = new Label("", Assets.generateLabelStyle(ChuanrC.getGlobalX(200.0f / 480)));
//		
//		goalTitle = new Label("Daily Goal:", Assets.generateLabelStyle(48));
//		table.add(goalTitle).bottom().expandY();
//		table.row();		
//		this.goalLabel = new Label(goal.toString(), Assets.generateLabelStyle(32));
//		table.add(goalLabel).bottom();
//		table.row();
//		this.rewardLabel = new Label("Reward: $" + goal.reward, Assets.generateLabelStyle(32));
//		table.add(rewardLabel).bottom().padBottom(ChuanrC.height*0.15f);

		this.goalCountdown = GOAL_TIME;
		this.countingDown = false;
		this.countdown = COUNTDOWN_TIME;

		// if tutorial mode, skip countdown
		if (master.profile.tutorialNeeded) {
			this.countdown = 0;
		}
		
		color = new Color(1, 1, 1, 1);
		
		master.cm.reset();
	}

	@Override
	public void render(float delta) {
		update(delta);

		batch.begin();
		bg.draw(batch);
		cm.draw(batch);
		grill.draw(batch);

//		DrawUI.drawStars(batch, getProfile());
//		DrawUI.drawMoney(batch, getProfile());
		DrawUI.drawFullUI(delta, batch, master.profile);
		batch.end();
		
		uiStage.draw();
	}

	// actually run a game loop
	public void update(float delta) {
		uiStage.act(delta);

		if (countingDown) {
			bg.act(delta);
			cm.act(delta);
			
			color.set(1, 1, 1, countdown - (int) countdown);
			
			countdownLabel.setColor(color);
			this.countdownLabel.setText("" + (int) (countdown + 1));
		}
		
//		System.out.println("goalCountdown: " + goalCountdown + ", countdown: " + countdown);
		
		// draw goal
		if (!this.countingDown) {
			this.goalCountdown -= delta;
			if (this.goalCountdown <= 0) {
				table.clear();
				table.add(countdownLabel).expand().center().padTop(ChuanrC.getGlobalY(1.0f / 4));
				table.row();
			
				table.add(goalTitle).bottom();
				table.row();
				table.add(goalLabel).bottom();
				table.row();
				table.add(rewardLabel).bottom().padBottom(ChuanrC.getGlobalY(0.15f));
			
				this.countingDown = true;
			}
		}
		// draw countdown
		else {
			this.countdown -= delta;
			this.countdownInt = (int) (countdown + 0.99);
			if (this.countdown <= 0) this.master.startDay();
		}
	}

	public void transition() {
		this.master.startDay();
	}

	@Override
	public void show() {
		super.show();
		// this doesn't work well for some reason
		Gdx.input.setInputProcessor(uiStage);
	}
}
