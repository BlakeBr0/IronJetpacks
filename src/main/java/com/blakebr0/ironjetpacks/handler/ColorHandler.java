package com.blakebr0.ironjetpacks.handler;

import com.blakebr0.cucumber.iface.IColored;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;

public final class ColorHandler {
	@SubscribeEvent
	public void onItemColors(ColorHandlerEvent.Item event) {
		var colors = event.getItemColors();
		var registry = JetpackRegistry.getInstance();

		if (registry.isErrored())
			return;

		ArrayList<Item> items = new ArrayList<>();

		for (var jetpack : registry.getAllJetpacks()) {
			items.add(jetpack.item);
			items.add(jetpack.cell);
			items.add(jetpack.thruster);
			items.add(jetpack.capacitor);
		}

		colors.register((stack, tint) -> {
			var item = (IColored) stack.getItem();
			return item.getColor(tint);
		},  items.toArray(new Item[0]));
	}
}
