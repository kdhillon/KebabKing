package com.kebabking.game;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
//import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.kebabking.game.Managers.Manager;
import com.kebabking.game.Purchases.PurchaseType;
import com.kebabking.game.Purchases.Purchaseable;


/// this class mananges and switches between the 6 different tables displayed by mainStoreScreen 
public class StoreScreen extends ActiveScreen {
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

	Table mainTable; // main table

	StoreSubtable foodTable;
	StoreSubtable grillTable;
	StoreSubtable mapTable;
	StoreSubtable adsTable;
	JewelerTable jadeTable;
	Table vanityTable;
	
	// tables should be created only when they're first opened.
	//		this means initializing the first purchasetype table for each one
	//		subsequent ones can be initialized when they are switched to?
	// otherwise they should be null
	
	public enum TableType {food, grill, map, ads, jade, vanity}; // keeps track of which table is currently selected

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
//
//	PurchaseType[] types;
//	Button[] typeButtons;
//	int currentTypeIndex;
//	Table currentTypeTable;

//	Table selectedPurchaseableTable;
//	Purchaseable[] purchaseables;
	// currently selected purchaseable
//	int currentPurchaseableIndex;
//	Table currentPurchaseableTable;
//	Table[] purchaseableTables;
	
	PurchaseType[] foodTypes = new PurchaseType[] {master.profile.inventory.meatQuality, master.profile.inventory.drinkQuality, master.profile.inventory.skewerType};
	PurchaseType[] grillTypes = new PurchaseType[] {master.profile.inventory.grillSize, master.profile.inventory.grillType, master.profile.inventory.grillStand};
	PurchaseType[] locationTypes = new PurchaseType[] {master.profile.inventory.locationType};
	PurchaseType[] adTypes = new PurchaseType[] {master.profile.inventory.adCampaign};
	
	PurchaseType[][] allTypes = new PurchaseType[][] {foodTypes, grillTypes, locationTypes, adTypes};
	
	// maps from the above tables to the corresponding subtables
	HashMap<PurchaseType[], StoreSubtable> typeTables = new HashMap<PurchaseType[], StoreSubtable>();
	HashMap<PurchaseType[], TableType> typeEnums = new HashMap<PurchaseType[], TableType>();
	
	// assume screen is 480x800 (6/10)
	// divide into grid of size 12/20.
	//	float unitWidth; // 
	//	float unitHeight;

	// TODO make private classes for Unlock button and Select button

	// can only access storescreen from summary screen and main menu screen (direct transition after summary screen)
	// uses scene2d for menus
	public StoreScreen(KebabKing master, MainStoreScreen mainStoreScreen) {
		super(master, true);
		this.mainStoreScreen = mainStoreScreen;

		this.tableWidth = KebabKing.getWidth();
		this.tableHeight = KebabKing.getHeight();

		// initialize unit width and height, useful for making layouts
		//		this.unitWidth = ChuanrC.getWidth() / UNITS_WIDTH;
		//		this.unitHeight = ChuanrC.getHeight() / UNITS_HEIGHT;
		//

		mainTable = new Table();
		//		mainTable.debugAll();
		mainTable.align(Align.top);
		mainTable.setSize(tableWidth, tableHeight);

		mainTable.setPosition((KebabKing.getWidth() - tableWidth) / 2, 0);

		uiStage.addActor(mainTable);

		// add title
		mainTable.row();

		initializeTables();
		
		initializeHashMap();
	}
	
