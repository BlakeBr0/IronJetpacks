package com.blakebr0.ironjetpacks.item;

import com.blakebr0.cucumber.iface.IColored;
import com.blakebr0.cucumber.iface.IComponentInitializer;
import com.blakebr0.cucumber.item.BaseArmorItem;
import com.blakebr0.cucumber.lib.Tooltips;
import com.blakebr0.cucumber.util.Formatting;
import com.blakebr0.ironjetpacks.client.handler.InputHandler;
import com.blakebr0.ironjetpacks.config.ModConfigs;
import com.blakebr0.ironjetpacks.lib.ModArmorMaterials;
import com.blakebr0.ironjetpacks.lib.ModTooltips;
import com.blakebr0.ironjetpacks.util.JetpackUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.Enchantable;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

public class JetpackItem extends BaseArmorItem implements IColored, IComponentInitializer {
    public JetpackItem(Identifier id) {
        super(id, ModArmorMaterials.JETPACK, ArmorType.CHESTPLATE, p -> p
                .stacksTo(1)
                .component(DataComponents.UNBREAKABLE, Unit.INSTANCE)
                .setNoCombineRepair()
        );
    }

    @Override
    public Component getName(ItemStack stack) {
        var jetpack = JetpackUtils.getJetpack(stack);
        return Component.translatable("item.ironjetpacks.jetpack", jetpack.getDisplayName());
    }

    /*
     * Jetpack logic is very much like Simply Jetpacks, since I used it to learn how to make this work
     * Credit to Tonius & Tomson124
     * https://github.com/Tomson124/SimplyJetpacks-2/blob/1.12/src/main/java/tonius/simplyjetpacks/item/rewrite/ItemJetpack.java
     */
    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slot) {
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
                        try (var tx = Transaction.openRoot()) {
                            energy.extract((int) usage, tx);
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
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        var energy = JetpackUtils.getEnergyStorage(stack);
        var stored = energy.getCapacityAsInt() - energy.getAmountAsInt();

        return Math.round(13.0F - stored * 13.0F / energy.getCapacityAsInt());
    }

    @Override
    public int getBarColor(ItemStack stack) {
        var energy = JetpackUtils.getEnergyStorage(stack);

        float f = Math.max(0.0F, (float) energy.getAmountAsInt() / (float) energy.getCapacityAsInt());

        return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        var jetpack = JetpackUtils.getJetpack(stack);
        return !jetpack.creative;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag) {
        var jetpack = JetpackUtils.getJetpack(stack);

        if (flag.isAdvanced()) {
            builder.accept(ModTooltips.JETPACK_ID.args(jetpack.getId().toString()).color(ChatFormatting.DARK_GRAY).toComponent());
            builder.accept(Component.literal(" "));
        }

        if (!jetpack.creative) {
            var energy = JetpackUtils.getEnergyStorage(stack);
            builder.accept(Formatting.number(energy.getAmountAsInt()).append(" / ").append(Formatting.energy(energy.getCapacityAsInt())).withStyle(ChatFormatting.GRAY));
        } else {
            builder.accept(ModTooltips.INFINITE.toComponent().append(" FE"));
        }

        var tier = ModTooltips.TIER.color(jetpack.rarity.color()).args(jetpack.creative ? "C" : jetpack.tier).toComponent();
        var engine = ModTooltips.ENGINE.color(JetpackUtils.isEngineOn(stack) ? ChatFormatting.GREEN : ChatFormatting.RED).toComponent();
        var hover = ModTooltips.HOVER.color(JetpackUtils.isHovering(stack) ? ChatFormatting.GREEN : ChatFormatting.RED).toComponent();
        var hud = ModTooltips.HUD.color(JetpackUtils.isHUDEnabled(stack) ? ChatFormatting.GREEN : ChatFormatting.RED).toComponent();

        builder.accept(Component.empty()
                .append(tier).append(" | ").withStyle(ChatFormatting.GRAY)
                .append(engine).append(" | ").withStyle(ChatFormatting.GRAY)
                .append(hover).append(" | ").withStyle(ChatFormatting.GRAY)
                .append(hud));

        var throttle = Component.literal((int) (JetpackUtils.getThrottle(stack) * 100) + "%");

        builder.accept(ModTooltips.THROTTLE.args(throttle).toComponent());

        if (ModConfigs.ENABLE_ADVANCED_INFO_TOOLTIPS.get()) {
            builder.accept(Component.literal(" "));

            if (!flag.hasShiftDown()) {
                builder.accept(Tooltips.HOLD_SHIFT_FOR_INFO.toComponent());
            } else {
                builder.accept(ModTooltips.FUEL_USAGE.args(jetpack.usage + " FE/t").toComponent());
                builder.accept(ModTooltips.VERTICAL_SPEED.args(jetpack.speedVert).toComponent());
                builder.accept(ModTooltips.VERTICAL_ACCELERATION.args(jetpack.accelVert).toComponent());
                builder.accept(ModTooltips.HORIZONTAL_SPEED.args(jetpack.speedSide).toComponent());
                builder.accept(ModTooltips.HOVER_SPEED.args(jetpack.speedHoverSlow).toComponent());
                builder.accept(ModTooltips.HOVER_ASCEND_SPEED.args(jetpack.speedHoverAscend).toComponent());
                builder.accept(ModTooltips.HOVER_DESCEND_SPEED.args(jetpack.speedHoverDescend).toComponent());
                builder.accept(ModTooltips.SPRINT_MODIFIER.args(jetpack.sprintSpeed).toComponent());
                builder.accept(ModTooltips.SPRINT_VERTICAL_MODIFIER.args(jetpack.sprintSpeedVert).toComponent());
                builder.accept(ModTooltips.SPRINT_FUEL_MODIFIER.args(jetpack.sprintFuel).toComponent());
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

    @Override
    public void initialize(ItemStack stack) {
        var jetpack = JetpackUtils.getJetpack(stack);

        stack.set(DataComponents.ATTRIBUTE_MODIFIERS, jetpack.createAttributeModifiers());

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

        if (ModConfigs.ENCHANTABLE_JETPACKS.get()) {
            stack.set(DataComponents.ENCHANTABLE, new Enchantable(jetpack.enchantablilty));
        }
    }

    private static void fly(Player player, double y) {
        var motion = player.getDeltaMovement();
        player.setDeltaMovement(motion.x(), y, motion.z());
    }
}
