package com.yungnickyoung.minecraft.ribbits.world.processor;

import com.mojang.serialization.MapCodec;
import com.yungnickyoung.minecraft.ribbits.module.BlockModule;
import com.yungnickyoung.minecraft.ribbits.module.StructureProcessorTypeModule;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jspecify.annotations.NullMarked;

/**
 * Replaces lapis blocks with water and seagrass.
 * Ensures solid block under the water.
 */
@NullMarked
public class LapisBlockProcessor extends StructureProcessor {
    public static final LapisBlockProcessor INSTANCE = new LapisBlockProcessor();
    public static final MapCodec<LapisBlockProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                             StructureTemplate.StructureBlockInfo blockInfoLocal,
                                                             StructureTemplate.StructureBlockInfo blockInfoGlobal,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfoGlobal.state().is(Blocks.LAPIS_BLOCK)) {
            if (levelReader instanceof WorldGenRegion worldGenRegion && !worldGenRegion.getCenter().equals(ChunkPos.containing(blockInfoGlobal.pos()))) {
                return blockInfoGlobal;
            }

            RandomSource random = structurePlacementData.getRandom(blockInfoGlobal.pos());

            blockInfoGlobal = random.nextFloat() < 0.9f
                    ? new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos(), Blocks.WATER.defaultBlockState(), null)
                    : new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos(), Blocks.SEAGRASS.defaultBlockState(), null);

            if (random.nextFloat() < 0.1f) {
                levelReader.getChunk(blockInfoGlobal.pos().above()).setBlockState(blockInfoGlobal.pos().above(), BlockModule.GIANT_LILYPAD.get().defaultBlockState());
            }

            // Set block below to dirt if not solid
            BlockState blockStateBelow = levelReader.getBlockState(blockInfoGlobal.pos().below());
            if (!blockStateBelow.isSolidRender()) {
                levelReader.getChunk(blockInfoGlobal.pos().below()).setBlockState(blockInfoGlobal.pos().below(), Blocks.DIRT.defaultBlockState());
            }
        }
        return blockInfoGlobal;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorTypeModule.LAPIS_BLOCK_PROCESSOR;
    }

    public static MapCodec<LapisBlockProcessor> codec() {
        return CODEC;
    }
}
