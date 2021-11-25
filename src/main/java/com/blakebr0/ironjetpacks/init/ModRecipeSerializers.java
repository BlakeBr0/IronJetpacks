package com.blakebr0.ironjetpacks.init;

import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.crafting.ingredient.JetpackComponentIngredient;
import com.blakebr0.ironjetpacks.crafting.ingredient.JetpackTierIngredient;
import com.blakebr0.ironjetpacks.crafting.recipe.JetpackUpgradeRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class ModRecipeSerializers {
    public static final RecipeSerializer<JetpackUpgradeRecipe> CRAFTING_JETPACK_UPGRADE = new JetpackUpgradeRecipe.Serializer();

    public static final IIngredientSerializer<JetpackTierIngredient> JETPACK_TIER_INGREDIENT = new JetpackTierIngredient.Serializer();
    public static final IIngredientSerializer<JetpackComponentIngredient> JETPACK_COMPONENT_INGREDIENT = new JetpackComponentIngredient.Serializer();

    @SubscribeEvent
    public void onRegisterSerializers(RegistryEvent.Register<RecipeSerializer<?>> event) {
        var registry = event.getRegistry();

        registry.register(CRAFTING_JETPACK_UPGRADE.setRegistryName(new ResourceLocation(IronJetpacks.MOD_ID, "crafting_jetpack_upgrade")));

        CraftingHelper.register(new ResourceLocation(IronJetpacks.MOD_ID, "jetpack_tier"), JETPACK_TIER_INGREDIENT);
        CraftingHelper.register(new ResourceLocation(IronJetpacks.MOD_ID, "jetpack_component"), JETPACK_COMPONENT_INGREDIENT);
    }
}
