package com.yungnickyoung.minecraft.ribbits.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yungnickyoung.minecraft.ribbits.client.model.RibbitModel;
import com.yungnickyoung.minecraft.ribbits.data.RibbitData;
import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import com.yungnickyoung.minecraft.ribbits.module.DataTicketModule;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class RibbitRenderer<R extends LivingEntityRenderState & GeoRenderState> extends GeoEntityRenderer<RibbitEntity, R> {

    public RibbitRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new RibbitModel());
    }

    @Override
    public void addRenderData(RibbitEntity animatable, Void relatedObject, R renderState, float partialTick) {
        RibbitData data = animatable.getRibbitData();
        renderState.addGeckolibData(DataTicketModule.DT_RIBBIT_DATA, data);
        renderState.addGeckolibData(DataTicketModule.DT_PLAYING_INSTRUMENT, animatable.getPlayingInstrument());
        renderState.addGeckolibData(DataTicketModule.DT_UMBRELLA_FALLING, animatable.isUmbrellaFalling());
        renderState.addGeckolibData(DataTicketModule.DT_IN_RAIN, animatable.isInRain());
        renderState.addGeckolibData(DataTicketModule.DT_IS_PRIDE_RIBBIT, animatable.isPrideRibbit());
    }

    @Override
    public void renderBone(R renderState, PoseStack poseStack, GeoBone bone, VertexConsumer buffer,
                           CameraRenderState cameraState, int packedLight, int packedOverlay, int renderColor) {
        if ("instrument".equals(bone.getName())) {
            bone.setHidden(true);
        }
        super.renderBone(renderState, poseStack, bone, buffer, cameraState, packedLight, packedOverlay, renderColor);
    }

    @Override
    public RenderType getRenderType(R renderState, ResourceLocation texture) {
        return RenderType.entityCutoutNoCull(texture);
    }

    @Override
    public ResourceLocation getTextureLocation(R renderState) {
        return super.getTextureLocation(renderState);
    }

    @Override
    public float getMotionAnimThreshold(RibbitEntity animatable) {
        return 0.0005f;
    }
}
