package com.yungnickyoung.minecraft.ribbits.services;

import com.yungnickyoung.minecraft.ribbits.module.StructureFeatureModuleForge;
import com.yungnickyoung.minecraft.ribbits.module.StructureProcessorModuleForge;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class ForgeRegistryHelper implements IRegistryHelper {
    @Override
    public void registerStructureProcessorType(ResourceLocation resourceLocation, StructureProcessorType<? extends StructureProcessor> structureProcessorType) {
        StructureProcessorModuleForge.STRUCTURE_PROCESSOR_TYPES.put(resourceLocation, structureProcessorType);
    }

    @Override
    public void registerStructureFeature(ResourceLocation resourceLocation, StructureFeature<? extends FeatureConfiguration> structureFeature) {
        StructureFeatureModuleForge.STRUCTURE_FEATURES.put(resourceLocation, structureFeature);
    }
}
