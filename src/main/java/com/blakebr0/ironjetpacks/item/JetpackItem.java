package com.blakebr0.ironjetpacks.item;

import com.blakebr0.cucumber.energy.EnergyCapabilityProvider;
import com.blakebr0.cucumber.energy.ItemEnergyStorage;
import com.blakebr0.cucumber.helper.NBTHelper;
import com.blakebr0.cucumber.iface.IColored;
import com.blakebr0.cucumber.item.BaseArmorItem;
import com.blakebr0.cucumber.lib.Localizable;
import com.blakebr0.cucumber.lib.Tooltips;
import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.client.model.ModelJetpack;
import com.blakebr0.ironjetpacks.config.ModConfigs;
import com.blakebr0.ironjetpacks.handler.InputHandler;
import com.blakebr0.ironjetpacks.lib.ModTooltips;
import com.blakebr0.ironjetpacks.registry.Jetpack;
import com.blakebr0.ironjetpacks.util.JetpackUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.function.Function;

public class JetpackItem extends BaseArmorItem implements IColored {
	private static final IEnergyStorage EMPTY_ENERGY_STORAGE = new EnergyStorage(0);
	private Jetpack jetpack;

	public JetpackItem(Jetpack jetpack, Function<Properties, Properties> properties) {
		super(JetpackUtils.makeArmorMaterial(jetpack), EquipmentSlotType.CHEST, properties.compose(p -> p.defaultMaxDamage(0).rarity(jetpack.rarity)));
		this.jetpack = jetpack;
	}
	
	public Jetpack getJetpack() {
		return this.jetpack;
	}
	
	public IEnergyStorage getEnergyStorage(ItemStack stack) {
//		return stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(EMPTY_ENERGY_STORAGE);
		return EMPTY_ENERGY_STORAGE;
	}
	
	public boolean isEngineOn(ItemStack stack) {
		return NBTHelper.getBoolean(stack, "Engine");
	}
	
	public boolean toggleEngine(ItemStack stack) {
		boolean current = NBTHelper.getBoolean(stack, "Engine");
		NBTHelper.flipBoolean(stack, "Engine");
		return !current;
	}
	
	public boolean isHovering(ItemStack stack) {
		return NBTHelper.getBoolean(stack, "Hover");
	}
	
	public boolean toggleHover(ItemStack stack) {
		boolean current = NBTHelper.getBoolean(stack, "Hover");
		NBTHelper.flipBoolean(stack, "Hover");
		return !current;
	}
	
	@Override
	public ITextComponent getDisplayName(ItemStack stack) {
		String name = StringUtils.capitalize(this.jetpack.name.replace(" ", "_"));
		return Localizable.of("item.ironjetpacks.jetpack").args(name).build();
	}
	
