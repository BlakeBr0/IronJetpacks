package com.blakebr0.ironjetpacks.registry;

import com.blakebr0.ironjetpacks.init.ModItems;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JetpackRegistry {
	private static final JetpackRegistry INSTANCE = new JetpackRegistry();
	private final Map<String, Jetpack> jetpacks = new LinkedHashMap<>();
	private final ArrayList<Integer> tiers = new ArrayList<>();
	private int lowestTier = Integer.MAX_VALUE;
	private boolean isErrored = false;
	
	public static JetpackRegistry getInstance() {
		return INSTANCE;
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
	
	public ArrayList<Integer> getAllTiers() {
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
}