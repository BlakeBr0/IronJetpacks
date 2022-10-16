package com.blakebr0.ironjetpacks.client.layer;

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
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class JetpackRenderLayer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(IronJetpacks.MOD_ID, "textures/armor/jetpack.png");
    private static final ResourceLocation TEXTURE_OVERLAY = new ResourceLocation(IronJetpacks.MOD_ID, "textures/armor/jetpack_overlay.png");

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

            if (curio.getItem() instanceof DyeableLeatherItem dyeable) {
                int i = dyeable.getColor(curio);
                float r = (float) (i >> 16 & 255) / 255.0F;
                float g = (float) (i >> 8 & 255) / 255.0F;
                float b = (float) (i & 255) / 255.0F;

                this.renderModel(matrix, buffer, lightness, curio.hasFoil(), model, r, g, b, TEXTURE);
                this.renderModel(matrix, buffer, lightness, curio.hasFoil(), model, 1.0F, 1.0F, 1.0F, TEXTURE_OVERLAY);
            } else {
                this.renderModel(matrix, buffer, lightness, curio.hasFoil(), model, 1.0F, 1.0F, 1.0F, TEXTURE);
            }
        });
    }

    private void renderModel(PoseStack matrix, MultiBufferSource buffer, int lightness, boolean foil, Model model, float r, float g, float b, ResourceLocation armorResource) {
        var vertex = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(armorResource), false, foil);
        model.renderToBuffer(matrix, vertex, lightness, OverlayTexture.NO_OVERLAY, r, g, b, 1.0F);
    }
}
