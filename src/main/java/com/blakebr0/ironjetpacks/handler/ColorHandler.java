package com.blakebr0.ironjetpacks.handler;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.blakebr0.cucumber.iface.IColoredItem;
import com.blakebr0.ironjetpacks.item.ItemJetpack;
import com.blakebr0.ironjetpacks.registry.Jetpack;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;

public class ColorHandler {
	
	public static final List<IColoredItem> COLORED_ITEMS = new ArrayList<>();
	
	public static void register() {
		BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();
		ItemColors itemColors = Minecraft.getMinecraft().getItemColors();
		
		for (Jetpack jetpack : JetpackRegistry.getInstance().getAllJetpacks()) {
			COLORED_ITEMS.add(jetpack.item);
			COLORED_ITEMS.add(jetpack.cell);
			COLORED_ITEMS.add(jetpack.thruster);
			COLORED_ITEMS.add(jetpack.capacitor);
		}
		
		itemColors.registerItemColorHandler((stack, tint) -> {
			IColoredItem item = (IColoredItem) stack.getItem();
			return stack.getItem() instanceof ItemJetpack ? tint == 1 ? item.color() : -1 : item.color();
		},  COLORED_ITEMS.toArray(new Item[0]));
	}
}
