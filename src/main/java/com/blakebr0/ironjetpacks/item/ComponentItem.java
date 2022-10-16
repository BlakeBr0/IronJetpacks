package com.blakebr0.ironjetpacks.item;

import com.blakebr0.cucumber.iface.IColored;
import com.blakebr0.cucumber.item.BaseItem;
import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry;
import com.blakebr0.ironjetpacks.util.JetpackUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import java.util.function.Function;

public class ComponentItem extends BaseItem implements IColored {
	private final String type;
	
	public ComponentItem(String type, Function<Properties, Properties> properties) {
		super(properties);
		this.type = type;
	}

	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		if (this.allowedIn(group)) {
			JetpackRegistry.getInstance().getJetpacks().forEach(jetpack -> {
				items.add(JetpackUtils.getItemForComponent(this, jetpack));
			});
		}
	}

	@Override
	public Component getName(ItemStack stack) {
		var jetpack = JetpackUtils.getJetpack(stack);
		return Localizable.of("item.ironjetpacks." + this.type).args(jetpack.displayName).build();
	}

	@Override
	public String getDescriptionId(ItemStack stack) {
		var jetpack = JetpackUtils.getJetpack(stack);
		return Localizable.of("item.ironjetpacks." + this.type).args(jetpack.displayName).buildString();
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
