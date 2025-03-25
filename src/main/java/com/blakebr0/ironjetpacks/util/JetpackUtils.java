package com.blakebr0.ironjetpacks.util;

import com.blakebr0.ironjetpacks.client.handler.InputHandler;
import com.blakebr0.ironjetpacks.compat.curios.CuriosCompat;
import com.blakebr0.ironjetpacks.config.ModConfigs;
import com.blakebr0.ironjetpacks.init.ModDataComponentTypes;
import com.blakebr0.ironjetpacks.init.ModItems;
import com.blakebr0.ironjetpacks.item.JetpackItem;
import com.blakebr0.ironjetpacks.registry.Jetpack;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;

public final class JetpackUtils {
	private static final IEnergyStorage EMPTY_ENERGY_STORAGE = new EnergyStorage(0);

	public static boolean isFlying(Player player) {
		if (player.isSpectator())
			return false;

		var stack = getEquippedJetpack(player);

		if (!stack.isEmpty() && isEngineOn(stack)) {
			var jetpack = getJetpack(stack);
			var energy = getEnergyStorage(stack);

			if (energy.getEnergyStored() > 0 || player.isCreative() || jetpack.creative) {
				if (isHovering(stack)) {
					return !player.onGround();
				}

				return InputHandler.isHoldingUp(player);
			}
		}

		return false;
	}

	public static ItemStack getEquippedJetpack(Player player) {
		var stack = player.getItemBySlot(EquipmentSlot.CHEST);
		if (!stack.isEmpty() && stack.getItem() instanceof JetpackItem)
			return stack;

		if (ModConfigs.isCuriosEnabled()) {
			return CuriosCompat.findJetpackCurio(player).orElse(ItemStack.EMPTY);
		}

		return ItemStack.EMPTY;
	}

	public static IEnergyStorage getEnergyStorage(ItemStack stack) {
		var energy = stack.getCapability(Capabilities.EnergyStorage.ITEM);
		return energy == null ? EMPTY_ENERGY_STORAGE : energy;
	}

	public static boolean isEngineOn(ItemStack stack) {
		return stack.getOrDefault(ModDataComponentTypes.JETPACK_ENGINE, false);
	}

	public static boolean toggleEngine(ItemStack stack) {
		boolean current = stack.getOrDefault(ModDataComponentTypes.JETPACK_ENGINE, false);
		stack.set(ModDataComponentTypes.JETPACK_ENGINE, !current);
		return !current;
	}

	public static boolean isHovering(ItemStack stack) {
		return stack.getOrDefault(ModDataComponentTypes.JETPACK_HOVER, false);
	}

	public static boolean toggleHover(ItemStack stack) {
		boolean current = stack.getOrDefault(ModDataComponentTypes.JETPACK_HOVER, false);
		stack.set(ModDataComponentTypes.JETPACK_HOVER, !current);
		return !current;
	}

	public static double getThrottle(ItemStack stack) {
		return stack.getOrDefault(ModDataComponentTypes.JETPACK_THROTTLE, 1.0D);
	}

	public static double incrementThrottle(ItemStack stack) {
		double throttle = getThrottle(stack);
		if (throttle < 1.0D) {
			throttle = Math.min(throttle + 0.2D, 1.0D);
			stack.set(ModDataComponentTypes.JETPACK_THROTTLE, throttle);
		}

		return throttle;
	}

	public static double decrementThrottle(ItemStack stack) {
		double throttle = getThrottle(stack);
		if (throttle > 0.2D) {
			throttle = Math.max(throttle - 0.2D, 0.2D);
			stack.set(ModDataComponentTypes.JETPACK_THROTTLE, throttle);
		}

		return throttle;
	}

	public static ItemStack getItemForJetpack(Jetpack jetpack) {
		var stack = new ItemStack(ModItems.JETPACK.get());
		stack.set(ModDataComponentTypes.JETPACK_ID, jetpack.getId());
		return stack;
	}

	public static ItemStack getItemForComponent(Item item, Jetpack jetpack) {
		var stack = new ItemStack(item);
		stack.set(ModDataComponentTypes.JETPACK_ID, jetpack.getId());
		return stack;
	}

	public static Jetpack getJetpack(ItemStack stack) {
		var id = stack.get(ModDataComponentTypes.JETPACK_ID);
		if (id != null) {
			return JetpackRegistry.getInstance().getJetpackById(id);
		}

		return Jetpack.UNDEFINED;
	}
}
