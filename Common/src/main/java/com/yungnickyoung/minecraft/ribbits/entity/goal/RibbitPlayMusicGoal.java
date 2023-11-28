package com.yungnickyoung.minecraft.ribbits.entity.goal;

import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import com.yungnickyoung.minecraft.ribbits.services.Services;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class RibbitPlayMusicGoal extends Goal {
    private final RibbitEntity ribbit;

    public RibbitPlayMusicGoal(RibbitEntity ribbit) {
        this.ribbit = ribbit;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return !this.ribbit.getUmbrellaFalling() && !this.ribbit.isDeadOrDying();
    }

    @Override
    public void start() {
        this.ribbit.getNavigation().stop();
        this.ribbit.setPlayingInstrument(true);

        this.ribbit.level.getEntitiesOfClass(RibbitEntity.class, this.ribbit.getBoundingBox().inflate(20.0d, 5.0d, 20.0d)).stream().filter(RibbitEntity::getPlayingInstrument).forEach((ribbit) -> {
            if (ribbit.getMasterRibbit() != null) {
                this.ribbit.setMasterRibbit(ribbit.getMasterRibbit());
            }
        });

        if (this.ribbit.getMasterRibbit() == null) {
            this.ribbit.setMasterRibbit(this.ribbit);
        }

        Services.PLATFORM.sendRibbitMusicS2CPacketToAll((ServerLevel) this.ribbit.level, this.ribbit, this.ribbit.getMasterRibbit());

        if (!this.ribbit.equals(this.ribbit.getMasterRibbit())) {
            this.ribbit.getMasterRibbit().addRibbitToPlayingMusic(this.ribbit);
        }
    }

    @Override
    public void stop() {
        if (this.ribbit.getMasterRibbit().equals(this.ribbit)) {
            this.ribbit.findNewMasterRibbit();
        }

        this.ribbit.setPlayingInstrument(false);
    }

    @Override
    public void tick() {
        if (this.ribbit.equals(this.ribbit.getMasterRibbit())) {
            Set<Player> newPlayers = new HashSet<>();

            for (Player player : this.ribbit.level.getEntitiesOfClass(Player.class, this.ribbit.getBoundingBox().inflate(20.0d, 5.0d, 20.0d))) {
                newPlayers.add(player);

                if (!this.ribbit.getPlayersHearingMusic().contains(player)) {
                    Services.PLATFORM.sendRibbitMusicS2CPacketToPlayer((ServerPlayer) player, (ServerLevel) this.ribbit.level, this.ribbit, this.ribbit.getMasterRibbit());
                }
            }

            this.ribbit.setPlayersHearingMusic(newPlayers);
        }
    }
}
