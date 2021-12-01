package com.blakebr0.ironjetpacks.init;

import com.blakebr0.cucumber.item.BaseItem;
import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.item.ComponentItem;
import com.blakebr0.ironjetpacks.item.JetpackItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static com.blakebr0.ironjetpacks.IronJetpacks.CREATIVE_TAB;

public final class ModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, IronJetpacks.MOD_ID);

	public static final RegistryObject<Item> STRAP = register("strap");
	public static final RegistryObject<Item> BASIC_COIL = register("basic_coil");
	public static final RegistryObject<Item> ADVANCED_COIL = register("advanced_coil");
	public static final RegistryObject<Item> ELITE_COIL = register("elite_coil");
	public static final RegistryObject<Item> ULTIMATE_COIL = register("ultimate_coil");

	public static final RegistryObject<Item> CELL = register("cell", () -> new ComponentItem("cell", p -> p.tab(CREATIVE_TAB)));
	public static final RegistryObject<Item> THRUSTER = register("thruster", () -> new ComponentItem("thruster", p -> p.tab(CREATIVE_TAB)));
	public static final RegistryObject<Item> CAPACITOR = register("capacitor", () -> new ComponentItem("capacitor", p -> p.tab(CREATIVE_TAB)));
	public static final RegistryObject<Item> JETPACK = register("jetpack", () -> new JetpackItem(p -> p.tab(CREATIVE_TAB)));

	private static RegistryObject<Item> register(String name) {
		return register(name, () -> new BaseItem(p -> p.tab(CREATIVE_TAB)));
	}

	private static RegistryObject<Item> register(String name, Supplier<? extends Item> item) {
		return REGISTRY.register(name, item);
	}
}
