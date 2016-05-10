package com.kebabking.game;

import org.apache.commons.lang3.text.WordUtils;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.kebabking.game.Purchases.PurchaseType;
import com.kebabking.game.Purchases.Purchaseable;

// represents a table like "food" 
public class StorePurchaseTypeSubtable extends Table {
	static int SELECTED_PURCHASEABLE_TITLE_SIZE = 35;
	static int SELECTED_PURCHASEABLE_DESCRIPTION_SIZE = 18;
	
	StoreSubtable parent;
	KebabKing master;
	
//	Table selectedPurchaseableTa/ble;
	Purchaseable[] purchaseables;
	
	// currently selected purchaseable
//	Deque<Integer> currentPurchaseableIndices;
	
//	int currentPurchaseableIndex;
//	Table currentPurchaseableTable;
//	ArrayList<Table> currentPurchaseableTables;
	
//	Table[] purchaseableTables;
	StorePurchaseableTable[] purchaseableTables;
	PurchaseType type;
	
	int selectedIndex;
//	int typeIndex;
	
//	HashSet<Table> selectedTables;
	Table typeSummary;
	
	int mainWidth;
	
	boolean needsInitialization;
	int needsPurchaseableUpdate;
	
	int checksCurrentlyDrawn; // count of purchaseables that are drawn with green check.

	// create 
	public StorePurchaseTypeSubtable(StoreSubtable parent, PurchaseType type, int mainWidth) {
		System.out.println("CREATING NEW purchaseType table");

		this.parent = parent;
		this.master = parent.master;
		this.type = type;
		this.mainWidth = mainWidth;
				
		this.purchaseables = type.values;
//		purchaseableTables = new Table[purchaseables.length];
		purchaseableTables = new StorePurchaseableTable[purchaseables.length];
		
		this.selectedIndex = -1;
		
		for (int i = 0; i < purchaseables.length; i++) {
			purchaseableTables[i] = new StorePurchaseableTable(master, this, purchaseables[i], mainWidth, i);
			if (type.isSelected(i)) {
				System.out.println(type.values[i].getName());
				purchaseableTables[i].select();
			}
		}
		
		this.needsInitialization = true;
	}
	
	// creates a table of dimensions 8 x 3.5 describing the purchaseable in question
	// There should be selected Purchaseable tables for each Purchasetype
	private void updateTypeSummaryTable(PurchaseType type) {
//		System.out.println("UPDATING SELECTED PURCHASEABLE TABLE");
//		Purchaseable purchaseable = null;
		System.out.println("updating type summary table");
		if (typeSummary == null) typeSummary = new Table();
		typeSummary.clear();
		
		typeSummary.top();
		
		// first add image
		int imageHeight = KebabKing.getGlobalY(0.15f);

		// note this is a fixed value, corresponding to the green part of the image
		TextureRegion iconReg;
//		if (purchaseable != null)
//			iconReg = purchaseable.getIcon();
//		else
		float padAboveTitle = 0;
		if (type.icon == null) {
//			iconReg = type.values[0].getIcon();
			padAboveTitle = KebabKing.getGlobalYFloat(0.1f);
		}
		else {
			iconReg = type.icon;

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

			typeSummary.add(icon).width(imageWidth).height(imageHeight).padTop(iconPad);
			typeSummary.row();

		}
		String titleText;
//		if (purchaseable != null) titleText = purchaseable.getName();
//		else {
			// TODO handle this better if more consumables added
			titleText = type.getName();
			titleText = titleText.toLowerCase();
			titleText = WordUtils.capitalize(titleText);			
//		}
		
		Label title = new Label(titleText, Assets.generateLabelStyleUI(SELECTED_PURCHASEABLE_TITLE_SIZE, titleText));
		title.setColor(MainStoreScreen.FONT_COLOR);
		typeSummary.add(title).padTop(padAboveTitle);
		typeSummary.row();
		
		String descText;
//		if (purchaseable != null) descText = purchaseable.getDescription();
//		else 
			descText = type.getDescription();
		
		Label description = new Label(descText, Assets.generateLabelStyleUILight(SELECTED_PURCHASEABLE_DESCRIPTION_SIZE, descText));
		description.setWrap(true);
		description.setAlignment(Align.center);
		description.setColor(MainStoreScreen.FONT_COLOR);
		typeSummary.add(description).width(mainWidth);

		System.out.println("type summary table updated!");
	}
	
	public ProfileInventory getInventory() {
		return parent.getInventory();
	}

	public void initialize() {
		System.out.println("creating purchase type table");

		needsInitialization = false;
		needsPurchaseableUpdate = -1;
		this.clear();
		
		// add section for currently selected item
		updateTypeSummaryTable(type);
		int selectedPad = KebabKing.getGlobalY(0.02f);
		this.add(typeSummary).fillX().padBottom(selectedPad).width(mainWidth);
		
		// if ads table, create special top table
//		if (type == master.profile.inventory.adCampaign) selectPurchaseable(-1);

		// now draw list of all purchaseable options
		this.purchaseables = type.values;
//		this.currentPurchaseableIndex = -1;
//		for (int i = 0; i < purchaseables.length; i++) {
//			if (purchaseables[i] == type.getCurrentSelected()) this.currentPurchaseableIndex = i;
//		}
		// currentPurchaseableIndex might equal -1

		System.out.println("creating purchaseable tables array");
//		this.purchaseableTables = new Table[purchaseables.length];
//		this.purchaseableTables2 = new StorePurchaseableTable[purchaseables.length];

		int purchaseablePad = KebabKing.getGlobalY(0.015f);

		System.out.println("creating purchaseable tables array: " + purchaseables.length);
		Table purchaseableListTable = new Table();
		for (int i = 0; i < purchaseables.length; i++) {
			System.out.println("creating purchaseable list table: " + i);
			//			System.out.println("updating purchaseable table " + i);
			purchaseableListTable.add(purchaseableTables[i]).expandX().left().padTop(purchaseablePad).fillX();
			purchaseableListTable.row();
		}

		this.row();
		System.out.println("creating scrollpane");
		ScrollPane sp = new ScrollPane(purchaseableListTable, Assets.getSPS());
		sp.setScrollingDisabled(true, false);
		this.add(sp).expandX().fillX();

		System.out.println("purchase type table updated");
	}
	
	public void updateAllUnlocks() {
		System.out.println("updating all unlocks");
		for (int i = 0; i < purchaseableTables.length; i++) {
			purchaseableTables[i].updateForUnlock(false);
		}
	}
	
	public void updatePurchaseableAfterUnlock(Purchaseable toUpdate) {
		System.out.println("Updating purchaseable after unlock: " + toUpdate.getName());
		int index = -1;
		for (int i = 0; i < type.values.length; i++) {
			if (type.values[i] == toUpdate) {
				index = i;
				break;
			}
		}
		if (index >= 0) {
//			purchaseableTables2[index].updateForDeselect();
			purchaseableTables[index].updateForUnlock(false);
		}
		else {
			System.out.println("index less than 1");
		}
	}
	
//	public void switchTo() {
//		updateAllUnlocks();
//	}

}
