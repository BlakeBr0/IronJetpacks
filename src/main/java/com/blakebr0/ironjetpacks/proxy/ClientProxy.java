package com.blakebr0.ironjetpacks.proxy;

import com.blakebr0.ironjetpacks.handler.ColorHandler;
import com.blakebr0.ironjetpacks.handler.KeybindHandler;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		KeybindHandler.register();
	}
	
	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		ColorHandler.register();
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
	}
}
