package com.yungnickyoung.minecraft.ribbits.client.model;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.data.RibbitData;
import com.yungnickyoung.minecraft.ribbits.data.RibbitInstrument;
import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import com.yungnickyoung.minecraft.ribbits.module.DataTicketModule;
import com.yungnickyoung.minecraft.ribbits.module.RibbitInstrumentModule;
import com.yungnickyoung.minecraft.ribbits.module.RibbitProfessionModule;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.processing.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class RibbitModel extends GeoModel<RibbitEntity> {
    private static final ResourceLocation TEXTURE = RibbitsCommon.id("textures/entity/ribbit.png");
    private static final ResourceLocation ANIMATIONS = RibbitsCommon.id("ribbit");

    @Override
    public ResourceLocation getModelResource(GeoRenderState renderState) {
        return RibbitsCommon.id("ribbit_master");
    }

    @Override
    public ResourceLocation getTextureResource(GeoRenderState renderState) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(RibbitEntity animatable) {
        return ANIMATIONS;
    }

    @Override
    public void setCustomAnimations(AnimationState<RibbitEntity> state) {
        RibbitData data = state.getData(DataTicketModule.DT_RIBBIT_DATA);
        boolean playingInstrument = Boolean.TRUE.equals(state.getData(DataTicketModule.DT_PLAYING_INSTRUMENT));
        boolean umbrellaFalling = Boolean.TRUE.equals(state.getData(DataTicketModule.DT_UMBRELLA_FALLING));
        boolean inRain = Boolean.TRUE.equals(state.getData(DataTicketModule.DT_IN_RAIN));
        boolean isPride = Boolean.TRUE.equals(state.getData(DataTicketModule.DT_IS_PRIDE_RIBBIT));

        getBone("body_default").ifPresent(b -> b.setHidden(true));
        getBone("body_merchant").ifPresent(b -> b.setHidden(true));
        if (data.getProfession().equals(RibbitProfessionModule.MERCHANT)) {
            showBone("body_merchant");
        } else {
            showBone("body_default");
        }

        String[] dynamicBones = new String[] {
                "gardener_hat", "sourcerer_hat", "leaf", "watering_can",
                "accessories", "fishing_rod", "fishing_rod_2", "fishing_rod_3",
                "guitar", "flute", "bongo", "bass",
                "umbrella", "umbrella2", "umbrella3",
                "pride"
        };
        for (String b : dynamicBones) {
            getBone(b).ifPresent(bone -> bone.setHidden(true));
        }

        if (playingInstrument && data.getInstrument() != RibbitInstrumentModule.NONE) {
            String instrumentBone = instrumentBoneName(data.getInstrument());
            if (instrumentBone != null) {
                showBone(instrumentBone);
            }
            return;
        }

        if (data.getProfession().equals(RibbitProfessionModule.GARDENER)) {
            showBone("watering_can");
            showBone("gardener_hat");
        } else if (data.getProfession().equals(RibbitProfessionModule.SORCERER)) {
            showBone("sourcerer_hat");
        } else if (data.getProfession().equals(RibbitProfessionModule.FISHERMAN)) {
            showBone("accessories");
            showBone("fishing_rod");
            showBone("fishing_rod_2");
            showBone("fishing_rod_3");
        } else if (data.getProfession().equals(RibbitProfessionModule.MERCHANT)) {
            showBone("leaf");
        }

        if (isPride) {
            showBone("pride");
        } else if (umbrellaFalling || inRain) {
            String suffix = data.getUmbrellaType().modelLocationSuffix();
            if (suffix.contains("1")) {
                showBone("umbrella");
            } else if (suffix.contains("2")) {
                showBone("umbrella2");
            } else if (suffix.contains("3")) {
                showBone("umbrella3");
            }
        }
    }

    private void showBone(String name) {
        getBone(name).ifPresent(bone -> bone.setHidden(false));
    }

    private String instrumentBoneName(RibbitInstrument instrument) {
        if (instrument == RibbitInstrumentModule.BASS) return "bass";
        if (instrument == RibbitInstrumentModule.BONGO) return "bongo";
        if (instrument == RibbitInstrumentModule.FLUTE) return "flute";
        if (instrument == RibbitInstrumentModule.GUITAR) return "guitar";
        return null;
    }
}
