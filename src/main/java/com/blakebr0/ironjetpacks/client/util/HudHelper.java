package com.blakebr0.ironjetpacks.client.util;

import com.blakebr0.ironjetpacks.config.ModConfig;
import com.blakebr0.ironjetpacks.item.ItemJetpack;
import com.blakebr0.ironjetpacks.lib.Tooltips;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.IEnergyStorage;

public class HudHelper {
	
	private static Minecraft mc = Minecraft.getMinecraft();
	
	public static HudPos getHudPos() {
		ScaledResolution res = new ScaledResolution(mc);
		int xOffset = ModConfig.confHudOffsetX;
		int yOffset = ModConfig.confHudOffsetY;
		
		switch (ModConfig.confHudPosMode) {
		case 0:
			return new HudPos(10 + xOffset, 30 + yOffset);
		case 1:
			return new HudPos(10 + xOffset, res.getScaledHeight() / 2 + yOffset);
		case 2:
			return new HudPos(10 + xOffset, res.getScaledHeight() - 30 + yOffset);
		}
		return null;
	}
	
	public static int getEnergyBarScaled(ItemJetpack jetpack, ItemStack stack) {
		if (jetpack.getJetpackType().creative) return 156;
		IEnergyStorage energy = jetpack.getEnergyStorage(stack);
		int i = energy.getEnergyStored();
		int j = energy.getMaxEnergyStored();
		return (int) (j != 0 && i != 0 ? (long) i * 156 / j : 0);
	}
	
	public static String getFuel(ItemJetpack jetpack, ItemStack stack) {
		if (jetpack.getJetpackType().creative) return Tooltips.INFINITE.get() + " FE";
		int number = jetpack.getEnergyStorage(stack).getEnergyStored();
		if (number >= 1000000000) {
			return number / 1000000000 + "G FE";
		} else if (number >= 1000000) {
			return number / 1000000 + "M FE";
		} else if (number >= 1000) {
			return number / 1000 + "k FE";
		} else {
			return String.valueOf(number) + " FE";
		}
	}
	
	public static String getOn(boolean on) {
		return on ? Tooltips.ON.get(10) : Tooltips.OFF.get(12);
	}
	
	public static class HudPos {
		
		public int x;
		public int y;
		int side;
		
		public HudPos(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
}
