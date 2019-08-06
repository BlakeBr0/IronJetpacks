//package com.blakebr0.ironjetpacks.crafting.recipe;
//
//import com.blakebr0.cucumber.lib.ItemPlaceholder;
//import com.blakebr0.ironjetpacks.item.JetpackItem;
//
//import net.minecraft.inventory.CraftingInventory;
//import net.minecraft.inventory.InventoryCrafting;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//import net.minecraft.item.crafting.ShapedRecipe;
//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.oredict.OreDictionary;
//import net.minecraftforge.oredict.ShapedOreRecipe;
//
//public class JetpackUpgradeRecipe extends ShapedRecipe {
//	public JetpackUpgradeRecipe(ResourceLocation group, Item result, Object... recipe) {
//		super(group, result, recipe);
//	}
//
//	@Override
//	public ItemStack getCraftingResult(CraftingInventory inv) {
//		ItemStack jetpack = inv.getStackInSlot(4);
//		if (!jetpack.isEmpty() && jetpack.getItem() instanceof JetpackItem) {
//			NBTTagCompound tag = jetpack.getTagCompound();
//			ItemStack result = output.copy();
//			if (tag != null) {
//				result.setTagCompound(tag);
//				return result;
//			}
//		}
//		return output.copy();
//	}
//}
