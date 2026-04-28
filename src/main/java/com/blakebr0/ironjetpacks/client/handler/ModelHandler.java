package com.blakebr0.ironjetpacks.client.handler;

import com.blakebr0.ironjetpacks.client.model.JetpackModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public final class ModelHandler {
    public static final ModelLayerLocation JETPACK_LAYER = new ModelLayerLocation(Identifier.parse("minecraft:player"), "ironjetpacks:jetpack");

    @SubscribeEvent
    public void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(JETPACK_LAYER, JetpackModel::createArmorLayer);
    }
}
