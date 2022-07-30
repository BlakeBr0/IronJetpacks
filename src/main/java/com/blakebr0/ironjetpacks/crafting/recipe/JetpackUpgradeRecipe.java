package com.blakebr0.ironjetpacks.crafting.recipe;

import com.blakebr0.cucumber.helper.NBTHelper;
import com.blakebr0.ironjetpacks.init.ModRecipeSerializers;
import com.blakebr0.ironjetpacks.item.JetpackItem;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

public class JetpackUpgradeRecipe extends ShapedRecipe {
    public JetpackUpgradeRecipe(ResourceLocation id, String group, int recipeWidth, int recipeHeight, NonNullList<Ingredient> inputs, ItemStack output) {
        super(id, group, recipeWidth, recipeHeight, inputs, output);
    }

    @Override
    public ItemStack assemble(CraftingContainer inv) {
        var stack = inv.getItem(4);
        var result = this.getResultItem().copy();

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
            var recipe = RecipeSerializer.SHAPED_RECIPE.fromJson(recipeId, json);
            return new JetpackUpgradeRecipe(recipeId, recipe.getGroup(), recipe.getRecipeWidth(), recipe.getRecipeHeight(), recipe.getIngredients(), recipe.getResultItem());
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

            return new JetpackUpgradeRecipe(recipeId, group, width, height, inputs, output);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, JetpackUpgradeRecipe recipe) {
            buffer.writeUtf(recipe.getGroup());
            buffer.writeVarInt(recipe.getRecipeWidth());
            buffer.writeVarInt(recipe.getRecipeHeight());

            for (var ingredient : recipe.getIngredients()) {
                ingredient.toNetwork(buffer);
            }

            buffer.writeItem(recipe.getResultItem());
        }
    }
}