	public void initializeHashMap() {
		typeTables.put(foodTypes, foodTable);
		typeTables.put(grillTypes, grillTable);
		typeTables.put(locationTypes, mapTable);
		typeTables.put(adTypes, adsTable);
		
		// TODO simplify this
		typeEnums.put(foodTypes, TableType.food);
		typeEnums.put(grillTypes, TableType.grill);
		typeEnums.put(locationTypes, TableType.map);
		typeEnums.put(adTypes, TableType.ads);
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
	
	public PurchaseType[] getContainingTypeArray(PurchaseType type) {
		for (PurchaseType[] p : allTypes) {
			for (int i = 0; i < p.length; i++) {
				if (p[i] == type) {
					return p;
				}
			}
		}
		return null;
	}
	
	public StoreSubtable getSubtableForType(PurchaseType type) {
		PurchaseType[] p = getContainingTypeArray(type);
		return typeTables.get(p);
	}

	// TODO, change this to only update the subtable we're worried about, so it doesn't scroll up.
	public void resetTable(TableType type) {
		switch(type) {
		//		case main:
		//			break;
		case food:
			//			System.out.println("initializeing food");
			foodTable = new StoreSubtable(foodTypes, this); //master.profile.inventory.stickType});
			break;
		case grill:
			grillTable = new StoreSubtable(grillTypes, this);
			break;
		case map:
			mapTable = new StoreSubtable(locationTypes, this);
			break;
		case ads:
			adsTable = new StoreSubtable(adTypes, this);
			break;
//		case vanity:
//			vanityTable = createTable("Vanity", new PurchaseType[] {master.profile.inventory.decorations, });
//			break;
		case jade:
			//coinsTable = createTable("Coins", new PurchaseType[] {master.profile.inventory.meatQuality, master.profile.inventory.grillSpecs.getType(), master.profile.inventory.grillSpecs.getGrillSize()});
			jadeTable = new JewelerTable(master);
			break;
		default:
			break;
		}
	}

	@Override
	public void render(float delta) {
//		System.out.println("rendering store table");
		if (this.currentTable == TableType.jade) 
			super.renderGrayAlpha(delta, 0.8f);
		else
			super.renderWhiteAlpha(delta, DrawUI.WHITE_ALPHA);

//		drawStore(batch);

		if (Gdx.input.isKeyJustPressed(Keys.BACK)) {
			clickBack();
		}
	}

	// needed for scroll panes
	@Override
	public void update(float delta, boolean ff) {
		super.update(delta, ff);
		uiStage.act(delta);
	}

//	public void drawStore(SpriteBatch batch) {
//		uiStage.draw();
//	}

	public Table newBackButton() {
		Table backButton = mainStoreScreen.newBackButtonInit();

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

	public void updateAll() {
		foodTable.markAllForUpdate();
		grillTable.markAllForUpdate();;
		mapTable.markAllForUpdate();
		adsTable.markAllForUpdate();
	}
	
	// update the purchasetype subtable for this purchaseable
	public void updateTableFor(Purchaseable p) {
		StoreSubtable subtable = getSubtableForType(p.getType());
		if (subtable == null) {
			System.out.println("Wanting to update " + p.getName() + " but it doesn't exist");
			return;
		}
		subtable.updateTableFor(p);
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

	public ProfileInventory getInventory() {
		return this.getProfile().inventory;
	}

	// This table should have an array of PurchaseTypes that are selectable
	// it should have an index corresponding to which one is selected.


	public void switchToMain() {
		mainTable.clear();
		Manager.analytics.sendEventHit("Store", "Switch To", "Main");
		this.currentTable = null;
		master.setScreen(mainStoreScreen);
	}
	
//	public void preparePurchaseTypes(PurchaseType[] types) {
//		this.types = types;
//		this.currentTypeIndex = 0;
//	}

	
	public void switchTo(TableType switchToThis) {
		Table newTable = null;
		String name = "NoType";
		
//		resetTable(switchToThis);

		// TODO somehow consolidate the enum "TableType" and the PurchaseType[] arrays
		switch(switchToThis) {
		//		case main:
		//			newTable = mainTable;
		//			name = "Main";
		//			break;
		case food:
//			preparePurchaseTypes(foodTypes);
			newTable = foodTable;
			foodTable.updateCurrent();
//			name = "Food";
			break;
		case grill:
//			preparePurchaseTypes(grillTypes);
			newTable = grillTable;
			name = "Grill";
			grillTable.updateCurrent();
			break;
		case map:
//			preparePurchaseTypes(locationTypes);
			newTable = mapTable;
			name = "Map";
			mapTable.updateCurrent();
			break;
		case ads:
//			preparePurchaseTypes(adTypes);
			newTable = adsTable;
			name = "Ads";
			adsTable.updateCurrent();
			break;
		case jade:
			newTable = jadeTable;
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
		
		if (newTable == jadeTable) {
//			mainTable.setSize(KebabKing.getWidth(), KebabKing.getHeight());
			mainTable.setBackground(new TextureRegionDrawable(Assets.getTextureRegion("market/Jeweler-12")));
		}
		else {
			TextureRegionDrawable nu = null;
			mainTable.setBackground(nu);
		}


		Table back = newBackButton();
		mainStoreScreen.addBackButton(back, mainTable);

		//		Button back = newBackButton();
		//		//		table.debugAll();
		//		int backPad = ChuanrC.getGlobalY(0.01f);
		//		mainTable.add(back).bottom().right().expandX().width(ChuanrC.getGlobalX(MainStoreScreen.BACK_BUTTON_WIDTH)).height(ChuanrC.getGlobalY(MainStoreScreen.BACK_BUTTON_HEIGHT)).padBottom(ChuanrC.getGlobalY(MainStoreScreen.BACK_BOTTOM_PAD)).padTop(backPad); 
		//.height(BackButtonHeight * unitHeight).width(4*unitWidth).right().padRight(0.5f*unitWidth).padBottom(BackButtonPad*unitHeight);

		this.currentTable = switchToThis;

		Manager.analytics.sendEventHit("Store", "Switch To", name);
	}
	
	public void switchToType(PurchaseType type) {
		PurchaseType[] p = getContainingTypeArray(type);
		switchTo(typeEnums.get(p));
		StoreSubtable s = typeTables.get(p);
		s.switchToPurchaseType(type);
	}

	public Profile getProfile() {
		return master.profile;
	}
	
	public void campaignEnded() {
		System.out.println("campaign ended, updating purchase type table for ad campaign");
		adsTable.updatePurchaseTypeTable(master.profile.inventory.adCampaign);
	}
}
