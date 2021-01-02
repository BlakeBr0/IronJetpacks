package com.blakebr0.ironjetpacks.client.model;

import com.blakebr0.ironjetpacks.item.JetpackItem;
import com.blakebr0.ironjetpacks.util.JetpackUtils;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.IEnergyStorage;

/*
 * This is a slightly modified version of the model from Simply Jetpacks
 * https://github.com/Tomson124/SimplyJetpacks-2/blob/1.12/src/main/java/tonius/simplyjetpacks/client/model/ModelJetpack.java
 */
public class JetpackModel extends BipedModel<LivingEntity> {
	private final JetpackItem jetpack;
	private final ModelRenderer[] energyBarLeft = new ModelRenderer[6];
	private final ModelRenderer[] energyBarRight = new ModelRenderer[6];

	public JetpackModel(JetpackItem jetpack) {
		super(1.0F, 0, 64, 64);
		this.jetpack = jetpack;

		this.bipedBody.showModel = true;
		this.bipedRightArm.showModel = false;
		this.bipedLeftArm.showModel = false;
		this.bipedHead.showModel = false;
		this.bipedHeadwear.showModel = false;
		this.bipedRightLeg.showModel = false;
		this.bipedLeftLeg.showModel = false;

		ModelRenderer middle = new ModelRenderer(this, 0, 54);
		middle.addBox(-2F, 5F, 3.6F, 4, 3, 2);
		middle.setRotationPoint(0F, 0F, 0F);
		middle.mirror = true;
		this.setRotation(middle, 0F, 0F, 0F);

		ModelRenderer leftCanister = new ModelRenderer(this, 0, 32);
		leftCanister.addBox(0.5F, 2F, 2.6F, 4, 7, 4);
		leftCanister.setRotationPoint(0F, 0F, 0F);
		leftCanister.mirror = true;
		this.setRotation(leftCanister, 0F, 0F, 0F);

		ModelRenderer rightCanister = new ModelRenderer(this, 17, 32);
		rightCanister.addBox(-4.5F, 2F, 2.6F, 4, 7, 4);
		rightCanister.setRotationPoint(0F, 0F, 0F);
		rightCanister.mirror = true;
		this.setRotation(rightCanister, 0F, 0F, 0F);

		ModelRenderer leftTip1 = new ModelRenderer(this, 0, 45);
		leftTip1.addBox(1F, 0F, 3.1F, 3, 2, 3);
		leftTip1.setRotationPoint(0F, 0F, 0F);
		leftTip1.mirror = true;
		this.setRotation(leftTip1, 0F, 0F, 0F);

		ModelRenderer leftTip2 = new ModelRenderer(this, 0, 50);
		leftTip2.addBox(1.5F, -1F, 3.6F, 2, 1, 2);
		leftTip2.setRotationPoint(0F, 0F, 0F);
		leftTip2.mirror = true;
		this.setRotation(leftTip2, 0F, 0F, 0F);

		ModelRenderer rightTip1 = new ModelRenderer(this, 17, 45);
		rightTip1.addBox(-4F, 0F, 3.1F, 3, 2, 3);
		rightTip1.setRotationPoint(0F, 0F, 0F);
		rightTip1.mirror = true;
		this.setRotation(rightTip1, 0F, 0F, 0F);

		ModelRenderer rightTip2 = new ModelRenderer(this, 17, 50);
		rightTip2.addBox(-3.5F, -1F, 3.6F, 2, 1, 2);
		rightTip2.setRotationPoint(0F, 0F, 0F);
		rightTip2.mirror = true;
		this.setRotation(rightTip2, 0F, 0F, 0F);

		ModelRenderer leftExhaust1 = new ModelRenderer(this, 35, 32);
		leftExhaust1.addBox(1F, 9F, 3.1F, 3, 1, 3);
		leftExhaust1.setRotationPoint(0F, 0F, 0F);
		leftExhaust1.mirror = true;
		this.setRotation(leftExhaust1, 0F, 0F, 0F);

		ModelRenderer leftExhaust2 = new ModelRenderer(this, 35, 37);
		leftExhaust2.addBox(0.5F, 10F, 2.6F, 4, 3, 4);
		leftExhaust2.setRotationPoint(0F, 0F, 0F);
		leftExhaust2.mirror = true;
		this.setRotation(leftExhaust2, 0F, 0F, 0F);

		ModelRenderer rightExhaust1 = new ModelRenderer(this, 48, 32);
		rightExhaust1.addBox(-4F, 9F, 3.1F, 3, 1, 3);
		rightExhaust1.setRotationPoint(0F, 0F, 0F);
		rightExhaust1.mirror = true;
		this.setRotation(rightExhaust1, 0F, 0F, 0F);

		ModelRenderer rightExhaust2 = new ModelRenderer(this, 35, 45);
		rightExhaust2.addBox(-4.5F, 10F, 2.6F, 4, 3, 4);
		rightExhaust2.setRotationPoint(0F, 0F, 0F);
		rightExhaust2.mirror = true;
		this.setRotation(rightExhaust2, 0F, 0F, 0F);

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

		for (int i = 0; i < 6; i++) {
			ModelRenderer left = new ModelRenderer(this, 16 + (i * 4), 55);
			this.energyBarLeft[i] = left;
			left.addBox(2F, 3F, 5.8F, 1, 5, 1, 0F);
			left.showModel = false;
			this.setRotation(left, 0F, 0F, 0F);
			this.bipedBody.addChild(left);

			ModelRenderer right = new ModelRenderer(this, 16 + (i * 4), 55);
			this.energyBarRight[i] = right;
			right.addBox(-3F, 3F, 5.8F, 1, 5, 1, 0F);
			right.showModel = false;
			this.setRotation(right, 0F, 0F, 0F);
			this.bipedBody.addChild(right);
		}
	}

	@Override
	public void setRotationAngles(LivingEntity entity, float f1, float f2, float f3, float netHeadYaw, float headPitch) {
		super.setRotationAngles(entity, f1, f2, f3, netHeadYaw, headPitch);

		if (this.jetpack.getJetpack().creative) {
			this.resetEnergyBars();
			this.energyBarLeft[5].showModel = true;
			this.energyBarRight[5].showModel = true;
		} else {
			ItemStack chest = entity.getItemStackFromSlot(EquipmentSlotType.CHEST);
			IEnergyStorage energy = JetpackUtils.getEnergyStorage(chest);
			double stored = (double) energy.getEnergyStored() / (double) energy.getMaxEnergyStored();

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

			this.resetEnergyBars();
			this.energyBarLeft[state].showModel = true;
			this.energyBarRight[state].showModel = true;
		}
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	private void resetEnergyBars() {
		for (int i = 0; i < 6; i++) {
			this.energyBarLeft[i].showModel = false;
			this.energyBarRight[i].showModel = false;
		}
	}
}
