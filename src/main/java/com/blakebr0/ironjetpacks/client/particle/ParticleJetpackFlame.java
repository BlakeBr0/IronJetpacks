//package com.blakebr0.ironjetpacks.client.particle;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.particle.FlameParticle;
//import net.minecraft.client.particle.IAnimatedSprite;
//import net.minecraft.client.particle.ParticleFlame;
//import net.minecraft.client.renderer.BufferBuilder;
//import net.minecraft.entity.Entity;
//import net.minecraft.world.World;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import net.minecraftforge.fml.client.FMLClientHandler;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//@OnlyIn(Dist.CLIENT)
//public class ParticleJetpackFlame extends FlameParticle {
//	public ParticleJetpackFlame(World world, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed) {
//		super(world, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed);
//	}
//
//	@Override
//	public int getBrightnessForRender(float f) {
//		Minecraft mc = Minecraft.getInstance();
//		return 190 + (int) (20.0F * (1.0F - mc.gameSettings.gamma));
//	}
//}
