package com.blakebr0.ironjetpacks.compat.jei;

import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.init.ModDataComponentTypes;
import com.blakebr0.ironjetpacks.init.ModItems;
import com.blakebr0.ironjetpacks.registry.Jetpack;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class JeiCompat implements IModPlugin {
    public static final ResourceLocation UID = IronJetpacks.resource("jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, ModItems.JETPACK.get(), (stack, ctx) -> stack.getOrDefault(ModDataComponentTypes.JETPACK_ID, Jetpack.UNDEFINED.getId()).toString());
        registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, ModItems.CELL.get(), (stack, ctx) -> stack.getOrDefault(ModDataComponentTypes.JETPACK_ID, Jetpack.UNDEFINED.getId()).toString());
        registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, ModItems.THRUSTER.get(), (stack, ctx) -> stack.getOrDefault(ModDataComponentTypes.JETPACK_ID, Jetpack.UNDEFINED.getId()).toString());
        registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, ModItems.CAPACITOR.get(), (stack, ctx) -> stack.getOrDefault(ModDataComponentTypes.JETPACK_ID, Jetpack.UNDEFINED.getId()).toString());
    }
}
