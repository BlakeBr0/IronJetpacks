package com.blakebr0.ironjetpacks.proxy;

import java.io.File;

import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.config.ModConfig;
import com.blakebr0.ironjetpacks.config.ModJetpacks;
import com.blakebr0.ironjetpacks.item.ModItems;
import com.blakebr0.ironjetpacks.network.IronNetwork;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
	
	public void preInit(FMLPreInitializationEvent event) {
		ModConfig.init(new File(event.getModConfigurationDirectory(), IronJetpacks.MOD_ID + "/ironjetpacks.cfg"));
		ModJetpacks.init(new File(event.getModConfigurationDirectory(), IronJetpacks.MOD_ID + "/jetpacks"));
		ModItems.register();
	}
	
	public void init(FMLInitializationEvent event) {
		IronNetwork.register();
	}
	
	public void postInit(FMLPostInitializationEvent event) {

	}
}
