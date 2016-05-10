package com.kebabking.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.kebabking.game.Purchases.PurchaseType;
import com.kebabking.game.Purchases.Purchaseable;

public class StoreSubtable extends Table {
	KebabKing master;

	StoreScreen parent;
	PurchaseType[] types;
	Button[] typeButtons;
	StorePurchaseTypeSubtable[] typeTables;

	int currentTypeIndex;
	Table currentTypeContainer;

	int mainWidth;

	public StoreSubtable(PurchaseType[] types, StoreScreen parent) {
		this.parent = parent;
		this.master = parent.master;

//		System.out.println("Creating store subtable: " + titleString);
//		System.out.println();

		this.types = types;
		this.typeTables = new StorePurchaseTypeSubtable[types.length];
		//			preparePurchaseTypes(types);
		currentTypeContainer = new Table();

		//		this.debugAll();
		this.setWidth(KebabKing.getWidth());
		this.setHeight(KebabKing.getHeight());

		mainWidth = KebabKing.getGlobalX(0.8f);

		Table main = new Table();
		//		main.debugAll();
		main.setWidth(mainWidth);
		main.setHeight(KebabKing.getGlobalY(0.7f));
		main.row();
		this.add(main).center().top().expandY();

		// add buttons for each this
		int buttonPad = KebabKing.getGlobalX(0.01f);
		int buttonWidth = mainWidth / types.length - buttonPad;
		int buttonHeight = KebabKing.getGlobalY(0.07f);

		typeButtons = new Button[types.length];
		Table buttonthis = new Table();
		if (types.length > 1) {
			for (int i = 0; i < types.length; i++) {
				Button button = new Button(Assets.getPurchaseTypeButtonStyle());

//				System.out.println(i);
				// 
				Label label = new Label(types[i].getName(), getLabelStyleForButtonCount(types.length, types[i].getName())); // so 48, 24, 16, 12, 9
				label.setColor(MainStoreScreen.FONT_COLOR);
				label.setWrap(true);
				label.setAlignment(Align.center);
				button.add(label).center().expandX().fillX();
				label.setTouchable(Touchable.disabled);

				button.addListener(new StrictInputListener() {
					@Override
					public void touch(InputEvent event) {
						switchToPurchaseType((Button) event.getTarget());
					}
				});

				buttonthis.add(button).width(buttonWidth).height(buttonHeight).padRight(buttonPad / 2).padLeft(buttonPad / 2);
				typeButtons[i] = button;
			}
		}
		main.add(buttonthis).top().expandY().padTop(KebabKing.getGlobalY(0.1f));

		switchToPurchaseType(0);
		main.row();
		main.add(currentTypeContainer).top().fillX().fillY();
		
//		for (int i = 0; i < types.length; i++) {
//			updatePurchaseTypeTable(i);
//		}
	}
	
	public ProfileInventory getInventory() {
		return parent.getInventory();
	}
	
//	public void markAllForUpdate() {
////		System.out.println("marking all for update");
//		for (int i = 0; i < types.length; i++) {
//			if (typeTables[i] != null)
//				typeTables[i].needsFullUpdate = true;
//		}
//	}
	
	public void onCampaignEnded(PurchaseType type) {
		for (int i = 0; i < types.length; i++) {
			if (types[i] == type) updatePurchaseTypeTable(i);
		}
	}
	
//	public void updateOnConsumableReset(PurchaseType type) {
//		for (int i = 0; i < types.length; i++) {
//			if (types[i] == (PurchaseType) type) typeTables[i].onConsumableReset();
//		}
//	}
	
