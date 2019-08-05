package com.blakebr0.ironjetpacks;

import com.blakebr0.ironjetpacks.item.ModItems;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class IJItemGroup extends ItemGroup {

	public IJItemGroup() {
		super(IronJetpacks.MOD_ID);
	}
	
	@Override
	public ItemStack createIcon() {
		JetpackRegistry registry = JetpackRegistry.getInstance();
		
		if (registry.getAllJetpacks().isEmpty()) {
		}
		return new ItemStack(ModItems.STRAP);

//		return new ItemStack(registry.getAllJetpacks().get(0).item);
	}
}
