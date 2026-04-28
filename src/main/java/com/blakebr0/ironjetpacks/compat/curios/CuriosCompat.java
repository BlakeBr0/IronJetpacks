package com.blakebr0.ironjetpacks.compat.curios;

import com.blakebr0.ironjetpacks.compat.curios.curio.JetpackCurio;
import com.blakebr0.ironjetpacks.compat.curios.renderer.JetpackCurioRenderer;
import com.blakebr0.ironjetpacks.init.ModItems;
import com.blakebr0.ironjetpacks.util.JetpackUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.client.ICurioRenderer;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Predicate;

public final class CuriosCompat {
    @SubscribeEvent
    public void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(CuriosCapability.ITEM, (stack, _) -> new JetpackCurio(stack), ModItems.JETPACK.get());
    }

    @SubscribeEvent
    public void onClientSetup(FMLClientSetupEvent event) {
        ICurioRenderer.register(ModItems.JETPACK.get(), JetpackCurioRenderer::new);
    }

    public static Optional<ItemStack> findJetpackCurio(LivingEntity entity) {
        return findJetpackCurio(entity, null);
    }

    public static Optional<ItemStack> findJetpackCurio(LivingEntity entity, @Nullable Predicate<SlotResult> predicate) {
        var optional = CuriosApi.getCuriosInventory(entity).flatMap(i -> i.findFirstCurio(ModItems.JETPACK.get()));

        if (predicate != null) {
        	optional = optional.filter(predicate);
        }

        return optional.map(SlotResult::stack).filter(stack -> JetpackUtils.getJetpack(stack).curios);
    }
}
