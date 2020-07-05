package com.blakebr0.ironjetpacks.item;

import com.blakebr0.cucumber.iface.IColored;
import com.blakebr0.cucumber.iface.IEnableable;
import com.blakebr0.cucumber.item.BaseItem;
import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.ironjetpacks.registry.Jetpack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Function;

public class ComponentItem extends BaseItem implements IColored, IEnableable {
	private final String name;
	private final String type;
	private final boolean enabled;
	private final int color;
	
	public ComponentItem(Jetpack jetpack, String type, Function<Properties, Properties> properties) {
		super(properties.compose(p -> p.rarity(jetpack.rarity)));
		this.name = jetpack.name;
		this.color = jetpack.color;
		this.enabled = !jetpack.disabled;
		this.type = type;
	}
	
	@Override
	public ITextComponent getDisplayName(ItemStack stack) {
		String name = StringUtils.capitalize(this.name.replace(" ", "_"));
		return Localizable.of("item.ironjetpacks." + this.type).args(name).build();
	}

	@Override
	public int getColor(int i) {
		return this.color;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}
}
