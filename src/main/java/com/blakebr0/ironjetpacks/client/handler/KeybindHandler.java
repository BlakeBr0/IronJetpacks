package com.blakebr0.ironjetpacks.client.handler;

import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.item.JetpackItem;
import com.blakebr0.ironjetpacks.lib.ModTooltips;
import com.blakebr0.ironjetpacks.network.payloads.DecrementThrottlePayload;
import com.blakebr0.ironjetpacks.network.payloads.IncrementThrottlePayload;
import com.blakebr0.ironjetpacks.network.payloads.ToggleEnginePayload;
import com.blakebr0.ironjetpacks.network.payloads.ToggleHoverPayload;
import com.blakebr0.ironjetpacks.network.payloads.UpdateInputPayload;
import com.blakebr0.ironjetpacks.util.JetpackUtils;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

public final class KeybindHandler {
    private static KeyMapping keyEngine;
    private static KeyMapping keyHover;
    private static KeyMapping keyAscend;
    private static KeyMapping keyDescend;
    private static KeyMapping keyIncrementThrottle;
    private static KeyMapping keyDecrementThrottle;

    private static boolean up = false;
    private static boolean down = false;
    private static boolean forwards = false;
    private static boolean backwards = false;
    private static boolean left = false;
    private static boolean right = false;
    private static boolean sprint = false;

    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        keyEngine = new KeyMapping("keybind.ironjetpacks.engine", GLFW.GLFW_KEY_V, IronJetpacks.NAME);
        keyHover = new KeyMapping("keybind.ironjetpacks.hover", GLFW.GLFW_KEY_H, IronJetpacks.NAME);
        keyAscend = new KeyMapping("keybind.ironjetpacks.ascend", InputConstants.UNKNOWN.getValue(), IronJetpacks.NAME);
        keyDescend = new KeyMapping("keybind.ironjetpacks.descend", InputConstants.UNKNOWN.getValue(), IronJetpacks.NAME);
        keyIncrementThrottle = new KeyMapping("keybind.ironjetpacks.increment_throttle", GLFW.GLFW_KEY_PERIOD, IronJetpacks.NAME);
        keyDecrementThrottle = new KeyMapping("keybind.ironjetpacks.decrement_throttle", GLFW.GLFW_KEY_COMMA, IronJetpacks.NAME);

        event.register(keyEngine);
        event.register(keyHover);
        event.register(keyAscend);
        event.register(keyDescend);
        event.register(keyIncrementThrottle);
        event.register(keyDecrementThrottle);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.Key event) {
        var player = Minecraft.getInstance().player;
        if (player == null)
            return;

        var chest = JetpackUtils.getEquippedJetpack(player);
        var item = chest.getItem();

        if (item instanceof JetpackItem) {
            handleInput(player, chest);
        }
    }

    @SubscribeEvent
    public void onMouseInput(InputEvent.MouseButton.Pre event) {
        var player = Minecraft.getInstance().player;
        if (player == null)
            return;

        var chest = JetpackUtils.getEquippedJetpack(player);
        var item = chest.getItem();

        if (item instanceof JetpackItem) {
            handleInput(player, chest);
        }
    }

    /*
     * Keyboard handling borrowed from Simply Jetpacks
     * https://github.com/Tomson124/SimplyJetpacks-2/blob/1.12/src/main/java/tonius/simplyjetpacks/client/handler/KeyTracker.java
     */
    @SubscribeEvent
    public void onClientTick(ClientTickEvent.Pre event) {
        var mc = Minecraft.getInstance();
        var settings = mc.options;

        if (mc.getConnection() == null)
            return;

        boolean upNow = keyAscend.isUnbound() ? settings.keyJump.isDown() : keyAscend.isDown();
        boolean downNow = keyDescend.isUnbound() ? settings.keyShift.isDown() : keyDescend.isDown();
        boolean forwardsNow = settings.keyUp.isDown();
        boolean backwardsNow = settings.keyDown.isDown();
        boolean leftNow = settings.keyLeft.isDown();
        boolean rightNow = settings.keyRight.isDown();
        boolean sprintNow = settings.keySprint.isDown();

        if (upNow != up || downNow != down || forwardsNow != forwards || backwardsNow != backwards || leftNow != left || rightNow != right || sprintNow != sprint) {
            up = upNow;
            down = downNow;
            forwards = forwardsNow;
            backwards = backwardsNow;
            left = leftNow;
            right = rightNow;
            sprint = sprintNow;

            update(up, down, forwards, backwards, left, right, sprint);
        }
    }

    public static void update(boolean up, boolean down, boolean forwards, boolean backwards, boolean left, boolean right, boolean sprint) {
        var player = Minecraft.getInstance().player;

        PacketDistributor.sendToServer(new UpdateInputPayload(up, down, forwards, backwards, left, right, sprint));
        InputHandler.update(player, up, down, forwards, backwards, left, right, sprint);
    }

    private static void handleInput(Player player, ItemStack stack) {
        if (keyEngine.consumeClick()) {
            boolean on = JetpackUtils.toggleEngine(stack);
            var state = on ? ModTooltips.ON.color(ChatFormatting.GREEN).build() : ModTooltips.OFF.color(ChatFormatting.RED).build();
            PacketDistributor.sendToServer(new ToggleEnginePayload());
            player.displayClientMessage(ModTooltips.TOGGLE_ENGINE.args(state).build(), true);
        }

        if (keyHover.consumeClick()) {
            boolean on = JetpackUtils.toggleHover(stack);
            var state = on ? ModTooltips.ON.color(ChatFormatting.GREEN).build() : ModTooltips.OFF.color(ChatFormatting.RED).build();
            PacketDistributor.sendToServer(new ToggleHoverPayload());
            player.displayClientMessage(ModTooltips.TOGGLE_HOVER.args(state).build(), true);
        }

        if (keyIncrementThrottle.consumeClick()) {
            double throttle = JetpackUtils.incrementThrottle(stack);
            var throttleText = Component.literal((int) (throttle * 100) + "%").withStyle(ChatFormatting.GREEN);
            PacketDistributor.sendToServer(new IncrementThrottlePayload());
            player.displayClientMessage(ModTooltips.CHANGE_THROTTLE.args(throttleText).build(), true);
        }

        if (keyDecrementThrottle.consumeClick()) {
            double throttle = JetpackUtils.decrementThrottle(stack);
            var throttleText = Component.literal((int) (throttle * 100) + "%").withStyle(ChatFormatting.RED);
            PacketDistributor.sendToServer(new DecrementThrottlePayload());
            player.displayClientMessage(ModTooltips.CHANGE_THROTTLE.args(throttleText).build(), true);
        }
    }
}
