package com.blakebr0.ironjetpacks.util;

import com.blakebr0.cucumber.helper.NBTHelper;
import com.blakebr0.ironjetpacks.handler.InputHandler;
import com.blakebr0.ironjetpacks.item.JetpackItem;
import com.blakebr0.ironjetpacks.registry.Jetpack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public final class JetpackUtils {
	private static final IEnergyStorage EMPTY_ENERGY_STORAGE = new EnergyStorage(0);

	public static boolean isFlying(PlayerEntity player) {
		ItemStack stack = player.getItemStackFromSlot(EquipmentSlotType.CHEST);
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

	public static IArmorMaterial makeArmorMaterial(Jetpack jetpack) {
		return new IArmorMaterial() {
			@Override
			public int getDurability(EquipmentSlotType slot) {
				return 0;
			}

			@Override
			public int getDamageReductionAmount(EquipmentSlotType slot) {
				return jetpack.armorPoints;
			}

			@Override
			public int getEnchantability() {
				return jetpack.enchantablilty;
			}

			@Override
			public SoundEvent getSoundEvent() {
				return SoundEvents.ITEM_ARMOR_EQUIP_GENERIC;
			}

			@Override
			public Ingredient getRepairMaterial() {
				return null;
			}

			@Override
			public String getName() {
				return "ironjetpacks:jetpack";
			}

			@Override
			public float getToughness() {
				return 0;
			}

			@Override
			public float getKnockbackResistance() {
				return 0F;
			}
		};
	}
}
