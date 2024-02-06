package com.yungnickyoung.minecraft.ribbits.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraftforge.common.PlantType;

public class GiantLilyPadBlockForge extends GiantLilyPadBlock {
    public GiantLilyPadBlockForge(Properties properties) {
        super(properties);
    }

    @Override
    public PlantType getPlantType(BlockGetter level, BlockPos pos) {
        return PlantType.WATER;
    }
}
