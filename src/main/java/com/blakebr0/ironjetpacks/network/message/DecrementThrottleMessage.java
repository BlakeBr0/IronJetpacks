package com.blakebr0.ironjetpacks.network.message;

import com.blakebr0.ironjetpacks.item.JetpackItem;
import com.blakebr0.ironjetpacks.util.JetpackUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public class DecrementThrottleMessage {
	public static DecrementThrottleMessage read(FriendlyByteBuf buffer) {
		return new DecrementThrottleMessage();
	}

	public static void write(DecrementThrottleMessage message, FriendlyByteBuf buffer) { }

	public static void onMessage(DecrementThrottleMessage message, Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			var player = context.get().getSender();

			if (player != null) {
				var stack = player.getItemBySlot(EquipmentSlot.CHEST);
				var item = stack.getItem();

				if (item instanceof JetpackItem) {
					JetpackUtils.decrementThrottle(stack);
				}
			}
		});

		context.get().setPacketHandled(true);
	}
}
