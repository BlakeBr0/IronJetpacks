package com.blakebr0.ironjetpacks.handler;

import com.blakebr0.ironjetpacks.client.handler.InputHandler;
import com.blakebr0.ironjetpacks.item.JetpackItem;
import com.blakebr0.ironjetpacks.util.JetpackUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public final class PlayerTickHandler {
    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Pre event) {
        var player = event.getEntity();
        var level = player.level();

        var stack = JetpackUtils.getEquippedJetpack(player);
        if (stack.isEmpty())
            return;

        var item = stack.getItem();
        if (item instanceof JetpackItem && JetpackUtils.isEngineOn(stack)) {
            var hover = JetpackUtils.isHovering(stack);

            if (InputHandler.isHoldingUp(player) || hover && !player.onGround()) {
                var jetpack = JetpackUtils.getJetpack(stack);

                double motionY = player.getDeltaMovement().y();
                double hoverSpeed = InputHandler.isHoldingDown(player) ? jetpack.speedHoverDescend : jetpack.speedHoverSlow;
                double currentAccel = jetpack.accelVert * (motionY < 0.3D ? 2.5D : 1.0D);
                double currentSpeedVertical = jetpack.speedVert * (player.isInWater() ? 0.4D : 1.0D);

                double usage = player.isSprinting() || InputHandler.isHoldingSprint(player) ? jetpack.usage * jetpack.sprintFuel : jetpack.usage;

                var creative = jetpack.creative;
                var energy = JetpackUtils.getEnergyStorage(stack);

                if (!player.isCreative() && !creative) {
                    try (var tx = Transaction.openRoot()) {
                        energy.extract((int) usage, tx);
                        tx.commit();
                    }
                }

                if (hover && player.isFallFlying()) {
                    player.stopFallFlying();
                }

                if (energy.getAmountAsInt() > 0 || player.isCreative() || creative) {
                    double throttle = JetpackUtils.getThrottle(stack);
                    double verticalSprintMulti = motionY >= 0 && InputHandler.isHoldingSprint(player) ? jetpack.sprintSpeedVert : 1.0D;

                    if (InputHandler.isHoldingUp(player)) {
                        if (!hover) {
                            fly(player, Math.min(motionY + currentAccel, currentSpeedVertical) * throttle * verticalSprintMulti);
                        } else {
                            if (InputHandler.isHoldingDown(player)) {
                                fly(player, Math.min(motionY + currentAccel, -jetpack.speedHoverSlow));
                            } else {
                                fly(player, Math.min(motionY + currentAccel, jetpack.speedHoverAscend) * throttle * verticalSprintMulti);
                            }
                        }
                    } else {
                        fly(player, Math.min(motionY + currentAccel, -hoverSpeed));
                    }

                    double speedSideways = (player.isCrouching() ? jetpack.speedSide * 0.5F : jetpack.speedSide) * throttle;
                    double speedForward = (player.isSprinting() ? speedSideways * jetpack.sprintSpeed : speedSideways) * throttle;

                    if (!player.isFallFlying()) {
                        if (InputHandler.isHoldingForwards(player)) {
                            player.moveRelative(1, new Vec3(0, 0, speedForward));
                        }

                        if (InputHandler.isHoldingBackwards(player)) {
                            player.moveRelative(1, new Vec3(0, 0, -speedSideways * 0.8F));
                        }

                        if (InputHandler.isHoldingLeft(player)) {
                            player.moveRelative(1, new Vec3(speedSideways, 0, 0));
                        }

                        if (InputHandler.isHoldingRight(player)) {
                            player.moveRelative(1, new Vec3(-speedSideways, 0, 0));
                        }
                    }

                    if (!level.isClientSide()) {
                        player.fallDistance = 0.0F;

                        if (player instanceof ServerPlayer) {
                            ((ServerPlayer) player).connection.aboveGroundTickCount = 0;
                        }
                    }
                }
            }
        }
    }

    private static void fly(Player player, double y) {
        var motion = player.getDeltaMovement();
        player.setDeltaMovement(motion.x, y, motion.z);
    }
}
