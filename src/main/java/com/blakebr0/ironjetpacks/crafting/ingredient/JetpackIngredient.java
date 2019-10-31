package com.blakebr0.ironjetpacks.crafting.ingredient;

import com.blakebr0.ironjetpacks.crafting.ModRecipeSerializers;
import com.blakebr0.ironjetpacks.item.JetpackItem;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.crafting.IIngredientSerializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class JetpackIngredient extends Ingredient {
    public static final List<JetpackItem> ALL_JETPACKS = new ArrayList<>();
    private ItemStack[] stacks;
    private IntList stacksPacked;
    private int tier;

    public JetpackIngredient(int tier) {
        super(Stream.of());
        this.tier = tier;
    }

    @Override
    public ItemStack[] getMatchingStacks() {
        if (this.stacks == null)
            this.stacks = ALL_JETPACKS.stream().filter(j -> j.getJetpack().tier == this.tier).map(ItemStack::new).toArray(ItemStack[]::new);

        return this.stacks;
    }

    @Override
    public IntList getValidItemStacksPacked() {
        if (this.stacksPacked == null) {
            if (this.stacks == null)
                this.stacks = ALL_JETPACKS.stream().filter(j -> j.getJetpack().tier == this.tier).map(ItemStack::new).toArray(ItemStack[]::new);

            this.stacksPacked = new IntArrayList(this.stacks.length);
            Arrays.stream(this.stacks).forEach(s -> this.stacksPacked.add(RecipeItemHelper.pack(s)));
            this.stacksPacked.sort(IntComparators.NATURAL_COMPARATOR);
        }

        return this.stacksPacked;
    }

    @Override
    public boolean test(ItemStack stack) {
        if (stack == null) {
            return false;
        } else if (ALL_JETPACKS.stream().noneMatch(j -> j.getJetpack().tier == this.tier)) {
            return stack.isEmpty();
        } else {
            if (this.stacks == null)
                this.stacks = ALL_JETPACKS.stream().filter(j -> j.getJetpack().tier == this.tier).map(ItemStack::new).toArray(ItemStack[]::new);

            for (ItemStack itemstack : this.stacks) {
                if (itemstack.getItem() == stack.getItem()) {
                    return true;
                }
            }

            return false;
        }
    }

    @Override
    public boolean hasNoMatchingItems() {
        return ALL_JETPACKS.stream().noneMatch(j -> j.getJetpack().tier == this.tier) && (this.stacks == null || this.stacks.length == 0) && (this.stacksPacked == null || this.stacksPacked.isEmpty());
    }

    @Override
    public JsonElement serialize() {
        JsonArray json = new JsonArray();
        ALL_JETPACKS.stream().filter(j -> j.getJetpack().tier == this.tier).filter(h -> h.getRegistryName() != null).forEach(h -> {
            JsonObject obj = new JsonObject();
            obj.addProperty("item", h.getRegistryName().toString());
            json.add(obj);
        });

        return json;
    }

    @Override
    protected void invalidate() {
        this.stacks = null;
        this.stacksPacked = null;
    }

    @Override
    public boolean isSimple() {
        return true;
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return ModRecipeSerializers.JETPACK_INGREDIENT;
    }

    public static class Serializer implements IIngredientSerializer<JetpackIngredient> {
        @Override
        public JetpackIngredient parse(PacketBuffer buffer) {
            return new JetpackIngredient(buffer.readInt());
        }

        @Override
        public JetpackIngredient parse(JsonObject json) {
            return new JetpackIngredient(json.get("tier").getAsInt());
        }

        @Override
        public void write(PacketBuffer buffer, JetpackIngredient ingredient) {
            buffer.writeInt(ingredient.tier);
        }
    }
}
