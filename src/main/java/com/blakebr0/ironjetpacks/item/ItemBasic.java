package com.blakebr0.ironjetpacks.item;

import com.blakebr0.ironjetpacks.IronJetpacks;

import net.minecraft.item.Item;

public class ItemBasic extends Item {

	public ItemBasic(String name) {
		this.setUnlocalizedName("ij." + name);
		this.setCreativeTab(IronJetpacks.CREATIVE_TAB);
	}
}
