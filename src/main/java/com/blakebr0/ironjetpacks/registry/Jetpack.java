package com.blakebr0.ironjetpacks.registry;

import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.item.ComponentItem;
import com.blakebr0.ironjetpacks.item.JetpackItem;
import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Jetpack {
	public String name;
	public String displayName;
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
	public float toughness;
	public float knockbackResistance;
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
	public double sprintSpeedVert;
	public double sprintFuel;
	
	public Jetpack(String name, int tier, int color, int armorPoints, int enchantability, String craftingMaterialString, float toughness, float knockbackResistance) {
		this.name = name;
		this.displayName = this.makeDisplayName();
		this.tier = tier;
		this.color = color;
		this.armorPoints = armorPoints;
		this.enchantablilty = enchantability;
		this.craftingMaterialString = craftingMaterialString;
		this.item = new JetpackItem(this, p -> p.tab(IronJetpacks.ITEM_GROUP));
		this.toughness = toughness;
		this.knockbackResistance = knockbackResistance;
	}
	
	public Jetpack setStats(int capacity, int usage, double speedVert, double accelVert, double speedSide, double speedHover, double speedHoverSlow, double sprintSpeed, double sprintSpeedVert, double sprintFuel) {
		this.capacity = capacity;
		this.usage = usage;
		this.speedVert = speedVert;
		this.accelVert = accelVert;
		this.speedSide = speedSide;
		this.speedHover = speedHover;
		this.speedHoverSlow = speedHoverSlow;
		this.sprintSpeed = sprintSpeed;
		this.sprintSpeedVert = sprintSpeedVert;
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
					ITag<Item> tag = TagCollectionManager.getInstance().getItems().getTag(new ResourceLocation(parts[1], parts[2]));
					if (tag != null && !tag.getValues().isEmpty())
						this.craftingMaterial = Ingredient.of(tag);
				} else if (parts.length >= 2) {
					Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(parts[0], parts[1]));
					if (item != null)
						this.craftingMaterial = Ingredient.of(item);
				}
			}
		}

		return this.craftingMaterial;
	}

	private String makeDisplayName() {
		String[] parts = this.name.replaceAll(" ", "_").split("_");
		return Arrays.stream(parts).map(StringUtils::capitalize).collect(Collectors.joining(" "));
	}

	public JsonObject toJson() {
		JsonObject json = new JsonObject();

		json.addProperty("name", this.name);
		json.addProperty("disable", this.disabled);
		json.addProperty("tier", this.tier);
		json.addProperty("color", Integer.toHexString(this.color));
		json.addProperty("armorPoints", this.armorPoints);
		json.addProperty("enchantability", this.enchantablilty);
		json.addProperty("craftingMaterial", this.craftingMaterialString);
		json.addProperty("creative", this.creative);
		json.addProperty("rarity", this.rarity.ordinal());
		json.addProperty("toughness", this.toughness);
		json.addProperty("knockbackResistance", this.knockbackResistance);

		json.addProperty("capacity", this.capacity);
		json.addProperty("usage", this.usage);
		json.addProperty("speedVertical", this.speedVert);
		json.addProperty("accelVertical", this.accelVert);
		json.addProperty("speedSideways", this.speedSide);
		json.addProperty("speedHoverDescend", this.speedHover);
		json.addProperty("speedHover", this.speedHoverSlow);
		json.addProperty("sprintSpeedMulti", this.sprintSpeed);
		json.addProperty("sprintSpeedMultiVertical", this.sprintSpeedVert);
		json.addProperty("sprintFuelMulti", this.sprintFuel);

		return json;
	}

	public static Jetpack fromJson(JsonObject json) {
		String name = json.get("name").getAsString();
		boolean disable = json.get("disable").getAsBoolean();
		int tier = json.get("tier").getAsInt();
		int color = Integer.parseInt(json.get("color").getAsString(), 16);
		int armorPoints = json.get("armorPoints").getAsInt();
		int enchantability = json.get("enchantability").getAsInt();
		String craftingMaterialString = json.get("craftingMaterial").getAsString();
		boolean creative = json.get("creative").getAsBoolean();
		Rarity rarity = Rarity.values()[json.get("rarity").getAsInt()];
		float toughness = json.get("toughness").getAsFloat();
		float knockbackResistance = json.get("knockbackResistance").getAsFloat();

		Jetpack jetpack = new Jetpack(name, tier, color, armorPoints, enchantability, craftingMaterialString, toughness, knockbackResistance)
				.setRarity(rarity)
				.setCreative(creative)
				.setDisabled(disable);

		int capacity = json.get("capacity").getAsInt();
		int usage = json.get("usage").getAsInt();
		double speedVert = json.get("speedVertical").getAsDouble();
		double accelVert = json.get("accelVertical").getAsDouble();
		double speedSide = json.get("speedSideways").getAsDouble();
		double speedHover = json.get("speedHoverDescend").getAsDouble();
		double speedHoverSlow = json.get("speedHover").getAsDouble();
		double sprintSpeed = json.get("sprintSpeedMulti").getAsDouble();
		double sprintSpeedVert = json.get("sprintSpeedMultiVertical").getAsDouble();
		double sprintFuel = json.get("sprintFuelMulti").getAsDouble();

		jetpack.setStats(capacity, usage, speedVert, accelVert, speedSide, speedHover, speedHoverSlow, sprintSpeed, sprintSpeedVert, sprintFuel);

		return jetpack;
	}
}
