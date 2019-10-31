package com.blakebr0.ironjetpacks.item;

import com.blakebr0.cucumber.item.BaseItem;
import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.config.ModJetpacks;
import com.blakebr0.ironjetpacks.registry.Jetpack;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.blakebr0.ironjetpacks.IronJetpacks.ITEM_GROUP;

public class ModItems {
	public static final List<Supplier<? extends Item>> ENTRIES = new ArrayList<>();

	public static final RegistryObject<BaseItem> STRAP = register("strap");
	public static final RegistryObject<BaseItem> BASIC_COIL = register("basic_coil");
	public static final RegistryObject<BaseItem> ADVANCED_COIL = register("advanced_coil");
	public static final RegistryObject<BaseItem> ELITE_COIL = register("elite_coil");
	public static final RegistryObject<BaseItem> ULTIMATE_COIL = register("ultimate_coil");

	@SubscribeEvent
	public void onRegisterItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();
		JetpackRegistry jetpacks = JetpackRegistry.getInstance();

		ENTRIES.stream().map(Supplier::get).forEach(registry::register);

		ModJetpacks.loadJsons();

		// Energy Cells
		for (Jetpack jetpack : jetpacks.getAllJetpacks()) {
			ComponentItem item = new ComponentItem(jetpack, "cell", p -> p.group(ITEM_GROUP));
			jetpack.setCellItem(item);
			registry.register(item.setRegistryName(jetpack.name + "_cell"));
		}
		
		// Thrusters
		for (Jetpack jetpack : jetpacks.getAllJetpacks()) {
			ComponentItem item = new ComponentItem(jetpack, "thruster", p -> p.group(ITEM_GROUP));
			jetpack.setThrusterItem(item);
			registry.register(item.setRegistryName(jetpack.name + "_thruster"));
		}
		
		// Capacitors
		for (Jetpack jetpack : jetpacks.getAllJetpacks()) {
			ComponentItem item = new ComponentItem(jetpack, "capacitor", p -> p.group(ITEM_GROUP));
			jetpack.setCapacitorItem(item);
			registry.register(item.setRegistryName(jetpack.name + "_capacitor"));
		}
		
		// Jetpacks
		for (Jetpack jetpack : jetpacks.getAllJetpacks()) {
			registry.register(jetpack.item.setRegistryName(jetpack.name + "_jetpack"));
		}
	}

	private static <T extends Item> RegistryObject<T> register(String name) {
		return register(name, () -> new BaseItem(p -> p.group(ITEM_GROUP)));
	}

	private static <T extends Item> RegistryObject<T> register(String name, Supplier<? extends Item> item) {
		ResourceLocation loc = new ResourceLocation(IronJetpacks.MOD_ID, name);
		ENTRIES.add(() -> item.get().setRegistryName(loc));
		return RegistryObject.of(loc, ForgeRegistries.ITEMS);
	}
}
