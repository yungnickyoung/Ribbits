package com.yungnickyoung.minecraft.ribbits.mixin.mixins.client.supporters;

import com.yungnickyoung.minecraft.ribbits.client.render.SupporterHatRenderLayer;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public abstract class AvatarRendererMixin<AvatarlikeEntity extends Avatar & ClientAvatarEntity> extends LivingEntityRenderer<AvatarlikeEntity, AvatarRenderState, PlayerModel> {

    public AvatarRendererMixin(EntityRendererProvider.Context context, PlayerModel entityModel, float f) {
        super(context, entityModel, f);
    }

    /*
     * Adds supporter hat renderer
     */
    @Inject(method = "<init>", at = @At("RETURN"))
    private void ribbits$addSupporterHatArmorLayer(EntityRendererProvider.Context context, boolean bl, CallbackInfo ci) {
        this.addLayer(new SupporterHatRenderLayer(this, context));
    }
}
