package com.blakebr0.ironjetpacks.init;

import com.blakebr0.ironjetpacks.IronJetpacks;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;

public final class ModArmorMaterials {
    public static final DeferredRegister<ArmorMaterial> REGISTRY = DeferredRegister.create(Registries.ARMOR_MATERIAL, IronJetpacks.MOD_ID);

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> JETPACK = REGISTRY.register("jetpack", () -> new ArmorMaterial(
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 0);
                map.put(ArmorItem.Type.LEGGINGS, 0);
                map.put(ArmorItem.Type.CHESTPLATE, 0);
                map.put(ArmorItem.Type.HELMET, 0);
            }),
            0, SoundEvents.ARMOR_EQUIP_GENERIC,
            () -> Ingredient.EMPTY,
            List.of(
                    new ArmorMaterial.Layer(IronJetpacks.resource("jetpack"), "", true),
                    new ArmorMaterial.Layer(IronJetpacks.resource("jetpack"), "_overlay", false)
            ),
            0.0F, 0.0F
    ));
}
