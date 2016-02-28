package com.kebabking.game;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kebabking.game.Customer.CustomerType;
import com.kebabking.game.Grill.Selected;
import com.kebabking.game.Purchases.MeatTypes;

public class Order {
	static final boolean ACCEPT_WRONG_SPICINESS = true;
	static final float ORDER_ROW_WIDTH = 1.25f;
	static final float ORDER_ROW_HEIGHT = .675f;
	static final float ICON_WIDTH  = 0.75f; // times unit height
	static final float ICON_HEIGHT = 0.63f;
	static final float ICON_OFFSET_X = 0.45f;
	static final float ICON_OFFSET_Y = 0.02f;
	static final float FONT_OFFSET_Y = 0.54f;

	// represents a food order 
	// too keep things simpler, allow either only spicy or not, and max two types 
	int first;
	int second;
	int third;

	boolean firstSpicy;
	boolean secondSpicy;
	boolean thirdSpicy;

	int beer;
	int total;

	int remaining;
	int raw; 			 // how many raw meats were given to this person
	int burnt; 			 // how many chuanrs were burnt
	int wrongSpiciness;
	int incorrect;		 // how many wrong chuanrs/beers were given to this person	

	KitchenScreen ks;
	Stage stage; // for drawing order
	Table table;

	// generate a totally random order based on the given type
	public Order(CustomerType type, KitchenScreen ks) {
		this.ks = ks;
				
		firstSpicy = (Math.random() < .5);
		secondSpicy = (Math.random() < .5);
		thirdSpicy = (Math.random() < .5);

		int min = type.minOrder;
		int max = type.maxOrder;

		// just the meat
		total = (int) (Math.random() * (max-min) + min);

		int count1 = (int) (Math.random() * total);
		int count2 = total-count1;

		double random = Math.random();

		if (ks.grill.boxes[2] != null) {
			// no chicken
			if (random < .33) {
				second = count1;
				third = count2;
			}
			// no beef
			else if (random < .66) {
				first = count1;
				third = count2;
			}
			// no lamb
			else {
				first = count1;
				second = count2;
			}
		}
		else if (ks.grill.boxes[1] != null) {
			first = count1;
			second = count2;
		}
		else {
			first = count1 + count2;
		}
		
		if (TutorialEventHandler.forceFirstOrder()) {
			first = TutorialEventHandler.FIRST_ORDER_MEAT_COUNT;
			second = 0;
			third = 0;
		}	
		if (TutorialEventHandler.forceSpiceOrder()) {
			first = 1;
			second = 0;
			third = 0;
			firstSpicy = secondSpicy = thirdSpicy = true;
		}
		else if (TutorialEventHandler.shouldNotOrderSpice()) firstSpicy = secondSpicy = thirdSpicy = false;

		// because Val wanted it.
		// reduces orders of "double" sized meats.
		MeatTypes.Type firstType = ks.grill.getType(Selected.FIRST);
		if (firstType.doubleWidth) first = (first + 1) / 2;

		MeatTypes.Type secondType = ks.grill.getType(Selected.SECOND);
		if (secondType != null && secondType.doubleWidth) second = (second + 1) / 2;

		MeatTypes.Type thirdType = ks.grill.getType(Selected.THIRD);
		if (thirdType != null && thirdType.doubleWidth) third = (third + 1) / 2;

		double beerRandom = Math.random();
		if (beerRandom < type.beerFactor) beer = 1;
		if (beerRandom * 2 < type.beerFactor) beer = 2;
		if (beerRandom * 3 < type.beerFactor) beer = 3;
		
		if (TutorialEventHandler.forceSecondOrder()) {
			beer = 1;
			first = 3;
			second = 0;
			third = 0;
		}
		if (TutorialEventHandler.shouldNotOrderBeer()) beer = 0;
		
		total = first + second + third + beer;
		remaining = total;

		this.stage = new Stage();
		//		stage.setDebugAll(true);
		table = new Table();
		stage.addActor(table);
		updateTable();
	}

	// returns total money received from this 
	public float[] giveMeat(ArrayList<Meat> set) {
		float[] totalRevCost = new float[2];
		for (Meat m : set) {
			float[] revCost = giveMeat(m);
			if (revCost != null) {
				TutorialEventHandler.handleMeatServed();
				totalRevCost[0] += revCost[0];
				totalRevCost[1] += revCost[1];
			}
		}
		//		System.out.println(incorrect);
		updateTable();
		return totalRevCost;
	}

