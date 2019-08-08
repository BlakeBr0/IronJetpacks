package com.blakebr0.ironjetpacks.handler;

import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Map;

public class TemporarilyFixModelsHandler {
    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event) {
        Map<ResourceLocation, IBakedModel> registry = event.getModelRegistry();

        IBakedModel cell = registry.get(new ModelResourceLocation("ironjetpacks:iron_cell", "inventory"));
        IBakedModel capacitor = registry.get(new ModelResourceLocation("ironjetpacks:iron_capacitor", "inventory"));
        IBakedModel thruster = registry.get(new ModelResourceLocation("ironjetpacks:iron_thruster", "inventory"));
        IBakedModel jetpack = registry.get(new ModelResourceLocation("ironjetpacks:iron_jetpack", "inventory"));

        registry.forEach((location, model) -> {
            if (location.getNamespace().equals("ironjetpacks")) {
                if (location.getPath().endsWith("_cell")) {
                    registry.replace(location, cell);
                }

                if (location.getPath().endsWith("_capacitor")) {
                    registry.replace(location, capacitor);
                }

                if (location.getPath().endsWith("_thruster")) {
                    registry.replace(location, thruster);
                }

                if (location.getPath().endsWith("_jetpack")) {
                    registry.replace(location, jetpack);
                }
            }
        });
    }
}
