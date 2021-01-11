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
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

public final class KeybindHandler {
	private static KeyBinding keyEngine;
	private static KeyBinding keyHover;
	private static KeyBinding keyDescend;
	private static KeyBinding keyIncrementThrottle;
	private static KeyBinding keyDecrementThrottle;

	private static boolean up = false;
	private static boolean down = false;
	private static boolean forwards = false;
	private static boolean backwards = false;
	private static boolean left = false;
	private static boolean right = false;
	private static boolean sprint = false;
	
	public static void onClientSetup() {
		keyEngine = new KeyBinding("keybind.ironjetpacks.engine", GLFW.GLFW_KEY_V, IronJetpacks.NAME);
		keyHover = new KeyBinding("keybind.ironjetpacks.hover", GLFW.GLFW_KEY_G, IronJetpacks.NAME);
		keyDescend = new KeyBinding("keybind.ironjetpacks.descend", InputMappings.INPUT_INVALID.getKeyCode(), IronJetpacks.NAME);
		keyIncrementThrottle = new KeyBinding("keybind.ironjetpacks.increment_throttle", GLFW.GLFW_KEY_PERIOD, IronJetpacks.NAME);
		keyDecrementThrottle = new KeyBinding("keybinding.ironjetpacks.decrement_throttle", GLFW.GLFW_KEY_COMMA, IronJetpacks.NAME);
		
		ClientRegistry.registerKeyBinding(keyEngine);
		ClientRegistry.registerKeyBinding(keyHover);
		ClientRegistry.registerKeyBinding(keyDescend);
		ClientRegistry.registerKeyBinding(keyIncrementThrottle);
		ClientRegistry.registerKeyBinding(keyDecrementThrottle);
	}
	
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		PlayerEntity player = Minecraft.getInstance().player;
		if (player == null)
			return;

		ItemStack chest = player.getItemStackFromSlot(EquipmentSlotType.CHEST);
		Item item = chest.getItem();
		
		if (item instanceof JetpackItem) {
			handleInput(player, chest);
		}
	}
	
	@SubscribeEvent
	public void onMouseInput(InputEvent.MouseInputEvent event) {
		PlayerEntity player = Minecraft.getInstance().player;
		if (player == null)
			return;

		ItemStack chest = player.getItemStackFromSlot(EquipmentSlotType.CHEST);
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
			GameSettings settings = mc.gameSettings;

			if (mc.getConnection() == null)
				return;
			
			boolean upNow = settings.keyBindJump.isKeyDown();
			boolean downNow = keyDescend.isInvalid() ? settings.keyBindSneak.isKeyDown() : keyDescend.isKeyDown();
			boolean forwardsNow = settings.keyBindForward.isKeyDown();
			boolean backwardsNow = settings.keyBindBack.isKeyDown();
			boolean leftNow = settings.keyBindLeft.isKeyDown();
			boolean rightNow = settings.keyBindRight.isKeyDown();
			boolean sprintNow = settings.keyBindSprint.isKeyDown();
			
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

	private static void handleInput(PlayerEntity player, ItemStack stack) {
		if (keyEngine.isPressed()) {
			boolean on = JetpackUtils.toggleEngine(stack);
			ITextComponent state = on ? ModTooltips.ON.color(TextFormatting.GREEN).build() : ModTooltips.OFF.color(TextFormatting.RED).build();
			NetworkHandler.INSTANCE.sendToServer(new ToggleEngineMessage());
			player.sendStatusMessage(ModTooltips.TOGGLE_ENGINE.args(state).build(), true);
		}

		if (keyHover.isPressed()) {
			boolean on = JetpackUtils.toggleHover(stack);
			ITextComponent state = on ? ModTooltips.ON.color(TextFormatting.GREEN).build() : ModTooltips.OFF.color(TextFormatting.RED).build();
			NetworkHandler.INSTANCE.sendToServer(new ToggleHoverMessage());
			player.sendStatusMessage(ModTooltips.TOGGLE_HOVER.args(state).build(), true);
		}

		if (keyIncrementThrottle.isPressed()) {
			double throttle = JetpackUtils.incrementThrottle(stack);
			IFormattableTextComponent throttleText = new StringTextComponent((int) (throttle * 100) + "%").mergeStyle(TextFormatting.GREEN);
			NetworkHandler.INSTANCE.sendToServer(new IncrementThrottleMessage());
			player.sendStatusMessage(ModTooltips.CHANGE_THROTTLE.args(throttleText).build(), true);
		}

		if (keyDecrementThrottle.isPressed()) {
			double throttle = JetpackUtils.decrementThrottle(stack);
			IFormattableTextComponent throttleText = new StringTextComponent((int) (throttle * 100) + "%").mergeStyle(TextFormatting.RED);
			NetworkHandler.INSTANCE.sendToServer(new DecrementThrottleMessage());
			player.sendStatusMessage(ModTooltips.CHANGE_THROTTLE.args(throttleText).build(), true);
		}
	}
}
