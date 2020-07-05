package com.blakebr0.ironjetpacks;

import com.blakebr0.ironjetpacks.client.ModelHandler;
import com.blakebr0.ironjetpacks.config.ModConfigs;
import com.blakebr0.ironjetpacks.crafting.JetpackDynamicRecipeManager;
import com.blakebr0.ironjetpacks.handler.ColorHandler;
import com.blakebr0.ironjetpacks.handler.HudHandler;
import com.blakebr0.ironjetpacks.handler.InputHandler;
import com.blakebr0.ironjetpacks.handler.JetpackClientHandler;
import com.blakebr0.ironjetpacks.handler.KeybindHandler;
import com.blakebr0.ironjetpacks.init.ModItems;
import com.blakebr0.ironjetpacks.init.ModRecipeSerializers;
import com.blakebr0.ironjetpacks.init.ModSounds;
import com.blakebr0.ironjetpacks.network.NetworkHandler;
import net.minecraft.item.ItemGroup;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(IronJetpacks.MOD_ID)
public class IronJetpacks {
	public static final String MOD_ID = "ironjetpacks";
	public static final String NAME = "Iron Jetpacks";

	public static final ItemGroup ITEM_GROUP = new IJItemGroup();

	public IronJetpacks() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		bus.register(this);
		bus.register(new ModItems());
		bus.register(new ModSounds());
		bus.register(new ModRecipeSerializers());
		bus.register(new ModConfigs());

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			bus.register(new ColorHandler());
			bus.register(new ModelHandler());
		});

		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ModConfigs.CLIENT);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ModConfigs.COMMON);
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ModConfigs.SERVER);
	}

	@SubscribeEvent
	public void onCommonSetup(FMLCommonSetupEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new InputHandler());

		ModRecipeSerializers.onCommonSetup();

		DeferredWorkQueue.runLater(() -> {
			NetworkHandler.onCommonSetup();
		});
	}

	@SubscribeEvent
	public void onClientSetup(FMLClientSetupEvent event) {
		MinecraftForge.EVENT_BUS.register(new KeybindHandler());
		MinecraftForge.EVENT_BUS.register(new HudHandler());
		MinecraftForge.EVENT_BUS.register(new JetpackClientHandler());

		KeybindHandler.onClientSetup();
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onServerSetup(FMLServerAboutToStartEvent event) {
		IReloadableResourceManager manager = (IReloadableResourceManager) event.getServer().getDataPackRegistries().func_240970_h_();

		manager.addReloadListener(new JetpackDynamicRecipeManager());
	}

	@SubscribeEvent
	public void onServerStopping(FMLServerStoppingEvent event) {
		InputHandler.clear();
	}
}
