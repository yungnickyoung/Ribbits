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
    private static final int TICKS_TO_WATER = 34;

    private final RibbitEntity ribbit;
    private final double range;
    private final int cooldownTicks;
    private final float speedModifier;

    private BlockPos targetCropPos;

    /**
     * How long the Ribbit has been performing the watering action/animation
     */
    private int wateringTicks = 0;

    public RibbitWaterCropsGoal(RibbitEntity ribbit, double range, float speedModifier, int cooldownTicks) {
        this.ribbit = ribbit;
        this.range = range;
        this.speedModifier = speedModifier;
        this.cooldownTicks = cooldownTicks;

        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public void start() {
        float waterModifier = this.ribbit.isInWater() ? RibbitEntity.WATER_SPEED_MULTIPLIER : 1.0f;

        this.ribbit.getNavigation().moveTo(this.targetCropPos.getX() + 0.5f,
                this.targetCropPos.getY(), this.targetCropPos.getZ() + 0.5f, this.speedModifier * waterModifier);
    }

    @Override
    public void stop() {
        this.wateringTicks = 0;
        this.ribbit.setWatering(false);
        this.targetCropPos = null;
        this.ribbit.setBuffCooldown(this.cooldownTicks);
    }

    @Override
    public boolean canUse() {
        if (this.ribbit.level().isNight()) return false;

        // Find the closest crop block that isn't fully grown
        Optional<BlockPos> cropPos = BlockPos.findClosestMatch(this.ribbit.getOnPos(), (int) range, 5, blockpos -> {
            if (this.ribbit.level().getBlockState(blockpos).getBlock() instanceof CropBlock cropBlock) {
                return this.ribbit.getBuffCooldown() == 0 && !cropBlock.isMaxAge(this.ribbit.level().getBlockState(blockpos));
            } else {
                return false;
            }
        });

        cropPos.ifPresent(blockPos -> this.targetCropPos = blockPos);
        return this.ribbit.getBuffCooldown() == 0 && cropPos.isPresent();
    }

    @Override
    public boolean canContinueToUse() {
        // wateringTicks of -1 means the goal has been stopped
        if (this.wateringTicks < 0) return false;

        boolean cropNearby = false;
        for (BlockPos nearbyPos : getNearbyPositions()) {
            if (this.ribbit.level().getBlockState(nearbyPos).getBlock() instanceof CropBlock cropBlock && !cropBlock.isMaxAge(this.ribbit.level().getBlockState(nearbyPos))) {
                cropNearby = true;
                break;
            }
        }

        return this.ribbit.distanceToSqr(this.targetCropPos.getX() + 0.5f, this.targetCropPos.getY(), this.targetCropPos.getZ() + 0.5f) > 1.0 || cropNearby || wateringTicks > 0;
    }

    @Override
    public boolean isInterruptable() {
        return this.wateringTicks < 0;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        // If the goal has been stopped, don't do anything
        if (this.wateringTicks < 0) {
            return;
        }

        float waterModifier = this.ribbit.isInWater() ? RibbitEntity.WATER_SPEED_MULTIPLIER : 1.0f;
        this.ribbit.getNavigation().setSpeedModifier(this.speedModifier * waterModifier);
        if (this.ribbit.distanceToSqr(this.targetCropPos.getX() + 0.5f, this.targetCropPos.getY(), this.targetCropPos.getZ() + 0.5f) < 2.0f) {
            if (this.wateringTicks == 0) {
                this.ribbit.getLookControl().setLookAt(this.targetCropPos.getX() + 0.5f, this.ribbit.getEyeY(), this.targetCropPos.getZ() + 0.5f);
            }

            this.ribbit.getNavigation().stop();
            this.ribbit.setWatering(true);
            this.wateringTicks++;

            if (this.wateringTicks >= TICKS_TO_WATER) {
                for (BlockPos pos : getNearbyPositions()) {
                    tryGrowCropAtPos(this.ribbit.level(), pos);
                }

                this.wateringTicks = -1; // Prevents watering again until the goal is stopped
            }
        } else if (this.ribbit.distanceToSqr(this.targetCropPos.getX() + 0.5f, this.targetCropPos.getY(), this.targetCropPos.getZ() + 0.5f) < 3.0f) {
            this.ribbit.setWatering(false);
            this.ribbit.getMoveControl().setWantedPosition(this.targetCropPos.getX() + 0.5f,
                    this.targetCropPos.getY(), this.targetCropPos.getZ() + 0.5f, this.speedModifier * waterModifier);
        } else {
            this.ribbit.setWatering(false);
            this.ribbit.getNavigation().moveTo(this.targetCropPos.getX() + 0.5f,
                    this.targetCropPos.getY(), this.targetCropPos.getZ() + 0.5f, this.speedModifier * waterModifier);
        }
    }

    private static void tryGrowCropAtPos(Level level, BlockPos pos) {
        BlockState blockState = level.getBlockState(pos);
        if (blockState.getBlock() instanceof CropBlock cropBlock) {
            if (cropBlock.isValidBonemealTarget(level, pos, blockState)) {
                if (level instanceof ServerLevel serverLevel) {
                    if (cropBlock.isBonemealSuccess(level, level.random, pos, blockState)) {
                        cropBlock.performBonemeal(serverLevel, level.random, pos, blockState);
                        serverLevel.sendParticles(ParticleTypes.FALLING_WATER, pos.getX() + 0.5, pos.getY() + 0.6d, pos.getZ() + 0.5, 8, 0.0d, 0.0d, 0.0d, 0.0d);
                    }
                }
            }
        }
    }

    private Iterable<BlockPos> getNearbyPositions() {
        return BlockPos.betweenClosed(
                Mth.floor(this.ribbit.getX() - 1.0),
                Mth.floor(this.ribbit.getY() - 1.0),
                Mth.floor(this.ribbit.getZ() - 1.0),
                Mth.floor(this.ribbit.getX() + 1.0),
                Mth.floor(this.ribbit.getBlockY() + 1.0),
                Mth.floor(this.ribbit.getZ() + 1.0));
    }
}