	public void updatePurchaseTypeTable(int typeIndex) {
		System.out.println("updating purchaseType table");
		if (typeTables[typeIndex] == null) {
			typeTables[typeIndex] = new StorePurchaseTypeSubtable(this, types[typeIndex], mainWidth);
		}
		if (typeTables[typeIndex].needsInitialization) {
			typeTables[typeIndex].initialize();
		}
		typeTables[typeIndex].updateAllUnlocks();
	}
	
	
	private void disableButton(int index) {
		typeButtons[index].setDisabled(true);
//		((Label) typeButtons[index].getChildren().first()).setStyle(getLabelStyleForButtonCount(typeButtons.length));
		((Label) typeButtons[index].getChildren().first()).setColor(Color.WHITE);
		typeButtons[index].setTouchable(Touchable.disabled);
	}
	private void enableButton(int index) {
		typeButtons[index].setDisabled(false);
//		((Label) typeButtons[index].getChildren().first()).setStyle(getLabelStyleForButtonCount(typeButtons.length));
		((Label) typeButtons[index].getChildren().first()).setColor(MainStoreScreen.FONT_COLOR);
		typeButtons[index].setTouchable(Touchable.enabled);
	}
	

	private LabelStyle getLabelStyleForButtonCount(int count, String text) {
		if (count == 1)
			throw new java.lang.AssertionError();
		else if (count == 2) {
//			if (!enabled)
				return Assets.generateLabelStyleUIChina(34, text);
//			else 
//				return Assets.generateLabelStyleUIChina(30);
		}
		else if (count == 3) {
//			if (!enabled)
				return Assets.generateLabelStyleUIChina(24, text);
//			else 
//				return Assets.generateLabelStyleUIChina(22);
		}
		else if (count == 4) {
//			if (!enabled)
				return Assets.generateLabelStyleUIChina(20, text);
//			else 
//				return Assets.generateLabelStyleUIChina(18);
		}
		else if (count == 5) {
//			if (!enabled)
				return Assets.generateLabelStyleUIChina(15, text);
//			else 
//				return Assets.generateLabelStyleUIChina(15);
		}
		return null;
	}
	
	// updates current purchaseType table
	public void updateCurrent() {
		if (typeTables[currentTypeIndex].needsInitialization) {
			typeTables[currentTypeIndex].initialize();
		}
		System.out.println("updating current: " + types[currentTypeIndex].getName());
		this.typeTables[this.currentTypeIndex].updateAllUnlocks();		
	}
		
	private int getIndexOfType(PurchaseType type) {
		for (int i = 0; i < types.length; i++) {
			if (types[i] == type) return i;
		}
		return -1;
	}
	
	// update the purchaseable
	public void updatePurchaseableForUnlock(Purchaseable p) {
		System.out.println("trying to update purchaseable after unlock");
		if (typeTables != null && p != null && getIndexOfType(p.getType()) >= 0 && typeTables[getIndexOfType(p.getType())] != null) {
			System.out.println("actually updating purchaseable after unlock");
			typeTables[getIndexOfType(p.getType())].updatePurchaseableAfterUnlock(p);
		}
	}
	
	// do everything 
	public void switchToPurchaseType(int typeIndex) {
		System.out.println("switching to purchase type");

		// first, null out currentType to save memory (hopefully)
//		typeTables[currentTypeIndex] = null;

		currentTypeIndex = typeIndex;
		// enable all buttons but disable this button
		if (typeButtons.length > 1) {
			for (int i = 0; i < typeButtons.length; i++) {
				enableButton(i);
			}
			disableButton(currentTypeIndex);
		}
		
//		if (typeTables[currentTypeIndex] != null)
//			System.out.println("needs update: " + typeTables[currentTypeIndex].needsFullUpdate);
		
		if (typeTables[currentTypeIndex] == null) {
			updatePurchaseTypeTable(currentTypeIndex);
			
			this.currentTypeContainer.clear();
			currentTypeContainer.add(typeTables[typeIndex]).width(mainWidth);
		}
		else {
			currentTypeContainer.clear();
			currentTypeContainer.add(typeTables[currentTypeIndex]);
//			updateSelectedPurchaseableTable(type.getCurrentSelected(), type);
		}
	}
	
	public void switchToPurchaseType(PurchaseType type) {
		switchToPurchaseType(getIndexOfType(type));
	}
	
	// this is the button that was clicked 
	public void switchToPurchaseType(Button listener) {
		//		System.out.println(listener.getText());
		int index = -1;
		for (int i = 0; i < typeButtons.length; i++) {
			if (typeButtons[i] == listener) index = i;
		}
		if (index == -1) throw new java.lang.AssertionError();
		switchToPurchaseType(index);
	}
}
