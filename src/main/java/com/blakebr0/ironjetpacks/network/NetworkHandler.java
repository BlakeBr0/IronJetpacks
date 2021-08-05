package com.blakebr0.ironjetpacks.network;

import com.blakebr0.ironjetpacks.network.message.DecrementThrottleMessage;
import com.blakebr0.ironjetpacks.network.message.IncrementThrottleMessage;
import com.blakebr0.ironjetpacks.network.message.ToggleEngineMessage;
import com.blakebr0.ironjetpacks.network.message.ToggleHoverMessage;
import com.blakebr0.ironjetpacks.network.message.UpdateInputMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

public class NetworkHandler {
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation("ironjetpacks", "ironjetpacks"), () -> "1.0", (s) -> true, (s) -> true);
	private static int id = 0;

	public static void onCommonSetup() {
		INSTANCE.registerMessage(id(), ToggleHoverMessage.class, ToggleHoverMessage::write, ToggleHoverMessage::read, ToggleHoverMessage::onMessage);
		INSTANCE.registerMessage(id(), UpdateInputMessage.class, UpdateInputMessage::write, UpdateInputMessage::read, UpdateInputMessage::onMessage);
		INSTANCE.registerMessage(id(), ToggleEngineMessage.class, ToggleEngineMessage::write, ToggleEngineMessage::read, ToggleEngineMessage::onMessage);
		INSTANCE.registerMessage(id(), IncrementThrottleMessage.class, IncrementThrottleMessage::write, IncrementThrottleMessage::read, IncrementThrottleMessage::onMessage);
		INSTANCE.registerMessage(id(), DecrementThrottleMessage.class, DecrementThrottleMessage::write, DecrementThrottleMessage::read, DecrementThrottleMessage::onMessage);
	}

	private static int id() {
		return id++;
	}
}
