package com.blakebr0.ironjetpacks.lib;

import com.blakebr0.ironjetpacks.IronJetpacks;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class ModTags {
    public static final TagKey<Item> NONE = TagKey.create(Registries.ITEM, IronJetpacks.resource("none"));
}
