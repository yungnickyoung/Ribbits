package com.yungnickyoung.minecraft.ribbits.mixin.mixins.supporters;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yungnickyoung.minecraft.ribbits.client.supporters.SupportersListClient;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixinNeoForge<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {
    public HumanoidArmorLayerMixinNeoForge(RenderLayerParent<T, M> $$0) {
        super($$0);
    }

    @Inject(method = "renderArmorPiece(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/model/HumanoidModel;FFFFFF)V", at = @At("HEAD"), cancellable = true)
    private void ribbits$dontRenderHelmetWhenWearingSupporterHat(PoseStack poseStack, MultiBufferSource bufferSource,
                                                                 LivingEntity entity, EquipmentSlot slot, int p_117123_,
                                                                 HumanoidModel model, float limbSwing, float limbSwingAmount,
                                                                 float partialTick, float ageInTicks, float netHeadYaw,
                                                                 float headPitch, CallbackInfo ci) {
        if (!(entity instanceof AbstractClientPlayer clientPlayer)) {
            return;
        }

        if (slot == EquipmentSlot.HEAD && SupportersListClient.isPlayerSupporterHatEnabled(clientPlayer.getUUID())) {
            model.head.visible = false;
            model.hat.visible = false;
            ci.cancel();
        }
    }
}
