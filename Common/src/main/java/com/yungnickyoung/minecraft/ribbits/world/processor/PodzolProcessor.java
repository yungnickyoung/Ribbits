package com.yungnickyoung.minecraft.ribbits.world.processor;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.ribbits.module.StructureProcessorTypeModule;
import com.yungnickyoung.minecraft.yungsapi.world.BlockStateRandomizer;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

/**
 * Randomly replaces some podzol with coarse dirt.
 * Forcibly replaces it with oak planks if there is water at the location instead.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PodzolProcessor extends StructureProcessor {
    public static final PodzolProcessor INSTANCE = new PodzolProcessor();
    public static final Codec<PodzolProcessor> CODEC = Codec.unit(() -> INSTANCE);

    private final BlockStateRandomizer randomOutputState = new BlockStateRandomizer(Blocks.PODZOL.defaultBlockState())
            .addBlock(Blocks.COARSE_DIRT.defaultBlockState(), .3f);

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                             StructureTemplate.StructureBlockInfo blockInfoLocal,
                                                             StructureTemplate.StructureBlockInfo blockInfoGlobal,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfoGlobal.state.is(Blocks.PODZOL)) {
            if (levelReader instanceof WorldGenRegion worldGenRegion && !worldGenRegion.getCenter().equals(new ChunkPos(blockInfoGlobal.pos))) {
                return blockInfoGlobal;
            }

            int y = levelReader.getHeight(Heightmap.Types.WORLD_SURFACE_WG, blockInfoGlobal.pos.getX(), blockInfoGlobal.pos.getZ());
            FluidState currState = levelReader.getFluidState(new BlockPos(blockInfoGlobal.pos.getX(), y - 1, blockInfoGlobal.pos.getZ()));
            if (currState.isEmpty()) {
                Random random = structurePlacementData.getRandom(blockInfoGlobal.pos);
                blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos, randomOutputState.get(random), null);
            } else {
                blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos, Blocks.OAK_PLANKS.defaultBlockState(), null);
            }
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorTypeModule.PODZOL_PROCESSOR;
    }
}
