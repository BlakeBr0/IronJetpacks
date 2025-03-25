package com.blakebr0.ironjetpacks.crafting.recipe;

import com.blakebr0.ironjetpacks.init.ModDataComponentTypes;
import com.blakebr0.ironjetpacks.init.ModItems;
import com.blakebr0.ironjetpacks.init.ModRecipeSerializers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;

public class JetpackUpgradeRecipe extends ShapedRecipe {
    private final ItemStack result;

    public JetpackUpgradeRecipe(String group, ShapedRecipePattern pattern, ItemStack result, boolean showNotification) {
        super(group, CraftingBookCategory.EQUIPMENT, pattern, result, showNotification);
        this.result = result;
    }

    @Override
    public ItemStack assemble(CraftingInput inventory, HolderLookup.Provider access) {
        var stack = inventory.getItem(4);
        var result = this.result.copy();

        if (!stack.isEmpty() && stack.is(ModItems.JETPACK)) {
            result.applyComponents(stack.getComponents());
            result.set(ModDataComponentTypes.JETPACK_ID, this.result.get(ModDataComponentTypes.JETPACK_ID));
        }

        return result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.CRAFTING_JETPACK_UPGRADE.get();
    }

    public static class Serializer implements RecipeSerializer<JetpackUpgradeRecipe> {
        public static final MapCodec<JetpackUpgradeRecipe> CODEC = RecordCodecBuilder.mapCodec(builder ->
                builder.group(
                        Codec.STRING.optionalFieldOf("group", "").forGetter(ShapedRecipe::getGroup),
                        ShapedRecipePattern.MAP_CODEC.forGetter(recipe -> recipe.pattern),
                        ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
                        Codec.BOOL.optionalFieldOf("show_notification", Boolean.TRUE).forGetter(ShapedRecipe::showNotification)
                ).apply(builder, JetpackUpgradeRecipe::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, JetpackUpgradeRecipe> STREAM_CODEC = StreamCodec.of(
                JetpackUpgradeRecipe.Serializer::toNetwork, JetpackUpgradeRecipe.Serializer::fromNetwork
        );

        @Override
        public MapCodec<JetpackUpgradeRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, JetpackUpgradeRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static JetpackUpgradeRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            var group = buffer.readUtf();
            var pattern = ShapedRecipePattern.STREAM_CODEC.decode(buffer);
            var result = ItemStack.STREAM_CODEC.decode(buffer);
            var showNotification = buffer.readBoolean();
            return new JetpackUpgradeRecipe(group, pattern, result, showNotification);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, JetpackUpgradeRecipe recipe) {
            buffer.writeUtf(recipe.getGroup());
            ShapedRecipePattern.STREAM_CODEC.encode(buffer, recipe.pattern);
            ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
            buffer.writeBoolean(recipe.showNotification());
        }
    }
}