	/*
	 * Jetpack logic is very much like Simply Jetpacks, since I used it to learn how to make this work
	 * Credit to Tonius & Tomson124
	 * https://github.com/Tomson124/SimplyJetpacks-2/blob/1.12/src/main/java/tonius/simplyjetpacks/item/rewrite/ItemJetpack.java
	 */
	@Override
	public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
		ItemStack chest = player.getItemStackFromSlot(EquipmentSlotType.CHEST);
		Item item = chest.getItem();
		if (!chest.isEmpty() && item instanceof JetpackItem) {
			JetpackItem jetpack = (JetpackItem) item;
			if (jetpack.isEngineOn(chest)) {
				boolean hover = jetpack.isHovering(chest);
				if (InputHandler.isHoldingUp(player) || hover && !player.onGround) {
					Jetpack info = jetpack.getJetpack();
					
					double hoverSpeed = InputHandler.isHoldingDown(player) ? info.speedHover : info.speedHoverSlow;
					double currentAccel = info.accelVert * (player.getMotion().y < 0.3D ? 2.5D : 1.0D);
					double currentSpeedVertical = info.speedVert * (player.isInWater() ? 0.4D : 1.0D);
					
					double usage = player.isSprinting() ? info.usage * info.sprintFuel : info.usage;
					
					boolean creative = info.creative;
					
					IEnergyStorage energy = jetpack.getEnergyStorage(chest);
					if (!player.isCreative() && !creative) {
						energy.extractEnergy((int) usage, false);
					}
					
					if (energy.getEnergyStored() > 0 || player.isCreative() || creative) {
						double motionY = player.getMotion().getY();
						if (InputHandler.isHoldingUp(player)) {
							if (!hover) {
								player.setMotion(0, Math.min(motionY + currentAccel, currentSpeedVertical), 0);
							} else {
								if (InputHandler.isHoldingDown(player)) {
									player.setMotion(0, Math.min(motionY + currentAccel, -info.speedHoverSlow), 0);
								} else {
									player.setMotion(0, Math.min(motionY + currentAccel, info.speedHover), 0);
								}
							}					
						} else {
							player.setMotion(0, Math.min(motionY + currentAccel, -hoverSpeed), 0);
						}
						
						float speedSideways = (float) (player.isSneaking() ? info.speedSide * 0.5F : info.speedSide);
						float speedForward = (float) (player.isSprinting() ? speedSideways * info.sprintSpeed : speedSideways);
						
						if (InputHandler.isHoldingForwards(player)) {
							player.moveRelative(0, new Vec3d(0, speedForward, speedForward));
						}
						
						if (InputHandler.isHoldingBackwards(player)) {
							player.moveRelative(0, new Vec3d(0, -speedSideways, speedSideways * 0.8F));
						}
						
						if (InputHandler.isHoldingLeft(player)) {
							player.moveRelative(speedSideways, new Vec3d(0, 0, speedSideways));
						}
						
						if (InputHandler.isHoldingRight(player)) {
							player.moveRelative(-speedSideways, new Vec3d(0, 0, speedSideways));
						}
						
						if (!world.isRemote) {
							player.fallDistance = 0.0F;
							
							if (player instanceof ServerPlayerEntity) {
								((ServerPlayerEntity) player).connection.floatingTickCount = 0;
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
		IEnergyStorage energy = this.getEnergyStorage(stack);
		double stored = energy.getMaxEnergyStored() - energy.getEnergyStored();
		return stored / energy.getMaxEnergyStored();
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return !this.jetpack.creative;
	}
	
	@Override
	public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag advanced) {
		if (!this.jetpack.creative) {
			IEnergyStorage energy = getEnergyStorage(stack);
			tooltip.add(new StringTextComponent(Utils.format(energy.getEnergyStored()) + " / " + Utils.format(energy.getMaxEnergyStored()) + " FE"));
		} else {
			tooltip.add(ModTooltips.INFINITE.build().appendText(" FE"));
		}

		ITextComponent tier = ModTooltips.TIER.color(this.jetpack.rarity.color).args(this.jetpack.creative ? "C" : this.jetpack.tier).build();
		ITextComponent engine = ModTooltips.ENGINE.color(isEngineOn(stack) ? TextFormatting.GREEN : TextFormatting.RED).build();
		ITextComponent hover = ModTooltips.HOVER.color(isHovering(stack) ? TextFormatting.GREEN : TextFormatting.RED).build();

		tooltip.add(ModTooltips.STATE_TOOLTIP_LAYOUT.args(tier, engine, hover).build());
		
//		if (ModConfigs.ENABLE_ADVANCED_INFO_TOOLTIPS.get()) {
			tooltip.add(new StringTextComponent(""));
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
				tooltip.add(ModTooltips.SPRINT_FUEL_MODIFIER.args(this.jetpack.sprintFuel).build());
			}
//		}
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public BipedModel getArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlotType slot, BipedModel _default) {
		return new ModelJetpack(this);
	}
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
		return !type.equals("overlay") ? IronJetpacks.MOD_ID + ":textures/armor/jetpack.png" : IronJetpacks.MOD_ID + ":textures/armor/jetpack_overlay.png";
	}
	
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
		return new EnergyCapabilityProvider(new ItemEnergyStorage(stack, this.jetpack.capacity));
	}

	@Override
	public int getColor(int i) {
		return i == 1 ? this.jetpack.color : -1;
	}
}
