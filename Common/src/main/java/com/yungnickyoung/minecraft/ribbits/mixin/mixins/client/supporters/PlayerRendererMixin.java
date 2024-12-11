package com.yungnickyoung.minecraft.ribbits.mixin.mixins.client.supporters;

import com.yungnickyoung.minecraft.ribbits.client.render.SupporterHatRenderLayer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public PlayerRendererMixin(EntityRendererProvider.Context ctx, PlayerModel<AbstractClientPlayer> entityModel, float f) {
        super(ctx, entityModel, f);
    }

    /*
     * Adds supporter hat renderer
     */
    @Inject(method = "<init>", at = @At("RETURN"))
    private void ribbits$addSupporterHatArmorLayer(EntityRendererProvider.Context context, boolean bl, CallbackInfo ci) {
        this.addLayer(new SupporterHatRenderLayer(this, context));
    }
}
