package com.blakebr0.ironjetpacks.client;

import com.blakebr0.ironjetpacks.IronJetpacks;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

public final class ModEquipmentAssets {
    public static final ResourceKey<EquipmentAsset> JETPACK = ResourceKey.create(EquipmentAssets.ROOT_ID, IronJetpacks.resource("jetpack"));
}
