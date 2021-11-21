package com.blakebr0.ironjetpacks.client.helper;

import com.blakebr0.cucumber.lib.Colors;
import com.blakebr0.ironjetpacks.config.ModConfigs;
import com.blakebr0.ironjetpacks.item.JetpackItem;
import com.blakebr0.ironjetpacks.lib.ModTooltips;
import com.blakebr0.ironjetpacks.util.JetpackUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

public final class HudHelper {
	public static HudPos getHudPos() {
		var window = Minecraft.getInstance().getWindow();
		int xOffset = ModConfigs.HUD_OFFSET_X.get();
		int yOffset = ModConfigs.HUD_OFFSET_Y.get();

		return switch (ModConfigs.HUD_POSITION.get()) {
			case 0 -> new HudPos(10 + xOffset, 30 + yOffset, 0);
			case 1 -> new HudPos(10 + xOffset, window.getGuiScaledHeight() / 2 + yOffset, 0);
			case 2 -> new HudPos(10 + xOffset, window.getGuiScaledHeight() - 30 + yOffset, 0);
			case 3 -> new HudPos(window.getGuiScaledWidth() - 8 - xOffset, 30 + yOffset, 1);
			case 4 -> new HudPos(window.getGuiScaledWidth() - 8 - xOffset, window.getGuiScaledHeight() / 2 + yOffset, 1);
			case 5 -> new HudPos(window.getGuiScaledWidth() - 8 - xOffset, window.getGuiScaledHeight() - 30 + yOffset, 1);
			default -> null;
		};

	}
	
	public static int getEnergyBarScaled(ItemStack stack) {
		var jetpack = JetpackUtils.getJetpack(stack);
		if (jetpack.creative)
			return 156;

		var energy = JetpackUtils.getEnergyStorage(stack);
		int i = energy.getEnergyStored();
		int j = energy.getMaxEnergyStored();

		return (int) (j != 0 && i != 0 ? (long) i * 156 / j : 0);
	}
	
	public static String getFuelString(ItemStack stack) {
		var jetpack = JetpackUtils.getJetpack(stack);
		if (jetpack.creative) {
			return ModTooltips.INFINITE.buildString() + Colors.GRAY + " FE";
		}

		int number = JetpackUtils.getEnergyStorage(stack).getEnergyStored();
		if (number >= 1000000000) {
			return number / 1000000000 + Colors.GRAY + "G FE";
		} else if (number >= 1000000) {
			return number / 1000000 + Colors.GRAY + "M FE";
		} else if (number >= 1000) {
			return number / 1000 + Colors.GRAY + "k FE";
		} else {
			return number + Colors.GRAY + " FE";
		}
	}
	
	public static String getStatusString(boolean on) {
		return on ? Colors.GREEN + ModTooltips.ON.buildString() : Colors.RED + ModTooltips.OFF.buildString();
	}
	
	public static class HudPos {
		public int x;
		public int y;
		public int side;
		
		public HudPos(int x, int y, int side) {
			this.x = x;
			this.y = y;
			this.side = side;
		}
	}
}
