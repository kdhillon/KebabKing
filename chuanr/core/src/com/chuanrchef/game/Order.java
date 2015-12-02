package com.chuanrchef.game;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.chuanrchef.game.Customer.CustomerType;

public class Order {
	static final float ORDER_ROW_WIDTH = 1.25f;
	static final float ORDER_ROW_HEIGHT = .675f;
	static final float ICON_WIDTH  = 0.75f; // times unit height
	static final float ICON_HEIGHT = 0.63f;
	static final float ICON_OFFSET_X = 0.45f;
	static final float ICON_OFFSET_Y = 0.02f;
	static final float FONT_OFFSET_Y = 0.54f;
	
	// represents a food order 
	// too keep things simpler, allow either only spicy or not, and max two types 
	int chicken;
	int beef;
	int lamb;
	
	boolean chickenSpicy;
	boolean beefSpicy;
	boolean lambSpicy;
	
	int beer;
	int total;
	
	int remaining;
	int raw; 			 // how many raw meats were given to this person
	int burnt; 			 // how many chuanrs were burnt
	int incorrect;		 // how many wrong chuanrs/beers were given to this person	
	
	Stage stage; // for drawing order
	Table table;
	
	// generate a totally random order based on the given type
	public Order(CustomerType type) {
		chickenSpicy = (Math.random() < .5);
		beefSpicy = (Math.random() < .5);
		lambSpicy = (Math.random() < .5);
		
		int min = type.minOrder;
		int max = type.maxOrder;
		
		// just the meat
		total = (int) (Math.random() * (max-min) + min);
		

		int count1 = (int) (Math.random() * total);
		int count2 = total-count1;
				
		double random = Math.random();
		// no chicken
		if (random < .33) {
			beef = count1;
			lamb = count2;
		}
		// no beef
		else if (random < .66) {
			chicken = count1;
			lamb = count2;
		}
		// no lamb
		else {
			chicken = count1;
			beef = count2;
		}
		
		// because Val wanted it.
		chicken /= 2;
		
		double beerRandom = Math.random();
		if (beerRandom < type.beerFactor) beer = 1;
		if (beerRandom * 2 < type.beerFactor) beer = 2;
		if (beerRandom * 3 < type.beerFactor) beer = 3;
		
		total = chicken + beef + lamb + beer;
		remaining = total;
		
		this.stage = new Stage();
//		stage.setDebugAll(true);
		table = new Table();
		stage.addActor(table);
		updateTable();
	}
	
	// returns total money received from this 
	public float giveMeat(ArrayList<Meat> set) {
		float revenue = 0;
		for (Meat m : set) revenue += giveMeat(m);
//		System.out.println(incorrect);
		updateTable();
		return revenue;
	}
	
	public float giveMeat(Meat meat) {
		switch (meat.type) {
		case CHICKEN:
			if (meat.spiced != chickenSpicy || chicken == 0) {
				System.out.println("wrong chicken");
				incorrect++;
				return 0;
			}
			break;
		case BEEF:
			if (meat.spiced != beefSpicy || beef == 0) {
				System.out.println("wrong beef");
				incorrect++;
				return 0;
			}
			break;
		case LAMB:
			if (meat.spiced != lambSpicy || lamb == 0) {
				System.out.println("wrong lamb");
				incorrect++;
				return 0;
			}
		}
		
		Meat.Type type = meat.type;
		if (type == Meat.Type.CHICKEN)
			chicken--;
		else if (type == Meat.Type.BEEF)
			beef--;
		else if (type == Meat.Type.LAMB)
			lamb--;
		
		remaining--;
		if (meat.state == Meat.State.BURNT) burnt++;
		if (meat.state == Meat.State.RAW) raw++;
		
		return Meat.getSellPrice(meat.type) - Meat.getBuyPrice(meat.type);
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
		return KitchenScreen.BEER_SELL_PRICE;
	}
	
	// table should include the speech bubble, orders, everything
	public void updateTable() {
		table.clear();
		
		int tablePadLeft = ChuanrC.getGlobalX(0.025f);
		int tablePadRight = ChuanrC.getGlobalX(0.01f);
		int tablePadTop = ChuanrC.getGlobalY(0.008f);
		int tablePadBottom = ChuanrC.getGlobalY(0.005f);
		
		int subTableWidth = ChuanrC.getGlobalX(0.095f);
		int subTableHeight = ChuanrC.getGlobalY(0.03f);
		int subTablePadY = ChuanrC.getGlobalY(0.003f);
		
		// should be a fixed tableWidth;
		int tableWidth = subTableWidth + tablePadLeft + tablePadRight;
 		int tableHeight = getTotalTypes() * subTableHeight + tablePadTop + tablePadBottom + subTablePadY * getTotalTypes();
		
		table.setBackground(new TextureRegionDrawable(Assets.speech));
		table.setSize(tableWidth, tableHeight);

		Table orders = new Table();
		
		if (this.chicken > 0) {
			orders.add(createSubTable(Meat.Type.CHICKEN, chickenSpicy, this.chicken)).width(subTableWidth).height(subTableHeight).padBottom(subTablePadY);
			orders.row();
		}
		if (this.beef > 0) {
			orders.add(createSubTable(Meat.Type.BEEF, beefSpicy, this.beef)).width(subTableWidth).height(subTableHeight).padBottom(subTablePadY);
			orders.row();
		}
		if (this.lamb > 0) {
			orders.add(createSubTable(Meat.Type.LAMB, lambSpicy, this.lamb)).width(subTableWidth).height(subTableHeight).padBottom(subTablePadY);
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
		Label count = new Label("" + quantity, Assets.generateLabelStyleUIHeavy(26, true));
		if (spice) {
			count.setColor(new Color(1, 1, 1, 1));
		}
		else {
			count.setColor(new Color(0, 0, 0, 1f));
		}
		subTable.add(count);
		
		int iconPad = ChuanrC.getGlobalX(0.01f);
		
		// Make sure same or less than above
		int iconHeight = ChuanrC.getGlobalY(0.03f);
		int iconWidth = icon.getRegionWidth() * iconHeight / icon.getRegionHeight();
		subTable.add(new Image(icon)).width(iconWidth).height(iconHeight).padLeft(iconPad);
		if (spice) subTable.setBackground(new TextureRegionDrawable(Assets.red));
		
//		int subTableWidth = ChuanrC.getGlobalX(0.13f);
//		int subTableHeight = ChuanrC.getGlobalY(0.04f);
//		subTable.setWidth(subTableWidth);
//		subTable.setHeight(subTableHeight);
		
		return subTable;		
	}
	
	public Table createSubTable(Meat.Type type, boolean spice, int quantity) {
		return createSubTable(Assets.getIcon(type), spice, quantity);
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
		
		if (chicken > 0) {
			if (chickenSpicy)
				str.append(chicken + " sp chicken, \n");
			else
				str.append(chicken + " chicken, \n");
		}
		if (beef > 0) {
			if (beefSpicy)
				str.append(beef + " sp beef, \n");
			else
				str.append(beef + " beef, \n");
		}
		if (lamb > 0) {
			if (lambSpicy)
				str.append(lamb + " sp lamb \n");
			else
				str.append(lamb + " lamb \n");
		}
		
		if (beer > 0)
			str.append(beer + " beer.");
		return str.toString();
	}
	
	// returns number of different types of items remaining (1-3)
	public int getTotalTypes() {
		// TODO remove for testing
//		return 3;
		
		int types = 0;
		if (chicken > 0) types++;
		if (beef > 0) types++;
		if (lamb > 0) types++;
		
		if (beer > 0) types++;
		return types;
	}
}
