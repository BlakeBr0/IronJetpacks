package com.blakebr0.ironjetpacks.registry;

import java.util.ArrayList;

import org.apache.commons.lang3.tuple.Pair;

import com.blakebr0.cucumber.lib.ItemPlaceholder;
import com.blakebr0.ironjetpacks.item.ItemComponent;
import com.blakebr0.ironjetpacks.item.ItemJetpack;
import com.blakebr0.ironjetpacks.item.ModItems;

import net.minecraft.item.Item;

public class JetpackRegistry {

	private static final JetpackRegistry INSTANCE = new JetpackRegistry();
	private ArrayList<Pair<String, ItemJetpack>> jetpacks = new ArrayList<>();
	private ArrayList<Integer> tiers = new ArrayList<>();
	private int lowestTier = Integer.MAX_VALUE;
	
	public static JetpackRegistry getInstance() {
		return INSTANCE;
	}
	
	public static Jetpack createJetpack(JetpackType type, JetpackEntry info) {
		return new Jetpack(type, info);
	}
	
	public static JetpackType createJetpackType(String name, int tier, int color, int armorPoints, int enchantability, ItemPlaceholder craftingMaterial) {
		return new JetpackType(name, tier, color, armorPoints, enchantability, craftingMaterial);
	}
	
	public static JetpackEntry createJetpackInfo(int capacity, int usage, double speedVert, double accelVert, double speedSide, double speedHover, double speedHoverSlow, double sprintSpeed, double sprintFuel) {
		return new JetpackEntry(capacity, usage, speedVert, accelVert, speedSide, speedHover, speedHoverSlow, sprintSpeed, sprintFuel);
	}
	
	public void register(JetpackType type, JetpackEntry info) {
		this.jetpacks.add(Pair.of(type.name, new ItemJetpack(type, info)));
		
		if (type.tier > -1 && !tiers.contains(type.tier)) {
			this.tiers.add(type.tier);
			this.tiers.sort(Integer::compareTo);
		}
		
		if (type.tier > -1 && type.tier < this.lowestTier) {
			this.lowestTier = type.tier;
		}
	}
	
	public ArrayList<Pair<String, ItemJetpack>> getAllJetpacks() {
		return this.jetpacks;
	}
	
	public ArrayList<Integer> getAllTiers() {
		return this.tiers;
	}
	
	public Integer getLowestTier() {
		return this.lowestTier;
	}
	
	public ItemJetpack getJetpackForName(String name) {
		for (Pair<String, ItemJetpack> j : this.jetpacks) {
			if (j.getLeft().equals(name)) {
				return j.getRight();
			}
		}
		return null;
	}
	
	public Item getCoilForTier(int tier) {
		return this.tiers.indexOf(tier) >= this.tiers.size() / 2 ? ModItems.ADVANCED_COIL : ModItems.BASIC_COIL;
	}
	
	public static class Jetpack {
		
		public JetpackType type;
		public JetpackEntry info;
		
		private Jetpack(JetpackType type, JetpackEntry info) {
			this.type = type;
			this.info = info;
		}
		
		public int getTier() {
			return type.tier;
		}
	}
	
	public static class JetpackType {
		
		public String name;
		public int tier;
		public int color;
		public int armorPoints;
		public int enchantablilty;
		public ItemPlaceholder craftingMaterial;		
		
		public boolean creative = false;
		public boolean disabled = false;
		public boolean forceRecipe = false;
		public ItemComponent cell;
		public ItemComponent thruster;
		public ItemComponent capacitor;

		private JetpackType(String name, int tier, int color, int armorPoints, int enchantability, ItemPlaceholder craftingMaterial) {
			this.name = name;
			this.tier = tier;
			this.color = color;
			this.armorPoints = armorPoints;
			this.enchantablilty = enchantability;
			this.craftingMaterial = craftingMaterial;
		}
		
		public JetpackType setCreative() {
			this.creative = true;
			this.tier = -1;
			return this;
		}
		
		public JetpackType setCreative(boolean set) {
			if (set) {
				this.setCreative();
			}
			return this;
		}
		
		public JetpackType setDisabled() {
			this.disabled = true;
			return this;
		}
		
		public JetpackType setDisabled(boolean set) {
			if (set) {
				this.setDisabled();
			}
			return this;
		}
		
		public JetpackType setForceRecipe() {
			this.forceRecipe = true;
			return this;
		}
		
		public JetpackType setForceRecipe(boolean set) {
			if (set) {
				this.setForceRecipe();
			}
			return this;
		}
		
		public JetpackType setCellItem(ItemComponent item) {
			this.cell = item;
			return this;
		}
		
		public JetpackType setThrusterItem(ItemComponent item) {
			this.thruster = item;
			return this;
		}
		
		public JetpackType setCapacitorItem(ItemComponent item) {
			this.capacitor = item;
			return this;
		}
	}
	
	public static class JetpackEntry {
		
		public int capacity;
		public int usage;
		public double speedVert;
		public double accelVert;
		public double speedSide;
		public double speedHover;
		public double speedHoverSlow;
		public double sprintSpeed;
		public double sprintFuel;
		
		private JetpackEntry(int capacity, int usage, double speedVert, double accelVert, double speedSide, double speedHover, double speedHoverSlow, double sprintSpeed, double sprintFuel) {
			this.capacity = capacity;
			this.usage = usage;
			this.speedVert = speedVert;
			this.accelVert = accelVert;
			this.speedSide = speedSide;
			this.speedHover = speedHover;
			this.speedHoverSlow = speedHoverSlow;
			this.sprintSpeed = sprintSpeed;
			this.sprintFuel = sprintFuel;
		}
	}
}