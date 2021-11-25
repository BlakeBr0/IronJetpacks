package com.blakebr0.ironjetpacks.crafting;

import com.blakebr0.cucumber.helper.RecipeHelper;
import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.config.ModConfigs;
import com.blakebr0.ironjetpacks.crafting.ingredient.JetpackIngredient;
import com.blakebr0.ironjetpacks.crafting.recipe.JetpackUpgradeRecipe;
import com.blakebr0.ironjetpacks.init.ModItems;
import com.blakebr0.ironjetpacks.registry.Jetpack;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry;
import com.blakebr0.ironjetpacks.util.JetpackUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.SerializationTags;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;

public class DynamicRecipeManager {
    private static final DynamicRecipeManager INSTANCE = new DynamicRecipeManager();

    public void onResourceManagerReload(ResourceManager manager) {
        JetpackRegistry.getInstance().getJetpacks().forEach(jetpack -> {
            var cell = makeCellRecipe(jetpack);
            var thruster = makeThrusterRecipe(jetpack);
            var capacitor = makeCapacitorRecipe(jetpack);
            var jetpackSelf = makeJetpackRecipe(jetpack);
            var jetpackUpgrade = makeJetpackUpgradeRecipe(jetpack);

            if (cell != null)
                RecipeHelper.addRecipe(cell);
            if (thruster != null)
                RecipeHelper.addRecipe(thruster);
            if (capacitor != null)
                RecipeHelper.addRecipe(capacitor);
            if (jetpackSelf != null)
                RecipeHelper.addRecipe(jetpackSelf);
            if (jetpackUpgrade != null)
                RecipeHelper.addRecipe(jetpackUpgrade);
        });
    }

    public static DynamicRecipeManager getInstance() {
        return INSTANCE;
    }

    private static ShapedRecipe makeCellRecipe(Jetpack jetpack) {
        if (!ModConfigs.ENABLE_CELL_RECIPES.get())
            return null;

        var jetpacks = JetpackRegistry.getInstance();

        var material = jetpack.getCraftingMaterial();
        if (material == Ingredient.EMPTY)
            return null;

        var redstoneTag = SerializationTags.getInstance().getOrEmpty(Registry.ITEM_REGISTRY).getTag(Tags.Items.DUSTS_REDSTONE.getName());
        if (redstoneTag == null)
            return null;

        var coil = Ingredient.of(jetpacks.getCoilForTier(jetpack.tier));
        var redstone = Ingredient.of(redstoneTag);
        var inputs = NonNullList.of(Ingredient.EMPTY,
                Ingredient.EMPTY, redstone, Ingredient.EMPTY,
                material, coil, material,
                Ingredient.EMPTY, redstone, Ingredient.EMPTY
        );

        var name = new ResourceLocation(IronJetpacks.MOD_ID, jetpack.name + "_cell");
        var output = JetpackUtils.getItemForComponent(ModItems.CELL.get(), jetpack);

        return new ShapedRecipe(name, "ironjetpacks:cells", 3, 3, inputs, output);
    }

    private static ShapedRecipe makeThrusterRecipe(Jetpack jetpack) {
        if (!ModConfigs.ENABLE_THRUSTER_RECIPES.get())
            return null;

        var jetpacks = JetpackRegistry.getInstance();

        var material = jetpack.getCraftingMaterial();
        if (material == Ingredient.EMPTY)
            return null;

        var coil = Ingredient.of(jetpacks.getCoilForTier(jetpack.tier));
        var cell = Ingredient.of(JetpackUtils.getItemForComponent(ModItems.CELL.get(), jetpack));
        var furnace = Ingredient.of(Blocks.FURNACE);
        var inputs = NonNullList.of(Ingredient.EMPTY,
                material, coil, material,
                coil, cell, coil,
                material, furnace, material
        );

        var name = new ResourceLocation(IronJetpacks.MOD_ID, jetpack.name + "_thruster");
        var output = JetpackUtils.getItemForComponent(ModItems.THRUSTER.get(), jetpack);

        return new ShapedRecipe(name, "ironjetpacks:thrusters", 3, 3, inputs, output);
    }

    private static ShapedRecipe makeCapacitorRecipe(Jetpack jetpack) {
        if (!ModConfigs.ENABLE_CAPACITOR_RECIPES.get())
            return null;

        var material = jetpack.getCraftingMaterial();
        if (material == Ingredient.EMPTY)
            return null;

        var cell = Ingredient.of(JetpackUtils.getItemForComponent(ModItems.CELL.get(), jetpack));
        var inputs = NonNullList.of(Ingredient.EMPTY,
                material, cell, material,
                material, cell, material,
                material, cell, material
        );

        var name = new ResourceLocation(IronJetpacks.MOD_ID, jetpack.name + "_capacitor");
        var output = JetpackUtils.getItemForComponent(ModItems.CAPACITOR.get(), jetpack);

        return new ShapedRecipe(name, "ironjetpacks:capacitors", 3, 3, inputs, output);
    }

    private static ShapedRecipe makeJetpackRecipe(Jetpack jetpack) {
        if (!ModConfigs.ENABLE_JETPACK_RECIPES.get())
            return null;

        var jetpacks = JetpackRegistry.getInstance();
        if (jetpack.tier != jetpacks.getLowestTier())
            return null;

        var material = jetpack.getCraftingMaterial();
        if (material == Ingredient.EMPTY)
            return null;

        var capacitor = Ingredient.of(JetpackUtils.getItemForComponent(ModItems.CAPACITOR.get(), jetpack));
        var thruster = Ingredient.of(JetpackUtils.getItemForComponent(ModItems.THRUSTER.get(), jetpack));
        var strap = Ingredient.of(ModItems.STRAP.get());
        var inputs = NonNullList.of(Ingredient.EMPTY,
                material, capacitor, material,
                material, strap, material,
                thruster, Ingredient.EMPTY, thruster
        );

        var name = new ResourceLocation(IronJetpacks.MOD_ID, jetpack.name + "_jetpack");
        var output = JetpackUtils.getItemForJetpack(jetpack);

        return new ShapedRecipe(name, "ironjetpacks:jetpacks", 3, 3, inputs, output);
    }

    private static JetpackUpgradeRecipe makeJetpackUpgradeRecipe(Jetpack jetpack) {
        if (!ModConfigs.ENABLE_JETPACK_RECIPES.get())
            return null;

        var jetpacks = JetpackRegistry.getInstance();
        if (jetpack.tier == jetpacks.getLowestTier())
            return null;

        var material = jetpack.getCraftingMaterial();
        if (material == Ingredient.EMPTY)
            return null;

        var capacitor = Ingredient.of(JetpackUtils.getItemForComponent(ModItems.CAPACITOR.get(), jetpack));
        var thruster = Ingredient.of(JetpackUtils.getItemForComponent(ModItems.THRUSTER.get(), jetpack));
        var jetpackTier = new JetpackIngredient(jetpack.tier - 1);
        var inputs = NonNullList.of(Ingredient.EMPTY,
                material, capacitor, material,
                material, jetpackTier, material,
                thruster, Ingredient.EMPTY, thruster
        );

        var name = new ResourceLocation(IronJetpacks.MOD_ID, jetpack.name + "_jetpack");
        var output = JetpackUtils.getItemForJetpack(jetpack);

        return new JetpackUpgradeRecipe(name, "ironjetpacks:jetpacks", 3, 3, inputs, output);
    }
}
