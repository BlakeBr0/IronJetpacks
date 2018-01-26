package com.blakebr0.ironjetpacks.crafting;

import org.apache.commons.lang3.tuple.Pair;

import com.blakebr0.cucumber.helper.RecipeHelper;
import com.blakebr0.cucumber.lib.ItemPlaceholder;
import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.config.ModConfig;
import com.blakebr0.ironjetpacks.crafting.recipe.JetpackUpgradeRecipe;
import com.blakebr0.ironjetpacks.item.ItemJetpack;
import com.blakebr0.ironjetpacks.item.ModItems;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

@EventBusSubscriber(modid = IronJetpacks.MOD_ID)
public class ModRecipes {

	@SubscribeEvent
	public static void onRegisterRecipes(RegistryEvent.Register<IRecipe> event) {
		RecipeHelper.addShapedRecipe(new ItemStack(ModItems.itemStrap), " I ", "LLL", " I ", 'I', "nuggetIron", 'L', Items.LEATHER);
		RecipeHelper.addShapedRecipe(new ItemStack(ModItems.itemBasicCoil), "  R", " I ", "R  ", 'R', "dustRedstone", 'I', "ingotIron");
		if (Loader.isModLoaded("thermalfoundation")) {
			RecipeHelper.addShapedRecipe(new ItemStack(ModItems.itemAdvancedCoil), "R  ", " G ", "  R", 'R', "dustRedstone", 'G', "ingotGold");
		} else {
			RecipeHelper.addShapedRecipe(new ItemStack(ModItems.itemAdvancedCoil), "  R", " G ", "R  ", 'R', "dustRedstone", 'G', "ingotGold");
		}
		
		for (Pair<String, ItemJetpack> jetpack : JetpackRegistry.getInstance().getAllJetpacks()) {			
			ItemPlaceholder mat = jetpack.getRight().getJetpackType().craftingMaterial;
			Item thruster = jetpack.getRight().getJetpackType().thruster;
			Item capacitor = jetpack.getRight().getJetpackType().capacitor;
			Item cell = jetpack.getRight().getJetpackType().cell;
			Item pack = ModItems.itemStrap;
			int tier = jetpack.getRight().getJetpackType().tier;
			ShapedOreRecipe jetpackRecipeBasic = null;
			JetpackUpgradeRecipe jetpackRecipeUpgrade = null;
			ShapedOreRecipe thrusterRecipe = null;
			ShapedOreRecipe capacitorRecipe = null;
			ShapedOreRecipe cellRecipe = null;
			boolean forceRecipes = jetpack.getRight().getJetpackType().forceRecipe;
			
			if (mat != null) {
				if (mat.getValue() instanceof String) {
					if (!OreDictionary.getOres(mat.getOreName(), false).isEmpty() || forceRecipes) {
						if (ModConfig.confBasicRecipes) {
							jetpackRecipeBasic = new ShapedOreRecipe(new ResourceLocation(IronJetpacks.MOD_ID, "jetpack"), jetpack.getRight(), "IEI", "IJI", "T T", 'I', mat.getOreName(), 'T', thruster, 'E', capacitor, 'J', pack);
						}
						
						if (ModConfig.confUpgradeRecipes) {
							if (tier > 0) {
								jetpackRecipeUpgrade = new JetpackUpgradeRecipe(new ResourceLocation(IronJetpacks.MOD_ID, "upgrade"), jetpack.getRight(), "IEI", "IJI", "T T", 'I', mat.getOreName(), 'T', thruster, 'E', capacitor, 'J', "jetpackTier" + (tier - 1));
							} else if (!ModConfig.confBasicRecipes) {
								jetpackRecipeBasic = new ShapedOreRecipe(new ResourceLocation(IronJetpacks.MOD_ID, "jetpack"), jetpack.getRight(), "IEI", "IJI", "T T", 'I', mat.getOreName(), 'T', thruster, 'E', capacitor, 'J', pack);
							}
						}
						
						thrusterRecipe = new ShapedOreRecipe(new ResourceLocation(IronJetpacks.MOD_ID, "thruster"), thruster, "ICI", "CEC", "IFI", 'I', mat.getOreName(), 'C', getCoil(tier), 'E', cell, 'F', Blocks.FURNACE);
						capacitorRecipe = new ShapedOreRecipe(new ResourceLocation(IronJetpacks.MOD_ID, "capacitor"), capacitor, "ICI", "ICI", "ICI", 'I', mat.getOreName(), 'C', cell);
						cellRecipe = new ShapedOreRecipe(new ResourceLocation(IronJetpacks.MOD_ID, "cell"), cell, " R ", "ICI", " R ", 'I', mat.getOreName(), 'C', getCoil(tier), 'R', "dustRedstone");
					}
				} else if (mat.getValue() instanceof ItemStack) {
					if (!mat.getStack().isEmpty() || forceRecipes) {
						if (ModConfig.confBasicRecipes) {
							jetpackRecipeBasic = new ShapedOreRecipe(new ResourceLocation(IronJetpacks.MOD_ID, "jetpack"), jetpack.getRight(), "IEI", "IJI", "T T", 'I', mat.getStack(), 'T', thruster, 'E', capacitor, 'J', pack);
						}
						
						if (ModConfig.confUpgradeRecipes) {
							if (tier > 0) {
								jetpackRecipeUpgrade = new JetpackUpgradeRecipe(new ResourceLocation(IronJetpacks.MOD_ID, "upgrade"), jetpack.getRight(), "IEI", "IJI", "T T", 'I', mat.getStack(), 'T', thruster, 'E', capacitor, 'J', "jetpackTier" + (tier - 1));
							} else if (!ModConfig.confBasicRecipes) {
								jetpackRecipeBasic = new ShapedOreRecipe(new ResourceLocation(IronJetpacks.MOD_ID, "jetpack"), jetpack.getRight(), "IEI", "IJI", "T T", 'I', mat.getStack(), 'T', thruster, 'E', capacitor, 'J', pack);
							}
						}
						
						thrusterRecipe = new ShapedOreRecipe(new ResourceLocation(IronJetpacks.MOD_ID, "thruster"), thruster, "ICI", "CEC", "IFI", 'I', mat.getStack(), 'C', getCoil(tier), 'E', cell, 'F', Blocks.FURNACE);
						capacitorRecipe = new ShapedOreRecipe(new ResourceLocation(IronJetpacks.MOD_ID, "capacitor"), capacitor, "ICI", "ICI", "ICI", 'I', mat.getStack(), 'C', cell);
						cellRecipe = new ShapedOreRecipe(new ResourceLocation(IronJetpacks.MOD_ID, "cell"), cell, " R ", "ICI", " R ", 'I', mat.getStack(), 'C', getCoil(tier), 'R', "dustRedstone");
					}
				}
				
				if (jetpackRecipeBasic != null) {
					jetpackRecipeBasic.setRegistryName(new ResourceLocation(IronJetpacks.MOD_ID, "jetpack_" + jetpack.getLeft()));
					event.getRegistry().register(jetpackRecipeBasic);
				}
				
				if (jetpackRecipeUpgrade != null) {
					jetpackRecipeUpgrade.setRegistryName(new ResourceLocation(IronJetpacks.MOD_ID, "upgrade_" + jetpack.getLeft()));
					event.getRegistry().register(jetpackRecipeUpgrade);
				}
				
				if (thrusterRecipe != null) {
					thrusterRecipe.setRegistryName(new ResourceLocation(IronJetpacks.MOD_ID, "thruster_" + jetpack.getLeft()));
					event.getRegistry().register(thrusterRecipe);
				}
				
				if (capacitorRecipe != null) {
					capacitorRecipe.setRegistryName(new ResourceLocation(IronJetpacks.MOD_ID, "capacitor_" + jetpack.getLeft()));
					event.getRegistry().register(capacitorRecipe);
				}
				
				if (cellRecipe != null) {
					cellRecipe.setRegistryName(new ResourceLocation(IronJetpacks.MOD_ID, "cell_" + jetpack.getLeft()));
					event.getRegistry().register(cellRecipe);
				}
			}
		}
	}
	
	private static Item getCoil(int tier) {
		return tier < 3 && tier > -1 ? ModItems.itemBasicCoil : ModItems.itemAdvancedCoil;
	}
}
