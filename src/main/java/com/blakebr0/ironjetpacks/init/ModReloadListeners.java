package com.blakebr0.ironjetpacks.init;

import com.blakebr0.ironjetpacks.registry.JetpackRegistry;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class ModReloadListeners implements ResourceManagerReloadListener {
    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        JetpackRegistry.getInstance().onResourceManagerReload(manager);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(this);
    }
}
