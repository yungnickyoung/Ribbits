package com.yungnickyoung.minecraft.ribbits.client.model;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;

public class SupporterHatModel extends EntityModel<AvatarRenderState> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(RibbitsCommon.id("supporter_hat"), "main");

    public final ModelPart head;

    public SupporterHatModel(ModelPart root) {
        super(root);
        this.head = root.getChild("main");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(21, 11).addBox(-5.0F, -8.0F, 5.0F, 10.0F, 5.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-5.0F, -8.0F, -5.0F, 10.0F, 0.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(21, 17).addBox(-5.0F, -8.0F, -5.0F, 10.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 11).addBox(-5.0F, -8.0F, -5.0F, 0.0F, 5.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(0, 11).mirror().addBox(5.0F, -8.0F, -5.0F, 0.0F, 5.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(21, 21).addBox(1.0F, -10.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(21, 21).mirror().addBox(-5.0F, -10.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(21, 21).addBox(1.0F, -10.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 26.0F, 0.0F));

        PartDefinition cube_r1 = main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 27).mirror().addBox(0.0F, 0.0F, -2.0F, 1.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.9F, -6.0F, 0.0F, 0.0F, 0.0F, 0.0873F));
        PartDefinition cube_r2 = main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 27).addBox(-1.0F, 0.0F, -2.0F, 1.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.9F, -6.0F, 0.0F, 0.0F, 0.0F, -0.0873F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(AvatarRenderState state) {
    }
}
