package com.yungnickyoung.minecraft.ribbits.entity.goal;

import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import com.yungnickyoung.minecraft.ribbits.services.Services;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.EnumSet;
import java.util.List;

public class RibbitPlayMusicGoal extends Goal {
    private final RibbitEntity ribbit;

    public RibbitPlayMusicGoal(RibbitEntity ribbit) {
        this.ribbit = ribbit;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        List<RibbitEntity> nearbyRibbits = this.ribbit.level.getNearbyEntities(RibbitEntity.class, TargetingConditions.DEFAULT, this.ribbit, this.ribbit.getBoundingBox().inflate(20.0d, 5.0d, 20.0d));

        return nearbyRibbits.size() > 2;
    }

    @Override
    public void start() {
        this.ribbit.getNavigation().stop();

        List<RibbitEntity> nearbyRibbits = this.ribbit.level.getNearbyEntities(RibbitEntity.class, TargetingConditions.DEFAULT, this.ribbit, this.ribbit.getBoundingBox().inflate(20.0d, 5.0d, 20.0d));

        for (RibbitEntity ribbit : nearbyRibbits) {
            Services.PLATFORM.sendRibbitMusicS2CPacket((ServerLevel) this.ribbit.level, this.ribbit);
        }

        this.ribbit.setPlayingInstrument(true);
    }

    @Override
    public void stop() {
        this.ribbit.setPlayingInstrument(false);
    }
}
