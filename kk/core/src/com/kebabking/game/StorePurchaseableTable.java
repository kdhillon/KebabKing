package com.kebabking.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.kebabking.game.Purchases.LocationType;
import com.kebabking.game.Purchases.PurchaseType;
import com.kebabking.game.Purchases.PurchaseTypeConsumable;
import com.kebabking.game.Purchases.Purchaseable;
import com.kebabking.game.Purchases.SimpleConsumable;

public class StorePurchaseableTable extends Table {
	static int PURCHASEABLE_TITLE_SIZE = 24;
	static int PURCHASEABLE_DESCRIPTION_SIZE = 16;
	static int DAILY_COST_SIZE = 22;
	static int PER_DAY_SIZE = 16;

	KebabKing master;
	StorePurchaseTypeSubtable parent;
	int index; // index in parent array
	Purchaseable purchaseable;
	PurchaseType type;

	boolean grayUnlock;

	boolean drawingCheck;

	Table icon;
	Table iconCover;
	int iconWidth;
	int iconHeight;

	Label title;
	Label desc;

	Table info;

	Table unlockButton;

	Table unlockTable;
	Table priceCashTable;
	Table priceCoinsTable;
	boolean unlockIsGreen;

	Table unlockButtonTable;

	boolean lockedByLevel;

	public StorePurchaseableTable(KebabKing master, StorePurchaseTypeSubtable parent, Purchaseable p, int mainWidth, int index) {
		this.master = master;
		this.parent = parent;
		this.purchaseable = p;
		this.type = p.getType();
		this.index = index;

		//		table.debugAll();

		// prev .15
		iconWidth = KebabKing.getGlobalX(0.15f);
		// guarantees a square button
		iconHeight = iconWidth;

		boolean lockedByRound = !type.unlockIfReady(purchaseable);
		this.lockedByLevel = lockedByRound;
		boolean locked = !type.isUnlocked(purchaseable);
		boolean consumable = type.consumable;
		boolean consumableActive = false;

		// for now, assume ad campaign
		if (consumable && master.profile.inventory.adCampaign.getActive() != null) {
			consumableActive = true;
		}

		boolean drawLock = lockedByRound || ((locked && (!consumable || (consumableActive && !type.isSelected(index))) && !consumableActive));
		boolean drawGray = lockedByRound || (consumable && (consumableActive && !type.isSelected(index)));

		icon = generateIconBox(purchaseable, iconWidth, iconHeight, type.isSelected(index) && index >= 0, drawLock, drawGray);

		iconCover = new Table();
		System.out.println("initializing button cover");
		updateButtonCover(iconCover, false, drawLock, drawGray, iconWidth, iconHeight);
		icon.add(iconCover).fill();

		this.add(icon).width(iconWidth).height(iconHeight).left();

		Color color = MainStoreScreen.FONT_COLOR;
		if (drawGray) color = MainStoreScreen.FONT_COLOR_GRAY;

		info = new Table();
		//		info.debugAll();
		int infoPad = KebabKing.getGlobalX(0.03f);
		int infoWidth = mainWidth - infoPad - iconWidth;

		title = new Label(purchaseable.getName(), Assets.generateLabelStyleUILight(PURCHASEABLE_TITLE_SIZE, purchaseable.getName()));
		title.setColor(color);
		title.setAlignment(Align.left);
		info.add(title).left().expandX();

		if (!lockedByRound && !consumable && purchaseable.getDailyCost() > 0) {
			Label pPrice1 = new Label(Assets.getCurrency() + floatToString(purchaseable.getDailyCost()), Assets.generateLabelStyleUIHeavy(DAILY_COST_SIZE, Assets.nums));
			pPrice1.setColor(MainStoreScreen.FONT_COLOR_GREEN);
			if (purchaseable.getDailyCost() <= 0) {
				pPrice1.setText(" ");
			}
			//		pPrice1.setColor(MainStoreScreen.FONT_COLOR_GREEN);
			pPrice1.setAlignment(Align.right);
			info.add(pPrice1).right().expandX().fillX().bottom();

			String perDay = " / " + Assets.strings.get("day");
			Label pPrice2 = new Label(perDay, Assets.generateLabelStyleUIHeavy(PER_DAY_SIZE, perDay));
			pPrice2.setColor(MainStoreScreen.FONT_COLOR_GREEN);
			if (purchaseable.getDailyCost() <= 0) {
				pPrice2.setText(" ");
			}
			pPrice2.setAlignment(Align.right);
			info.add(pPrice2).right().bottom().padBottom(KebabKing.getGlobalY(0.002f));
		}

		info.row();

		String descText = purchaseable.getDescription();
		if (lockedByRound) {
			if (purchaseable.getType() != master.profile.inventory.locationType)
				descText = "Available at " + LocationType.getLocationAt(purchaseable.unlockWithLocation()).getName();
			else descText = "Available at level " + purchaseable.unlockAtLevel();
		}
		else if (purchaseable.getDescription() == null || purchaseable.getDescription().length() == 0) {
			//			pDesc = new Label("???", Assets.generateLabelStyleUILight(PURCHASEABLE_DESCRIPTION_SIZE, "???"));
			descText = null;
			//			throw new java.lang.AssertionError();
		}

		String descTextFull = purchaseable.getDescription() + descText;
		if (this.purchaseable.getName().equals("Rice Paddies")) {
			System.out.println("DESC TEXT FULL: " + descTextFull);
		}

		if (descText != null) {
			// there's no description for meat_
			desc = new Label(descText, Assets.generateLabelStyleUILight(PURCHASEABLE_DESCRIPTION_SIZE, descTextFull));
			desc.setWrap(true);
			desc.setColor(color);
			desc.setAlignment(Align.left);

			info.add(desc).left().width(infoWidth).colspan(3);
		}
		info.row();

		unlockButtonTable = new Table();
		info.add(unlockButtonTable).colspan(3).left().expandX();

		// specifically for meat types
		boolean shouldNotAddUnlock = (type == master.profile.inventory.meatTypes && purchaseable.unlockWithLocation() == 1); 
//		if (shouldNotAddUnlock) {
//			unlockButton = new Table();
//			// this is hacky, solves problem of updating table after forced unlock
//		}
		
		// if locked but not by round
		if (locked && !lockedByRound && !shouldNotAddUnlock) {
			// add unlock button
			// create unlock button
			
			addUnlockButton();
			//			info.debugAll();
		}

		this.add(info).expandX().left().padLeft(infoPad).fillX();

		// Should not be able to select a table if it hasn't been unlocked by cash or by round
		if (!locked) {
			addSelectListeners();
		}
	}

