package com.blakebr0.ironjetpacks.config;

import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.registry.Jetpack;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class ModJetpacks {
	private static final Logger LOGGER = LogManager.getLogger(IronJetpacks.NAME);
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

	private static final Jetpack WOOD = new Jetpack("wood", 0, 0x6B511F, 1, 15, "tag:minecraft:planks", 0F, 0F);
	private static final Jetpack STONE = new Jetpack("stone", 1, 0x7F7F7F, 2, 12, "tag:forge:stone", 0F, 0F);
	private static final Jetpack IRON = new Jetpack("iron", 2, 0xC1C1C1, 3, 9, "tag:forge:ingots/iron", 0F, 0F);
	private static final Jetpack GOLD = new Jetpack("gold", 3, 0xDEDE00, 2, 25, "tag:forge:ingots/gold", 0F, 0F);
	private static final Jetpack DIAMOND = new Jetpack("diamond", 4, 0x4AEDD1, 4, 10, "tag:forge:gems/diamond", 0F, 0F);
	private static final Jetpack EMERALD = new Jetpack("emerald", 5, 0x41F384, 4, 15, "tag:forge:gems/emerald", 0F, 0F);
	private static final Jetpack CREATIVE = new Jetpack("creative", 0, 0xCF1AE9, 8, 0, "null", 0F, 0F).setCreative();

	private static final Jetpack COPPER = new Jetpack("copper", 1, 0xCE7201, 2, 12, "tag:forge:ingots/copper", 0F, 0F);
	private static final Jetpack BRONZE = new Jetpack("bronze", 2, 0xEC9E3F, 3, 9, "tag:forge:ingots/bronze", 0F, 0F);
	private static final Jetpack SILVER = new Jetpack("silver", 2, 0x9FC4DD, 3, 12, "tag:forge:ingots/silver", 0F, 0F);
	private static final Jetpack STEEL = new Jetpack("steel", 3, 0x565656, 3, 15, "tag:forge:ingots/steel", 0F, 0F);
	private static final Jetpack ELECTRUM = new Jetpack("electrum", 3, 0xA79135, 2, 18, "tag:forge:ingots/electrum", 0F, 0F);
	private static final Jetpack INVAR = new Jetpack("invar", 3, 0x929D97, 3, 15, "tag:forge:ingots/invar", 0F, 0F);
	private static final Jetpack PLATINUM = new Jetpack("platinum", 4, 0x6FEAEF, 4, 12, "tag:forge:ingots/platinum", 0F, 0F);

	static {
		WOOD.setStats(20000, 32, 0.18D, 0.10D, 0.06D, 0.16D, 0.14D, 1.0D, 1.0D, 1.0D);
		STONE.setStats(100000, 70, 0.25D, 0.11D, 0.08D, 0.18D, 0.1D, 1.0D, 1.0D, 1.0D);
		IRON.setStats(800000, 120, 0.41D, 0.12D, 0.14D, 0.27D, 0.075D, 1.1D, 1.05D, 2.1D);
		GOLD.setStats(10000000, 300, 0.61D, 0.13D, 0.15D, 0.34D, 0.03D, 1.5D, 1.25D, 3.2D);
		DIAMOND.setStats(30000000, 650, 0.90D, 0.15D, 0.19D, 0.41D, 0.005D, 1.8D, 1.4D, 3.8D);
		EMERALD.setStats(48000000, 880, 1.03D, 0.17D, 0.21D, 0.45D, 0.0D, 2.0D, 1.5D, 4.0D);
		CREATIVE.setStats(0, 0, 1.03D, 0.17D, 0.21D, 0.45D, 0.0D, 2.0D, 1.5D, 0.0D);

		COPPER.setStats(250000, 85, 0.29D, 0.11D, 0.1D, 0.23D, 0.092D, 1.05D, 1.025D, 1.4D);
		BRONZE.setStats(800000, 120, 0.41D, 0.12D, 0.14D, 0.27D, 0.075D, 1.1D, 1.05D, 2.1D);
		SILVER.setStats(1200000, 150, 0.48D, 0.13D, 0.15D, 0.3D, 0.07D, 1.3D, 1.15D, 2.7D);
		STEEL.setStats(12000000, 350, 0.67D, 0.135D, 0.155D, 0.35D, 0.025D, 1.5D, 1.25D, 3.2D);
		ELECTRUM.setStats(10000000, 310, 0.79D, 0.14D, 0.17D, 0.37D, 0.03D, 1.6D, 1.3D, 3.5D);
		INVAR.setStats(12000000, 350, 0.61D, 0.13D, 0.15D, 0.34D, 0.03D, 1.5D, 1.25D, 3.2D);
		PLATINUM.setStats(36000000, 720, 0.92D, 0.155D, 0.193D, 0.42D, 0.005D, 1.8D,  1.4D, 3.8D);
	}
	
	public static void loadJsons() {
		final JetpackRegistry registry = JetpackRegistry.getInstance();
		File dir = FMLPaths.CONFIGDIR.get().resolve("ironjetpacks/jetpacks").toFile();
		
		if (!dir.exists() && dir.mkdirs()) {
			for (Jetpack jetpack : defaults()) {
				File file = new File(dir, jetpack.name + ".json");

				try (Writer writer = new FileWriter(file)) {
					GSON.toJson(jetpack.toJson(), writer);
				} catch (Exception e) {
					LOGGER.error("An error occurred while generating jetpack jsons", e);
				}
			}
		}
		
		File[] files = dir.listFiles((FileFilter) FileFilterUtils.suffixFileFilter(".json"));
		
		if (files == null)
			return;
		
		List<Jetpack> jetpacks = new ArrayList<>();
		
		for (File file : files) {
			Jetpack jetpack = null;
			FileReader reader = null;

			try {
				JsonParser parser = new JsonParser();
				reader = new FileReader(file);
				JsonObject json = parser.parse(reader).getAsJsonObject();

				reader.close();

				if (handleMigrations(json)) {
					try (Writer writer = new FileWriter(file)) {
						GSON.toJson(json, writer);
					} catch (Exception e) {
						LOGGER.error("An error occurred while migrating jetpack json {}", file.getName(), e);
						continue;
					}
				}

				jetpack = Jetpack.fromJson(json);
			} catch (Exception e) {
				LOGGER.error("An error occurred while reading jetpack json {}", file.getName(), e);
			} finally {
				IOUtils.closeQuietly(reader);
			}
			
			if (jetpack != null && !jetpack.disabled) {
				jetpacks.add(jetpack);
			}
		}

		jetpacks.sort(Comparator.comparingInt(Jetpack::getTier));
		
		for (Jetpack j : jetpacks) {
			registry.register(j);
		}
	}

	private static boolean handleMigrations(JsonObject json) {
		boolean changed = false;

		// add creative flag
		if (!json.has("creative")) {
			json.addProperty("creative", false);
			changed = true;
		}

		// add rarity field
		if (!json.has("rarity")) {
			json.addProperty("rarity", 0);
			changed = true;
		}

		// add vertical sprint speed field
		if (!json.has("sprintSpeedMultiVertical")) {
			json.addProperty("sprintSpeedMultiVertical", 1.0D);
			changed = true;
		}

		// add armor toughness field
		if (!json.has("toughness")) {
			json.addProperty("toughness", 0F);
			changed = true;
		}

		// add knockback resistance field
		if (!json.has("knockbackResistance")) {
			json.addProperty("knockbackResistance", 0F);
			changed = true;
		}

		return changed;
	}
	
	private static List<Jetpack> defaults() {
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
