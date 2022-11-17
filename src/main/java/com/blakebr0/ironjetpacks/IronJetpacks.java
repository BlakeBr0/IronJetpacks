package com.blakebr0.ironjetpacks;

import com.blakebr0.ironjetpacks.client.ModelHandler;
import com.blakebr0.ironjetpacks.compat.curios.CuriosCompat;
import com.blakebr0.ironjetpacks.config.ModConfigs;
import com.blakebr0.ironjetpacks.crafting.DynamicRecipeManager;
import com.blakebr0.ironjetpacks.handler.ColorHandler;
import com.blakebr0.ironjetpacks.handler.HudHandler;
import com.blakebr0.ironjetpacks.handler.InputHandler;
import com.blakebr0.ironjetpacks.handler.JetpackClientHandler;
import com.blakebr0.ironjetpacks.handler.KeybindHandler;
import com.blakebr0.ironjetpacks.init.ModItems;
import com.blakebr0.ironjetpacks.init.ModRecipeSerializers;
import com.blakebr0.ironjetpacks.init.ModReloadListeners;
import com.blakebr0.ironjetpacks.init.ModSounds;
import com.blakebr0.ironjetpacks.network.NetworkHandler;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(IronJetpacks.MOD_ID)
public final class IronJetpacks {
	public static final String MOD_ID = "ironjetpacks";
	public static final String NAME = "Iron Jetpacks";
	public static final Logger LOGGER = LoggerFactory.getLogger(NAME);
	public static final CreativeModeTab CREATIVE_TAB = new IJCreativeTab();

	public IronJetpacks() {
		var bus = FMLJavaModLoadingContext.get().getModEventBus();

		bus.register(this);
		bus.register(new ModSounds());
		bus.register(new ModRecipeSerializers());

		ModItems.REGISTRY.register(bus);
		ModRecipeSerializers.REGISTRY.register(bus);

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			bus.register(new ColorHandler());
			bus.register(new ModelHandler());
			bus.register(new HudHandler());
			bus.addListener(KeybindHandler::onRegisterKeyMappings);
		});

		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ModConfigs.CLIENT);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ModConfigs.COMMON);
	}

	@SubscribeEvent
	public void onCommonSetup(FMLCommonSetupEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new ModReloadListeners());
		MinecraftForge.EVENT_BUS.register(new InputHandler());
		MinecraftForge.EVENT_BUS.register(DynamicRecipeManager.getInstance());
		MinecraftForge.EVENT_BUS.register(JetpackRegistry.getInstance());

		if (ModConfigs.isCuriosInstalled()) {
			MinecraftForge.EVENT_BUS.register(new CuriosCompat());
		}

		event.enqueueWork(() -> {
			NetworkHandler.onCommonSetup();
		});

		JetpackRegistry.getInstance().writeDefaultJetpackFiles();
	}

	@SubscribeEvent
	public void onClientSetup(FMLClientSetupEvent event) {
		MinecraftForge.EVENT_BUS.register(new KeybindHandler());
		MinecraftForge.EVENT_BUS.register(new JetpackClientHandler());
	}

	@SubscribeEvent
	public void onInterModEnqueue(InterModEnqueueEvent event) {
		if (ModConfigs.isCuriosEnabled()) {
			CuriosCompat.onInterModEnqueue(event);
		}
	}
}
