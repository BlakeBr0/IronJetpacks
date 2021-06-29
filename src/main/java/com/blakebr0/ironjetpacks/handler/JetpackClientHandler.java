package com.blakebr0.ironjetpacks.handler;

import com.blakebr0.cucumber.util.Pos3d;
import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.ironjetpacks.config.ModConfigs;
import com.blakebr0.ironjetpacks.item.JetpackItem;
import com.blakebr0.ironjetpacks.registry.Jetpack;
import com.blakebr0.ironjetpacks.sound.JetpackSound;
import com.blakebr0.ironjetpacks.util.JetpackUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.ParticleStatus;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

public final class JetpackClientHandler {
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (event.phase == TickEvent.Phase.END) {
            if (mc.player != null && mc.level != null) {
                if (!mc.isPaused()) {
                    ItemStack chest = mc.player.getItemBySlot(EquipmentSlotType.CHEST);
                    Item item = chest.getItem();
                    if (!chest.isEmpty() && item instanceof JetpackItem && JetpackUtils.isFlying(mc.player)) {
                        if (ModConfigs.ENABLE_JETPACK_PARTICLES.get() && (mc.options.particles != ParticleStatus.MINIMAL)) {
                            Jetpack jetpack = ((JetpackItem) item).getJetpack();
                            Random rand = Utils.RANDOM;

                            Pos3d playerPos = new Pos3d(mc.player).translate(0, 1.5, 0);

                            float random = (rand.nextFloat() - 0.5F) * 0.1F;
                            double[] sneakBonus = mc.player.isCrouching() ? new double[]{-0.30, -0.10} : new double[]{0, 0};

                            Pos3d vLeft = (Pos3d) new Pos3d(-0.18, -0.90 + sneakBonus[1], -0.30 + sneakBonus[0]).xRot(0).yRot(mc.player.yBodyRot);
                            Pos3d vRight = (Pos3d) new Pos3d(0.18, -0.90 + sneakBonus[1], -0.30 + sneakBonus[0]).xRot(0).yRot(mc.player.yBodyRot);

                            Pos3d v = playerPos.translate(vLeft).translate(new Pos3d(mc.player.getDeltaMovement().scale(jetpack.speedSide)));
                            mc.particleEngine.createParticle(ParticleTypes.FLAME, v.x, v.y, v.z, random, -0.2D, random);
                            mc.particleEngine.createParticle(ParticleTypes.SMOKE, v.x, v.y, v.z, random, -0.2D, random);

                            v = playerPos.translate(vRight).translate(new Pos3d(mc.player.getDeltaMovement().scale(jetpack.speedSide)));
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
    }
}
