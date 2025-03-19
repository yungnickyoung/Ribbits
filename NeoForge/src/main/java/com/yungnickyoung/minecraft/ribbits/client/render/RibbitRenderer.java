package com.yungnickyoung.minecraft.ribbits.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yungnickyoung.minecraft.ribbits.client.model.RibbitModel;
import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RibbitRenderer extends GeoEntityRenderer<RibbitEntity> {

    public RibbitRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new RibbitModel());
    }

    @Override
    public void renderRecursively(PoseStack poseStack, RibbitEntity animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (bone.getName().equals("instrument")) {
            bone.setHidden(true);
        }

        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public RenderType getRenderType(RibbitEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutoutNoCull(texture);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull RibbitEntity entity) {
        return super.getTextureLocation(entity);
    }

    @Override
    public float getMotionAnimThreshold(RibbitEntity animatable) {
        return 0.0005f;
    }
}
