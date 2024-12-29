package com.blakebr0.ironjetpacks.item;

import com.blakebr0.cucumber.iface.IColored;
import com.blakebr0.cucumber.item.BaseItem;
import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.ironjetpacks.util.JetpackUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class ComponentItem extends BaseItem implements IColored {
	private final String type;
	
	public ComponentItem(String type) {
		super();
		this.type = type;
	}

	@Override
	public Component getName(ItemStack stack) {
		var jetpack = JetpackUtils.getJetpack(stack);
		return Localizable.of("item.ironjetpacks." + this.type).args(jetpack.getDisplayName()).build();
	}

	@Override
	public String getDescriptionId(ItemStack stack) {
		var jetpack = JetpackUtils.getJetpack(stack);
		return Localizable.of("item.ironjetpacks." + this.type).args(jetpack.getDisplayName()).buildString();
	}

	@Override
	public Rarity getRarity(ItemStack stack) {
		var jetpack = JetpackUtils.getJetpack(stack);

		if (!stack.isEnchanted()) {
			return jetpack.rarity;
		} else {
			return switch (jetpack.rarity) {
				case COMMON, UNCOMMON -> Rarity.RARE;
				case RARE -> Rarity.EPIC;
				case EPIC -> jetpack.rarity;
			};
		}
	}

	@Override
	public int getColor(int i, ItemStack stack) {
		var jetpack = JetpackUtils.getJetpack(stack);
		return jetpack.color;
	}
}
