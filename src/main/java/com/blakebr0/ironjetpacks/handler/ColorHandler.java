package com.blakebr0.ironjetpacks.handler;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.blakebr0.cucumber.iface.IColoredItem;
import com.blakebr0.ironjetpacks.item.ItemJetpack;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;

public class ColorHandler {
	
	public static List<IColoredItem> coloredItems = new ArrayList<>();
	
	public static void register() {
		BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();
		ItemColors itemColors = Minecraft.getMinecraft().getItemColors();
		
		for (Pair<String, ItemJetpack> jetpack : JetpackRegistry.getInstance().getAllJetpacks()) {
			coloredItems.add(jetpack.getRight());
			coloredItems.add((IColoredItem) jetpack.getRight().getJetpackType().cell);
			coloredItems.add((IColoredItem) jetpack.getRight().getJetpackType().thruster);
			coloredItems.add((IColoredItem) jetpack.getRight().getJetpackType().capacitor);
		}
		
		itemColors.registerItemColorHandler((stack, tint) -> {
			IColoredItem item = (IColoredItem) stack.getItem();
			return stack.getItem() instanceof ItemJetpack ? tint == 1 ? item.color() : -1 : item.color();
		},  coloredItems.toArray(new Item[0]));
	}
}
