package com.blakebr0.ironjetpacks.compat.curios.renderer;

import com.blakebr0.ironjetpacks.client.ModEquipmentAssets;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public record JetpackCurioRenderer() implements ICurioRenderer {
    @Override
    public <S extends LivingEntityRenderState, M extends EntityModel<? super S>> void render(
            ItemStack stack,
            SlotContext slotContext,
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            int packedLight,
            S renderState,
            RenderLayerParent<S, M> renderLayerParent,
            EntityRendererProvider.Context context,
            float yRotation,
            float xRotation
    ) {
        context.getEquipmentRenderer().renderLayers(
                EquipmentClientInfo.LayerType.HUMANOID,
                ModEquipmentAssets.JETPACK,
                renderLayerParent.getModel(),
                renderState,
                stack,
                poseStack,
                submitNodeCollector,
                renderState.lightCoords,
                renderState.outlineColor
        );
    }
}
