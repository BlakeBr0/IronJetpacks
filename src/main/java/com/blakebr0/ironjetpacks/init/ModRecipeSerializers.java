package com.blakebr0.ironjetpacks.init;

import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.crafting.ingredient.JetpackIngredient;
import com.blakebr0.ironjetpacks.crafting.recipe.JetpackUpgradeRecipe;
import com.blakebr0.ironjetpacks.item.JetpackItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public final class ModRecipeSerializers {
    public static final RecipeSerializer<JetpackUpgradeRecipe> CRAFTING_JETPACK_UPGRADE = new JetpackUpgradeRecipe.Serializer();

    public static final IIngredientSerializer<JetpackIngredient> JETPACK_INGREDIENT = new JetpackIngredient.Serializer();

    @SubscribeEvent
    public void onRegisterSerializers(RegistryEvent.Register<RecipeSerializer<?>> event) {
        var registry = event.getRegistry();

        registry.register(CRAFTING_JETPACK_UPGRADE.setRegistryName(new ResourceLocation(IronJetpacks.MOD_ID, "crafting_jetpack_upgrade")));

        CraftingHelper.register(new ResourceLocation(IronJetpacks.MOD_ID, "jetpack"), JETPACK_INGREDIENT);
    }

    public static void onCommonSetup() {
        List<JetpackItem> jetpacks = new ArrayList<>();

        ForgeRegistries.ITEMS.getValues().stream()
                .filter(i -> i instanceof JetpackItem)
                .forEach(i -> jetpacks.add((JetpackItem) i));

        JetpackIngredient.ALL_JETPACKS.addAll(jetpacks);
    }
}
