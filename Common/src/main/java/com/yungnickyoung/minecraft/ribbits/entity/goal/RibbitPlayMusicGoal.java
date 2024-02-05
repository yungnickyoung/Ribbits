package com.yungnickyoung.minecraft.ribbits.entity.goal;

import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import com.yungnickyoung.minecraft.ribbits.services.Services;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        // Scan for other ribbits playing music and sync master ribbit with them
        this.ribbit.level.getEntitiesOfClass(RibbitEntity.class, this.ribbit.getBoundingBox().inflate(20.0d, 5.0d, 20.0d)).stream().filter(RibbitEntity::getPlayingInstrument).forEach((ribbit) -> {
            if (ribbit.getMasterRibbit() != null) {
                this.ribbit.setMasterRibbit(ribbit.getMasterRibbit());
            }
        });

        // If this ribbit is the first to start playing music, it becomes the master ribbit
        if (this.ribbit.getMasterRibbit() == null) {
            this.ribbit.setMasterRibbit(this.ribbit);
        }

        Services.PLATFORM.sendRibbitMusicS2CPacketToAll((ServerLevel) this.ribbit.level, this.ribbit, this.ribbit.getMasterRibbit());

        // If this ribbit is not the master ribbit, add it to the master ribbit's list of ribbits playing music
        if (!this.ribbit.isMasterRibbit()) {
            this.ribbit.getMasterRibbit().addRibbitToPlayingMusic(this.ribbit);
        }
    }

    @Override
    public void stop() {
        if (this.ribbit.isMasterRibbit()) {
            this.ribbit.findNewMasterRibbit();
        }

        this.ribbit.setPlayingInstrument(false);
    }

    @Override
    public void tick() {
        // While playing music, only the master ribbit will send music packets to players
        if (this.ribbit.equals(this.ribbit.getMasterRibbit())) {
            Set<Player> playersPreviouslyHearingMusic = this.ribbit.getPlayersHearingMusic();
            Set<Player> playersCurrentlyHearingMusic = new HashSet<>();

            List<Player> playersInRange = this.ribbit.level.getEntitiesOfClass(Player.class, this.ribbit.getBoundingBox().inflate(20.0d, 10.0d, 20.0d));
            for (Player player : playersInRange) {
                playersCurrentlyHearingMusic.add(player);

                // If the player was not already hearing music, send them a packet to start hearing music
                if (!playersPreviouslyHearingMusic.contains(player)) {
                    Services.PLATFORM.sendRibbitMusicS2CPacketToPlayer((ServerPlayer) player, (ServerLevel) this.ribbit.level, this.ribbit, this.ribbit.getMasterRibbit());
                }
            }

            // If the player was previously hearing music but is no longer in range, send them a packet to stop hearing music?
            // TODO

            this.ribbit.setPlayersHearingMusic(playersCurrentlyHearingMusic);
        }
    }
}
