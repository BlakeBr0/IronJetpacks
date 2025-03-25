package com.blakebr0.ironjetpacks;

import com.blakebr0.ironjetpacks.client.ModClientExtensions;
import com.blakebr0.ironjetpacks.client.ModelHandler;
import com.blakebr0.ironjetpacks.client.handler.ColorHandler;
import com.blakebr0.ironjetpacks.client.handler.HudHandler;
import com.blakebr0.ironjetpacks.client.handler.InputHandler;
import com.blakebr0.ironjetpacks.client.handler.JetpackClientHandler;
import com.blakebr0.ironjetpacks.client.handler.KeybindHandler;
import com.blakebr0.ironjetpacks.compat.ControllableCompat;
import com.blakebr0.ironjetpacks.config.ModConfigs;
import com.blakebr0.ironjetpacks.crafting.DynamicRecipeManager;
import com.blakebr0.ironjetpacks.handler.RegisterCapabilityHandler;
import com.blakebr0.ironjetpacks.init.ModArmorMaterials;
import com.blakebr0.ironjetpacks.init.ModCreativeModeTabs;
import com.blakebr0.ironjetpacks.init.ModDataComponentTypes;
import com.blakebr0.ironjetpacks.init.ModIngredientTypes;
import com.blakebr0.ironjetpacks.init.ModItems;
import com.blakebr0.ironjetpacks.init.ModRecipeSerializers;
import com.blakebr0.ironjetpacks.init.ModSounds;
import com.blakebr0.ironjetpacks.network.NetworkHandler;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(IronJetpacks.MOD_ID)
public final class IronJetpacks {
	public static final String MOD_ID = "ironjetpacks";
	public static final String NAME = "Iron Jetpacks";
	public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

	public IronJetpacks(IEventBus bus, ModContainer mod) {
		bus.register(this);

		ModArmorMaterials.REGISTRY.register(bus);
		ModDataComponentTypes.REGISTRY.register(bus);
		ModItems.REGISTRY.register(bus);
		ModCreativeModeTabs.REGISTRY.register(bus);
		ModSounds.REGISTRY.register(bus);
		ModIngredientTypes.REGISTRY.register(bus);
		ModRecipeSerializers.REGISTRY.register(bus);

		bus.register(new RegisterCapabilityHandler());
		bus.register(new NetworkHandler());

		if (FMLEnvironment.dist == Dist.CLIENT) {
			bus.register(new ColorHandler());
			bus.register(new ModelHandler());
			bus.register(new ModClientExtensions());
			bus.addListener(KeybindHandler::onRegisterKeyMappings);
		}

		mod.registerConfig(ModConfig.Type.CLIENT, ModConfigs.CLIENT);
		mod.registerConfig(ModConfig.Type.COMMON, ModConfigs.COMMON);
	}

	@SubscribeEvent
	public void onCommonSetup(FMLCommonSetupEvent event) {
		NeoForge.EVENT_BUS.register(new InputHandler());
		NeoForge.EVENT_BUS.register(DynamicRecipeManager.getInstance());
		NeoForge.EVENT_BUS.register(JetpackRegistry.getInstance());

		JetpackRegistry.getInstance().writeDefaultJetpackFiles();
	}

	@SubscribeEvent
	public void onClientSetup(FMLClientSetupEvent event) {
		NeoForge.EVENT_BUS.register(new KeybindHandler());
		NeoForge.EVENT_BUS.register(new HudHandler());
		NeoForge.EVENT_BUS.register(new JetpackClientHandler());

		if (ModConfigs.isControllableInstalled()) {
			NeoForge.EVENT_BUS.register(new ControllableCompat());
		}
	}

	public static ResourceLocation resource(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}
