package com.blakebr0.ironjetpacks.crafting.recipe;

import com.blakebr0.cucumber.helper.NBTHelper;
import com.blakebr0.ironjetpacks.init.ModRecipeSerializers;
import com.blakebr0.ironjetpacks.item.JetpackItem;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

public class JetpackUpgradeRecipe extends ShapedRecipe {
    private final ItemStack result;

    public JetpackUpgradeRecipe(ResourceLocation id, String group, int width, int height, NonNullList<Ingredient> inputs, ItemStack result, boolean showNotification) {
        super(id, group, CraftingBookCategory.EQUIPMENT, width, height, inputs, result, showNotification);
        this.result = result;
    }

    @Override
    public ItemStack assemble(CraftingContainer inventory, RegistryAccess access) {
        var stack = inventory.getItem(4);
        var result = this.getResultItem(access).copy();

        if (!stack.isEmpty() && stack.getItem() instanceof JetpackItem) {
            var tag = stack.getTag();

            if (tag != null) {
                var jetpack = NBTHelper.getString(result, "Id");

                tag = tag.copy();
                tag.putString("Id", jetpack);

                result.setTag(tag);

                return result;
            }
        }

        return result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.CRAFTING_JETPACK_UPGRADE.get();
    }

    public static class Serializer implements RecipeSerializer<JetpackUpgradeRecipe> {
        @Override
        public JetpackUpgradeRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            var recipe = (JetpackUpgradeRecipe) RecipeSerializer.SHAPED_RECIPE.fromJson(recipeId, json);
            return new JetpackUpgradeRecipe(recipeId, recipe.getGroup(), recipe.getRecipeWidth(), recipe.getRecipeHeight(), recipe.getIngredients(), recipe.result, recipe.showNotification());
        }

        @Override
        public JetpackUpgradeRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            var group = buffer.readUtf(32767);
            int width = buffer.readVarInt();
            int height = buffer.readVarInt();
            var inputs = NonNullList.withSize(width * height, Ingredient.EMPTY);

            for (int k = 0; k < inputs.size(); k++) {
                inputs.set(k, Ingredient.fromNetwork(buffer));
            }

            var output = buffer.readItem();
            var showNotification = buffer.readBoolean();

            return new JetpackUpgradeRecipe(recipeId, group, width, height, inputs, output, showNotification);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, JetpackUpgradeRecipe recipe) {
            buffer.writeUtf(recipe.getGroup());
            buffer.writeVarInt(recipe.getRecipeWidth());
            buffer.writeVarInt(recipe.getRecipeHeight());

            for (var ingredient : recipe.getIngredients()) {
                ingredient.toNetwork(buffer);
            }

            buffer.writeItem(recipe.result);
            buffer.writeBoolean(recipe.showNotification());
        }
    }
}
