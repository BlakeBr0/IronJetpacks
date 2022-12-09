package com.blakebr0.ironjetpacks.client.handler;

import com.blakebr0.cucumber.iface.IColored;
import com.blakebr0.ironjetpacks.init.ModItems;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class ColorHandler {
	@SubscribeEvent
	public void onItemColors(RegisterColorHandlersEvent.Item event) {
		var registry = JetpackRegistry.getInstance();

		if (registry.isErrored())
			return;

		event.register((stack, tint) -> {
			var item = (IColored) stack.getItem();
			return item.getColor(tint, stack);
		}, ModItems.JETPACK.get(), ModItems.CELL.get(), ModItems.THRUSTER.get(), ModItems.CAPACITOR.get());
	}
}