	public float[] giveMeat(Meat meat) {
		Grill.Selected selected = ks.grill.getSelectedForType(meat.type);

		switch (selected) {
		case FIRST:
			if (first == 0 || (meat.spiced != firstSpicy && !ACCEPT_WRONG_SPICINESS)) {
				System.out.println("I don't want chicken");
				incorrect++;
				return null;
			}
			if (meat.spiced != firstSpicy) {
				//  incorrect spiciness, but still counts as chicken.
				wrongSpiciness++;

			}
			break;
		case SECOND:
			if (second == 0  || (meat.spiced != secondSpicy && !ACCEPT_WRONG_SPICINESS)) {
				System.out.println("wrong beef");
				incorrect++;
				return null;
			}
			if (meat.spiced != secondSpicy) {
				wrongSpiciness++;
			}
			break;
		case THIRD:
			if (third == 0  || (meat.spiced != thirdSpicy && !ACCEPT_WRONG_SPICINESS)) {
				System.out.println("wrong lamb");
				incorrect++;
				return null;
			}
			if (meat.spiced != thirdSpicy) {
				wrongSpiciness++;
			}
			break;
		default:
			break;
		}

		//		Meat.Type type = meat.type;
		if (selected == Selected.FIRST)
			first--;
		else if (selected == Selected.SECOND)
			second--;
		else if (selected == Selected.THIRD)
			third--;

		remaining--;
		if (meat.state == Meat.State.BURNT) burnt++;
		if (meat.state == Meat.State.RAW) {
			raw++;
			TutorialEventHandler.handleServeRaw();
		}

		StatsHandler.serveKebab();

		float[] revCost = new float[2];
		revCost[0] = meat.getSellPrice();
		revCost[1] = meat.getBuyPrice();
		return revCost;
	}

	//	public float giveBeer(int count) {
	//		float price = 0;
	//		for (int i = 0; i < count; i++) {
	//			price += giveBeer();
	//		}
	//		return price;
	//	}

	public float giveBeer() {
		//		System.out.println("giving beer!");
		if (beer == 0) {
			incorrect++;
			return 0;
		}
		beer--;
		remaining--;
		updateTable();
		TutorialEventHandler.handleBeerServed();
		StatsHandler.serveDrink();
		return ks.getDrinkSellPrice();
	}

	// table should include the speech bubble, orders, everything
	public void updateTable() {
		table.clear();

		int tablePadLeft = KebabKing.getGlobalX(0.025f);
		int tablePadRight = KebabKing.getGlobalX(0.01f);
		int tablePadTop = KebabKing.getGlobalY(0.008f);
		int tablePadBottom = KebabKing.getGlobalY(0.005f);

		int subTableWidth = KebabKing.getGlobalX(0.095f);
		int subTableHeight = KebabKing.getGlobalY(0.03f);
		int subTablePadY = KebabKing.getGlobalY(0.003f);

		// should be a fixed tableWidth;
		int tableWidth = subTableWidth + tablePadLeft + tablePadRight;
		int tableHeight = getTotalTypes() * subTableHeight + tablePadTop + tablePadBottom + subTablePadY * getTotalTypes();

		table.setBackground(new TextureRegionDrawable(Assets.speech));
		table.setSize(tableWidth, tableHeight);

		Table orders = new Table();


		// TODO will have to make this "first, second, third" as well
		if (this.first > 0) {
			orders.add(createSubTable(ks.grill.getType(Selected.FIRST), firstSpicy, this.first)).width(subTableWidth).height(subTableHeight).padBottom(subTablePadY);
			orders.row();
		}
		if (this.second > 0) {
			orders.add(createSubTable(ks.grill.getType(Selected.SECOND), secondSpicy, this.second)).width(subTableWidth).height(subTableHeight).padBottom(subTablePadY);
			orders.row();
		}
		if (this.third > 0) {
			orders.add(createSubTable(ks.grill.getType(Selected.THIRD), thirdSpicy, this.third)).width(subTableWidth).height(subTableHeight).padBottom(subTablePadY);
			orders.row();
		}

		if (this.beer > 0) {
			orders.add(createSubTable(Assets.beerIcon, false, this.beer)).width(subTableWidth).height(subTableHeight).padBottom(subTablePadY);
			orders.row();
		}

		table.add(orders).padTop(tablePadTop).padBottom(tablePadBottom).padLeft(tablePadLeft).padRight(tablePadRight);
	}

