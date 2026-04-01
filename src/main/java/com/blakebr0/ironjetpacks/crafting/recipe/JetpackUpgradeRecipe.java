package com.blakebr0.ironjetpacks.crafting.recipe;

import com.blakebr0.ironjetpacks.init.ModDataComponentTypes;
import com.blakebr0.ironjetpacks.init.ModItems;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.NormalCraftingRecipe;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;

import java.util.List;

public class JetpackUpgradeRecipe extends NormalCraftingRecipe {
    public static final MapCodec<JetpackUpgradeRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(builder ->
            builder.group(
                    CommonInfo.MAP_CODEC.forGetter(o -> o.commonInfo),
                    CraftingBookInfo.MAP_CODEC.forGetter(o -> o.bookInfo),
                    ShapedRecipePattern.MAP_CODEC.forGetter(recipe -> recipe.pattern),
                    ItemStackTemplate.CODEC.fieldOf("result").forGetter(recipe -> recipe.result)
            ).apply(builder, JetpackUpgradeRecipe::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, JetpackUpgradeRecipe> STREAM_CODEC = StreamCodec.of(
            JetpackUpgradeRecipe::toNetwork, JetpackUpgradeRecipe::fromNetwork
    );
    public static final RecipeSerializer<JetpackUpgradeRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    private final ShapedRecipePattern pattern;
    private final ItemStackTemplate result;

    public JetpackUpgradeRecipe(Recipe.CommonInfo commonInfo, CraftingRecipe.CraftingBookInfo bookInfo, ShapedRecipePattern pattern, ItemStackTemplate result) {
        super(commonInfo, bookInfo);
        this.pattern = pattern;
        this.result = result;
    }

    @Override
    public boolean matches(CraftingInput inventory, Level level) {
        return this.pattern.matches(inventory);
    }

    @Override
    public ItemStack assemble(CraftingInput inventory) {
        var stack = inventory.getItem(4);
        var result = this.result.create();

        if (!stack.isEmpty() && stack.is(ModItems.JETPACK)) {
            result.applyComponents(stack.getComponents());
            result.set(ModDataComponentTypes.JETPACK_ID, this.result.get(ModDataComponentTypes.JETPACK_ID));
        }

        return result;
    }

    @Override
    public RecipeSerializer<JetpackUpgradeRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public List<RecipeDisplay> display() {
        return List.of(
                new ShapedCraftingRecipeDisplay(
                        this.pattern.width(),
                        this.pattern.height(),
                        this.pattern.ingredients().stream().map(e -> e.map(Ingredient::display).orElse(SlotDisplay.Empty.INSTANCE)).toList(),
                        new SlotDisplay.ItemStackSlotDisplay(this.result),
                        new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)
                )
        );
    }

    @Override
    public PlacementInfo createPlacementInfo() {
        return PlacementInfo.createFromOptionals(this.pattern.ingredients());
    }

    private static JetpackUpgradeRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
        var commonInfo = CommonInfo.STREAM_CODEC.decode(buffer);
        var bookInfo = CraftingBookInfo.STREAM_CODEC.decode(buffer);
        var pattern = ShapedRecipePattern.STREAM_CODEC.decode(buffer);
        var result = ItemStackTemplate.STREAM_CODEC.decode(buffer);

        return new JetpackUpgradeRecipe(commonInfo, bookInfo, pattern, result);
    }

    private static void toNetwork(RegistryFriendlyByteBuf buffer, JetpackUpgradeRecipe recipe) {
        CommonInfo.STREAM_CODEC.encode(buffer, recipe.commonInfo);
        CraftingBookInfo.STREAM_CODEC.encode(buffer, recipe.bookInfo);
        ShapedRecipePattern.STREAM_CODEC.encode(buffer, recipe.pattern);
        ItemStackTemplate.STREAM_CODEC.encode(buffer, recipe.result);
    }
}
