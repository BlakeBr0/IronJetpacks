package com.blakebr0.ironjetpacks.handler;

import java.util.HashMap;
import java.util.Map;

import com.blakebr0.ironjetpacks.IronJetpacks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

/*
 * Keyboard handling system borrowed from Simply Jetpacks
 * https://github.com/Tomson124/SimplyJetpacks-2/blob/1.12/src/main/java/tonius/simplyjetpacks/handler/SyncHandler.java
 */
@EventBusSubscriber(modid = IronJetpacks.MOD_ID)
public class InputHandler {

	public static final Map<EntityPlayer, Boolean> holdingUp = new HashMap<>();
	public static final Map<EntityPlayer, Boolean> holdingDown = new HashMap<>();
	public static final Map<EntityPlayer, Boolean> holdingForwards = new HashMap<>();
	public static final Map<EntityPlayer, Boolean> holdingBackwards = new HashMap<>();
	public static final Map<EntityPlayer, Boolean> holdingLeft = new HashMap<>();
	public static final Map<EntityPlayer, Boolean> holdingRight = new HashMap<>();
	
	public static boolean isHoldingUp(EntityPlayer player) {
		return holdingUp.containsKey(player) && holdingUp.get(player);
	}
	
	public static boolean isHoldingDown(EntityPlayer player) {
		return holdingDown.containsKey(player) && holdingDown.get(player);
	}
	
	public static boolean isHoldingForwards(EntityPlayer player) {
		return holdingForwards.containsKey(player) && holdingForwards.get(player);
	}
	
	public static boolean isHoldingBackwards(EntityPlayer player) {
		return holdingBackwards.containsKey(player) && holdingBackwards.get(player);
	}
	
	public static boolean isHoldingLeft(EntityPlayer player) {
		return holdingLeft.containsKey(player) && holdingLeft.get(player);
	}
	
	public static boolean isHoldingRight(EntityPlayer player) {
		return holdingRight.containsKey(player) && holdingRight.get(player);
	}
	
	public static void update(EntityPlayer player, boolean up, boolean down, boolean forwards, boolean backwards, boolean left, boolean right) {
		holdingUp.put(player, up);
		holdingDown.put(player, down);
		holdingForwards.put(player, forwards);
		holdingBackwards.put(player, backwards);
		holdingLeft.put(player, left);
		holdingRight.put(player, right);
	}
	
	public static void remove(EntityPlayer player) {
		holdingUp.remove(player);
		holdingDown.remove(player);
		holdingForwards.remove(player);
		holdingBackwards.remove(player);
		holdingLeft.remove(player);
		holdingRight.remove(player);
	}
	
	public static void clear() {
		holdingUp.clear();
		holdingDown.clear();
		holdingForwards.clear();
		holdingBackwards.clear();
		holdingLeft.clear();
		holdingRight.clear();
	}
	
	@SubscribeEvent
	public static void onLogout(PlayerLoggedOutEvent event) {
		remove(event.player);
	}
	
	@SubscribeEvent
	public static void onChangeDimension(PlayerChangedDimensionEvent event) {
		remove(event.player);
	}
}
