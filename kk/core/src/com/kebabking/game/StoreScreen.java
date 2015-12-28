package com.kebabking.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
//import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kebabking.game.Managers.Manager;
import com.kebabking.game.Purchases.Inventory;
import com.kebabking.game.Purchases.PurchaseType;
import com.kebabking.game.Purchases.Purchaseable;

public class StoreScreen extends ActiveScreen {
	static float UNITS_WIDTH = 12f;
	static float UNITS_HEIGHT = 20f;
	static float TitleHeight = 2.15f;
	static int SELECTED_PURCHASEABLE_TITLE_SIZE = 35;
	static int PURCHASEABLE_TITLE_SIZE = 24;
	static int SELECTED_PURCHASEABLE_DESCRIPTION_SIZE = 18;
	static int PURCHASEABLE_DESCRIPTION_SIZE = 16;
	static int DAILY_COST_SIZE = 22;
	static int PER_DAY_SIZE = 16;
	static int UNLOCK_SIZE = 26;

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
	Button[] typeButtons;
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
		super(master);
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

		uiStage.addActor(mainTable);

		// add title
		mainTable.row();

		//		setupMainTable();
		//		switchTo(TableType.main);

		initializeTables();

		//		switchTo(TableType.)

		//		master.profile.inventory.adCampaign.setTable(this, TableType.ads);
		//		master.profile.inventory.meatQuality.setTable(this, TableType.food);
		//		master.profile.inventory.drinkQuality.setTable(this, TableType.food);
		//		master.profile.inventory.locationType.setTable(this, TableType.map);
		//		master.profile.inventory.grillSpecs.getGrillSize().setTable(this, TableType.grill);
		//		master.profile.inventory.grillSpecs.getType().setTable(this, TableType.grill);
		//		master.profile.inventory.grillStand.setTable(this,  TableType.vanity);
		//		master.profile.inventory.decorations.setTable(this,  TableType.vanity);
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
			foodTable = createTable("Food", new PurchaseType[] {master.profile.inventory.meatQuality, master.profile.inventory.drinkQuality,}); //master.profile.inventory.stickType});
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
		super.renderWhiteAlpha(delta, 0.6f, uiStage);

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

