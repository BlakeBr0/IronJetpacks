package com.blakebr0.ironjetpacks.client;

import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry;
import com.google.common.base.Stopwatch;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ModelHandler {
    private static final Logger LOGGER = LogManager.getLogger(IronJetpacks.NAME);

    @SubscribeEvent
    public void onRegisterModels(ModelRegistryEvent event) {
        ModelLoader.addSpecialModel(new ResourceLocation(IronJetpacks.MOD_ID, "item/cell"));
        ModelLoader.addSpecialModel(new ResourceLocation(IronJetpacks.MOD_ID, "item/capacitor"));
        ModelLoader.addSpecialModel(new ResourceLocation(IronJetpacks.MOD_ID, "item/thruster"));
        ModelLoader.addSpecialModel(new ResourceLocation(IronJetpacks.MOD_ID, "item/jetpack"));
    }

    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Map<ResourceLocation, IBakedModel> registry = event.getModelRegistry();

        IBakedModel cell = registry.get(new ResourceLocation(IronJetpacks.MOD_ID, "item/cell"));
        IBakedModel capacitor = registry.get(new ResourceLocation(IronJetpacks.MOD_ID, "item/capacitor"));
        IBakedModel thruster = registry.get(new ResourceLocation(IronJetpacks.MOD_ID, "item/thruster"));
        IBakedModel jetpack = registry.get(new ResourceLocation(IronJetpacks.MOD_ID, "item/jetpack"));

        JetpackRegistry.getInstance().getAllJetpacks().forEach(pack -> {
            ResourceLocation cellLocation = pack.cell.getRegistryName();
            if (cellLocation != null) {
                ModelResourceLocation location = new ModelResourceLocation(cellLocation, "inventory");
                registry.replace(location, cell);
            }

            ResourceLocation capacitorLocation = pack.capacitor.getRegistryName();
            if (capacitorLocation != null) {
                ModelResourceLocation location = new ModelResourceLocation(capacitorLocation, "inventory");
                registry.replace(location, capacitor);
            }

            ResourceLocation thrusterLocation = pack.thruster.getRegistryName();
            if (thrusterLocation != null) {
                ModelResourceLocation location = new ModelResourceLocation(thrusterLocation, "inventory");
                registry.replace(location, thruster);
            }

            ResourceLocation jetpackLocation = pack.item.getRegistryName();
            if (jetpackLocation != null) {
                ModelResourceLocation location = new ModelResourceLocation(jetpackLocation, "inventory");
                registry.replace(location, jetpack);
            }
        });

        LOGGER.info("Model replacement took {} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }
}
