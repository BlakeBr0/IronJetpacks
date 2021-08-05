package com.blakebr0.ironjetpacks.init;

import com.blakebr0.cucumber.item.BaseItem;
import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.config.ModJetpacks;
import com.blakebr0.ironjetpacks.item.ComponentItem;
import com.blakebr0.ironjetpacks.registry.Jetpack;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import static com.blakebr0.ironjetpacks.IronJetpacks.ITEM_GROUP;

public final class ModItems {
	public static final Map<RegistryObject<Item>, Supplier<Item>> ENTRIES = new LinkedHashMap<>();

	public static final RegistryObject<Item> STRAP = register("strap");
	public static final RegistryObject<Item> BASIC_COIL = register("basic_coil");
	public static final RegistryObject<Item> ADVANCED_COIL = register("advanced_coil");
	public static final RegistryObject<Item> ELITE_COIL = register("elite_coil");
	public static final RegistryObject<Item> ULTIMATE_COIL = register("ultimate_coil");

	@SubscribeEvent
	public void onRegisterItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();
		JetpackRegistry jetpacks = JetpackRegistry.getInstance();

		ENTRIES.forEach((reg, item) -> {
			registry.register(item.get());
			reg.updateReference(registry);
		});

		ModJetpacks.loadJsons();

		// Energy Cells
		for (Jetpack jetpack : jetpacks.getAllJetpacks()) {
			ComponentItem item = new ComponentItem(jetpack, "cell", p -> p.tab(ITEM_GROUP));
			jetpack.setCellItem(item);
			registry.register(item.setRegistryName(jetpack.name + "_cell"));
		}
		
		// Thrusters
		for (Jetpack jetpack : jetpacks.getAllJetpacks()) {
			ComponentItem item = new ComponentItem(jetpack, "thruster", p -> p.tab(ITEM_GROUP));
			jetpack.setThrusterItem(item);
			registry.register(item.setRegistryName(jetpack.name + "_thruster"));
		}
		
		// Capacitors
		for (Jetpack jetpack : jetpacks.getAllJetpacks()) {
			ComponentItem item = new ComponentItem(jetpack, "capacitor", p -> p.tab(ITEM_GROUP));
			jetpack.setCapacitorItem(item);
			registry.register(item.setRegistryName(jetpack.name + "_capacitor"));
		}
		
		// Jetpacks
		for (Jetpack jetpack : jetpacks.getAllJetpacks()) {
			registry.register(jetpack.item.setRegistryName(jetpack.name + "_jetpack"));
		}
	}

	private static RegistryObject<Item> register(String name) {
		return register(name, () -> new BaseItem(p -> p.tab(ITEM_GROUP)));
	}

	private static RegistryObject<Item> register(String name, Supplier<? extends Item> item) {
		ResourceLocation loc = new ResourceLocation(IronJetpacks.MOD_ID, name);
		RegistryObject<Item> reg = RegistryObject.of(loc, ForgeRegistries.ITEMS);
		ENTRIES.put(reg, () -> item.get().setRegistryName(loc));
		return reg;
	}
}
