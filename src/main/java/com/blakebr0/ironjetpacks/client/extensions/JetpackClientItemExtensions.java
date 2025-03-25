package com.blakebr0.ironjetpacks.client.extensions;

import com.blakebr0.ironjetpacks.client.ModelHandler;
import com.blakebr0.ironjetpacks.client.model.JetpackModel;
import com.blakebr0.ironjetpacks.util.JetpackUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public class JetpackClientItemExtensions implements IClientItemExtensions {
    public static final JetpackClientItemExtensions INSTANCE = new JetpackClientItemExtensions();

    private JetpackModel[] models;

    @Override
    public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> _default) {
        if (this.models == null) {
            this.models = new JetpackModel[6];

            for (int i = 0; i < 6; i++) {
                var layer = Minecraft.getInstance().getEntityModels().bakeLayer(ModelHandler.JETPACK_LAYER);

                this.models[i] = new JetpackModel(layer, i);
            }
        }

        var jetpack = JetpackUtils.getJetpack(stack);

        if (jetpack.creative) {
            return this.models[5];
        }

        var energy = JetpackUtils.getEnergyStorage(stack);
        var stored = (double) energy.getEnergyStored() / (double) energy.getMaxEnergyStored();

        int state = 0;
        if (stored > 0.8) {
            state = 5;
        } else if (stored > 0.6) {
            state = 4;
        } else if (stored > 0.4) {
            state = 3;
        } else if (stored > 0.2) {
            state = 2;
        } else if (stored > 0) {
            state = 1;
        }

        return this.models[state];
    }
}
