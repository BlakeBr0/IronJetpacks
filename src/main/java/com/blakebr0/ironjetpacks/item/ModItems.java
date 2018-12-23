package com.blakebr0.ironjetpacks.item;

import com.blakebr0.cucumber.registry.ModRegistry;
import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.registry.Jetpack;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry;

public class ModItems {
	
	public static final ItemBasic STRAP = new ItemBasic("strap");
	public static final ItemBasic BASIC_COIL = new ItemBasic("basic_coil");
	public static final ItemBasic ADVANCED_COIL = new ItemBasic("advanced_coil");
	public static final ItemBasic ELITE_COIL = new ItemBasic("elite_coil");
	public static final ItemBasic ULTIMATE_COIL = new ItemBasic("ultimate_coil");
	
	public static void register() {
		final ModRegistry registry = IronJetpacks.REGISTRY;
		final JetpackRegistry jetpacks = JetpackRegistry.getInstance();
		
		registry.register(STRAP, "strap");
		registry.register(BASIC_COIL, "basic_coil");
		registry.register(ADVANCED_COIL, "advanced_coil");
		registry.register(ELITE_COIL, "elite_coil");
		registry.register(ULTIMATE_COIL, "ultimate_coil");

		// Energy Cells
		for (Jetpack jetpack : jetpacks.getAllJetpacks()) {
			ItemComponent item = new ItemComponent(jetpack.name, "cell");
			item.setColor(jetpack.item.color());
			jetpack.setCellItem(item);
			registry.register(item, jetpack.name + "_cell");
		}
		
		// Thrusters
		for (Jetpack jetpack : jetpacks.getAllJetpacks()) {
			ItemComponent item = new ItemComponent(jetpack.name, "thruster");
			item.setColor(jetpack.item.color());
			jetpack.setThrusterItem(item);
			registry.register(item, jetpack.name + "_thruster");
		}
		
		// Capacitors
		for (Jetpack jetpack : jetpacks.getAllJetpacks()) {
			ItemComponent item = new ItemComponent(jetpack.name, "capacitor");
			item.setColor(jetpack.item.color());
			jetpack.setCapacitorItem(item);
			registry.register(item, jetpack.name + "_capacitor");
		}
		
		// Jetpacks
		for (Jetpack jetpack : jetpacks.getAllJetpacks()) {
			registry.register(jetpack.item, jetpack.name + "_jetpack");
			registry.addOre(jetpack.item, "jetpackTier" + jetpack.tier);
		}
	}
}
