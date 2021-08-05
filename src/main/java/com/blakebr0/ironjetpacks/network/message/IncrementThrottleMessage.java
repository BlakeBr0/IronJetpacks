package com.blakebr0.ironjetpacks.network.message;

import com.blakebr0.ironjetpacks.item.JetpackItem;
import com.blakebr0.ironjetpacks.util.JetpackUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class IncrementThrottleMessage {
	public static IncrementThrottleMessage read(FriendlyByteBuf buffer) {
		return new IncrementThrottleMessage();
	}

	public static void write(IncrementThrottleMessage message, FriendlyByteBuf buffer) {

	}

	public static void onMessage(IncrementThrottleMessage message, Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			ServerPlayer player = context.get().getSender();
			if (player != null) {
				ItemStack stack = player.getItemBySlot(EquipmentSlot.CHEST);
				Item item = stack.getItem();
				if (item instanceof JetpackItem) {
					JetpackUtils.incrementThrottle(stack);
				}
			}
		});

		context.get().setPacketHandled(true);
	}
}
