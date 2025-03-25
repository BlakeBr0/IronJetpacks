package com.blakebr0.ironjetpacks.crafting;

import com.blakebr0.cucumber.event.RecipeManagerLoadingEvent;
import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.config.ModConfigs;
import com.blakebr0.ironjetpacks.crafting.ingredient.JetpackTierIngredient;
import com.blakebr0.ironjetpacks.crafting.recipe.JetpackUpgradeRecipe;
import com.blakebr0.ironjetpacks.init.ModItems;
import com.blakebr0.ironjetpacks.registry.Jetpack;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry;
import com.blakebr0.ironjetpacks.util.JetpackUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;

import java.util.Optional;

public class DynamicRecipeManager {
    private static final DynamicRecipeManager INSTANCE = new DynamicRecipeManager();

    @SubscribeEvent
    public void onRecipeManagerLoading(RecipeManagerLoadingEvent event) {
        JetpackRegistry.getInstance().loadJetpacks();

        for (var jetpack : JetpackRegistry.getInstance().getJetpacks()) {
            var cell = makeCellRecipe(jetpack);
            var thruster = makeThrusterRecipe(jetpack);
            var capacitor = makeCapacitorRecipe(jetpack);
            var jetpackSelf = makeJetpackRecipe(jetpack);
            var jetpackUpgrade = makeJetpackUpgradeRecipe(jetpack);

            if (cell != null)
                event.addRecipe(cell);
            if (thruster != null)
                event.addRecipe(thruster);
            if (capacitor != null)
                event.addRecipe(capacitor);
            if (jetpackSelf != null)
                event.addRecipe(jetpackSelf);
            if (jetpackUpgrade != null)
                event.addRecipe(jetpackUpgrade);
        }
    }

    public static DynamicRecipeManager getInstance() {
        return INSTANCE;
    }

    private static RecipeHolder<ShapedRecipe> makeCellRecipe(Jetpack jetpack) {
        if (!ModConfigs.ENABLE_CELL_RECIPES.get())
            return null;

        var material = jetpack.getCraftingMaterial();
        if (material == Ingredient.EMPTY)
            return null;

        var coil = Ingredient.of(JetpackRegistry.getInstance().getCoilForTier(jetpack.tier));
        var redstone = Ingredient.of(Tags.Items.DUSTS_REDSTONE);
        var inputs = NonNullList.of(Ingredient.EMPTY,
                Ingredient.EMPTY, redstone, Ingredient.EMPTY,
                material, coil, material,
                Ingredient.EMPTY, redstone, Ingredient.EMPTY
        );

        var id = IronJetpacks.resource(jetpack.name + "_cell");
        var pattern = new ShapedRecipePattern(3, 3, inputs, Optional.empty());
        var result = JetpackUtils.getItemForComponent(ModItems.CELL.get(), jetpack);

        return new RecipeHolder<>(id, new ShapedRecipe("ironjetpacks:cells", CraftingBookCategory.MISC, pattern, result));
    }

    private static RecipeHolder<ShapedRecipe> makeThrusterRecipe(Jetpack jetpack) {
        if (!ModConfigs.ENABLE_THRUSTER_RECIPES.get())
            return null;

        var material = jetpack.getCraftingMaterial();
        if (material == Ingredient.EMPTY)
            return null;

        var coil = Ingredient.of(JetpackRegistry.getInstance().getCoilForTier(jetpack.tier));
        var cell = DataComponentIngredient.of(true, JetpackUtils.getItemForComponent(ModItems.CELL.get(), jetpack));
        var furnace = Ingredient.of(Blocks.FURNACE);
        var inputs = NonNullList.of(Ingredient.EMPTY,
                material, coil, material,
                coil, cell, coil,
                material, furnace, material
        );

        var id = IronJetpacks.resource(jetpack.name + "_thruster");
        var pattern = new ShapedRecipePattern(3, 3, inputs, Optional.empty());
        var result = JetpackUtils.getItemForComponent(ModItems.THRUSTER.get(), jetpack);

        return new RecipeHolder<>(id, new ShapedRecipe("ironjetpacks:thrusters", CraftingBookCategory.MISC, pattern, result));
    }

