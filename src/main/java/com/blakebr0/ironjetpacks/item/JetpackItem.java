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
import com.blakebr0.ironjetpacks.client.model.JetpackModel;
import com.blakebr0.ironjetpacks.config.ModConfigs;
import com.blakebr0.ironjetpacks.handler.InputHandler;
import com.blakebr0.ironjetpacks.lib.ModTooltips;
import com.blakebr0.ironjetpacks.registry.Jetpack;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry;
import com.blakebr0.ironjetpacks.util.JetpackUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.List;
import java.util.function.Function;

public class JetpackItem extends BaseArmorItem implements IColored, IDyeableArmorItem, IEnableable {
	private final String jetpackName;
	private IArmorMaterial armorMaterial;
	private BipedModel<?> model;

	public JetpackItem(String jetpackName, Function<Properties, Properties> properties) {
		super(ArmorMaterial.LEATHER, EquipmentSlotType.CHEST, properties.compose(p -> p.defaultDurability(0)));
		this.jetpackName = jetpackName;
	}
	
	@Override
	public ITextComponent getName(ItemStack stack) {
		return Localizable.of("item.ironjetpacks.jetpack").args(this.getJetpack().displayName).build();
	}

	@Override
	public String getDescriptionId(ItemStack stack) {
		return Localizable.of("item.ironjetpacks.jetpack").args(this.getJetpack().displayName).buildString();
	}

	@Override
	public Rarity getRarity(ItemStack stack) {
		if (!stack.isEnchanted()) {
			return this.getJetpack().rarity;
		} else {
			switch (this.getJetpack().rarity) {
				case COMMON:
				case UNCOMMON:
					return Rarity.RARE;
				case RARE:
					return Rarity.EPIC;
				case EPIC:
				default:
					return this.getJetpack().rarity;
			}
		}
	}

	@Override
	public IArmorMaterial getMaterial() {
		if (this.armorMaterial == null) {
			this.armorMaterial = JetpackUtils.makeArmorMaterial(this.getJetpack());
		}

		return this.armorMaterial;
	}

	@Override
	public int getEnchantmentValue() {
		return this.getMaterial().getEnchantmentValue();
	}

	@Override
	public boolean isValidRepairItem(ItemStack p_82789_1_, ItemStack p_82789_2_) {
		return this.getMaterial().getRepairIngredient().test(p_82789_2_) || super.isValidRepairItem(p_82789_1_, p_82789_2_);
	}

