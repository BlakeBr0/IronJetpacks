package com.blakebr0.ironjetpacks.registry;

import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.init.ModItems;
import com.blakebr0.ironjetpacks.lib.ModJetpacks;
import com.blakebr0.ironjetpacks.network.payloads.SyncJetpacksPayload;
import com.google.common.base.Stopwatch;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class JetpackRegistry {
	private static final JetpackRegistry INSTANCE = new JetpackRegistry();
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	private final Map<Identifier, Jetpack> jetpacks = new LinkedHashMap<>();
	private final ArrayList<Integer> tiers = new ArrayList<>();
	private int lowestTier = Integer.MAX_VALUE;

	@SubscribeEvent
	public void onDatapackSync(OnDatapackSyncEvent event) {
		var payload = new SyncJetpacksPayload(this.getJetpacks());
		var player = event.getPlayer();

		if (player != null) {
			PacketDistributor.sendToPlayer(player, payload);
		} else {
			PacketDistributor.sendToAllPlayers(payload);
		}
	}

	public void register(Jetpack jetpack) {
		if (this.jetpacks.containsKey(jetpack.getId())) {
			throw new RuntimeException(String.format("Tried to register multiple jetpacks with the same name: %s", jetpack.name));
		}

		this.jetpacks.put(jetpack.getId(), jetpack);

		if (jetpack.tier > -1 && !this.tiers.contains(jetpack.tier)) {
			this.tiers.add(jetpack.tier);
			this.tiers.sort(Integer::compareTo);
		}

		if (jetpack.tier > -1 && jetpack.tier < this.lowestTier) {
			this.lowestTier = jetpack.tier;
		}
	}

	public List<Jetpack> getJetpacks() {
		return new ArrayList<>(this.jetpacks.values());
	}

	public List<Integer> getAllTiers() {
		return this.tiers;
	}

	public Integer getLowestTier() {
		return this.lowestTier;
	}

	public Jetpack getJetpackById(Identifier id) {
		return this.jetpacks.getOrDefault(id, Jetpack.UNDEFINED);
	}

	public Item getCoilForTier(int tier) {
		float tiers = this.tiers.size();
		float index = this.tiers.indexOf(tier);

		if (index / tiers > 0.75F)
			return ModItems.ULTIMATE_COIL.get();

		if (index / tiers > 0.5F)
			return ModItems.ELITE_COIL.get();

		if (index / tiers > 0.25F)
			return ModItems.ADVANCED_COIL.get();

		return ModItems.BASIC_COIL.get();
	}

	public void loadJetpacks(SyncJetpacksPayload payload) {
		this.jetpacks.clear();

		for (var jetpack : payload.jetpacks()) {
			this.jetpacks.put(jetpack.getId(), jetpack);
		}

		IronJetpacks.LOGGER.info("Loaded {} jetpacks from the server", this.jetpacks.size());
	}

	public void writeDefaultJetpackFiles() {
		var dir = FMLPaths.CONFIGDIR.get().resolve("ironjetpacks/jetpacks").toFile();

		if (!dir.exists() && dir.mkdirs()) {
			for (var jetpack : ModJetpacks.getDefaults()) {
				var file = new File(dir, jetpack.name + ".json");

				try (var writer = new FileWriter(file)) {
					GSON.toJson(jetpack.toJson(), writer);
				} catch (Exception e) {
					IronJetpacks.LOGGER.error("An error occurred while generating jetpack jsons", e);
				}
			}
		}
	}

	public void loadJetpacks() {
		var stopwatch = Stopwatch.createStarted();
		var dir = FMLPaths.CONFIGDIR.get().resolve("ironjetpacks/jetpacks").toFile();

		this.writeDefaultJetpackFiles();

		this.jetpacks.clear();

		if (!dir.mkdirs() && dir.isDirectory()) {
			this.loadFiles(dir);
		}

		stopwatch.stop();

		IronJetpacks.LOGGER.info("Loaded {} jetpack type(s) in {} ms", this.jetpacks.size(), stopwatch.elapsed(TimeUnit.MILLISECONDS));
	}

	private void loadFiles(File dir) {
		var files = dir.listFiles((FileFilter) FileFilterUtils.suffixFileFilter(".json"));

		if (files == null)
			return;

		List<Jetpack> jetpacks = new ArrayList<>();

		for (var file : files) {
			Jetpack jetpack = null;
			InputStreamReader reader = null;

			try {
				reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);

				var json = JsonParser.parseReader(reader).getAsJsonObject();

				reader.close();

				try (var writer = new FileWriter(file)) {
					GSON.toJson(json, writer);
				} catch (Exception e) {
					IronJetpacks.LOGGER.error("An error occurred while migrating jetpack json {}", file.getName(), e);
					continue;
				}

				jetpack = Jetpack.fromJson(json);
			} catch (Exception e) {
				IronJetpacks.LOGGER.error("An error occurred while reading jetpack json {}", file.getName(), e);
			} finally {
				IOUtils.closeQuietly(reader);
			}

			if (jetpack != null && !jetpack.disabled) {
				jetpacks.add(jetpack);
			}
		}

		jetpacks.sort(Comparator.comparingInt(Jetpack::getTier));

		for (var jetpack : jetpacks) {
			this.register(jetpack);
		}
	}

	public static JetpackRegistry getInstance() {
		return INSTANCE;
	}
}