package com.blakebr0.ironjetpacks.network.message;

import com.blakebr0.ironjetpacks.handler.SyncHandler;
import com.blakebr0.ironjetpacks.item.ItemJetpack;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageUpdateJetpack implements IMessage {

	public int entityId;
	public boolean state;
	
	public MessageUpdateJetpack() {
		
	}
	
	public MessageUpdateJetpack(int entityId, boolean state) {
		this.entityId = entityId;
		this.state = state;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.entityId = buf.readInt();
		this.state = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(entityId);
		buf.writeBoolean(state);
	}

	public static class Handler implements IMessageHandler<MessageUpdateJetpack, IMessage> {

		@Override
		public IMessage onMessage(MessageUpdateJetpack message, MessageContext ctx) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}

		private void handle(MessageUpdateJetpack message, MessageContext ctx) {
			Entity entity = FMLClientHandler.instance().getClient().world.getEntityByID(message.entityId);
			if (entity != null && entity instanceof EntityLivingBase && entity != FMLClientHandler.instance().getClient().player) {
				SyncHandler.update(message.entityId, message.state);
			}
		}
	}
}
