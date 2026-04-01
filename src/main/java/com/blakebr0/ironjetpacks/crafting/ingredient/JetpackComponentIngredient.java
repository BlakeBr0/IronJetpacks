package com.blakebr0.ironjetpacks.crafting.ingredient;

import com.blakebr0.ironjetpacks.init.ModDataComponentTypes;
import com.blakebr0.ironjetpacks.init.ModIngredientTypes;
import com.blakebr0.ironjetpacks.init.ModItems;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class JetpackComponentIngredient implements ICustomIngredient {
    public static final MapCodec<JetpackComponentIngredient> CODEC = RecordCodecBuilder.mapCodec(builder ->
            builder.group(
                    Identifier.CODEC.fieldOf("jetpack").forGetter(ingredient -> ingredient.jetpack),
                    ComponentType.CODEC.fieldOf("component").forGetter(ingredient -> ingredient.type)
            ).apply(builder, JetpackComponentIngredient::new)
    );

    private final Identifier jetpack;
    private final ComponentType type;
    private final ItemStack stack;

    public JetpackComponentIngredient(Identifier jetpack, ComponentType type) {
        this.jetpack = jetpack;
        this.type = type;

        this.stack = switch (type) {
            case CELL -> new ItemStack(ModItems.CELL.get());
            case THRUSTER -> new ItemStack(ModItems.THRUSTER.get());
            case CAPACITOR -> new ItemStack(ModItems.CAPACITOR.get());
            case JETPACK -> new ItemStack(ModItems.JETPACK.get());
        };

        this.stack.set(ModDataComponentTypes.JETPACK_ID, this.jetpack);
    }

    @Override
    public boolean test(@Nullable ItemStack input) {
        if (input == null)
            return false;

        var jetpackID = input.get(ModDataComponentTypes.JETPACK_ID.get());
        if (jetpackID == null)
            return false;

        return ItemStack.isSameItem(this.stack, input) && jetpackID.equals(this.stack.get(ModDataComponentTypes.JETPACK_ID.get()));
    }

    @Override
    public Stream<Holder<Item>> items() {
        return Stream.of(this.stack.typeHolder());
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