	public void addSelectListeners() {
		icon.addListener(new SuperStrictInputListener() {
			@Override
			public void touch(InputEvent event){
				click();
			}
		});	
		title.addListener(new SuperStrictInputListener() {
			public void touch(InputEvent event) {					
				click();
			}
		});	
	}

	public void addUnlockButton() {
		unlockButtonTable.clear();
		int unlockHeight = KebabKing.getGlobalY(0.035f);
		unlockButton = createUnlockButton(purchaseable, type, unlockHeight);
		unlockButtonTable.add(unlockButton).height(unlockHeight).left().padTop(KebabKing.getGlobalYFloat(0.004f));
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

		int regWidth = full.getRegionWidth();
		int regHeight = full.getRegionHeight(); 

		// just use this to decide how "big" to draw region.
		// always draw in native (texture) aspect ratio, with longer side as 
		// the bigger side.		
		float aspectTexture = regWidth * 1.0f / regHeight;

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
		//		Image icon = new Image();

		float imagePad = (buttonWidth - width)/2.0f;
		button.add(icon).center().width(width).height(height).padLeft(imagePad).padRight(imagePad);
		//		button.debugAll();
		return button;
	}

	public static void updateButtonCover(Table buttonCover, boolean green, boolean drawLock, boolean drawGray, int buttonWidth, int buttonHeight) {
		buttonCover.clear();
		if (green) {
			buttonCover.add(new Image(Assets.green9PatchSmall)).width(buttonWidth).height(buttonHeight).padLeft(-buttonWidth);//.padTop(-buttonHeight + imagePadY);
		}
		else {
			buttonCover.add(new Image(Assets.gray9PatchSmallThin)).width(buttonWidth).height(buttonHeight).padLeft(-buttonWidth);//.padTop(-buttonHeight + imagePadY);
		}

		if (green) {
			System.out.println("Drawing check");
			Image check = new Image(Assets.purchaseableCheck);
			int checkWidth = (int) (buttonWidth/4.0f);
			int checkHeight = (int) (buttonHeight/4.0f);
			buttonCover.add(check).top().left().width(checkWidth).height(checkHeight).padLeft(-1.1f*checkWidth).padTop(0.1f*checkWidth);//.padLeft(checkWidth);//.padTop(-checkHeight/2);
		}
		else {
			System.out.println("removing check");
		}

		if (drawLock) {
			buttonCover.add(new Image(Assets.gray9PatchSmallFilled)).width(buttonWidth).height(buttonHeight).padLeft(-buttonWidth);//.padTop(-buttonHeight + imagePadY);
			buttonCover.add(new Image(Assets.marketLock)).width(buttonWidth).height(buttonHeight).padLeft(-buttonWidth);//.padTop(-buttonHeight + imagePadY);
		}
		// don't draw if consumable, unless another is already active
		else if (drawGray) {
			buttonCover.add(new Image(Assets.gray9PatchSmallFilled)).width(buttonWidth).height(buttonHeight).padLeft(-buttonWidth);//.padTop(-buttonHeight + imagePadY);
		}
	}


