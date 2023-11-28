package com.yungnickyoung.minecraft.ribbits.entity.goal;

import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumSet;
import java.util.Optional;

public class RibbitWaterCropsGoal extends Goal {
    private final RibbitEntity ribbit;
    private final double range;
    private final int averageTickToWater;

    private BlockPos waterPos;
    private int wateringTicks = 0;

    public RibbitWaterCropsGoal(RibbitEntity ribbit, double range, int averageTicksToWater) {
        this.ribbit = ribbit;
        this.range = range;
        this.averageTickToWater = averageTicksToWater;

        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public void start() {
        this.ribbit.getMoveControl().setWantedPosition(this.waterPos.getX(), this.waterPos.getY(), this.waterPos.getZ(), 2.0);
    }

    @Override
    public void stop() {
        this.wateringTicks = 0;
        this.ribbit.setWatering(false);
        this.waterPos = null;
    }

    @Override
    public boolean canUse() {
        Optional<BlockPos> cropPos = BlockPos.findClosestMatch(this.ribbit.getOnPos(), (int) range, 5, blockpos -> {
            if (this.ribbit.level.getBlockState(blockpos).getBlock() instanceof CropBlock cropBlock) {
                return !cropBlock.isMaxAge(this.ribbit.level.getBlockState(blockpos));
            } else {
                return false;
            }
        });

        cropPos.ifPresent(blockPos -> this.waterPos = blockPos);

        return cropPos.isPresent();
    }

    @Override
    public boolean canContinueToUse() {
        Iterable<BlockPos> nearbyPositions = BlockPos.betweenClosed(Mth.floor(this.ribbit.getX() - 2.0), Mth.floor(this.ribbit.getY() - 2.0), Mth.floor(this.ribbit.getZ() - 2.0), Mth.floor(this.ribbit.getX() + 2.0), this.ribbit.getBlockY(), Mth.floor(this.ribbit.getZ() + 2.0));

        boolean cropNearby = false;
        for (BlockPos nearbyPos : nearbyPositions) {
            if (this.ribbit.level.getBlockState(nearbyPos).getBlock() instanceof CropBlock cropBlock && !cropBlock.isMaxAge(this.ribbit.level.getBlockState(nearbyPos))) {
                cropNearby = true;
                break;
            }
        }

        return this.ribbit.distanceToSqr(this.waterPos.getX(), this.waterPos.getY(), this.waterPos.getZ()) > 2.0 || cropNearby;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (this.ribbit.distanceToSqr(this.waterPos.getX(), this.waterPos.getY(), this.waterPos.getZ()) <= 2.0) {
            this.ribbit.setWatering(true);
            this.wateringTicks++;

            if (this.wateringTicks % this.averageTickToWater == 0) {
                Iterable<BlockPos> nearbyPositions = BlockPos.betweenClosed(Mth.floor(this.ribbit.getX() - 2.0), Mth.floor(this.ribbit.getY() - 2.0), Mth.floor(this.ribbit.getZ() - 2.0), Mth.floor(this.ribbit.getX() + 2.0), this.ribbit.getBlockY(), Mth.floor(this.ribbit.getZ() + 2.0));

                for (BlockPos pos : nearbyPositions) {
                    growCrop(this.ribbit.level, pos);
                    ((ServerLevel) this.ribbit.level).sendParticles(ParticleTypes.FALLING_WATER, pos.getX(), pos.getY() + 0.6d, pos.getZ(), 8, 0.0d, 0.0d, 0.0d, 0.0d);
                }
            }
        } else {
            this.ribbit.setWatering(false);
            this.ribbit.getMoveControl().setWantedPosition(this.waterPos.getX(), this.waterPos.getY(), this.waterPos.getZ(), 2.0f);
        }
    }

    public static boolean growCrop(Level level, BlockPos pos) {
        BlockState block = level.getBlockState(pos);
        if (block.getBlock() instanceof CropBlock cropBlock) {
            if (cropBlock.isValidBonemealTarget(level, pos, block, level.isClientSide)) {
                if (level instanceof ServerLevel) {
                    if (cropBlock.isBonemealSuccess(level, level.random, pos, block)) {
                        cropBlock.performBonemeal((ServerLevel)level, level.random, pos, block);
                    }
                }

                return true;
            }
        }

        return false;
    }
}
