package com.kebabking.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TrashPile {
	static final boolean DRAW_TRASH_PILE = true;

	static final int TRASH_PILE_SMALL = 2;
	static final int TRASH_PILE_MED = 4;
	static final int TRASH_PILE_BIG = 8;
	
	
	static final float TP_Y = 0.3f;
	
	static final float TP_WIDTH_L = 0.3f;
	static final float TP_WIDTH_M = 0.3f;
	static final float TP_WIDTH_S = 0.3f;
	
	static final float TP_HEIGHT_L = 0.2f;
	static final float TP_HEIGHT_M = 0.15f;
	static final float TP_HEIGHT_S = 0.1f;

	static final float TP_X_L = 0.5f - TP_WIDTH_L/2;
	static final float TP_X_M = 0.5f - TP_WIDTH_M/2;
	static final float TP_X_S = 0.5f - TP_WIDTH_S/2;

	KitchenScreen ks;
	
	
	public TrashPile(KitchenScreen ks) {
		this.ks = ks;
	}
	
	public void draw(SpriteBatch batch) {
		if (ks.kebabsTrashed >= TRASH_PILE_BIG) {
			batch.draw(Assets.trashIcon, KebabKing.getGlobalX(TP_X_L), KebabKing.getGlobalY(TP_Y), KebabKing.getGlobalX(TP_WIDTH_L), KebabKing.getGlobalY(TP_HEIGHT_L));
		}
		else if (ks.kebabsTrashed >= TRASH_PILE_MED) {
			batch.draw(Assets.trashIcon, KebabKing.getGlobalX(TP_X_M), KebabKing.getGlobalY(TP_Y), KebabKing.getGlobalX(TP_WIDTH_M), KebabKing.getGlobalY(TP_HEIGHT_M));			
		}
		else if (ks.kebabsTrashed >= TRASH_PILE_SMALL) {
			batch.draw(Assets.trashIcon, KebabKing.getGlobalX(TP_X_S), KebabKing.getGlobalY(TP_Y), KebabKing.getGlobalX(TP_WIDTH_S), KebabKing.getGlobalY(TP_HEIGHT_S));
		}
	}
}
