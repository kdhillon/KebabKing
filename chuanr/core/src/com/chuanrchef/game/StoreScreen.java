package com.chuanrchef.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.chuanrchef.game.Purchases.PurchaseType;
import com.chuanrchef.game.Purchases.Purchaseable;

public class StoreScreen extends ScreenTemplate {
	static float UNITS_WIDTH = 12f;
	static float UNITS_HEIGHT = 20f;
	static float TitleHeight = 2.15f;
	static float TitlePad = 0.15f;
	
	static float BackButtonHeight = 1.5f;
	static float BackButtonPad = 0.4f;
	
	static float MainButtonWidth = 4.8f;
	static float MainButtonHeight = 4.8f;
	
	static float MainTextHeight = 1f;
	static float MainPad = 0.2f;
	static float MainIconWidth = 2.7f;
	static float MainIconHeight = 2.7f;
	
	static float TypeTitleHeight = 1.5f;
	static float TypeTitlePad = 0.15f;
	static float PurchaseableTitleHeight = 1;
	
	static float IconWidth = 2.5f;
	static float IconHeight = 2.5f;
	
	static float DescriptionHeight = 2f;
	static float DescriptionWidth = 5.5f;
	
	static float CostHeight = 1f;
	static float IconPad = 0.15f;
	static float UnlockPad = 0.15f;
	
	static float UnlockButtonHeight = 1.35f;
	static float ArrowWidth = 1f;
	static float ArrowHeight = 1f;
	static float LowerTablePad = 0.5f;
	
	static float PurchaseableTableWidth = IconWidth + DescriptionWidth;
	static float PurchaseableTableHeight = PurchaseableTitleHeight + DescriptionHeight + CostHeight;
	
	static float PurchaseTableWidth = PurchaseableTableWidth + 2*ArrowWidth;
	static float PurchaseTableHeight = TypeTitleHeight + PurchaseableTableHeight*2 + UnlockButtonHeight + LowerTablePad*2;

	static float SPPadTop = 0.25f;
	static float SPPadBottom =  0.25f;
	
	ChuanrC master;

	Background bg; 

	Grill grill;
	CustomerManager cm;

	SpriteBatch batch;

	Stage uiStage;
	Table table; // main table

	Table mainTable; // has 6 buttons.

	Table foodTable;
	Table grillTable;
	Table mapTable;
	Table adsTable;
	Table coinsTable;

	enum TableType {main, food, grill, map, ads, coins}; // keeps track of which table is currently selected

	TableType currentTable;
	

	// main menu buttons
//	Button foodButton;
//	Button grillButton;
//	Button mapButton;
//	Button marketingButton;
//	Button coinsButton;
//	Button backButton;
//
//	// grill menu buttons
//	Button grillSmall, grillMed, grillLg;
//
//	// map menu buttons
//	Button selectVillage, selectOutskirts, selectSuburbs, selectUniversity, selectCBD;


	enum PurchaseScreen{MAIN, FOOD, GRILL, MAP, ADS, PERMITS}; 
	PurchaseScreen currentScreen;

	int tableWidth;
	int tableHeight;
//	int titleHeight;
	float buttonWidth, buttonHeight;

	TextButtonStyle tbs;

	// assume screen is 480x800 (6/10)
	// divide into grid of size 12/20.
	float unitWidth; // 
	float unitHeight;

	// can only access storescreen from summary screen and main menu screen (direct transition after summary screen)
	// uses scene2d for menus
	public StoreScreen(ChuanrC master) {
		this.master = master;
		this.batch = master.batch;
		this.bg = master.bg;
		this.cm = master.cm;
		this.grill = master.grill;

		this.tableWidth = ChuanrC.width;
		this.tableHeight = ChuanrC.height;

		this.tableWidth = ChuanrC.width * 63 / 64;
		this.tableHeight = ChuanrC.height * 63 / 64;
		
		this.tableWidth = ChuanrC.width;
		this.tableHeight = ChuanrC.height - (int) (DrawUI.UI_BAR_HEIGHT * ChuanrC.height) + 10; // total size of table, everything in it.
		
	
		// initialize unit width and height, useful for making layouts
		this.unitWidth = ChuanrC.width / UNITS_WIDTH;
		this.unitHeight = ChuanrC.height / UNITS_HEIGHT;
		
		this.buttonWidth = unitWidth * MainButtonWidth;
		this.buttonHeight = unitHeight * MainButtonHeight;

		ScreenViewport viewport = new ScreenViewport();
		uiStage = new Stage(viewport, batch);		

		//		uiStage.setDebugAll(true);

		table = new Table();
//		table.debugAll();
		table.align(Align.top);
		//		table.setBackground("default-rect");
		table.setSize(tableWidth, tableHeight);
		
//		table.setPosition((ChuanrC.width - tableWidth) / 2, (ChuanrC.height - tableHeight) / 8);
		table.setPosition((ChuanrC.width - tableWidth) / 2, 0);

//		table.setPosition((ChuanrC.width - tableWidth) / 2, (ChuanrC.height - tableHeight) / 2);
		table.setBackground(Assets.getStoreBackground());
		uiStage.addActor(table);

		// add title
		table.row();

		setupMainTable();
		switchTo(TableType.main);

		initializeTables();
	}

