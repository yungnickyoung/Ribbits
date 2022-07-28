package com.yungnickyoung.minecraft.ribbits.services;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public interface IRegistryHelper {
    void registerStructureProcessorType(ResourceLocation resourceLocation, StructureProcessorType<? extends StructureProcessor> structureProcessorType);
    void registerStructureFeature(ResourceLocation resourceLocation, StructureFeature<? extends FeatureConfiguration> structureFeature);
}
