package com.blakebr0.ironjetpacks.network.message;

import com.blakebr0.ironjetpacks.handler.InputHandler;
import com.blakebr0.ironjetpacks.item.ItemJetpack;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageUpdateInput implements IMessage {

	public boolean up;
	public boolean down;
	public boolean forwards;
	public boolean backwards;
	public boolean left;
	public boolean right;
	
	public MessageUpdateInput() {
		
	}
	
	public MessageUpdateInput(boolean up, boolean down, boolean forwards, boolean backwards, boolean left, boolean right) {
		this.up = up;
		this.down = down;
		this.forwards = forwards;
		this.backwards = backwards;
		this.left = left;
		this.right = right;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.up = buf.readBoolean();
		this.down = buf.readBoolean();
		this.forwards = buf.readBoolean();
		this.backwards = buf.readBoolean();
		this.left = buf.readBoolean();
		this.right = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(this.up);
		buf.writeBoolean(this.down);
		buf.writeBoolean(this.forwards);
		buf.writeBoolean(this.backwards);
		buf.writeBoolean(this.left);
		buf.writeBoolean(this.right);
	}

	public static class Handler implements IMessageHandler<MessageUpdateInput, IMessage> {

		@Override
		public IMessage onMessage(MessageUpdateInput message, MessageContext ctx) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}

		private void handle(MessageUpdateInput message, MessageContext ctx) {
			EntityPlayer player = ctx.getServerHandler().player;
			if (player != null) {
				InputHandler.update(player, message.up, message.down, message.forwards, message.backwards, message.left, message.right);
			}
		}
	}
}
