package com.kebabking.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

/* new pause screen created for every kitchen screen */
public class PauseScreen extends ActiveScreen  {
//	static String defaultDesc = "People are getting sick from your nasty ass meat! Luckily, you've got more options. Want to upgrade?";
//	static String defaultDesc = "Hope you're enjoying Kebab King!";
	
	static final String[] tips = {
		"You can serve multiple kebabs by tapping them, then tapping a customer!",
		"Days feel too short? Upgrade your Grill Stand to increase day length!",
		"Customers getting impatient? Better Skewers will increase their satisfaction!",
		"Buying better quality meat will pay for itself in profits!",
		"New seafood kebabs coming soon!",
		"Vegetarian? New veggie kebabs coming soon!",
		"Some kebab types, like chicken, require two sticks and take up twice as much space on the grill.",
		"Any meat still on your grill at the end of the day is refunded!",
		"The Jeweler gives you jade based on how satisfied he is!",
		"Drink and meat upgrades bring more profit per item!",
		"Don't forget to use marketing campaigns to bring more customers to your stand.",
		"Remember: a good chef is always cooking!",
		"Kebabs cooking too slowly? Upgrade your grill type to increase cook speed!",
		"Customers won't accept kebabs they didn't order!",
		"Love Kebab King? We love you too!",
		"You can tap the spice brush once, then tap meat to spice it!",
	};

	Table bigTable;
	Label text;
	Image image;
	
	public PauseScreen(KebabKing master) {
		super(master, true, "Pause");	
		
		bigTable = new Table();
		bigTable.setSize(KebabKing.getWidth(), KebabKing.getHeight());
		uiStage.addActor(bigTable);
		
		Table topBar = generateTopTable();
		bigTable.add(topBar).top().expandX().fillX().padTop(KebabKing.getGlobalY(0.25f));
		bigTable.row();
		
		Table textTable = generateTextTable();
		bigTable.add(textTable).padTop(KebabKing.getGlobalY(0.1f)).top();
		
//		TextureRegion bg = Assets.getTextureRegion("screens/pause-03");		
//		float width = KebabKing.getGlobalX(0.5f);
//		float height = width * bg.getRegionHeight() / bg.getRegionWidth();
		bigTable.row();
		Table resumeTable = generateResumeButton();
		bigTable.add(resumeTable).top().expandY().padTop(KebabKing.getGlobalY(0.1f));; //.width(width).height(height);
	}
	
	public Table generateTopTable() {
		Table topBar = new Table();
//		topBar.debugAll();
		Table topLeft = new Table();
		Table topRight = new Table();
		topLeft.setBackground(new TextureRegionDrawable(Assets.white));
		topRight.setBackground(new TextureRegionDrawable(Assets.white));
		
		String pausedString = Assets.strings.get("paused");
		Label topText = new Label(pausedString, Assets.generateLabelStyleUIChina(50, pausedString));
		
		topBar.add(topLeft).expandX().fillX().height(KebabKing.getGlobalY(SummaryScreen.BAR_HEIGHT));
		
		float imagePadX = KebabKing.getGlobalX(0.1f);
		topBar.add(topText).top().padLeft(imagePadX).padRight(imagePadX);
		topBar.add(topRight).expandX().fillX().height(KebabKing.getGlobalY(SummaryScreen.BAR_HEIGHT));;
		return topBar;
	}
	
	public Table generateTextTable() {
		Table textTable = new Table();
				
		
		StringBuilder sum = new StringBuilder();
		for (String s : tips)
			sum.append(s);
		
		text = new Label("", Assets.generateLabelStyleUI(20, sum.toString()));
		text.setAlignment(Align.center);
		text.setWrap(true);
		text.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
				updateRandomTip();
			}
		});
		
		textTable.add(text).width(KebabKing.getGlobalX(0.8f));
		textTable.row();
		
		Table jadeButton = new Table();
		TextureRegion jade = Assets.getTextureRegion("screens/pause-02");
		jadeButton.setBackground(new TextureRegionDrawable(jade));
		Label ok = new Label("OK", Assets.generateLabelStyleUIChina(70, "OK"));
		jadeButton.add(ok).top().padTop(KebabKing.getGlobalY(0.01f)).expandY();
		
//		float width = KebabKing.getGlobalX(0.25f);
//		float height = width * jade.getRegionHeight() / jade.getRegionWidth();
//		textTable.add(jadeButton).width(width).height(height).padTop(KebabKing.getGlobalY(0.01f));
		
		jadeButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
				goToMarket();
			}
		});
		
		return textTable;
	}
	
	public void updateRandomTip() {
		text.setText(getRandomTip());
	}
	
//	public String getTextToDisplay() {
//		return getRandomTip();
//	}
	
	public String getRandomTip() {
		int index = (int) (Math.random() * tips.length);
		return tips[index];
	}
	
	public Table generateResumeButton() {
		Table button = DrawUI.getBlueButton(Assets.strings.get("resume") , 44);
		
		button.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
				unpause();
			}
		});	
		
		return button;
	}
	
	public void goToMarket() {
		System.out.println("Go to market!");
	}
		
	@Override
	public void render(float delta) {
		super.renderGrayBg(delta);
		
//		uiStage.draw();
	}
	
	@Override
	public void update(float delta, boolean ff) {
		// do nothing, we are overriding active screen so nothing happens on pause
	}

	@Override
	public void show() {
		super.show();
		updateRandomTip();
	}
	
	public void unpause() {
		System.out.println("Unpausing");
		master.kitchenUnpause();
	}
	
	@Override
	public void dispose() {
		super.dispose();
		this.uiStage.dispose();
	}
}
