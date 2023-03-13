package com.yungnickyoung.minecraft.ribbits.client.model;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import java.util.List;

public class RibbitModel extends AnimatedGeoModel<RibbitEntity> {
    private static final List<ResourceLocation> MUSIC_MODELS = List.of(
            new ResourceLocation(RibbitsCommon.MOD_ID, "geo/bass_ribbit.geo.json"),
            new ResourceLocation(RibbitsCommon.MOD_ID, "geo/bongo_ribbit.geo.json"),
            new ResourceLocation(RibbitsCommon.MOD_ID, "geo/flute_ribbit.geo.json"),
            new ResourceLocation(RibbitsCommon.MOD_ID, "geo/guitar_ribbit.geo.json")
    );

    private static final List<ResourceLocation> PROFESSION_MODELS = List.of(
            new ResourceLocation(RibbitsCommon.MOD_ID, "geo/gardener_ribbit.geo.json"),
            new ResourceLocation(RibbitsCommon.MOD_ID, "geo/sorcerer_ribbit.geo.json")
    );

    private static final List<ResourceLocation> UMBRELLA_MODELS = List.of(
            new ResourceLocation(RibbitsCommon.MOD_ID, "geo/umbrella_ribbit_1.geo.json"),
            new ResourceLocation(RibbitsCommon.MOD_ID, "geo/umbrella_ribbit_2.geo.json"),
            new ResourceLocation(RibbitsCommon.MOD_ID, "geo/umbrella_ribbit_3.geo.json")
    );

    private static final ResourceLocation TEXTURE = new ResourceLocation(RibbitsCommon.MOD_ID, "textures/entity/ribbit.png");
    private static final ResourceLocation ANIMATIONS = new ResourceLocation(RibbitsCommon.MOD_ID, "animations/ribbit.animation.json");


    @Override
    public ResourceLocation getModelLocation(RibbitEntity object) {
        if (object.getRibbitData().getProfession() == 0) {
            return MUSIC_MODELS.get(object.getRibbitData().getInstrument());
        } else {
            // This next line is (-1) since the nitwit is 0, and uses the music models from above.
            return PROFESSION_MODELS.get(object.getRibbitData().getProfession() - 1);
        }
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