	public void initializeTables() {
		for (TableType type : TableType.values()) {
			if (type != TableType.main) {
				resetTable(type);
			}
		}
	}

	public void resetCurrentTable() {
		this.resetTable(currentTable);
		this.switchTo(currentTable);
	}

	public void resetTable(TableType type) {
		switch(type) {
		case main:
			break;
		case food:
			System.out.println("initializeing food");
			foodTable = createTable("Food", new PurchaseType[] {master.profile.inventory.meatQuality, master.profile.inventory.drinkQuality});
			break;
		case grill:
			grillTable = createTable("Grill", new PurchaseType[] {master.profile.inventory.grillSpecs.getGrillSize(), master.profile.inventory.grillSpecs.getType()});
			break;
		case map:
			mapTable = createTable("Map", new PurchaseType[] {master.profile.inventory.locationType});
			break;
		case ads:
			adsTable = createTable("Ads", new PurchaseType[] {master.profile.inventory.adCampaign});
			break;
		case coins:
			//coinsTable = createTable("Coins", new PurchaseType[] {master.profile.inventory.meatQuality, master.profile.inventory.grillSpecs.getType(), master.profile.inventory.grillSpecs.getGrillSize()});
			coinsTable = new CoinsTable(master);
			break;
		}
	}

	@Override
	public void render(float delta) {
		update(delta);

		batch.begin();
//		bg.draw(batch);
//		cm.draw(batch);
//		grill.draw(batch);
//		DrawUI.drawMoney(batch, getProfile());
//		DrawUI.drawCoins(batch, getProfile());
		batch.end();

		drawStore(batch);
		
		batch.begin();
		DrawUI.drawFullUI(delta, batch, master.profile);
		batch.end();
		
		if (Gdx.input.isKeyJustPressed(Keys.BACK)) {
			clickBack();
		}
	}

	public void drawStore(SpriteBatch batch) {
		uiStage.draw();
	}

	// actually run a game loop
	public void update(float delta) {	
//		bg.act(delta);
//		cm.act(delta);
		uiStage.act(delta);
	}

	public void setupMainTable() {
		mainTable = new Table();
//		mainTable.debugAll();
		
		Label mainTitle = new Label("Market", Assets.getMarketLS());
		
		mainTitle.setAlignment(Align.center);
		mainTable.add(mainTitle).height((TitleHeight - TitlePad) * unitHeight).padBottom(TitlePad * unitHeight);
		
		Table subTable = new Table();
		
		// add first row
		mainTable.row();
		
		Button foodButton = generateMainButton("Food", TableType.food, Assets.getFoodIcon());
		subTable.add(foodButton).size(buttonWidth, buttonHeight).center();

		Button grillButton = generateMainButton("Grill", TableType.grill, Assets.getGrillIcon());
		subTable.add(grillButton).size(buttonWidth, buttonHeight).center();

		subTable.row();
		
		Button mapButton = generateMainButton("Map", TableType.map, Assets.getMapIcon());
		subTable.add(mapButton).size(buttonWidth, buttonHeight).center();

		Button adsButton = generateMainButton("Ads", TableType.ads, Assets.getAdsIcon());
		subTable.add(adsButton).size(buttonWidth, buttonHeight).center();
		
		subTable.row();
		
		Button coinsButton = generateMainButton("Coins", TableType.coins, Assets.getCoinsIcon());
		subTable.add(coinsButton).size(buttonWidth, buttonHeight).center();
		
		Button otherButton = generateMainButton("Bonus!", TableType.coins, null);
		subTable.add(otherButton).size(buttonWidth, buttonHeight).center();
		
		mainTable.add(subTable);
	}
	
