package com.blakebr0.ironjetpacks.config;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.io.filefilter.FileFilterUtils;

import com.blakebr0.cucumber.lib.ItemPlaceholder;
import com.blakebr0.ironjetpacks.config.json.Serializers;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry.Jetpack;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry.JetpackEntry;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry.JetpackType;
import com.google.gson.Gson;

import net.minecraft.init.Blocks;

public class ModJetpacks {
	
	private static final JetpackType WOOD_TYPE = JetpackRegistry.createJetpackType("wood", 0, 0x6B511F, 1, 15, ItemPlaceholder.of("plankWood"));
	private static final JetpackEntry WOOD_INFO = JetpackRegistry.createJetpackInfo(20000, 32, 0.18D, 0.10D, 0.06D, 0.16D, 0.14D, 1.0D, 1.0D);
	
	private static final JetpackType STONE_TYPE = JetpackRegistry.createJetpackType("stone", 1, 0x7F7F7F, 2, 12, ItemPlaceholder.of(Blocks.STONE));
	private static final JetpackEntry STONE_INFO = JetpackRegistry.createJetpackInfo(100000, 70, 0.25D, 0.11D, 0.08D, 0.18D, 0.1D, 1.0D, 1.0D);
	
	private static final JetpackType IRON_TYPE = JetpackRegistry.createJetpackType("iron", 2, 0xC1C1C1, 3, 9, ItemPlaceholder.of("ingotIron"));
	private static final JetpackEntry IRON_INFO = JetpackRegistry.createJetpackInfo(800000, 120, 0.41D, 0.12D, 0.14D, 0.27D, 0.075D, 1.1D, 2.1D);

	private static final JetpackType GOLD_TYPE = JetpackRegistry.createJetpackType("gold", 3, 0xDEDE00, 2, 25, ItemPlaceholder.of("ingotGold"));
	private static final JetpackEntry GOLD_INFO = JetpackRegistry.createJetpackInfo(10000000, 300, 0.61D, 0.13D, 0.15D, 0.34D, 0.03D, 1.5D, 3.2D);
	
	private static final JetpackType DIAMOND_TYPE = JetpackRegistry.createJetpackType("diamond", 4, 0x4AEDD1, 4, 10, ItemPlaceholder.of("gemDiamond"));
	private static final JetpackEntry DIAMOND_INFO = JetpackRegistry.createJetpackInfo(30000000, 650, 0.90D, 0.15D, 0.19D, 0.41D, 0.005D, 1.8D, 3.8D);

	private static final JetpackType EMERALD_TYPE = JetpackRegistry.createJetpackType("emerald", 5, 0x41F384, 4, 15, ItemPlaceholder.of("gemEmerald"));
	private static final JetpackEntry EMERALD_INFO = JetpackRegistry.createJetpackInfo(48000000, 880, 1.03D, 0.17D, 0.21D, 0.45D, 0.0D, 2.0D, 4.0D);
	
	private static final JetpackType CREATIVE_TYPE = JetpackRegistry.createJetpackType("creative", 0, 0xCF1AE9, 8, 0, null).setCreative();
	private static final JetpackEntry CREATIVE_INFO = JetpackRegistry.createJetpackInfo(0, 0, 1.03D, 0.17D, 0.21D, 0.45D, 0.0D, 2.0D, 0.0D);
	
	private static final JetpackType COPPER_TYPE = JetpackRegistry.createJetpackType("copper", 1, 0xCE7201, 2, 12, ItemPlaceholder.of("ingotCopper"));
	private static final JetpackEntry COPPER_INFO = JetpackRegistry.createJetpackInfo(250000, 85, 0.29D, 0.11D, 0.1D, 0.23D, 0.092D, 1.05D, 1.4D);

	private static final JetpackType BRONZE_TYPE = JetpackRegistry.createJetpackType("bronze", 2, 0xEC9E3F, 3, 9, ItemPlaceholder.of("ingotBronze"));
	private static final JetpackEntry BRONZE_INFO = JetpackRegistry.createJetpackInfo(800000, 120, 0.41D, 0.12D, 0.14D, 0.27D, 0.075D, 1.1D, 2.1D);

	private static final JetpackType SILVER_TYPE = JetpackRegistry.createJetpackType("silver", 2, 0x9FC4DD, 3, 12, ItemPlaceholder.of("ingotSilver"));
	private static final JetpackEntry SILVER_INFO = JetpackRegistry.createJetpackInfo(1200000, 150, 0.48D, 0.13D, 0.15D, 0.3D, 0.07D, 1.3D, 2.7D);

