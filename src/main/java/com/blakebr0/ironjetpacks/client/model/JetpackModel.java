package com.blakebr0.ironjetpacks.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.world.entity.LivingEntity;

public class JetpackModel extends HumanoidModel<LivingEntity> {
	private static final String MIDDLE = "middle";
	private static final String LEFT_CANISTER = "left_canister";
	private static final String RIGHT_CANISTER = "right_canister";
	private static final String LEFT_TIP_1 = "left_tip_1";
	private static final String LEFT_TIP_2 = "left_tip_2";
	private static final String RIGHT_TIP_1 = "right_tip_1";
	private static final String RIGHT_TIP_2 = "right_tip_2";
	private static final String LEFT_EXHAUST_1 = "left_exhaust_1";
	private static final String LEFT_EXHAUST_2 = "left_exhaust_2";
	private static final String RIGHT_EXHAUST_1 = "right_exhaust_1";
	private static final String RIGHT_EXHAUST_2 = "right_exhaust_2";
	private static final String LEFT_ENERGY_BAR = "left_energy_bar";
	private static final String RIGHT_ENERGY_BAR = "right_energy_bar";

	private final ModelPart middle;
	private final ModelPart leftCanister;
	private final ModelPart rightCanister;
	private final ModelPart leftTip1;
	private final ModelPart leftTip2;
	private final ModelPart rightTip1;
	private final ModelPart rightTip2;
	private final ModelPart leftExhaust1;
	private final ModelPart leftExhaust2;
	private final ModelPart rightExhaust1;
	private final ModelPart rightExhaust2;
	private final ModelPart[] energyBarLeft = new ModelPart[6];
	private final ModelPart[] energyBarRight = new ModelPart[6];

	public JetpackModel(ModelPart part, int energyBarState) {
		super(part);
		this.middle = part.getChild(MIDDLE);
		this.leftCanister = part.getChild(LEFT_CANISTER);
		this.rightCanister = part.getChild(RIGHT_CANISTER);
		this.leftTip1 = part.getChild(LEFT_TIP_1);
		this.leftTip2 = part.getChild(LEFT_TIP_2);
		this.rightTip1 = part.getChild(RIGHT_TIP_1);
		this.rightTip2 = part.getChild(RIGHT_TIP_2);
		this.leftExhaust1 = part.getChild(LEFT_EXHAUST_1);
		this.leftExhaust2 = part.getChild(LEFT_EXHAUST_2);
		this.rightExhaust1 = part.getChild(RIGHT_EXHAUST_1);
		this.rightExhaust2 = part.getChild(RIGHT_EXHAUST_2);

		for (int i = 0; i < 6; i++) {
			this.energyBarLeft[i] = part.getChild(LEFT_ENERGY_BAR + "_" + i);
			this.energyBarRight[i] = part.getChild(RIGHT_ENERGY_BAR + "_" + i);

			this.energyBarLeft[i].visible = i == energyBarState;
			this.energyBarRight[i].visible = i == energyBarState;
		}
	}

	@Override
	protected Iterable<ModelPart> headParts() {
		return ImmutableList.of();
	}

	@Override
	protected Iterable<ModelPart> bodyParts() {
		this.middle.copyFrom(this.body);
		this.leftCanister.copyFrom(this.middle);
		this.rightCanister.copyFrom(this.middle);
		this.leftTip1.copyFrom(this.middle);
		this.leftTip2.copyFrom(this.middle);
		this.rightTip1.copyFrom(this.middle);
		this.rightTip2.copyFrom(this.middle);
		this.leftExhaust1.copyFrom(this.middle);
		this.leftExhaust2.copyFrom(this.middle);
		this.rightExhaust1.copyFrom(this.middle);
		this.rightExhaust2.copyFrom(this.middle);

		for (int i = 0; i < 6; i++) {
			this.energyBarLeft[i].copyFrom(this.middle);
			this.energyBarRight[i].copyFrom(this.middle);
		}

		ImmutableList.Builder<ModelPart> parts = ImmutableList.builder();

		parts.add(
				this.body,
				this.middle,
				this.leftCanister,
				this.rightCanister,
				this.leftTip1,
				this.leftTip2,
				this.rightTip1,
				this.rightTip2,
				this.leftExhaust1,
				this.leftExhaust2,
				this.rightExhaust1,
				this.rightExhaust2,
				this.leftArm,
				this.rightArm
		);

		for (int i = 0; i < 6; i++) {
			parts.add(this.energyBarLeft[i]);
			parts.add(this.energyBarRight[i]);
		}

		return parts.build();
	}

