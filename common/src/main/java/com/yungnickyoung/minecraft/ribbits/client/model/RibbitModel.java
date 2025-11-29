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
import java.util.Map;
import java.util.Set;

public class RibbitModel extends GeoModel<RibbitEntity> {
    private static final ResourceLocation TEXTURE = RibbitsCommon.id("textures/entity/ribbit.png");
    private static final ResourceLocation ANIMATIONS = RibbitsCommon.id("ribbit");
    private static final Map<Object, Set<String>> PROFESSION_BONES = Map.of(
            RibbitProfessionModule.GARDENER, Set.of("watering_can", "gardener_hat"),
            RibbitProfessionModule.SORCERER, Set.of("sourcerer_hat"),
            RibbitProfessionModule.FISHERMAN, Set.of("accessories", "fishing_rod", "fishing_rod_2", "fishing_rod_3"),
            RibbitProfessionModule.MERCHANT, Set.of("leaf")
    );

    private static final Map<RibbitInstrument, String> INSTRUMENT_BONES = Map.of(
            RibbitInstrumentModule.BASS, "bass",
            RibbitInstrumentModule.BONGO, "bongo",
            RibbitInstrumentModule.FLUTE, "flute",
            RibbitInstrumentModule.GUITAR, "guitar"
    );

    private static final Map<String, String> UMBRELLA_BONES = Map.of(
            "1", "umbrella",
            "2", "umbrella2",
            "3", "umbrella3"
    );

    private static final Set<String> ALL_DYNAMIC_BONES = Set.of(
            "gardener_hat", "sourcerer_hat", "leaf", "watering_can",
            "accessories", "fishing_rod", "fishing_rod_2", "fishing_rod_3",
            "guitar", "flute", "bongo", "bass",
            "umbrella", "umbrella2", "umbrella3",
            "pride"
    );

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

        Set<String> desired = new HashSet<>(8);

        if (data.getProfession().equals(RibbitProfessionModule.MERCHANT)) {
            desired.add("body_merchant");
        } else {
            desired.add("body_default");
        }

        if (playingInstrument && data.getInstrument() != RibbitInstrumentModule.NONE) {
            String inst = INSTRUMENT_BONES.get(data.getInstrument());
            if (inst != null) desired.add(inst);
        } else {
            desired.addAll(PROFESSION_BONES.getOrDefault(data.getProfession(), Set.of()));

            if (isPride) {
                desired.add("pride");

            } else if (umbrellaFalling || inRain) {
                String suffix = data.getUmbrellaType().modelLocationSuffix();
                UMBRELLA_BONES.forEach((k, v) -> {
                    if (suffix.contains(k)) desired.add(v);
                });
            }
        }

        Set<String> currently = (Set<String>) state.getDataOrDefault(
                DataTicketModule.DT_VISIBLE_BONES,
                new HashSet<>()
        );

        if (currently.isEmpty()) {
            hideBone("body_default");
            hideBone("body_merchant");
            ALL_DYNAMIC_BONES.forEach(this::hideBone);
        }

        for (String prev : new HashSet<>(currently)) {
            if (!desired.contains(prev)) {
                hideBone(prev);
                currently.remove(prev);
            }
        }

        for (String need : desired) {
            if (!currently.contains(need)) {
                showBone(need);
                currently.add(need);
            }
        }

        state.setData(DataTicketModule.DT_VISIBLE_BONES, currently);
    }

    private void hideBone(String name) {
        getBone(name).ifPresent(b -> b.setHidden(true));
    }

    private void showBone(String name) {
        getBone(name).ifPresent(b -> b.setHidden(false));
    }
}
