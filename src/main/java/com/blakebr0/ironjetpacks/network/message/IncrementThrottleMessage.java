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

public class IncrementThrottleMessage {
	public static IncrementThrottleMessage read(PacketBuffer buffer) {
		return new IncrementThrottleMessage();
	}

	public static void write(IncrementThrottleMessage message, PacketBuffer buffer) {

	}

	public static void onMessage(IncrementThrottleMessage message, Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			ServerPlayerEntity player = context.get().getSender();
			if (player != null) {
				ItemStack stack = player.getItemStackFromSlot(EquipmentSlotType.CHEST);
				Item item = stack.getItem();
				if (item instanceof JetpackItem) {
					JetpackUtils.incrementThrottle(stack);
				}
			}
		});

		context.get().setPacketHandled(true);
	}
}
