package com.blakebr0.ironjetpacks;

import com.blakebr0.ironjetpacks.item.ModItems;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class IJCreativeTab extends CreativeTabs {

	public IJCreativeTab() {
		super(IronJetpacks.MOD_ID);
	}
	
	@Override
	public ItemStack getTabIconItem() {
		JetpackRegistry registry = JetpackRegistry.getInstance();
		
		if (registry.getAllJetpacks().isEmpty()) {
			return new ItemStack(ModItems.STRAP);
		}
		
		return new ItemStack(registry.getAllJetpacks().get(0).item);
	}
}
