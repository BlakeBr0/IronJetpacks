package com.blakebr0.ironjetpacks.crafting;

import com.blakebr0.cucumber.helper.RecipeHelper;
import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.config.ModConfigs;
import com.blakebr0.ironjetpacks.crafting.ingredient.JetpackIngredient;
import com.blakebr0.ironjetpacks.crafting.recipe.JetpackUpgradeRecipe;
import com.blakebr0.ironjetpacks.init.ModItems;
import com.blakebr0.ironjetpacks.registry.Jetpack;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DynamicRecipeManager implements IResourceManagerReloadListener {
    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        JetpackRegistry.getInstance().getAllJetpacks().forEach(jetpack -> {
            ShapedRecipe cell = this.makeCellRecipe(jetpack);
            ShapedRecipe thruster = this.makeThrusterRecipe(jetpack);
            ShapedRecipe capacitor = this.makeCapacitorRecipe(jetpack);
            ShapedRecipe jetpackSelf = this.makeJetpackRecipe(jetpack);
            JetpackUpgradeRecipe jetpackUpgrade = this.makeJetpackUpgradeRecipe(jetpack);

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

        JetpackRegistry jetpacks = JetpackRegistry.getInstance();

        Ingredient material = jetpack.getCraftingMaterial();
        if (material == Ingredient.EMPTY)
            return null;

        ITag<Item> redstoneTag = TagCollectionManager.func_242178_a().func_241836_b().get(Tags.Items.DUSTS_REDSTONE.getName());
        if (redstoneTag == null)
            return null;

        Ingredient coil = Ingredient.fromItems(jetpacks.getCoilForTier(jetpack.tier));
        Ingredient redstone = Ingredient.fromTag(redstoneTag);
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
        if (!ModConfigs.ENABLE_THRUSTER_RECIPES.get())
            return null;

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

        ResourceLocation name = new ResourceLocation(IronJetpacks.MOD_ID, jetpack.name + "_thruster");
        ItemStack output = new ItemStack(jetpack.thruster);

        return new ShapedRecipe(name, "ironjetpacks:thrusters", 3, 3, inputs, output);
    }

    private ShapedRecipe makeCapacitorRecipe(Jetpack jetpack) {
        if (!ModConfigs.ENABLE_CAPACITOR_RECIPES.get())
            return null;

        Ingredient material = jetpack.getCraftingMaterial();
        if (material == Ingredient.EMPTY)
            return null;

        Ingredient cell = Ingredient.fromItems(jetpack.cell);
        NonNullList<Ingredient> inputs = NonNullList.from(Ingredient.EMPTY,
                material, cell, material,
                material, cell, material,
                material, cell, material
        );

        ResourceLocation name = new ResourceLocation(IronJetpacks.MOD_ID, jetpack.name + "_capacitor");
        ItemStack output = new ItemStack(jetpack.capacitor);

        return new ShapedRecipe(name, "ironjetpacks:capacitors", 3, 3, inputs, output);
    }

    private ShapedRecipe makeJetpackRecipe(Jetpack jetpack) {
        if (!ModConfigs.ENABLE_JETPACK_RECIPES.get())
            return null;

        JetpackRegistry jetpacks = JetpackRegistry.getInstance();
        if (jetpack.tier != jetpacks.getLowestTier())
            return null;

        Ingredient material = jetpack.getCraftingMaterial();
        if (material == Ingredient.EMPTY)
            return null;

        Ingredient capacitor = Ingredient.fromItems(jetpack.capacitor);
        Ingredient thruster = Ingredient.fromItems(jetpack.thruster);
        Ingredient strap = Ingredient.fromItems(ModItems.STRAP.get());
        NonNullList<Ingredient> inputs = NonNullList.from(Ingredient.EMPTY,
                material, capacitor, material,
                material, strap, material,
                thruster, Ingredient.EMPTY, thruster
        );

        ResourceLocation name = new ResourceLocation(IronJetpacks.MOD_ID, jetpack.name + "_jetpack");
        ItemStack output = new ItemStack(jetpack.item);

        return new ShapedRecipe(name, "ironjetpacks:jetpacks", 3, 3, inputs, output);
    }

    private JetpackUpgradeRecipe makeJetpackUpgradeRecipe(Jetpack jetpack) {
        if (!ModConfigs.ENABLE_JETPACK_RECIPES.get())
            return null;

        JetpackRegistry jetpacks = JetpackRegistry.getInstance();
        if (jetpack.tier == jetpacks.getLowestTier())
            return null;

        Ingredient material = jetpack.getCraftingMaterial();
        if (material == Ingredient.EMPTY)
            return null;

        Ingredient capacitor = Ingredient.fromItems(jetpack.capacitor);
        Ingredient thruster = Ingredient.fromItems(jetpack.thruster);
        Ingredient jetpackTier = new JetpackIngredient(jetpack.tier - 1);
        NonNullList<Ingredient> inputs = NonNullList.from(Ingredient.EMPTY,
                material, capacitor, material,
                material, jetpackTier, material,
                thruster, Ingredient.EMPTY, thruster
        );

        ResourceLocation name = new ResourceLocation(IronJetpacks.MOD_ID, jetpack.name + "_jetpack");
        ItemStack output = new ItemStack(jetpack.item);

        return new JetpackUpgradeRecipe(name, "ironjetpacks:jetpacks", 3, 3, inputs, output);
    }
}
