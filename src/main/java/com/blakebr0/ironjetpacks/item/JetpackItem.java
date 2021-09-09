package com.blakebr0.ironjetpacks.item;

import com.blakebr0.cucumber.energy.EnergyCapabilityProvider;
import com.blakebr0.cucumber.energy.ItemEnergyStorage;
import com.blakebr0.cucumber.iface.IColored;
import com.blakebr0.cucumber.iface.IEnableable;
import com.blakebr0.cucumber.item.BaseArmorItem;
import com.blakebr0.cucumber.lib.Tooltips;
import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.client.ModelHandler;
import com.blakebr0.ironjetpacks.client.model.JetpackModel;
import com.blakebr0.ironjetpacks.config.ModConfigs;
import com.blakebr0.ironjetpacks.handler.InputHandler;
import com.blakebr0.ironjetpacks.lib.ModTooltips;
import com.blakebr0.ironjetpacks.registry.Jetpack;
import com.blakebr0.ironjetpacks.util.JetpackUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class JetpackItem extends BaseArmorItem implements IColored, DyeableLeatherItem, IEnableable {
	private final Jetpack jetpack;

	public JetpackItem(Jetpack jetpack, Function<Properties, Properties> properties) {
		super(JetpackUtils.makeArmorMaterial(jetpack), EquipmentSlot.CHEST, properties.compose(p -> p.defaultDurability(0).rarity(jetpack.rarity)));
		this.jetpack = jetpack;
	}
	
	@Override
	public Component getName(ItemStack stack) {
		return Localizable.of("item.ironjetpacks.jetpack").args(this.jetpack.displayName).build();
	}

	@Override
	public String getDescriptionId(ItemStack stack) {
		return Localizable.of("item.ironjetpacks.jetpack").args(this.jetpack.displayName).buildString();
	}

	/*
	 * Jetpack logic is very much like Simply Jetpacks, since I used it to learn how to make this work
	 * Credit to Tonius & Tomson124
	 * https://github.com/Tomson124/SimplyJetpacks-2/blob/1.12/src/main/java/tonius/simplyjetpacks/item/rewrite/ItemJetpack.java
	 */
	@Override
	public void onArmorTick(ItemStack stack, Level level, Player player) {
		var chest = player.getItemBySlot(EquipmentSlot.CHEST);
		var item = chest.getItem();

		if (!chest.isEmpty() && item instanceof JetpackItem jetpack) {
			if (JetpackUtils.isEngineOn(chest)) {
				var hover = JetpackUtils.isHovering(chest);

				if (InputHandler.isHoldingUp(player) || hover && !player.isOnGround()) {
					Jetpack info = jetpack.getJetpack();

					double motionY = player.getDeltaMovement().y();
					double hoverSpeed = InputHandler.isHoldingDown(player) ? info.speedHover : info.speedHoverSlow;
					double currentAccel = info.accelVert * (motionY < 0.3D ? 2.5D : 1.0D);
					double currentSpeedVertical = info.speedVert * (player.isInWater() ? 0.4D : 1.0D);
					
					double usage = player.isSprinting() || InputHandler.isHoldingSprint(player) ? info.usage * info.sprintFuel : info.usage;
					
					var creative = info.creative;
					
					var energy = JetpackUtils.getEnergyStorage(chest);
					if (!player.isCreative() && !creative) {
						energy.extractEnergy((int) usage, false);
					}
					
					if (energy.getEnergyStored() > 0 || player.isCreative() || creative) {
						double throttle = JetpackUtils.getThrottle(stack);
						double verticalSprintMulti = motionY >= 0 && InputHandler.isHoldingSprint(player) ? info.sprintSpeedVert : 1.0D;

						if (InputHandler.isHoldingUp(player)) {
							if (!hover) {
								fly(player, Math.min(motionY + currentAccel, currentSpeedVertical) * throttle * verticalSprintMulti);
							} else {
								if (InputHandler.isHoldingDown(player)) {
									fly(player, Math.min(motionY + currentAccel, -info.speedHoverSlow) * throttle);
								} else {
									fly(player, Math.min(motionY + currentAccel, info.speedHover) * throttle * verticalSprintMulti);
								}
							}
						} else {
							fly(player, Math.min(motionY + currentAccel, -hoverSpeed) * throttle);
						}
						
						double speedSideways = (player.isCrouching() ? info.speedSide * 0.5F : info.speedSide) * throttle;
						double speedForward = (player.isSprinting() ? speedSideways * info.sprintSpeed : speedSideways) * throttle;
						
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
		return ModConfigs.ENCHANTABLE_JETPACKS.get() && this.jetpack.enchantablilty > 0;
	}
	
	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return ModConfigs.ENCHANTABLE_JETPACKS.get() && this.jetpack.enchantablilty > 0;
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		var energy = JetpackUtils.getEnergyStorage(stack);
		double stored = energy.getMaxEnergyStored() - energy.getEnergyStored();

		return stored / energy.getMaxEnergyStored();
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return !this.jetpack.creative;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag advanced) {
		if (!this.jetpack.creative) {
			var energy = JetpackUtils.getEnergyStorage(stack);
			tooltip.add(new TextComponent(Utils.format(energy.getEnergyStored()) + " / " + Utils.format(energy.getMaxEnergyStored()) + " FE").withStyle(ChatFormatting.GRAY));
		} else {
			tooltip.add(ModTooltips.INFINITE.build().append(" FE"));
		}

		var tier = ModTooltips.TIER.color(this.jetpack.rarity.color).args(this.jetpack.creative ? "C" : this.jetpack.tier).build();
		var engine = ModTooltips.ENGINE.color(JetpackUtils.isEngineOn(stack) ? ChatFormatting.GREEN : ChatFormatting.RED).build();
		var hover = ModTooltips.HOVER.color(JetpackUtils.isHovering(stack) ? ChatFormatting.GREEN : ChatFormatting.RED).build();

		tooltip.add(ModTooltips.STATE_TOOLTIP_LAYOUT.args(tier, engine, hover).build());

		var throttle = new TextComponent((int) (JetpackUtils.getThrottle(stack) * 100) + "%");

		tooltip.add(ModTooltips.THROTTLE.args(throttle).build());
		
		if (ModConfigs.ENABLE_ADVANCED_INFO_TOOLTIPS.get()) {
			tooltip.add(new TextComponent(""));

			if (!Screen.hasShiftDown()) {
				tooltip.add(Tooltips.HOLD_SHIFT_FOR_INFO.build());
			} else {
				tooltip.add(ModTooltips.FUEL_USAGE.args(this.jetpack.usage + " FE/t").build());
				tooltip.add(ModTooltips.VERTICAL_SPEED.args(this.jetpack.speedVert).build());
				tooltip.add(ModTooltips.VERTICAL_ACCELERATION.args(this.jetpack.accelVert).build());
				tooltip.add(ModTooltips.HORIZONTAL_SPEED.args(this.jetpack.speedSide).build());
				tooltip.add(ModTooltips.HOVER_SPEED.args(this.jetpack.speedHoverSlow).build());
				tooltip.add(ModTooltips.DESCEND_SPEED.args(this.jetpack.speedHover).build());
				tooltip.add(ModTooltips.SPRINT_MODIFIER.args(this.jetpack.sprintSpeed).build());
				tooltip.add(ModTooltips.SPRINT_VERTICAL_MODIFIER.args(this.jetpack.sprintSpeedVert).build());
				tooltip.add(ModTooltips.SPRINT_FUEL_MODIFIER.args(this.jetpack.sprintFuel).build());
			}
		}
	}

	@Override
	public void initializeClient(Consumer<IItemRenderProperties> consumer) {
		consumer.accept(ItemRenderProperties.INSTANCE);
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		return type == null ? IronJetpacks.MOD_ID + ":textures/armor/jetpack.png" : IronJetpacks.MOD_ID + ":textures/armor/jetpack_overlay.png";
	}
	
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
		return new EnergyCapabilityProvider(new ItemEnergyStorage(stack, this.jetpack.capacity));
	}

	@Override
	public int getColor(int i) {
		return i == 1 ? this.jetpack.color : -1;
	}

	@Override
	public boolean hasCustomColor(ItemStack stack) {
		return true;
	}

	@Override
	public int getColor(ItemStack stack) {
		return this.jetpack.color;
	}

	@Override
	public void clearColor(ItemStack stack) { }

	@Override
	public void setColor(ItemStack stack, int color) { }

	@Override
	public boolean isEnabled() {
		return !this.jetpack.disabled;
	}

	public Jetpack getJetpack() {
		return this.jetpack;
	}

	private static void fly(Player player, double y) {
		var motion = player.getDeltaMovement();
		player.setDeltaMovement(motion.x(), y, motion.z());
	}

	static class ItemRenderProperties implements IItemRenderProperties {
		public static final ItemRenderProperties INSTANCE = new ItemRenderProperties();

		private JetpackModel model;

		@Override
		public <A extends HumanoidModel<?>> A getArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, A _default) {
			if (this.model == null) {
				var layer = Minecraft.getInstance().getEntityModels().bakeLayer(ModelHandler.JETPACK_LAYER);
				var jetpack = (JetpackItem) stack.getItem();

				this.model = new JetpackModel(jetpack, layer);
			}

			return (A) this.model;
		}
	}
}
