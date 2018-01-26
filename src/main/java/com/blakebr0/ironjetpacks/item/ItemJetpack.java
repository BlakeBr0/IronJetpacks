package com.blakebr0.ironjetpacks.item;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import com.blakebr0.cucumber.energy.EnergyStorageItem;
import com.blakebr0.cucumber.iface.IColoredItem;
import com.blakebr0.cucumber.iface.IModelHelper;
import com.blakebr0.cucumber.lib.Colors;
import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.config.ModConfig;
import com.blakebr0.ironjetpacks.handler.InputHandler;
import com.blakebr0.ironjetpacks.lib.EnergyCapabilityProvider;
import com.blakebr0.ironjetpacks.lib.Tooltips;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry.JetpackEntry;
import com.blakebr0.ironjetpacks.registry.JetpackRegistry.JetpackType;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class ItemJetpack extends ItemArmor implements ISpecialArmor, IModelHelper, IColoredItem {
		
	private JetpackEntry info;
	private JetpackType type;

	public ItemJetpack(JetpackType type, JetpackEntry info) {
		super(makeMaterial(type), 2, EntityEquipmentSlot.CHEST);
		this.setUnlocalizedName("ij.jetpack");
		this.setCreativeTab(IronJetpacks.CREATIVE_TAB);
		this.setMaxDamage(0);
		this.type = type;
		this.info = info;
	}
	
	private static ArmorMaterial makeMaterial(JetpackType type) {
		return EnumHelper.addArmorMaterial("IJ:" + type.name.toUpperCase(Locale.ROOT), "ironjetpacks:jetpack", 0, new int[] { 0, 0, type.armorPoints, 0 }, type.enchantablilty, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0);
	}
	
	public JetpackType getJetpackType() {
		return type;
	}
	
	public JetpackEntry getJetpackInfo() {
		return info;
	}
	
	public IEnergyStorage getEnergyStorage(ItemStack stack) {
		if (stack.hasCapability(CapabilityEnergy.ENERGY, null)) {
			return stack.getCapability(CapabilityEnergy.ENERGY, null);
		}
		return null;
	}
	
	public boolean isEngineOn(ItemStack stack) {
		NBTTagCompound tag = stack.getTagCompound();
		if (tag != null && tag.hasKey("Engine")) {
			return tag.getBoolean("Engine");
		} else {
			return true;
		}
	}
	
	public boolean toggleEngine(ItemStack stack) {
		NBTTagCompound tag = stack.getTagCompound();
		boolean current;
		if (tag != null && tag.hasKey("Engine")) {
			current = tag.getBoolean("Engine");
		} else {
			if (tag == null) {
				tag = new NBTTagCompound();
				stack.setTagCompound(tag);
			}
			current = true;
		}
		tag.setBoolean("Engine", !current);
		return !current;
	}
	
	public boolean isHovering(ItemStack stack) {
		NBTTagCompound tag = stack.getTagCompound();
		if (tag != null && tag.hasKey("Hover")) {
			return tag.getBoolean("Hover");
		} else {
			return false;
		}
	}
	
	public boolean toggleHover(ItemStack stack) {
		NBTTagCompound tag = stack.getTagCompound();
		boolean current;
		if (tag != null && tag.hasKey("Hover")) {
			current = tag.getBoolean("Hover");
		} else {
			if (tag == null) {
				tag = new NBTTagCompound();
				stack.setTagCompound(tag);
			}
			current = false;
		}
		tag.setBoolean("Hover", !current);
		return !current;
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		String name = StringUtils.capitalize(this.type.name.replace(" ", "_"));
		return name + " " + Utils.localize(this.getUnlocalizedName() + ".name");
	}
	
	/*
	 * Jetpack logic is very much like Simply Jetpacks, since I used it to learn how to make this work
	 * Credit to Tonius & Tomson124
	 * https://github.com/Tomson124/SimplyJetpacks-2/blob/1.12/src/main/java/tonius/simplyjetpacks/item/rewrite/ItemJetpack.java
	 */
	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
		ItemStack chest = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		
		if (!chest.isEmpty() && chest.getItem() instanceof ItemJetpack) {
			ItemJetpack jetpack = (ItemJetpack) chest.getItem();
			
			if (jetpack.isEngineOn(chest)) {
				boolean hover = jetpack.isHovering(chest);
				
				if (InputHandler.isHoldingUp(player) || hover && !player.onGround) {
					JetpackEntry info = jetpack.getJetpackInfo();
					
					double hoverSpeed = InputHandler.isHoldingDown(player) ? info.speedHover : info.speedHoverSlow;
					double currentAccel = info.accelVert * (player.motionY < 0.3D ? 2.5D : 1.0D);
					double currentSpeedVertical = info.speedVert * (player.isInWater() ? 0.4D : 1.0D);
					
					double usage = player.isSprinting() ? info.usage * info.sprintFuel : info.usage;
					
					boolean creative = jetpack.getJetpackType().creative;
					
					IEnergyStorage energy = jetpack.getEnergyStorage(chest);
					if (!player.capabilities.isCreativeMode && !creative) {
						energy.extractEnergy((int) usage, false);
					}
					
					if (energy.getEnergyStored() > 0 || player.capabilities.isCreativeMode || creative) {
						if (InputHandler.isHoldingUp(player)) {
							if (!hover) {
								player.motionY = Math.min(player.motionY + currentAccel, currentSpeedVertical);
							} else {
								if (InputHandler.isHoldingDown(player)) {
									player.motionY = Math.min(player.motionY + currentAccel, -info.speedHoverSlow);
								} else {
									player.motionY = Math.min(player.motionY + currentAccel, info.speedHover);
								}
							}					
						} else {
							player.motionY = Math.min(player.motionY + currentAccel, -hoverSpeed);
						}
						
						float speedSideways = (float) (player.isSneaking() ? info.speedSide * 0.5F : info.speedSide);
						float speedForward = (float) (player.isSprinting() ? speedSideways * info.sprintSpeed : speedSideways);
						
						if (InputHandler.isHoldingForwards(player)) {
							player.moveRelative(0, 0, speedForward, speedForward);
						}
						
						if (InputHandler.isHoldingBackwards(player)) {
							player.moveRelative(0, 0, -speedSideways, speedSideways * 0.8F);
						}
						
						if (InputHandler.isHoldingLeft(player)) {
							player.moveRelative(speedSideways, 0, 0, speedSideways);
						}
						
						if (InputHandler.isHoldingRight(player)) {
							player.moveRelative(-speedSideways, 0, 0, speedSideways);
						}
						
						if (!world.isRemote) {
							player.fallDistance = 0.0F;
							
							if (player instanceof EntityPlayerMP) {
								((EntityPlayerMP) player).connection.floatingTickCount = 0;
							}
						}
					}
				}
			}
		}
	}
	
	@Override
	public boolean isEnchantable(ItemStack stack) {
		return ModConfig.confEnchantableJetpacks ? type.enchantablilty > 0 : false;
	}
	
	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return ModConfig.confEnchantableJetpacks;
	}
	
	@Override
	public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
		if (!source.isUnblockable()) {
			int energyPerDamage = info.usage;
			IEnergyStorage energy = getEnergyStorage(armor);
			int maxAbsorbed = energyPerDamage > 0 ? 25 * (energy.getEnergyStored() / energyPerDamage) : 0;
			if (energy.getEnergyStored() < energyPerDamage) {
				return new ArmorProperties(0, 0.65D * (type.armorPoints / 20.0D), Integer.MAX_VALUE);
			}
			return new ArmorProperties(0, 0.85D * (type.armorPoints / 20.0D), maxAbsorbed);
		}
		return new ArmorProperties(0, 0, 0);
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
		return 0;
	}

	@Override
	public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
		if (!type.creative) {
			getEnergyStorage(stack).extractEnergy(info.usage, false);
		}
	}
	
	@Override
	public int getColor(ItemStack stack) {
		return this.color();
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		IEnergyStorage energy = getEnergyStorage(stack);
		double stored = energy.getMaxEnergyStored() - energy.getEnergyStored();
		return (double) stored / energy.getMaxEnergyStored();
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return !type.creative;
	}
	
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
		if (!type.creative) {
			IEnergyStorage energy = getEnergyStorage(stack);
			tooltip.add(Utils.format(energy.getEnergyStored()) + " / " + Utils.format(energy.getMaxEnergyStored()) + " FE");
		} else {
			tooltip.add(Tooltips.INFINITE.get() + Colors.GRAY + " FE");
		}
		
		tooltip.add(Tooltips.ENGINE.get() + (isEngineOn(stack) ? Tooltips.ON.get(10) : Tooltips.OFF.get(12)));
		tooltip.add(Tooltips.HOVER.get() + (isHovering(stack) ? Tooltips.ON.get(10) : Tooltips.OFF.get(12)));
		tooltip.add(Tooltips.TIER.get() + (type.creative ? Tooltips.CREATIVE.get() : type.tier));
		
		if (ModConfig.confAdvancedInfo) {
			tooltip.add("");
			if (!Utils.isShiftKeyDown()) {
				tooltip.add(Tooltips.HOLD_SHIFT.get());
			} else {
				tooltip.add(Tooltips.FUEL_USAGE.get() + info.usage + " FE/t");
				tooltip.add(Tooltips.VERTICAL_SPEED.get() + info.speedVert);
				tooltip.add(Tooltips.VERTICAL_ACCELERATION.get() + info.accelVert);
				tooltip.add(Tooltips.HORIZONTAL_SPEED.get() + info.speedSide);
				tooltip.add(Tooltips.HOVER_SPEED.get() + info.speedHoverSlow);
				tooltip.add(Tooltips.DESCEND_SPEED.get() + info.speedHover);
				tooltip.add(Tooltips.SPRINT_MODIFIER.get() + info.sprintSpeed);
				tooltip.add(Tooltips.SPRINT_FUEL_MODIFIER.get() + info.sprintFuel);
			}
		}
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return this.type.creative ? EnumRarity.EPIC : EnumRarity.COMMON;
	}
	
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return new EnergyCapabilityProvider(new EnergyStorageItem(stack, info.capacity));
	}

	@Override
	public void initModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(IronJetpacks.MOD_ID + ":jetpack", "inventory"));
	}

	@Override
	public int color() {
		return type.color;
	}
}
