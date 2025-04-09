package com.yungnickyoung.minecraft.ribbits.mixin.mixins.client.maraca;

import com.yungnickyoung.minecraft.ribbits.client.render.MaracaInHandRenderer;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(ModelBakery.class)
public abstract class ModelBakeryMixin {
    @Shadow
    abstract UnbakedModel getModel(ResourceLocation $$0);

    @Shadow protected abstract void loadSpecialItemModelAndDependencies(ModelResourceLocation $$0);

    @Shadow @Final private Map<ModelResourceLocation, UnbakedModel> topLevelModels;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void ribbits$addMaracaModelToBakery(CallbackInfo ci) {
        this.loadSpecialItemModelAndDependencies(MaracaInHandRenderer.MARACA_IN_HAND_MODEL);
        UnbakedModel unbakedModel = this.topLevelModels.get(MaracaInHandRenderer.MARACA_IN_HAND_MODEL);
        unbakedModel.resolveParents(this::getModel);
    }
}
