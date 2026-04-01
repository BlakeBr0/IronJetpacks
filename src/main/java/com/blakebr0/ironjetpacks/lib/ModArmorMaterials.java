package com.blakebr0.ironjetpacks.lib;

import com.blakebr0.ironjetpacks.client.ModEquipmentAssets;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;

import java.util.EnumMap;

public final class ModArmorMaterials {
    public static final ArmorMaterial JETPACK = new ArmorMaterial(
            0,
            Util.make(new EnumMap<>(ArmorType.class), map -> {
                map.put(ArmorType.CHESTPLATE, 0);
            }),
            10, SoundEvents.ARMOR_EQUIP_GENERIC,
            0.0F, 0.0F,
            ModTags.NONE,
            ModEquipmentAssets.JETPACK
    );
}
