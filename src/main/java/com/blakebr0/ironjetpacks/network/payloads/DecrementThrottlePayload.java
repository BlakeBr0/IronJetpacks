package com.blakebr0.ironjetpacks.network.payloads;

import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.item.JetpackItem;
import com.blakebr0.ironjetpacks.util.JetpackUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record DecrementThrottlePayload() implements CustomPacketPayload {
    public static final Type<DecrementThrottlePayload> TYPE = new Type<>(IronJetpacks.resource("decrement_throttle"));

    public static final StreamCodec<ByteBuf, DecrementThrottlePayload> STREAM_CODEC = StreamCodec.unit(new DecrementThrottlePayload());

    @Override
    public Type<DecrementThrottlePayload> type() {
        return TYPE;
    }

    public static void handleServer(DecrementThrottlePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            var player = context.player();
            var stack = JetpackUtils.getEquippedJetpack(player);
            var item = stack.getItem();

            if (item instanceof JetpackItem) {
                JetpackUtils.decrementThrottle(stack);
            }
        });
    }
}
