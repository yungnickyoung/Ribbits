package com.yungnickyoung.minecraft.ribbits.block;

import com.yungnickyoung.minecraft.ribbits.module.PlacedFeatureModule;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.WaterlilyBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Optional;

public class GiantLilyPadBlock extends WaterlilyBlock implements BonemealableBlock {
    protected static final VoxelShape AABB = Block.box(0.0, 0.0, 0.0, 16.0, 1.5, 16.0);

    public GiantLilyPadBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return AABB;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos blockPos, BlockState blockState) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel, RandomSource random, BlockPos blockPos, BlockState blockState) {
        Optional<PlacedFeature> placedFeature = serverLevel.registryAccess()
                .registryOrThrow(Registries.PLACED_FEATURE)
                .getOptional(PlacedFeatureModule.GIANT_LILYPAD_PATCH);
        placedFeature.ifPresent(feature -> feature.place(serverLevel, serverLevel.getChunkSource().getGenerator(), random, blockPos));
    }
}
