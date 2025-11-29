package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.world.feature.RibbitsVegetationBlockFeature;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegister;

@AutoRegister(RibbitsCommon.MOD_ID)
public class FeatureModule {
    @AutoRegister("vegetation_block_feature")
    public static final RibbitsVegetationBlockFeature RIBBITS_VEGETATION_FEATURE = new RibbitsVegetationBlockFeature();
}
