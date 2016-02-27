package com.kebabking.game;

import org.apache.commons.lang3.text.WordUtils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.kebabking.game.Purchases.PurchaseType;
import com.kebabking.game.Purchases.PurchaseTypeConsumable;
import com.kebabking.game.Purchases.Purchaseable;
import com.kebabking.game.Purchases.SimpleConsumable;

// represents a table like "food" 
public class StorePurchaseTypeSubtable extends Table {
	static int SELECTED_PURCHASEABLE_TITLE_SIZE = 35;
	static int PURCHASEABLE_TITLE_SIZE = 24;
	static int SELECTED_PURCHASEABLE_DESCRIPTION_SIZE = 18;
	static int PURCHASEABLE_DESCRIPTION_SIZE = 16;
	static int DAILY_COST_SIZE = 22;
	static int PER_DAY_SIZE = 16;
	static int UNLOCK_SIZE = 26;
	
	StoreSubtable parent;
	KebabKing master;
	
//	Table selectedPurchaseableTa/ble;
	Purchaseable[] purchaseables;
	
	// currently selected purchaseable
//	Deque<Integer> currentPurchaseableIndices;
	
//	int currentPurchaseableIndex;
//	Table currentPurchaseableTable;
//	ArrayList<Table> currentPurchaseableTables;
	
	Table[] purchaseableTables;
	PurchaseType type;
//	int typeIndex;
	
//	HashSet<Table> selectedTables;
	Table typeSummary;
	
	int mainWidth;
	
	boolean needsUpdate;
	
	// create 
	public StorePurchaseTypeSubtable(StoreSubtable parent, PurchaseType type, int mainWidth) {
		this.parent = parent;
		this.master = parent.master;
		this.type = type;
		this.mainWidth = mainWidth;
				
		this.purchaseables = type.values;
		purchaseableTables = new Table[purchaseables.length];
		
//		if (type.allowsMultipleSelect()) {
////			currentPurchaseableTables = new ArrayList<Table>();
//			currentPurchaseableIndices = new Deque<Integer>();
//		
//		}
//		if (!type.consumable)
//			selectPurchaseable(0);
//		else selectPurchaseable(-1);
	}
	
	public Table createPurchaseableTable(int index) {
		purchaseableTables[index] = new Table();
		updatePurchaseableTable(index);
		return purchaseableTables[index];
	}

	public String floatToString(float value) {
		if ((int) value == value) return "" + ((int) value);
		else return "" + value;
	}
	
	private int getIndex(Purchaseable p) {
		int index = -1;
		for (int i = 0; i < this.purchaseableTables.length; i++) {
			if (purchaseables[i] == p)
				index = i;
		}
		return index;
	}

	public void clickPurchaseableTable(Table table) {
		int index = -1;
		for (int i = 0; i < this.purchaseableTables.length; i++) {
			if (purchaseableTables[i] == table)
				index = i;
		}
		if (index == -1) throw new java.lang.AssertionError();
		
		if (!type.consumable)
			this.selectPurchaseable(index);
	}

	// you shouldn't be able to select consumables
	public void selectPurchaseable(int index) {
//		System.out.println("Selecting: " + index);
//		System.out.println();
		
		if (index < 0) {
			updateTypeSummaryTable(type);
			return;
		}
		
		int removed = type.addToSelected(index);
		updatePurchaseableTable(index);
		if (removed > -1) updatePurchaseableTable(removed);
		
		this.updateTypeSummaryTable(type);

		// hacky
		if (type == master.profile.inventory.locationType) {
			// note that this replaces any existing ad campaign.
			System.out.println("resettting distribution");
			master.cm.updateCustomerDistribution();
		}
		
		master.save();
	}
	
