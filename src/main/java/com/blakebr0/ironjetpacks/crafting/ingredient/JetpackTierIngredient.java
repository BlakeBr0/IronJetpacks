package com.blakebr0.ironjetpacks.crafting.ingredient;

import com.blakebr0.ironjetpacks.init.ModIngredientTypes;
import com.blakebr0.ironjetpacks.registry.Jetpack;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry;
import com.blakebr0.ironjetpacks.util.JetpackUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class JetpackTierIngredient implements ICustomIngredient {
    public static final MapCodec<JetpackTierIngredient> CODEC = RecordCodecBuilder.mapCodec(builder ->
            builder.group(
                    Codec.INT.fieldOf("tier").forGetter(ingredient -> ingredient.tier)
            ).apply(builder, JetpackTierIngredient::new)
    );

    private final int tier;
    private ItemStack[] stacks;

    public JetpackTierIngredient(int tier) {
        this.tier = tier;
    }

    @Override
    public Stream<ItemStack> getItems() {
        if (this.stacks == null) {
            this.initMatchingStacks();
        }

        return Stream.of(this.stacks);
    }

    @Override
    public boolean test(@Nullable ItemStack stack) {
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
    public boolean isSimple() {
        return false;
    }

    @Override
    public IngredientType<?> getType() {
        return ModIngredientTypes.JETPACK_TIER_INGREDIENT.get();
    }

    private void initMatchingStacks() {
        this.stacks = JetpackRegistry.getInstance().getJetpacks()
                .stream()
                .filter(j -> j.getTier() == this.tier)
                .map(JetpackUtils::getItemForJetpack)
                .toArray(ItemStack[]::new);
    }

    public static Ingredient of(int tier) {
        return new JetpackTierIngredient(tier).toVanilla();
    }
}
