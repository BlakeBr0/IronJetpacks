package com.blakebr0.ironjetpacks.network.message;

import com.blakebr0.ironjetpacks.network.NetworkHandler;
import com.blakebr0.ironjetpacks.registry.Jetpack;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class SyncJetpacksMessage implements IntSupplier {
    private List<Jetpack> jetpacks = new ArrayList<>();
    private int loginIndex;

    public SyncJetpacksMessage() { }

    @Override
    public int getAsInt() {
        return this.loginIndex;
    }

    public int getLoginIndex() {
        return this.loginIndex;
    }

    public void setLoginIndex(int loginIndex) {
        this.loginIndex = loginIndex;
    }

    public List<Jetpack> getJetpacks() {
        return this.jetpacks;
    }

    public static SyncJetpacksMessage read(PacketBuffer buffer) {
        SyncJetpacksMessage message = new SyncJetpacksMessage();

        message.jetpacks = JetpackRegistry.getInstance().readFromBuffer(buffer);

        return message;
    }

    public static void write(SyncJetpacksMessage message, PacketBuffer buffer) {
        JetpackRegistry.getInstance().writeToBuffer(buffer);
    }

    public static void onMessage(SyncJetpacksMessage message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            JetpackRegistry.getInstance().loadJetpacks(message);

            NetworkHandler.INSTANCE.reply(new AcknowledgeMessage(), context.get());
        });

        context.get().setPacketHandled(true);
    }
}