	// creates a table of dimensions 8 x 3.5 describing the purchaseable in question
	// There should be selected Purchaseable tables for each Purchasetype
	private void updateTypeSummaryTable(PurchaseType type) {
//		System.out.println("UPDATING SELECTED PURCHASEABLE TABLE");
//		Purchaseable purchaseable = null;
		
		if (typeSummary == null) typeSummary = new Table();
		typeSummary.clear();
		
		typeSummary.top();
		
		// first add image
		int imageHeight = KebabKing.getGlobalY(0.1f);

		// note this is a fixed value, corresponding to the green part of the image
		TextureRegion iconReg;
//		if (purchaseable != null)
//			iconReg = purchaseable.getIcon();
//		else
		if (type.icon == null)
			iconReg = type.values[0].getIcon();
		else 
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
		String titleText;
//		if (purchaseable != null) titleText = purchaseable.getName();
//		else {
			// TODO handle this better if more consumables added
			titleText = type.getName();
			titleText = titleText.toLowerCase();
			titleText = WordUtils.capitalize(titleText);			
//		}
		
		Label title = new Label(titleText, Assets.generateLabelStyleUIWhite(SELECTED_PURCHASEABLE_TITLE_SIZE, Assets.alpha));
		title.setColor(MainStoreScreen.FONT_COLOR);
		typeSummary.add(title);
		typeSummary.row();
		
		String descText;
//		if (purchaseable != null) descText = purchaseable.getDescription();
//		else 
			descText = type.getDescription();
		
		Label description = new Label(descText, Assets.generateLabelStyleUILight(SELECTED_PURCHASEABLE_DESCRIPTION_SIZE, Assets.allChars));
		description.setWrap(true);
		description.setAlignment(Align.center);
		description.setColor(MainStoreScreen.FONT_COLOR);
		typeSummary.add(description).width(mainWidth);
	}
	
	public ProfileInventory getInventory() {
		return parent.getInventory();
	}
	
	private Button createUnlockButton(Purchaseable purchaseable, PurchaseType type, float height) {
		UnlockSelectButton button  = new UnlockSelectButton(type, purchaseable, this.getInventory());

		// it's actually two buttons next to each other
		// one says "unlock", the other says "$10" or "jade 10"
		Table bothButtons = new Table();
		String toWrite = "UNLOCK";
		if (type.consumable) {
			toWrite = "ACTIVATE";
		}
		Label unlock = new Label(toWrite, Assets.generateLabelStyleUIChinaWhite(UNLOCK_SIZE, "UNLOCK ACTIVATE"));
		unlock.setTouchable(Touchable.disabled);
		Table unlockTable = new Table();
		
		if (master.profile.inventory.canAffordPurchase(type, purchaseable))
			unlockTable.setBackground(new TextureRegionDrawable(Assets.marketGreen));
		else
			unlockTable.setBackground(new TextureRegionDrawable(Assets.grayLight));
			
		unlockTable.add(unlock).padLeft(KebabKing.getGlobalX(0.01f)).padRight(KebabKing.getGlobalX(0.01f)).fill();
		bothButtons.add(unlockTable).width(KebabKing.getGlobalX(0.2f)).fill();

		Table priceTable = new Table();
		
		if (purchaseable.cashToUnlock() > 0 || purchaseable.coinsToUnlock() <= 0) {
			Table priceCashTable = new Table();
			
			// need a special label for this, in case it's not in the english font we're using
			Label currencySymbol = new Label(Assets.currencyChar, Assets.generateLabelStyleUIWhite(22, Assets.currencyChar));
			Label priceCash = new Label("", Assets.generateLabelStyleUIChinaWhite(26, Assets.nums));
			priceCash.setTouchable(Touchable.disabled);
			
			if (master.profile.getCash() >= purchaseable.cashToUnlock()) {
				priceCashTable.setBackground(new TextureRegionDrawable(Assets.marketDarkGreen));
			}
			else {
				priceCashTable.setBackground(new TextureRegionDrawable(Assets.gray));			
			}

			priceCash.setText(purchaseable.cashToUnlock() + "");
			priceCashTable.add(currencySymbol).padLeft(KebabKing.getGlobalX(0.01f)).padRight(KebabKing.getGlobalX(0.005f));
			priceCashTable.add(priceCash).padRight(KebabKing.getGlobalX(0.01f));
			priceTable.add(priceCashTable).expand().fill();;
		}
	
		if (purchaseable.coinsToUnlock() > 0) {
			Table priceCoinsTable = new Table();
			Label priceCoins = new Label("", Assets.generateLabelStyleUIChinaWhite(26, Assets.nums));
			priceCoins.setTouchable(Touchable.disabled);
			
			if (master.profile.getCoins() >= purchaseable.coinsToUnlock()) {
				priceCoinsTable.setBackground(new TextureRegionDrawable(Assets.marketDarkGreen));
			}
			else {
				priceCoinsTable.setBackground(new TextureRegionDrawable(Assets.gray));			
			}
			
			priceCoinsTable.add(new Image(Assets.marketJade)).width(height);
			priceCoins.setText("" + purchaseable.coinsToUnlock() + "");
			priceCoinsTable.add(priceCoins).padLeft(KebabKing.getGlobalX(-0.005f)).padRight(KebabKing.getGlobalX(0.005f));
			priceTable.add(priceCoinsTable).expandX().fill();
		}
		
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

		// disable button if haven't had enough rounds
		//		if (!type.availableForUnlock(purchaseable)) {
		//			button.setDisabled(true);
		//			button.setTouchable(Touchable.disabled);
		//		}
		return button;
	}

