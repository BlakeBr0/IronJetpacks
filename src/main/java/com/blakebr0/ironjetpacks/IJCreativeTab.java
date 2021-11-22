package com.blakebr0.ironjetpacks;

import com.blakebr0.ironjetpacks.init.ModItems;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry;
import com.blakebr0.ironjetpacks.util.JetpackUtils;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class IJCreativeTab extends CreativeModeTab {
	public IJCreativeTab() {
		super(IronJetpacks.MOD_ID);
	}
	
	@Override
	public ItemStack makeIcon() {
		var jetpack = JetpackRegistry.getInstance().getJetpacks()
				.stream()
				.findFirst()
				.orElse(null);

		if (jetpack != null) {
			return JetpackUtils.getItemForJetpack(jetpack);
		}

		return new ItemStack(ModItems.STRAP.get());
	}
}