	public void updatePurchaseTypeTable(PurchaseType type) {
		if (this.currentTypeTable == null) this.currentTypeTable = new Table();
		this.currentTypeTable.clear();

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

		boolean lockedByRound = !type.unlockIfReady(purchaseable);
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
		Label pTitle = new Label(purchaseable.getName(), Assets.generateLabelStyleUILight(PURCHASEABLE_TITLE_SIZE, Assets.alpha));
		pTitle.setColor(color);
		pTitle.setAlignment(Align.left);
		info.add(pTitle).left();

		Label pPrice1 = new Label("$" + floatToString(purchaseable.getDailyCost()), Assets.generateLabelStyleUIHeavyWhite(DAILY_COST_SIZE, Assets.nums + "$"));
		pPrice1.setColor(MainStoreScreen.FONT_COLOR_GREEN);
		if (purchaseable.getDailyCost() <= 0) {
			pPrice1.setText(" ");
		}
		//		pPrice1.setColor(MainStoreScreen.FONT_COLOR_GREEN);
		pPrice1.setAlignment(Align.right);
		info.add(pPrice1).right().expandX().fillX().bottom();

		Label pPrice2 = new Label(" / DAY", Assets.generateLabelStyleUIHeavyWhite(PER_DAY_SIZE, " / DAY"));
		pPrice2.setColor(MainStoreScreen.FONT_COLOR_GREEN);
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
			Label pDesc = new Label(purchaseable.getDescription(), Assets.generateLabelStyleUILight(PURCHASEABLE_DESCRIPTION_SIZE, Assets.allChars));
			if (purchaseable.getDescription() == null || purchaseable.getDescription().length() == 0) {
				pDesc = new Label("???", Assets.generateLabelStyleUILight(PURCHASEABLE_DESCRIPTION_SIZE, "???"));
			}
			if (lockedByRound) 
				pDesc.setText("Available at level " + purchaseable.unlockAtLevel());
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
		Label unlock = new Label("UNLOCK", Assets.generateLabelStyleUIChinaWhite(UNLOCK_SIZE, "UNLOCK"));
		unlock.setTouchable(Touchable.disabled);
		Table unlockTable = new Table();
		unlockTable.setBackground(new TextureRegionDrawable(Assets.marketGreen));
		unlockTable.add(unlock).padLeft(KebabKing.getGlobalX(0.01f)).padRight(KebabKing.getGlobalX(0.01f));
		bothButtons.add(unlockTable).width(KebabKing.getGlobalX(0.2f));

		Table priceTable = new Table();
		Label price = new Label("", Assets.generateLabelStyleUIChinaWhite(26, Assets.nums + "$"));
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
		Label title = new Label(purchaseable.getName(), Assets.generateLabelStyleUIWhite(SELECTED_PURCHASEABLE_TITLE_SIZE, Assets.alpha));
		title.setColor(MainStoreScreen.FONT_COLOR);
		table.add(title);
		table.row();
		Label description = new Label(purchaseable.getDescription(), Assets.generateLabelStyleUILight(SELECTED_PURCHASEABLE_DESCRIPTION_SIZE, Assets.allChars));
		description.setWrap(false);
		description.setAlignment(Align.center);
		description.setColor(MainStoreScreen.FONT_COLOR);
		table.add(description).width(mainWidth);

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

		typeButtons = new Button[types.length];
		Table buttonTable = new Table();
		if (types.length > 1) {
			for (int i = 0; i < types.length; i++) {
				Button button = new Button(Assets.getPurchaseTypeButtonStyle());

				// 
				Label label = new Label(types[i].getName(), getLabelStyleForButtonCount(types.length)); // so 48, 24, 16, 12, 9
				label.setColor(MainStoreScreen.FONT_COLOR);
				button.add(label);
				label.setTouchable(Touchable.disabled);

				button.addListener(new InputListener() {
					public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
						return true;
					}

					public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
						switchToPurchaseType((Button) event.getTarget());
					}
				});

				buttonTable.add(button).width(buttonWidth).height(buttonHeight).padRight(buttonPad / 2).padLeft(buttonPad / 2);
				typeButtons[i] = button;
			}
		}
		main.add(buttonTable).top().expandY().padTop(KebabKing.getGlobalY(0.1f));

		this.currentTypeIndex = 0;
		switchToPurchaseType();
		main.row();
		main.add(currentTypeTable).top().fillX().fillY();

		table.row();

		return table;
	}

	private void disableButton(int index) {
		typeButtons[index].setDisabled(true);
		((Label) typeButtons[index].getChildren().first()).setStyle(getLabelStyleForButtonCount(typeButtons.length));
		((Label) typeButtons[index].getChildren().first()).setColor(Color.WHITE);
		typeButtons[index].setTouchable(Touchable.disabled);
	}
	private void enableButton(int index) {
		typeButtons[index].setDisabled(false);
		((Label) typeButtons[index].getChildren().first()).setStyle(getLabelStyleForButtonCount(typeButtons.length));
		((Label) typeButtons[index].getChildren().first()).setColor(MainStoreScreen.FONT_COLOR);
		typeButtons[index].setTouchable(Touchable.enabled);
	}

	private LabelStyle getLabelStyleForButtonCount(int count) {
		if (count == 1)
			throw new java.lang.AssertionError();
		else if (count == 2) {
//			if (!enabled)
				return Assets.generateLabelStyleUIChinaWhite(30, Assets.upper);
//			else 
//				return Assets.generateLabelStyleUIChina(30);
		}
		else if (count == 3) {
//			if (!enabled)
				return Assets.generateLabelStyleUIChinaWhite(22, Assets.upper);
//			else 
//				return Assets.generateLabelStyleUIChina(22);
		}
		else if (count == 4) {
//			if (!enabled)
				return Assets.generateLabelStyleUIChinaWhite(18, Assets.upper);
//			else 
//				return Assets.generateLabelStyleUIChina(18);
		}
		else if (count == 5) {
//			if (!enabled)
				return Assets.generateLabelStyleUIChinaWhite(15, Assets.upper);
//			else 
//				return Assets.generateLabelStyleUIChina(15);
		}
		return null;
	}

	// this is the button that was clicked 
	private void switchToPurchaseType(Button listener) {
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
		if (typeButtons.length > 1) {
			for (int i = 0; i < typeButtons.length; i++) {
				enableButton(i);
			}
			disableButton(currentTypeIndex);
		}
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
