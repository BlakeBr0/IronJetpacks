package com.blakebr0.ironjetpacks.item;

import com.blakebr0.cucumber.iface.IColored;
import com.blakebr0.cucumber.iface.IEnableable;
import com.blakebr0.cucumber.item.BaseItem;
import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.ironjetpacks.registry.Jetpack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import java.util.function.Function;

public class ComponentItem extends BaseItem implements IColored, IEnableable {
	private final Jetpack jetpack;
	private final String type;
	
	public ComponentItem(Jetpack jetpack, String type, Function<Properties, Properties> properties) {
		super(properties.compose(p -> p.rarity(jetpack.rarity)));
		this.jetpack = jetpack;
		this.type = type;
	}
	
	@Override
	public ITextComponent getDisplayName(ItemStack stack) {
		return Localizable.of("item.ironjetpacks." + this.type).args(this.jetpack.displayName).build();
	}

	@Override
	public int getColor(int i) {
		return this.jetpack.color;
	}

	@Override
	public boolean isEnabled() {
		return !this.jetpack.disabled;
	}
}
