package com.yungnickyoung.minecraft.ribbits.client.model;

import com.geckolib.model.GeoModel;
import com.geckolib.renderer.base.GeoRenderState;
import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import net.minecraft.resources.Identifier;

public class RibbitModel extends GeoModel<RibbitEntity> {
    private static final Identifier TEXTURE = RibbitsCommon.id("textures/entity/ribbit.png");
    private static final Identifier ANIMATIONS = RibbitsCommon.id("ribbit");

    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return RibbitsCommon.id("ribbit_master");
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        return TEXTURE;
    }

    @Override
    public Identifier getAnimationResource(RibbitEntity animatable) {
        return ANIMATIONS;
    }
}
