package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.world.structure.RibbitVillageStructure;
import com.yungnickyoung.minecraft.yungsapi.api.YungJigsawConfig;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegister;
import net.minecraft.world.level.levelgen.feature.StructureFeature;

@AutoRegister(RibbitsCommon.MOD_ID)
public class StructureFeatureModule {
    @AutoRegister("ribbit_village")
    public static StructureFeature<YungJigsawConfig> RIBBIT_VILLAGE = new RibbitVillageStructure();
}
