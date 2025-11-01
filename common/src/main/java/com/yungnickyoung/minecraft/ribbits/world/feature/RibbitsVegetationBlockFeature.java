package com.yungnickyoung.minecraft.ribbits.world.feature;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.List;
import java.util.Optional;

public class RibbitsVegetationBlockFeature extends Feature<RibbitsVegetationFeatureConfig> {
    public RibbitsVegetationBlockFeature() {
        super(RibbitsVegetationFeatureConfig.CODEC);
    }

    public boolean place(FeaturePlaceContext<RibbitsVegetationFeatureConfig> ctx) {
        WorldGenLevel worldGenLevel = ctx.level();
        BlockPos origin = ctx.origin();
        Optional<BlockStateProvider> onSolidBlockStates = ctx.config().onSolidStateProvider();
        List<BlockState> cannotPlaceOn = ctx.config().cannotPlaceOn();

        // Check for blocks we can't place on.
        if (cannotPlaceOn.contains(worldGenLevel.getBlockState(origin.below()))) {
            return false;
        }

        // Check for water, in which case we place lily pads instead.
        if (ctx.config().onLiquidStateProvider().isPresent() && worldGenLevel.getBlockState(origin.below()).is(Blocks.WATER)) {
            BlockStateProvider onWaterBlockStates = ctx.config().onLiquidStateProvider().get();
            worldGenLevel.setBlock(origin, onWaterBlockStates.getState(ctx.random(), origin), 2);
            return true;
        }

        // If we have no block states to place, return false.
        if (onSolidBlockStates.isEmpty()) {
            return false;
        }

        BlockState toPlace = onSolidBlockStates.get().getState(ctx.random(), origin);

        // Place block if it can survive.
        if (toPlace.canSurvive(worldGenLevel, origin)) {
            if (toPlace.getBlock() instanceof DoublePlantBlock) {
                if (!worldGenLevel.isEmptyBlock(origin.above())) {
                    return false;
                }

                DoublePlantBlock.placeAt(worldGenLevel, toPlace, origin, 3);
            } else {
                worldGenLevel.setBlock(origin, toPlace, 3);
            }

            return true;
        } else {
            return false;
        }
    }
}
