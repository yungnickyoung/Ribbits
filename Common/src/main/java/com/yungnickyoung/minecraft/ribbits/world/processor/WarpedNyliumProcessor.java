package com.yungnickyoung.minecraft.ribbits.world.processor;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.ribbits.module.StructureProcessorTypeModule;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import java.util.Random;

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
            if (currState.isAir()) {
                blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos, Blocks.AIR.defaultBlockState(), null);
                // Schedule ticks for adjacent fluids
                BlockPos pos = blockInfoGlobal.pos;
                ChunkPos chunkPos = new ChunkPos(pos);
                if (levelReader instanceof WorldGenRegion worldGenRegion) {
                    Direction.Plane.HORIZONTAL.forEach(direction -> {
                        BlockPos neighborPos = pos.relative(direction);
                        if (chunkPos.equals(new ChunkPos(neighborPos))) {
                            worldGenRegion.scheduleTick(neighborPos, Fluids.WATER, 0);
                        }
                    });
                }
            } else if (currState.getFluidState().is(Fluids.WATER)) {
                blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos, Blocks.WATER.defaultBlockState(), null);
                if (levelReader instanceof WorldGenRegion worldGenRegion) {
                    worldGenRegion.scheduleTick(blockInfoGlobal.pos, Fluids.WATER, 0);
                }
            } else {
                blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos, Blocks.GRASS_BLOCK.defaultBlockState(), null);
            }

            // Generate pillar of dirt to fill any air/water below
//            BlockPos.MutableBlockPos mutable = blockInfoGlobal.pos.mutable().move(Direction.DOWN);
//            BlockState currBlockState = levelReader.getBlockState(mutable);
//            while (mutable.getY() > levelReader.getMinBuildHeight()
//                    && mutable.getY() < levelReader.getMaxBuildHeight()
//                    && (currBlockState.isAir() || !levelReader.getFluidState(mutable).isEmpty())) {
//                levelReader.getChunk(mutable).setBlockState(mutable, Blocks.DIRT.defaultBlockState(), false);
//
//                // Update to next position
//                mutable.move(Direction.DOWN);
//                currBlockState = levelReader.getBlockState(mutable);
//            }
        }

        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorTypeModule.WARPED_NYLIUM_PROCESSOR;
    }
}
