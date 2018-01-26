package com.blakebr0.ironjetpacks.crafting.recipe;

import com.blakebr0.cucumber.lib.ItemPlaceholder;
import com.blakebr0.ironjetpacks.item.ItemJetpack;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class JetpackUpgradeRecipe extends ShapedOreRecipe {

	public JetpackUpgradeRecipe(ResourceLocation group, Item result, Object... recipe) {
		super(group, result, recipe);
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack jetpack = inv.getStackInSlot(4);
		if (!jetpack.isEmpty() && jetpack.getItem() instanceof ItemJetpack) {
			NBTTagCompound tag = jetpack.getTagCompound();
			ItemStack result = output.copy();
			if (tag != null) {
				result.setTagCompound(tag);
				return result;
			}
		}
		return output.copy();
	}
}
