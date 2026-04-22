package com.yungnickyoung.minecraft.ribbits.client.render;

import com.geckolib.renderer.GeoEntityRenderer;
import com.geckolib.renderer.base.BoneSnapshots;
import com.geckolib.renderer.base.GeoRenderState;
import com.geckolib.renderer.base.RenderPassInfo;
import com.yungnickyoung.minecraft.ribbits.client.model.RibbitModel;
import com.yungnickyoung.minecraft.ribbits.data.RibbitData;
import com.yungnickyoung.minecraft.ribbits.data.RibbitInstrument;
import com.yungnickyoung.minecraft.ribbits.data.RibbitProfession;
import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import com.yungnickyoung.minecraft.ribbits.module.DataTicketModule;
import com.yungnickyoung.minecraft.ribbits.module.RibbitInstrumentModule;
import com.yungnickyoung.minecraft.ribbits.module.RibbitProfessionModule;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RibbitRenderer<R extends LivingEntityRenderState & GeoRenderState> extends GeoEntityRenderer<RibbitEntity, R> {
    private static final Map<RibbitProfession, Set<String>> PROFESSION_BONES = Map.of(
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

    private static final Map<String, String> FISHERMAN_UMBRELLA_BONES = Map.of(
            "1", "fisherman_umbrella",
            "2", "fisherman_umbrella2",
            "3", "fisherman_umbrella3"
    );

    private static final Set<String> ALL_DYNAMIC_BONES = Set.of(
            "gardener_hat", "sourcerer_hat", "leaf", "watering_can",
            "accessories", "fishing_rod", "fishing_rod_2", "fishing_rod_3",
            "guitar", "flute", "bongo", "bass",
            "umbrella", "umbrella2", "umbrella3",
            "fisherman_umbrella", "fisherman_umbrella2", "fisherman_umbrella3",
            "pride"
    );

    public RibbitRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new RibbitModel());
    }

    @Override
    public void addRenderData(RibbitEntity animatable, Void relatedObject, R renderState, float partialTick) {
        RibbitData data = animatable.getRibbitData();
        renderState.addGeckolibData(DataTicketModule.DT_RIBBIT_DATA, data);
        renderState.addGeckolibData(DataTicketModule.DT_PLAYING_INSTRUMENT, animatable.getPlayingInstrument());
        renderState.addGeckolibData(DataTicketModule.DT_UMBRELLA_FALLING, animatable.isUmbrellaFalling());
        renderState.addGeckolibData(DataTicketModule.DT_IN_RAIN, animatable.isInRain());
        renderState.addGeckolibData(DataTicketModule.DT_IS_PRIDE_RIBBIT, animatable.isPrideRibbit());
    }

    @Override
    public void adjustModelBonesForRender(RenderPassInfo<R> renderPassInfo, BoneSnapshots snapshots) {
        RibbitData data = renderPassInfo.renderState().getGeckolibData(DataTicketModule.DT_RIBBIT_DATA);
        if (data == null) {
            return;
        }

        boolean playingInstrument = Boolean.TRUE.equals(renderPassInfo.renderState().getGeckolibData(DataTicketModule.DT_PLAYING_INSTRUMENT));
        boolean umbrellaFalling = Boolean.TRUE.equals(renderPassInfo.renderState().getGeckolibData(DataTicketModule.DT_UMBRELLA_FALLING));
        boolean inRain = Boolean.TRUE.equals(renderPassInfo.renderState().getGeckolibData(DataTicketModule.DT_IN_RAIN));
        boolean isPride = Boolean.TRUE.equals(renderPassInfo.renderState().getGeckolibData(DataTicketModule.DT_IS_PRIDE_RIBBIT));

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
                Map<String, String> currentUmbrellaMap =
                        RibbitProfessionModule.FISHERMAN.equals(data.getProfession())
                                ? FISHERMAN_UMBRELLA_BONES
                                : UMBRELLA_BONES;

                currentUmbrellaMap.forEach((k, v) -> {
                    if (suffix.contains(k)) desired.add(v);
                });
            }
        }

        hideBone(snapshots, "instrument");
        hideBone(snapshots, "body_default");
        hideBone(snapshots, "body_merchant");
        ALL_DYNAMIC_BONES.forEach(name -> hideBone(snapshots, name));

        for (String boneName : desired) {
            showBone(snapshots, boneName);
        }
    }

    private static void hideBone(BoneSnapshots snapshots, String boneName) {
        snapshots.ifPresent(boneName, snapshot -> snapshot.skipRender(true).skipChildrenRender(true));
    }

    private static void showBone(BoneSnapshots snapshots, String boneName) {
        snapshots.ifPresent(boneName, snapshot -> snapshot.skipRender(false).skipChildrenRender(false));
    }

    @Override
    public RenderType getRenderType(R renderState, Identifier texture) {
        return RenderTypes.entityCutout(texture);
    }

    @Override
    public Identifier getTextureLocation(R renderState) {
        return super.getTextureLocation(renderState);
    }

    @Override
    public float getMotionAnimThreshold(RibbitEntity animatable) {
        return 0.0005f;
    }
}
