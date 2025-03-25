package com.blakebr0.ironjetpacks.init;

import com.blakebr0.ironjetpacks.IronJetpacks;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModSounds {
	public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(Registries.SOUND_EVENT, IronJetpacks.MOD_ID);

	public static final DeferredHolder<SoundEvent, SoundEvent> JETPACK = REGISTRY.register("jetpack", () -> SoundEvent.createVariableRangeEvent(IronJetpacks.resource("jetpack")));
}
