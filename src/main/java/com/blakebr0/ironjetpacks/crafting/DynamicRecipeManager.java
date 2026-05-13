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
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;

import java.util.List;
import java.util.Map;

public class DynamicRecipeManager {
    private static final DynamicRecipeManager INSTANCE = new DynamicRecipeManager();

    @SubscribeEvent
    public void onRecipeManagerLoading(RecipeManagerLoadingEvent event) {
        JetpackRegistry.getInstance().loadJetpacks();

        var registries = event.getRegistries();

        for (var jetpack : JetpackRegistry.getInstance().getJetpacks()) {
            var cell = makeCellRecipe(jetpack, registries);
            var thruster = makeThrusterRecipe(jetpack, registries);
            var capacitor = makeCapacitorRecipe(jetpack, registries);
            var jetpackSelf = makeJetpackRecipe(jetpack, registries);
            var jetpackUpgrade = makeJetpackUpgradeRecipe(jetpack, registries);

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

    private static RecipeHolder<ShapedRecipe> makeCellRecipe(Jetpack jetpack, HolderLookup.Provider registries) {
        if (!ModConfigs.ENABLE_CELL_RECIPES.get())
            return null;

        var material = jetpack.getCraftingMaterial(registries);
        if (material == null)
            return null;

        var coil = Ingredient.of(JetpackRegistry.getInstance().getCoilForTier(jetpack.tier));
        var redstone = Ingredient.of(registries.getOrThrow(Tags.Items.DUSTS_REDSTONE));

        var keys = Map.of(
                'M', material,
                'C', coil,
                'R', redstone
        );
        var shape = List.of(
                " R ",
                "MCM",
                " R "
        );

        var id = IronJetpacks.resource(jetpack.name + "_cell");
        var pattern = ShapedRecipePattern.of(keys, shape);
        var result = JetpackUtils.getItemForComponent(ModItems.CELL.get(), jetpack);

        return new RecipeHolder<>(
                ResourceKey.create(Registries.RECIPE, id),
                new ShapedRecipe(
                        new Recipe.CommonInfo(false),
                        new CraftingRecipe.CraftingBookInfo(CraftingBookCategory.MISC, "ironjetpacks:cells"),
                        pattern,
                        result
                )
        );
    }

    private static RecipeHolder<ShapedRecipe> makeThrusterRecipe(Jetpack jetpack, HolderLookup.Provider registries) {
        if (!ModConfigs.ENABLE_THRUSTER_RECIPES.get())
            return null;

        var material = jetpack.getCraftingMaterial(registries);
        if (material == null)
            return null;

        var coil = Ingredient.of(JetpackRegistry.getInstance().getCoilForTier(jetpack.tier));
        var cell = DataComponentIngredient.of(false, JetpackUtils.getItemForComponent(ModItems.CELL.get(), jetpack));
        var furnace = Ingredient.of(Blocks.FURNACE);

        var keys = Map.of(
                'M', material,
                'C', coil,
                'E', cell,
                'F', furnace
        );
        var shape = List.of(
                "MCM",
                "CEC",
                "MFM"
        );

        var id = IronJetpacks.resource(jetpack.name + "_thruster");
        var pattern = ShapedRecipePattern.of(keys, shape);
        var result = JetpackUtils.getItemForComponent(ModItems.THRUSTER.get(), jetpack);

        return new RecipeHolder<>(
                ResourceKey.create(Registries.RECIPE, id),
                new ShapedRecipe(
                        new Recipe.CommonInfo(false),
                        new CraftingRecipe.CraftingBookInfo(CraftingBookCategory.MISC, "ironjetpacks:thrusters"),
                        pattern,
                        result
                ));
    }

    private static RecipeHolder<ShapedRecipe> makeCapacitorRecipe(Jetpack jetpack, HolderLookup.Provider registries) {
        if (!ModConfigs.ENABLE_CAPACITOR_RECIPES.get())
            return null;

        var material = jetpack.getCraftingMaterial(registries);
        if (material == null)
            return null;

        var cell = DataComponentIngredient.of(false, JetpackUtils.getItemForComponent(ModItems.CELL.get(), jetpack));

        var keys = Map.of(
                'M', material,
                'E', cell
        );
        var shape = List.of(
                "MEM",
                "MEM",
                "MEM"
        );

        var id = IronJetpacks.resource(jetpack.name + "_capacitor");
        var pattern = ShapedRecipePattern.of(keys, shape);
        var result = JetpackUtils.getItemForComponent(ModItems.CAPACITOR.get(), jetpack);

        return new RecipeHolder<>(
                ResourceKey.create(Registries.RECIPE, id),
                new ShapedRecipe(
                        new Recipe.CommonInfo(false),
                        new CraftingRecipe.CraftingBookInfo(CraftingBookCategory.MISC, "ironjetpacks:capacitors"),
                        pattern,
                        result
                ));
    }

    private static RecipeHolder<ShapedRecipe> makeJetpackRecipe(Jetpack jetpack, HolderLookup.Provider registries) {
        if (!ModConfigs.ENABLE_JETPACK_RECIPES.get())
            return null;

        if (jetpack.tier != JetpackRegistry.getInstance().getLowestTier())
            return null;

        var material = jetpack.getCraftingMaterial(registries);
        if (material == null)
            return null;

        var capacitor = DataComponentIngredient.of(false, JetpackUtils.getItemForComponent(ModItems.CAPACITOR.get(), jetpack));
        var thruster = DataComponentIngredient.of(false, JetpackUtils.getItemForComponent(ModItems.THRUSTER.get(), jetpack));
        var strap = Ingredient.of(ModItems.STRAP.get());

        var keys = Map.of(
                'M', material,
                'C', capacitor,
                'S', strap,
                'T', thruster
        );
        var shape = List.of(
                "MCM",
                "MSM",
                "T T"
        );

        var id = IronJetpacks.resource(jetpack.name + "_jetpack");
        var pattern = ShapedRecipePattern.of(keys, shape);
        var result = JetpackUtils.getItemForJetpack(jetpack);

        return new RecipeHolder<>(
                ResourceKey.create(Registries.RECIPE, id),
                new ShapedRecipe(
                        new Recipe.CommonInfo(false),
                        new CraftingRecipe.CraftingBookInfo(CraftingBookCategory.EQUIPMENT, "ironjetpacks:jetpacks"),
                        pattern,
                        result
                )
        );
    }

    private static RecipeHolder<JetpackUpgradeRecipe> makeJetpackUpgradeRecipe(Jetpack jetpack, HolderLookup.Provider registries) {
        if (!ModConfigs.ENABLE_JETPACK_RECIPES.get())
            return null;

        if (jetpack.tier == JetpackRegistry.getInstance().getLowestTier())
            return null;

        var material = jetpack.getCraftingMaterial(registries);
        if (material == null)
            return null;

        var capacitor = DataComponentIngredient.of(false, JetpackUtils.getItemForComponent(ModItems.CAPACITOR.get(), jetpack));
        var thruster = DataComponentIngredient.of(false, JetpackUtils.getItemForComponent(ModItems.THRUSTER.get(), jetpack));
        var jetpackTier = JetpackTierIngredient.of(jetpack.tier - 1);

        var keys = Map.of(
                'M', material,
                'C', capacitor,
                'J', jetpackTier,
                'T', thruster
        );
        var shape = List.of(
                "MCM",
                "MJM",
                "T T"
        );

        var id = IronJetpacks.resource(jetpack.name + "_jetpack");
        var pattern = ShapedRecipePattern.of(keys, shape);
        var result = JetpackUtils.getItemForJetpack(jetpack);

        return new RecipeHolder<>(
                ResourceKey.create(Registries.RECIPE, id),
                new JetpackUpgradeRecipe(
                        new Recipe.CommonInfo(false),
                        new CraftingRecipe.CraftingBookInfo(CraftingBookCategory.EQUIPMENT, "ironjetpacks:jetpacks"),
                        pattern,
                        result
                )
        );
    }
}
