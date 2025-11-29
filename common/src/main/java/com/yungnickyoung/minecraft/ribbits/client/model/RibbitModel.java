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

import java.util.HashSet;
import java.util.Set;

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

        Set<String> desired = new HashSet<>();
        if (data.getProfession().equals(RibbitProfessionModule.MERCHANT)) {
            desired.add("body_merchant");
        } else {
            desired.add("body_default");
        }

        if (playingInstrument && data.getInstrument() != RibbitInstrumentModule.NONE) {
            String instrumentBone = instrumentBoneName(data.getInstrument());
            if (instrumentBone != null) desired.add(instrumentBone);
        } else {
            if (data.getProfession().equals(RibbitProfessionModule.GARDENER)) {
                desired.add("watering_can");
                desired.add("gardener_hat");
            } else if (data.getProfession().equals(RibbitProfessionModule.SORCERER)) {
                desired.add("sourcerer_hat");
            } else if (data.getProfession().equals(RibbitProfessionModule.FISHERMAN)) {
                desired.add("accessories");
                desired.add("fishing_rod");
                desired.add("fishing_rod_2");
                desired.add("fishing_rod_3");
            } else if (data.getProfession().equals(RibbitProfessionModule.MERCHANT)) {
                desired.add("leaf");
            }

            if (isPride) {
                desired.add("pride");
            } else if (umbrellaFalling || inRain) {
                String suffix = data.getUmbrellaType().modelLocationSuffix();
                if (suffix.contains("1")) {
                    desired.add("umbrella");
                } else if (suffix.contains("2")) {
                    desired.add("umbrella2");
                } else if (suffix.contains("3")) {
                    desired.add("umbrella3");
                }
            }
        }

        Set<String> currently = (Set<String>) state.getDataOrDefault(DataTicketModule.DT_VISIBLE_BONES, new HashSet<>());
        if (currently.isEmpty()) {
            getBone("body_default").ifPresent(b -> b.setHidden(true));
            getBone("body_merchant").ifPresent(b -> b.setHidden(true));
            String[] dynamicBones = new String[]{
                    "gardener_hat", "sourcerer_hat", "leaf", "watering_can",
                    "accessories", "fishing_rod", "fishing_rod_2", "fishing_rod_3",
                    "guitar", "flute", "bongo", "bass",
                    "umbrella", "umbrella2", "umbrella3",
                    "pride"
            };
            for (String b : dynamicBones) {
                getBone(b).ifPresent(bone -> bone.setHidden(true));
            }
        }

        for (String prev : new HashSet<>(currently)) {
            if (!desired.contains(prev)) {
                getBone(prev).ifPresent(bone -> bone.setHidden(true));
                currently.remove(prev);
            }
        }
        for (String name : desired) {
            if (!currently.contains(name)) {
                showBone(name);
                currently.add(name);
            }
        }
        state.setData(DataTicketModule.DT_VISIBLE_BONES, currently);
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
