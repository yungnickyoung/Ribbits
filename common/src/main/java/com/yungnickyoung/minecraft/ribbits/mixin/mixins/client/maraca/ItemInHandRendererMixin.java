package com.yungnickyoung.minecraft.ribbits.mixin.mixins.client.maraca;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yungnickyoung.minecraft.ribbits.client.render.MaracaInHandRenderer;
import com.yungnickyoung.minecraft.ribbits.module.ItemModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {
    @Shadow
    protected abstract void applyItemArmTransform(PoseStack $$0, HumanoidArm $$1, float $$2);

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    public abstract void renderItem(LivingEntity livingEntity, ItemStack itemStack, ItemDisplayContext itemDisplayContext, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i);

    @Inject(method = "renderArmWithItem",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V", shift = At.Shift.AFTER),
            cancellable = true)
    private void ribbits$renderArmWithItem(AbstractClientPlayer player, float partialTick, float interpPitch,
                                           InteractionHand hand, float swingProgress, ItemStack handItem,
                                           float equipProgress, PoseStack poseStack, SubmitNodeCollector submitNodeCollector,
                                           int packedLight, CallbackInfo ci) {
        if (!player.isScoping()) {
            boolean isMainHand = hand == InteractionHand.MAIN_HAND;
            HumanoidArm arm = isMainHand ? player.getMainArm() : player.getMainArm().getOpposite();
            boolean isRightArm = arm == HumanoidArm.RIGHT;
            if (handItem.is(ItemModule.MARACA.get())) {
                if (player.isUsingItem() && player.getUseItemRemainingTicks() > 0 && player.getUsedItemHand() == hand) {
                    this.applyMaracaTransform(poseStack, partialTick, arm, handItem, equipProgress);
                    this.renderItem(player, handItem, isRightArm ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND, poseStack, submitNodeCollector, packedLight);
                    poseStack.popPose();
                    ci.cancel();
                }
            }
        }
    }

    @Unique
    private void applyMaracaTransform(PoseStack poseStack, float partialTick, HumanoidArm arm, ItemStack handItem, float equipProgress) {
        this.applyItemArmTransform(poseStack, arm, equipProgress);
        MaracaInHandRenderer.applyMaracaTransform(poseStack, partialTick, arm, handItem, equipProgress);
    }
}