	public static Table generateIconBox(Purchaseable purchaseable, int buttonWidth, int buttonHeight, boolean green, boolean drawLock, boolean drawGray) {		
		// actually populate the table
		// first thing is the 9 patch on the left

		ButtonStyle bs = Assets.getButtonStylePurchaseableWhite();
		Button button = new Button(bs);
		
		TextureRegion full = purchaseable.getIcon();
		if (full == null) {
			full = Assets.questionMark;
		}
		
		// draw two boxes, one white, then one either green or gray on top.
		// then we can draw icons to be exactly the height of the box.

		// if the icon is wider than long, crop out appropriate part of image

		// DONT CROP
		int regWidth = full.getRegionWidth();
		int regHeight = full.getRegionHeight(); 
		
		// just use this to decide how "big" to draw region.
		// always draw in native (texture) aspect ratio, with longer side as 
		// the bigger side.
		//	float aspectButton = (buttonWidth) * 1.0f / (buttonHeight); 
		
//		float aspectButton = 1;
		float aspectTexture = regWidth * 1.0f / regHeight;
//		
		int width, height;
		if (aspectTexture > 1) {
			width = buttonWidth - 2;
			height = (int) (buttonHeight / aspectTexture);
		}
		else if (aspectTexture < 1) {
			width = (int) (buttonWidth * aspectTexture);
			height = buttonHeight - 2;
		}
		else {
			// don't pad at all, assume box has alpha in corners.
			width = buttonWidth-2; // -2 is just extra padding
			height = buttonHeight-2;
		}
		
		Image icon = new Image(full);
		float imagePad = (buttonWidth - width)/2.0f;
		button.add(icon).center().width(width).height(height).padLeft(imagePad).padRight(imagePad);
		
		if (green) {
			button.add(new Image(Assets.green9PatchSmall)).width(buttonWidth).height(buttonHeight).padLeft(-buttonWidth);//.padTop(-buttonHeight + imagePadY);
		}
		else {
			button.add(new Image(Assets.gray9PatchSmall)).width(buttonWidth).height(buttonHeight).padLeft(-buttonWidth);//.padTop(-buttonHeight + imagePadY);
		}
		
//		if (green) {
//			Image check = new Image(Assets.purchaseableCheck);
//			int checkWidth = (int) (buttonWidth/4.0f);
//			int checkHeight = (int) (buttonHeight/4.0f);
//			button.add(check).top().right().width(checkWidth).height(checkHeight).padLeft(-checkWidth).padTop(-checkHeight/2);
//		}
		
		if (drawLock) {
			button.add(new Image(Assets.gray9PatchSmallFilled)).width(buttonWidth).height(buttonHeight).padLeft(-buttonWidth);//.padTop(-buttonHeight + imagePadY);
			button.add(new Image(Assets.marketLock)).width(buttonWidth).height(buttonHeight).padLeft(-buttonWidth);//.padTop(-buttonHeight + imagePadY);
		}
		// don't draw if consumable, unless another is already active
		else if (drawGray) {
			button.add(new Image(Assets.gray9PatchSmallFilled)).width(buttonWidth).height(buttonHeight).padLeft(-buttonWidth);//.padTop(-buttonHeight + imagePadY);
		}
		
		return button;
	}
	
