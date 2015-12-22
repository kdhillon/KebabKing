package com.kebabking.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
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
//import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kebabking.game.Managers.Manager;
import com.kebabking.game.Purchases.Inventory;
import com.kebabking.game.Purchases.PurchaseType;
import com.kebabking.game.Purchases.Purchaseable;

public class StoreScreen extends ScreenTemplate {
	static float UNITS_WIDTH = 12f;
	static float UNITS_HEIGHT = 20f;
	static float TitleHeight = 2.15f;

	//	static float BackButtonHeight = 1.5f;
	//	static float BackButtonPad = 0.4f;

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

	MainStoreScreen mainStoreScreen;

	KebabKing master;

	Background bg; 

	Grill grill;
	CustomerManager cm;

	SpriteBatch batch;

	Stage uiStage;
	Table mainTable; // main table

	Table foodTable;
	Table grillTable;
	Table mapTable;
	Table adsTable;
	Table coinsTable;
	Table vanityTable;

	public enum TableType {food, grill, map, ads, coins, vanity}; // keeps track of which table is currently selected

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

	int mainWidth;
	int tableWidth;
	int tableHeight;
	//	int titleHeight;
	float buttonWidth, buttonHeight;

	TextButtonStyle tbs;

	PurchaseType[] types;
	TextButton[] typeButtons;
	int currentTypeIndex;
	Table currentTypeTable;

	Table selectedPurchaseableTable;
	Purchaseable[] purchaseables;
	// currently selected purchaseable
	int currentPurchaseableIndex;
	Table currentPurchaseableTable;
	Table[] purchaseableTables;

	// assume screen is 480x800 (6/10)
	// divide into grid of size 12/20.
	//	float unitWidth; // 
	//	float unitHeight;

	// TODO make private classes for Unlock button and Select button

	// can only access storescreen from summary screen and main menu screen (direct transition after summary screen)
	// uses scene2d for menus
	public StoreScreen(KebabKing master, MainStoreScreen mainStoreScreen) {
		this.master = master;
		this.batch = master.batch;
		this.bg = master.bg;
		this.cm = master.cm;
		this.grill = master.grill;
		this.mainStoreScreen = mainStoreScreen;

		this.tableWidth = KebabKing.getWidth();
		this.tableHeight = KebabKing.getHeight();

		// initialize unit width and height, useful for making layouts
		//		this.unitWidth = ChuanrC.getWidth() / UNITS_WIDTH;
		//		this.unitHeight = ChuanrC.getHeight() / UNITS_HEIGHT;
		//
		ScreenViewport viewport = new ScreenViewport();
		uiStage = new Stage(viewport, batch);		

//		uiStage.setDebugAll(true);

		mainTable = new Table();
//		mainTable.debugAll();
		mainTable.align(Align.top);
		mainTable.setSize(tableWidth, tableHeight);

		mainTable.setPosition((KebabKing.getWidth() - tableWidth) / 2, 0);

		mainTable.setBackground(new TextureRegionDrawable(Assets.whiteAlpha));

		uiStage.addActor(mainTable);

		// add title
		mainTable.row();

		//		setupMainTable();
		//		switchTo(TableType.main);

		initializeTables();

		//		switchTo(TableType.)

		master.profile.inventory.adCampaign.setTable(this, TableType.ads);
		master.profile.inventory.meatQuality.setTable(this, TableType.food);
		master.profile.inventory.drinkQuality.setTable(this, TableType.food);
		master.profile.inventory.locationType.setTable(this, TableType.map);
		master.profile.inventory.grillSpecs.getGrillSize().setTable(this, TableType.grill);
		master.profile.inventory.grillSpecs.getType().setTable(this, TableType.grill);
		master.profile.inventory.grillStand.setTable(this,  TableType.vanity);
		master.profile.inventory.decorations.setTable(this,  TableType.vanity);
	}

	public void initializeTables() {
		for (TableType type : TableType.values()) {
			//			if (type != TableType.main) {
			resetTable(type);
			//			}
		}
	}

	public void resetCurrentTable() {
		this.resetTable(currentTable);
		this.switchTo(currentTable);
	}

