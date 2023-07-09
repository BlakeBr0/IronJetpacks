package com.blakebr0.ironjetpacks.init;

import com.blakebr0.cucumber.util.FeatureFlagDisplayItemGenerator;
import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry;
import com.blakebr0.ironjetpacks.util.JetpackUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, IronJetpacks.MOD_ID);

    public static final RegistryObject<CreativeModeTab> CREATIVE_TAB = REGISTRY.register("creative_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.ironjetpacks"))
            .icon(() -> {
                var jetpack = JetpackRegistry.getInstance().getJetpacks()
                        .stream()
                        .findFirst()
                        .orElse(null);

                return jetpack != null ? JetpackUtils.getItemForJetpack(jetpack) : new ItemStack(ModItems.STRAP.get());
            })
            .displayItems(FeatureFlagDisplayItemGenerator.create((parameters, output) -> {
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
            }))
            .build());
}