	private static final JetpackType STEEL_TYPE = JetpackRegistry.createJetpackType("steel", 3, 0x565656, 3, 15, ItemPlaceholder.of("ingotSteel"));
	private static final JetpackEntry STEEL_INFO = JetpackRegistry.createJetpackInfo(12000000, 350, 0.67D, 0.135D, 0.155D, 0.35D, 0.025D, 1.5D, 3.2D);

	private static final JetpackType ELECTRUM_TYPE = JetpackRegistry.createJetpackType("electrum", 3, 0xA79135, 2, 18, ItemPlaceholder.of("ingotElectrum"));
	private static final JetpackEntry ELECTRUM_INFO = JetpackRegistry.createJetpackInfo(10000000, 310, 0.79D, 0.14D, 0.17D, 0.37D, 0.03D, 1.6D, 3.5D);

	private static final JetpackType INVAR_TYPE = JetpackRegistry.createJetpackType("invar", 3, 0x929D97, 3, 15, ItemPlaceholder.of("ingotInvar"));
	private static final JetpackEntry INVAR_INFO = JetpackRegistry.createJetpackInfo(12000000, 350, 0.61D, 0.13D, 0.15D, 0.34D, 0.03D, 1.5D, 3.2D);

	private static final JetpackType PLATINUM_TYPE = JetpackRegistry.createJetpackType("platinum", 4, 0x6FEAEF, 4, 12, ItemPlaceholder.of("ingotPlatinum"));
	private static final JetpackEntry PLATINUM_INFO = JetpackRegistry.createJetpackInfo(36000000, 720, 0.92D, 0.155D, 0.193D, 0.42D, 0.005D, 1.8D, 3.8D);
	
	public static void init(File dir) {
		final JetpackRegistry registry = JetpackRegistry.getInstance();
		Gson gson = Serializers.initGson();
		
		if (!dir.exists() && dir.mkdirs()) {		
			for (Jetpack jetpack : defaults()) {
				String json = gson.toJson(jetpack);
				try {
					File file = new File(dir, jetpack.type.name + ".json");
					FileWriter writer = new FileWriter(file);
					writer.write(json);
					writer.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		File[] files = dir.listFiles((FileFilter) FileFilterUtils.suffixFileFilter(".json"));
		
		if (files == null) {
			return;
		}
		
		List<Jetpack> jetpacks = new ArrayList<>();
		
		for (File file : files) {
			Jetpack jetpack = null;
			try {
				FileReader reader = new FileReader(file);
				jetpack = gson.fromJson(reader, Jetpack.class);
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if (!jetpack.type.disabled) {
				jetpacks.add(jetpack);
			}
		}
		
		jetpacks.sort(Comparator.comparingInt(Jetpack::getTier));
		
		for (Jetpack j : jetpacks) {
			registry.register(j.type, j.info);
		}
	}
	
	public static List<Jetpack> defaults() {
		List<Jetpack> defaults = new ArrayList<>();
		defaults.add(JetpackRegistry.createJetpack(WOOD_TYPE, WOOD_INFO));
		defaults.add(JetpackRegistry.createJetpack(STONE_TYPE, STONE_INFO));
		defaults.add(JetpackRegistry.createJetpack(IRON_TYPE, IRON_INFO));
		defaults.add(JetpackRegistry.createJetpack(GOLD_TYPE, GOLD_INFO));
		defaults.add(JetpackRegistry.createJetpack(DIAMOND_TYPE, DIAMOND_INFO));
		defaults.add(JetpackRegistry.createJetpack(EMERALD_TYPE, EMERALD_INFO));
		defaults.add(JetpackRegistry.createJetpack(CREATIVE_TYPE, CREATIVE_INFO));
		defaults.add(JetpackRegistry.createJetpack(COPPER_TYPE, COPPER_INFO));
		defaults.add(JetpackRegistry.createJetpack(BRONZE_TYPE, BRONZE_INFO));
		defaults.add(JetpackRegistry.createJetpack(SILVER_TYPE, SILVER_INFO));
		defaults.add(JetpackRegistry.createJetpack(STEEL_TYPE, STEEL_INFO));
		defaults.add(JetpackRegistry.createJetpack(ELECTRUM_TYPE, ELECTRUM_INFO));
		defaults.add(JetpackRegistry.createJetpack(INVAR_TYPE, INVAR_INFO));
		defaults.add(JetpackRegistry.createJetpack(PLATINUM_TYPE, PLATINUM_INFO));
		
		defaults.sort(Comparator.comparingInt(Jetpack::getTier));		
		return defaults;
	}
}
