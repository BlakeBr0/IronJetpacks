package com.blakebr0.ironjetpacks.network;

import com.blakebr0.ironjetpacks.network.payloads.DecrementThrottlePayload;
import com.blakebr0.ironjetpacks.network.payloads.IncrementThrottlePayload;
import com.blakebr0.ironjetpacks.network.payloads.SyncJetpacksPayload;
import com.blakebr0.ironjetpacks.network.payloads.ToggleEnginePayload;
import com.blakebr0.ironjetpacks.network.payloads.ToggleHoverPayload;
import com.blakebr0.ironjetpacks.network.payloads.UpdateInputPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public final class NetworkHandler {
	@SubscribeEvent
	public void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event) {
		var registrar = event.registrar("1");

		registrar.playToServer(DecrementThrottlePayload.TYPE, DecrementThrottlePayload.STREAM_CODEC, DecrementThrottlePayload::handleServer);
		registrar.playToServer(IncrementThrottlePayload.TYPE, IncrementThrottlePayload.STREAM_CODEC, IncrementThrottlePayload::handleServer);
		registrar.playToServer(ToggleEnginePayload.TYPE, ToggleEnginePayload.STREAM_CODEC, ToggleEnginePayload::handleServer);
		registrar.playToServer(ToggleHoverPayload.TYPE, ToggleHoverPayload.STREAM_CODEC, ToggleHoverPayload::handleServer);
		registrar.playToServer(UpdateInputPayload.TYPE, UpdateInputPayload.STREAM_CODEC, UpdateInputPayload::handleServer);

		registrar.playToClient(SyncJetpacksPayload.TYPE, SyncJetpacksPayload.STREAM_CODEC, SyncJetpacksPayload::handleClient);
	}
}
