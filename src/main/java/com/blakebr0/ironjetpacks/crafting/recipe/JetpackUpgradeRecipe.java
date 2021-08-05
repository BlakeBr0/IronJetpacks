package com.blakebr0.ironjetpacks.crafting.recipe;

import com.blakebr0.ironjetpacks.init.ModRecipeSerializers;
import com.blakebr0.ironjetpacks.item.JetpackItem;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class JetpackUpgradeRecipe extends ShapedRecipe {
    public JetpackUpgradeRecipe(ResourceLocation id, String group, int recipeWidth, int recipeHeight, NonNullList<Ingredient> inputs, ItemStack output) {
        super(id, group, recipeWidth, recipeHeight, inputs, output);
    }

    @Override
    public ItemStack assemble(CraftingContainer inv) {
        ItemStack jetpack = inv.getItem(4);
        ItemStack result = this.getResultItem().copy();

        if (!jetpack.isEmpty() && jetpack.getItem() instanceof JetpackItem) {
            CompoundTag tag = jetpack.getTag();
            if (tag != null) {
                result.setTag(tag);
                return result;
            }
        }

        return result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.CRAFTING_JETPACK_UPGRADE;
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<JetpackUpgradeRecipe> {
        @Override
        public JetpackUpgradeRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            ShapedRecipe recipe = RecipeSerializer.SHAPED_RECIPE.fromJson(recipeId, json);
            return new JetpackUpgradeRecipe(recipeId, recipe.getGroup(), recipe.getRecipeWidth(), recipe.getRecipeHeight(), recipe.getIngredients(), recipe.getResultItem());
        }

        @Override
        public JetpackUpgradeRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            int i = buffer.readVarInt();
            int j = buffer.readVarInt();
            String s = buffer.readUtf(32767);
            NonNullList<Ingredient> inputs = NonNullList.withSize(i * j, Ingredient.EMPTY);

            for (int k = 0; k < inputs.size(); k++) {
                inputs.set(k, Ingredient.fromNetwork(buffer));
            }

            ItemStack output = buffer.readItem();
            return new JetpackUpgradeRecipe(recipeId, s, i, j, inputs, output);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, JetpackUpgradeRecipe recipe) {
            buffer.writeVarInt(recipe.getRecipeWidth());
            buffer.writeVarInt(recipe.getRecipeHeight());
            buffer.writeUtf(recipe.getGroup());

            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredient.toNetwork(buffer);
            }

            buffer.writeItem(recipe.getResultItem());
        }
    }
}