	private Button createUnlockButton(Purchaseable purchaseable, PurchaseType type, float height) {
		UnlockSelectButton button  = new UnlockSelectButton(type, purchaseable, this.getInventory());

		// if false, will use green on some and grey on some
		boolean useSolidColor = true;

		// it's actually two buttons next to each other
		// one says "unlock", the other says "$10" or "jade 10"

		Label unlock;
		if (type.consumable) {
			String toWrite = Assets.strings.get("activate");
			unlock = new Label(toWrite, Assets.generateLabelStyleUIChina(22, toWrite));
		}
		else {
			String toWrite = Assets.strings.get("unlock");
			unlock = new Label(toWrite, Assets.generateLabelStyleUIChina(26, toWrite));
		}
		unlock.setTouchable(Touchable.disabled);

		Table bothButtons = new Table();

		unlockTable = new Table();

		if (master.profile.inventory.canAffordPurchase(type, purchaseable)) {
			this.unlockIsGreen = false;
		}

		unlockTable.add(unlock).padLeft(KebabKing.getGlobalX(0.01f)).padRight(KebabKing.getGlobalX(0.01f)).fill();
		bothButtons.add(unlockTable).width(KebabKing.getGlobalX(0.2f)).expandX().fillY();

		Table priceTable = new Table();

		if (purchaseable.cashToUnlock() > 0 || purchaseable.coinsToUnlock() <= 0) {
			priceCashTable = new Table();

			// need a special label for this, in case it's not in the english font we're using
			Label currencySymbol = new Label(Assets.getCurrency(), Assets.generateLabelStyleUI(22, Assets.getCurrency()));
			Label priceCash = new Label("", Assets.generateLabelStyleUIChina(26, Assets.nums));
			priceCash.setTouchable(Touchable.disabled);

			if (master.profile.getCash() >= purchaseable.cashToUnlock()) {
				if (!useSolidColor || master.profile.getCoins() >= purchaseable.coinsToUnlock()) {
					//					priceCashTable.setBackground(new TextureRegionDrawable(Assets.marketDarkGreen));
				}
				else {
					//					priceCashTable.setBackground(new TextureRegionDrawable(Assets.gray));			
				}
			}
			else {
				//				priceCashTable.setBackground(new TextureRegionDrawable(Assets.gray));			
			}

			priceCash.setText(LanguageManager.localizeNumber(purchaseable.cashToUnlock()));
			priceCashTable.add(currencySymbol).padLeft(KebabKing.getGlobalX(0.01f)).padRight(KebabKing.getGlobalX(0.005f));
			priceCashTable.add(priceCash).padRight(KebabKing.getGlobalX(0.01f));
			priceTable.add(priceCashTable).expand().fill();;
		}

		if (purchaseable.coinsToUnlock() > 0) {
			priceCoinsTable = new Table();
			Label priceCoins = new Label("", Assets.generateLabelStyleUIChina(26, Assets.nums));
			priceCoins.setTouchable(Touchable.disabled);

			if (master.profile.getCoins() >= purchaseable.coinsToUnlock()) {
				if (!useSolidColor || master.profile.getCash() >= purchaseable.cashToUnlock()) {
				}
				else {
					//					priceCoinsTable.setBackground(new TextureRegionDrawable(Assets.gray));			
				}
			}
			else {
				//				priceCoinsTable.setBackground(new TextureRegionDrawable(Assets.gray));			
			}

			priceCoinsTable.add(new Image(Assets.marketJade)).width(height);
			priceCoins.setText(LanguageManager.localizeNumber(purchaseable.coinsToUnlock()));
			priceCoinsTable.add(priceCoins).padLeft(KebabKing.getGlobalX(-0.005f)).padRight(KebabKing.getGlobalX(0.005f));
			priceTable.add(priceCoinsTable).expandX();//.fill();
		}

		bothButtons.add(priceTable);//.padLeft(padLeft);

		button.add(bothButtons);
		button.addListener(new SuperStrictInputListener() {
			public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if (shouldCancel()) return;

				UnlockSelectButton usb = (UnlockSelectButton) event.getListenerActor();
				attemptUnlock(usb.type, usb.purchaseable);
			}
		});	

