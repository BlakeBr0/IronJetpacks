package com.blakebr0.ironjetpacks.util;

import com.blakebr0.cucumber.helper.NBTHelper;
import com.blakebr0.ironjetpacks.handler.InputHandler;
import com.blakebr0.ironjetpacks.item.JetpackItem;
import com.blakebr0.ironjetpacks.registry.Jetpack;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public final class JetpackUtils {
	private static final IEnergyStorage EMPTY_ENERGY_STORAGE = new EnergyStorage(0);

	public static boolean isFlying(Player player) {
		ItemStack stack = player.getItemBySlot(EquipmentSlot.CHEST);
		if (!stack.isEmpty()) {
			Item item = stack.getItem();
			if (item instanceof JetpackItem) {
				JetpackItem jetpack = (JetpackItem) item;
				if (isEngineOn(stack) && (getEnergyStorage(stack).getEnergyStored() > 0 || player.isCreative() || jetpack.getJetpack().creative)) {
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

	public static ArmorMaterial makeArmorMaterial(Jetpack jetpack) {
		return new ArmorMaterial() {
			@Override
			public int getDurabilityForSlot(EquipmentSlot slot) {
				return 0;
			}

			@Override
			public int getDefenseForSlot(EquipmentSlot slot) {
				return jetpack.armorPoints;
			}

			@Override
			public int getEnchantmentValue() {
				return jetpack.enchantablilty;
			}

			@Override
			public SoundEvent getEquipSound() {
				return SoundEvents.ARMOR_EQUIP_GENERIC;
			}

			@Override
			public Ingredient getRepairIngredient() {
				return Ingredient.EMPTY;
			}

			@Override
			public String getName() {
				return "ironjetpacks:jetpack";
			}

			@Override
			public float getToughness() {
				return jetpack.toughness;
			}

			@Override
			public float getKnockbackResistance() {
				return jetpack.knockbackResistance;
			}
		};
	}
}
