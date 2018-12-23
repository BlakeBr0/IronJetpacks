package com.blakebr0.ironjetpacks.config.json;

import java.lang.reflect.Type;

import com.blakebr0.cucumber.lib.ItemPlaceholder;
import com.blakebr0.ironjetpacks.registry.Jetpack;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry;
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
	    gson.registerTypeAdapter(JETPACK.getType(), JETPACK);
	    
	    return gson.create();
	}
	
	public static final SerializerBase<Jetpack> JETPACK = new SerializerBase<Jetpack>() {

		@Override
		public Jetpack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
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
			
			Jetpack jetpack = JetpackRegistry.createJetpack(name, tier, color, armorPoints, enchantability, craftingMaterial).setCreative(creative);
			
			int capacity = obj.get("capacity").getAsInt();
			int usage = obj.get("usage").getAsInt();
			double speedVert = obj.get("speed_vertical").getAsDouble();
			double accelVert = obj.get("accel_vertical").getAsDouble();
			double speedSide = obj.get("speed_sideways").getAsDouble();
			double speedHover = obj.get("speed_hover_descend").getAsDouble();
			double speedHoverSlow = obj.get("speed_hover").getAsDouble();
			double sprintSpeed = obj.get("sprint_speed_multi").getAsDouble();
			double sprintFuel = obj.get("sprint_fuel_multi").getAsDouble();
			
			jetpack.setStats(capacity, usage, speedVert, accelVert, speedSide, speedHover, speedHoverSlow, sprintSpeed, sprintFuel);
			
			return jetpack;
		}
		
		@Override
		public JsonElement serialize(Jetpack src, Type typeOfSrc, JsonSerializationContext context) {
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
		}
		
		@Override
		public Type getType() {
			return Jetpack.class;
		}
	};
}
