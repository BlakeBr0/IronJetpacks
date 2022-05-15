package com.blakebr0.ironjetpacks.client;

import com.blakebr0.ironjetpacks.client.layer.JetpackRenderLayer;
import com.blakebr0.ironjetpacks.client.model.JetpackModel;
import com.blakebr0.ironjetpacks.config.ModConfigs;
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

    @SubscribeEvent
    public void onAddLayers(EntityRenderersEvent.AddLayers event) {
        if (ModConfigs.isCuriosEnabled()) {
            addJetpackLayerToPlayerSkin(event, "default");
            addJetpackLayerToPlayerSkin(event, "slim");
        }
    }

    @SuppressWarnings("unchecked rawtypes")
    private static void addJetpackLayerToPlayerSkin(EntityRenderersEvent.AddLayers event, String skinName) {
        var renderer = event.getSkin(skinName);

        if (renderer != null) {
            renderer.addLayer(new JetpackRenderLayer(renderer));
        }
    }
}
