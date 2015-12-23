package com.kebabking.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kebabking.game.StoreScreen.TableType;

public class MainStoreScreen extends ActiveScreen {
	static float UNITS_WIDTH = 12f;
	static float UNITS_HEIGHT = 20f;
	
	static float BACK_BUTTON_WIDTH = 0.3f;
	static float BACK_BUTTON_HEIGHT = 0.05f;
	static float BACK_BOTTOM_PAD = 0.01f;
	
	static Color FONT_COLOR = new Color(0.3f, 0.2f, 0.2f, 1); 
	static Color FONT_COLOR_GRAY = new Color(0.7f, 0.6f, 0.6f, 1); 
	static Color FONT_COLOR_GREEN = new Color(0.223f, 0.707f, 0.289f, 1); 

	static float TitlePad = 0.15f;
	static float TitleHeight = 2.15f;

	static float MainButtonWidth = 4.8f;
	static float MainButtonHeight = 4.8f;

	static float MainTextHeight = 1f;
	static float MainPad = 0.2f;
	static float MainIconWidth = 2.7f;
	static float MainIconHeight = 2.7f;
	
	// This guy should have 6 or so additional screens it can go to?
	// Or there should be one screen with the same layout, called StoreScreen.
	// There is a table on that screen that just updates itself.
	
	// The reason is that this screen is very different from the other screens
	StoreScreen storeScreen;
	Stage uiStage;
	
	Table mainTable;
	
	float buttonWidth, buttonHeight;
	
	float unitWidth; 
	float unitHeight;
	
	Table table;
	
	public MainStoreScreen(KebabKing master) {
		super(master);
		
		// initialize unit width and height, useful for making layouts
		this.unitWidth = KebabKing.getWidth() / UNITS_WIDTH;
		this.unitHeight = KebabKing.getHeight() / UNITS_HEIGHT;
		
		this.storeScreen = new StoreScreen(master, this);
		
		ScreenViewport viewport = new ScreenViewport();
		uiStage = new Stage(viewport, batch);		
//		uiStage.setDebugAll(true);
		
		table = new Table();
//				table.debugAll();
		table.align(Align.top);
		table.setSize(KebabKing.getWidth(), KebabKing.getHeight());

		table.setPosition(0, 0);
		
		uiStage.addActor(table);
		
		setupMainTable();
	}
	

	@Override
	public void render(float delta) {
		super.render(delta);

		drawStore(batch);

		if (Gdx.input.isKeyJustPressed(Keys.BACK)) {
			clickBack();
		}
	}
	
	public void drawStore(SpriteBatch batch) {
		uiStage.draw();
	}

	public void setupMainTable() {
		mainTable = new Table();
		
		// first, draw a transparent background over mainTable;
		mainTable.setBackground(new TextureRegionDrawable(Assets.whiteAlpha));
		mainTable.setSize(KebabKing.getWidth(), KebabKing.getHeight());
		
//		mainTable.debugAll();
		
		// add title
		int title_pad_top = KebabKing.getGlobalY(0.15f);
		int title_width = KebabKing.getGlobalX(0.4f);
		int title_height = KebabKing.getGlobalY(0.07f);
		Image title = new Image(Assets.marketTitle);
		mainTable.add(title).center().padTop(title_pad_top).top().width(title_width).height(title_height);
		mainTable.row();
		
		// add text below title
		String text = "welcome to the bazaar";
		Label label = new Label(text, Assets.generateLabelStyleUILight(32));
		label.setColor(FONT_COLOR);
		mainTable.add(label).center().expandY().top();
		mainTable.row();
		
		// add shelf
		int shelf_width = KebabKing.getGlobalX(0.8f);
		int shelf_height = KebabKing.getGlobalY(0.6f);
		int shelf_pos_y = KebabKing.getGlobalY(0.1f);
		
		Table shelf = new Table();
		shelf.setBackground(new TextureRegionDrawable(Assets.marketShelf));
		shelf.setSize(shelf_width, shelf_height);
		shelf.setPosition((KebabKing.getWidth() - shelf_width)/2, shelf_pos_y);
		mainTable.addActor(shelf);
		
		// This is the padding between buttons.
		float SHELF_MIDDLE_PERCENTAGE_X = 1.0f/19.65f;
		float SHELF_MIDDLE_PERCENTAGE_Y = 1.0f/26.7f;
		float buttonPadX = SHELF_MIDDLE_PERCENTAGE_X * shelf_width/2;
		float buttonPadY = SHELF_MIDDLE_PERCENTAGE_Y * shelf_width/2;
		
		float buttonWidth = 1.0f/2.54f * shelf_width;
		float buttonHeight = 1.0f/3.2f * shelf_height;
		
		float buttonsPadTop = 1.0f/18.1f * shelf_height;
		
		// add buttons
		Button foodButton = generateButton(TableType.food);
		Button grillButton = generateButton(TableType.grill);
		Button mapButton = generateButton(TableType.map);
		Button adsButton = generateButton(TableType.ads);
		Button coinsButton = generateButton(TableType.coins);

		shelf.add(foodButton).width(buttonWidth).height(buttonHeight).center().top().padTop(buttonsPadTop).padBottom(buttonPadY).padLeft(buttonPadX).padRight(buttonPadX);
		shelf.add(grillButton).width(buttonWidth).height(buttonHeight).center().top().padTop(buttonsPadTop).padBottom(buttonPadY).padLeft(buttonPadX).padRight(buttonPadX);
		shelf.row();
		shelf.add(mapButton).width(buttonWidth).height(buttonHeight).center().top().padTop(buttonPadY).padBottom(buttonPadY).padLeft(buttonPadX).padRight(buttonPadX);
		shelf.add(adsButton).width(buttonWidth).height(buttonHeight).center().top().padTop(buttonPadY).padBottom(buttonPadY).padLeft(buttonPadX).padRight(buttonPadX);
		shelf.row();
		// TODO add Play button directly on top of coinsbutton
		shelf.add(coinsButton).width(buttonWidth * 2).height(buttonHeight * 0.9f).center().top().expandY().padTop(buttonPadY*2f).padBottom(buttonPadY).padLeft(buttonPadX).padRight(buttonPadX).colspan(2);
		
		addBackButton(newBackButton(), mainTable);
		
		this.table.addActor(mainTable);
		
		// add back button
		// TODO make back button a global thing...? like a part of the big layout
	}
	
	public void addBackButton(Button button, Table table) {
		int backPad = KebabKing.getGlobalY(0.01f);
		table.add(button).bottom().right().expandX().width(KebabKing.getGlobalX(BACK_BUTTON_WIDTH)).height(KebabKing.getGlobalY(BACK_BUTTON_HEIGHT)).padBottom(KebabKing.getGlobalY(BACK_BOTTOM_PAD)).padTop(backPad);
	}
	
	public Button newBackButtonInit() {
		TextButton backButton = new TextButton("back", Assets.getBackButtonStyle(25));
		return backButton;
	}
	public Button newBackButton() {
		Button backButton = newBackButtonInit();
		
		backButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
				clickBack();
			}
		});	
		return backButton;
	}
	
	public Button generateButton(final TableType type) {
		Button button = new Button(Assets.getSpecificMarketButtonStyle(type));
		button.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
				// switch to 
				switchTo(type);
			}
		});
		return button;
	}
	
	public void switchTo(TableType type) {
		storeScreen.switchTo(type);
		master.setScreen(storeScreen);
	}
	
	public void clickBack() {
		master.storeToMain();
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		// need to replace whatever input processor was being used previously
		System.out.println("storescreen");
		DrawUI.setInput(uiStage);
	}
}
