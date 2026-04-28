package com.blakebr0.ironjetpacks.handler;

import com.blakebr0.ironjetpacks.init.ModDataComponentTypes;
import com.blakebr0.ironjetpacks.init.ModItems;
import com.blakebr0.ironjetpacks.util.JetpackUtils;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.energy.ItemAccessEnergyHandler;

public final class RegisterCapabilityHandler {
    @SubscribeEvent
    public void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(Capabilities.Energy.ITEM, (stack, access) -> {
            var jetpack = JetpackUtils.getJetpack(stack);
            return new ItemAccessEnergyHandler(access, ModDataComponentTypes.JETPACK_ENERGY.get(), jetpack.capacity);
        }, ModItems.JETPACK.get());
    }
}