		this.updateForUnlock(true);

		// disable button if haven't had enough rounds
		//		if (!type.availableForUnlock(purchaseable)) {
		//			button.setDisabled(true);
		//			button.setTouchable(Touchable.disabled);
		//		}
		return button;
	}


	public void attemptUnlock(PurchaseType type, Purchaseable purchaseable) {
		if (type.consumable) {
			boolean success = this.master.profile.inventory.purchaseConsumable((SimpleConsumable) purchaseable, (PurchaseTypeConsumable) type);
			System.out.println(purchaseable.getName() + " consumed: " + success);
			if (success) {
				unlockSuccess(purchaseable, type);
			}
		}
		else {
			boolean success = this.master.profile.inventory.unlock(purchaseable, type);
			if (!success) unlockFail(purchaseable);
			else unlockSuccess(purchaseable, type);
		}
	}

	// take care of this yourself.
	public void unlockSuccess(Purchaseable purchaseable, PurchaseType type) {
		if (purchaseable.coinsToUnlock() > 0)
			System.out.println("You unlocked " + purchaseable.getName() + " for " + purchaseable.coinsToUnlock() + " coins");
		else {
			System.out.println("You unlocked " + purchaseable.getName() + " for " + Assets.getCurrency() + purchaseable.cashToUnlock());
		}

		// verify unlocked
		//		if (!type.isUnlocked(purchaseable)) System.out.println("SHOULD BE UNLOCKED");
		//		else {
		//			System.out.println("successfully unlocked!");
		//		}

		// TODO select
		select();
		parent.updateAllUnlocks();


		// TODO update
		//		updateFull();

		// update the customer distribution for new location
		if (type == master.profile.inventory.locationType) {
			master.cm.updateCustomerDistribution();
		}	
	}


	public void click() {
		if (!type.consumable && !this.isSelected()) {
			System.out.println("clicking");
			this.select();

			// hacky
			if (type == master.profile.inventory.locationType) {
				// note that this replaces any existing ad campaign.
				System.out.println("resettting distribution");
				master.cm.updateCustomerDistribution();
			}

			master.save();
		}
	}

	// you shouldn't be able to select consumables
	public void select() {
		//		if (index < 0) {
		//			updateTypeSummaryTable(type);
		//			return;
		//		}
		// do nothing if already selected and clicking this.
		if (this.drawingCheck) {
			System.out.println("can't deselect");
			return;
		}
		
		System.out.println("currently selected: " + type.getSelected().size());

		// update this table when selected
		int oldSelected = parent.selectedIndex;
		type.removeOrSelect(index);
		System.out.println("currently selected: " + type.getSelected().size());

		this.updateForSelect();
		parent.selectedIndex = index;
		if (oldSelected >= 0 && oldSelected != this.index && type.getMaxSelectable() == 1) {
			parent.purchaseableTables[oldSelected].updateForDeselect();
		}
		//		if (type.getMaxSelectable() > 1 && type.getMaxSelectable() >= type.getSelected().size() && oldSelected >= 0) 
		//			parent.purchaseableTables[oldSelected].updateForDeselect();
//		else if (type.getMaxSelectable() > 1 &&  parent.checksCurrentlyDrawn >= type.getMaxSelectable()) {
//			//			parent.updateAllUnlocks();
//			System.out.println("updating in select for multi-selectable type");
//			for (int i = 0; i < type.values.length; i++) {
//				if (parent.purchaseableTables[i] == null) continue;
//				if (!type.getSelected().contains(i) && parent.purchaseableTables[i].drawingCheck) {
//					parent.purchaseableTables[i].updateForDeselect();
//				}
//			}
//		}
		else if (type.getMaxSelectable() > 1) {
			//			parent.updateAllUnlocks();
			for (int i = 0; i < type.values.length; i++) {
				if (parent.purchaseableTables[i] == null || !type.isUnlocked(type.values[i])) continue;
				parent.purchaseableTables[i].updateForDeselect();
			}
			for (int i = 0; i < type.values.length; i++) {
				if (parent.purchaseableTables[i] == null || !type.isUnlocked(type.values[i])) continue;
				if (type.isSelected(i))
					parent.purchaseableTables[i].updateForSelect();
			}
		}
		
		System.out.println("currently selected: " + type.getSelected().size());
	}

	public boolean isSelected() {
		return this.index == parent.selectedIndex;
	}

	public void unlockFail(Purchaseable purchaseable) {
		//		DrawUI.launchNotification("Sorry", "You can't afford " + purchaseable.getName(), null);
		System.out.println("You can't afford that or it's not available!");
	}

	public String floatToString(float value) {
		if ((int) value == value) return "" + ((int) value);
		else return "" + value;
	}

	public void updateForSelect() {
		System.out.println("updating for select");
		updateButtonCover(iconCover, true, false, false, iconWidth, iconHeight);
		if (!drawingCheck) {
			this.parent.checksCurrentlyDrawn++;
			drawingCheck = true;
		}

//		System.out.println("checks drawn: " + parent.checksCurrentlyDrawn);
		//		 make it green
		System.out.println("Updating " + index + " for select");
	}

	public void updateForDeselect() {
		System.out.println("updating for deselect: " + this.purchaseable.getName());
		updateButtonCover(iconCover, false, false, false, iconWidth, iconHeight);
		if (drawingCheck) {
			drawingCheck = false;
			this.parent.checksCurrentlyDrawn--;
		}
//		System.out.println("checks drawn: " + parent.checksCurrentlyDrawn);
		// make it not grey
		//		System.out.println("Updating " + index + " for deselect");
	}

	public void updateForUnlock(boolean force) {
		System.out.println("updating for unlock: " + purchaseable.getName() + " force: " + force);
		if (this.unlockedByLevel() && this.lockedByLevel) {
			updateForUnlockedByLevel();
		}

		if (!this.unlocked() && unlockTable != null) {
			System.out.println("trying to unlock: " + (unlockTable != null));
			if (master.profile.inventory.canAffordPurchase(type, purchaseable) && (!this.type.consumable || ((PurchaseTypeConsumable) this.type).getActive() == null)) {
				if (!unlockIsGreen || force) {
					unlockTable.setBackground(Assets.marketGreenD);
					if (priceCashTable != null)
						priceCashTable.setBackground(Assets.marketDarkGreenD);
					if (priceCoinsTable != null)
						priceCoinsTable.setBackground(Assets.marketDarkGreenD);
					unlockIsGreen = true;
				}
			}
			else {
				if (unlockIsGreen || force) {
					unlockTable.setBackground(Assets.grayLightD);
					if (priceCashTable != null)
						priceCashTable.setBackground(Assets.grayD);			
					if (priceCoinsTable != null)
						priceCoinsTable.setBackground(Assets.grayD);			
					unlockIsGreen = false;
				}
			}	
		}
		// for consumables that reset
		if (drawingCheck && purchaseable.getType().consumable && ((PurchaseTypeConsumable)purchaseable.getType()).getActive() != purchaseable) {
			System.out.println("updating consumable for unlock");
			updateButtonCover(iconCover, false, false, false, iconWidth, iconHeight);
			drawingCheck = false;
		}

		// this was just purchased
		if (this.unlocked() && this.unlockButton != null) {
			this.unlockButton.clear();
			this.unlockButtonTable.clear();
			this.unlockButtonTable = null;
			this.unlockButton = null;
			addSelectListeners();
		}

		if (this.purchaseable.getType().isSelected(this.index)) {
			System.out.println(this.purchaseable.getType().getFirstSelected().getName());
			this.select();
		}
	}

	public void updateForUnlockedByLevel() {
		System.out.println("updating: " + purchaseable.getName() + " for unlocked by level");
		this.lockedByLevel = false;
		addUnlockButton();

		desc.setText(purchaseable.getDescription());
		desc.setColor(MainStoreScreen.FONT_COLOR);
		title.setColor(MainStoreScreen.FONT_COLOR);
		// need to update description, add unlock button and update font color
	}

	public boolean unlocked() {
		return type.isUnlocked(purchaseable);
	}

	public boolean unlockedByLevel() {
		return type.availableForUnlock(purchaseable);
	}

	public ProfileInventory getInventory() {
		return master.profile.inventory;
	}
}
