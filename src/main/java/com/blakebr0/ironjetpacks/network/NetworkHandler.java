package com.blakebr0.ironjetpacks.network;

import com.blakebr0.ironjetpacks.network.message.ToggleEngineMessage;
import com.blakebr0.ironjetpacks.network.message.ToggleHoverMessage;
import com.blakebr0.ironjetpacks.network.message.UpdateInputMessage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class NetworkHandler {
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation("ironjetpacks", "ironjetpacks"), () -> "1.0", (s) -> true, (s) -> true);
	private static int id = 0;

	public static void onCommonSetup() {
		INSTANCE.registerMessage(id(), ToggleHoverMessage.class, ToggleHoverMessage::write, ToggleHoverMessage::read, ToggleHoverMessage::onMessage);
		INSTANCE.registerMessage(id(), UpdateInputMessage.class, UpdateInputMessage::write, UpdateInputMessage::read, UpdateInputMessage::onMessage);
		INSTANCE.registerMessage(id(), ToggleEngineMessage.class, ToggleEngineMessage::write, ToggleEngineMessage::read, ToggleEngineMessage::onMessage);
	}

	private static int id() {
		return id++;
	}
}
