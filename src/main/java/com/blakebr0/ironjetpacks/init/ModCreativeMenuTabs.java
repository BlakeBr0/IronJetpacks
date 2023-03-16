package com.blakebr0.ironjetpacks.init;

import com.blakebr0.cucumber.util.FeatureFlagDisplayItemGenerator;
import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry;
import com.blakebr0.ironjetpacks.util.JetpackUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class ModCreativeMenuTabs {
    @SubscribeEvent
    public void onRegisterCreativeModeTabs(CreativeModeTabEvent.Register event) {
        event.registerCreativeModeTab(new ResourceLocation(IronJetpacks.MOD_ID, "creative_mode_tab"), (builder) -> {
            var displayItems = FeatureFlagDisplayItemGenerator.create((parameters, output) -> {
                output.accept(ModItems.STRAP.get());
                output.accept(ModItems.BASIC_COIL.get());
                output.accept(ModItems.ADVANCED_COIL.get());
                output.accept(ModItems.ELITE_COIL.get());
                output.accept(ModItems.ULTIMATE_COIL.get());

                for (var jetpack : JetpackRegistry.getInstance().getJetpacks()) {
                    output.accept(JetpackUtils.getItemForComponent(ModItems.CELL.get(), jetpack));
                    output.accept(JetpackUtils.getItemForComponent(ModItems.THRUSTER.get(), jetpack));
                    output.accept(JetpackUtils.getItemForComponent(ModItems.CAPACITOR.get(), jetpack));
                    output.accept(JetpackUtils.getItemForJetpack(jetpack));
                }
            });

            var jetpack = JetpackRegistry.getInstance().getJetpacks()
                    .stream()
                    .findFirst()
                    .orElse(null);

            var icon = jetpack != null ? JetpackUtils.getItemForJetpack(jetpack) : new ItemStack(ModItems.STRAP.get());

            builder.title(Component.translatable("itemGroup.ironjetpacks"))
                    .icon(() -> icon)
                    .displayItems(displayItems);
        });
    }
}
