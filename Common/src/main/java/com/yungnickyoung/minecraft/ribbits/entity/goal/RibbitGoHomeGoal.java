package com.yungnickyoung.minecraft.ribbits.entity.goal;

import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class RibbitGoHomeGoal extends Goal {
    private final RibbitEntity ribbit;
    private final float homePointRange;
    private final float speedModifier;
    private final int interval;
    private boolean stuck;

    public RibbitGoHomeGoal(RibbitEntity ribbit, float homePointRange, float speedModifier, int interval) {
        this.ribbit = ribbit;
        this.homePointRange = homePointRange;
        this.speedModifier = speedModifier;
        this.interval = interval;

        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.ribbit.getRandom().nextInt(RibbitGoHomeGoal.reducedTickDelay(interval)) != 0) {
            return false;
        }

        if (this.ribbit.getHomePosition().closerToCenterThan(this.ribbit.position(), homePointRange)) {
            return false;
        }

        return this.ribbit.level().isNight();
    }

    @Override
    public void start() {
        this.stuck = false;
    }

    @Override
    public boolean canContinueToUse() {
        return !this.ribbit.isVehicle() &&
                !this.ribbit.getHomePosition().closerToCenterThan(this.ribbit.position(), homePointRange) &&
                !this.stuck;
    }

    @Override
    public void tick() {
        float waterModifier = this.ribbit.isInWater() ? RibbitEntity.WATER_SPEED_MULTIPLIER : 1.0f;
        this.ribbit.getNavigation().setSpeedModifier(this.speedModifier * waterModifier);

        BlockPos homePos = this.ribbit.getHomePosition();

        if (this.ribbit.getNavigation().isDone()) {
            boolean closeEnoughForDirectNavigation = homePos.closerToCenterThan(this.ribbit.position(), 16.0);
            if (closeEnoughForDirectNavigation) {
                Vec3 distanceVariance = new Vec3(
                        this.ribbit.getRandom().nextFloat() * this.homePointRange * 2 - this.homePointRange,
                        this.ribbit.getRandom().nextFloat() * this.homePointRange * 2 - this.homePointRange,
                        this.ribbit.getRandom().nextFloat() * this.homePointRange * 2 - this.homePointRange);

                this.ribbit.getNavigation().moveTo(distanceVariance.x() + homePos.getX(),
                        distanceVariance.y() + homePos.getY(),
                        distanceVariance.z() + homePos.getZ(),
                        this.speedModifier * waterModifier);
            } else {
                Vec3 homePosVec = Vec3.atBottomCenterOf(homePos);

                Vec3 nextPos = DefaultRandomPos.getPosTowards(this.ribbit, 16, 3, homePosVec, Math.PI / 10);
                if (nextPos == null) {
                    nextPos = DefaultRandomPos.getPosTowards(this.ribbit, 24, 7, homePosVec, Math.PI / 2);
                }

                if (nextPos == null) {
                    this.stuck = true;
                } else {
                    BlockPos targetBlock = BlockPos.containing(nextPos);

                    if (this.ribbit.level().getFluidState(targetBlock).is(FluidTags.WATER)) {
                        while (this.ribbit.level().getFluidState(targetBlock.above()).is(FluidTags.WATER)) {
                            targetBlock = targetBlock.above();
                        }
                        nextPos = Vec3.atBottomCenterOf(targetBlock);
                    }

                    this.ribbit.getNavigation().moveTo(
                            nextPos.x,
                            nextPos.y,
                            nextPos.z,
                            this.speedModifier * waterModifier);
                }
            }
        }
    }

    @Override
    public void stop() {
        this.ribbit.getNavigation().stop();
    }
}