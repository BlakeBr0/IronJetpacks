package com.blakebr0.ironjetpacks.network.message;

import com.blakebr0.ironjetpacks.item.JetpackItem;
import com.blakebr0.ironjetpacks.util.JetpackUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ToggleEngineMessage {
	public static ToggleEngineMessage read(PacketBuffer buffer) {
		return new ToggleEngineMessage();
	}

	public static void write(ToggleEngineMessage message, PacketBuffer buffer) {

	}

	public static void onMessage(ToggleEngineMessage message, Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			ServerPlayerEntity player = context.get().getSender();
			if (player != null) {
				ItemStack stack = player.getItemBySlot(EquipmentSlotType.CHEST);
				Item item = stack.getItem();
				if (item instanceof JetpackItem) {
					JetpackUtils.toggleEngine(stack);
				}
			}
		});

		context.get().setPacketHandled(true);
	}
}
