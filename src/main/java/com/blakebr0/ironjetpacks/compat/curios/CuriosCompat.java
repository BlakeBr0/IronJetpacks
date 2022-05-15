package com.blakebr0.ironjetpacks.compat.curios;

import com.blakebr0.ironjetpacks.compat.curios.curio.JetpackCurio;
import com.blakebr0.ironjetpacks.init.ModItems;
import com.blakebr0.ironjetpacks.item.JetpackItem;
import com.blakebr0.ironjetpacks.util.JetpackUtils;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.Optional;

public class CuriosCompat {
    public static void onInterModEnqueue(InterModEnqueueEvent event) {
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.BODY.getMessageBuilder().build());
    }

    public static Optional<ItemStack> findJetpackCurio(LivingEntity entity) {
        return CuriosApi.getCuriosHelper().findFirstCurio(entity, ModItems.JETPACK.get())
                .map(SlotResult::stack)
                .filter(stack -> JetpackUtils.getJetpack(stack).curios);
    }

    @SubscribeEvent
    public void onAttachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        var stack = event.getObject();

        if (stack.getItem() instanceof JetpackItem) {
            var curio = new JetpackCurio(stack);

            event.addCapability(CuriosCapability.ID_ITEM, new ICapabilityProvider() {
                final LazyOptional<ICurio> capability = LazyOptional.of(() -> curio);

                @Override
                public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
                    return CuriosCapability.ITEM.orEmpty(cap, capability);
                }
            });
        }
    }
}
