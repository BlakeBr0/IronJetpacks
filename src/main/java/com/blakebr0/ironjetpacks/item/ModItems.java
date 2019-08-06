package com.blakebr0.ironjetpacks.item;

import com.blakebr0.cucumber.item.BaseItem;
import com.blakebr0.ironjetpacks.config.ModJetpacks;
import com.blakebr0.ironjetpacks.registry.Jetpack;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import static com.blakebr0.ironjetpacks.IronJetpacks.ITEM_GROUP;

public class ModItems {
	public static final BaseItem STRAP = new BaseItem(p -> p.group(ITEM_GROUP));
	public static final BaseItem BASIC_COIL = new BaseItem(p -> p.group(ITEM_GROUP));
	public static final BaseItem ADVANCED_COIL = new BaseItem(p -> p.group(ITEM_GROUP));
	public static final BaseItem ELITE_COIL = new BaseItem(p -> p.group(ITEM_GROUP));
	public static final BaseItem ULTIMATE_COIL = new BaseItem(p -> p.group(ITEM_GROUP));

	@SubscribeEvent
	public void onRegisterItems(RegistryEvent.Register<Item> event) {
		ModJetpacks.loadJsons();

		IForgeRegistry<Item> registry = event.getRegistry();
		JetpackRegistry jetpacks = JetpackRegistry.getInstance();
		
		registry.register(STRAP.setRegistryName("strap"));
		registry.register(BASIC_COIL.setRegistryName("basic_coil"));
		registry.register(ADVANCED_COIL.setRegistryName("advanced_coil"));
		registry.register(ELITE_COIL.setRegistryName("elite_coil"));
		registry.register(ULTIMATE_COIL.setRegistryName("ultimate_coil"));

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
}
