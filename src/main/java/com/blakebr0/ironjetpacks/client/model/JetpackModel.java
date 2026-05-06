package com.blakebr0.ironjetpacks.client.model;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;

public class JetpackModel extends HumanoidModel<HumanoidRenderState> {
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

	public JetpackModel(ModelPart part, int energyBarState) {
		super(part);

		for (int i = 0; i < 6; i++) {
			part.getChild("body").getChild(LEFT_ENERGY_BAR + "_" + i).visible = i == energyBarState;
			part.getChild("body").getChild(RIGHT_ENERGY_BAR + "_" + i).visible = i == energyBarState;
		}
	}

	public static LayerDefinition createArmorLayer() {
		var mesh = HumanoidModel.createMesh(new CubeDeformation(1.0F), 0F);
		var root = mesh.getRoot();
		var body = root.getChild("body");

		body.addOrReplaceChild(MIDDLE, CubeListBuilder.create().mirror()
				.texOffs(0, 54)
				.addBox(-2F, 5F, 3.6F, 4, 3, 2),
				PartPose.ZERO
		);

		body.addOrReplaceChild(LEFT_CANISTER, CubeListBuilder.create().mirror()
				.texOffs(0, 32)
				.addBox(0.5F, 2F, 2.6F, 4, 7, 4),
				PartPose.ZERO
		);

		body.addOrReplaceChild(RIGHT_CANISTER, CubeListBuilder.create().mirror()
				.texOffs(17, 32)
				.addBox(-4.5F, 2F, 2.6F, 4, 7, 4),
				PartPose.ZERO
		);

		body.addOrReplaceChild(LEFT_TIP_1, CubeListBuilder.create().mirror()
				.texOffs(0, 45)
				.addBox(1F, 0F, 3.1F, 3, 2, 3),
				PartPose.ZERO
		);

		body.addOrReplaceChild(LEFT_TIP_2, CubeListBuilder.create().mirror()
				.texOffs(0, 50)
				.addBox(1.5F, -1F, 3.6F, 2, 1, 2),
				PartPose.ZERO
		);

		body.addOrReplaceChild(RIGHT_TIP_1, CubeListBuilder.create().mirror()
				.texOffs(17, 45)
				.addBox(-4F, 0F, 3.1F, 3, 2, 3),
				PartPose.ZERO
		);

		body.addOrReplaceChild(RIGHT_TIP_2, CubeListBuilder.create().mirror()
				.texOffs(17, 50)
				.addBox(-3.5F, -1F, 3.6F, 2, 1, 2),
				PartPose.ZERO
		);

		body.addOrReplaceChild(LEFT_EXHAUST_1, CubeListBuilder.create().mirror()
				.texOffs(35, 32)
				.addBox(1F, 9F, 3.1F, 3, 1, 3),
				PartPose.ZERO
		);

		body.addOrReplaceChild(LEFT_EXHAUST_2, CubeListBuilder.create().mirror()
				.texOffs(35, 37)
				.addBox(0.5F, 10F, 2.6F, 4, 3, 4),
				PartPose.ZERO
		);

		body.addOrReplaceChild(RIGHT_EXHAUST_1, CubeListBuilder.create().mirror()
				.texOffs(48, 32)
				.addBox(-4F, 9F, 3.1F, 3, 1, 3),
				PartPose.ZERO
		);

		body.addOrReplaceChild(RIGHT_EXHAUST_2, CubeListBuilder.create().mirror()
				.texOffs(35, 45)
				.addBox(-4.5F, 10F, 2.6F, 4, 3, 4),
				PartPose.ZERO
		);

		for (int i = 0; i < 6; i++) {
			body.addOrReplaceChild(LEFT_ENERGY_BAR + "_" + i, CubeListBuilder.create()
					.texOffs(16 + (i * 4), 55)
					.addBox(2F, 3F, 5.8F, 1, 5, 1),
					PartPose.ZERO
			);

			body.addOrReplaceChild(RIGHT_ENERGY_BAR + "_" + i, CubeListBuilder.create()
					.texOffs(16 + (i * 4), 55)
					.addBox(-3F, 3F, 5.8F, 1, 5, 1),
					PartPose.ZERO
			);
		}

		return LayerDefinition.create(mesh, 64, 64);
	}
}