	public Table createSubTable(TextureRegion icon, boolean spice, int quantity) {
		Table subTable = new Table();
		Label count;		
		count = new Label("" + quantity, Assets.generateLabelStyleUIHeavyWhite(22, Assets.nums));
		if (!spice) {
			count.setColor(MainStoreScreen.FONT_COLOR);
		}
		subTable.add(count);

		int iconPad = KebabKing.getGlobalX(0.01f);

		// Make sure same or less than above
		int iconHeight = KebabKing.getGlobalY(0.025f);
		int iconWidth = icon.getRegionWidth() * iconHeight / icon.getRegionHeight();
		subTable.add(new Image(icon)).width(iconWidth).height(iconHeight).padLeft(iconPad);
		if (spice) subTable.setBackground(new TextureRegionDrawable(Assets.redBright));

		//		int subTableWidth = ChuanrC.getGlobalX(0.13f);
		//		int subTableHeight = ChuanrC.getGlobalY(0.04f);
		//		subTable.setWidth(subTableWidth);
		//		subTable.setHeight(subTableHeight);

		return subTable;		
	}

	public Table createSubTable(MeatTypes.Type type, boolean spice, int quantity) {
		return createSubTable(type.icon, spice, quantity);
	}

	// draw this order
	public void draw(float x, float y) {
		int customerWidth = (int) (Customer.TEXTURE_WIDTH * KitchenScreen.UNIT_WIDTH);
		int customerHeight = (int) (Customer.TEXTURE_HEIGHT * KitchenScreen.UNIT_HEIGHT);

		table.setPosition(x + customerWidth * 0.75f, y + customerHeight * 0.45f - getTotalTypes() * customerHeight * 0.075f);
		stage.draw();
	}

	/** 
	 * Print the order in an easy to read format
	 */
	public String toString() {
		StringBuilder str = new StringBuilder("");

		if (first > 0) {
			if (firstSpicy)
				str.append(first + " sp chicken, \n");
			else
				str.append(first + " chicken, \n");
		}
		if (second > 0) {
			if (secondSpicy)
				str.append(second + " sp beef, \n");
			else
				str.append(second + " beef, \n");
		}
		if (third > 0) {
			if (thirdSpicy)
				str.append(third + " sp lamb \n");
			else
				str.append(third + " lamb \n");
		}

		if (beer > 0)
			str.append(beer + " beer.");
		return str.toString();
	}

	public boolean hasSpicy() {
		return	(firstSpicy && first > 0) || 
				(secondSpicy && second > 0) ||
				(thirdSpicy && third > 0);
	}
	
	public MeatTypes.Type getSpicyType() {
		if (firstSpicy && first > 0)
			return ks.grill.getType(Selected.FIRST);
		if (secondSpicy && second > 0)
			return ks.grill.getType(Selected.SECOND);
		if (thirdSpicy && third > 0)
			return ks.grill.getType(Selected.THIRD);
		return null;
	}
	
//	public void removeSpice() {
//		this.firstSpicy = this.secondSpicy = this.thirdSpicy = false;
//	}

	public boolean hasDouble() {
		if (ks.grill.getType(Selected.FIRST) != null) {
			if (ks.grill.getType(Selected.FIRST).doubleWidth) return true;
		}
		if (ks.grill.getType(Selected.SECOND) != null) {
			if (ks.grill.getType(Selected.SECOND).doubleWidth) return true;
		}
		if (ks.grill.getType(Selected.THIRD) != null)
			if (ks.grill.getType(Selected.THIRD).doubleWidth) return true;
		return false;
	}
	
	public boolean hasBeer() {
		return this.beer > 0;
	}
//	
//	public void removeBeer() {
//		this.beer = 0;
//	}
	
	public MeatTypes.Type getDoubleType() {
		if (ks.grill.getType(Selected.FIRST).doubleWidth)
			return ks.grill.getType(Selected.FIRST);
		if (ks.grill.getType(Selected.SECOND).doubleWidth)
			return ks.grill.getType(Selected.SECOND);
		if (ks.grill.getType(Selected.THIRD).doubleWidth) 
			return ks.grill.getType(Selected.THIRD);
		return null;
	}
	
	// returns number of different types of items remaining (1-3)
	public int getTotalTypes() {
		int types = 0;
		if (first > 0) types++;
		if (second > 0) types++;
		if (third > 0) types++;

		if (beer > 0) types++;
		return types;
	}
}
