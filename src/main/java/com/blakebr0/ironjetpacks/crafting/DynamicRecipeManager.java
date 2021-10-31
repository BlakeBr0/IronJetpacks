package com.blakebr0.ironjetpacks.crafting;

import com.blakebr0.cucumber.helper.RecipeHelper;
import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.config.ModConfigs;
import com.blakebr0.ironjetpacks.crafting.ingredient.JetpackIngredient;
import com.blakebr0.ironjetpacks.crafting.recipe.JetpackUpgradeRecipe;
import com.blakebr0.ironjetpacks.init.ModItems;
import com.blakebr0.ironjetpacks.registry.Jetpack;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.tags.SerializationTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DynamicRecipeManager implements ResourceManagerReloadListener {
    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        JetpackRegistry.getInstance().getAllJetpacks().forEach(jetpack -> {
            var cell = this.makeCellRecipe(jetpack);
            var thruster = this.makeThrusterRecipe(jetpack);
            var capacitor = this.makeCapacitorRecipe(jetpack);
            var jetpackSelf = this.makeJetpackRecipe(jetpack);
            var jetpackUpgrade = this.makeJetpackUpgradeRecipe(jetpack);

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

    @SubscribeEvent
    public void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(this);
    }

    private ShapedRecipe makeCellRecipe(Jetpack jetpack) {
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
        var output = new ItemStack(jetpack.cell);

        return new ShapedRecipe(name, "ironjetpacks:cells", 3, 3, inputs, output);
    }

    private ShapedRecipe makeThrusterRecipe(Jetpack jetpack) {
        if (!ModConfigs.ENABLE_THRUSTER_RECIPES.get())
            return null;

        var jetpacks = JetpackRegistry.getInstance();

        var material = jetpack.getCraftingMaterial();
        if (material == Ingredient.EMPTY)
            return null;

        var coil = Ingredient.of(jetpacks.getCoilForTier(jetpack.tier));
        var cell = Ingredient.of(jetpack.cell);
        var furnace = Ingredient.of(Blocks.FURNACE);
        var inputs = NonNullList.of(Ingredient.EMPTY,
                material, coil, material,
                coil, cell, coil,
                material, furnace, material
        );

        var name = new ResourceLocation(IronJetpacks.MOD_ID, jetpack.name + "_thruster");
        var output = new ItemStack(jetpack.thruster);

        return new ShapedRecipe(name, "ironjetpacks:thrusters", 3, 3, inputs, output);
    }

    private ShapedRecipe makeCapacitorRecipe(Jetpack jetpack) {
        if (!ModConfigs.ENABLE_CAPACITOR_RECIPES.get())
            return null;

        var material = jetpack.getCraftingMaterial();
        if (material == Ingredient.EMPTY)
            return null;

        var cell = Ingredient.of(jetpack.cell);
        var inputs = NonNullList.of(Ingredient.EMPTY,
                material, cell, material,
                material, cell, material,
                material, cell, material
        );

        var name = new ResourceLocation(IronJetpacks.MOD_ID, jetpack.name + "_capacitor");
        var output = new ItemStack(jetpack.capacitor);

        return new ShapedRecipe(name, "ironjetpacks:capacitors", 3, 3, inputs, output);
    }

    private ShapedRecipe makeJetpackRecipe(Jetpack jetpack) {
        if (!ModConfigs.ENABLE_JETPACK_RECIPES.get())
            return null;

        var jetpacks = JetpackRegistry.getInstance();
        if (jetpack.tier != jetpacks.getLowestTier())
            return null;

        var material = jetpack.getCraftingMaterial();
        if (material == Ingredient.EMPTY)
            return null;

        var capacitor = Ingredient.of(jetpack.capacitor);
        var thruster = Ingredient.of(jetpack.thruster);
        var strap = Ingredient.of(ModItems.STRAP.get());
        var inputs = NonNullList.of(Ingredient.EMPTY,
                material, capacitor, material,
                material, strap, material,
                thruster, Ingredient.EMPTY, thruster
        );

        var name = new ResourceLocation(IronJetpacks.MOD_ID, jetpack.name + "_jetpack");
        var output = new ItemStack(jetpack.item);

        return new ShapedRecipe(name, "ironjetpacks:jetpacks", 3, 3, inputs, output);
    }

    private JetpackUpgradeRecipe makeJetpackUpgradeRecipe(Jetpack jetpack) {
        if (!ModConfigs.ENABLE_JETPACK_RECIPES.get())
            return null;

        var jetpacks = JetpackRegistry.getInstance();
        if (jetpack.tier == jetpacks.getLowestTier())
            return null;

        var material = jetpack.getCraftingMaterial();
        if (material == Ingredient.EMPTY)
            return null;

        var capacitor = Ingredient.of(jetpack.capacitor);
        var thruster = Ingredient.of(jetpack.thruster);
        var jetpackTier = new JetpackIngredient(jetpack.tier - 1);
        var inputs = NonNullList.of(Ingredient.EMPTY,
                material, capacitor, material,
                material, jetpackTier, material,
                thruster, Ingredient.EMPTY, thruster
        );

        var name = new ResourceLocation(IronJetpacks.MOD_ID, jetpack.name + "_jetpack");
        var output = new ItemStack(jetpack.item);

        return new JetpackUpgradeRecipe(name, "ironjetpacks:jetpacks", 3, 3, inputs, output);
    }
}
