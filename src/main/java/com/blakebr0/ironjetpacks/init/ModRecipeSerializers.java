package com.blakebr0.ironjetpacks.init;

import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.crafting.ingredient.JetpackComponentIngredient;
import com.blakebr0.ironjetpacks.crafting.ingredient.JetpackTierIngredient;
import com.blakebr0.ironjetpacks.crafting.recipe.JetpackUpgradeRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public final class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, IronJetpacks.MOD_ID);

    public static final RegistryObject<RecipeSerializer<?>> CRAFTING_JETPACK_UPGRADE = register("crafting_jetpack_upgrade", JetpackUpgradeRecipe.Serializer::new);

    public static final IIngredientSerializer<?> JETPACK_TIER_INGREDIENT = new JetpackTierIngredient.Serializer();
    public static final IIngredientSerializer<?> JETPACK_COMPONENT_INGREDIENT = new JetpackComponentIngredient.Serializer();

    @SubscribeEvent
    public void onRegisterSerializers(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.RECIPE_SERIALIZERS, registry -> {
            CraftingHelper.register(new ResourceLocation(IronJetpacks.MOD_ID, "jetpack_tier"), JETPACK_TIER_INGREDIENT);
            CraftingHelper.register(new ResourceLocation(IronJetpacks.MOD_ID, "jetpack_component"), JETPACK_COMPONENT_INGREDIENT);
        });
    }

    private static RegistryObject<RecipeSerializer<?>> register(String name, Supplier<RecipeSerializer<?>> serializer) {
        return REGISTRY.register(name, serializer);
    }
}
