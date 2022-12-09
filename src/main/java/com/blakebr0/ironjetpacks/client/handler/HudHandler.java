package com.blakebr0.ironjetpacks.client.handler;

import com.blakebr0.cucumber.lib.Colors;
import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.client.helper.HudHelper;
import com.blakebr0.ironjetpacks.config.ModConfigs;
import com.blakebr0.ironjetpacks.item.JetpackItem;
import com.blakebr0.ironjetpacks.util.JetpackUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class HudHandler {
    private static final ResourceLocation HUD_TEXTURE = new ResourceLocation(IronJetpacks.MOD_ID, "textures/gui/hud.png");

    private static final IGuiOverlay HUD_OVERLAY = (gui, matrix, partialTick, width, height) -> {
        var mc = Minecraft.getInstance();
        if (mc.player != null && isVisible(mc)) {
            var chest = JetpackUtils.getEquippedJetpack(mc.player);
            var item = chest.getItem();

            if (!chest.isEmpty() && item instanceof JetpackItem) {
                var pos = HudHelper.getHudPos();
                if (pos != null) {
                    int xPos = (int) (pos.x / 0.33) - 18;
                    int yPos = (int) (pos.y / 0.33) - 78;

                    RenderSystem.setShader(GameRenderer::getPositionTexShader);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    RenderSystem.setShaderTexture(0, HUD_TEXTURE);

                    matrix.pushPose();
                    matrix.scale(0.33F, 0.33F, 1.0F);
                    Screen.blit(matrix, xPos, yPos, 0, 0, 28, 156, 256, 256);
                    int i2 = HudHelper.getEnergyBarScaled(chest);
                    Screen.blit(matrix, xPos, 166 - i2 + yPos - 10, 28, 156 - i2, 28, i2, 256, 256);
                    matrix.popPose();

                    var fuel = Colors.GRAY + HudHelper.getFuelString(chest);
                    var throttle = Colors.GRAY + "T: " + (int) (JetpackUtils.getThrottle(chest) * 100) + "%";
                    var engine = Colors.GRAY + "E: " + HudHelper.getStatusString(JetpackUtils.isEngineOn(chest));
                    var hover = Colors.GRAY + "H: " + HudHelper.getStatusString(JetpackUtils.isHovering(chest));

                    if (pos.side == 1) {
                        mc.font.drawShadow(matrix, fuel, pos.x - 8 - mc.font.width(fuel), pos.y - 21, 16383998);
                        mc.font.drawShadow(matrix, fuel, pos.x - 8 - mc.font.width(throttle), pos.y - 6, 16383998);
                        mc.font.drawShadow(matrix, engine, pos.x - 8 - mc.font.width(engine), pos.y + 4, 16383998);
                        mc.font.drawShadow(matrix, hover, pos.x - 8 - mc.font.width(hover), pos.y + 14, 16383998);
                    } else {
                        mc.font.drawShadow(matrix, fuel, pos.x + 6, pos.y - 21, 16383998);
                        mc.font.drawShadow(matrix, throttle, pos.x + 6, pos.y - 6, 16383998);
                        mc.font.drawShadow(matrix, engine, pos.x + 6, pos.y + 4, 16383998);
                        mc.font.drawShadow(matrix, hover, pos.x + 6, pos.y + 14, 16383998);
                    }
                }
            }
        }
    };

    @SubscribeEvent
    public void onRegisterGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAbove(VanillaGuiOverlay.HOTBAR.id(), "jetpack_hud", HUD_OVERLAY);
    }

    private static boolean isVisible(Minecraft mc) {
        return ModConfigs.ENABLE_HUD.get()
                && (ModConfigs.SHOW_HUD_OVER_CHAT.get()
                || !ModConfigs.SHOW_HUD_OVER_CHAT.get()
                && !(mc.screen instanceof ChatScreen))
                && !mc.options.hideGui
                && !mc.options.renderDebug;
    }
}
