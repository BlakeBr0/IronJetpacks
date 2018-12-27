package com.blakebr0.ironjetpacks.registry;

import com.blakebr0.cucumber.lib.ItemPlaceholder;
import com.blakebr0.ironjetpacks.item.ItemComponent;
import com.blakebr0.ironjetpacks.item.ItemJetpack;

import net.minecraft.item.EnumRarity;

public class Jetpack {
	
	public String name;
	public int tier;
	public int color;
	public int armorPoints;
	public int enchantablilty;
	public ItemPlaceholder craftingMaterial;
	public ItemJetpack item;
	public boolean creative = false;
	public boolean disabled = false;
	public boolean forceRecipe = false;
	public EnumRarity rarity = EnumRarity.COMMON;
	public ItemComponent cell;
	public ItemComponent thruster;
	public ItemComponent capacitor;
	public int capacity;
	public int usage;
	public double speedVert;
	public double accelVert;
	public double speedSide;
	public double speedHover;
	public double speedHoverSlow;
	public double sprintSpeed;
	public double sprintFuel;
	
	public Jetpack(String name, int tier, int color, int armorPoints, int enchantability, ItemPlaceholder craftingMaterial) {
		this.name = name;
		this.tier = tier;
		this.color = color;
		this.armorPoints = armorPoints;
		this.enchantablilty = enchantability;
		this.craftingMaterial = craftingMaterial;
		this.item = new ItemJetpack(this);
	}
	
	public Jetpack setStats(int capacity, int usage, double speedVert, double accelVert, double speedSide, double speedHover, double speedHoverSlow, double sprintSpeed, double sprintFuel) {
		this.capacity = capacity;
		this.usage = usage;
		this.speedVert = speedVert;
		this.accelVert = accelVert;
		this.speedSide = speedSide;
		this.speedHover = speedHover;
		this.speedHoverSlow = speedHoverSlow;
		this.sprintSpeed = sprintSpeed;
		this.sprintFuel = sprintFuel;
		
		return this;
	}
	
	public Jetpack setCreative() {
		this.creative = true;
		this.tier = -1;
		this.rarity = EnumRarity.EPIC;
		
		return this;
	}
	
	public Jetpack setCreative(boolean set) {
		if (set) this.setCreative();
		return this;
	}
	
	public Jetpack setDisabled() {
		this.disabled = true;
		return this;
	}
	
	public Jetpack setDisabled(boolean set) {
		if (set) this.setDisabled();
		return this;
	}
	
	public Jetpack setForceRecipe() {
		this.forceRecipe = true;
		return this;
	}
	
	public Jetpack setForceRecipe(boolean set) {
		if (set) this.setForceRecipe();
		return this;
	}
	
	public Jetpack setRarity(EnumRarity rarity) {
		this.rarity = rarity;
		return this;
	}
	
	public Jetpack setCellItem(ItemComponent item) {
		this.cell = item;
		return this;
	}
	
	public Jetpack setThrusterItem(ItemComponent item) {
		this.thruster = item;
		return this;
	}
	
	public Jetpack setCapacitorItem(ItemComponent item) {
		this.capacitor = item;
		return this;
	}
	
	public int getTier() {
		return this.tier;
	}
}
