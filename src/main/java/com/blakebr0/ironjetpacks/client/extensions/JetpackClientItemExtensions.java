package com.blakebr0.ironjetpacks.client.extensions;

import com.blakebr0.ironjetpacks.client.handler.ModelHandler;
import com.blakebr0.ironjetpacks.client.model.JetpackModel;
import com.blakebr0.ironjetpacks.util.JetpackUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public class JetpackClientItemExtensions implements IClientItemExtensions {
    public static final JetpackClientItemExtensions INSTANCE = new JetpackClientItemExtensions();

    private JetpackModel[] models;

    @Override
    public Model<?> getHumanoidArmorModel(ItemStack stack, EquipmentClientInfo.LayerType layerType, Model original) {
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
        var stored = (double) energy.getAmountAsInt() / (double) energy.getCapacityAsInt();

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

    @Override
    public int getArmorLayerTintColor(ItemStack stack, EquipmentClientInfo.Layer layer, int layerIdx, int fallbackColor) {
        var jetpack = JetpackUtils.getJetpack(stack);
        return layer.dyeable().isPresent() ? jetpack.color : -1;
    }
}
