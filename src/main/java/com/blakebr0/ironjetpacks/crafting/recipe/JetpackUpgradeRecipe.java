package com.blakebr0.ironjetpacks.crafting.recipe;

import com.blakebr0.ironjetpacks.crafting.ModRecipeSerializers;
import com.blakebr0.ironjetpacks.item.JetpackItem;
import com.google.gson.JsonObject;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class JetpackUpgradeRecipe extends ShapedRecipe {
    public JetpackUpgradeRecipe(ResourceLocation id, String group, int recipeWidth, int recipeHeight, NonNullList<Ingredient> inputs, ItemStack output) {
        super(id, group, recipeWidth, recipeHeight, inputs, output);
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        ItemStack jetpack = inv.getStackInSlot(4);
        if (!jetpack.isEmpty() && jetpack.getItem() instanceof JetpackItem) {
            CompoundNBT tag = jetpack.getTag();
            ItemStack result = this.getRecipeOutput().copy();
            if (tag != null) {
                result.setTag(tag);
                return result;
            }
        }
        return this.getRecipeOutput().copy();
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.CRAFTING_JETPACK_UPGRADE;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<JetpackUpgradeRecipe> {
        @Override
        public JetpackUpgradeRecipe read(ResourceLocation recipeId, JsonObject json) {
            ShapedRecipe recipe = IRecipeSerializer.CRAFTING_SHAPED.read(recipeId, json);
            return new JetpackUpgradeRecipe(recipeId, recipe.getGroup(), recipe.getRecipeWidth(), recipe.getRecipeHeight(), recipe.getIngredients(), recipe.getRecipeOutput());
        }

        @Override
        public JetpackUpgradeRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            int i = buffer.readVarInt();
            int j = buffer.readVarInt();
            String s = buffer.readString(32767);
            NonNullList<Ingredient> inputs = NonNullList.withSize(i * j, Ingredient.EMPTY);

            for (int k = 0; k < inputs.size(); k++) {
                inputs.set(k, Ingredient.read(buffer));
            }

            ItemStack output = buffer.readItemStack();
            return new JetpackUpgradeRecipe(recipeId, s, i, j, inputs, output);
        }

        @Override
        public void write(PacketBuffer buffer, JetpackUpgradeRecipe recipe) {
            buffer.writeVarInt(recipe.getRecipeWidth());
            buffer.writeVarInt(recipe.getRecipeHeight());
            buffer.writeString(recipe.getGroup());

            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredient.write(buffer);
            }

            buffer.writeItemStack(recipe.getRecipeOutput());
        }
    }
}
