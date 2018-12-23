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
import com.blakebr0.ironjetpacks.registry.Jetpack;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry;
import com.google.gson.Gson;

import net.minecraft.init.Blocks;

public class ModJetpacks {
	
	private static final Jetpack WOOD = JetpackRegistry.createJetpack("wood", 0, 0x6B511F, 1, 15, ItemPlaceholder.of("plankWood"));
	private static final Jetpack STONE = JetpackRegistry.createJetpack("stone", 1, 0x7F7F7F, 2, 12, ItemPlaceholder.of(Blocks.STONE));
	private static final Jetpack IRON = JetpackRegistry.createJetpack("iron", 2, 0xC1C1C1, 3, 9, ItemPlaceholder.of("ingotIron"));
	private static final Jetpack GOLD = JetpackRegistry.createJetpack("gold", 3, 0xDEDE00, 2, 25, ItemPlaceholder.of("ingotGold"));
	private static final Jetpack DIAMOND = JetpackRegistry.createJetpack("diamond", 4, 0x4AEDD1, 4, 10, ItemPlaceholder.of("gemDiamond"));
	private static final Jetpack EMERALD = JetpackRegistry.createJetpack("emerald", 5, 0x41F384, 4, 15, ItemPlaceholder.of("gemEmerald"));
	private static final Jetpack CREATIVE = JetpackRegistry.createJetpack("creative", 0, 0xCF1AE9, 8, 0, null).setCreative();

	private static final Jetpack COPPER = JetpackRegistry.createJetpack("copper", 1, 0xCE7201, 2, 12, ItemPlaceholder.of("ingotCopper"));
	private static final Jetpack BRONZE = JetpackRegistry.createJetpack("bronze", 2, 0xEC9E3F, 3, 9, ItemPlaceholder.of("ingotBronze"));
	private static final Jetpack SILVER = JetpackRegistry.createJetpack("silver", 2, 0x9FC4DD, 3, 12, ItemPlaceholder.of("ingotSilver"));
	private static final Jetpack STEEL = JetpackRegistry.createJetpack("steel", 3, 0x565656, 3, 15, ItemPlaceholder.of("ingotSteel"));
	private static final Jetpack ELECTRUM = JetpackRegistry.createJetpack("electrum", 3, 0xA79135, 2, 18, ItemPlaceholder.of("ingotElectrum"));
	private static final Jetpack INVAR = JetpackRegistry.createJetpack("invar", 3, 0x929D97, 3, 15, ItemPlaceholder.of("ingotInvar"));
	private static final Jetpack PLATINUM = JetpackRegistry.createJetpack("platinum", 4, 0x6FEAEF, 4, 12, ItemPlaceholder.of("ingotPlatinum"));

	static {		
		WOOD.setStats(20000, 32, 0.18D, 0.10D, 0.06D, 0.16D, 0.14D, 1.0D, 1.0D);
		STONE.setStats(100000, 70, 0.25D, 0.11D, 0.08D, 0.18D, 0.1D, 1.0D, 1.0D);
		IRON.setStats(800000, 120, 0.41D, 0.12D, 0.14D, 0.27D, 0.075D, 1.1D, 2.1D);
		GOLD.setStats(10000000, 300, 0.61D, 0.13D, 0.15D, 0.34D, 0.03D, 1.5D, 3.2D);
		DIAMOND.setStats(30000000, 650, 0.90D, 0.15D, 0.19D, 0.41D, 0.005D, 1.8D, 3.8D);
		EMERALD.setStats(48000000, 880, 1.03D, 0.17D, 0.21D, 0.45D, 0.0D, 2.0D, 4.0D);
		CREATIVE.setStats(0, 0, 1.03D, 0.17D, 0.21D, 0.45D, 0.0D, 2.0D, 0.0D);

		COPPER.setStats(250000, 85, 0.29D, 0.11D, 0.1D, 0.23D, 0.092D, 1.05D, 1.4D);
		BRONZE.setStats(800000, 120, 0.41D, 0.12D, 0.14D, 0.27D, 0.075D, 1.1D, 2.1D);
		SILVER.setStats(1200000, 150, 0.48D, 0.13D, 0.15D, 0.3D, 0.07D, 1.3D, 2.7D);
		STEEL.setStats(12000000, 350, 0.67D, 0.135D, 0.155D, 0.35D, 0.025D, 1.5D, 3.2D);
		ELECTRUM.setStats(10000000, 310, 0.79D, 0.14D, 0.17D, 0.37D, 0.03D, 1.6D, 3.5D);
		INVAR.setStats(12000000, 350, 0.61D, 0.13D, 0.15D, 0.34D, 0.03D, 1.5D, 3.2D);
		PLATINUM.setStats(36000000, 720, 0.92D, 0.155D, 0.193D, 0.42D, 0.005D, 1.8D, 3.8D);
	}
	
	public static void init(File dir) {
		final JetpackRegistry registry = JetpackRegistry.getInstance();
		Gson gson = Serializers.initGson();
		
		if (!dir.exists() && dir.mkdirs()) {		
			for (Jetpack jetpack : defaults()) {
				String json = gson.toJson(jetpack);
				try {
					File file = new File(dir, jetpack.name + ".json");
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
			
			if (!jetpack.disabled) {
				jetpacks.add(jetpack);
			}
		}
		
		jetpacks.sort(Comparator.comparingInt(Jetpack::getTier));
		
		for (Jetpack j : jetpacks) {
			registry.register(j);
		}
	}
	
	public static List<Jetpack> defaults() {
		List<Jetpack> defaults = new ArrayList<>();
		defaults.add(WOOD);
		defaults.add(STONE);
		defaults.add(IRON);
		defaults.add(GOLD);
		defaults.add(DIAMOND);
		defaults.add(EMERALD);
		defaults.add(CREATIVE);
		
		defaults.add(COPPER);
		defaults.add(BRONZE);
		defaults.add(SILVER);
		defaults.add(STEEL);
		defaults.add(ELECTRUM);
		defaults.add(INVAR);
		defaults.add(PLATINUM);
		
		defaults.sort(Comparator.comparingInt(Jetpack::getTier));	
		
		return defaults;
	}
}
