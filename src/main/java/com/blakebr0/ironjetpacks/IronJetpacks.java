package com.blakebr0.ironjetpacks;

import com.blakebr0.cucumber.registry.ModRegistry;
import com.blakebr0.ironjetpacks.handler.InputHandler;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

@Mod(IronJetpacks.NAME)
public class IronJetpacks {

	public static final String MOD_ID = "ironjetpacks";
	public static final String NAME = "Iron Jetpacks";

	public static final ItemGroup ITEM_GROUP = new IJItemGroup();

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}
	
	@EventHandler
	public void serverStopping(FMLServerStoppingEvent event) {
		InputHandler.clear();
	}
}
