package com.blakebr0.ironjetpacks.client;

import com.blakebr0.ironjetpacks.IronJetpacks;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Map;

public class ModelHandler {
    @SubscribeEvent
    public void onRegisterModels(ModelRegistryEvent event) {
        ModelLoader.addSpecialModel(new ResourceLocation(IronJetpacks.MOD_ID, "item/cell"));
        ModelLoader.addSpecialModel(new ResourceLocation(IronJetpacks.MOD_ID, "item/capacitor"));
        ModelLoader.addSpecialModel(new ResourceLocation(IronJetpacks.MOD_ID, "item/thruster"));
        ModelLoader.addSpecialModel(new ResourceLocation(IronJetpacks.MOD_ID, "item/jetpack"));
    }

    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event) {
        Map<ResourceLocation, IBakedModel> registry = event.getModelRegistry();

        IBakedModel cell = registry.get(new ResourceLocation(IronJetpacks.MOD_ID, "item/cell"));
        IBakedModel capacitor = registry.get(new ResourceLocation(IronJetpacks.MOD_ID, "item/capacitor"));
        IBakedModel thruster = registry.get(new ResourceLocation(IronJetpacks.MOD_ID, "item/thruster"));
        IBakedModel jetpack = registry.get(new ResourceLocation(IronJetpacks.MOD_ID, "item/jetpack"));

        registry.forEach((location, model) -> {
            if (location.getNamespace().equals("ironjetpacks")) {
                if (location.getPath().endsWith("_cell"))
                    registry.replace(location, cell);

                if (location.getPath().endsWith("_capacitor"))
                    registry.replace(location, capacitor);

                if (location.getPath().endsWith("_thruster"))
                    registry.replace(location, thruster);

                if (location.getPath().endsWith("_jetpack"))
                    registry.replace(location, jetpack);
            }
        });
    }

    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent.Pre event) {
        if (event.getMap().getBasePath().equals("textures")) {
            event.addSprite(new ResourceLocation(IronJetpacks.MOD_ID, "items/cell"));
            event.addSprite(new ResourceLocation(IronJetpacks.MOD_ID, "items/capacitor"));
            event.addSprite(new ResourceLocation(IronJetpacks.MOD_ID, "items/thruster"));
            event.addSprite(new ResourceLocation(IronJetpacks.MOD_ID, "items/jetpack"));
        }
    }
}
