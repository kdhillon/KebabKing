package com.kebabking.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.kebabking.game.OnlinePurchaseHandler.PurchaseableOnline;

public class JewelerTable extends Table {
	static final float SELECT_TABLE_WIDTH = 0.45f;
	
	static final float playLocX = 0.5f;
	static final float playLocY = 0.585f;
	
	KebabKing master;

	PurchaseableOnline[] options;
	Table[] entries;

	int numOptions;
	int currentOption;

	Table selectedTableContainer;
	Table selectedTable;
	
	TextureRegion jade;
	TextureRegion cash;

	// create a table in which people can buy coins 
	public JewelerTable(KebabKing master) {
		super();
		//		System.out.println("RUNNING");
		this.master = master;
		//		Label title = new Label("Coins!", Assets.gen);
		//		title.setAlignment(Align.center);
		//		this.add(title).height(KebabKing.getGlobalY(StoreScreen.TitleHeight / StoreScreen.UNITS_HEIGHT));

		jade = Assets.getTextureRegion("market/Jeweler-09");
		cash = Assets.getTextureRegion("market/Jeweler-10");
		
		initializeOptions();

		initializeBG();
		initializeMain();

		initializeSelectArea();

		setCurrentOption(0);
	}

	private void initializeBG() {
	}

	private void initializeMain() {
//		this.debug();
		String theJewelerText = Assets.strings.get("the_jeweler");
		Label theJeweler = new Label(theJewelerText, Assets.generateLabelStyleUIChina(40, theJewelerText));
		this.add(theJeweler).top().padTop(KebabKing.getGlobalY(0.12f));

		String welcomesYouText = Assets.strings.get("welcomes_you");
		Label welcomesYou = new Label(welcomesYouText, Assets.generateLabelStyleUI(20, welcomesYouText));
		this.row();
		this.add(welcomesYou).top();

		TextureRegion jewelerReg = Assets.getTextureRegion("market/Jeweler-08");
		Image jeweler = new Image(jewelerReg);
		
		// jeweler is no longer touchable.
//		jeweler.setTouchable(Touchable.enabled);
//		jeweler.addListener(new InputListener() {
//			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
//				return true;
//			}
//			public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
////				master.switchToJadeWheelScreen();
//				// TODO fix this.
//			}
//		});
		
		this.row();
		float height = KebabKing.getGlobalY(0.3f);
		float width = height * jewelerReg.getRegionWidth() / jewelerReg.getRegionHeight();
		this.add(jeweler).width(width).height(height).expandY().top().padTop(KebabKing.getGlobalY(0.01f));
	}

