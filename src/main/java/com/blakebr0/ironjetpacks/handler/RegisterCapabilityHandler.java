package com.blakebr0.ironjetpacks.handler;

import com.blakebr0.ironjetpacks.compat.curios.CuriosCompat;
import com.blakebr0.ironjetpacks.config.ModConfigs;
import com.blakebr0.ironjetpacks.init.ModDataComponentTypes;
import com.blakebr0.ironjetpacks.init.ModItems;
import com.blakebr0.ironjetpacks.util.JetpackUtils;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.ComponentEnergyStorage;

public final class RegisterCapabilityHandler {
    @SubscribeEvent
    public void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(Capabilities.EnergyStorage.ITEM, (stack, context) -> {
            var jetpack = JetpackUtils.getJetpack(stack);
            return new ComponentEnergyStorage(stack, ModDataComponentTypes.JETPACK_ENERGY.get(), jetpack.capacity);
        }, ModItems.JETPACK.get());

        if (ModConfigs.isCuriosEnabled()) {
            CuriosCompat.registerCapabilities(event);
        }
    }
}
