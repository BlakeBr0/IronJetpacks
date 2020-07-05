package com.blakebr0.ironjetpacks;

import com.blakebr0.ironjetpacks.init.ModItems;
import com.blakebr0.ironjetpacks.registry.Jetpack;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import java.util.List;

public class IJItemGroup extends ItemGroup {
	public IJItemGroup() {
		super(IronJetpacks.MOD_ID);
	}
	
	@Override
	public ItemStack createIcon() {
		List<Jetpack> jetpacks = JetpackRegistry.getInstance().getAllJetpacks();
		if (!jetpacks.isEmpty()) {
			return new ItemStack(jetpacks.get(0).item);
		}

		return new ItemStack(ModItems.STRAP.get());
	}
}
