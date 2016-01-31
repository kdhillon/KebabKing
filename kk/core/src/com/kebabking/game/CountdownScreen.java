package com.kebabking.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

public class CountdownScreen extends ActiveScreen  {
	static int COUNTDOWN_TIME = 0;
	static float GOAL_TIME = 0f;

	DailyGoal goal;

	Table table;
//	Label countdownLabel;
	Label goalTitle;
	Label goalLabel;
	Label rewardLabel;

//	SpriteBatch batch;

	float goalCountdown;

	boolean countingDown;
	float countdown;
	int countdownInt;

	Color color;

	// first displays daily goal for a few seconds, then counts down to start round
	public CountdownScreen(KebabKing master) {
		super(master, true);

//		uiStage.setDebugAll(true);

		table = new Table();
		uiStage.addActor(table);
		table.setSize(KebabKing.getWidth(), KebabKing.getHeight());
		table.setPosition(0, 0);
		table.align(Align.center);
		table.align(Align.top);

		table.row();

		goal = new DailyGoal();

//		this.countdownLabel = new Label("", Assets.generateLabelStyleUIChinaWhite(200));
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
//		if (master.profile.tutorialNeeded) {
//			this.countdown = 0;
//		}

		color = new Color(1, 1, 1, 1);

		master.cm.reset();
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		uiStage.draw();
	}

	@Override
	// actually run a game loop
	public void update(float delta, boolean ff) {
		uiStage.act(delta);

		if (countingDown) {
			super.update(delta, ff);

			color.set(1, 1, 1, countdown - (int) countdown);

//			countdownLabel.setColor(color);
//			this.countdownLabel.setText("" + (int) (countdown + 1));
		}

//		System.out.println("goalCountdown: " + goalCountdown + ", countdown: " + countdown);

		// draw goal
		if (!this.countingDown) {

			if (master.batch.getColor() != Color.WHITE)
				master.batch.setColor(Color.WHITE);

			this.goalCountdown -= delta;
			if (this.goalCountdown <= 0) {
				table.clear();
//				table.add(countdownLabel).expand().center().padTop(KebabKing.getGlobalY(1.0f / 4));
				table.row();

				table.add(goalTitle).bottom();
				table.row();
				table.add(goalLabel).bottom();
				table.row();
				table.add(rewardLabel).bottom().padBottom(KebabKing.getGlobalY(0.15f));

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
}
