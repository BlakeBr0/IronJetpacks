package com.blakebr0.ironjetpacks.crafting;

import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.crafting.recipe.JetpackUpgradeRecipe;
import com.blakebr0.ironjetpacks.registry.Jetpack;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.resources.IResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class JetpackDynamicRecipeManager implements ISelectiveResourceReloadListener {
    @Override
    public void onResourceManagerReload(IResourceManager manager, Predicate<IResourceType> predicate) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
//        if (server.getWorld(DimensionType.OVERWORLD).getWorldInfo().getDisabledDataPacks().contains("mod:" + IronJetpacks.MOD_ID))
//            return;

        RecipeManager recipeManager = server.getRecipeManager();
        recipeManager.recipes = new HashMap<>(recipeManager.recipes);
        recipeManager.recipes.replaceAll((t, v) -> new HashMap<>(recipeManager.recipes.get(t)));

        Map<ResourceLocation, IRecipe<?>> recipes = recipeManager.recipes.get(IRecipeType.CRAFTING);
        JetpackRegistry jetpacks = JetpackRegistry.getInstance();

        jetpacks.getAllJetpacks().forEach(jetpack -> {
            ShapedRecipe cell = this.makeCellRecipe(jetpack);
            ShapedRecipe thruster = this.makeThrusterRecipe(jetpack);
            ShapedRecipe capacitor = this.makeCapacitorRecipe(jetpack);
            ShapedRecipe jetpackSelf = this.makeJetpackRecipe(jetpack);
            JetpackUpgradeRecipe jetpackUpgrade = this.makeJetpackUpgradeRecipe(jetpack);

            if (cell != null)
                recipes.put(cell.getId(), cell);
            if (thruster != null)
                recipes.put(thruster.getId(), thruster);
            if (capacitor != null)
                recipes.put(capacitor.getId(), capacitor);
            if (jetpackSelf != null)
                recipes.put(jetpackSelf.getId(), jetpackSelf);
            if (jetpackUpgrade != null)
                recipes.put(jetpackUpgrade.getId(), jetpackUpgrade);
        });
    }

    private ShapedRecipe makeCellRecipe(Jetpack jetpack) {
        JetpackRegistry jetpacks = JetpackRegistry.getInstance();

        Ingredient material = jetpack.getCraftingMaterial();
        if (material == Ingredient.EMPTY)
            return null;

        Ingredient coil = Ingredient.fromItems(jetpacks.getCoilForTier(jetpack.tier));
        Ingredient redstone = Ingredient.fromTag(Tags.Items.DUSTS_REDSTONE);
        NonNullList<Ingredient> inputs = NonNullList.from(Ingredient.EMPTY,
                Ingredient.EMPTY, redstone, Ingredient.EMPTY,
                material, coil, material,
                Ingredient.EMPTY, redstone, Ingredient.EMPTY
        );

        ResourceLocation name = new ResourceLocation(IronJetpacks.MOD_ID, jetpack.name + "_cell");
        ItemStack output = new ItemStack(jetpack.cell);

        return new ShapedRecipe(name, "ironjetpacks:cells", 3, 3, inputs, output);
    }

    private ShapedRecipe makeThrusterRecipe(Jetpack jetpack) {
        Ingredient material = jetpack.getCraftingMaterial();
        if (material == Ingredient.EMPTY)
            return null;

        Ingredient cell = Ingredient.fromItems(jetpack.cell);
        NonNullList<Ingredient> inputs = NonNullList.from(Ingredient.EMPTY,
                material, cell, material,
                material, cell, material,
                material, cell, material
        );

        ResourceLocation name = new ResourceLocation(IronJetpacks.MOD_ID, jetpack.name + "_thruster");
        ItemStack output = new ItemStack(jetpack.thruster);

        return new ShapedRecipe(name, "ironjetpacks:thrusters", 3, 3, inputs, output);
    }

    private ShapedRecipe makeCapacitorRecipe(Jetpack jetpack) {
        JetpackRegistry jetpacks = JetpackRegistry.getInstance();

        Ingredient material = jetpack.getCraftingMaterial();
        if (material == Ingredient.EMPTY)
            return null;

        Ingredient coil = Ingredient.fromItems(jetpacks.getCoilForTier(jetpack.tier));
        Ingredient cell = Ingredient.fromItems(jetpack.cell);
        Ingredient furnace = Ingredient.fromItems(Blocks.FURNACE);
        NonNullList<Ingredient> inputs = NonNullList.from(Ingredient.EMPTY,
                material, coil, material,
                coil, cell, coil,
                material, furnace, material
        );

        ResourceLocation name = new ResourceLocation(IronJetpacks.MOD_ID, jetpack.name + "_capacitor");
        ItemStack output = new ItemStack(jetpack.cell);

        return new ShapedRecipe(name, "ironjetpacks:capacitors", 3, 3, inputs, output);
    }

    private ShapedRecipe makeJetpackRecipe(Jetpack jetpack) {
        return null;
    }

    private JetpackUpgradeRecipe makeJetpackUpgradeRecipe(Jetpack jetpack) {
        return null;
    }
}
