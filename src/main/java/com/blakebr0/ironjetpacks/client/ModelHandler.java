package com.blakebr0.ironjetpacks.client;

import com.blakebr0.ironjetpacks.client.model.JetpackModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ModelHandler {
    public static final ModelLayerLocation JETPACK_LAYER = new ModelLayerLocation(new ResourceLocation("minecraft:player"), "ironjetpacks:jetpack");

    @SubscribeEvent
    public void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(JETPACK_LAYER, JetpackModel::createBodyLayer);
    }
}
