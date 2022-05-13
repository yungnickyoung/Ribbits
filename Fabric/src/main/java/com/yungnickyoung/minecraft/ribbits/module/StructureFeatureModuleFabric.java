package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.world.structure.RibbitVillage;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class StructureFeatureModuleFabric {
    public static void init() {
        StructureFeatureModule.RIBBIT_VILLAGE = register("ribbit_village", new RibbitVillage());
    }

    private static <FC extends FeatureConfiguration> StructureFeature<FC> register(String name, StructureFeature<FC> structureFeature) {
        return Registry.register(Registry.STRUCTURE_FEATURE, new ResourceLocation(RibbitsCommon.MOD_ID, name), structureFeature);
    }
}
