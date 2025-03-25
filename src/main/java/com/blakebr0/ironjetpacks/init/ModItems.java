package com.blakebr0.ironjetpacks.init;

import com.blakebr0.cucumber.item.BaseItem;
import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.item.ComponentItem;
import com.blakebr0.ironjetpacks.item.JetpackItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(Registries.ITEM, IronJetpacks.MOD_ID);

	public static final DeferredHolder<Item, Item> STRAP = REGISTRY.register("strap", () -> new BaseItem());
	public static final DeferredHolder<Item, Item> BASIC_COIL = REGISTRY.register("basic_coil", () -> new BaseItem());
	public static final DeferredHolder<Item, Item> ADVANCED_COIL = REGISTRY.register("advanced_coil", () -> new BaseItem());
	public static final DeferredHolder<Item, Item> ELITE_COIL = REGISTRY.register("elite_coil", () -> new BaseItem());
	public static final DeferredHolder<Item, Item> ULTIMATE_COIL = REGISTRY.register("ultimate_coil", () -> new BaseItem());

	public static final DeferredHolder<Item, Item> CELL = REGISTRY.register("cell", () -> new ComponentItem("cell"));
	public static final DeferredHolder<Item, Item> THRUSTER = REGISTRY.register("thruster", () -> new ComponentItem("thruster"));
	public static final DeferredHolder<Item, Item> CAPACITOR = REGISTRY.register("capacitor", () -> new ComponentItem("capacitor"));
	public static final DeferredHolder<Item, Item> JETPACK = REGISTRY.register("jetpack", JetpackItem::new);
}
