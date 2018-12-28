package com.blakebr0.ironjetpacks.client.model;

import com.blakebr0.ironjetpacks.item.ItemJetpack;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.IEnergyStorage;

/*
 * This is a slightly modified version of the model from Simply Jetpacks
 * https://github.com/Tomson124/SimplyJetpacks-2/blob/1.12/src/main/java/tonius/simplyjetpacks/client/model/ModelJetpack.java
 */
public class ModelJetpack extends ModelBiped {
	
	public static final ModelBox[] ENERGY_STATES_1 = new ModelBox[6];
	public static final ModelBox[] ENERGY_STATES_2 = new ModelBox[6];
	
	private ItemJetpack jetpack;
	private ModelRenderer energyBar1, energyBar2;

	public ModelJetpack(ItemJetpack jetpack) {
		super(1.0F, 0, 64, 64);
		
		this.jetpack = jetpack;

		this.bipedBody.showModel = true;
		this.bipedRightArm.showModel = false;
		this.bipedLeftArm.showModel = false;
		this.bipedHead.showModel = false;
		this.bipedHeadwear.showModel = false;
		this.bipedRightLeg.showModel = false;
		this.bipedLeftLeg.showModel = false;
				
		ENERGY_STATES_1[0] = new ModelBox(new ModelRenderer(this).setTextureSize(64, 64), 16, 55, 2F, 3F, 5.8F, 1, 5, 1, 0F, true);
		ENERGY_STATES_1[1] = new ModelBox(new ModelRenderer(this).setTextureSize(64, 64), 20, 55, 2F, 3F, 5.8F, 1, 5, 1, 0F, true);
		ENERGY_STATES_1[2] = new ModelBox(new ModelRenderer(this).setTextureSize(64, 64), 24, 55, 2F, 3F, 5.8F, 1, 5, 1, 0F, true);
		ENERGY_STATES_1[3] = new ModelBox(new ModelRenderer(this).setTextureSize(64, 64), 28, 55, 2F, 3F, 5.8F, 1, 5, 1, 0F, true);
		ENERGY_STATES_1[4] = new ModelBox(new ModelRenderer(this).setTextureSize(64, 64), 32, 55, 2F, 3F, 5.8F, 1, 5, 1, 0F, true);
		ENERGY_STATES_1[5] = new ModelBox(new ModelRenderer(this).setTextureSize(64, 64), 36, 55, 2F, 3F, 5.8F, 1, 5, 1, 0F, true);
		
		ENERGY_STATES_2[0] = new ModelBox(new ModelRenderer(this).setTextureSize(64, 64), 16, 55, -3F, 3F, 5.8F, 1, 5, 1, 0F, true);
		ENERGY_STATES_2[1] = new ModelBox(new ModelRenderer(this).setTextureSize(64, 64), 20, 55, -3F, 3F, 5.8F, 1, 5, 1, 0F, true);
		ENERGY_STATES_2[2] = new ModelBox(new ModelRenderer(this).setTextureSize(64, 64), 24, 55, -3F, 3F, 5.8F, 1, 5, 1, 0F, true);
		ENERGY_STATES_2[3] = new ModelBox(new ModelRenderer(this).setTextureSize(64, 64), 28, 55, -3F, 3F, 5.8F, 1, 5, 1, 0F, true);
		ENERGY_STATES_2[4] = new ModelBox(new ModelRenderer(this).setTextureSize(64, 64), 32, 55, -3F, 3F, 5.8F, 1, 5, 1, 0F, true);
		ENERGY_STATES_2[5] = new ModelBox(new ModelRenderer(this).setTextureSize(64, 64), 36, 55, -3F, 3F, 5.8F, 1, 5, 1, 0F, true);


		ModelRenderer middle = new ModelRenderer(this, 0, 54).setTextureSize(64, 64);
		middle.addBox(-2F, 5F, 3.6F, 4, 3, 2);
		middle.setRotationPoint(0F, 0F, 0F);
		middle.mirror = true;
		this.setRotation(middle, 0F, 0F, 0F);

		ModelRenderer leftCanister = new ModelRenderer(this, 0, 32).setTextureSize(64, 64);
		leftCanister.addBox(0.5F, 2F, 2.6F, 4, 7, 4);
		leftCanister.setRotationPoint(0F, 0F, 0F);
		leftCanister.mirror = true;
		this.setRotation(leftCanister, 0F, 0F, 0F);

		ModelRenderer rightCanister = new ModelRenderer(this, 17, 32).setTextureSize(64, 64);
		rightCanister.addBox(-4.5F, 2F, 2.6F, 4, 7, 4);
		rightCanister.setRotationPoint(0F, 0F, 0F);
		rightCanister.mirror = true;
		this.setRotation(rightCanister, 0F, 0F, 0F);

		ModelRenderer leftTip1 = new ModelRenderer(this, 0, 45).setTextureSize(64, 64);
		leftTip1.addBox(1F, 0F, 3.1F, 3, 2, 3);
		leftTip1.setRotationPoint(0F, 0F, 0F);
		leftTip1.mirror = true;
		this.setRotation(leftTip1, 0F, 0F, 0F);

		ModelRenderer leftTip2 = new ModelRenderer(this, 0, 50).setTextureSize(64, 64);
		leftTip2.addBox(1.5F, -1F, 3.6F, 2, 1, 2);
		leftTip2.setRotationPoint(0F, 0F, 0F);
		leftTip2.mirror = true;
		this.setRotation(leftTip2, 0F, 0F, 0F);

		ModelRenderer rightTip1 = new ModelRenderer(this, 17, 45).setTextureSize(64, 64);
		rightTip1.addBox(-4F, 0F, 3.1F, 3, 2, 3);
		rightTip1.setRotationPoint(0F, 0F, 0F);
		rightTip1.mirror = true;
		this.setRotation(rightTip1, 0F, 0F, 0F);

		ModelRenderer rightTip2 = new ModelRenderer(this, 17, 50).setTextureSize(64, 64);
		rightTip2.addBox(-3.5F, -1F, 3.6F, 2, 1, 2);
		rightTip2.setRotationPoint(0F, 0F, 0F);
		rightTip2.mirror = true;
		this.setRotation(rightTip2, 0F, 0F, 0F);

		ModelRenderer leftExhaust1 = new ModelRenderer(this, 35, 32).setTextureSize(64, 64);
		leftExhaust1.addBox(1F, 9F, 3.1F, 3, 1, 3);
		leftExhaust1.setRotationPoint(0F, 0F, 0F);
		leftExhaust1.mirror = true;
		this.setRotation(leftExhaust1, 0F, 0F, 0F);

		ModelRenderer leftExhaust2 = new ModelRenderer(this, 35, 37).setTextureSize(64, 64);
		leftExhaust2.addBox(0.5F, 10F, 2.6F, 4, 3, 4);
		leftExhaust2.setRotationPoint(0F, 0F, 0F);
		leftExhaust2.mirror = true;
		this.setRotation(leftExhaust2, 0F, 0F, 0F);

		ModelRenderer rightExhaust1 = new ModelRenderer(this, 48, 32).setTextureSize(64, 64);
		rightExhaust1.addBox(-4F, 9F, 3.1F, 3, 1, 3);
		rightExhaust1.setRotationPoint(0F, 0F, 0F);
		rightExhaust1.mirror = true;
		this.setRotation(rightExhaust1, 0F, 0F, 0F);

		ModelRenderer rightExhaust2 = new ModelRenderer(this, 35, 45).setTextureSize(64, 64);
		rightExhaust2.addBox(-4.5F, 10F, 2.6F, 4, 3, 4);
		rightExhaust2.setRotationPoint(0F, 0F, 0F);
		rightExhaust2.mirror = true;
		this.setRotation(rightExhaust2, 0F, 0F, 0F);
		
		this.energyBar1 = new ModelRenderer(this, 16, 55).setTextureSize(64, 64);
		this.energyBar1.cubeList.add(ENERGY_STATES_1[0]);
		this.energyBar1.setRotationPoint(0F, 0F, 0F);
		this.energyBar1.mirror = true;
		this.setRotation(energyBar1, 0F, 0F, 0F);
		
		this.energyBar2 = new ModelRenderer(this, 16, 55).setTextureSize(64, 64);
		this.energyBar2.cubeList.add(ENERGY_STATES_2[0]);
		this.energyBar2.setRotationPoint(0F, 0F, 0F);
		this.energyBar2.mirror = true;
		this.setRotation(energyBar2, 0F, 0F, 0F);

		this.bipedBody.addChild(middle);
		this.bipedBody.addChild(leftCanister);
		this.bipedBody.addChild(rightCanister);
		this.bipedBody.addChild(leftTip1);
		this.bipedBody.addChild(leftTip2);
		this.bipedBody.addChild(rightTip1);
		this.bipedBody.addChild(rightTip2);
		this.bipedBody.addChild(leftExhaust1);
		this.bipedBody.addChild(leftExhaust2);
		this.bipedBody.addChild(rightExhaust1);
		this.bipedBody.addChild(rightExhaust2);
		this.bipedBody.addChild(this.energyBar1);
		this.bipedBody.addChild(this.energyBar2);
	}
	
	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (this.jetpack != null && entity instanceof EntityLivingBase) {
			ModelBox oldBox1 = this.energyBar1.cubeList.get(0);
			
			if (this.jetpack.getJetpack().creative) {
				this.energyBar1.cubeList.set(0, ENERGY_STATES_1[5]);
				this.energyBar2.cubeList.set(0, ENERGY_STATES_2[5]);
			} else {
				ItemStack chest = ((EntityLivingBase) entity).getItemStackFromSlot(EntityEquipmentSlot.CHEST);
				IEnergyStorage energy = this.jetpack.getEnergyStorage(chest);
				double stored = (double) ((double) energy.getEnergyStored() / (double) energy.getMaxEnergyStored());

				int state = 0;
				if (stored > 0.8) {
					state = 5;
				} else if (stored > 0.6) {
					state = 4;
				} else if (stored > 0.4) {
					state = 3;
				} else if (stored > 0.2) {
					state = 2;
				} else if (stored > 0) {
					state = 1;
				}
				
				if (oldBox1 != ENERGY_STATES_1[state]) {					
					this.energyBar1.cubeList.set(0, ENERGY_STATES_1[state]);
					this.energyBar2.cubeList.set(0, ENERGY_STATES_2[state]);
					this.energyBar1.compiled = false;
					this.energyBar2.compiled = false;
				}
			}
			
			super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		} else {
			super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		}
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