	public static LayerDefinition createBodyLayer() {
		var mesh = HumanoidModel.createMesh(new CubeDeformation(1.0F), 0F);
		var root = mesh.getRoot();

		root.addOrReplaceChild(MIDDLE, CubeListBuilder.create().mirror()
				.texOffs(0, 54)
				.addBox(-2F, 5F, 3.6F, 4, 3, 2),
				PartPose.ZERO
		);

		root.addOrReplaceChild(LEFT_CANISTER, CubeListBuilder.create().mirror()
				.texOffs(0, 32)
				.addBox(0.5F, 2F, 2.6F, 4, 7, 4),
				PartPose.ZERO
		);

		root.addOrReplaceChild(RIGHT_CANISTER, CubeListBuilder.create().mirror()
				.texOffs(17, 32)
				.addBox(-4.5F, 2F, 2.6F, 4, 7, 4),
				PartPose.ZERO
		);

		root.addOrReplaceChild(LEFT_TIP_1, CubeListBuilder.create().mirror()
				.texOffs(0, 45)
				.addBox(1F, 0F, 3.1F, 3, 2, 3),
				PartPose.ZERO
		);

		root.addOrReplaceChild(LEFT_TIP_2, CubeListBuilder.create().mirror()
				.texOffs(0, 50)
				.addBox(1.5F, -1F, 3.6F, 2, 1, 2),
				PartPose.ZERO
		);

		root.addOrReplaceChild(RIGHT_TIP_1, CubeListBuilder.create().mirror()
				.texOffs(17, 45)
				.addBox(-4F, 0F, 3.1F, 3, 2, 3),
				PartPose.ZERO
		);

		root.addOrReplaceChild(RIGHT_TIP_2, CubeListBuilder.create().mirror()
				.texOffs(17, 50)
				.addBox(-3.5F, -1F, 3.6F, 2, 1, 2),
				PartPose.ZERO
		);

		root.addOrReplaceChild(LEFT_EXHAUST_1, CubeListBuilder.create().mirror()
				.texOffs(35, 32)
				.addBox(1F, 9F, 3.1F, 3, 1, 3),
				PartPose.ZERO
		);

		root.addOrReplaceChild(LEFT_EXHAUST_2, CubeListBuilder.create().mirror()
				.texOffs(35, 37)
				.addBox(0.5F, 10F, 2.6F, 4, 3, 4),
				PartPose.ZERO
		);

		root.addOrReplaceChild(RIGHT_EXHAUST_1, CubeListBuilder.create().mirror()
				.texOffs(48, 32)
				.addBox(-4F, 9F, 3.1F, 3, 1, 3),
				PartPose.ZERO
		);

		root.addOrReplaceChild(RIGHT_EXHAUST_2, CubeListBuilder.create().mirror()
				.texOffs(35, 45)
				.addBox(-4.5F, 10F, 2.6F, 4, 3, 4),
				PartPose.ZERO
		);

		for (int i = 0; i < 6; i++) {
			root.addOrReplaceChild(LEFT_ENERGY_BAR + "_" + i, CubeListBuilder.create()
					.texOffs(16 + (i * 4), 55)
					.addBox(2F, 3F, 5.8F, 1, 5, 1),
					PartPose.ZERO
			);

			root.addOrReplaceChild(RIGHT_ENERGY_BAR + "_" + i, CubeListBuilder.create()
					.texOffs(16 + (i * 4), 55)
					.addBox(-3F, 3F, 5.8F, 1, 5, 1),
					PartPose.ZERO
			);
		}

		return LayerDefinition.create(mesh, 64, 64);
	}
}
