package com.blakebr0.ironjetpacks.crafting.ingredient;

import com.blakebr0.ironjetpacks.init.ModDataComponentTypes;
import com.blakebr0.ironjetpacks.init.ModIngredientTypes;
import com.blakebr0.ironjetpacks.init.ModItems;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class JetpackComponentIngredient implements ICustomIngredient {
    public static final MapCodec<JetpackComponentIngredient> CODEC = RecordCodecBuilder.mapCodec(builder ->
            builder.group(
                    ResourceLocation.CODEC.fieldOf("jetpack").forGetter(ingredient -> ingredient.jetpack),
                    ComponentType.CODEC.fieldOf("component").forGetter(ingredient -> ingredient.type)
            ).apply(builder, JetpackComponentIngredient::new)
    );

    private final ResourceLocation jetpack;
    private final ComponentType type;
    private ItemStack[] stacks;

    public JetpackComponentIngredient(ResourceLocation jetpack, ComponentType type) {
        this.jetpack = jetpack;
        this.type = type;
    }

    @Override
    public boolean test(@Nullable ItemStack input) {
        if (input == null)
            return false;

        return this.getItems().anyMatch(s -> ItemStack.isSameItemSameComponents(s, input));
    }

    @Override
    public Stream<ItemStack> getItems() {
        if (this.stacks == null) {
            var stack = switch (type) {
                case CELL -> new ItemStack(ModItems.CELL.get());
                case THRUSTER -> new ItemStack(ModItems.THRUSTER.get());
                case CAPACITOR -> new ItemStack(ModItems.CAPACITOR.get());
                case JETPACK -> new ItemStack(ModItems.JETPACK.get());
            };

            stack.set(ModDataComponentTypes.JETPACK_ID, this.jetpack);

            this.stacks = new ItemStack[] { stack };
        }

        return Stream.of(this.stacks);
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public IngredientType<?> getType() {
        return ModIngredientTypes.JETPACK_COMPONENT_INGREDIENT.get();
    }

    public enum ComponentType implements StringRepresentable {
        CELL("cell"),
        THRUSTER("thruster"),
        CAPACITOR("capacitor"),
        JETPACK("jetpack");

        public static final Codec<ComponentType> CODEC = StringRepresentable.fromEnum(ComponentType::values);

        public final String name;

        ComponentType(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}
