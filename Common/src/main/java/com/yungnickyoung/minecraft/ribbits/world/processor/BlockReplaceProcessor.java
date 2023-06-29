package com.yungnickyoung.minecraft.ribbits.world.processor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yungnickyoung.minecraft.ribbits.module.StructureProcessorTypeModule;
import com.yungnickyoung.minecraft.yungsapi.world.BlockStateRandomizer;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlockReplaceProcessor extends StructureProcessor {
    public static final Codec<BlockReplaceProcessor> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                    BlockState.CODEC.fieldOf("target_block").forGetter(config -> config.targetBlock),
                    BlockStateRandomizer.CODEC.fieldOf("output").forGetter(config -> config.output),
                    Codec.BOOL.optionalFieldOf("randomize_facing", false).forGetter(config -> config.randomizeFacing),
                    Codec.BOOL.optionalFieldOf("randomize_half", false).forGetter(config -> config.randomizeHalf),
                    Codec.BOOL.optionalFieldOf("copy_input_properties", false).forGetter(config -> config.copyInputProperties),
                    Codec.BOOL.optionalFieldOf("preserve_waterlog", false).forGetter(config -> config.preserveWaterlog))
            .apply(instance, instance.stable(BlockReplaceProcessor::new)));

    public final BlockState targetBlock;
    public final BlockStateRandomizer output;
    public final boolean randomizeFacing;
    public final boolean randomizeHalf;
    public final boolean copyInputProperties;
    public final boolean preserveWaterlog;

    private BlockReplaceProcessor(BlockState targetBlock,
                                  BlockStateRandomizer output,
                                  boolean randomizeFacing,
                                  boolean randomizeHalf,
                                  boolean copyInputProperties,
                                  boolean preserveWaterlog) {
        this.targetBlock = targetBlock;
        this.output = output;
        this.randomizeFacing = randomizeFacing;
        this.randomizeHalf = randomizeHalf;
        this.copyInputProperties = copyInputProperties;
        this.preserveWaterlog = preserveWaterlog;
    }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                             StructureTemplate.StructureBlockInfo blockInfoLocal,
                                                             StructureTemplate.StructureBlockInfo blockInfoGlobal,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfoGlobal.state.is(this.targetBlock.getBlock())) {
            Random random = structurePlacementData.getRandom(blockInfoGlobal.pos);
            BlockState outputState = output.get(random);

            if (this.copyInputProperties) {
                if (blockInfoGlobal.state.hasProperty(StairBlock.FACING) && outputState.hasProperty(StairBlock.FACING)) {
                    outputState = outputState.setValue(StairBlock.FACING, blockInfoGlobal.state.getValue(StairBlock.FACING));
                }
                if (blockInfoGlobal.state.hasProperty(StairBlock.HALF) && outputState.hasProperty(StairBlock.HALF)) {
                    outputState = outputState.setValue(StairBlock.HALF, blockInfoGlobal.state.getValue(StairBlock.HALF));
                }
                if (blockInfoGlobal.state.hasProperty(StairBlock.SHAPE) && outputState.hasProperty(StairBlock.SHAPE)) {
                    outputState = outputState.setValue(StairBlock.SHAPE, blockInfoGlobal.state.getValue(StairBlock.SHAPE));
                }
                if (blockInfoGlobal.state.hasProperty(SlabBlock.TYPE) && outputState.hasProperty(SlabBlock.TYPE)) {
                    outputState = outputState.setValue(SlabBlock.TYPE, blockInfoGlobal.state.getValue(SlabBlock.TYPE));
                }
                if (blockInfoGlobal.state.hasProperty(WallBlock.NORTH_WALL) && outputState.hasProperty(WallBlock.NORTH_WALL)) {
                    outputState = outputState.setValue(WallBlock.NORTH_WALL, blockInfoGlobal.state.getValue(WallBlock.NORTH_WALL));
                }
                if (blockInfoGlobal.state.hasProperty(WallBlock.EAST_WALL) && outputState.hasProperty(WallBlock.EAST_WALL)) {
                    outputState = outputState.setValue(WallBlock.EAST_WALL, blockInfoGlobal.state.getValue(WallBlock.EAST_WALL));
                }
                if (blockInfoGlobal.state.hasProperty(WallBlock.SOUTH_WALL) && outputState.hasProperty(WallBlock.SOUTH_WALL)) {
                    outputState = outputState.setValue(WallBlock.SOUTH_WALL, blockInfoGlobal.state.getValue(WallBlock.SOUTH_WALL));
                }
                if (blockInfoGlobal.state.hasProperty(WallBlock.WEST_WALL) && outputState.hasProperty(WallBlock.WEST_WALL)) {
                    outputState = outputState.setValue(WallBlock.WEST_WALL, blockInfoGlobal.state.getValue(WallBlock.WEST_WALL));
                }
                if (blockInfoGlobal.state.hasProperty(FenceBlock.NORTH) && outputState.hasProperty(FenceBlock.NORTH)) {
                    outputState = outputState.setValue(FenceBlock.NORTH, blockInfoGlobal.state.getValue(FenceBlock.NORTH));
                }
                if (blockInfoGlobal.state.hasProperty(FenceBlock.EAST) && outputState.hasProperty(FenceBlock.EAST)) {
                    outputState = outputState.setValue(FenceBlock.EAST, blockInfoGlobal.state.getValue(FenceBlock.EAST));
                }
                if (blockInfoGlobal.state.hasProperty(FenceBlock.SOUTH) && outputState.hasProperty(FenceBlock.SOUTH)) {
                    outputState = outputState.setValue(FenceBlock.SOUTH, blockInfoGlobal.state.getValue(FenceBlock.SOUTH));
                }
                if (blockInfoGlobal.state.hasProperty(FenceBlock.WEST) && outputState.hasProperty(FenceBlock.WEST)) {
                    outputState = outputState.setValue(FenceBlock.WEST, blockInfoGlobal.state.getValue(FenceBlock.WEST));
                }
                if (blockInfoGlobal.state.hasProperty(WallBlock.UP) && outputState.hasProperty(WallBlock.UP)) {
                    outputState = outputState.setValue(WallBlock.UP, blockInfoGlobal.state.getValue(WallBlock.UP));
                }

            }

            // Randomize output
            if (this.randomizeFacing) {
                if (outputState.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                    outputState = outputState.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.Plane.HORIZONTAL.getRandomDirection(random));
                }
                if (outputState.hasProperty(BlockStateProperties.FACING)) {
                    outputState = outputState.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.getRandom(random));
                }
            }
            if (this.randomizeHalf) {
                if (outputState.hasProperty(BlockStateProperties.HALF)) {
                    outputState = outputState.setValue(BlockStateProperties.HALF, random.nextBoolean() ? Half.TOP : Half.BOTTOM);
                }
                if (outputState.hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF)) {
                    outputState = outputState.setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, random.nextBoolean() ? DoubleBlockHalf.UPPER : DoubleBlockHalf.LOWER);
                }
                if (outputState.hasProperty(BlockStateProperties.SLAB_TYPE)) {
                    outputState = outputState.setValue(BlockStateProperties.SLAB_TYPE, random.nextBoolean() ? SlabType.TOP : SlabType.BOTTOM);
                }
            }

            // Schedule fluid tick, if applicable
            if (levelReader instanceof WorldGenRegion worldGenRegion && (outputState.is(Blocks.WATER) || outputState.is(Blocks.LAVA))) {
                FlowingFluid fluid = outputState.is(Blocks.WATER) ? Fluids.WATER : Fluids.LAVA;
                worldGenRegion.scheduleTick(blockInfoGlobal.pos, fluid, 0);
            }

            if (this.preserveWaterlog && outputState.hasProperty(BlockStateProperties.WATERLOGGED)
                    && blockInfoGlobal.state.hasProperty(BlockStateProperties.WATERLOGGED) && blockInfoGlobal.state.getValue(BlockStateProperties.WATERLOGGED)) {
                outputState = outputState.setValue(BlockStateProperties.WATERLOGGED, true);
            }

            blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos, outputState, blockInfoGlobal.nbt);
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorTypeModule.BLOCK_REPLACE_PROCESSOR;
    }
}
