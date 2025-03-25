package com.blakebr0.ironjetpacks.item;

import com.blakebr0.cucumber.iface.IColored;
import com.blakebr0.cucumber.item.BaseArmorItem;
import com.blakebr0.cucumber.lib.Tooltips;
import com.blakebr0.cucumber.util.Formatting;
import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.ironjetpacks.client.handler.InputHandler;
import com.blakebr0.ironjetpacks.config.ModConfigs;
import com.blakebr0.ironjetpacks.init.ModArmorMaterials;
import com.blakebr0.ironjetpacks.lib.ModTooltips;
import com.blakebr0.ironjetpacks.util.JetpackUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Unbreakable;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class JetpackItem extends BaseArmorItem implements IColored {
    public JetpackItem() {
        super(ModArmorMaterials.JETPACK, Type.CHESTPLATE, p -> p
                .stacksTo(1)
                .component(DataComponents.UNBREAKABLE, new Unbreakable(false))
                .setNoRepair()
        );
    }

    @Override
    public void verifyComponentsAfterLoad(ItemStack stack) {
        var jetpack = JetpackUtils.getJetpack(stack);

        if (stack.isEnchanted()) {
            var rarity = switch (jetpack.rarity) {
                case COMMON, UNCOMMON -> Rarity.RARE;
                case RARE -> Rarity.EPIC;
                case EPIC -> jetpack.rarity;
            };

            stack.set(DataComponents.RARITY, rarity);
        } else {
            stack.set(DataComponents.RARITY, jetpack.rarity);
        }
    }

    @Override
    public Component getName(ItemStack stack) {
        var jetpack = JetpackUtils.getJetpack(stack);
        return Localizable.of("item.ironjetpacks.jetpack").args(jetpack.getDisplayName()).build();
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        var jetpack = JetpackUtils.getJetpack(stack);
        return Localizable.of("item.ironjetpacks.jetpack").args(jetpack.getDisplayName()).buildString();
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        var jetpack = JetpackUtils.getJetpack(stack);
        return jetpack.enchantablilty;
    }

    /*
     * Jetpack logic is very much like Simply Jetpacks, since I used it to learn how to make this work
     * Credit to Tonius & Tomson124
     * https://github.com/Tomson124/SimplyJetpacks-2/blob/1.12/src/main/java/tonius/simplyjetpacks/item/rewrite/ItemJetpack.java
     */
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean isSelected) {
        if (entity instanceof Player player) {
            var chest = JetpackUtils.getEquippedJetpack(player);
            if (chest.isEmpty() || chest != stack)
                return;

            var item = chest.getItem();
            if (item instanceof JetpackItem && JetpackUtils.isEngineOn(chest)) {
                var hover = JetpackUtils.isHovering(chest);

                if (InputHandler.isHoldingUp(player) || hover && !player.onGround()) {
                    var jetpack = JetpackUtils.getJetpack(stack);

                    double motionY = player.getDeltaMovement().y();
                    double hoverSpeed = InputHandler.isHoldingDown(player) ? jetpack.speedHoverDescend : jetpack.speedHoverSlow;
                    double currentAccel = jetpack.accelVert * (motionY < 0.3D ? 2.5D : 1.0D);
                    double currentSpeedVertical = jetpack.speedVert * (player.isInWater() ? 0.4D : 1.0D);

                    double usage = player.isSprinting() || InputHandler.isHoldingSprint(player) ? jetpack.usage * jetpack.sprintFuel : jetpack.usage;

                    var creative = jetpack.creative;
                    var energy = JetpackUtils.getEnergyStorage(chest);

                    if (!player.isCreative() && !creative) {
                        energy.extractEnergy((int) usage, false);
                    }

                    if (hover && player.isFallFlying()) {
                        player.stopFallFlying();
                    }

                    if (energy.getEnergyStored() > 0 || player.isCreative() || creative) {
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
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        var jetpack = JetpackUtils.getJetpack(stack);
        return ModConfigs.ENCHANTABLE_JETPACKS.get() && jetpack.enchantablilty > 0;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        var jetpack = JetpackUtils.getJetpack(stack);
        return ModConfigs.ENCHANTABLE_JETPACKS.get() && jetpack.enchantablilty > 0;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        var energy = JetpackUtils.getEnergyStorage(stack);
        var stored = energy.getMaxEnergyStored() - energy.getEnergyStored();

        return Math.round(13.0F - stored * 13.0F / energy.getMaxEnergyStored());
    }

    @Override
    public int getBarColor(ItemStack stack) {
        var energy = JetpackUtils.getEnergyStorage(stack);

        float f = Math.max(0.0F, (float) energy.getEnergyStored() / (float) energy.getMaxEnergyStored());

        return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        var jetpack = JetpackUtils.getJetpack(stack);
        return !jetpack.creative;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        var jetpack = JetpackUtils.getJetpack(stack);

        if (flag.isAdvanced()) {
            tooltip.add(ModTooltips.JETPACK_ID.args(jetpack.getId().toString()).color(ChatFormatting.DARK_GRAY).build());
            tooltip.add(Component.literal(" "));
        }

        if (!jetpack.creative) {
            var energy = JetpackUtils.getEnergyStorage(stack);
            tooltip.add(Formatting.number(energy.getEnergyStored()).append(" / ").append(Formatting.energy(energy.getMaxEnergyStored()).withStyle(ChatFormatting.GRAY)));
        } else {
            tooltip.add(ModTooltips.INFINITE.build().append(" FE"));
        }

        var tier = ModTooltips.TIER.color(jetpack.rarity.color()).args(jetpack.creative ? "C" : jetpack.tier).build();
        var engine = ModTooltips.ENGINE.color(JetpackUtils.isEngineOn(stack) ? ChatFormatting.GREEN : ChatFormatting.RED).build();
        var hover = ModTooltips.HOVER.color(JetpackUtils.isHovering(stack) ? ChatFormatting.GREEN : ChatFormatting.RED).build();

        tooltip.add(ModTooltips.STATE_TOOLTIP_LAYOUT.args(tier, engine, hover).build());

        var throttle = Component.literal((int) (JetpackUtils.getThrottle(stack) * 100) + "%");

        tooltip.add(ModTooltips.THROTTLE.args(throttle).build());

        if (ModConfigs.ENABLE_ADVANCED_INFO_TOOLTIPS.get()) {
            tooltip.add(Component.literal(" "));

            if (!Screen.hasShiftDown()) {
                tooltip.add(Tooltips.HOLD_SHIFT_FOR_INFO.build());
            } else {
                tooltip.add(ModTooltips.FUEL_USAGE.args(jetpack.usage + " FE/t").build());
                tooltip.add(ModTooltips.VERTICAL_SPEED.args(jetpack.speedVert).build());
                tooltip.add(ModTooltips.VERTICAL_ACCELERATION.args(jetpack.accelVert).build());
                tooltip.add(ModTooltips.HORIZONTAL_SPEED.args(jetpack.speedSide).build());
                tooltip.add(ModTooltips.HOVER_SPEED.args(jetpack.speedHoverSlow).build());
                tooltip.add(ModTooltips.HOVER_ASCEND_SPEED.args(jetpack.speedHoverAscend).build());
                tooltip.add(ModTooltips.HOVER_DESCEND_SPEED.args(jetpack.speedHoverDescend).build());
                tooltip.add(ModTooltips.SPRINT_MODIFIER.args(jetpack.sprintSpeed).build());
                tooltip.add(ModTooltips.SPRINT_VERTICAL_MODIFIER.args(jetpack.sprintSpeedVert).build());
                tooltip.add(ModTooltips.SPRINT_FUEL_MODIFIER.args(jetpack.sprintFuel).build());
            }
        }
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        var jetpack = JetpackUtils.getJetpack(stack);
        return jetpack.createAttributeModifiers();
    }

    @Override
    public int getColor(int i, ItemStack stack) {
        var jetpack = JetpackUtils.getJetpack(stack);
        return i == 1 ? jetpack.color : -1;
    }

    private static void fly(Player player, double y) {
        var motion = player.getDeltaMovement();
        player.setDeltaMovement(motion.x(), y, motion.z());
    }
}
