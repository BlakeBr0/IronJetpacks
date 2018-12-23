package com.blakebr0.ironjetpacks.registry;

import java.util.ArrayList;

import com.blakebr0.cucumber.lib.ItemPlaceholder;
import com.blakebr0.ironjetpacks.item.ItemJetpack;
import com.blakebr0.ironjetpacks.item.ModItems;

import net.minecraft.item.Item;

public class JetpackRegistry {

	private static final JetpackRegistry INSTANCE = new JetpackRegistry();
	private ArrayList<Jetpack> jetpacks = new ArrayList<>();
	private ArrayList<Integer> tiers = new ArrayList<>();
	private int lowestTier = Integer.MAX_VALUE;
	
	public static JetpackRegistry getInstance() {
		return INSTANCE;
	}
	
	public static Jetpack createJetpack(String name, int tier, int color, int armorPoints, int enchantability, ItemPlaceholder craftingMaterial) {
		return new Jetpack(name, tier, color, armorPoints, enchantability, craftingMaterial);
	}
	
	public void register(Jetpack jetpack) {
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
	
	public ItemJetpack getJetpackForName(String name) {		
		return this.jetpacks.stream().filter(j -> j.name.equals(name)).findFirst().orElse(null).item;
	}
	
	public Item getCoilForTier(int tier) {
		return this.tiers.indexOf(tier) >= this.tiers.size() / 2 ? ModItems.ADVANCED_COIL : ModItems.BASIC_COIL;
	}
}