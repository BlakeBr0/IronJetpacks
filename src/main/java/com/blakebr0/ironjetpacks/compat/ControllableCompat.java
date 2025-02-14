package com.blakebr0.ironjetpacks.compat;

import com.blakebr0.ironjetpacks.client.handler.KeybindHandler;
import com.mrcrayfish.controllable.Controllable;
import com.mrcrayfish.controllable.client.binding.ButtonBindings;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;

public final class ControllableCompat {
    private static boolean up = false;
    private static boolean down = false;
    private static boolean forwards = false;
    private static boolean backwards = false;
    private static boolean left = false;
    private static boolean right = false;
    private static boolean sprint = false;

    @SubscribeEvent
    public void onClientTick(ClientTickEvent.Pre event) {
        var mc = Minecraft.getInstance();
        if (mc.getConnection() == null)
            return;

        var controller = Controllable.getController();

        var forwardsNow = controller != null && controller.getLThumbStickYValue() < -0.5;
        var backwardsNow = controller != null && controller.getLThumbStickYValue() > 0.5;
        var leftNow = controller != null && controller.getLThumbStickXValue() < -0.5;
        var rightNow = controller != null && controller.getLThumbStickXValue() > 0.5;

        var keyAscend = getKeyMapping("keybind.ironjetpacks.ascend.custom");
        var keyDescend = getKeyMapping("keybind.ironjetpacks.descend.custom");

        var upNow = keyAscend != null ? keyAscend.isDown() : ButtonBindings.JUMP.isButtonDown();
        var downNow = keyDescend != null ? keyDescend.isDown() : ButtonBindings.SNEAK.isButtonDown();
        var sprintNow = ButtonBindings.SPRINT.isButtonDown();

        if (upNow != up || downNow != down || forwardsNow != forwards || backwardsNow != backwards || leftNow != left || rightNow != right || sprintNow != sprint) {
            up = upNow;
            down = downNow;
            forwards = forwardsNow;
            backwards = backwardsNow;
            left = leftNow;
            right = rightNow;
            sprint = sprintNow;

            KeybindHandler.update(up, down, forwards, backwards, left, right, sprint);
        }
    }

    private static KeyMapping getKeyMapping(String key) {
        var adapter = Controllable.getBindingRegistry().getKeyAdapterByDescriptionKey(key);
        return adapter != null ? adapter.getKeyMapping() : null;
    }
}
