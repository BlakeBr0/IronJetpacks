package com.blakebr0.ironjetpacks.config.json;

import java.lang.reflect.Type;

import org.apache.commons.lang3.tuple.Pair;

import com.blakebr0.cucumber.lib.ItemPlaceholder;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry.Jetpack;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry.JetpackEntry;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry.JetpackType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class Serializers {
	
	public static Gson initGson() {
	    GsonBuilder gson = new GsonBuilder();
	    gson.setPrettyPrinting();
	    gson.serializeNulls();
	    gson.disableHtmlEscaping();
	    for (SerializerBase<?> s : ALL_SERIALIZERS) {
	    	gson.registerTypeAdapter(s.getType(), s);
	    }
	    return gson.create();
	}
	
	public static final SerializerBase<Jetpack> JETPACK = new SerializerBase<Jetpack>() {

		@Override
		public Jetpack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			JsonObject obj = json.getAsJsonObject();
			JetpackType type = context.deserialize(obj.get("jetpack_type"), JetpackType.class);
			JetpackEntry info = context.deserialize(obj.get("jetpack_info"), JetpackEntry.class);
			return JetpackRegistry.createJetpack(type, info);
		}
		
		@Override
		public JsonElement serialize(Jetpack src, Type typeOfSrc, JsonSerializationContext context) {
			JsonObject obj = new JsonObject();
			obj.add("jetpack_type", context.serialize(src.type));
			obj.add("jetpack_info", context.serialize(src.info));
			return obj;
		}
		
		@Override
		public Type getType() {
			return Jetpack.class;
		}
	};
	
	public static final SerializerBase<JetpackType> JETPACK_TYPE = new SerializerBase<JetpackType>() {
		
		@Override
		public JetpackType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			JsonObject obj = json.getAsJsonObject();
			String name = obj.get("name").getAsString();
			boolean disable = obj.get("disable").getAsBoolean();
			int tier = obj.get("tier").getAsInt();
			int color = obj.get("color").getAsInt();
			int armorPoints = obj.get("armor_points").getAsInt();
			int enchantability = obj.get("enchantability").getAsInt();
			String craftingItem = obj.get("crafting_material").getAsString();
			ItemPlaceholder craftingMaterial = null;
			if (!craftingItem.equalsIgnoreCase("null")) {
				if (craftingItem.startsWith("ore:")) {
					craftingMaterial = ItemPlaceholder.of(craftingItem.substring(4));
				} else {
					String[] parts = craftingItem.split(":");
					Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(parts[0], parts[1]));
					craftingMaterial = ItemPlaceholder.of(new ItemStack(item, 1, Integer.valueOf(parts[2])));
				}
			}
			boolean forceRecipes = obj.get("force_recipes").getAsBoolean();
			boolean creative = false;
			if (obj.has("creative")) {
				creative = obj.get("creative").getAsBoolean();
			}
			return JetpackRegistry.createJetpackType(name, tier, color, armorPoints, enchantability, craftingMaterial).setDisabled(disable).setForceRecipe(forceRecipes).setCreative(creative);
		};
		
		@Override
		public JsonElement serialize(JetpackType src, Type typeOfSrc, JsonSerializationContext context) {
			JsonObject obj = new JsonObject();
			obj.addProperty("name", src.name);
			obj.addProperty("disable", src.disabled);
			obj.addProperty("tier", src.tier);
			obj.addProperty("color", src.color);
			obj.addProperty("armor_points", src.armorPoints);
			obj.addProperty("enchantability", src.enchantablilty);
			String mat = src.craftingMaterial == null ? "null" 
					: !src.craftingMaterial.getOreName().isEmpty() ? "ore:" + src.craftingMaterial.getOreName()
					: src.craftingMaterial.getStack().getItem().getRegistryName().toString() + ":" + src.craftingMaterial.getStack().getMetadata();
			obj.addProperty("crafting_material", mat);
			obj.addProperty("force_recipes", false);
			if (src.creative) {
				obj.addProperty("creative", true);
			}
			return obj;
		};
		
		@Override
		public Type getType() {
			return JetpackType.class;
		}
	};
	
	public static final SerializerBase<JetpackEntry> JETPACK_INFO = new SerializerBase<JetpackEntry>() {

		@Override
		public JetpackEntry deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			JsonObject obj = json.getAsJsonObject();
			int capacity = obj.get("capacity").getAsInt();
			int usage = obj.get("usage").getAsInt();
			double speedVert = obj.get("speed_vertical").getAsDouble();
			double accelVert = obj.get("accel_vertical").getAsDouble();
			double speedSide = obj.get("speed_sideways").getAsDouble();
			double speedHover = obj.get("speed_hover_descend").getAsDouble();
			double speedHoverSlow = obj.get("speed_hover").getAsDouble();
			double sprintSpeed = obj.get("sprint_speed_multi").getAsDouble();
			double sprintFuel = obj.get("sprint_fuel_multi").getAsDouble();
			return JetpackRegistry.createJetpackInfo(capacity, usage, speedVert, accelVert, speedSide, speedHover, speedHoverSlow, sprintSpeed, sprintFuel);
		};
		
		@Override
		public JsonElement serialize(JetpackEntry src, Type typeOfSrc, JsonSerializationContext context) {
			JsonObject obj = new JsonObject();
			obj.addProperty("capacity", src.capacity);
			obj.addProperty("usage", src.usage);
			obj.addProperty("speed_vertical", src.speedVert);
			obj.addProperty("accel_vertical", src.accelVert);
			obj.addProperty("speed_sideways", src.speedSide);
			obj.addProperty("speed_hover_descend", src.speedHover);
			obj.addProperty("speed_hover", src.speedHoverSlow);
			obj.addProperty("sprint_speed_multi", src.sprintSpeed);
			obj.addProperty("sprint_fuel_multi", src.sprintFuel);
			return obj;
		};
		
		@Override
		public Type getType() {
			return JetpackEntry.class;
		}
	};
	
	public static final SerializerBase<?>[] ALL_SERIALIZERS = new SerializerBase[] {
			JETPACK,
			JETPACK_TYPE,
			JETPACK_INFO
	};
}
