package com.blakebr0.ironjetpacks.client;

import com.blakebr0.ironjetpacks.client.layer.JetpackRenderLayer;
import com.blakebr0.ironjetpacks.client.model.JetpackModel;
import com.blakebr0.ironjetpacks.config.ModConfigs;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public class ModelHandler {
    public static final ModelLayerLocation JETPACK_LAYER = new ModelLayerLocation(Identifier.parse("minecraft:player"), "ironjetpacks:jetpack");

    @SubscribeEvent
    public void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(JETPACK_LAYER, JetpackModel::createBodyLayer);
    }

    @SubscribeEvent
    public void onAddLayers(EntityRenderersEvent.AddLayers event) {
//        TODO curio layer
//        if (ModConfigs.isCuriosInstalled()) {
//            addLayerToPlayerSkin(event, PlayerSkin.Model.WIDE);
//            addLayerToPlayerSkin(event, PlayerSkin.Model.SLIM);
//        }
    }

//    @SuppressWarnings("unchecked rawtypes")
//    private static void addLayerToPlayerSkin(EntityRenderersEvent.AddLayers event, PlayerSkin.Model skin) {
//        var renderer = event.getSkin(skin);
//
//        if (renderer instanceof LivingEntityRenderer<?,?> livingEntityRenderer) {
//            livingEntityRenderer.addLayer(new JetpackRenderLayer(livingEntityRenderer));
//        }
//    }
}
