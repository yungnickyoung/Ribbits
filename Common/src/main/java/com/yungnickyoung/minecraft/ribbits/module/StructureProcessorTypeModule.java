package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.world.processor.BlockReplaceProcessor;
import com.yungnickyoung.minecraft.ribbits.world.processor.PillarProcessor;
import com.yungnickyoung.minecraft.ribbits.world.processor.PodzolProcessor;
import com.yungnickyoung.minecraft.ribbits.world.processor.WarpedNyliumProcessor;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegister;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

@AutoRegister(RibbitsCommon.MOD_ID)
public class StructureProcessorTypeModule {
    @AutoRegister("pillar_processor")
    public static StructureProcessorType<PillarProcessor> PILLAR_PROCESSOR = () -> PillarProcessor.CODEC;

    @AutoRegister("podzol_processor")
    public static StructureProcessorType<PodzolProcessor> PODZOL_PROCESSOR = () -> PodzolProcessor.CODEC;

    @AutoRegister("warped_nylium_processor")
    public static StructureProcessorType<WarpedNyliumProcessor> WARPED_NYLIUM_PROCESSOR = () -> WarpedNyliumProcessor.CODEC;

    @AutoRegister("block_replace_processor")
    public static StructureProcessorType<BlockReplaceProcessor> BLOCK_REPLACE_PROCESSOR = () -> BlockReplaceProcessor.CODEC;
}