	public void initializeSelectArea() {
		Table selectArea = new Table();
//		selectArea.debug();
		Table left = new Table();
		left.setTouchable(Touchable.enabled);
		left.setBackground(new TextureRegionDrawable(Assets.getTextureRegion("market/Jeweler-13")));

		left.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
				goLeft();
			}
		});

		Table right = new Table();
		right.setTouchable(Touchable.enabled);
		TextureRegion arrowRight = Assets.getTextureRegion("market/Jeweler-14");
		right.setBackground(new TextureRegionDrawable(arrowRight));

		right.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
				goRight();
			}
		});
		
		float totalWidth = KebabKing.getGlobalX(0.7f);
		float centerWidth = KebabKing.getGlobalX(SELECT_TABLE_WIDTH);
		float arrowWidth = (totalWidth - centerWidth)/2;
		float arrowHeight = arrowWidth * arrowRight.getRegionHeight() / arrowRight.getRegionWidth();
		float centerHeight = KebabKing.getGlobalYFloat(0.35f);
		selectArea.add(left).top().width(arrowWidth).height(arrowHeight);
		selectedTableContainer = new Table();
		selectArea.add(selectedTableContainer).width(centerWidth).height(centerHeight);
		selectArea.add(right).top().width(arrowWidth).height(arrowHeight);
		
		this.row();
		this.add(selectArea);
	}

	private void initializeOptions() {
		options = PurchaseableOnline.values;
		numOptions = options.length;
		entries = new Table[numOptions];

		for (int i = 0; i < numOptions; i++) {
			PurchaseableOnline p = options[i];
			entries[i] = newTableEntry(p);
		}
	}

	public void setCurrentOption(int index) {
		// set the main table
		selectedTable = entries[index];
		selectedTableContainer.clear();
		selectedTableContainer.add(selectedTable).top().expand();
		System.out.println("selected: " + options[index].name);
	}

	public void goLeft() {
		currentOption -= 1;
		if (currentOption < 0) currentOption = numOptions - 1;
		setCurrentOption(currentOption);
	}

	public void goRight() {
		currentOption += 1;
		if (currentOption > numOptions - 1) currentOption = 0;
		setCurrentOption(currentOption);
	}

	private Table newTableEntry(PurchaseableOnline op) {
		Table entry = new Table();
		
		float totalWidth = KebabKing.getGlobalX(SELECT_TABLE_WIDTH);

		Label title = new Label(op.name, Assets.generateLabelStyleUIChina(30, op.name));
		title.setWrap(true);
		title.setAlignment(Align.center);
		
		entry.add(title).top().padTop(KebabKing.getGlobalY(0.005f)).center().width(totalWidth);
		
		Label price = new Label(Assets.getIAPCurrency() + LanguageManager.localizeNumber(op.price) + " " + Assets.getIAPCurrencyAbbrev(), Assets.generateLabelStyleUI(16, Assets.nums + Assets.getIAPCurrency() + Assets.getIAPCurrencyAbbrev()));
		entry.row();
		entry.add(price).top().expandY();
		//		Label title = new Label("" + op.coins + " Coins!", Assets.generateLabelStyleUIChinaWhite(30, Assets.nums + " Coins!"));

		//		entry.add(title);
		
		Table valueTable = new Table();
//		valueTable.debug();
		Table jadeTable = new Table();
		Image jadeImage = new Image(jade);
		Label jadeCount = new Label("x " + LanguageManager.localizeNumber(op.jade), Assets.generateLabelStyleUI(20, Assets.nums + "x"));
		float halfWidth = totalWidth * 0.3f;
		float halfHeight = halfWidth * jade.getRegionHeight() / jade.getRegionWidth();
//		float imagePad = totalWidth * 0.3f;
		jadeTable.add(jadeImage).width(halfWidth).height(halfHeight);
		jadeTable.row();
		jadeTable.add(jadeCount).padTop(KebabKing.getGlobalY(-0.01f));
		valueTable.add(jadeTable).left().expandX().width(totalWidth/2);
		
		Table cashTable = new Table();
		Image cashImage = new Image(cash);
		Label cashCount = new Label("x " + LanguageManager.localizeNumber((int) op.cash), Assets.generateLabelStyleUI(20, Assets.nums + "x"));
//		float imagePad = totalWidth * 0.3f;
		cashTable.add(cashImage).width(halfWidth).height(halfHeight);
		cashTable.row();
		cashTable.add(cashCount).padTop(KebabKing.getGlobalY(-0.01f));
		valueTable.add(cashTable).right().expandX().width(totalWidth/2);
		
		entry.row();
		entry.add(valueTable).expand();
		
		entry.row();
		
		String completePurchaseText = Assets.strings.get("complete_purchase");
		Label completePurchase = new Label(completePurchaseText, Assets.generateLabelStyleUI(16, completePurchaseText));
		entry.add(completePurchase).padTop(KebabKing.getGlobalY(0.01f));
				
		Table okButton = DrawUI.getBlueButton(Assets.strings.get("ok"), 40);
		entry.row();
//		float buttonWidth = KebabKing.getGlobalX(0.2f);
//		float buttonHeight = KebabKing.getGlobalY(0.07f);
		entry.add(okButton).padTop(KebabKing.getGlobalY(0.0f));
		
		okButton.addListener(new StrictInputListener() {
			public void touch(InputEvent event) {
				OnlinePurchaseHandler.attemptPurchase(options[currentOption]);
			}
		});
				
		return entry;
	}

	//	private void buy(PurchaseableOnline op) {
	//		master.makePurchase(op);
	//	}
}