    private static RecipeHolder<ShapedRecipe> makeCapacitorRecipe(Jetpack jetpack) {
        if (!ModConfigs.ENABLE_CAPACITOR_RECIPES.get())
            return null;

        var material = jetpack.getCraftingMaterial();
        if (material == Ingredient.EMPTY)
            return null;

        var cell = DataComponentIngredient.of(true, JetpackUtils.getItemForComponent(ModItems.CELL.get(), jetpack));
        var inputs = NonNullList.of(Ingredient.EMPTY,
                material, cell, material,
                material, cell, material,
                material, cell, material
        );

        var id = IronJetpacks.resource(jetpack.name + "_capacitor");
        var pattern = new ShapedRecipePattern(3, 3, inputs, Optional.empty());
        var result = JetpackUtils.getItemForComponent(ModItems.CAPACITOR.get(), jetpack);

        return new RecipeHolder<>(id, new ShapedRecipe("ironjetpacks:capacitors", CraftingBookCategory.MISC, pattern, result));
    }

    private static RecipeHolder<ShapedRecipe> makeJetpackRecipe(Jetpack jetpack) {
        if (!ModConfigs.ENABLE_JETPACK_RECIPES.get())
            return null;

        if (jetpack.tier != JetpackRegistry.getInstance().getLowestTier())
            return null;

        var material = jetpack.getCraftingMaterial();
        if (material == Ingredient.EMPTY)
            return null;

        var capacitor = DataComponentIngredient.of(true, JetpackUtils.getItemForComponent(ModItems.CAPACITOR.get(), jetpack));
        var thruster = DataComponentIngredient.of(true, JetpackUtils.getItemForComponent(ModItems.THRUSTER.get(), jetpack));
        var strap = Ingredient.of(ModItems.STRAP.get());
        var inputs = NonNullList.of(Ingredient.EMPTY,
                material, capacitor, material,
                material, strap, material,
                thruster, Ingredient.EMPTY, thruster
        );

        var id = IronJetpacks.resource(jetpack.name + "_jetpack");
        var pattern = new ShapedRecipePattern(3, 3, inputs, Optional.empty());
        var result = JetpackUtils.getItemForJetpack(jetpack);

        return new RecipeHolder<>(id, new ShapedRecipe("ironjetpacks:jetpacks", CraftingBookCategory.EQUIPMENT, pattern, result));
    }

    private static RecipeHolder<JetpackUpgradeRecipe> makeJetpackUpgradeRecipe(Jetpack jetpack) {
        if (!ModConfigs.ENABLE_JETPACK_RECIPES.get())
            return null;

        if (jetpack.tier == JetpackRegistry.getInstance().getLowestTier())
            return null;

        var material = jetpack.getCraftingMaterial();
        if (material == Ingredient.EMPTY)
            return null;

        var capacitor = DataComponentIngredient.of(true, JetpackUtils.getItemForComponent(ModItems.CAPACITOR.get(), jetpack));
        var thruster = DataComponentIngredient.of(true, JetpackUtils.getItemForComponent(ModItems.THRUSTER.get(), jetpack));
        var jetpackTier = JetpackTierIngredient.of(jetpack.tier - 1);
        var inputs = NonNullList.of(Ingredient.EMPTY,
                material, capacitor, material,
                material, jetpackTier, material,
                thruster, Ingredient.EMPTY, thruster
        );

        var id = IronJetpacks.resource(jetpack.name + "_jetpack");
        var pattern = new ShapedRecipePattern(3, 3, inputs, Optional.empty());
        var result = JetpackUtils.getItemForJetpack(jetpack);

        return new RecipeHolder<>(id, new JetpackUpgradeRecipe("ironjetpacks:jetpacks", pattern, result, false));
    }
}
