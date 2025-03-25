package com.blakebr0.ironjetpacks.client.layer;

import com.blakebr0.cucumber.iface.IColored;
import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.compat.curios.CuriosCompat;
import com.blakebr0.ironjetpacks.item.JetpackItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public class JetpackRenderLayer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {
    private static final ResourceLocation TEXTURE = IronJetpacks.resource("textures/models/armor/jetpack_layer_1.png");
    private static final ResourceLocation TEXTURE_OVERLAY = IronJetpacks.resource("textures/models/armor/jetpack_layer_1_overlay.png");

    public JetpackRenderLayer(RenderLayerParent<T, M> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack matrix, MultiBufferSource buffer, int lightness, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        var stack = entity.getItemBySlot(EquipmentSlot.CHEST);

        if (stack.getItem() instanceof JetpackItem)
            return;

        CuriosCompat.findJetpackCurio(entity, slot -> slot.slotContext().visible()).ifPresent(curio -> {
            var model = IClientItemExtensions.of(curio).getHumanoidArmorModel(entity, curio, EquipmentSlot.CHEST, null);

            this.getParentModel().copyPropertiesTo((HumanoidModel<T>) model);

            if (curio.getItem() instanceof IColored colored) {
                int color = colored.getColor(1, curio);
                int r = color >> 16 & 255;
                int g = color >> 8 & 255;
                int b = color & 255;

                this.renderModel(matrix, buffer, lightness, curio.hasFoil(), model, r, g, b, TEXTURE);
                this.renderModel(matrix, buffer, lightness, curio.hasFoil(), model, 255, 255, 255, TEXTURE_OVERLAY);
            } else {
                this.renderModel(matrix, buffer, lightness, curio.hasFoil(), model, 255, 255, 255, TEXTURE);
            }
        });
    }

    private void renderModel(PoseStack matrix, MultiBufferSource buffer, int lightness, boolean foil, Model model, int r, int g, int b, ResourceLocation armorResource) {
        var vertex = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(armorResource), foil);
        model.renderToBuffer(matrix, vertex, lightness, OverlayTexture.NO_OVERLAY, FastColor.ARGB32.color(255, r, g, b));
    }
}