	// TODO, change this to only update the subtable we're worried about, so it doesn't scroll up.
	public void resetTable(TableType type) {
		switch(type) {
		//		case main:
		//			break;
		case food:
			//			System.out.println("initializeing food");
			foodTable = createTable("Food", new PurchaseType[] {master.profile.inventory.meatQuality, master.profile.inventory.drinkQuality});
			break;
		case grill:
			grillTable = createTable("Grill", new PurchaseType[] {master.profile.inventory.grillSpecs.getGrillSize(), master.profile.inventory.grillSpecs.getType(), master.profile.inventory.grillStand});
			break;
		case map:
			mapTable = createTable("Map", new PurchaseType[] {master.profile.inventory.locationType});
			break;
		case ads:
			adsTable = createTable("Ads", new PurchaseType[] {master.profile.inventory.adCampaign});
			break;
		case vanity:
			vanityTable = createTable("Vanity", new PurchaseType[] {master.profile.inventory.decorations, });
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
		bg.draw(batch);
		cm.draw(batch);
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
		bg.act(delta);
		cm.act(delta);
		uiStage.act(delta);
	}

	public Button newBackButton() {
		Button backButton = mainStoreScreen.newBackButtonInit();

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
		System.out.println("switching to main");
		switchToMain();
	}

	// set up a table to work with a given purchasetype!
	//	public void updatePurchaseTypeTable(PurchaseType type) {
	//		Table table = new Table();
	////		table.setBackground(Assets.getPurchaseTypeBackground());
	//		//		table.debugAll();
	//
	//		// add title
	//		Label title = new Label(type.getName(), Assets.getPurchaseTypeTitleLS());
	//
	//		title.setAlignment(Align.center);
	//		table.add(title).height(TypeTitleHeight * unitHeight).top();//.width(PurchaseTableWidth * unitWidth);//.padBottom(unitHeight * 0.5f);
	//		table.row();
	//
	//		if (type.getCurrentSelected() != null) {
	//			System.out.println("creating current with " + type.getCurrentSelected().getName());
	//		}
	//		// add section for currently selected item
	//		Table current = createPurchaseableTable(type.getCurrentSelected(), type);
	//		table.add(current).width(PurchaseableTableWidth * unitWidth)
	//		.height(PurchaseableTableHeight * unitHeight); //.pad(unitHeight, unitWidth, unitHeight, unitWidth);
	//
	//
	//		// contains button, table, and button. Maybe also button at bottom.
	//		Table scrollAndNext = new Table();
	//
	//		Purchaseable defaultNext = type.getNext(type.getCurrentSelected(), false);
	//
	//		// don't select the same one
	//		if (defaultNext == type.getCurrentSelected()) defaultNext = type.getNext(defaultNext, false);
	//
	//		// default to the next one to the right
	//		Table next = createPurchaseableTable(defaultNext, type);
	//		next.setName("next_table");
	//
	//		// add "Unlock" or "Select" button 
	//		TextButton unlockSelect = createUnlockSelectButton(defaultNext, type);
	//		unlockSelect.setName("upgrade_select");
	//
	//		//try to add upgrade select to "next" PurchaseableTable!
	//		next.row();
	//		next.add(unlockSelect).height((UnlockButtonHeight - UnlockPad) * unitHeight).padBottom(UnlockPad*unitHeight);
	//
	//		// create left scroll button 
	//		StoreScrollButton scrollLeft = new StoreScrollButton(scrollAndNext, type, defaultNext);
	//		ButtonStyle bs = new ButtonStyle();
	//		bs.down = Assets.getArrowLeftUp();
	//		bs.up = Assets.getArrowLeftDown();
	//
	//		scrollLeft.setStyle(bs);
	//
	//		scrollLeft.addListener(new InputListener() {
	//			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
	//				return true;
	//			}
	//			public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
	//				StoreScrollButton ssb = (StoreScrollButton) event.getTarget();
	//				tableScroll(ssb, true);
	//			}
	//		});
	//
	//		// create right scroll button
	//		StoreScrollButton scrollRight = new StoreScrollButton(scrollAndNext, type, defaultNext);
	//
	//		ButtonStyle bs2 = new ButtonStyle();
	//		bs2.down = Assets.getArrowRightUp();
	//		bs2.up = Assets.getArrowRightDown();
	//
	//		scrollRight.setStyle(bs2);
	//
	//		scrollRight.addListener(new InputListener() {
	//			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
	//				return true;
	//			}
	//			public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
	//				StoreScrollButton ssb = (StoreScrollButton) event.getTarget();
	//				tableScroll(ssb, false);
	//			}
	//		});	
	//
	//		scrollRight.other = scrollLeft;
	//		scrollLeft.other = scrollRight;
	//
	//		scrollAndNext.add(scrollLeft).width(ArrowWidth*unitWidth).height(ArrowHeight * unitHeight);
	//		scrollAndNext.add(next).width(PurchaseableTableWidth * unitWidth)
	//		.height((PurchaseableTableHeight + UnlockButtonHeight) * unitHeight);
	//		scrollAndNext.add(scrollRight).width(ArrowWidth*unitWidth).height(ArrowHeight * unitHeight);
	//
	//		table.row();
	//		table.add(scrollAndNext).width((PurchaseTableWidth) * unitWidth)
	//		.height((PurchaseableTableHeight + UnlockButtonHeight) * unitHeight).padTop(LowerTablePad * unitWidth).padBottom(LowerTablePad*unitHeight);
	//
	//		// return
	//		return table;
	//	}

	public void updatePurchaseTypeTable(PurchaseType type) {
		if (this.currentTypeTable == null) this.currentTypeTable = new Table();
		this.currentTypeTable.clear();

		//		table.setBackground(Assets.getPurchaseTypeBackground());
		//		table.debugAll();

		// add title
		//		Label title = new Label(type.getName(), Assets.getPurchaseTypeTitleLS());
		//
		//		title.setAlignment(Align.center);
		//		currentTypeTable.add(title).height(TypeTitleHeight * unitHeight).top();//.width(PurchaseTableWidth * unitWidth);//.padBottom(unitHeight * 0.5f);
		//		currentTypeTable.row();

		//		if (type.getCurrentSelected() != null) {
		//			System.out.println("creating current with " + type.getCurrentSelected().getName());
		//		}
		// add section for currently selected item
		updateSelectedPurchaseableTable(type.getCurrentSelected(), type);
		int selectedPad = KebabKing.getGlobalY(0.02f);
		currentTypeTable.add(selectedPurchaseableTable).fillX().padBottom(selectedPad);

		// now draw list of all purchaseable options
		this.purchaseables = type.values;
		this.currentPurchaseableIndex = -1;
		for (int i = 0; i < purchaseables.length; i++) {
			if (purchaseables[i] == type.getCurrentSelected()) this.currentPurchaseableIndex = i;
		}
		
		this.purchaseableTables = new Table[purchaseables.length];

		int purchaseablePad = KebabKing.getGlobalY(0.01f);

		Table purchaseableListTable = new Table();
		for (int i = 0; i < purchaseables.length; i++) {
//			System.out.println("updating purchaseable table " + i);
			purchaseableListTable.add(createPurchaseableTable(i)).expandX().left().padTop(purchaseablePad).fillX();
			purchaseableListTable.row();
		}
		currentTypeTable.row();
		ScrollPane sp = new ScrollPane(purchaseableListTable, Assets.getSPS());
		currentTypeTable.add(sp).expandX().fillX();
	}

	public Table createPurchaseableTable(int index) {
		purchaseableTables[index] = new Table();
		updatePurchaseableTable(index);
		return purchaseableTables[index];
	}

	public void updatePurchaseableTable(int index) {
//		System.out.println("updating purchaseable table with current purchasable index " + currentPurchaseableIndex);
		Table table = purchaseableTables[index];
		Purchaseable purchaseable = purchaseables[index];
		PurchaseType type = types[currentTypeIndex];
		if (purchaseable == null) return;
		table.clear();
//		table.debugAll();

		// actually populate the table
		// first thing is the 9 patch on the left
		ButtonStyle bs = Assets.getButtonStylePurchaseableGray();
		if (this.currentPurchaseableIndex == index) 
			bs = Assets.getButtonStylePurchaseableGreen();
		Button button = new Button(bs);

		int buttonHeight = KebabKing.getGlobalX(0.15f);
		int buttonWidth = buttonHeight;
		
		int imagePadX, imagePadY;
		
		if (this.currentPurchaseableIndex == index) {
			imagePadX = Assets.GREEN_9PATCH_OFFSET_X/2 - 4;
			imagePadY = (int) (Assets.GREEN_9PATCH_OFFSET_X/2 * 2.5f);
		}
		else { 
			imagePadX = Assets.GREEN_9PATCH_OFFSET_X/2 / 3;
			imagePadY = (int) (Assets.GREEN_9PATCH_OFFSET_X/2 * 2.5f);
		}
						
		TextureRegion full = purchaseable.getIcon();
		if (full == null) {
			full = Assets.questionMark;
		}

		int iconWidth = buttonWidth - imagePadX;
		int iconHeight = buttonHeight - imagePadY;

		// if the icon is wider than long, crop out appropriate part of image

		// crop to that aspect ratio
		int regWidth = full.getRegionWidth();
		int regHeight = full.getRegionHeight(); 
		float aspectButton = (iconWidth) * 1.0f / (iconHeight); 

		TextureRegion half;
		if (regWidth / regHeight > aspectButton) {
//			System.out.println("reg Width > regHeight" + regWidth + " , " + regHeight);
			float cropWidth = (aspectButton * regHeight);
			half = new TextureRegion(full, (int) (regWidth/2 - cropWidth/2), 0, (int) cropWidth, full.getRegionHeight());
		}
		// TODO when region is taller than wide, make this cleverly allow less vertical padding so it fits well inside the box
		else {
			float cropHeight = regHeight/aspectButton;
			half = new TextureRegion(full, 0, (int) (regHeight/2 - cropHeight/2), full.getRegionWidth(), (int) cropHeight);
		}
		Image icon = new Image(half);		
		button.add(icon).center().width(iconWidth).height(iconHeight);

		if (this.currentPurchaseableIndex == index) {
			Image check = new Image(Assets.purchaseableCheck);
			int checkWidth = (int) (buttonWidth/4.0f);
			int checkHeight = (int) (buttonHeight/4.0f);
			button.add(check).top().right().width(checkWidth).height(checkHeight).padLeft(-checkWidth).padTop(-checkHeight/2);
		}
		table.add(button).width(buttonWidth).height(buttonHeight).left();

		boolean lockedByRound = !type.isUnlockedByLevel(purchaseable);
		boolean locked = !type.isUnlocked(purchaseable);
		
		Color color = MainStoreScreen.FONT_COLOR;
		if (lockedByRound) {
			color = MainStoreScreen.FONT_COLOR_GRAY;
			button.add(new Image(Assets.gray9PatchSmallFilled)).width(buttonWidth).height(buttonHeight).padLeft(-buttonWidth + imagePadX);//.padTop(-buttonHeight + imagePadY);
		}
		else if (locked) {
			button.add(new Image(Assets.gray9PatchSmallFilled)).width(buttonWidth).height(buttonHeight).padLeft(-buttonWidth + imagePadX);//.padTop(-buttonHeight + imagePadY);
			button.add(new Image(Assets.marketLock)).width(buttonWidth).height(buttonHeight).padLeft(-buttonWidth);//.padTop(-buttonHeight + imagePadY);
		}
		
		Table info = new Table();
//		info.debugAll();
		int infoPad = KebabKing.getGlobalX(0.03f);
		int infoWidth = mainWidth - infoPad - buttonWidth;
		Label pTitle = new Label(purchaseable.getName(), Assets.generateLabelStyleUILight(24));
		pTitle.setColor(color);
		pTitle.setAlignment(Align.left);
		info.add(pTitle).left();
		
		// TODO add daily cost here
		Label pPrice1 = new Label("$" + floatToString(purchaseable.getDailyCost()), Assets.generateLabelStyleUIHeavyGreen(22));
		if (purchaseable.getDailyCost() <= 0) {
			pPrice1.setText(" ");
		}
//		pPrice1.setColor(MainStoreScreen.FONT_COLOR_GREEN);
		pPrice1.setAlignment(Align.right);
		info.add(pPrice1).right().expandX().fillX().bottom();
		
		Label pPrice2 = new Label(" / DAY", Assets.generateLabelStyleUIHeavyGreen(16));
		if (purchaseable.getDailyCost() <= 0) {
			pPrice2.setText(" ");
		}
		pPrice2.setAlignment(Align.right);
		info.add(pPrice2).right().bottom().padBottom(KebabKing.getGlobalY(0.002f));
		
		info.row();

		// if locked but not by round
		if (locked && !lockedByRound) {
			// add unlock button
			// create unlock button
			int unlockHeight = KebabKing.getGlobalY(0.04f);
			Button unlockButton = createUnlockButton(purchaseable, type, unlockHeight);
			Table unlockButtonTable = new Table();
			unlockButtonTable.add(unlockButton).height(unlockHeight).left();
			info.add(unlockButtonTable).colspan(3).left();
		}
		// otherwise just draw with gray
		else {
			Label pDesc = new Label(purchaseable.getDescription(), Assets.generateLabelStyleUILight(18));
			if (purchaseable.getDescription() == null || purchaseable.getDescription().length() == 0) {
				pDesc = new Label("???", Assets.generateLabelStyleUILight(16));
			}
			if (lockedByRound) 
				pDesc.setText("Available after Day " + purchaseable.unlockAtLevel());
			pDesc.setWrap(true);
			pDesc.setColor(color);
			pDesc.setAlignment(Align.left);
			info.add(pDesc).left().width(infoWidth).colspan(3);
		}
		table.add(info).expandX().left().padLeft(infoPad).fillX();

		// Should not be able to select a table if it hasn't been unlocked by cash or by round
		if (!locked) {
			table.addListener(new InputListener() {
				public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
					return true;
				}
				public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
					clickPurchaseableTable((Table) event.getListenerActor());
				}
			});	
		}
		// add a listener on everything except the unlock button
//		else if (!lockedByRound) {
//			button.addListener(new InputListener() {
//				public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
//					return true;
//				}
//				public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
//					clickPurchaseableTable((Table) event.getListenerActor().getParent());
//				}
//			});	
//		}
	}
	
	public String floatToString(float value) {
		if ((int) value == value) return "" + ((int) value);
		else return "" + value;
	}

	public void clickPurchaseableTable(Table table) {
		int index = -1;
		for (int i = 0; i < this.purchaseableTables.length; i++) {
			if (purchaseableTables[i] == table)
				index = i;
		}
		if (index == -1) throw new java.lang.AssertionError();
		this.selectPurchaseable(index);
	}

	//	// switches the table in the current table to the next one, left or right
	//	public void tableScroll(StoreScrollButton ssb, boolean left) {
	//		Purchaseable next = ssb.type.getNext(ssb.current, left);
	//
	//		// don't display the one that is selected
	//		if (next == ssb.type.getCurrentSelected()) next = ssb.type.getNext(next, left);
	//
	//		ssb.current = next;
	//		ssb.other.current = next;
	//		setCurrentPurchaseableTable(ssb.scroll, next, ssb.type);
	//	}

	public void selectPurchaseable(int index) {
		int oldSelected = currentPurchaseableIndex;
		this.currentPurchaseableIndex = index;
		this.updatePurchaseableTable(oldSelected);
		this.updatePurchaseableTable(index);
		this.currentPurchaseableTable = this.purchaseableTables[index];
		this.updateSelectedPurchaseableTable(purchaseables[index], types[currentTypeIndex]);
	}

	//	public void setCurrentPurchaseableTable(Table scrollAndNext, Purchaseable purchaseable, PurchaseType type) {
	//		Table newPurchaseable = createPurchaseableTable(purchaseable, type);
	//		//try to add upgrade select to "next" PurchaseableTable!
	//		TextButton newUBS = createUnlockSelectButton(purchaseable, type);
	//		newPurchaseable.row();
	//		newPurchaseable.add(newUBS); //.height(UnlockButtonHeight * unitHeight); //.padTop(0.5f * unitHeight);
	//
	//		scrollAndNext.getCell(scrollAndNext.findActor("next_table")).setActor(null);
	//		scrollAndNext.getCell(scrollAndNext.findActor("next_table")).setActor(newPurchaseable);
	//		newPurchaseable.setName("next_table");
	//		//		main.getCell(main.findActor("upgrade_select")).setActor(null);
	//		//		main.getCell(main.findActor("upgrade_select")).setActor(newUBS);
	//		//		newUBS.setName("upgrade_select");
	//	}

	private Button createUnlockButton(Purchaseable purchaseable, PurchaseType type, float height) {
		UnlockSelectButton button  = new UnlockSelectButton(type, purchaseable, this.getInventory());
		
//		// SELECT
//		if (type.isUnlocked(purchaseable)) {
//			button.setText("Select");
//			button.addListener(new InputListener() {
//				public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
//					return true;
//				}
//				public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
//					UnlockSelectButton usb = (UnlockSelectButton) event.getTarget();
//					usb.type.setCurrent(usb.purchaseable);
//					//					System.out.println("selected");
//					resetCurrentTable();
//				}
//			});	
//		}
//		// if unlocked but consumable TODO
//		//		else if (type)
		// UNLOCK
//		else {
		
		// it's actually two buttons next to each other
		// one says "unlock", the other says "$10" or "jade 10"
		Table bothButtons = new Table();
		Label unlock = new Label("Unlock", Assets.generateLabelStyleUIChinaWhite(26));
		unlock.setTouchable(Touchable.disabled);
		Table unlockTable = new Table();
		unlockTable.setBackground(new TextureRegionDrawable(Assets.marketGreen));
		unlockTable.add(unlock).padLeft(KebabKing.getGlobalX(0.01f)).padRight(KebabKing.getGlobalX(0.01f));
		bothButtons.add(unlockTable).width(KebabKing.getGlobalX(0.2f));
		
		Table priceTable = new Table();
		Label price = new Label("", Assets.generateLabelStyleUIChinaWhite(26));
		price.setTouchable(Touchable.disabled);
		if (purchaseable.coinsToUnlock() > 0) {
			priceTable.add(new Image(Assets.marketJade)).width(height);
			price.setText("" + purchaseable.coinsToUnlock() + "");
		}
		else {
			price.setText("$" + purchaseable.cashToUnlock() + "");
		}
		priceTable.setBackground(new TextureRegionDrawable(Assets.marketDarkGreen));
		priceTable.add(price).padLeft(KebabKing.getGlobalX(0.01f)).padRight(KebabKing.getGlobalX(0.01f));;
		
		bothButtons.add(priceTable);//.padLeft(padLeft);
		
		button.add(bothButtons);
		button.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
				UnlockSelectButton usb = (UnlockSelectButton) event.getListenerActor();
				attemptUnlock(usb.type, usb.purchaseable);
			}
		});	
