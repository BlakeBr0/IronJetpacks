//package com.blakebr0.ironjetpacks.client.particle;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.particle.IAnimatedSprite;
//import net.minecraft.client.particle.SmokeParticle;
//import net.minecraft.world.World;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//
//@OnlyIn(Dist.CLIENT)
//public class JetpackSmokeParticle extends SmokeParticle {
//	public JetpackSmokeParticle(World world, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, IAnimatedSprite sprite) {
//		super(world, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, 1.0F, sprite);
//	}
//
//	@Override
//	public int getBrightnessForRender(float f) {
//		Minecraft mc = Minecraft.getInstance();
//		return 190 + (int) (20.0F * (1.0F - mc.gameSettings.gamma));
//	}
//}