	/*
	 * Jetpack logic is very much like Simply Jetpacks, since I used it to learn how to make this work
	 * Credit to Tonius & Tomson124
	 * https://github.com/Tomson124/SimplyJetpacks-2/blob/1.12/src/main/java/tonius/simplyjetpacks/item/rewrite/ItemJetpack.java
	 */
	@Override
	public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
		ItemStack chest = player.getItemBySlot(EquipmentSlotType.CHEST);
		Item item = chest.getItem();
		if (!chest.isEmpty() && item instanceof JetpackItem) {
			JetpackItem jetpack = (JetpackItem) item;
			if (JetpackUtils.isEngineOn(chest)) {
				boolean hover = JetpackUtils.isHovering(chest);
				if (InputHandler.isHoldingUp(player) || hover && !player.isOnGround()) {
					Jetpack info = jetpack.getJetpack();

					double motionY = player.getDeltaMovement().y();
					double hoverSpeed = InputHandler.isHoldingDown(player) ? info.speedHover : info.speedHoverSlow;
					double currentAccel = info.accelVert * (motionY < 0.3D ? 2.5D : 1.0D);
					double currentSpeedVertical = info.speedVert * (player.isInWater() ? 0.4D : 1.0D);
					
					double usage = player.isSprinting() || InputHandler.isHoldingSprint(player) ? info.usage * info.sprintFuel : info.usage;
					
					boolean creative = info.creative;
					
					IEnergyStorage energy = JetpackUtils.getEnergyStorage(chest);
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
							player.moveRelative(1, new Vector3d(0, 0, speedForward));
						}
						
						if (InputHandler.isHoldingBackwards(player)) {
							player.moveRelative(1, new Vector3d(0, 0, -speedSideways * 0.8F));
						}
						
						if (InputHandler.isHoldingLeft(player)) {
							player.moveRelative(1, new Vector3d(speedSideways, 0, 0));
						}
						
						if (InputHandler.isHoldingRight(player)) {
							player.moveRelative(1, new Vector3d(-speedSideways, 0, 0));
						}
						
						if (!world.isClientSide()) {
							player.fallDistance = 0.0F;
							
							if (player instanceof ServerPlayerEntity) {
								((ServerPlayerEntity) player).connection.aboveGroundTickCount = 0;
							}
						}
					}
				}
			}
		}
	}
	
	@Override
	public boolean isEnchantable(ItemStack stack) {
		return ModConfigs.ENCHANTABLE_JETPACKS.get() && this.getJetpack().enchantablilty > 0;
	}
	
	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return ModConfigs.ENCHANTABLE_JETPACKS.get() && this.getJetpack().enchantablilty > 0;
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		IEnergyStorage energy = JetpackUtils.getEnergyStorage(stack);
		double stored = energy.getMaxEnergyStored() - energy.getEnergyStored();
		return stored / energy.getMaxEnergyStored();
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return !this.getJetpack().creative;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag advanced) {
		if (!this.getJetpack().creative) {
			IEnergyStorage energy = JetpackUtils.getEnergyStorage(stack);
			tooltip.add(new StringTextComponent(Utils.format(energy.getEnergyStored()) + " / " + Utils.format(energy.getMaxEnergyStored()) + " FE").withStyle(TextFormatting.GRAY));
		} else {
			tooltip.add(ModTooltips.INFINITE.build().append(" FE"));
		}

		ITextComponent tier = ModTooltips.TIER.color(this.getJetpack().rarity.color).args(this.getJetpack().creative ? "C" : this.getJetpack().tier).build();
		ITextComponent engine = ModTooltips.ENGINE.color(JetpackUtils.isEngineOn(stack) ? TextFormatting.GREEN : TextFormatting.RED).build();
		ITextComponent hover = ModTooltips.HOVER.color(JetpackUtils.isHovering(stack) ? TextFormatting.GREEN : TextFormatting.RED).build();

		tooltip.add(ModTooltips.STATE_TOOLTIP_LAYOUT.args(tier, engine, hover).build());

		ITextComponent throttle = new StringTextComponent((int) (JetpackUtils.getThrottle(stack) * 100) + "%");

		tooltip.add(ModTooltips.THROTTLE.args(throttle).build());
		
		if (ModConfigs.ENABLE_ADVANCED_INFO_TOOLTIPS.get()) {
			tooltip.add(new StringTextComponent(""));
			if (!Screen.hasShiftDown()) {
				tooltip.add(Tooltips.HOLD_SHIFT_FOR_INFO.build());
			} else {
				tooltip.add(ModTooltips.FUEL_USAGE.args(this.getJetpack().usage + " FE/t").build());
				tooltip.add(ModTooltips.VERTICAL_SPEED.args(this.getJetpack().speedVert).build());
				tooltip.add(ModTooltips.VERTICAL_ACCELERATION.args(this.getJetpack().accelVert).build());
				tooltip.add(ModTooltips.HORIZONTAL_SPEED.args(this.getJetpack().speedSide).build());
				tooltip.add(ModTooltips.HOVER_SPEED.args(this.getJetpack().speedHoverSlow).build());
				tooltip.add(ModTooltips.DESCEND_SPEED.args(this.getJetpack().speedHover).build());
				tooltip.add(ModTooltips.SPRINT_MODIFIER.args(this.getJetpack().sprintSpeed).build());
				tooltip.add(ModTooltips.SPRINT_VERTICAL_MODIFIER.args(this.getJetpack().sprintSpeedVert).build());
				tooltip.add(ModTooltips.SPRINT_FUEL_MODIFIER.args(this.getJetpack().sprintFuel).build());
			}
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public BipedModel<?> getArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlotType slot, BipedModel _default) {
		if (this.model == null)
			this.model = new JetpackModel(this);

		return this.model;
	}
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
		return type == null ? IronJetpacks.MOD_ID + ":textures/armor/jetpack.png" : IronJetpacks.MOD_ID + ":textures/armor/jetpack_overlay.png";
	}
	
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
		return new EnergyCapabilityProvider(new ItemEnergyStorage(stack, this.getJetpack().capacity));
	}

	@Override
	public int getColor(int i) {
		return i == 1 ? this.getJetpack().color : -1;
	}

	@Override
	public boolean hasCustomColor(ItemStack stack) {
		return true;
	}

	@Override
	public int getColor(ItemStack stack) {
		return this.getJetpack().color;
	}

	@Override
	public void clearColor(ItemStack stack) { }

	@Override
	public void setColor(ItemStack stack, int color) { }

	@Override
	public boolean isEnabled() {
		return !this.getJetpack().disabled;
	}

	public Jetpack getJetpack() {
		return JetpackRegistry.getInstance().getJetpackByName(this.jetpackName);
	}

	private static void fly(PlayerEntity player, double y) {
		Vector3d motion = player.getDeltaMovement();
		player.setDeltaMovement(motion.x(), y, motion.z());
	}
}
