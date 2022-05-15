package com.blakebr0.ironjetpacks.handler;

import com.blakebr0.cucumber.helper.VecHelper;
import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.ironjetpacks.client.sound.JetpackSound;
import com.blakebr0.ironjetpacks.config.ModConfigs;
import com.blakebr0.ironjetpacks.item.JetpackItem;
import com.blakebr0.ironjetpacks.util.JetpackUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class JetpackClientHandler {
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END)
            return;

        var mc = Minecraft.getInstance();
        if (mc.player != null && mc.level != null && !mc.isPaused()) {
            var chest = JetpackUtils.getEquippedJetpack(mc.player);
            var item = chest.getItem();

            if (!chest.isEmpty() && item instanceof JetpackItem && JetpackUtils.isFlying(mc.player)) {
                if (ModConfigs.ENABLE_JETPACK_PARTICLES.get() && (mc.options.particles != ParticleStatus.MINIMAL)) {
                    var jetpack = JetpackUtils.getJetpack(chest);
                    var rand = Utils.RANDOM;

                    var playerPos = mc.player.position().add(0, 1.5, 0);

                    float random = (rand.nextFloat() - 0.5F) * 0.1F;
                    double[] sneakBonus = mc.player.isCrouching() ? new double[] { -0.30, -0.10 } : new double[] { 0, 0 };

                    var vLeft = VecHelper.rotate(new Vec3(-0.18, -0.90 + sneakBonus[1], -0.30 + sneakBonus[0]), mc.player.yBodyRot, 0, 0);
                    var vRight = VecHelper.rotate(new Vec3(0.18, -0.90 + sneakBonus[1], -0.30 + sneakBonus[0]), mc.player.yBodyRot, 0, 0);

                    var v = playerPos.add(vLeft).add(mc.player.getDeltaMovement().scale(jetpack.speedSide));
                    mc.particleEngine.createParticle(ParticleTypes.FLAME, v.x, v.y, v.z, random, -0.2D, random);
                    mc.particleEngine.createParticle(ParticleTypes.SMOKE, v.x, v.y, v.z, random, -0.2D, random);

                    v = playerPos.add(vRight).add(mc.player.getDeltaMovement().scale(jetpack.speedSide));
                    mc.particleEngine.createParticle(ParticleTypes.FLAME, v.x, v.y, v.z, random, -0.2D, random);
                    mc.particleEngine.createParticle(ParticleTypes.SMOKE, v.x, v.y, v.z, random, -0.2D, random);
                }

                if (ModConfigs.ENABLE_JETPACK_SOUNDS.get() && !JetpackSound.playing(mc.player.getId())) {
                    mc.getSoundManager().play(new JetpackSound(mc.player));
                }
            }
        }
    }
}
