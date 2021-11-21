package com.blakebr0.ironjetpacks.registry;

import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.item.ComponentItem;
import com.blakebr0.ironjetpacks.item.JetpackItem;
import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.SerializationTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.Ingredient;
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
		this.item = new JetpackItem(name, p -> p.tab(IronJetpacks.CREATIVE_TAB));
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
					Tag<Item> tag = SerializationTags.getInstance().getOrEmpty(Registry.ITEM_REGISTRY).getTag(new ResourceLocation(parts[1], parts[2]));
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
		var parts = this.name.replaceAll(" ", "_").split("_");
		return Arrays.stream(parts).map(StringUtils::capitalize).collect(Collectors.joining(" "));
	}

	public JsonObject toJson() {
		var json = new JsonObject();

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
		var name = json.get("name").getAsString();
		boolean disable = json.get("disable").getAsBoolean();
		int tier = json.get("tier").getAsInt();
		int color = Integer.parseInt(json.get("color").getAsString(), 16);
		int armorPoints = json.get("armorPoints").getAsInt();
		int enchantability = json.get("enchantability").getAsInt();
		var craftingMaterialString = json.get("craftingMaterial").getAsString();
		boolean creative = json.get("creative").getAsBoolean();
		var rarity = Rarity.values()[json.get("rarity").getAsInt()];
		float toughness = json.get("toughness").getAsFloat();
		float knockbackResistance = json.get("knockbackResistance").getAsFloat();

		var jetpack = new Jetpack(name, tier, color, armorPoints, enchantability, craftingMaterialString, toughness, knockbackResistance)
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

	public void write(FriendlyByteBuf buffer) {
		buffer.writeUtf(this.name);
		buffer.writeBoolean(this.disabled);
		buffer.writeVarInt(this.tier);
		buffer.writeVarInt(this.color);
		buffer.writeVarInt(this.armorPoints);
		buffer.writeVarInt(this.enchantablilty);
		buffer.writeUtf(this.craftingMaterialString);
		buffer.writeBoolean(this.creative);
		buffer.writeVarInt(this.rarity.ordinal());
		buffer.writeFloat(this.toughness);
		buffer.writeFloat(this.knockbackResistance);

		buffer.writeVarInt(this.capacity);
		buffer.writeVarInt(this.usage);
		buffer.writeDouble(this.speedVert);
		buffer.writeDouble(this.accelVert);
		buffer.writeDouble(this.speedSide);
		buffer.writeDouble(this.speedHover);
		buffer.writeDouble(this.speedHoverSlow);
		buffer.writeDouble(this.sprintSpeed);
		buffer.writeDouble(this.sprintSpeedVert);
		buffer.writeDouble(this.sprintFuel);
	}

	public static Jetpack read(FriendlyByteBuf buffer) {
		String name = buffer.readUtf();
		boolean disabled = buffer.readBoolean();
		int tier = buffer.readVarInt();
		int color = buffer.readVarInt();
		int armorPoints = buffer.readVarInt();
		int enchantability = buffer.readVarInt();
		String craftingMaterialString = buffer.readUtf();
		boolean creative = buffer.readBoolean();
		Rarity rarity = Rarity.values()[buffer.readVarInt()];
		float toughness = buffer.readFloat();
		float knockbackResistance = buffer.readFloat();

		Jetpack jetpack = new Jetpack(name, tier, color, armorPoints, enchantability, craftingMaterialString, toughness, knockbackResistance)
				.setRarity(rarity)
				.setCreative(creative)
				.setDisabled(disabled);

		int capacity = buffer.readVarInt();
		int usage = buffer.readVarInt();
		double speedVert = buffer.readDouble();
		double accelVert = buffer.readDouble();
		double speedSide = buffer.readDouble();
		double speedHover = buffer.readDouble();
		double speedHoverSlow = buffer.readDouble();
		double sprintSpeed = buffer.readDouble();
		double sprintSpeedVert = buffer.readDouble();
		double sprintFuel = buffer.readDouble();

		jetpack.setStats(capacity, usage, speedVert, accelVert, speedSide, speedHover, speedHoverSlow, sprintSpeed, sprintSpeedVert, sprintFuel);

		return jetpack;
	}
}
