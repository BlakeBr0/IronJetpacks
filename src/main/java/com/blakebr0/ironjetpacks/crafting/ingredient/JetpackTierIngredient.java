package com.blakebr0.ironjetpacks.crafting.ingredient;

import com.blakebr0.ironjetpacks.init.ModRecipeSerializers;
import com.blakebr0.ironjetpacks.registry.Jetpack;
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
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.common.crafting.StrictNBTIngredient;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.stream.Stream;

public class JetpackTierIngredient extends Ingredient {
    private final int tier;
    private ItemStack[] stacks;
    private IntList stacksPacked;

    public JetpackTierIngredient(int tier) {
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
        } else if (!JetpackRegistry.getInstance().getAllTiers().contains(this.tier)) {
            return stack.isEmpty();
        } else {
            if (this.stacks == null) {
                this.initMatchingStacks();
            }

            for (var itemstack : this.stacks) {
                if (itemstack.getItem() == stack.getItem()) {
                    var jetpack = JetpackUtils.getJetpack(stack);
                    return jetpack != Jetpack.UNDEFINED && jetpack.tier == this.tier;
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
                    var stack = JetpackUtils.getItemForJetpack(jetpack);

                    obj.addProperty("type", CraftingHelper.getID(StrictNBTIngredient.Serializer.INSTANCE).toString());
                    obj.addProperty("item", ForgeRegistries.ITEMS.getKey(stack.getItem()).toString());
                    obj.addProperty("count", stack.getCount());
                    if (stack.hasTag())
                        obj.addProperty("nbt", stack.getTag().toString());

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
        return ModRecipeSerializers.JETPACK_TIER_INGREDIENT;
    }

    private void initMatchingStacks() {
        this.stacks = JetpackRegistry.getInstance().getJetpacks()
                .stream()
                .filter(j -> j.getTier() == this.tier)
                .map(JetpackUtils::getItemForJetpack)
                .toArray(ItemStack[]::new);
    }

    public static class Serializer implements IIngredientSerializer<JetpackTierIngredient> {
        @Override
        public JetpackTierIngredient parse(FriendlyByteBuf buffer) {
            return new JetpackTierIngredient(buffer.readInt());
        }

        @Override
        public JetpackTierIngredient parse(JsonObject json) {
            return new JetpackTierIngredient(json.get("tier").getAsInt());
        }

        @Override
        public void write(FriendlyByteBuf buffer, JetpackTierIngredient ingredient) {
            buffer.writeInt(ingredient.tier);
        }
    }
}
