package com.blakebr0.ironjetpacks.handler;

import com.blakebr0.cucumber.client.helper.RenderHelper;
import com.blakebr0.cucumber.lib.Colors;
import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.client.helper.HudHelper;
import com.blakebr0.ironjetpacks.client.helper.HudHelper.HudPos;
import com.blakebr0.ironjetpacks.config.ModConfigs;
import com.blakebr0.ironjetpacks.item.JetpackItem;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class HudHandler {
    private static final ResourceLocation HUD_TEXTURE = new ResourceLocation(IronJetpacks.MOD_ID, "textures/gui/hud.png");

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL)
            return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            if (ModConfigs.ENABLE_HUD.get() && (ModConfigs.SHOW_HUD_OVER_CHAT.get() || !ModConfigs.SHOW_HUD_OVER_CHAT.get() && !(mc.currentScreen instanceof ChatScreen)) && !mc.gameSettings.hideGUI && !mc.gameSettings.showDebugInfo) {
                ItemStack chest = mc.player.getItemStackFromSlot(EquipmentSlotType.CHEST);
                Item item = chest.getItem();
                if (!chest.isEmpty() && item instanceof JetpackItem) {
                    JetpackItem jetpack = (JetpackItem) item;
                    HudPos pos = HudHelper.getHudPos();
                    if (pos != null) {
                        int xPos = (int) (pos.x / 0.33) - 18;
                        int yPos = (int) (pos.y / 0.33) - 78;

                        mc.getTextureManager().bindTexture(HUD_TEXTURE);

                        RenderSystem.pushMatrix();
                        RenderSystem.scaled(0.33, 0.33, 1.0);
                        RenderHelper.drawTexturedModalRect(xPos, yPos, 0, 0, 28, 156);
                        int i2 = HudHelper.getEnergyBarScaled(jetpack, chest);
                        RenderHelper.drawTexturedModalRect(xPos, 166 - i2 + yPos - 10, 28, 156 - i2, 28, i2);
                        RenderSystem.popMatrix();

                        String fuel = Colors.GRAY + HudHelper.getFuelString(jetpack, chest);
                        String engine = Colors.GRAY + "E: " + HudHelper.getStatusString(jetpack.isEngineOn(chest));
                        String hover = Colors.GRAY + "H: " + HudHelper.getStatusString(jetpack.isHovering(chest));

                        MatrixStack stack = event.getMatrixStack();
                        if (pos.side == 1) {
                            mc.fontRenderer.func_238405_a_(stack, fuel, pos.x - 8 - mc.fontRenderer.getStringWidth(fuel), pos.y - 21, 16383998);
                            mc.fontRenderer.func_238405_a_(stack, engine, pos.x - 8 - mc.fontRenderer.getStringWidth(engine), pos.y + 4, 16383998);
                            mc.fontRenderer.func_238405_a_(stack, hover, pos.x - 8 - mc.fontRenderer.getStringWidth(hover), pos.y + 14, 16383998);
                        } else {
                            mc.fontRenderer.func_238405_a_(stack, fuel, pos.x + 6, pos.y - 21, 16383998);
                            mc.fontRenderer.func_238405_a_(stack, engine, pos.x + 6, pos.y + 4, 16383998);
                            mc.fontRenderer.func_238405_a_(stack, hover, pos.x + 6, pos.y + 14, 16383998);
                        }

                        mc.getTextureManager().bindTexture(AbstractGui.field_230665_h_);
                    }
                }
            }
        }
    }
}
