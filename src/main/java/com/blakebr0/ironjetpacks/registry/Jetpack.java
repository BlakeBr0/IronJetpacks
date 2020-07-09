package com.blakebr0.ironjetpacks.registry;

import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.item.ComponentItem;
import com.blakebr0.ironjetpacks.item.JetpackItem;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class Jetpack {
	public String name;
	public int tier;
	public int color;
	public int armorPoints;
	public int enchantablilty;
	public String craftingMaterialString;
	private Ingredient craftingMaterial;
	public JetpackItem item;
	public boolean creative = false;
	public boolean disabled = false;
	public Rarity rarity = Rarity.COMMON;
	public ComponentItem cell;
	public ComponentItem thruster;
	public ComponentItem capacitor;
	public int capacity;
	public int usage;
	public double speedVert;
	public double accelVert;
	public double speedSide;
	public double speedHover;
	public double speedHoverSlow;
	public double sprintSpeed;
	public double sprintFuel;
	
	public Jetpack(String name, int tier, int color, int armorPoints, int enchantability, String craftingMaterialString) {
		this.name = name;
		this.tier = tier;
		this.color = color;
		this.armorPoints = armorPoints;
		this.enchantablilty = enchantability;
		this.craftingMaterialString = craftingMaterialString;
		this.item = new JetpackItem(this, p -> p.group(IronJetpacks.ITEM_GROUP));
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
		this.rarity = Rarity.EPIC;
		
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
	
	public Jetpack setRarity(Rarity rarity) {
		this.rarity = rarity;
		return this;
	}
	
	public Jetpack setCellItem(ComponentItem item) {
		this.cell = item;
		return this;
	}
	
	public Jetpack setThrusterItem(ComponentItem item) {
		this.thruster = item;
		return this;
	}
	
	public Jetpack setCapacitorItem(ComponentItem item) {
		this.capacitor = item;
		return this;
	}
	
	public int getTier() {
		return this.tier;
	}

	public Ingredient getCraftingMaterial() {
		if (this.craftingMaterial == null) {
			this.craftingMaterial = Ingredient.EMPTY;
			if (!this.craftingMaterialString.equalsIgnoreCase("null")) {
				String[] parts = craftingMaterialString.split(":");
				if (parts.length >= 3 && this.craftingMaterialString.startsWith("tag:")) {
					ITag<Item> tag = TagCollectionManager.func_232928_e_().func_232925_b_().get(new ResourceLocation(parts[1], parts[2]));
					if (tag != null && !tag.getAllElements().isEmpty())
						this.craftingMaterial = Ingredient.fromTag(tag);
				} else if (parts.length >= 2) {
					Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(parts[0], parts[1]));
					if (item != null)
						this.craftingMaterial = Ingredient.fromItems(item);
				}
			}
		}

		return this.craftingMaterial;
	}
}
