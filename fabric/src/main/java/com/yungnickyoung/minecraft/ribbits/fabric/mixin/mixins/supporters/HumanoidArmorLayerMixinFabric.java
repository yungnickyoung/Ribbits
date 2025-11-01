package com.yungnickyoung.minecraft.ribbits.fabric.mixin.mixins.supporters;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yungnickyoung.minecraft.ribbits.client.supporters.SupportersListClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixinFabric<S extends HumanoidRenderState, M extends HumanoidModel<S>, A extends HumanoidModel<S>> extends RenderLayer<S, M> {
    public HumanoidArmorLayerMixinFabric(RenderLayerParent<S, M> parent) {
        super(parent);
    }

    @Inject(method = "renderArmorPiece", at = @At("HEAD"), cancellable = true)
    private void ribbits$dontRenderHelmetWhenWearingSupporterHat(PoseStack poseStack,
                                                                 SubmitNodeCollector submitNodeCollector,
                                                                 ItemStack itemStack,
                                                                 EquipmentSlot slot,
                                                                 int packedLight,
                                                                 S humanoidRenderState,
                                                                 CallbackInfo ci) {
        if (slot != EquipmentSlot.HEAD) return;

        if (!(humanoidRenderState instanceof AvatarRenderState state)) return;

        var level = Minecraft.getInstance().level;
        if (level == null) return;
        var entity = level.getEntity(state.id);
        if (!(entity instanceof AbstractClientPlayer clientPlayer)) return;

        if (SupportersListClient.isPlayerSupporterHatEnabled(clientPlayer.getUUID())) {
            ci.cancel();
        }
    }
}
