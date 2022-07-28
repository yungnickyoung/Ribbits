package com.yungnickyoung.minecraft.ribbits.services;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class FabricRegistryHelper implements IRegistryHelper {
    @Override
    public void registerStructureProcessorType(ResourceLocation resourceLocation, StructureProcessorType<? extends StructureProcessor> structureProcessorType) {
        Registry.register(Registry.STRUCTURE_PROCESSOR, resourceLocation, structureProcessorType);
    }

    @Override
    public void registerStructureFeature(ResourceLocation resourceLocation, StructureFeature<? extends FeatureConfiguration> structureFeature) {
        Registry.register(Registry.STRUCTURE_FEATURE, resourceLocation, structureFeature);
    }
}
