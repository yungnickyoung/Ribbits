package com.yungnickyoung.minecraft.ribbits.client.model;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.entity.FrostMinerEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class FrostMinerModel extends AnimatedGeoModel<FrostMinerEntity> {
    private static final ResourceLocation MODEL = new ResourceLocation(RibbitsCommon.MOD_ID, "geo/miner.geo.json");
    private static final ResourceLocation TEXTURE = new ResourceLocation(RibbitsCommon.MOD_ID, "textures/entity/miner.png");
    private static final ResourceLocation ANIMATIONS = new ResourceLocation(RibbitsCommon.MOD_ID, "animations/miner.animation.json");

    @Override
    public ResourceLocation getModelLocation(FrostMinerEntity frostMinerEntity) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureLocation(FrostMinerEntity frostMinerEntity) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(FrostMinerEntity frostMinerEntity) {
        return ANIMATIONS;
    }
}
