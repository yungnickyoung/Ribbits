package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.world.feature.RibbitsVegetationFeature;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegister;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;

@AutoRegister(RibbitsCommon.MOD_ID)
public class FeatureModule {
    @AutoRegister("ribbits_vegetation_feature")
    public static final Feature<NoneFeatureConfiguration> RIBBITS_VEGETATION_FEATURE = new RibbitsVegetationFeature();
}
