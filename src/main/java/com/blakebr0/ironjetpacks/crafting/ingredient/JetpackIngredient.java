package com.blakebr0.ironjetpacks.crafting.ingredient;

import com.blakebr0.ironjetpacks.init.ModRecipeSerializers;
import com.blakebr0.ironjetpacks.item.JetpackItem;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry;
import com.blakebr0.ironjetpacks.util.JetpackUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.common.crafting.NBTIngredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class JetpackIngredient extends Ingredient {
    public static final List<JetpackItem> ALL_JETPACKS = new ArrayList<>();
    private final int tier;
    private ItemStack[] stacks;
    private IntList stacksPacked;

    public JetpackIngredient(int tier) {
        super(Stream.of());
        this.tier = tier;
    }

    @Override
    public ItemStack[] getItems() {
        if (this.stacks == null) {
            this.initMatchingStacks();
        }

        return this.stacks;
    }

    @Override
    public IntList getStackingIds() {
        if (this.stacksPacked == null) {
            if (this.stacks == null) {
                this.initMatchingStacks();
            }

            this.stacksPacked = new IntArrayList(this.stacks.length);
            Arrays.stream(this.stacks).forEach(s -> this.stacksPacked.add(StackedContents.getStackingIndex(s)));
            this.stacksPacked.sort(IntComparators.NATURAL_COMPARATOR);
        }

        return this.stacksPacked;
    }

    @Override
    public boolean test(ItemStack stack) {
        if (stack == null) {
            return false;
        } else if (JetpackRegistry.getInstance().getJetpacks().stream().noneMatch(j -> j.getTier() == this.tier)) {
            return stack.isEmpty();
        } else {
            if (this.stacks == null) {
                this.initMatchingStacks();
            }

            for (var itemstack : this.stacks) {
                if (itemstack.getItem() == stack.getItem()) {
                    return true;
                }
            }

            return false;
        }
    }

    @Override
    public boolean isEmpty() {
        return JetpackRegistry.getInstance().getJetpacks()
                .stream()
                .noneMatch(j -> j.getTier() == this.tier)
                && (this.stacks == null || this.stacks.length == 0)
                && (this.stacksPacked == null || this.stacksPacked.isEmpty());
    }

    @Override
    public JsonElement toJson() {
        var json = new JsonArray();

        JetpackRegistry.getInstance().getJetpacks()
                .stream()
                .filter(j -> j.getTier() == this.tier)
                .forEach(jetpack -> {
                    var obj = new JsonObject();
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

    private void initMatchingStacks() {
        this.stacks = JetpackRegistry.getInstance().getJetpacks()
                .stream()
                .filter(j -> j.getTier() == this.tier)
                .map(JetpackUtils::getItemForJetpack)
                .toArray(ItemStack[]::new);
    }

    public static class Serializer implements IIngredientSerializer<JetpackIngredient> {
        @Override
        public JetpackIngredient parse(FriendlyByteBuf buffer) {
            return new JetpackIngredient(buffer.readInt());
        }

        @Override
        public JetpackIngredient parse(JsonObject json) {
            return new JetpackIngredient(json.get("tier").getAsInt());
        }

        @Override
        public void write(FriendlyByteBuf buffer, JetpackIngredient ingredient) {
            buffer.writeInt(ingredient.tier);
        }
    }
}
