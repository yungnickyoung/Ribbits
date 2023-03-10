package com.yungnickyoung.minecraft.ribbits.client.model;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class RibbitModel extends AnimatedGeoModel<RibbitEntity> {
    private static final ResourceLocation MODEL = new ResourceLocation(RibbitsCommon.MOD_ID, "geo/bass_ribbit.geo.json");
    private static final ResourceLocation TEXTURE = new ResourceLocation(RibbitsCommon.MOD_ID, "textures/entity/ribbit.png");
    private static final ResourceLocation ANIMATIONS = new ResourceLocation(RibbitsCommon.MOD_ID, "animations/ribbit.animation.json");


    @Override
    public ResourceLocation getModelLocation(RibbitEntity object) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureLocation(RibbitEntity object) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(RibbitEntity animatable) {
        return ANIMATIONS;
    }
}
