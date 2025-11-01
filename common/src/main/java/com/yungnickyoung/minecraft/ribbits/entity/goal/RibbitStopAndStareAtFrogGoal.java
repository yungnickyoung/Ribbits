package com.yungnickyoung.minecraft.ribbits.entity.goal;


import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.frog.Frog;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

public class RibbitStopAndStareAtFrogGoal extends Goal {
    private final RibbitEntity ribbit;
    @Nullable
    private Frog followingFrog;
    private final float searchRadius;

    public RibbitStopAndStareAtFrogGoal(RibbitEntity ribbit, float searchRadius) {
        this.ribbit = ribbit;
        this.searchRadius = searchRadius;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        List<Frog> nearbyFrogs = this.ribbit.level().getEntitiesOfClass(Frog.class, this.ribbit.getBoundingBox().inflate(this.searchRadius), target -> target instanceof Frog);
        if (!nearbyFrogs.isEmpty()) {
            for (Frog frog : nearbyFrogs) {
                if (!frog.isInvisible() && this.ribbit.hasLineOfSight(frog)) {
                    this.followingFrog = frog;
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return this.followingFrog != null
                && !this.followingFrog.isInvisible()
                && this.ribbit.hasLineOfSight(this.followingFrog)
                && this.ribbit.distanceToSqr(this.followingFrog) <= (this.searchRadius * this.searchRadius);
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
        this.followingFrog = null;
    }

    @Override
    public void tick() {
        if (this.followingFrog != null && !this.ribbit.isLeashed()) {
            this.ribbit.getLookControl().setLookAt(this.followingFrog, 10.0F, (float) this.ribbit.getMaxHeadXRot());
        }
    }
}
