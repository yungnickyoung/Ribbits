package com.yungnickyoung.minecraft.ribbits.entity.goal;

import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class RibbitGoHomeGoal extends Goal {
    private final RibbitEntity ribbit;
    private final float homePointRange;
    private final float speedModifier;
    private final int interval;

    public RibbitGoHomeGoal(RibbitEntity ribbit, float homePointRange, float speedModifier, int interval) {
        this.ribbit = ribbit;
        this.homePointRange = homePointRange;
        this.speedModifier = speedModifier;
        this.interval = interval;

        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.ribbit.distanceToSqr(this.ribbit.getHomePosition().getX(),
                this.ribbit.getHomePosition().getY(),
                this.ribbit.getHomePosition().getZ()) <= this.homePointRange * this.homePointRange &&
                this.ribbit.getRandom().nextInt(RandomStrollGoal.reducedTickDelay(this.interval)) != 0) {
            return false;
        }

        return this.ribbit.level().isDarkOutside();
    }

    @Override
    public boolean canContinueToUse() {
        return !this.ribbit.getNavigation().isDone() && !this.ribbit.isVehicle();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();

        float waterModifier = this.ribbit.isInWater() ? RibbitEntity.WATER_SPEED_MULTIPLIER : 1.0f;
        this.ribbit.getNavigation().setSpeedModifier(this.speedModifier * waterModifier);
    }

    @Override
    public void start() {
        Vec3 distanceVariance = new Vec3(
                this.ribbit.getRandom().nextFloat() * this.homePointRange * 2 - this.homePointRange,
                this.ribbit.getRandom().nextFloat() * this.homePointRange * 2 - this.homePointRange,
                this.ribbit.getRandom().nextFloat() * this.homePointRange * 2 - this.homePointRange);

        float waterModifier = this.ribbit.isInWater() ? RibbitEntity.WATER_SPEED_MULTIPLIER : 1.0f;

        this.ribbit.getNavigation().moveTo(distanceVariance.x() + this.ribbit.getHomePosition().getX(),
                distanceVariance.y() + this.ribbit.getHomePosition().getY(),
                distanceVariance.z() + this.ribbit.getHomePosition().getZ(), this.speedModifier * waterModifier);
    }

    @Override
    public void stop() {
        this.ribbit.getNavigation().stop();
    }
}
