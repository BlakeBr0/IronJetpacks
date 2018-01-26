package com.blakebr0.ironjetpacks.lib;

import com.blakebr0.cucumber.util.Utils;

import net.minecraft.util.text.TextFormatting;

public enum Tooltips {
	
	HOLD_SHIFT("hold_shift"),
	
	ON("on"),
	OFF("off"),
	
	CREATIVE("creative"),
	INFINITE("infinite"),
	
	ENGINE("engine"),
	HOVER("hover"),
	TIER("tier"),
	
	FUEL_USAGE("fuel_usage"),
	VERTICAL_SPEED("vert_speed"),
	VERTICAL_ACCELERATION("vert_accel"),
	HORIZONTAL_SPEED("hori_speed"),
	HOVER_SPEED("hover_speed"),
	DESCEND_SPEED("desc_speed"),
	SPRINT_MODIFIER("sprint_mod"),
	SPRINT_FUEL_MODIFIER("sprint_fuel"),
	
	TOGGLED_ENGINE("toggle_engine"),
	TOGGLED_HOVER("toggle_hover"),
	
	;
	
	private String tip;
	
	Tooltips(String tip) {
		this.tip = Utils.localize("tooltip.ij." + tip);
	}
	
	public String get() {
		return this.tip;
	}
	
	public String get(int color) {
		return TextFormatting.values()[color % 16].toString() + this.tip;
	}
}
