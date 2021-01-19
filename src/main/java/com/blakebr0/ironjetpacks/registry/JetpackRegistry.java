package com.blakebr0.ironjetpacks.registry;

import com.blakebr0.ironjetpacks.init.ModItems;
import com.blakebr0.ironjetpacks.item.JetpackItem;
import net.minecraft.item.Item;

import java.util.ArrayList;

public class JetpackRegistry {
	private static final JetpackRegistry INSTANCE = new JetpackRegistry();
	private final ArrayList<Jetpack> jetpacks = new ArrayList<>();
	private final ArrayList<Integer> tiers = new ArrayList<>();
	private int lowestTier = Integer.MAX_VALUE;
	private boolean isErrored = false;
	
	public static JetpackRegistry getInstance() {
		return INSTANCE;
	}
	
	public static Jetpack createJetpack(String name, int tier, int color, int armorPoints, int enchantability, String craftingMaterialString) {
		return new Jetpack(name, tier, color, armorPoints, enchantability, craftingMaterialString);
	}
	
	public void register(Jetpack jetpack) {
		if (this.jetpacks.stream().anyMatch(j -> j.name.equals(jetpack.name))) {
			this.isErrored = true;
			throw new RuntimeException(String.format("Tried to register multiple jetpacks with the same name: %s", jetpack.name));
		}

		this.jetpacks.add(jetpack);
		
		if (jetpack.tier > -1 && !this.tiers.contains(jetpack.tier)) {
			this.tiers.add(jetpack.tier);
			this.tiers.sort(Integer::compareTo);
		}
		
		if (jetpack.tier > -1 && jetpack.tier < this.lowestTier) {
			this.lowestTier = jetpack.tier;
		}
	}
	
	public ArrayList<Jetpack> getAllJetpacks() {
		return this.jetpacks;
	}
	
	public ArrayList<Integer> getAllTiers() {
		return this.tiers;
	}
	
	public Integer getLowestTier() {
		return this.lowestTier;
	}
	
	public JetpackItem getJetpackForName(String name) {
		Jetpack jetpack = this.jetpacks.stream().filter(j -> j.name.equals(name)).findFirst().orElse(null);
		return jetpack == null ? null : jetpack.item;
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