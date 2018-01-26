package com.blakebr0.ironjetpacks.sound;

import com.blakebr0.ironjetpacks.IronJetpacks;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class ModSounds {

	public static SoundEvent soundJetpack = new SoundEvent(new ResourceLocation(IronJetpacks.MOD_ID, "jetpack"));
	
	@SubscribeEvent
	public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
		event.getRegistry().register(soundJetpack.setRegistryName(new ResourceLocation(IronJetpacks.MOD_ID, "jetpack")));
	}
}
