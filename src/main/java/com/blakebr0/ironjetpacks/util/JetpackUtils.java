package com.blakebr0.ironjetpacks.util;

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

public final class JetpackUtils {
	public static boolean isFlying(PlayerEntity player) {
		ItemStack stack = player.getItemStackFromSlot(EquipmentSlotType.CHEST);
		if (!stack.isEmpty()) {
			Item item = stack.getItem();
			if (item instanceof JetpackItem) {
				JetpackItem jetpack = (JetpackItem) item;
				if (jetpack.isEngineOn(stack) && (jetpack.getEnergyStorage(stack).getEnergyStored() > 0 || player.isCreative() || jetpack.getJetpack().creative)) {
					if (jetpack.isHovering(stack)) {
						return !player.func_233570_aj_();
					} else {
						return InputHandler.isHoldingUp(player);
					}
				}
			}
		}

		return false;
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
			public float func_230304_f_() {
				return 0F;
			}
		};
	}
}
