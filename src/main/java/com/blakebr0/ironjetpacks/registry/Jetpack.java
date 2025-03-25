package com.blakebr0.ironjetpacks.registry;

import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.item.JetpackItem;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.locale.Language;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.crafting.Ingredient;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Jetpack {
	private static final ResourceLocation ATTRIBUTE_ID = IronJetpacks.resource("armor.jetpack");

	public static final Jetpack UNDEFINED = new Jetpack("undefined", 0, 0xFFF, 0, 0, "null", 0F, 0F).setCurios(false);

	public static final StreamCodec<FriendlyByteBuf, Jetpack> STREAM_CODEC = StreamCodec.of(Jetpack::encode, Jetpack::read);

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
	public boolean curios = true;

	public int capacity;
	public int usage;
	public double speedVert;
	public double accelVert;
	public double speedSide;
	public double speedHoverAscend;
	public double speedHoverDescend;
	public double speedHoverSlow;
	public double sprintSpeed;
	public double sprintSpeedVert;
	public double sprintFuel;

	public Jetpack(String name, int tier, int color, int armorPoints, int enchantability, String craftingMaterialString, float toughness, float knockbackResistance) {
		this.id = IronJetpacks.resource(name);
		this.name = name;
		this.displayName = this.makeDisplayName();
		this.tier = tier;
		this.color = FastColor.ARGB32.color(255, color);
		this.armorPoints = armorPoints;
		this.enchantablilty = enchantability;
		this.craftingMaterialString = craftingMaterialString;
		this.toughness = toughness;
		this.knockbackResistance = knockbackResistance;
	}

	public void setStats(int capacity, int usage, double speedVert, double accelVert, double speedSide, double speedHoverAscend, double speedHoverDescend, double speedHoverSlow, double sprintSpeed, double sprintSpeedVert, double sprintFuel) {
		this.capacity = capacity;
		this.usage = usage;
		this.speedVert = speedVert;
		this.accelVert = accelVert;
		this.speedSide = speedSide;
		this.speedHoverAscend = speedHoverAscend;
		this.speedHoverDescend = speedHoverDescend;
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

	public Jetpack setCurios(boolean curios) {
		this.curios = curios;
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
					var tag = ItemTags.create(ResourceLocation.fromNamespaceAndPath(parts[1], parts[2]));
					this.craftingMaterial = Ingredient.of(tag);
				} else if (parts.length >= 2) {
					BuiltInRegistries.ITEM.getOptional(ResourceLocation.fromNamespaceAndPath(parts[0], parts[1]))
                            .ifPresent(value -> this.craftingMaterial = Ingredient.of(value));
				}
			}
		}

		return this.craftingMaterial;
	}

	public Component getDisplayName() {
		var key = String.format("jetpack.%s.name", this.name.replaceAll(" ", "_"));
		if (Language.getInstance().has(key)) {
			return Component.translatable(key);
		}

		return Component.literal(this.displayName);
	}

	public ItemAttributeModifiers createAttributeModifiers() {
		var modifiers = ItemAttributeModifiers.builder();

		modifiers.add(Attributes.ARMOR, new AttributeModifier(ATTRIBUTE_ID, this.armorPoints, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.CHEST);
		modifiers.add(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(ATTRIBUTE_ID, this.toughness, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.CHEST);

		if (this.knockbackResistance > 0) {
			modifiers.add(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(ATTRIBUTE_ID, this.knockbackResistance, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.CHEST);
		}

		return modifiers.build();
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
		json.addProperty("color", Integer.toHexString(this.color & 0x00FFFFFF));
		json.addProperty("armorPoints", this.armorPoints);
		json.addProperty("enchantability", this.enchantablilty);
		json.addProperty("craftingMaterial", this.craftingMaterialString);
		json.addProperty("creative", this.creative);
		json.addProperty("rarity", this.rarity.ordinal());
		json.addProperty("toughness", this.toughness);
		json.addProperty("knockbackResistance", this.knockbackResistance);
		json.addProperty("curios", this.curios);

		json.addProperty("capacity", this.capacity);
		json.addProperty("usage", this.usage);
		json.addProperty("speedVertical", this.speedVert);
		json.addProperty("accelVertical", this.accelVert);
		json.addProperty("speedSideways", this.speedSide);
		json.addProperty("speedHoverAscend", this.speedHoverAscend);
		json.addProperty("speedHoverDescend", this.speedHoverDescend);
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
		var curios = json.get("curios").getAsBoolean();

		var jetpack = new Jetpack(name, tier, color, armorPoints, enchantability, craftingMaterialString, toughness, knockbackResistance)
				.setRarity(rarity)
				.setCreative(creative)
				.setDisabled(disable)
				.setCurios(curios);

		var capacity = json.get("capacity").getAsInt();
		var usage = json.get("usage").getAsInt();
		var speedVert = json.get("speedVertical").getAsDouble();
		var accelVert = json.get("accelVertical").getAsDouble();
		var speedSide = json.get("speedSideways").getAsDouble();
		var speedHoverAscend = json.get("speedHoverAscend").getAsDouble();
		var speedHoverDescend = json.get("speedHoverDescend").getAsDouble();
		var speedHoverSlow = json.get("speedHover").getAsDouble();
		var sprintSpeed = json.get("sprintSpeedMulti").getAsDouble();
		var sprintSpeedVert = json.get("sprintSpeedMultiVertical").getAsDouble();
		var sprintFuel = json.get("sprintFuelMulti").getAsDouble();

		jetpack.setStats(capacity, usage, speedVert, accelVert, speedSide, speedHoverAscend, speedHoverDescend, speedHoverSlow, sprintSpeed, sprintSpeedVert, sprintFuel);

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
		buffer.writeBoolean(this.curios);

		buffer.writeVarInt(this.capacity);
		buffer.writeVarInt(this.usage);
		buffer.writeDouble(this.speedVert);
		buffer.writeDouble(this.accelVert);
		buffer.writeDouble(this.speedSide);
		buffer.writeDouble(this.speedHoverAscend);
		buffer.writeDouble(this.speedHoverDescend);
		buffer.writeDouble(this.speedHoverSlow);
		buffer.writeDouble(this.sprintSpeed);
		buffer.writeDouble(this.sprintSpeedVert);
		buffer.writeDouble(this.sprintFuel);
	}

	public static void encode(FriendlyByteBuf buffer, Jetpack jetpack) {
		jetpack.write(buffer);
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
		var curios = buffer.readBoolean();

		var jetpack = new Jetpack(name, tier, color, armorPoints, enchantability, craftingMaterialString, toughness, knockbackResistance)
				.setRarity(rarity)
				.setCreative(creative)
				.setDisabled(disabled)
				.setCurios(curios);

		var capacity = buffer.readVarInt();
		var usage = buffer.readVarInt();
		var speedVert = buffer.readDouble();
		var accelVert = buffer.readDouble();
		var speedSide = buffer.readDouble();
		var speedHoverAscend = buffer.readDouble();
		var speedHoverDescend = buffer.readDouble();
		var speedHoverSlow = buffer.readDouble();
		var sprintSpeed = buffer.readDouble();
		var sprintSpeedVert = buffer.readDouble();
		var sprintFuel = buffer.readDouble();

		jetpack.setStats(capacity, usage, speedVert, accelVert, speedSide, speedHoverAscend, speedHoverDescend, speedHoverSlow, sprintSpeed, sprintSpeedVert, sprintFuel);

		return jetpack;
	}
}
