package com.yungnickyoung.minecraft.ribbits.world.feature;

import com.yungnickyoung.minecraft.ribbits.module.BlockModule;
import net.minecraft.core.BlockPos;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;

import java.util.List;

public class RibbitsVegetationFeature extends Feature<NoneFeatureConfiguration> {
    private final List<BlockState> cannotPlaceOn = List.of(Blocks.PODZOL.defaultBlockState(),
            Blocks.COARSE_DIRT.defaultBlockState());

    public RibbitsVegetationFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        WorldGenLevel worldGenLevel = ctx.level();
        BlockPos origin = ctx.origin();
        BlockStateProvider blockStates = new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                .add(BlockModule.UMBRELLA_LEAF.get().defaultBlockState(), 1)
                .add(BlockModule.TOADSTOOL.get().defaultBlockState(), 3)
                .add(BlockModule.SWAMP_DAISY.get().defaultBlockState(), 1)
                .build());
        BlockState toPlace = blockStates.getState(ctx.random(), origin);

        // Check for water, in which case we place lily pads instead.
        if (worldGenLevel.getBlockState(origin.below()).is(Blocks.WATER)) {
            worldGenLevel.setBlock(origin, BlockModule.GIANT_LILYPAD.get().defaultBlockState(), 2);
            return true;
        }

        // Check for blocks we can't place on.
        if (this.cannotPlaceOn.contains(worldGenLevel.getBlockState(origin.below()))) {
            return false;
        }

        // Place block if it can survive.
        if (toPlace.canSurvive(worldGenLevel, origin)) {
            if (toPlace.getBlock() instanceof DoublePlantBlock) {
                if (!worldGenLevel.isEmptyBlock(origin.above())) {
                    return false;
                }

                DoublePlantBlock.placeAt(worldGenLevel, toPlace, origin, 2);
            } else {
                worldGenLevel.setBlock(origin, toPlace, 2);
            }

            return true;
        } else {
            return false;
        }
    }
}
