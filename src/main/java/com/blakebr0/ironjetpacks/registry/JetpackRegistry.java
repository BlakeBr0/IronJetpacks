package com.blakebr0.ironjetpacks.registry;

import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.init.ModItems;
import com.blakebr0.ironjetpacks.lib.ModJetpacks;
import com.blakebr0.ironjetpacks.network.NetworkHandler;
import com.blakebr0.ironjetpacks.network.message.SyncJetpacksMessage;
import com.google.common.base.Stopwatch;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.PacketDistributor;
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
	private final Map<ResourceLocation, Jetpack> jetpacks = new LinkedHashMap<>();
	private final ArrayList<Integer> tiers = new ArrayList<>();
	private int lowestTier = Integer.MAX_VALUE;
	private boolean isErrored = false;

	@SubscribeEvent
	public void onDatapackSync(OnDatapackSyncEvent event) {
		var message = new SyncJetpacksMessage(this.getJetpacks());
		var player = event.getPlayer();

		if (player != null) {
			NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
		} else {
			NetworkHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), message);
		}
	}

	public void onResourceManagerReload(ResourceManager manager) {
		this.loadJetpacks();
	}

	public void register(Jetpack jetpack) {
		if (this.jetpacks.containsKey(jetpack.getId())) {
			this.isErrored = true;
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

	public Jetpack getJetpackById(ResourceLocation id) {
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

	public boolean isErrored() {
		return this.isErrored;
	}

	public void writeToBuffer(FriendlyByteBuf buffer) {
		buffer.writeVarInt(this.jetpacks.size());

		this.jetpacks.forEach((id, jetpack) -> {
			jetpack.write(buffer);
		});
	}

	public List<Jetpack> readFromBuffer(FriendlyByteBuf buffer) {
		List<Jetpack> jetpacks = new ArrayList<>();

		int size = buffer.readVarInt();

		for (int i = 0; i < size; i++) {
			Jetpack jetpack = Jetpack.read(buffer);

			jetpacks.add(jetpack);
		}

		return jetpacks;
	}

	public void loadJetpacks(SyncJetpacksMessage message) {
		this.jetpacks.clear();

		for (var jetpack : message.getJetpacks()) {
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

	private void loadJetpacks() {
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

				var parser = new JsonParser();
				var json = parser.parse(reader).getAsJsonObject();

				reader.close();

				if (handleMigrations(json)) {
					try (var writer = new FileWriter(file)) {
						GSON.toJson(json, writer);
					} catch (Exception e) {
						IronJetpacks.LOGGER.error("An error occurred while migrating jetpack json {}", file.getName(), e);
						continue;
					}
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

		// add hover ascend speed
		if (!json.has("speedHoverAscend")) {
			if (json.has("speedHoverDescend")) {
				json.addProperty("speedHoverAscend", json.get("speedHoverDescend").getAsDouble());
			} else {
				json.addProperty("speedHoverAscend", 0.25D);
			}

			changed = true;
		}

		return changed;
	}
}