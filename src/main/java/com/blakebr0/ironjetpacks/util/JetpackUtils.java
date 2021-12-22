package com.blakebr0.ironjetpacks.util;

import com.blakebr0.cucumber.helper.NBTHelper;
import com.blakebr0.ironjetpacks.handler.InputHandler;
import com.blakebr0.ironjetpacks.init.ModItems;
import com.blakebr0.ironjetpacks.item.ComponentItem;
import com.blakebr0.ironjetpacks.item.JetpackItem;
import com.blakebr0.ironjetpacks.registry.Jetpack;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public final class JetpackUtils {
	private static final IEnergyStorage EMPTY_ENERGY_STORAGE = new EnergyStorage(0);

	public static boolean isFlying(Player player) {
		if (player.isSpectator())
			return false;

		var stack = player.getItemBySlot(EquipmentSlot.CHEST);

		if (!stack.isEmpty()) {
			var item = stack.getItem();

			if (item instanceof JetpackItem) {
				if (!isEngineOn(stack))
					return false;

				var jetpack = JetpackUtils.getJetpack(stack);

				if (getEnergyStorage(stack).getEnergyStored() > 0 || player.isCreative() || jetpack.creative) {
					if (isHovering(stack)) {
						return !player.isOnGround();
					} else {
						return InputHandler.isHoldingUp(player);
					}
				}
			}
		}

		return false;
	}

	public static IEnergyStorage getEnergyStorage(ItemStack stack) {
		if (CapabilityEnergy.ENERGY == null)
			return EMPTY_ENERGY_STORAGE;

		return stack.getCapability(CapabilityEnergy.ENERGY).orElse(EMPTY_ENERGY_STORAGE);
	}

	public static boolean isEngineOn(ItemStack stack) {
		return NBTHelper.getBoolean(stack, "Engine");
	}

	public static boolean toggleEngine(ItemStack stack) {
		boolean current = NBTHelper.getBoolean(stack, "Engine");
		NBTHelper.flipBoolean(stack, "Engine");
		return !current;
	}

	public static boolean isHovering(ItemStack stack) {
		return NBTHelper.getBoolean(stack, "Hover");
	}

	public static boolean toggleHover(ItemStack stack) {
		boolean current = NBTHelper.getBoolean(stack, "Hover");
		NBTHelper.flipBoolean(stack, "Hover");
		return !current;
	}

	public static double getThrottle(ItemStack stack) {
		if (!NBTHelper.hasKey(stack, "Throttle")) {
			NBTHelper.setDouble(stack, "Throttle", 1.0D);
		}

		return NBTHelper.getDouble(stack, "Throttle");
	}

	public static double incrementThrottle(ItemStack stack) {
		double throttle = getThrottle(stack);
		if (throttle < 1.0D) {
			throttle = Math.min(throttle + 0.2D, 1.0D);
			NBTHelper.setDouble(stack, "Throttle", throttle);
		}

		return throttle;
	}

	public static double decrementThrottle(ItemStack stack) {
		double throttle = getThrottle(stack);
		if (throttle > 0.2D) {
			throttle = Math.max(throttle - 0.2D, 0.2D);
			NBTHelper.setDouble(stack, "Throttle", throttle);
		}

		return throttle;
	}

	public static CompoundTag makeTag(Jetpack jetpack) {
		var nbt = new CompoundTag();
		nbt.putString("Id", jetpack.getId().toString());
		return nbt;
	}

	public static ItemStack getItemForJetpack(Jetpack jetpack) {
		var nbt = makeTag(jetpack);
		var stack = new ItemStack(ModItems.JETPACK.get());

		stack.setTag(nbt);

		return stack;
	}

	public static ItemStack getItemForComponent(Item item, Jetpack jetpack) {
		var nbt = new CompoundTag();
		nbt.putString("Id", jetpack.getId().toString());

		var stack = new ItemStack(item);
		stack.setTag(nbt);

		return stack;
	}

	public static Jetpack getJetpack(ItemStack stack) {
		var id = NBTHelper.getString(stack, "Id");
		if (!id.isEmpty()) {
			return JetpackRegistry.getInstance().getJetpackById(ResourceLocation.tryParse(id));
		}

		return Jetpack.UNDEFINED;
	}
}
