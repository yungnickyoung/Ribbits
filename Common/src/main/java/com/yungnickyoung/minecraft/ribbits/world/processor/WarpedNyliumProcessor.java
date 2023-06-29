package com.yungnickyoung.minecraft.ribbits.world.processor;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.ribbits.module.StructureProcessorTypeModule;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Replaces warped nylium with grass block if no water present.
 * If water is present, then it remains as water.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class WarpedNyliumProcessor extends StructureProcessor {
    public static final WarpedNyliumProcessor INSTANCE = new WarpedNyliumProcessor();
    public static final Codec<WarpedNyliumProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                             StructureTemplate.StructureBlockInfo blockInfoLocal,
                                                             StructureTemplate.StructureBlockInfo blockInfoGlobal,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfoGlobal.state.is(Blocks.WARPED_NYLIUM)) {
            if (levelReader instanceof WorldGenRegion worldGenRegion && !worldGenRegion.getCenter().equals(new ChunkPos(blockInfoGlobal.pos))) {
                return blockInfoGlobal;
            }

            BlockState currState = levelReader.getBlockState(blockInfoGlobal.pos);

            if (currState.getFluidState().is(Fluids.WATER)) {
                blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos, Blocks.WATER.defaultBlockState(), null);
                if (levelReader instanceof WorldGenRegion worldGenRegion) {
                    worldGenRegion.scheduleTick(blockInfoGlobal.pos, Fluids.WATER, 0);
                }
            } else {
                blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos, Blocks.GRASS_BLOCK.defaultBlockState(), null);
            }

            // Generate pillar of dirt to fill any air/water below
        }

        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorTypeModule.WARPED_NYLIUM_PROCESSOR;
    }
}
