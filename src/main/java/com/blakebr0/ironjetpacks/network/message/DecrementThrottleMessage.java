package com.blakebr0.ironjetpacks.network.message;

import com.blakebr0.cucumber.network.message.Message;
import com.blakebr0.ironjetpacks.item.JetpackItem;
import com.blakebr0.ironjetpacks.util.JetpackUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DecrementThrottleMessage extends Message<DecrementThrottleMessage> {
	public DecrementThrottleMessage read(FriendlyByteBuf buffer) {
		return new DecrementThrottleMessage();
	}

	public void write(DecrementThrottleMessage message, FriendlyByteBuf buffer) { }

	public void onMessage(DecrementThrottleMessage message, Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			var player = context.get().getSender();

			if (player != null) {
				var stack = JetpackUtils.getEquippedJetpack(player);
				var item = stack.getItem();

				if (item instanceof JetpackItem) {
					JetpackUtils.decrementThrottle(stack);
				}
			}
		});

		context.get().setPacketHandled(true);
	}
}
