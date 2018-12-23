package com.blakebr0.ironjetpacks.util;

import com.blakebr0.ironjetpacks.handler.InputHandler;
import com.blakebr0.ironjetpacks.item.ItemJetpack;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class JetpackUtils {

	public static boolean isFlying(EntityPlayer player) {
		ItemStack stack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		if (!stack.isEmpty()) {
			if (stack.getItem() instanceof ItemJetpack) {
				ItemJetpack jetpack = (ItemJetpack) stack.getItem();
				if (jetpack.isEngineOn(stack) && (jetpack.getEnergyStorage(stack).getEnergyStored() > 0 
						|| player.capabilities.isCreativeMode || jetpack.getJetpack().creative)) {
					if (jetpack.isHovering(stack)) {
						return !player.onGround;
					} else {
						return InputHandler.isHoldingUp(player);
					}
				}
			}
		}
		return false;
	}
}
