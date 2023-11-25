package com.blakebr0.ironjetpacks.init;

import com.blakebr0.ironjetpacks.IronJetpacks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModSounds {
	public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, IronJetpacks.MOD_ID);

	public static final RegistryObject<SoundEvent> JETPACK = REGISTRY.register("jetpack", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(IronJetpacks.MOD_ID, "jetpack")));
}
