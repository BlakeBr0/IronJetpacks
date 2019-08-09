package com.blakebr0.ironjetpacks.crafting.ingredient;

import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.item.JetpackItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class ModIngredients {
    public static final IIngredientSerializer<JetpackIngredient> JETPACK_INGREDIENT;

    static {
        JETPACK_INGREDIENT = CraftingHelper.register(new ResourceLocation(IronJetpacks.MOD_ID, "jetpack"), new JetpackIngredient.Serializer());
    }

    public static void onCommonSetup() {
        List<JetpackItem> jetpacks = new ArrayList<>();
        ForgeRegistries.ITEMS.getValues().stream().filter(i -> i instanceof JetpackItem).forEach(i -> jetpacks.add((JetpackItem) i));
        JetpackIngredient.ALL_JETPACKS.addAll(jetpacks);
    }
}
