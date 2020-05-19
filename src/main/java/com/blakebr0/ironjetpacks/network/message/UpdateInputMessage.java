package com.blakebr0.ironjetpacks.network.message;

import com.blakebr0.ironjetpacks.handler.InputHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateInputMessage {
	private final boolean up;
	private final boolean down;
	private final boolean forwards;
	private final boolean backwards;
	private final boolean left;
	private final boolean right;
	
	public UpdateInputMessage(boolean up, boolean down, boolean forwards, boolean backwards, boolean left, boolean right) {
		this.up = up;
		this.down = down;
		this.forwards = forwards;
		this.backwards = backwards;
		this.left = left;
		this.right = right;
	}

	public static UpdateInputMessage read(PacketBuffer buffer) {
		return new UpdateInputMessage(buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean());
	}

	public static void write(UpdateInputMessage message, PacketBuffer buffer) {
		buffer.writeBoolean(message.up);
		buffer.writeBoolean(message.down);
		buffer.writeBoolean(message.forwards);
		buffer.writeBoolean(message.backwards);
		buffer.writeBoolean(message.left);
		buffer.writeBoolean(message.right);
	}

	public static void onMessage(UpdateInputMessage message, Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			PlayerEntity player = context.get().getSender();
			if (player != null) {
				InputHandler.update(player, message.up, message.down, message.forwards, message.backwards, message.left, message.right);
			}
		});

		context.get().setPacketHandled(true);
	}
}
