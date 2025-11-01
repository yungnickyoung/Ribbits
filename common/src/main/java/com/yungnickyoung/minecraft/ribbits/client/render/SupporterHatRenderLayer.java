package com.yungnickyoung.minecraft.ribbits.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.client.model.SupporterHatModel;
import com.yungnickyoung.minecraft.ribbits.client.supporters.SupportersListClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class SupporterHatRenderLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
    private static final ResourceLocation TEXTURE = RibbitsCommon.id("textures/entity/player/supporter_hat.png");

    private SupporterHatModel hatModel;

    public SupporterHatRenderLayer(RenderLayerParent<AvatarRenderState, PlayerModel> renderLayerParent, Context context) {
        super(renderLayerParent);

        // For some reason, when other mods crash on startup on Forge due to missing dependencies,
        // Ribbits gets erroneously blamed, so we're just gonna silently catch that error.

        try {
            this.hatModel = new SupporterHatModel(context.bakeLayer(SupporterHatModel.LAYER_LOCATION));
        } catch (IllegalArgumentException e) {
            // No-op
        }
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLight, AvatarRenderState state, float f, float g) {
        if (this.hatModel == null) return;

        if (!shouldRenderHat(state)) return;

        poseStack.pushPose();
        hatModel.head.y = this.getParentModel().head.y - 0.6f + getRenderYOffset(state);
        this.getParentModel().head.translateAndRotate(poseStack);

        submitNodeCollector.submitModel(
                this.hatModel,
                state,
                poseStack,
                RenderType.entityCutoutNoCull(TEXTURE),
                packedLight,
                OverlayTexture.NO_OVERLAY,
                state.outlineColor,
                null
        );
        poseStack.popPose();
    }

    private boolean shouldRenderHat(AvatarRenderState state) {
        var level = Minecraft.getInstance().level;
        if (level == null) return false;
        var entity = level.getEntity(state.id);
        if (!(entity instanceof AbstractClientPlayer clientPlayer)) return false;
        return SupportersListClient.isPlayerSupporterHatEnabled(clientPlayer.getUUID());
    }

    private float getRenderYOffset(AvatarRenderState state) {
        return state.isCrouching ? -4.25F : 0;
    }
}