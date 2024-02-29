package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegister;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;

@AutoRegister(RibbitsCommon.MOD_ID)
public class ConfiguredFeatureModule {
//    @AutoRegister("veg_patch")
//    public static final AutoRegisterConfiguredFeature<RandomPatchConfiguration> VEG_PATCH = AutoRegisterConfiguredFeature.of(
//            Feature.FLOWER,
//            () -> new RandomPatchConfiguration(
//                    20, // tries
//                    6, // xz spread
//                    2, // y spread
//                    PlacementUtils.onlyWhenEmpty(FeatureModule.RIBBITS_VEGETATION_FEATURE, new NoneFeatureConfiguration()) // PlacedFeature
//            )
//    );
}
