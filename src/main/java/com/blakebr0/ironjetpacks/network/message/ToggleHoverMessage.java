package com.blakebr0.ironjetpacks.network.message;

import com.blakebr0.cucumber.network.message.Message;
import com.blakebr0.ironjetpacks.item.JetpackItem;
import com.blakebr0.ironjetpacks.util.JetpackUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ToggleHoverMessage extends Message<ToggleHoverMessage> {
	public ToggleHoverMessage read(FriendlyByteBuf buffer) {
		return new ToggleHoverMessage();
	}

	public void write(ToggleHoverMessage message, FriendlyByteBuf buffer) { }

	public void onMessage(ToggleHoverMessage message, Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			var player = context.get().getSender();

			if (player != null) {
				var stack = JetpackUtils.getEquippedJetpack(player);
				var item = stack.getItem();

				if (item instanceof JetpackItem) {
					JetpackUtils.toggleHover(stack);
				}
			}
		});

		context.get().setPacketHandled(true);
	}
}