	public void attemptUnlock(PurchaseType type, Purchaseable purchaseable) {
		if (type.consumable) {
			boolean success = this.master.profile.inventory.purchaseConsumable((SimpleConsumable) purchaseable, (PurchaseTypeConsumable) type);
			System.out.println(purchaseable.getName() + " consumed: " + success);
			if (success) unlockSuccess(purchaseable, type);
		}
		else {
			boolean success = this.master.profile.inventory.unlock(purchaseable, type);
			if (!success) unlockFail(purchaseable);
			else unlockSuccess(purchaseable, type);
		}
	}
	
	public void update() {
		System.out.println("updating purchase type table");

		needsUpdate = false;
		this.clear();
		
		// add section for currently selected item
		updateTypeSummaryTable(type);
		int selectedPad = KebabKing.getGlobalY(0.02f);
		this.add(typeSummary).fillX().padBottom(selectedPad).width(mainWidth);
		
		// if ads table, create special top table
		if (type == master.profile.inventory.adCampaign) selectPurchaseable(-1);

		// now draw list of all purchaseable options
		this.purchaseables = type.values;
//		this.currentPurchaseableIndex = -1;
//		for (int i = 0; i < purchaseables.length; i++) {
//			if (purchaseables[i] == type.getCurrentSelected()) this.currentPurchaseableIndex = i;
//		}
		// currentPurchaseableIndex might equal -1
		
		this.purchaseableTables = new Table[purchaseables.length];

		int purchaseablePad = KebabKing.getGlobalY(0.01f);

		Table purchaseableListTable = new Table();
		for (int i = 0; i < purchaseables.length; i++) {
			//			System.out.println("updating purchaseable table " + i);
			purchaseableListTable.add(createPurchaseableTable(i)).expandX().left().padTop(purchaseablePad).fillX();
			purchaseableListTable.row();
		}

		this.row();
		ScrollPane sp = new ScrollPane(purchaseableListTable, Assets.getSPS());
		sp.setScrollingDisabled(true, false);
		this.add(sp).expandX().fillX();
	}

	// take care of this yourself.
	public void unlockSuccess(Purchaseable purchaseable, PurchaseType type) {
		if (purchaseable.coinsToUnlock() > 0)
			System.out.println("You unlocked " + purchaseable.getName() + " for " + purchaseable.coinsToUnlock() + " coins");
		else {
			System.out.println("You unlocked " + purchaseable.getName() + " for " + Assets.currencyChar + purchaseable.cashToUnlock());
		}

		// verify unlocked
		if (!type.isUnlocked(purchaseable)) System.out.println("SHOULD BE UNLOCKED");
		else {
			System.out.println("successfully unlocked!");
		}

		selectPurchaseable(getIndex(purchaseable));
		update();
		
		// update the customer distribution for new location
		if (type == master.profile.inventory.locationType) {
			master.cm.updateCustomerDistribution();
		}
	}

