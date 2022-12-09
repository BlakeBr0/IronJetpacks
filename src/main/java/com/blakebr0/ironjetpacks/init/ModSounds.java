package com.blakebr0.ironjetpacks.init;

import com.blakebr0.ironjetpacks.IronJetpacks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

public final class ModSounds {
	public static final SoundEvent JETPACK = SoundEvent.createVariableRangeEvent(new ResourceLocation(IronJetpacks.MOD_ID, "jetpack"));
	
	@SubscribeEvent
	public void onRegisterSounds(RegisterEvent event) {
		event.register(ForgeRegistries.Keys.SOUND_EVENTS, registry -> {
			registry.register("jetpack", JETPACK);
		});
	}
}
