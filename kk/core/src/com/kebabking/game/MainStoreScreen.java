package com.kebabking.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.kebabking.game.StoreScreen.TableType;
import com.kebabking.game.Purchases.Purchaseable;

public class MainStoreScreen extends ActiveScreen {
	static float UNITS_WIDTH = 12f;
	static float UNITS_HEIGHT = 20f;
	
	static float BACK_BUTTON_WIDTH = 0.4f;
	static float BACK_BUTTON_HEIGHT = 0.09f;
	static float BACK_BOTTOM_PAD = 0.025f;
	
	static Color FONT_COLOR = new Color(0.22f, 0.2f, 0.2f, 1); 
	static Color FONT_COLOR_GRAY = new Color(0.22f, 0.2f, 0.2f, 0.8f); 
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
	
	Table mainTable;
	
	float buttonWidth, buttonHeight;
	
	float unitWidth; 
	float unitHeight;
	
	Table table;
	
	public MainStoreScreen(KebabKing master) {
		super(master, true, "Main Store");
		
		// initialize unit width and height, useful for making layouts
		this.unitWidth = KebabKing.getWidth() / UNITS_WIDTH;
		this.unitHeight = KebabKing.getHeight() / UNITS_HEIGHT;
		
		this.storeScreen = new StoreScreen(master, this);
		
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
		super.renderWhiteAlpha(delta, DrawUI.WHITE_ALPHA);

//		drawStore(batch);

		if (Gdx.input.isKeyJustPressed(Keys.BACK)) {
			clickBack();
		}
	}
	
	@Override
	// needed for scroll panes
	public void update(float delta, boolean ff) {
		super.update(delta, ff);
		uiStage.act(delta);
	}
	
//	public void drawStore(SpriteBatch batch) {
//	}

	public void setupMainTable() {
		mainTable = new Table();
		mainTable.setSize(KebabKing.getWidth(), KebabKing.getHeight());
//		mainTable.debugAll();
		
		// add title
		int title_pad_top = KebabKing.getGlobalY(0.08f);
		int title_width = KebabKing.getGlobalX(0.3f);
		int title_height = KebabKing.getGlobalY(0.05f);
//		Image title = new Image(Assets.marketTitle);
//		mainTable.add(title).center().padTop(title_pad_top).top().width(title_width).height(title_height);
//		mainTable.row();
		
		// add text below title
		String text = Assets.strings.get("welcome_to_the_market");
		Label label = new Label(text, Assets.generateLabelStyleUILight(28, text));
		label.setColor(FONT_COLOR);
		mainTable.add(label).center().expandY().top().padTop(title_pad_top * 1.5f);
		mainTable.row();
//		
		// add shelf
		int shelf_width = KebabKing.getGlobalX(0.8f);
		int shelf_height_o = KebabKing.getGlobalY(0.6f);
		int shelf_height = KebabKing.getGlobalY(0.667f);
		int shelf_pos_y = KebabKing.getGlobalY(0.15f);
		
		Table shelf = new Table();
		shelf.setBackground(new TextureRegionDrawable(Assets.marketShelf));
		shelf.setSize(shelf_width, shelf_height);
		shelf.setPosition((KebabKing.getWidth() - shelf_width)/2, shelf_pos_y);

		mainTable.addActor(shelf);
		
		// This is the padding between buttons.
		float SHELF_MIDDLE_PERCENTAGE_X = 1.0f/19.65f;
		float SHELF_MIDDLE_PERCENTAGE_Y = 1.0f/31.7f;
		float buttonPadX = SHELF_MIDDLE_PERCENTAGE_X * shelf_width/2;
		float buttonPadY = SHELF_MIDDLE_PERCENTAGE_Y * shelf_height/2;
		
		float buttonWidth = 1.0f/2.54f * shelf_width;
		float buttonHeight = 1.0f/3.2f * shelf_height_o;
		
		float buttonsPadTop = 1.0f/17f * shelf_height_o * 0.9f;
		
		// add buttons
		Table foodButton = generateButton(TableType.food, buttonWidth, buttonHeight);
		Table grillButton = generateButton(TableType.grill, buttonWidth, buttonHeight);
		Table mapButton = generateButton(TableType.map, buttonWidth, buttonHeight);
		Table adsButton = generateButton(TableType.ads, buttonWidth, buttonHeight);
		
		Table wheelButton = generateButton(TableType.wheel, buttonWidth, buttonHeight * 1.15f);
		Table coinsButton = generateButton(TableType.jeweler, buttonWidth, buttonHeight * 1.15f);
		
//		String getJade = Assets.strings.get("get_jade");
//		Label earnJade = new Label(getJade, Assets.generateLabelStyleUIChina(26, getJade));
//		coinsButton.debugAll();
//		coinsButton.add(earnJade).top().padTop(KebabKing.getGlobalY(0.1f)).expandY().padRight(KebabKing.getGlobalX(0.02f));
//		coinsButton.add(earnJade).center().padTop(KebabKing.getGlobalY(0.06f)).expandY().padRight(KebabKing.getGlobalX(0.02f));

		shelf.add(foodButton).width(buttonWidth).height(buttonHeight).center().top().padTop(buttonsPadTop).padBottom(buttonPadY).padLeft(buttonPadX).padRight(buttonPadX);
		shelf.add(grillButton).width(buttonWidth).height(buttonHeight).center().top().padTop(buttonsPadTop).padBottom(buttonPadY).padLeft(buttonPadX).padRight(buttonPadX);
		shelf.row();
		shelf.add(mapButton).width(buttonWidth).height(buttonHeight).center().top().padTop(buttonPadY).padBottom(buttonPadY).padLeft(buttonPadX).padRight(buttonPadX);
		shelf.add(adsButton).width(buttonWidth).height(buttonHeight).center().top().padTop(buttonPadY).padBottom(buttonPadY).padLeft(buttonPadX).padRight(buttonPadX);
		shelf.row();
		
		shelf.add(wheelButton).width(buttonWidth).height(buttonHeight).center().bottom().padBottom(KebabKing.getGlobalYFloat(0.003f)).padLeft(buttonPadX).padRight(buttonPadX).expandY();
		shelf.add(coinsButton).width(buttonWidth).height(buttonHeight).center().bottom().padBottom(KebabKing.getGlobalYFloat(0.003f)).padLeft(buttonPadX).padRight(buttonPadX);
		
		KebabKing.print("BUTTON WIDTH: " + buttonWidth + " BUTTON HEIGHT: " + buttonHeight);
		
		// TODO add Play button directly on top of coinsbutton
//		shelf.add(wheelButton).width(buttonWidth).height(buttonHeight).center().top().expandY().padTop(buttonPadY).padBottom(buttonPadY).padLeft(buttonPadX).padRight(buttonPadX);
//		shelf.add(coinsButton).width(buttonWidth).height(buttonHeight).center().top().expandY().padTop(buttonPadY).padBottom(buttonPadY).padLeft(buttonPadX).padRight(buttonPadX);
		shelf.row();
		addBackButton(newBackButton(), mainTable);
		
		this.table.addActor(mainTable);
	}
	
