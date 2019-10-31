package com.blakebr0.ironjetpacks.handler;

import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.item.JetpackItem;
import com.blakebr0.ironjetpacks.lib.ModTooltips;
import com.blakebr0.ironjetpacks.network.NetworkHandler;
import com.blakebr0.ironjetpacks.network.message.ToggleEngineMessage;
import com.blakebr0.ironjetpacks.network.message.ToggleHoverMessage;
import com.blakebr0.ironjetpacks.network.message.UpdateInputMessage;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

public class KeybindHandler {
	private static KeyBinding keyEngine;
	private static KeyBinding keyHover;
	
	private static boolean up = false;
	private static boolean down = false;
	private static boolean forwards = false;
	private static boolean backwards = false;
	private static boolean left = false;
	private static boolean right = false;
	
	public static void onClientSetup() {
		keyEngine = new KeyBinding("key.ironjetpacks.engine", GLFW.GLFW_KEY_V, IronJetpacks.NAME);
		keyHover = new KeyBinding("key.ironjetpacks.hover", GLFW.GLFW_KEY_G, IronJetpacks.NAME);
		
		ClientRegistry.registerKeyBinding(keyEngine);
		ClientRegistry.registerKeyBinding(keyHover);
	}
	
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		PlayerEntity player = Minecraft.getInstance().player;
		if (player == null)
			return;

		ItemStack chest = player.getItemStackFromSlot(EquipmentSlotType.CHEST);
		Item item = chest.getItem();
		
		if (item instanceof JetpackItem) {
			JetpackItem jetpack = (JetpackItem) item;
			
			if (keyEngine.isPressed()) {
				boolean on = jetpack.toggleEngine(chest);
				NetworkHandler.INSTANCE.sendToServer(new ToggleEngineMessage());
				ITextComponent state = on ? ModTooltips.ON.color(TextFormatting.GREEN).build() : ModTooltips.OFF.color(TextFormatting.RED).build();
				player.sendStatusMessage(ModTooltips.TOGGLE_ENGINE.args(state).build(), true);
			}
			
			if (keyHover.isPressed()) {
				boolean on = jetpack.toggleHover(chest);
				ITextComponent state = on ? ModTooltips.ON.color(TextFormatting.GREEN).build() : ModTooltips.OFF.color(TextFormatting.RED).build();
				NetworkHandler.INSTANCE.sendToServer(new ToggleHoverMessage());
				player.sendStatusMessage(ModTooltips.TOGGLE_HOVER.args(state).build(), true);
			}
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
			JetpackItem jetpack = (JetpackItem) item;
			
			if (keyEngine.isPressed()) {
				boolean on = jetpack.toggleEngine(chest);
				ITextComponent state = on ? ModTooltips.ON.color(TextFormatting.GREEN).build() : ModTooltips.OFF.color(TextFormatting.RED).build();
				NetworkHandler.INSTANCE.sendToServer(new ToggleEngineMessage());
				player.sendStatusMessage(ModTooltips.TOGGLE_ENGINE.args(state).build(), true);
			}
			
			if (keyHover.isPressed()) {
				boolean on = jetpack.toggleHover(chest);
				ITextComponent state = on ? ModTooltips.ON.color(TextFormatting.GREEN).build() : ModTooltips.OFF.color(TextFormatting.RED).build();
				NetworkHandler.INSTANCE.sendToServer(new ToggleHoverMessage());
				player.sendStatusMessage(ModTooltips.TOGGLE_HOVER.args(state).build(), true);
			}
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
			boolean downNow = settings.keyBindSneak.isKeyDown();
			boolean forwardsNow = settings.keyBindForward.isKeyDown();
			boolean backwardsNow = settings.keyBindBack.isKeyDown();
			boolean leftNow = settings.keyBindLeft.isKeyDown();
			boolean rightNow = settings.keyBindRight.isKeyDown();
			
			if (upNow != up || downNow != down || forwardsNow != forwards || backwardsNow != backwards || leftNow != left || rightNow != right) {
				up = upNow;
				down = downNow;
				forwards = forwardsNow;
				backwards = backwardsNow;
				left = leftNow;
				right = rightNow;

				NetworkHandler.INSTANCE.sendToServer(new UpdateInputMessage(upNow, downNow, forwardsNow, backwardsNow, leftNow, rightNow));
				InputHandler.update(mc.player, upNow, downNow, forwardsNow, backwardsNow, leftNow, rightNow);
			}
		}
	}
}
