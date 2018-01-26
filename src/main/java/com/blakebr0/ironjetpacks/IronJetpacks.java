package com.blakebr0.ironjetpacks;

import com.blakebr0.cucumber.registry.ModRegistry;
import com.blakebr0.ironjetpacks.handler.InputHandler;
import com.blakebr0.ironjetpacks.proxy.CommonProxy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

@Mod(name = IronJetpacks.NAME, modid = IronJetpacks.MOD_ID, version = IronJetpacks.VERSION, dependencies = IronJetpacks.DEPENDENCIES, guiFactory = IronJetpacks.GUI_FACTORY)
public class IronJetpacks {

	public static final String NAME = "Iron Jetpacks";
	public static final String MOD_ID = "ironjetpacks";
	public static final String VERSION = "${version}";
	public static final String DEPENDENCIES = "required-after:cucumber@[1.0.4,)";
	public static final String GUI_FACTORY = "com.blakebr0.ironjetpacks.config.gui.GuiFactory";
	
	public static final ModRegistry REGISTRY = ModRegistry.create(MOD_ID);
	public static final CreativeTabs CREATIVE_TAB = new IJCreativeTab();
	
	@SidedProxy(clientSide = "com.blakebr0.ironjetpacks.proxy.ClientProxy", serverSide = "com.blakebr0.ironjetpacks.proxy.ServerProxy")
	public static CommonProxy proxy;

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
