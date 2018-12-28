package com.blakebr0.ironjetpacks.item;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import com.blakebr0.cucumber.energy.EnergyCapabilityProvider;
import com.blakebr0.cucumber.energy.EnergyStorageItem;
import com.blakebr0.cucumber.helper.ResourceHelper;
import com.blakebr0.cucumber.iface.IColoredItem;
import com.blakebr0.cucumber.iface.IModelHelper;
import com.blakebr0.cucumber.lib.Colors;
import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.ironjetpacks.IronJetpacks;
import com.blakebr0.ironjetpacks.client.model.ModelJetpack;
import com.blakebr0.ironjetpacks.config.ModConfig;
import com.blakebr0.ironjetpacks.handler.InputHandler;
import com.blakebr0.ironjetpacks.lib.Tooltips;
import com.blakebr0.ironjetpacks.registry.Jetpack;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemJetpack extends ItemArmor implements ISpecialArmor, IModelHelper, IColoredItem {
		
	private Jetpack jetpack;
	private ModelBiped model;

	public ItemJetpack(Jetpack jetpack) {
		super(makeMaterial(jetpack), 2, EntityEquipmentSlot.CHEST);
		this.setUnlocalizedName("ij.jetpack");
		this.setCreativeTab(IronJetpacks.CREATIVE_TAB);
		this.setMaxDamage(0);
		this.jetpack = jetpack;
	}
	
	private static ArmorMaterial makeMaterial(Jetpack jetpack) {
		return EnumHelper.addArmorMaterial("IJ:" + jetpack.name.toUpperCase(Locale.ROOT), "ironjetpacks:jetpack", 0, new int[] { 0, 0, jetpack.armorPoints, 0 }, jetpack.enchantablilty, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0);
	}
	
	public Jetpack getJetpack() {
		return this.jetpack;
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
		String name = StringUtils.capitalize(this.jetpack.name.replace(" ", "_"));
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
					Jetpack info = jetpack.getJetpack();
					
					double hoverSpeed = InputHandler.isHoldingDown(player) ? info.speedHover : info.speedHoverSlow;
					double currentAccel = info.accelVert * (player.motionY < 0.3D ? 2.5D : 1.0D);
					double currentSpeedVertical = info.speedVert * (player.isInWater() ? 0.4D : 1.0D);
					
					double usage = player.isSprinting() ? info.usage * info.sprintFuel : info.usage;
					
					boolean creative = info.creative;
					
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
		return ModConfig.confEnchantableJetpacks && this.jetpack.enchantablilty > 0;
	}
	
	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return ModConfig.confEnchantableJetpacks && this.jetpack.enchantablilty > 0;
	}
	
	@Override
	public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
		if (!source.isUnblockable()) {
			int energyPerDamage = this.jetpack.usage;
			IEnergyStorage energy = getEnergyStorage(armor);
			int maxAbsorbed = energyPerDamage > 0 ? 25 * (energy.getEnergyStored() / energyPerDamage) : 0;
			if (energy.getEnergyStored() < energyPerDamage) {
				return new ArmorProperties(0, 0.65D * (this.jetpack.armorPoints / 20.0D), Integer.MAX_VALUE);
			}
			
			return new ArmorProperties(0, 0.85D * (this.jetpack.armorPoints / 20.0D), maxAbsorbed);
		}
		
		return new ArmorProperties(0, 0, 0);
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
		return 0;
	}

	@Override
	public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
		if (!this.jetpack.creative) {
			getEnergyStorage(stack).extractEnergy(this.jetpack.usage, false);
		}
	}
	
	@Override
	public boolean hasColor(ItemStack stack) {
		return true;
	}
	
	@Override
	public int getColor(ItemStack stack) {
		return this.color();
	}
	
	@Override
	public boolean hasOverlay(ItemStack stack) {
		return true;
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		IEnergyStorage energy = getEnergyStorage(stack);
		double stored = energy.getMaxEnergyStored() - energy.getEnergyStored();
		return (double) stored / energy.getMaxEnergyStored();
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return !this.jetpack.creative;
	}
	
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
		if (!this.jetpack.creative) {
			IEnergyStorage energy = getEnergyStorage(stack);
			tooltip.add(Utils.format(energy.getEnergyStored()) + " / " + Utils.format(energy.getMaxEnergyStored()) + " FE");
		} else {
			tooltip.add(Tooltips.INFINITE.get() + Colors.GRAY + " FE");
		}
		
		tooltip.add(this.jetpack.rarity.rarityColor.toString() + Tooltips.TIER.get(this.jetpack.creative ? "C" : this.jetpack.tier) + Colors.GRAY + " - " +
				Tooltips.ENGINE.get(isEngineOn(stack) ? 10 : 12) + Colors.GRAY + " - " +
				Tooltips.HOVER.get(isHovering(stack) ? 10 : 12));
		
		if (ModConfig.confAdvancedInfo) {
			tooltip.add("");
			if (!Utils.isShiftKeyDown()) {
				tooltip.add(Tooltips.HOLD_SHIFT.get());
			} else {
				tooltip.add(Tooltips.FUEL_USAGE.get(this.jetpack.usage + " FE/t"));
				tooltip.add(Tooltips.VERTICAL_SPEED.get(this.jetpack.speedVert));
				tooltip.add(Tooltips.VERTICAL_ACCELERATION.get(this.jetpack.accelVert));
				tooltip.add(Tooltips.HORIZONTAL_SPEED.get(this.jetpack.speedSide));
				tooltip.add(Tooltips.HOVER_SPEED.get(this.jetpack.speedHoverSlow));
				tooltip.add(Tooltips.DESCEND_SPEED.get(this.jetpack.speedHover));
				tooltip.add(Tooltips.SPRINT_MODIFIER.get(this.jetpack.sprintSpeed));
				tooltip.add(Tooltips.SPRINT_FUEL_MODIFIER.get(this.jetpack.sprintFuel));
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
		if (this.model == null) this.model = new ModelJetpack(this);		
		return this.model;
	}
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		return type != "overlay" ? IronJetpacks.MOD_ID + ":textures/armor/jetpack.png" : IronJetpacks.MOD_ID + ":textures/armor/jetpack_overlay.png";
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return this.jetpack.rarity;
	}
	
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return new EnergyCapabilityProvider(new EnergyStorageItem(stack, this.jetpack.capacity));
	}

	@Override
	public void initModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, ResourceHelper.getModelResource(IronJetpacks.MOD_ID, "jetpack", "inventory"));
	}

	@Override
	public int color() {
		return this.jetpack.color;
	}
}
