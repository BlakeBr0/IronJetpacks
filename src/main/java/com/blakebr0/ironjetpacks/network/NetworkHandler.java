package com.blakebr0.ironjetpacks.network;

import com.blakebr0.ironjetpacks.network.message.AcknowledgeMessage;
import com.blakebr0.ironjetpacks.network.message.DecrementThrottleMessage;
import com.blakebr0.ironjetpacks.network.message.IncrementThrottleMessage;
import com.blakebr0.ironjetpacks.network.message.SyncJetpacksMessage;
import com.blakebr0.ironjetpacks.network.message.ToggleEngineMessage;
import com.blakebr0.ironjetpacks.network.message.ToggleHoverMessage;
import com.blakebr0.ironjetpacks.network.message.UpdateInputMessage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.FMLHandshakeHandler;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class NetworkHandler {
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation("ironjetpacks", "ironjetpacks"), () -> "1.0", (s) -> true, (s) -> true);
	private static int id = 0;

	public static void onCommonSetup() {
		INSTANCE.registerMessage(id(), ToggleHoverMessage.class, ToggleHoverMessage::write, ToggleHoverMessage::read, ToggleHoverMessage::onMessage);
		INSTANCE.registerMessage(id(), UpdateInputMessage.class, UpdateInputMessage::write, UpdateInputMessage::read, UpdateInputMessage::onMessage);
		INSTANCE.registerMessage(id(), ToggleEngineMessage.class, ToggleEngineMessage::write, ToggleEngineMessage::read, ToggleEngineMessage::onMessage);
		INSTANCE.registerMessage(id(), IncrementThrottleMessage.class, IncrementThrottleMessage::write, IncrementThrottleMessage::read, IncrementThrottleMessage::onMessage);
		INSTANCE.registerMessage(id(), DecrementThrottleMessage.class, DecrementThrottleMessage::write, DecrementThrottleMessage::read, DecrementThrottleMessage::onMessage);

		INSTANCE.messageBuilder(SyncJetpacksMessage.class, id())
				.loginIndex(SyncJetpacksMessage::getLoginIndex, SyncJetpacksMessage::setLoginIndex)
				.encoder(SyncJetpacksMessage::write)
				.decoder(SyncJetpacksMessage::read)
				.consumer((message, context) -> {
					BiConsumer<SyncJetpacksMessage, Supplier<NetworkEvent.Context>> handler;
					if (context.get().getDirection().getReceptionSide().isServer()) {
						handler = FMLHandshakeHandler.indexFirst((handshake, msg, ctx) -> SyncJetpacksMessage.onMessage(msg, ctx));
					} else {
						handler = SyncJetpacksMessage::onMessage;
					}

					handler.accept(message, context);
				})
				.markAsLoginPacket()
				.add();

		INSTANCE.messageBuilder(AcknowledgeMessage.class, id())
				.loginIndex(AcknowledgeMessage::getLoginIndex, AcknowledgeMessage::setLoginIndex)
				.encoder(AcknowledgeMessage::write)
				.decoder(AcknowledgeMessage::read)
				.consumer((message, context) -> {
					BiConsumer<AcknowledgeMessage, Supplier<NetworkEvent.Context>> handler;
					if (context.get().getDirection().getReceptionSide().isServer()) {
						handler = FMLHandshakeHandler.indexFirst((handshake, msg, ctx) -> AcknowledgeMessage.onMessage(msg, ctx));
					} else {
						handler = AcknowledgeMessage::onMessage;
					}

					handler.accept(message, context);
				})
				.markAsLoginPacket()
				.add();
	}

	private static int id() {
		return id++;
	}
}
