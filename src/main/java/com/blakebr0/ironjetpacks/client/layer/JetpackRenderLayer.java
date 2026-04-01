package com.blakebr0.ironjetpacks.client.layer;

import com.blakebr0.ironjetpacks.IronJetpacks;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;

public class JetpackRenderLayer<T extends HumanoidRenderState, M extends HumanoidModel<T>> extends RenderLayer<T, M>  {
    private static final Identifier TEXTURE = IronJetpacks.resource("textures/models/armor/jetpack_layer_1.png");
    private static final Identifier TEXTURE_OVERLAY = IronJetpacks.resource("textures/models/armor/jetpack_layer_1_overlay.png");

    private final EquipmentLayerRenderer equipmentLayerRenderer;

    public JetpackRenderLayer(RenderLayerParent<T, M> parent, EquipmentLayerRenderer equipmentLayerRenderer) {
        super(parent);
        this.equipmentLayerRenderer = equipmentLayerRenderer;
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, T t, float v, float v1) {
//        TODO: curio model layer
        //        var stack = entity.getItemBySlot(EquipmentSlot.CHEST);
//
//        if (stack.getItem() instanceof JetpackItem)
//            return;
//
//        CuriosCompat.findJetpackCurio(entity, slot -> slot.slotContext().visible()).ifPresent(curio -> {
//            var model = IClientItemExtensions.of(curio).getHumanoidArmorModel(entity, curio, EquipmentSlot.CHEST, null);
//
//            this.getParentModel().copyPropertiesTo((HumanoidModel<T>) model);
//
//            if (curio.getItem() instanceof IColored colored) {
//                int color = colored.getColor(1, curio);
//                int r = color >> 16 & 255;
//                int g = color >> 8 & 255;
//                int b = color & 255;
//
//                this.renderModel(matrix, buffer, lightness, curio.hasFoil(), model, r, g, b, TEXTURE);
//                this.renderModel(matrix, buffer, lightness, curio.hasFoil(), model, 255, 255, 255, TEXTURE_OVERLAY);
//            } else {
//                this.renderModel(matrix, buffer, lightness, curio.hasFoil(), model, 255, 255, 255, TEXTURE);
//            }
//        });
    }

    private void renderArmorPiece(
            PoseStack poseStack, SubmitNodeCollector submitNodeCollector, ItemStack itemStack, Model<T> model, int lightCoords, T state
    ) {
        Equippable equippable = itemStack.get(DataComponents.EQUIPPABLE);
        if (equippable != null) {
            this.equipmentLayerRenderer.renderLayers(EquipmentClientInfo.LayerType.HUMANOID, equippable.assetId().orElseThrow(), model, state, itemStack, poseStack, submitNodeCollector, lightCoords, state.outlineColor);
        }
    }
}
