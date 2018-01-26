package com.blakebr0.ironjetpacks.handler;

import java.util.Random;

import com.blakebr0.cucumber.lib.Pos3d;
import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.client.particle.ParticleJetpackFlame;
import com.blakebr0.ironjetpacks.client.particle.ParticleJetpackSmoke;
import com.blakebr0.ironjetpacks.config.ModConfig;
import com.blakebr0.ironjetpacks.item.ItemJetpack;
import com.blakebr0.ironjetpacks.sound.SoundJetpack;
import com.blakebr0.ironjetpacks.util.JetpackUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(modid = IronJetpacks.MOD_ID, value = Side.CLIENT)
public class JetpackClientHandler {
	
	@SubscribeEvent
	public static void onClientTick(ClientTickEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		if (event.phase == Phase.END) {
			if (mc.player != null && mc.world != null) {
				if (!mc.isGamePaused()) {
					ItemStack chest = mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
					if (!chest.isEmpty() && chest.getItem() instanceof ItemJetpack && JetpackUtils.isFlying(mc.player)) {
						if (ModConfig.confJetpackParticles && (mc.gameSettings.particleSetting == 0 || mc.gameSettings.particleSetting == 1 && mc.world.getTotalWorldTime() % 4L == 0)) {
							ItemJetpack jetpack = (ItemJetpack) chest.getItem();
							
							Random rand = Utils.rand;

							Pos3d playerPos = new Pos3d(mc.player).translate(0, 1.5, 0);

							float random = (rand.nextFloat() - 0.5F) * 0.1F;

							Pos3d vLeft = new Pos3d(-0.18, -0.90, -0.35).rotatePitch(0).rotateYaw(mc.player.renderYawOffset);
							Pos3d vRight = new Pos3d(0.18, -0.90, -0.35).rotatePitch(0).rotateYaw(mc.player.renderYawOffset);

							Pos3d v = playerPos.translate(vLeft).translate(new Pos3d(mc.player.motionX, mc.player.motionY, mc.player.motionZ));
							mc.effectRenderer.addEffect(new ParticleJetpackFlame(mc.world, v.x, v.y, v.z, random, -0.2D, random));
							mc.effectRenderer.addEffect(new ParticleJetpackSmoke(mc.world, v.x, v.y, v.z, random, -0.2D, random));
							
							v = playerPos.translate(vRight).translate(new Pos3d(mc.player.motionX, mc.player.motionY, mc.player.motionZ));
							mc.effectRenderer.addEffect(new ParticleJetpackFlame(mc.world, v.x, v.y, v.z, random, -0.2D, random));
							mc.effectRenderer.addEffect(new ParticleJetpackSmoke(mc.world, v.x, v.y, v.z, random, -0.2D, random));
						}
						
						if (ModConfig.confJetpackSounds && !SoundJetpack.playing(mc.player.getEntityId())) {
							mc.getSoundHandler().playSound(new SoundJetpack(mc.player));
						}
					}
				}
			}
		}
	}
}
