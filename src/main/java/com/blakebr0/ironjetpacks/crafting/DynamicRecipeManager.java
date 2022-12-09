package com.blakebr0.ironjetpacks.crafting;

import com.blakebr0.cucumber.event.RegisterRecipesEvent;
import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.config.ModConfigs;
import com.blakebr0.ironjetpacks.crafting.ingredient.JetpackTierIngredient;
import com.blakebr0.ironjetpacks.crafting.recipe.JetpackUpgradeRecipe;
import com.blakebr0.ironjetpacks.init.ModItems;
import com.blakebr0.ironjetpacks.registry.Jetpack;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry;
import com.blakebr0.ironjetpacks.util.JetpackUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.StrictNBTIngredient;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DynamicRecipeManager {
    private static final DynamicRecipeManager INSTANCE = new DynamicRecipeManager();

    @SubscribeEvent
    public void onRegisterRecipes(RegisterRecipesEvent event) {
        for (var jetpack : JetpackRegistry.getInstance().getJetpacks()) {
            var cell = makeCellRecipe(jetpack);
            var thruster = makeThrusterRecipe(jetpack);
            var capacitor = makeCapacitorRecipe(jetpack);
            var jetpackSelf = makeJetpackRecipe(jetpack);
            var jetpackUpgrade = makeJetpackUpgradeRecipe(jetpack);

            if (cell != null)
                event.register(cell);
            if (thruster != null)
                event.register(thruster);
            if (capacitor != null)
                event.register(capacitor);
            if (jetpackSelf != null)
                event.register(jetpackSelf);
            if (jetpackUpgrade != null)
                event.register(jetpackUpgrade);
        }
    }

    public static DynamicRecipeManager getInstance() {
        return INSTANCE;
    }

    private static ShapedRecipe makeCellRecipe(Jetpack jetpack) {
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

        var name = new ResourceLocation(IronJetpacks.MOD_ID, jetpack.name + "_cell");
        var output = JetpackUtils.getItemForComponent(ModItems.CELL.get(), jetpack);

        return new ShapedRecipe(name, "ironjetpacks:cells", CraftingBookCategory.MISC, 3, 3, inputs, output);
    }

    private static ShapedRecipe makeThrusterRecipe(Jetpack jetpack) {
        if (!ModConfigs.ENABLE_THRUSTER_RECIPES.get())
            return null;

        var material = jetpack.getCraftingMaterial();
        if (material == Ingredient.EMPTY)
            return null;

        var coil = Ingredient.of(JetpackRegistry.getInstance().getCoilForTier(jetpack.tier));
        var cell = StrictNBTIngredient.of(JetpackUtils.getItemForComponent(ModItems.CELL.get(), jetpack));
        var furnace = Ingredient.of(Blocks.FURNACE);
        var inputs = NonNullList.of(Ingredient.EMPTY,
                material, coil, material,
                coil, cell, coil,
                material, furnace, material
        );

        var name = new ResourceLocation(IronJetpacks.MOD_ID, jetpack.name + "_thruster");
        var output = JetpackUtils.getItemForComponent(ModItems.THRUSTER.get(), jetpack);

        return new ShapedRecipe(name, "ironjetpacks:thrusters", CraftingBookCategory.MISC, 3, 3, inputs, output);
    }

    private static ShapedRecipe makeCapacitorRecipe(Jetpack jetpack) {
        if (!ModConfigs.ENABLE_CAPACITOR_RECIPES.get())
            return null;

        var material = jetpack.getCraftingMaterial();
        if (material == Ingredient.EMPTY)
            return null;

        var cell = StrictNBTIngredient.of(JetpackUtils.getItemForComponent(ModItems.CELL.get(), jetpack));
        var inputs = NonNullList.of(Ingredient.EMPTY,
                material, cell, material,
                material, cell, material,
                material, cell, material
        );

        var name = new ResourceLocation(IronJetpacks.MOD_ID, jetpack.name + "_capacitor");
        var output = JetpackUtils.getItemForComponent(ModItems.CAPACITOR.get(), jetpack);

        return new ShapedRecipe(name, "ironjetpacks:capacitors", CraftingBookCategory.MISC, 3, 3, inputs, output);
    }

    private static ShapedRecipe makeJetpackRecipe(Jetpack jetpack) {
        if (!ModConfigs.ENABLE_JETPACK_RECIPES.get())
            return null;

        if (jetpack.tier != JetpackRegistry.getInstance().getLowestTier())
            return null;

        var material = jetpack.getCraftingMaterial();
        if (material == Ingredient.EMPTY)
            return null;

        var capacitor = StrictNBTIngredient.of(JetpackUtils.getItemForComponent(ModItems.CAPACITOR.get(), jetpack));
        var thruster = StrictNBTIngredient.of(JetpackUtils.getItemForComponent(ModItems.THRUSTER.get(), jetpack));
        var strap = Ingredient.of(ModItems.STRAP.get());
        var inputs = NonNullList.of(Ingredient.EMPTY,
                material, capacitor, material,
                material, strap, material,
                thruster, Ingredient.EMPTY, thruster
        );

        var name = new ResourceLocation(IronJetpacks.MOD_ID, jetpack.name + "_jetpack");
        var output = JetpackUtils.getItemForJetpack(jetpack);

        return new ShapedRecipe(name, "ironjetpacks:jetpacks", CraftingBookCategory.MISC, 3, 3, inputs, output);
    }

    private static JetpackUpgradeRecipe makeJetpackUpgradeRecipe(Jetpack jetpack) {
        if (!ModConfigs.ENABLE_JETPACK_RECIPES.get())
            return null;

        if (jetpack.tier == JetpackRegistry.getInstance().getLowestTier())
            return null;

        var material = jetpack.getCraftingMaterial();
        if (material == Ingredient.EMPTY)
            return null;

        var capacitor = StrictNBTIngredient.of(JetpackUtils.getItemForComponent(ModItems.CAPACITOR.get(), jetpack));
        var thruster = StrictNBTIngredient.of(JetpackUtils.getItemForComponent(ModItems.THRUSTER.get(), jetpack));
        var jetpackTier = new JetpackTierIngredient(jetpack.tier - 1);
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
