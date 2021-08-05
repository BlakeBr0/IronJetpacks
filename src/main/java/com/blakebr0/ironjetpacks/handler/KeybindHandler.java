package com.blakebr0.ironjetpacks.handler;

import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.item.JetpackItem;
import com.blakebr0.ironjetpacks.lib.ModTooltips;
import com.blakebr0.ironjetpacks.network.NetworkHandler;
import com.blakebr0.ironjetpacks.network.message.DecrementThrottleMessage;
import com.blakebr0.ironjetpacks.network.message.IncrementThrottleMessage;
import com.blakebr0.ironjetpacks.network.message.ToggleEngineMessage;
import com.blakebr0.ironjetpacks.network.message.ToggleHoverMessage;
import com.blakebr0.ironjetpacks.network.message.UpdateInputMessage;
import com.blakebr0.ironjetpacks.util.JetpackUtils;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

public final class KeybindHandler {
	private static KeyMapping keyEngine;
	private static KeyMapping keyHover;
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
	
	public static void onClientSetup() {
		keyEngine = new KeyMapping("keybind.ironjetpacks.engine", GLFW.GLFW_KEY_V, IronJetpacks.NAME);
		keyHover = new KeyMapping("keybind.ironjetpacks.hover", GLFW.GLFW_KEY_G, IronJetpacks.NAME);
		keyDescend = new KeyMapping("keybind.ironjetpacks.descend", InputConstants.UNKNOWN.getValue(), IronJetpacks.NAME);
		keyIncrementThrottle = new KeyMapping("keybind.ironjetpacks.increment_throttle", GLFW.GLFW_KEY_PERIOD, IronJetpacks.NAME);
		keyDecrementThrottle = new KeyMapping("keybinding.ironjetpacks.decrement_throttle", GLFW.GLFW_KEY_COMMA, IronJetpacks.NAME);
		
		ClientRegistry.registerKeyBinding(keyEngine);
		ClientRegistry.registerKeyBinding(keyHover);
		ClientRegistry.registerKeyBinding(keyDescend);
		ClientRegistry.registerKeyBinding(keyIncrementThrottle);
		ClientRegistry.registerKeyBinding(keyDecrementThrottle);
	}
	
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		Player player = Minecraft.getInstance().player;
		if (player == null)
			return;

		ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
		Item item = chest.getItem();
		
		if (item instanceof JetpackItem) {
			handleInput(player, chest);
		}
	}
	
	@SubscribeEvent
	public void onMouseInput(InputEvent.MouseInputEvent event) {
		Player player = Minecraft.getInstance().player;
		if (player == null)
			return;

		ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
		Item item = chest.getItem();

		if (item instanceof JetpackItem) {
			handleInput(player, chest);
		}
	}
	
	/*
	 * Keyboard handling borrowed from Simply Jetpacks
	 * https://github.com/Tomson124/SimplyJetpacks-2/blob/1.12/src/main/java/tonius/simplyjetpacks/client/handler/KeyTracker.java
	 */
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.START) {
			Minecraft mc = Minecraft.getInstance();
			Options settings = mc.options;

			if (mc.getConnection() == null)
				return;
			
			boolean upNow = settings.keyJump.isDown();
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

				NetworkHandler.INSTANCE.sendToServer(new UpdateInputMessage(upNow, downNow, forwardsNow, backwardsNow, leftNow, rightNow, sprintNow));
				InputHandler.update(mc.player, upNow, downNow, forwardsNow, backwardsNow, leftNow, rightNow, sprintNow);
			}
		}
	}

	private static void handleInput(Player player, ItemStack stack) {
		if (keyEngine.consumeClick()) {
			boolean on = JetpackUtils.toggleEngine(stack);
			Component state = on ? ModTooltips.ON.color(ChatFormatting.GREEN).build() : ModTooltips.OFF.color(ChatFormatting.RED).build();
			NetworkHandler.INSTANCE.sendToServer(new ToggleEngineMessage());
			player.displayClientMessage(ModTooltips.TOGGLE_ENGINE.args(state).build(), true);
		}

		if (keyHover.consumeClick()) {
			boolean on = JetpackUtils.toggleHover(stack);
			Component state = on ? ModTooltips.ON.color(ChatFormatting.GREEN).build() : ModTooltips.OFF.color(ChatFormatting.RED).build();
			NetworkHandler.INSTANCE.sendToServer(new ToggleHoverMessage());
			player.displayClientMessage(ModTooltips.TOGGLE_HOVER.args(state).build(), true);
		}

		if (keyIncrementThrottle.consumeClick()) {
			double throttle = JetpackUtils.incrementThrottle(stack);
			MutableComponent throttleText = new TextComponent((int) (throttle * 100) + "%").withStyle(ChatFormatting.GREEN);
			NetworkHandler.INSTANCE.sendToServer(new IncrementThrottleMessage());
			player.displayClientMessage(ModTooltips.CHANGE_THROTTLE.args(throttleText).build(), true);
		}

		if (keyDecrementThrottle.consumeClick()) {
			double throttle = JetpackUtils.decrementThrottle(stack);
			MutableComponent throttleText = new TextComponent((int) (throttle * 100) + "%").withStyle(ChatFormatting.RED);
			NetworkHandler.INSTANCE.sendToServer(new DecrementThrottleMessage());
			player.displayClientMessage(ModTooltips.CHANGE_THROTTLE.args(throttleText).build(), true);
		}
	}
}
