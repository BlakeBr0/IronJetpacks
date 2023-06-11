package com.blakebr0.ironjetpacks.compat.curios.curio;

import com.blakebr0.ironjetpacks.config.ModConfigs;
import com.blakebr0.ironjetpacks.util.JetpackUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.List;

public record JetpackCurio(ItemStack stack) implements ICurio {
    @Override
    public ItemStack getStack() {
        return this.stack;
    }

    @Override
    public boolean canEquip(SlotContext context) {
        return this.isCurioJetpack();
    }

    @Override
    public boolean canEquipFromUse(SlotContext context) {
        return this.isCurioJetpack();
    }

    @Override
    public void curioTick(SlotContext context) {
        if (this.isCurioJetpack()) {
            var entity = context.entity();
            if (entity instanceof Player player) {
                this.stack.onArmorTick(player.level(), player);
            }
        }
    }

    @Override
    public List<Component> getSlotsTooltip(List<Component> tooltips) {
        return this.isCurioJetpack() ? tooltips : List.of();
    }

    private boolean isCurioJetpack() {
        return ModConfigs.ENABLE_CURIOS_INTEGRATION.get() && JetpackUtils.getJetpack(this.stack).curios;
    }
}
