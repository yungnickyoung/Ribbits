package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.services.Services;
import com.yungnickyoung.minecraft.ribbits.world.structure.RibbitVillage;
import com.yungnickyoung.minecraft.yungsapi.api.YungJigsawConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.StructureFeature;

public class StructureFeatureModule {
    public static StructureFeature<YungJigsawConfig> RIBBIT_VILLAGE = new RibbitVillage();

    public static void init() {
        Services.REGISTRY.registerStructureFeature(new ResourceLocation(RibbitsCommon.MOD_ID, "ribbit_village"), RIBBIT_VILLAGE);
    }
}
