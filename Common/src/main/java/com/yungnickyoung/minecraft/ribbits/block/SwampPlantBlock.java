package com.yungnickyoung.minecraft.ribbits.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Optional;

public class SwampPlantBlock extends BushBlock implements BonemealableBlock {
    public static final MapCodec<SwampPlantBlock> CODEC = RecordCodecBuilder.mapCodec(builder -> builder
            .group(
                    propertiesCodec(),
                    ResourceKey.codec(Registries.PLACED_FEATURE).fieldOf("bonemeal_patch").forGetter(block -> block.bonemealPatch)
            ).apply(builder, SwampPlantBlock::new));

    private static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    private final ResourceKey<PlacedFeature> bonemealPatch;

    public SwampPlantBlock(Properties properties, ResourceKey<PlacedFeature> bonemealPatch) {
        super(properties);
        this.bonemealPatch = bonemealPatch;
    }

    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        Vec3 offset = blockState.getOffset(blockGetter, blockPos);
        return SHAPE.move(offset.x, offset.y, offset.z);
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
                .getOptional(this.bonemealPatch);
        placedFeature.ifPresent(feature -> feature.place(serverLevel, serverLevel.getChunkSource().getGenerator(), random, blockPos));
    }

    @Override
    protected MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }
}
