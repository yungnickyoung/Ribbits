package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.world.processor.*;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class StructureProcessorTypeModule {
    public static final DeferredRegister<StructureProcessorType<?>> STRUCTURE_PROCESSOR_TYPES =
            DeferredRegister.create(RibbitsCommon.MOD_ID, Registries.STRUCTURE_PROCESSOR);

    public static final RegistrySupplier<StructureProcessorType<PillarProcessor>> PILLAR_PROCESSOR =
            STRUCTURE_PROCESSOR_TYPES.register("pillar_processor", () -> PillarProcessor::codec);

    public static final RegistrySupplier<StructureProcessorType<PodzolProcessor>> PODZOL_PROCESSOR =
            STRUCTURE_PROCESSOR_TYPES.register("podzol_processor", () -> PodzolProcessor::codec);

    public static final RegistrySupplier<StructureProcessorType<WarpedNyliumProcessor>> WARPED_NYLIUM_PROCESSOR =
            STRUCTURE_PROCESSOR_TYPES.register("warped_nylium_processor", () -> WarpedNyliumProcessor::codec);

    public static final RegistrySupplier<StructureProcessorType<BlockReplaceProcessor>> BLOCK_REPLACE_PROCESSOR =
            STRUCTURE_PROCESSOR_TYPES.register("block_replace_processor", () -> BlockReplaceProcessor::codec);

    public static final RegistrySupplier<StructureProcessorType<LapisBlockProcessor>> LAPIS_BLOCK_PROCESSOR =
            STRUCTURE_PROCESSOR_TYPES.register("lapis_block_processor", () -> LapisBlockProcessor::codec);

    public static final RegistrySupplier<StructureProcessorType<BrewingStandProcessor>> BREWING_STAND_PROCESSOR =
            STRUCTURE_PROCESSOR_TYPES.register("brewing_stand_processor", () -> BrewingStandProcessor::codec);

    public static void init() {
        STRUCTURE_PROCESSOR_TYPES.register();
    }
}