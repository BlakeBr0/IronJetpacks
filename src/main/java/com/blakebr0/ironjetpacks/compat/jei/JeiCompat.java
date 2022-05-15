package com.blakebr0.ironjetpacks.compat.jei;

import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.init.ModItems;
import com.blakebr0.ironjetpacks.util.JetpackUtils;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class JeiCompat implements IModPlugin {
    public static final ResourceLocation UID = new ResourceLocation(IronJetpacks.MOD_ID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        ModItems.JETPACK.ifPresent(item -> {
            registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, item, (stack, ctx) -> {
                var jetpack = JetpackUtils.getJetpack(stack);
                return jetpack.getId().toString();
            });
        });

        ModItems.CELL.ifPresent(item -> {
            registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, item, (stack, ctx) -> {
                var jetpack = JetpackUtils.getJetpack(stack);
                return jetpack.getId().toString();
            });
        });

        ModItems.THRUSTER.ifPresent(item -> {
            registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, item, (stack, ctx) -> {
                var jetpack = JetpackUtils.getJetpack(stack);
                return jetpack.getId().toString();
            });
        });

        ModItems.CAPACITOR.ifPresent(item -> {
            registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, item, (stack, ctx) -> {
                var jetpack = JetpackUtils.getJetpack(stack);
                return jetpack.getId().toString();
            });
        });
    }
}
