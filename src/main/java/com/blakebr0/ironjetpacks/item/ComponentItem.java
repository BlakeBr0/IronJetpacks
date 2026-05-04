package com.blakebr0.ironjetpacks.item;

import com.blakebr0.cucumber.iface.IColored;
import com.blakebr0.cucumber.iface.IComponentInitializer;
import com.blakebr0.cucumber.item.BaseItem;
import com.blakebr0.ironjetpacks.util.JetpackUtils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class ComponentItem extends BaseItem implements IColored, IComponentInitializer {
	private final String type;
	
	public ComponentItem(Identifier id, String type) {
		super(id);
		this.type = type;
	}

	@Override
	public Component getName(ItemStack stack) {
		var jetpack = JetpackUtils.getJetpack(stack);
		return Component.translatable("item.ironjetpacks." + this.type, jetpack.getDisplayName());
	}

	@Override
	public int getColor(int i, ItemStack stack) {
		var jetpack = JetpackUtils.getJetpack(stack);
		return jetpack.color;
	}

	@Override
	public void initialize(ItemStack stack) {
		var jetpack = JetpackUtils.getJetpack(stack);

		if (stack.isEnchanted()) {
			var rarity = switch (jetpack.rarity) {
				case COMMON, UNCOMMON -> Rarity.RARE;
				case RARE -> Rarity.EPIC;
				case EPIC -> jetpack.rarity;
			};

			stack.set(DataComponents.RARITY, rarity);
		} else {
			stack.set(DataComponents.RARITY, jetpack.rarity);
		}
	}
}
