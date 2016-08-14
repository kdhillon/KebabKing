package com.kebabking.game;
import java.util.Locale;

import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Language {
	String code;
	Locale locale;
	FreeTypeFontGenerator regular;
	FreeTypeFontGenerator heavy;
	FreeTypeFontGenerator light;
	FreeTypeFontGenerator chinaFont;
	FreeTypeFontGenerator titleFont;

	boolean extraPadding;
	public Language(String code, FreeTypeFontGenerator regular, FreeTypeFontGenerator heavy, FreeTypeFontGenerator light, FreeTypeFontGenerator china, FreeTypeFontGenerator title, boolean pad) {
		this.locale = new Locale(code);
		this.regular = regular;
		this.heavy = heavy;
		this.light = light;
		this.chinaFont = china;
		this.titleFont = title;
		this.extraPadding = pad;
	}
}
