package com.blakebr0.ironjetpacks.network;

import com.blakebr0.cucumber.network.BaseNetworkHandler;
import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.network.message.DecrementThrottleMessage;
import com.blakebr0.ironjetpacks.network.message.IncrementThrottleMessage;
import com.blakebr0.ironjetpacks.network.message.SyncJetpacksMessage;
import com.blakebr0.ironjetpacks.network.message.ToggleEngineMessage;
import com.blakebr0.ironjetpacks.network.message.ToggleHoverMessage;
import com.blakebr0.ironjetpacks.network.message.UpdateInputMessage;
import net.minecraft.resources.ResourceLocation;

public class NetworkHandler {
	public static final BaseNetworkHandler INSTANCE = new BaseNetworkHandler(new ResourceLocation(IronJetpacks.MOD_ID, "main"));

	public static void onCommonSetup() {
		INSTANCE.register(ToggleHoverMessage.class, new ToggleHoverMessage());
		INSTANCE.register(UpdateInputMessage.class, new UpdateInputMessage());
		INSTANCE.register(ToggleEngineMessage.class, new ToggleEngineMessage());
		INSTANCE.register(IncrementThrottleMessage.class, new IncrementThrottleMessage());
		INSTANCE.register(DecrementThrottleMessage.class, new DecrementThrottleMessage());
		INSTANCE.register(SyncJetpacksMessage.class, new SyncJetpacksMessage());
	}
}
