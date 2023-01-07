package com.yungnickyoung.minecraft.ribbits.world.feature;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.ribbits.world.feature.config.RibbitsSimplePlantBlockConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.List;

public class RibbitsSimplePlantBlock extends Feature<RibbitsSimplePlantBlockConfig> {
    public RibbitsSimplePlantBlock(Codec<RibbitsSimplePlantBlockConfig> codec) {
        super(codec);
    }

    public boolean place(FeaturePlaceContext<RibbitsSimplePlantBlockConfig> ctx) {
        RibbitsSimplePlantBlockConfig config = ctx.config();
        WorldGenLevel worldGenLevel = ctx.level();
        BlockPos origin = ctx.origin();
        BlockState toPlace = config.toPlace().getState(ctx.random(), origin);
        List<BlockState> cannotPlaceOn = config.cannotPlaceOn();

        if (cannotPlaceOn.contains(worldGenLevel.getBlockState(origin.below()))) {
            return false;
        }

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
