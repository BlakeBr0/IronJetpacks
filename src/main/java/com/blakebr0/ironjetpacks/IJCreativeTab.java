package com.blakebr0.ironjetpacks;

import com.blakebr0.ironjetpacks.init.ModItems;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class IJCreativeTab extends CreativeModeTab {
	public IJCreativeTab() {
		super(IronJetpacks.MOD_ID);
	}
	
	@Override
	public ItemStack makeIcon() {
		var jetpacks = JetpackRegistry.getInstance().getAllJetpacks();

		if (!jetpacks.isEmpty()) {
			return new ItemStack(jetpacks.get(0).item);
		}

		return new ItemStack(ModItems.STRAP.get());
	}
}
