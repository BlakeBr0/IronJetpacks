package com.blakebr0.ironjetpacks.handler;

import org.lwjgl.input.Keyboard;

import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.item.ItemJetpack;
import com.blakebr0.ironjetpacks.lib.Tooltips;
import com.blakebr0.ironjetpacks.network.IronNetwork;
import com.blakebr0.ironjetpacks.network.message.MessageToggleEngine;
import com.blakebr0.ironjetpacks.network.message.MessageToggleHover;
import com.blakebr0.ironjetpacks.network.message.MessageUpdateInput;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.MouseInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(modid = IronJetpacks.MOD_ID, value = Side.CLIENT)
public class KeybindHandler {

	public static KeyBinding keyEngine;
	public static KeyBinding keyHover;
	
	public static boolean up = false;
	public static boolean down = false;
	public static boolean forwards = false;
	public static boolean backwards = false;
	public static boolean left = false;
	public static boolean right = false;
	
	public static void register() {
		keyEngine = new KeyBinding("key.ij.engine", Keyboard.KEY_V, IronJetpacks.NAME);
		keyHover = new KeyBinding("key.ij.hover", Keyboard.KEY_G, IronJetpacks.NAME);
		
		ClientRegistry.registerKeyBinding(keyEngine);
		ClientRegistry.registerKeyBinding(keyHover);
	}
	
	@SubscribeEvent
	public static void onKeyInput(KeyInputEvent event) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		ItemStack chest = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		
		if (chest.getItem() instanceof ItemJetpack) {
			ItemJetpack jetpack = (ItemJetpack) chest.getItem();
			
			if (keyEngine.isPressed()) {
				boolean on = jetpack.toggleEngine(chest);
				IronNetwork.INSTANCE.sendToServer(new MessageToggleEngine());
				player.sendMessage(new TextComponentString(Tooltips.TOGGLED_ENGINE.get() + (on ? Tooltips.ON.get(10) : Tooltips.OFF.get(12))));
			}
			
			if (keyHover.isPressed()) {
				boolean on = jetpack.toggleHover(chest);
				IronNetwork.INSTANCE.sendToServer(new MessageToggleHover());
				player.sendMessage(new TextComponentString(Tooltips.TOGGLED_HOVER.get() + (on ? Tooltips.ON.get(10) : Tooltips.OFF.get(12))));
			}
		}
	}
	
	@SubscribeEvent
	public static void onMouseInput(MouseInputEvent event) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		ItemStack chest = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		
		if (chest.getItem() instanceof ItemJetpack) {
			ItemJetpack jetpack = (ItemJetpack) chest.getItem();
			
			if (keyEngine.isPressed()) {
				boolean on = jetpack.toggleEngine(chest);
				IronNetwork.INSTANCE.sendToServer(new MessageToggleEngine());
				player.sendMessage(new TextComponentString(Tooltips.TOGGLED_ENGINE.get() + (on ? Tooltips.ON.get(10) : Tooltips.OFF.get(12))));
			}
			
			if (keyHover.isPressed()) {
				boolean on = jetpack.toggleHover(chest);
				IronNetwork.INSTANCE.sendToServer(new MessageToggleHover());
				player.sendMessage(new TextComponentString(Tooltips.TOGGLED_HOVER.get() + (on ? Tooltips.ON.get(10) : Tooltips.OFF.get(12))));
			}
		}
	}
	
	/*
	 * Keyboard handling borrowed from Simply Jetpacks
	 * https://github.com/Tomson124/SimplyJetpacks-2/blob/1.12/src/main/java/tonius/simplyjetpacks/client/handler/KeyTracker.java
	 */
	@SubscribeEvent
	public static void onClientTick(ClientTickEvent event) {
		if (event.phase == Phase.START) {
			GameSettings settings = Minecraft.getMinecraft().gameSettings;
			
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
				
				IronNetwork.INSTANCE.sendToServer(new MessageUpdateInput(upNow, downNow, forwardsNow, backwardsNow, leftNow, rightNow));
				InputHandler.update(Minecraft.getMinecraft().player, upNow, downNow, forwardsNow, backwardsNow, leftNow, rightNow);
			}
		}
	}
}
