package com.blakebr0.ironjetpacks.registry;

import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.config.ModJetpacks;
import com.blakebr0.ironjetpacks.init.ModItems;
import com.blakebr0.ironjetpacks.network.NetworkHandler;
import com.blakebr0.ironjetpacks.network.message.SyncJetpacksMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fmllegacy.network.PacketDistributor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JetpackRegistry implements ResourceManagerReloadListener {
	private static final JetpackRegistry INSTANCE = new JetpackRegistry();
	private final Map<String, Jetpack> jetpacks = new LinkedHashMap<>();
	private final ArrayList<Integer> tiers = new ArrayList<>();
	private int lowestTier = Integer.MAX_VALUE;
	private boolean isErrored = false;

	@Override
	public void onResourceManagerReload(ResourceManager manager) {
		this.jetpacks.clear();
		ModJetpacks.loadJsons();
	}

	@SubscribeEvent
	public void onAddReloadListeners(AddReloadListenerEvent event) {
		event.addListener(this);
	}

	@SubscribeEvent
	public void onDatapackSync(OnDatapackSyncEvent event) {
		var message = new SyncJetpacksMessage(this.getAllJetpacks());
		var player = event.getPlayer();

		if (player != null) {
			NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
		} else {
			NetworkHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), message);
		}
	}

	public void register(Jetpack jetpack) {
		if (this.jetpacks.containsKey(jetpack.name)) {
			this.isErrored = true;
			throw new RuntimeException(String.format("Tried to register multiple jetpacks with the same name: %s", jetpack.name));
		}

		this.jetpacks.put(jetpack.name, jetpack);

		if (jetpack.tier > -1 && !this.tiers.contains(jetpack.tier)) {
			this.tiers.add(jetpack.tier);
			this.tiers.sort(Integer::compareTo);
		}

		if (jetpack.tier > -1 && jetpack.tier < this.lowestTier) {
			this.lowestTier = jetpack.tier;
		}
	}

	public List<Jetpack> getAllJetpacks() {
		return new ArrayList<>(this.jetpacks.values());
	}

	public List<Integer> getAllTiers() {
		return this.tiers;
	}

	public Integer getLowestTier() {
		return this.lowestTier;
	}

	public Jetpack getJetpackByName(String name) {
		return this.jetpacks.get(name);
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

		this.jetpacks.forEach((name, jetpack) -> {
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

		for (Jetpack jetpack : message.getJetpacks()) {
			this.jetpacks.put(jetpack.name, jetpack);
		}

		IronJetpacks.LOGGER.info("Loaded {} singularities from the server", this.jetpacks.size());
	}

	public static JetpackRegistry getInstance() {
		return INSTANCE;
	}
}