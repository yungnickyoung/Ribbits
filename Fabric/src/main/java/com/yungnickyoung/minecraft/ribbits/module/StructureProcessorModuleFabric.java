package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class StructureProcessorModuleFabric {
    public static void init() {
        // TODO
    }

    private static <P extends StructureProcessor> StructureProcessorType<P> register(String name, StructureProcessorType<P> processorType) {
        return  Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(RibbitsCommon.MOD_ID, name), processorType);
    }
}
