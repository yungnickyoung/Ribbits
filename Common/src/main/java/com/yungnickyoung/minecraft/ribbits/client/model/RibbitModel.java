package com.yungnickyoung.minecraft.ribbits.client.model;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import com.yungnickyoung.minecraft.ribbits.module.RibbitInstrumentModule;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RibbitModel extends GeoModel<RibbitEntity> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(RibbitsCommon.MOD_ID, "textures/entity/ribbit.png");
    private static final ResourceLocation ANIMATIONS = new ResourceLocation(RibbitsCommon.MOD_ID, "animations/ribbit.animation.json");

    private static final ResourceLocation PRIDE_MODEL = new ResourceLocation(RibbitsCommon.MOD_ID, "geo/pride_ribbit.geo.json");

    @Override
    public ResourceLocation getModelResource(RibbitEntity ribbitEntity) {
        if (ribbitEntity.getPlayingInstrument() && ribbitEntity.getRibbitData().getInstrument() != RibbitInstrumentModule.NONE) {
            return ribbitEntity.getRibbitData().getInstrument().getModelId();
        } else if (ribbitEntity.isUmbrellaFalling() || ribbitEntity.isInRain()) {
            return RibbitsCommon.id("geo/umbrella/" + ribbitEntity.getRibbitData().getProfession().getId().getPath() + "/" + ribbitEntity.getRibbitData().getUmbrellaType().getModelLocationSuffix());
        } else {
            if (ribbitEntity.isPrideRibbit()) {
                return PRIDE_MODEL;
            }
            
            return ribbitEntity.getRibbitData().getProfession().getModelLocation();
        }
    }

    @Override
    public ResourceLocation getTextureResource(RibbitEntity object) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(RibbitEntity animatable) {
        return ANIMATIONS;
    }
}
