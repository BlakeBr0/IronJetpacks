package com.blakebr0.ironjetpacks.init;

import com.blakebr0.ironjetpacks.IronJetpacks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public final class ModSounds {
	public static final SoundEvent JETPACK = new SoundEvent(new ResourceLocation(IronJetpacks.MOD_ID, "jetpack"));
	
	@SubscribeEvent
	public void onRegisterSounds(RegistryEvent.Register<SoundEvent> event) {
		IForgeRegistry<SoundEvent> registry = event.getRegistry();

		registry.register(JETPACK.setRegistryName("jetpack"));
	}
}
