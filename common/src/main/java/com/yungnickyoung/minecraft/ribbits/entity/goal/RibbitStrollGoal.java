package com.yungnickyoung.minecraft.ribbits.entity.goal;

import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class RibbitStrollGoal extends RandomStrollGoal {
    private final int dayHomeRange;
    private final RibbitEntity ribbit;

    public RibbitStrollGoal(RibbitEntity mob, double speedModifier, int dayHomeRange) {
        super(mob, speedModifier);
        this.ribbit = mob;
        this.dayHomeRange = dayHomeRange;

        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    public RibbitStrollGoal(RibbitEntity mob, double speedModifier, int interval, int dayHomeRange) {
        super(mob, speedModifier, interval);
        this.ribbit = mob;
        this.dayHomeRange = dayHomeRange;
    }

    public RibbitStrollGoal(RibbitEntity mob, double speedModifier, int interval, boolean checkNoActionTime, int dayHomeRange) {
        super(mob, speedModifier, interval, checkNoActionTime);
        this.ribbit = mob;
        this.dayHomeRange = dayHomeRange;
    }

    @Override
    public void start() {
        float waterModifier = this.ribbit.isInWater() ? RibbitEntity.WATER_SPEED_MULTIPLIER : 1.0f;
        this.mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier * waterModifier);
    }

    @Override
    public boolean canUse() {
        if (this.ribbit.level().isDarkOutside()) {
            return false;
        }

        return super.canUse();
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

    @Nullable
    @Override
    protected Vec3 getPosition() {
        BlockPos distanceVariance = new BlockPos(this.mob.getRandom().nextInt(this.dayHomeRange * 2) - this.dayHomeRange,
                this.mob.getRandom().nextInt(this.dayHomeRange * 2) - this.dayHomeRange,
                this.mob.getRandom().nextInt(this.dayHomeRange * 2) - this.dayHomeRange);

        return new Vec3(distanceVariance.getX() + this.ribbit.getHomePosition().getX(),
                distanceVariance.getY() + this.ribbit.getHomePosition().getY(),
                distanceVariance.getZ() + this.ribbit.getHomePosition().getZ());
    }
}
