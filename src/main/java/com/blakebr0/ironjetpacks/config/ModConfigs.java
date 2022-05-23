package com.blakebr0.ironjetpacks.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModList;

public final class ModConfigs {
	public static final ForgeConfigSpec CLIENT;
	public static final ForgeConfigSpec COMMON;

	public static final ForgeConfigSpec.BooleanValue ENABLE_JETPACK_SOUNDS;
	public static final ForgeConfigSpec.BooleanValue ENABLE_JETPACK_PARTICLES;
	public static final ForgeConfigSpec.BooleanValue ENABLE_ADVANCED_INFO_TOOLTIPS;

	public static final ForgeConfigSpec.BooleanValue ENABLE_HUD;
	public static final ForgeConfigSpec.IntValue HUD_POSITION;
	public static final ForgeConfigSpec.IntValue HUD_OFFSET_X;
	public static final ForgeConfigSpec.IntValue HUD_OFFSET_Y;
	public static final ForgeConfigSpec.BooleanValue SHOW_HUD_OVER_CHAT;

	// Client
	static {
		final var client = new ForgeConfigSpec.Builder();

		client.comment("General configuration options.").push("General");
		ENABLE_JETPACK_SOUNDS = client
				.comment("Enable jetpack sounds?")
				.define("jetpackSounds", true);
		ENABLE_JETPACK_PARTICLES = client
				.comment("Enable jetpack particles?")
				.define("jetpackParticles", true);
		ENABLE_ADVANCED_INFO_TOOLTIPS = client
				.comment("Enable jetpack stat tooltips?")
				.define("advancedTooltips", true);
		client.pop();

		client.comment("HUD configuration options.").push("HUD");
		ENABLE_HUD = client
				.comment("Enable the HUD?")
				.define("enable", true);
		HUD_POSITION = client
				.comment("The position preset for the HUD.", "0=Top Left, 1=Middle Left, 2=Bottom Left, 3=Top Right, 4=Middle Right, 5=Bottom Right")
				.defineInRange("position", 1, 0, 5);
		HUD_OFFSET_X = client
				.comment("The X offset for the HUD.")
				.defineInRange("offsetX", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
		HUD_OFFSET_Y = client
				.comment("The Y offset for the HUD.")
				.defineInRange("offsetY", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
		SHOW_HUD_OVER_CHAT = client
				.comment("Show HUD over the chat?")
				.define("showOverChat", false);
		client.pop();

		CLIENT = client.build();
	}

	public static final ForgeConfigSpec.BooleanValue ENCHANTABLE_JETPACKS;
	public static final ForgeConfigSpec.BooleanValue ENABLE_CURIOS_INTEGRATION;

	public static final ForgeConfigSpec.BooleanValue ENABLE_CELL_RECIPES;
	public static final ForgeConfigSpec.BooleanValue ENABLE_THRUSTER_RECIPES;
	public static final ForgeConfigSpec.BooleanValue ENABLE_CAPACITOR_RECIPES;
	public static final ForgeConfigSpec.BooleanValue ENABLE_JETPACK_RECIPES;

	// Common
	static {
		final var common = new ForgeConfigSpec.Builder();

		common.comment("General configuration options.").push("General");
		ENCHANTABLE_JETPACKS = common
				.comment("Should jetpacks be enchantable?")
				.define("enchantableJetpacks", false);
		ENABLE_CURIOS_INTEGRATION = common
				.comment("Enable Curios integration.")
				.define("curiosIntegration", true);
		common.pop();

		common.comment("Dynamic recipe options.").push("Recipe");
		ENABLE_CELL_RECIPES = common
				.comment("Enable default recipes for Energy Cells?")
				.define("cells", true);
		ENABLE_THRUSTER_RECIPES = common
				.comment("Enable default recipes for Thrusters?")
				.define("thrusters", true);
		ENABLE_CAPACITOR_RECIPES = common
				.comment("Enable default recipes for Capacitors?")
				.define("capacitors", true);
		ENABLE_JETPACK_RECIPES = common
				.comment("Enable default recipes for Jetpacks?")
				.define("jetpacks", true);
		common.pop();

		COMMON = common.build();
	}

	public static boolean isCuriosInstalled() {
		return ModList.get().isLoaded("curios");
	}

	public static boolean isCuriosEnabled() {
		return isCuriosInstalled() && ENABLE_CURIOS_INTEGRATION.get();
	}
}
