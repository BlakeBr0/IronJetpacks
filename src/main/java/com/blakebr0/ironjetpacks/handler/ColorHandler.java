package com.blakebr0.ironjetpacks.handler;

import com.blakebr0.cucumber.iface.IColored;
import com.blakebr0.ironjetpacks.registry.Jetpack;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class ColorHandler {
	@SubscribeEvent
	public void onItemColors(ColorHandlerEvent.Item event) {
		ItemColors colors = event.getItemColors();
		JetpackRegistry registry = JetpackRegistry.getInstance();

		if (registry.isErrored())
			return;

		ArrayList<Item> coloredItems = new ArrayList<>();

		for (Jetpack jetpack : registry.getAllJetpacks()) {
			coloredItems.add(jetpack.item);
			coloredItems.add(jetpack.cell);
			coloredItems.add(jetpack.thruster);
			coloredItems.add(jetpack.capacitor);
		}

		colors.register((stack, tint) -> {
			IColored item = (IColored) stack.getItem();
			return item.getColor(tint);
		},  coloredItems.toArray(new Item[0]));
	}
}