	public Button generateMainButton(String name, final TableType switchTo, TextureRegion icon) {
		Button button = new Button();
		button.setStyle(Assets.getMainButtonStyle());
		Image image;
		if (icon != null) {
			image = new Image(icon);
		}
		else {
			image = new Image();
		}
		button.add(image).width(MainIconWidth*unitWidth).height(MainIconHeight*unitHeight).padTop(MainPad * unitHeight).padBottom(MainPad * unitHeight);
		button.row();
		Label label = new Label(name, Assets.getMainLS());
		button.add(label).height(MainTextHeight*unitHeight);
		button.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
				switchTo(switchTo);
			}
		});
		return button;
	}

//	public Button newMainBackButton() {
//		backButton = new TextButton("Back", Assets.getMainButtonStyle());
//		mainTable.add(backButton).size(buttonWidth, buttonHeight).center();
//		backButton.addListener(new InputListener() {
//			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
//				return true;
//			}
//			public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
//				master.storeToMain();
//			}
//		});	
//		return backButton;
//	}
	
	public Button newBackButton() {
		TextButton backButton = new TextButton("Back", Assets.getBackButtonStyle());

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
	
	public void clickBack() {
		if (this.currentTable == TableType.main) {
			master.storeToMain();
		}
		else {
			this.switchTo(TableType.main);
		}
	}

	// set up a table to work with a given purchasetype!
	public Table createPurchaseTypeTable(PurchaseType type) {
		Table table = new Table();
		table.setBackground(Assets.getPurchaseTypeBackground());
//		table.debugAll();

		// add title
		Label title = new Label(type.getName(), Assets.getPurchaseTypeTitleLS());

		title.setAlignment(Align.center);
		table.add(title).height(TypeTitleHeight * unitHeight).top();//.width(PurchaseTableWidth * unitWidth);//.padBottom(unitHeight * 0.5f);
		table.row();

		System.out.println("creating current with " + type.getCurrentSelected().getName());
		// add section for currently selected item
		Table current = createPurchaseableTable(type.getCurrentSelected());
		table.add(current).width(PurchaseableTableWidth * unitWidth)
		.height(PurchaseableTableHeight * unitHeight); //.pad(unitHeight, unitWidth, unitHeight, unitWidth);

		// contains button, table, and button. Maybe also button at bottom.
		Table scrollAndNext = new Table();

		Purchaseable defaultNext = type.getNext(type.getCurrentSelected(), false);

		// don't select the same one
		if (defaultNext == type.getCurrentSelected()) defaultNext = type.getNext(defaultNext, false);

		// default to the next one to the right
		Table next = createPurchaseableTable(defaultNext);
		next.setName("next_table");
		
		// add "Unlock" or "Select" button 
		TextButton unlockSelect = createUnlockSelectButton(defaultNext, type);
		unlockSelect.setName("upgrade_select");

		//try to add upgrade select to "next" PurchaseableTable!
		next.row();
		next.add(unlockSelect).height((UnlockButtonHeight - UnlockPad) * unitHeight).padBottom(UnlockPad*unitHeight);

		// create left scroll button 
		StoreScrollButton scrollLeft = new StoreScrollButton(scrollAndNext, type, defaultNext);
		ButtonStyle bs = new ButtonStyle();
		bs.down = Assets.getArrowLeftUp();
		bs.up = Assets.getArrowLeftDown();

		scrollLeft.setStyle(bs);

		scrollLeft.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
				StoreScrollButton ssb = (StoreScrollButton) event.getTarget();
				tableScroll(ssb, true);
			}
		});

		// create right scroll button
		StoreScrollButton scrollRight = new StoreScrollButton(scrollAndNext, type, defaultNext);

		ButtonStyle bs2 = new ButtonStyle();
		bs2.down = Assets.getArrowRightUp();
		bs2.up = Assets.getArrowRightDown();

		scrollRight.setStyle(bs2);

		scrollRight.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
				StoreScrollButton ssb = (StoreScrollButton) event.getTarget();
				tableScroll(ssb, false);
			}
		});	

		scrollAndNext.add(scrollLeft).width(ArrowWidth*unitWidth).height(ArrowHeight * unitHeight);
		scrollAndNext.add(next).width(PurchaseableTableWidth * unitWidth)
		.height((PurchaseableTableHeight + UnlockButtonHeight) * unitHeight);
		scrollAndNext.add(scrollRight).width(ArrowWidth*unitWidth).height(ArrowHeight * unitHeight);
		
		table.row();
		table.add(scrollAndNext).width((PurchaseTableWidth) * unitWidth)
		.height((PurchaseableTableHeight + UnlockButtonHeight) * unitHeight).padTop(LowerTablePad * unitWidth).padBottom(LowerTablePad*unitHeight);
	
		// return
		return table;
	}

	// switches the table in the current table to the next one, left or right
	public void tableScroll(StoreScrollButton ssb, boolean left) {
		Purchaseable next = ssb.type.getNext(ssb.current, left);

		// don't display the one that is selected
		if (next == ssb.type.getCurrentSelected()) next = ssb.type.getNext(next, left);

		ssb.current = next;
		setCurrentPurchaseableTable(ssb.scroll, next, ssb.type);
	}

	public void setCurrentPurchaseableTable(Table scrollAndNext, Purchaseable purchaseable, PurchaseType type) {
		Table newPurchaseable = createPurchaseableTable(purchaseable);
		//try to add upgrade select to "next" PurchaseableTable!
		TextButton newUBS = createUnlockSelectButton(purchaseable, type);
		newPurchaseable.row();
		newPurchaseable.add(newUBS).height(UnlockButtonHeight * unitHeight); //.padTop(0.5f * unitHeight);

		scrollAndNext.getCell(scrollAndNext.findActor("next_table")).setActor(null);
		scrollAndNext.getCell(scrollAndNext.findActor("next_table")).setActor(newPurchaseable);
		newPurchaseable.setName("next_table");

	

//		main.getCell(main.findActor("upgrade_select")).setActor(null);
//		main.getCell(main.findActor("upgrade_select")).setActor(newUBS);
//		newUBS.setName("upgrade_select");
	}

	private TextButton createUnlockSelectButton(Purchaseable purchaseable, PurchaseType type) {
		UnlockSelectButton button  = new UnlockSelectButton(Assets.getUnlockButtonStyle(), type, purchaseable);

		button.getLabel().setTouchable(Touchable.disabled);

		// SELECT
		if (type.unlocked(purchaseable)) {
			button.setText("Select");
			button.addListener(new InputListener() {
				public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
					return true;
				}
				public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
					UnlockSelectButton usb = (UnlockSelectButton) event.getTarget();
					usb.type.setCurrent(usb.purchaseable);
					System.out.println("selected");
					resetCurrentTable();
				}
			});	
		}
		// UNLOCK
		else {
			if (purchaseable.coinsToUnlock() > 0) {
				button.setText("Unlock (" + purchaseable.coinsToUnlock() + " c)");
			}
			else {
				button.setText("Unlock ($" + purchaseable.cashToUnlock() + ")");
			}
			button.addListener(new InputListener() {
				public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
					return true;
				}
				public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
					UnlockSelectButton usb = (UnlockSelectButton) event.getTarget();
					attemptUnlock(usb.type, usb.purchaseable);
				}
			});	
		}
		return button;
	}

	public void attemptUnlock(PurchaseType type, Purchaseable purchaseable) {
		boolean success = this.master.profile.inventory.unlock(purchaseable, type);
		if (!success) unlockFail(purchaseable);
		else unlockSuccess(purchaseable, type);

	}

	public void unlockSuccess(Purchaseable purchaseable, PurchaseType type) {
		if (purchaseable.coinsToUnlock() > 0)
			System.out.println("You unlocked " + purchaseable.getName() + " for " + purchaseable.coinsToUnlock() + " coins");
		else {
			System.out.println("You unlocked " + purchaseable.getName() + " for $" + purchaseable.cashToUnlock());
		}

		// verify unlocked
		if (!type.unlocked(purchaseable)) System.out.println("SHOULD BE UNLOCKED");
		else {
			System.out.println("successfully unlocked!");
		}

		// must update everything now TODO
		this.resetCurrentTable();
	}

	public void unlockFail(Purchaseable purchaseable) {
		System.out.println("You can't afford that!");
	}

	// creates a table of dimensions 8 x 3.5 describing the purchaseable in question
	public Table createPurchaseableTable(Purchaseable purchaseable) {
		Table table = new Table();
		table.setBackground(Assets.getPurchaseableBackground());
	
//		table.debugAll();

		// title takes up top row, 
		Label title = new Label(purchaseable.getName(), Assets.getPurchaseableTitleLS()); 
		title.setAlignment(Align.center);
		table.add(title).height(1 * unitHeight).colspan(2);//.width(PurchaseableTableWidth * unitWidth)

		// add icon
		Image icon;
		if (purchaseable.getIcon() != null) {
			icon = new Image(purchaseable.getIcon());
		}
		else icon = new Image(Assets.getDefaultIcon());

		Table obligatory = new Table();
//		obligatory.debugAll();
		// 1.5 + 1 + 
		table.row();
		obligatory.add(icon).width((IconWidth-2*IconPad) * unitWidth).height((IconHeight - 2*IconPad) * unitHeight)
		.pad(IconPad * unitHeight, IconPad * unitWidth, IconPad * unitHeight, IconPad * unitWidth);

		// add description and cost per day to a subtable
		Table subtable = new Table();
//		subtable.debugAll();

		// description 
		Label description = new Label(purchaseable.getDescription(), Assets.getDescriptionLS());
		description.setWrap(true); // hopefully wraps in parent
		subtable.add(description).width(DescriptionWidth * unitWidth).height(DescriptionHeight * unitHeight).left();

		// price per day
		Label pricePerDay = new Label("$" + purchaseable.getDailyCost() + " per day!", Assets.getCostLS());
		if (purchaseable.getDailyCost() == 0) pricePerDay.setText("");
		pricePerDay.setAlignment(Align.left);
		subtable.row();
		subtable.add(pricePerDay).width(DescriptionWidth * unitWidth).height(CostHeight * unitHeight);

		// finally add to table
		obligatory.add(subtable); //.width(PurchaseableTableWidth * unitWidth).height(2.5f * unitHeight);

		table.add(obligatory).width(PurchaseableTableWidth * unitWidth);

		// don't forget to return
		return table;		
	}

	// create and return a full table for the given purchase types with the given title
	public Table createTable(String titleString, PurchaseType[] types) {
		Table table = new Table();
//		table.debugAll();
		table.setWidth(ChuanrC.width);
		table.setHeight(ChuanrC.height);

		// add title
		Label title = new Label(titleString, Assets.getStoreTitleLS());
		title.setAlignment(Align.center);
		table.add(title).height(TitleHeight * unitHeight);

		table.row();

		// contains all purchase tables
		Table tallTable = new Table(); 
		tallTable.row();
//		tallTable.debugAll();

		for (PurchaseType type : types) {
			// just do one for now
			Table subTable = createPurchaseTypeTable(type);
			tallTable.add(subTable).width(PurchaseTableWidth * unitWidth)
			.height(PurchaseTableHeight * unitHeight).padTop(unitHeight*0.5f).padBottom(0).expandY().fillY().top(); // should auto add padding
			tallTable.row();
		}

		// create scrollpane for remaining tables
		ScrollPane sp = new ScrollPane(tallTable, Assets.getSPS());
//		sp.setScrollbarsOnTop(false);
		table.add(sp).width(tableWidth).expandY().padTop(SPPadTop* unitHeight).padBottom(SPPadTop*unitHeight);//height(tableHeight - ((SPPadTop + TitleHeight + BackButtonHeight) * unitHeight)).padTop(SPPadTop * unitHeight); 
		table.row();
		
		return table;
	}

	public void switchTo(TableType switchToThis) {
		Table newTable = null;
		String name = "NoType";

		switch(switchToThis) {
		case main:
			newTable = mainTable;
			name = "Main";
			break;
		case food:
			newTable = foodTable;
			name = "Food";
			break;
		case grill:
			newTable = grillTable;
			name = "Grill";
			break;
		case map:
			newTable = mapTable;
			name = "Map";
			break;
		case ads:
			newTable = adsTable;
			name = "Ads";
			break;
		case coins:
			newTable = coinsTable;
			name = "Coins";
			break;
		}

		table.clear();
		table.row();
		//		table.add(title).height(titleHeight);
		//		table.row();
		table.add(newTable).width(tableWidth).height(tableHeight-(BackButtonHeight + BackButtonPad)*unitHeight).top();
		table.row();
		
		Button back = newBackButton();
//		table.debugAll();
		table.add(back).height(BackButtonHeight * unitHeight).width(4*unitWidth).right().padRight(0.5f*unitWidth).padBottom(BackButtonPad*unitHeight);
		
		this.currentTable = switchToThis;
		
		this.master.platformSpec.sendEventHit("Store", "Switch To", name);
	}

	public Profile getProfile() {
		return master.profile;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		// need to replace whatever input processor was being used previously
		System.out.println("storescreen");
		
		DrawUI.setInput(uiStage);
	}
}
