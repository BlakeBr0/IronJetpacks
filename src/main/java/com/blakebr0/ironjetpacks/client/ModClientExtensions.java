package com.blakebr0.ironjetpacks.client;

import com.blakebr0.ironjetpacks.client.extensions.JetpackClientItemExtensions;
import com.blakebr0.ironjetpacks.init.ModItems;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

public final class ModClientExtensions {
    @SubscribeEvent
    public void onClientTick(RegisterClientExtensionsEvent event) {
        event.registerItem(JetpackClientItemExtensions.INSTANCE, ModItems.JETPACK.get());
    }
}
