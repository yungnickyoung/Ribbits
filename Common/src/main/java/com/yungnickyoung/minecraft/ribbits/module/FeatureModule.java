package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.world.feature.RibbitsSimplePlantBlock;
import com.yungnickyoung.minecraft.ribbits.world.feature.config.RibbitsSimplePlantBlockConfig;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegister;
import net.minecraft.world.level.levelgen.feature.Feature;

@AutoRegister(RibbitsCommon.MOD_ID)
public class FeatureModule {
    @AutoRegister("simple_block")
    public static final Feature<RibbitsSimplePlantBlockConfig> SIMPLE_PLANT_BLOCK = new RibbitsSimplePlantBlock(RibbitsSimplePlantBlockConfig.CODEC);
}
