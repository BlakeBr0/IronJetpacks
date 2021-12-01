package com.blakebr0.ironjetpacks.network.message;

import com.blakebr0.cucumber.network.message.Message;
import com.blakebr0.ironjetpacks.registry.Jetpack;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class SyncJetpacksMessage extends Message<SyncJetpacksMessage> {
    private final List<Jetpack> jetpacks;

    public SyncJetpacksMessage() {
        this.jetpacks = List.of();
    }

    public SyncJetpacksMessage(List<Jetpack> jetpacks) {
        this.jetpacks = jetpacks;
    }
    public List<Jetpack> getJetpacks() {
        return this.jetpacks;
    }

    public SyncJetpacksMessage read(FriendlyByteBuf buffer) {
        var jetpacks = JetpackRegistry.getInstance().readFromBuffer(buffer);

        return new SyncJetpacksMessage(jetpacks);
    }

    public void write(SyncJetpacksMessage message, FriendlyByteBuf buffer) {
        JetpackRegistry.getInstance().writeToBuffer(buffer);
    }

    public void onMessage(SyncJetpacksMessage message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            JetpackRegistry.getInstance().loadJetpacks(message);
        });

        context.get().setPacketHandled(true);
    }
}