	public void unlockFail(Purchaseable purchaseable) {
//		DrawUI.launchNotification("Sorry", "You can't afford " + purchaseable.getName(), null);
		System.out.println("You can't afford that!");
	}
	
	
	public void updatePurchaseableTable(int index) {
		//		System.out.println("updating purchaseable table with current purchasable index " + currentPurchaseableIndex);
		if (index < 0) return;
		if (purchaseableTables[index] == null) {
			purchaseableTables[index] = new Table();
		}
		
		Table table = purchaseableTables[index];
		Purchaseable purchaseable = purchaseables[index];
		if (purchaseable == null) return;
		table.clear();
		//		table.debugAll();

		// prev .15
		int buttonWidth = KebabKing.getGlobalX(0.15f);
		// guarantees a square button
		int buttonHeight = buttonWidth;
		
		boolean lockedByRound = !type.unlockIfReady(purchaseable);
		boolean locked = !type.isUnlocked(purchaseable);
		boolean consumable = type.consumable;
		boolean consumableActive = false;

		// for now, assume ad campaign
		if (consumable && master.profile.inventory.adCampaign.getActive() != null) {
			consumableActive = true;
		}
		
		boolean drawLock = lockedByRound || ((locked && (!consumable || (consumableActive && !type.isSelected(index))) && !consumableActive));
		boolean drawGray = lockedByRound || (consumable && (consumableActive && !type.isSelected(index)));
		
		Table button = generateIconBox(purchaseable, buttonWidth, buttonHeight, type.isSelected(index) && index >= 0, drawLock, drawGray);
		
		table.add(button).width(buttonWidth).height(buttonHeight).left();
	
		Color color = MainStoreScreen.FONT_COLOR;
		if (drawGray) color = MainStoreScreen.FONT_COLOR_GRAY;
		

		Table info = new Table();
		//		info.debugAll();
		int infoPad = KebabKing.getGlobalX(0.03f);
		int infoWidth = mainWidth - infoPad - buttonWidth;
		Label pTitle = new Label(purchaseable.getName(), Assets.generateLabelStyleUILight(PURCHASEABLE_TITLE_SIZE, Assets.alpha));
		pTitle.setColor(color);
		pTitle.setAlignment(Align.left);
		info.add(pTitle).left().expandX();

		if (!lockedByRound && !locked && !consumable && purchaseable.getDailyCost() > 0) {
			Label pPrice1 = new Label(Assets.currencyChar + floatToString(purchaseable.getDailyCost()), Assets.generateLabelStyleUIHeavyWhite(DAILY_COST_SIZE, Assets.nums));
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
		}

		info.row();

		Label pDesc = new Label(purchaseable.getDescription(), Assets.generateLabelStyleUILight(PURCHASEABLE_DESCRIPTION_SIZE, Assets.allChars));
		if (lockedByRound) 
			pDesc.setText("Available at level " + purchaseable.unlockAtLevel());
		else if (purchaseable.getDescription() == null || purchaseable.getDescription().length() == 0) {
//			pDesc = new Label("???", Assets.generateLabelStyleUILight(PURCHASEABLE_DESCRIPTION_SIZE, "???"));
			pDesc = null;
		}
//		info.debugAll();
		if (pDesc != null) {
			pDesc.setWrap(true);
			pDesc.setColor(color);
			pDesc.setAlignment(Align.left);
			info.add(pDesc).left().width(infoWidth).colspan(3);
			info.row();
		}
		
		// if locked but not by round
		if (locked && !lockedByRound && !consumableActive) {
			// add unlock button
			// create unlock button
			int unlockHeight = KebabKing.getGlobalY(0.04f);
			Button unlockButton = createUnlockButton(purchaseable, type, unlockHeight);
			Table unlockButtonTable = new Table();
			unlockButtonTable.add(unlockButton).height(unlockHeight).left();
			info.add(unlockButtonTable).colspan(3).left().expandX();
//			info.debugAll();
		}
		
		table.add(info).expandX().left().padLeft(infoPad).fillX();

		// Should not be able to select a table if it hasn't been unlocked by cash or by round
		if (!locked && !consumable) {
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
}