//		}

		// disable button if haven't had enough rounds
//		if (!type.availableForUnlock(purchaseable)) {
//			button.setDisabled(true);
//			button.setTouchable(Touchable.disabled);
//		}

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
		if (!type.isUnlocked(purchaseable)) System.out.println("SHOULD BE UNLOCKED");
		else {
			System.out.println("successfully unlocked!");
		}
		
		// must update everything now TODO
		this.resetCurrentTable();
	}

	public void unlockFail(Purchaseable purchaseable) {
		System.out.println("You can't afford that!");
	}

	public Inventory getInventory() {
		return this.getProfile().inventory;
	}

	// creates a table of dimensions 8 x 3.5 describing the purchaseable in question
	public Table updateSelectedPurchaseableTable(Purchaseable purchaseable, PurchaseType type) {
		if (selectedPurchaseableTable == null) selectedPurchaseableTable = new Table();
		Table table = selectedPurchaseableTable;

		table.clear();
		if (purchaseable == null) {
			// TODO create "none" table
			return table;
			//				throw new java.lang.AssertionError();
		}
		table.top();
		//
		// first add image
		int imageHeight = KebabKing.getGlobalY(0.1f);

		// note this is a fixed value, corresponding to the green part of the image
		TextureRegion iconReg = purchaseable.getIcon();
		if (iconReg == null)
			iconReg = Assets.questionMark;
		int regHeight = 1;
		int regWidth = 2;
		if (iconReg != null) {
			regWidth = iconReg.getRegionWidth();
			regHeight = iconReg.getRegionHeight();
		}
		int imageWidth = imageHeight * regWidth / regHeight;

		// adjust for big images
		if (imageWidth > mainWidth) {
			imageWidth = mainWidth;
			imageHeight = imageWidth * regHeight / regWidth;
		}

		int iconPad = KebabKing.getGlobalY(0.05f);
		Image icon = new Image(iconReg); 
		
		table.add(icon).width(imageWidth).height(imageHeight).padTop(iconPad);
		table.row();
		Label title = new Label(purchaseable.getName(), Assets.generateLabelStyleUI(35));
		title.setColor(MainStoreScreen.FONT_COLOR);
		table.add(title);
		table.row();
		Label description = new Label(purchaseable.getDescription(), Assets.generateLabelStyleUILight(22));
		description.setWrap(false);
		description.setAlignment(Align.center);
		description.setColor(MainStoreScreen.FONT_COLOR);
		table.add(description).width(mainWidth);

		// TODO add daily cost!
		return table;		
	}

	// This table should have an array of PurchaseTypes that are selectable
	// it should have an index corresponding to which one is selected.

	// create and return a full table for the given purchase types with the given title
	public Table createTable(String titleString, PurchaseType[] types) {
		this.types = types;
		this.currentTypeIndex = 0;

		Table table = new Table();
//		table.debugAll();
		table.setWidth(KebabKing.getWidth());
		table.setHeight(KebabKing.getHeight());

		mainWidth = KebabKing.getGlobalX(0.8f);

		Table main = new Table();
//		main.debugAll();
		main.setWidth(mainWidth);
		main.setHeight(KebabKing.getGlobalY(0.7f));
		main.row();
		table.add(main).center().top().expandY();

		// add buttons for each table
		int buttonPad = KebabKing.getGlobalX(0.01f);
		int buttonWidth = mainWidth / types.length - buttonPad;
		int buttonHeight = KebabKing.getGlobalY(0.05f);

		typeButtons = new TextButton[types.length]; 
		Table buttonTable = new Table();
		for (int i = 0; i < types.length; i++) {
			TextButton button = new TextButton(types[i].getName(), Assets.getPurchaseTypeButtonStyle(50 / types.length));
			button.getLabel().setTouchable(Touchable.disabled);
			button.addListener(new InputListener() {
				public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
					return true;
				}
				public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
					switchToPurchaseType((TextButton) event.getTarget());
				}
			});	

			buttonTable.add(button).width(buttonWidth).height(buttonHeight).padRight(buttonPad/2).padLeft(buttonPad/2);
			typeButtons[i] = button;
		}
		// disable the first button
		if (types.length <= 1) {
			buttonTable.clear();
		}
		main.add(buttonTable).top().expandY().padTop(KebabKing.getGlobalY(0.1f));

		this.currentTypeIndex = 0;
		switchToPurchaseType();
		main.row();
		main.add(currentTypeTable).top().fillX().fillY();

		table.row();

		return table;
	}
	//
	//	// creates a table of dimensions 8 x 3.5 describing the purchaseable in question
	//	public Table createPurchaseableTable(Purchaseable purchaseable, PurchaseType type) {
	//		Table table = new Table();
	//
	//		Table obligatory = new Table();
	//
	//		// title takes up top row, 
	//		Label title;
	//
	//		if (purchaseable != null)
	//			title = new Label(purchaseable.getName(), Assets.getPurchaseableTitleLS()); 
	//		else 
	//			title = new Label("None", Assets.getPurchaseableTitleLS()); 
	//
	//		title.setAlignment(Align.center);
	//		table.add(title); //.height(1 * unitHeight).colspan(2);//.width(PurchaseableTableWidth * unitWidth)
	//		table.row();
	//
	//		//		// add icon
	//		//		if (purchaseable != null) {
	//		//			Image icon;
	//		//			if (purchaseable.getIcon() != null) {
	//		//				icon = new Image(purchaseable.getIcon());
	//		//			}
	//		//			else icon = new Image(Assets.getDefaultIcon());
	//		//
	//		//			//		obligatory.debugAll();
	//		//			// 1.5 + 1 + 
	//		//
	//		//			obligatory.add(icon).width((IconWidth-2*IconPad) * unitWidth).height((IconHeight - 2*IconPad) * unitHeight)
	//		//			.pad(IconPad * unitHeight, IconPad * unitWidth, IconPad * unitHeight, IconPad * unitWidth);
	//		//		}
	//
	//		// add description and cost per day to a subtable
	//		Table subtable = new Table();
	//		//		subtable.debugAll();
	//
	//		Label description;
	//		if (purchaseable != null) {
	//			description = new Label(purchaseable.getDescription(), Assets.getDescriptionLS());
	//		}
	//		else {
	//			description = new Label("", Assets.getDescriptionLS());
	//		}
	//		// description 
	//		description.setWrap(true); // hopefully wraps in parent
	//		subtable.add(description);//.width(DescriptionWidth * unitWidth).height(DescriptionHeight * unitHeight).left();
	//
	//		if (purchaseable != null) {
	//			// if it's a consumable (ad campaign) and the current selected one, then put a button for purchasing it 
	//			if (type == master.profile.inventory.adCampaign && type.getCurrentSelected() == purchaseable && purchaseable != AdCampaign.Campaign.LEVEL0) {
	//				UnlockSelectButton button  = new UnlockSelectButton(Assets.getUnlockButtonStyle(), type, purchaseable, this.getInventory());
	//				button.getLabel().setTouchable(Touchable.disabled);
	//
	//				button.addListener(new InputListener() {
	//					public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
	//						return true;
	//					}
	//					public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
	//						UnlockSelectButton usb = (UnlockSelectButton) event.getTarget();
	//						System.out.println(usb.type);
	//						System.out.println(usb.purchaseable);
	//						System.out.println(usb.inventory);
	//
	//						if (usb.inventory.purchaseConsumable(usb.purchaseable, usb.type)) {
	//							System.out.println("purchased " + usb.purchaseable.getName());
	//						}
	//						resetCurrentTable();
	//					}
	//				});	
	//				button.setText("Purchase ($" + purchaseable.getDailyCost() + ")");
	//
	//				subtable.row();
	//				subtable.add(button); //.width(DescriptionWidth * unitWidth).height(CostHeight * unitHeight);
	//			}
	//			else {
	//				// price per day
	//				Label pricePerDay = new Label("$" + purchaseable.getDailyCost() + " per day!", Assets.getCostLS());
	//				if (purchaseable.getDailyCost() == 0) pricePerDay.setText("");
	//				pricePerDay.setAlignment(Align.left);
	//				subtable.row();
	//				subtable.add(pricePerDay); // .width(DescriptionWidth * unitWidth).height(CostHeight * unitHeight);
	//			}
	//		}
	//
	//		// finally add to table
	//		obligatory.add(subtable); //.width(PurchaseableTableWidth * unitWidth).height(2.5f * unitHeight);
	//
	//		table.add(obligatory); //.width(PurchaseableTableWidth * unitWidth);
	//
	//		// don't forget to return
	//		return table;		
	//	}


	private void disableButton(int index) {
		typeButtons[index].setDisabled(true);
		typeButtons[index].setTouchable(Touchable.disabled);
	}
	private void enableButton(int index) {
		typeButtons[index].setDisabled(false);
		typeButtons[index].setTouchable(Touchable.enabled);
	}

	// this is the button that was clicked 
	private void switchToPurchaseType(TextButton listener) {
		//		System.out.println(listener.getText());
		int index = -1;
		for (int i = 0; i < typeButtons.length; i++) {
			if (typeButtons[i] == listener) index = i;
		}
		if (index == -1) throw new java.lang.AssertionError();
		this.currentTypeIndex = index;
		switchToPurchaseType();
	}

	// do everything 
	public void switchToPurchaseType() {
		// enable all buttons but disable this button
		for (int i = 0; i < typeButtons.length; i++) {
			enableButton(i);
		}
		disableButton(currentTypeIndex);
		PurchaseType toSwitchTo = types[currentTypeIndex];

		updatePurchaseTypeTable(toSwitchTo);
	}

	public void switchToMain() {
		mainTable.clear();
		Manager.analytics.sendEventHit("Store", "Switch To", "Main");
		this.currentTable = null;
		master.setScreen(mainStoreScreen);
	}

	public void switchTo(TableType switchToThis) {
		Table newTable = null;
		String name = "NoType";
		resetTable(switchToThis);

		switch(switchToThis) {
		//		case main:
		//			newTable = mainTable;
		//			name = "Main";
		//			break;
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
		case vanity:
			newTable = vanityTable;
			name = "Vanity";
			break;
		}

		mainTable.clear();
		mainTable.row();
		//		table.add(title).height(titleHeight);
		//		table.row();
		mainTable.add(newTable).expandY().top();
		mainTable.row();

		Button back = newBackButton();
		mainStoreScreen.addBackButton(back, mainTable);

		//		Button back = newBackButton();
		//		//		table.debugAll();
		//		int backPad = ChuanrC.getGlobalY(0.01f);
		//		mainTable.add(back).bottom().right().expandX().width(ChuanrC.getGlobalX(MainStoreScreen.BACK_BUTTON_WIDTH)).height(ChuanrC.getGlobalY(MainStoreScreen.BACK_BUTTON_HEIGHT)).padBottom(ChuanrC.getGlobalY(MainStoreScreen.BACK_BOTTOM_PAD)).padTop(backPad); 
		//.height(BackButtonHeight * unitHeight).width(4*unitWidth).right().padRight(0.5f*unitWidth).padBottom(BackButtonPad*unitHeight);

		this.currentTable = switchToThis;

		Manager.analytics.sendEventHit("Store", "Switch To", name);
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
