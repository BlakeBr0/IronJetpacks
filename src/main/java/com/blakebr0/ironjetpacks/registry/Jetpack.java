package com.blakebr0.ironjetpacks.registry;

import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.item.JetpackItem;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.SerializationTags;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

public class Jetpack {
	private static final UUID ATTRIBUTE_ID = UUID.fromString("7FBA2C56-DD5E-4071-8519-D2643E707E40");

	public static final Jetpack UNDEFINED = new Jetpack("undefined", 0, 0xFFF, 0, 0, "null", 0F, 0F);

	private final ResourceLocation id;
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
	public Multimap<Attribute, AttributeModifier> attributeModifiers;

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
		this.id = new ResourceLocation(IronJetpacks.MOD_ID, name);
		this.name = name;
		this.displayName = this.makeDisplayName();
		this.tier = tier;
		this.color = color;
		this.armorPoints = armorPoints;
		this.enchantablilty = enchantability;
		this.craftingMaterialString = craftingMaterialString;
		this.toughness = toughness;
		this.knockbackResistance = knockbackResistance;
		this.attributeModifiers = this.createAttributeModifiers();
	}

	public void setStats(int capacity, int usage, double speedVert, double accelVert, double speedSide, double speedHover, double speedHoverSlow, double sprintSpeed, double sprintSpeedVert, double sprintFuel) {
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
	}

	public ResourceLocation getId() {
		return this.id;
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

	public int getTier() {
		return this.tier;
	}

	public Ingredient getCraftingMaterial() {
		if (this.craftingMaterial == null) {
			this.craftingMaterial = Ingredient.EMPTY;

			if (!this.craftingMaterialString.equalsIgnoreCase("null")) {
				var parts = craftingMaterialString.split(":");
				if (parts.length >= 3 && this.craftingMaterialString.startsWith("tag:")) {
					var tag = SerializationTags.getInstance().getOrEmpty(Registry.ITEM_REGISTRY).getTag(new ResourceLocation(parts[1], parts[2]));
					if (tag != null && !tag.getValues().isEmpty())
						this.craftingMaterial = Ingredient.of(tag);
				} else if (parts.length >= 2) {
					var item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(parts[0], parts[1]));
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

	private ImmutableMultimap<Attribute, AttributeModifier> createAttributeModifiers() {
		ImmutableMultimap.Builder<Attribute, AttributeModifier> modifiers = ImmutableMultimap.builder();

		modifiers.put(Attributes.ARMOR, new AttributeModifier(ATTRIBUTE_ID, "Armor modifier", this.armorPoints, AttributeModifier.Operation.ADDITION));
		modifiers.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(ATTRIBUTE_ID, "Armor toughness", this.toughness, AttributeModifier.Operation.ADDITION));

		if (this.knockbackResistance > 0) {
			modifiers.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(ATTRIBUTE_ID, "Armor knockback resistance", this.knockbackResistance, AttributeModifier.Operation.ADDITION));
		}

		return modifiers.build();
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
		var disable = json.get("disable").getAsBoolean();
		var tier = json.get("tier").getAsInt();
		var color = Integer.parseInt(json.get("color").getAsString(), 16);
		var armorPoints = json.get("armorPoints").getAsInt();
		var enchantability = json.get("enchantability").getAsInt();
		var craftingMaterialString = json.get("craftingMaterial").getAsString();
		var creative = json.get("creative").getAsBoolean();
		var rarity = Rarity.values()[json.get("rarity").getAsInt()];
		var toughness = json.get("toughness").getAsFloat();
		var knockbackResistance = json.get("knockbackResistance").getAsFloat();

		var jetpack = new Jetpack(name, tier, color, armorPoints, enchantability, craftingMaterialString, toughness, knockbackResistance)
				.setRarity(rarity)
				.setCreative(creative)
				.setDisabled(disable);

		var capacity = json.get("capacity").getAsInt();
		var usage = json.get("usage").getAsInt();
		var speedVert = json.get("speedVertical").getAsDouble();
		var accelVert = json.get("accelVertical").getAsDouble();
		var speedSide = json.get("speedSideways").getAsDouble();
		var speedHover = json.get("speedHoverDescend").getAsDouble();
		var speedHoverSlow = json.get("speedHover").getAsDouble();
		var sprintSpeed = json.get("sprintSpeedMulti").getAsDouble();
		var sprintSpeedVert = json.get("sprintSpeedMultiVertical").getAsDouble();
		var sprintFuel = json.get("sprintFuelMulti").getAsDouble();

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
		var name = buffer.readUtf();
		var disabled = buffer.readBoolean();
		var tier = buffer.readVarInt();
		var color = buffer.readVarInt();
		var armorPoints = buffer.readVarInt();
		var enchantability = buffer.readVarInt();
		var craftingMaterialString = buffer.readUtf();
		var creative = buffer.readBoolean();
		var rarity = Rarity.values()[buffer.readVarInt()];
		var toughness = buffer.readFloat();
		var knockbackResistance = buffer.readFloat();

		var jetpack = new Jetpack(name, tier, color, armorPoints, enchantability, craftingMaterialString, toughness, knockbackResistance)
				.setRarity(rarity)
				.setCreative(creative)
				.setDisabled(disabled);

		var capacity = buffer.readVarInt();
		var usage = buffer.readVarInt();
		var speedVert = buffer.readDouble();
		var accelVert = buffer.readDouble();
		var speedSide = buffer.readDouble();
		var speedHover = buffer.readDouble();
		var speedHoverSlow = buffer.readDouble();
		var sprintSpeed = buffer.readDouble();
		var sprintSpeedVert = buffer.readDouble();
		var sprintFuel = buffer.readDouble();

		jetpack.setStats(capacity, usage, speedVert, accelVert, speedSide, speedHover, speedHoverSlow, sprintSpeed, sprintSpeedVert, sprintFuel);

		return jetpack;
	}
}