	public void addBackButton(Table button, Table table) {
		int backPad = KebabKing.getGlobalY(0.01f);
		table.add(button).bottom().center().expandX().padBottom(KebabKing.getGlobalY(BACK_BOTTOM_PAD)).padTop(backPad).height(KebabKing.getGlobalY(BACK_BUTTON_HEIGHT)); //width(KebabKing.getGlobalX(BACK_BUTTON_WIDTH)).height(KebabKing.getGlobalY(BACK_BUTTON_HEIGHT))
	}
	
	public Table newBackButtonInit() {
//		Table backButton = new Table();
//		backButton.setTouchable(Touchable.enabled);
//		Label back = new Label("back", Assets.generateLabelStyleUIChinaWhite(36, "back"));
//		backButton.setBackground(new TextureRegionDrawable(Assets.getTextureRegion("market/Jeweler-11")));
//		backButton.add(back);
		return DrawUI.getBlueButton(Assets.strings.get("back"), 44);
	}
	
	public Table newBackButton() {
		Table backButton = newBackButtonInit();
		
		backButton.addListener(new StrictInputListener() {
			public void touch(InputEvent event) {
				clickBack();
			}
		});	
		return backButton;
	}
	
	
	public Table generateButton(final TableType type, float buttonWidth, float buttonHeight) {
		Table container = new Table();
		Button button = new Button(Assets.getSpecificMarketButtonStyle(type));
		container.setTouchable(Touchable.enabled);
		container.addListener(new StrictInputListener() {
			public void touch(InputEvent event) {
				// switch to 
				switchTo(type);
			}
		});
		
		container.add(button).expand().width(buttonWidth).height(Math.min(buttonHeight, buttonWidth * 1.15f));
		
		Table labelTable = new Table();
		labelTable.setBackground(new TextureRegionDrawable(Assets.whiteAlpha));
		
		String text;
		switch(type) {
		case food:
			text = Assets.strings.get("food_n_drink");
			break;
		case grill:
			text = Assets.strings.get("grill_n_tools");
			break;
		case map:
			text = Assets.strings.get("location");
			break;
		case ads:
			text = Assets.strings.get("advertising");
			break;
		case jeweler:
			text = Assets.strings.get("the_jeweler");
			break;
		case wheel:
			text = Assets.strings.get("wheel_of_jade");
			break;
		default:
			text = "";
		}
		
		float padTop = KebabKing.getGlobalYFloat(0.000f);
		float labelHeight = KebabKing.getGlobalYFloat(0.035f);
		
		Label title = new Label(text, Assets.generateLabelStyleUI(21, text));
		title.setColor(FONT_COLOR);
		labelTable.add(title);
		container.row();
		container.add(labelTable).padTop(-buttonHeight * 2 + labelHeight + padTop).width(buttonWidth).height(labelHeight);
		container.toBack();
		
		return container;
	}
	
	public void switchTo(Purchaseable p) {
		KebabKing.print("switching main store screen to " + p.getName());
		storeScreen.switchToType(p.getType());
		master.setScreen(storeScreen);
	}
	
	public void switchTo(TableType type) {
//		if (type == TableType.coins) {
//			Manager.iab.makePurchase("android.test.purchased");
//
//			return;
//		}
		storeScreen.switchTo(type);
		master.setScreen(storeScreen);
	}
	
	public void clickBack() {
		master.leaveStore();
	}

}
