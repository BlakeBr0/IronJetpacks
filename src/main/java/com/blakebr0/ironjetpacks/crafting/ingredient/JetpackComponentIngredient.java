package com.blakebr0.ironjetpacks.crafting.ingredient;

import com.blakebr0.ironjetpacks.init.ModItems;
import com.blakebr0.ironjetpacks.init.ModRecipeSerializers;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.IIngredientSerializer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class JetpackComponentIngredient extends Ingredient {
    private final String jetpackId;
    private final ComponentType type;

    public JetpackComponentIngredient(String jetpackId, ComponentType type) {
        super(Stream.empty());
        this.jetpackId = jetpackId;
        this.type = type;
    }

    public JetpackComponentIngredient(String jetpackId, ComponentType type, Stream<ItemValue> itemList) {
        super(itemList);
        this.jetpackId = jetpackId;
        this.type = type;
    }

    @Override
    public boolean test(ItemStack input) {
        if (input == null)
            return false;

        if (!super.test(input))
            return false;

        return Arrays.stream(this.getItems()).anyMatch(s -> s.getDamageValue() == input.getDamageValue() && (!s.hasTag() || s.areShareTagsEqual(input)));
    }

    @Override
    public JsonElement toJson() {
        var json = new JsonObject();

        json.addProperty("type", "ironjetpacks:jetpack_component");
        json.addProperty("component", this.type.name);
        json.addProperty("crop", this.jetpackId);

        return json;
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return ModRecipeSerializers.JETPACK_COMPONENT_INGREDIENT;
    }

    public static class Serializer implements IIngredientSerializer<JetpackComponentIngredient> {
        @Override
        public JetpackComponentIngredient parse(FriendlyByteBuf buffer) {
            var jetpackId = buffer.readUtf();
            var type = ComponentType.fromName(buffer.readUtf());

            var itemList = Stream.generate(buffer::readItem)
                    .limit(buffer.readVarInt())
                    .map(ItemValue::new);

            return new JetpackComponentIngredient(jetpackId, type, itemList);
        }

        @Override
        public JetpackComponentIngredient parse(JsonObject json) {
            var jetpackId = GsonHelper.getAsString(json, "jetpack");
            var typeName = GsonHelper.getAsString(json, "component");
            var type = ComponentType.fromName(typeName);
            var stack = switch (type) {
                case CELL -> new ItemStack(ModItems.CELL.get());
                case THRUSTER -> new ItemStack(ModItems.THRUSTER.get());
                case CAPACITOR -> new ItemStack(ModItems.CAPACITOR.get());
                case JETPACK -> new ItemStack(ModItems.JETPACK.get());
            };

            var nbt = new CompoundTag();
            nbt.putString("Id", jetpackId);

            stack.setTag(nbt);

            return new JetpackComponentIngredient(jetpackId, type, Stream.of(new ItemValue(stack)));
        }

        @Override
        public void write(FriendlyByteBuf buffer, JetpackComponentIngredient ingredient) {
            buffer.writeUtf(ingredient.jetpackId);
            buffer.writeUtf(ingredient.type.name);

            var items = ingredient.getItems();

            buffer.writeVarInt(items.length);

            for (var item : items) {
                buffer.writeItem(item);
            }
        }
    }

    public enum ComponentType {
        CELL("cell"),
        THRUSTER("thruster"),
        CAPACITOR("capacitor"),
        JETPACK("jetpack");

        private static final Map<String, ComponentType> LOOKUP = new HashMap<>();
        public final String name;

        static {
            for (var value : values()) {
                LOOKUP.put(value.name, value);
            }
        }

        ComponentType(String name) {
            this.name = name;
        }

        public static ComponentType fromName(String name) {
            return LOOKUP.get(name);
        }
    }
}
