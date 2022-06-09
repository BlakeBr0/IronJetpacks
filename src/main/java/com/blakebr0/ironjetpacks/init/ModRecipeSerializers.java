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
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

public final class ModRecipeSerializers {
    public static final RecipeSerializer<JetpackUpgradeRecipe> CRAFTING_JETPACK_UPGRADE = new JetpackUpgradeRecipe.Serializer();

    public static final IIngredientSerializer<JetpackTierIngredient> JETPACK_TIER_INGREDIENT = new JetpackTierIngredient.Serializer();
    public static final IIngredientSerializer<JetpackComponentIngredient> JETPACK_COMPONENT_INGREDIENT = new JetpackComponentIngredient.Serializer();

    @SubscribeEvent
    public void onRegisterSerializers(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.RECIPE_SERIALIZERS, registry -> {
            registry.register(new ResourceLocation(IronJetpacks.MOD_ID, "crafting_jetpack_upgrade"), CRAFTING_JETPACK_UPGRADE);

            CraftingHelper.register(new ResourceLocation(IronJetpacks.MOD_ID, "jetpack_tier"), JETPACK_TIER_INGREDIENT);
            CraftingHelper.register(new ResourceLocation(IronJetpacks.MOD_ID, "jetpack_component"), JETPACK_COMPONENT_INGREDIENT);
        });
    }
}
