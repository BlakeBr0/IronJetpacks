package com.blakebr0.ironjetpacks.config;

import java.io.File;

import com.blakebr0.ironjetpacks.IronJetpacks;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid = IronJetpacks.MOD_ID)
public class ModConfig {
	
	public static Configuration config;
	
	public static boolean confJetpackSounds;
	public static boolean confJetpackParticles;
	public static boolean confAdvancedInfo;
	
	public static boolean confEnableHud;
	public static int confHudPosMode;
	public static int confHudOffsetX;
	public static int confHudOffsetY;
	public static boolean confShowHudOnChat;
	
	public static boolean confEnchantableJetpacks;

	public static boolean confBasicRecipes;
	public static boolean confUpgradeRecipes;
	
	@SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(IronJetpacks.MOD_ID)) {
            ModConfig.syncConfig();
        }
    }
	
	public static void init(File file) {
        config = new Configuration(file);
        syncConfig();
	}
	
	public static void syncConfig() {
		String category;
		
		category = "client";
		config.setCategoryComment(category, "General client settings");
		confJetpackSounds = config.getBoolean("jetpack_sounds", category, true, "Should jetpack sounds be enabled?");
		confJetpackParticles = config.getBoolean("jetpack_particles", category, true, "Should jetpack particles be enabled?");
		confAdvancedInfo = config.getBoolean("advanced_info", category, true, "Should the advanced stat info tooltips for jetpacks be enabled?");
		
		category = "hud";
		config.setCategoryComment(category, "HUD settings");
		confEnableHud = config.getBoolean("hud_enabled", category, true, "Should the hud be enabled?");
		confHudPosMode = config.getInt("hud_position", category, 1, 0, 5, "The position of the hud. 0=Top Left, 1=Middle Left, 2=Bottom Left, 3=Top Right, 4=Middle Right, 5=Bottom Right");
		confHudOffsetX = config.getInt("hud_offset_x", category, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, "The X offset of the hud.");
		confHudOffsetY = config.getInt("hud_offset_y", category, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, "The Y offset of the hud.");
		confShowHudOnChat = config.getBoolean("hud_over_chat", category, false, "Should the hud render over the chat?");
		
		category = "general";
		config.setCategoryComment(category, "General settings");
		confEnchantableJetpacks = config.getBoolean("enchantable_jetpacks", category, true, "Should jetpacks be enchantable?");
		
		category = "recipes";
		config.setCategoryComment(category, "Recipe settings");
		confBasicRecipes = config.getBoolean("basic_recipes", category, false, "Non-nested recipes. Each jetpack requires a Leather Strap instead of a previous tier jetpack.");
		confUpgradeRecipes = config.getBoolean("upgrade_recipes", category, true, "Nested recipes. Each jetpack requires a previous tier jetpack.");
		
		if (config.hasChanged()) {
			config.save();
		}
	}
}
