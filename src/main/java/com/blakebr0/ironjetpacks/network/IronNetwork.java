package com.blakebr0.ironjetpacks.network;

import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.network.message.MessageToggleEngine;
import com.blakebr0.ironjetpacks.network.message.MessageToggleHover;
import com.blakebr0.ironjetpacks.network.message.MessageUpdateInput;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class IronNetwork {
	
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(IronJetpacks.MOD_ID);
	
	public static void register() {
		INSTANCE.registerMessage(MessageToggleHover.Handler.class, MessageToggleHover.class, 0, Side.SERVER);
		INSTANCE.registerMessage(MessageUpdateInput.Handler.class, MessageUpdateInput.class, 1, Side.SERVER);
		INSTANCE.registerMessage(MessageToggleEngine.Handler.class, MessageToggleEngine.class, 2, Side.SERVER);
		//INSTANCE.registerMessage(MessageUpdateJetpack.Handler.class, MessageUpdateJetpack.class, 3, Side.CLIENT);
	}
}
