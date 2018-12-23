package com.blakebr0.ironjetpacks.item;

import org.apache.commons.lang3.tuple.Pair;

import com.blakebr0.cucumber.registry.ModRegistry;
import com.blakebr0.ironjetpacks.IronJetpacks;
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
		for (Pair<String, ItemJetpack> jetpack : jetpacks.getAllJetpacks()) {
			ItemComponent item = new ItemComponent(jetpack.getLeft(), "cell");
			item.setColor(jetpack.getRight().color());
			jetpack.getRight().getJetpackType().setCellItem(item);
			registry.register(item, jetpack.getLeft() + "_cell");
		}
		
		// Thrusters
		for (Pair<String, ItemJetpack> jetpack : jetpacks.getAllJetpacks()) {
			ItemComponent item = new ItemComponent(jetpack.getLeft(), "thruster");
			item.setColor(jetpack.getRight().color());
			jetpack.getRight().getJetpackType().setThrusterItem(item);
			registry.register(item, jetpack.getLeft() + "_thruster");
		}
		
		// Capacitors
		for (Pair<String, ItemJetpack> jetpack : jetpacks.getAllJetpacks()) {
			ItemComponent item = new ItemComponent(jetpack.getLeft(), "capacitor");
			item.setColor(jetpack.getRight().color());
			jetpack.getRight().getJetpackType().setCapacitorItem(item);
			registry.register(item, jetpack.getLeft() + "_capacitor");
		}
		
		// Jetpacks
		for (Pair<String, ItemJetpack> jetpack : jetpacks.getAllJetpacks()) {
			registry.register(jetpack.getRight(), jetpack.getLeft() + "_jetpack");
			registry.addOre(jetpack.getRight(), "jetpackTier" + jetpack.getRight().getJetpackType().tier);
		}
	}
}
