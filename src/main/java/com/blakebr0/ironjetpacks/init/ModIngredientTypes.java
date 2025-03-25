package com.blakebr0.ironjetpacks.init;

import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.crafting.ingredient.JetpackComponentIngredient;
import com.blakebr0.ironjetpacks.crafting.ingredient.JetpackTierIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class ModIngredientTypes {
    public static final DeferredRegister<IngredientType<?>> REGISTRY = DeferredRegister.create(NeoForgeRegistries.INGREDIENT_TYPES, IronJetpacks.MOD_ID);

    public static final DeferredHolder<IngredientType<?>, IngredientType<?>> JETPACK_TIER_INGREDIENT = REGISTRY.register("jetpack_tier", () -> new IngredientType<>(JetpackTierIngredient.CODEC));
    public static final DeferredHolder<IngredientType<?>, IngredientType<?>> JETPACK_COMPONENT_INGREDIENT = REGISTRY.register("jetpack_component", () -> new IngredientType<>(JetpackComponentIngredient.CODEC));
}
