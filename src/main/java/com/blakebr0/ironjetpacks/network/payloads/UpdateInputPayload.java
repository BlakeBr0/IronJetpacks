package com.blakebr0.ironjetpacks.network.payloads;

import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.client.handler.InputHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record UpdateInputPayload(boolean up, boolean down, boolean forwards, boolean backwards, boolean left, boolean right, boolean sprint) implements CustomPacketPayload {
    public static final Type<UpdateInputPayload> TYPE = new Type<>(IronJetpacks.resource("update_inputs"));

    public static final StreamCodec<ByteBuf, UpdateInputPayload> STREAM_CODEC = StreamCodec.of(UpdateInputPayload::encode, UpdateInputPayload::decode);

    @Override
    public Type<UpdateInputPayload> type() {
        return TYPE;
    }

    private static UpdateInputPayload decode(ByteBuf buf) {
        return new UpdateInputPayload(
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readBoolean()
        );
    }

    private static void encode(ByteBuf buf, UpdateInputPayload payload) {
        buf.writeBoolean(payload.up);
        buf.writeBoolean(payload.down);
        buf.writeBoolean(payload.forwards);
        buf.writeBoolean(payload.backwards);
        buf.writeBoolean(payload.left);
        buf.writeBoolean(payload.right);
        buf.writeBoolean(payload.sprint);
    }

    public static void handleServer(UpdateInputPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            var player = context.player();

            InputHandler.update(player, payload.up, payload.down, payload.forwards, payload.backwards, payload.left, payload.right, payload.sprint);
        });
    }
}
