package com.blakebr0.ironjetpacks.item;

import org.apache.commons.lang3.StringUtils;

import com.blakebr0.cucumber.iface.IColoredItem;
import com.blakebr0.cucumber.iface.IModelHelper;
import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.ironjetpacks.IronJetpacks;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;

public class ItemComponent extends Item implements IModelHelper, IColoredItem {
	
	private String name;
	private String type;
	private int color = 0;
	
	public ItemComponent(String name, String type) {
		this.setUnlocalizedName("ij." + type);
		this.setCreativeTab(IronJetpacks.CREATIVE_TAB);
		this.name = name;
		this.type = type;
	}
	
	public ItemComponent setColor(int color) {
		this.color = color;
		return this;
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		String name = StringUtils.capitalize(this.name.replace(" ", "_"));
		return name + " " + Utils.localize(this.getUnlocalizedName() + ".name");
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return this.name.equalsIgnoreCase("creative") ? EnumRarity.EPIC : EnumRarity.COMMON;
	}

	@Override
	public void initModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(IronJetpacks.MOD_ID + ":" + this.type, "inventory"));		
	}

	@Override
	public int color() {
		return this.color;
	}
}
