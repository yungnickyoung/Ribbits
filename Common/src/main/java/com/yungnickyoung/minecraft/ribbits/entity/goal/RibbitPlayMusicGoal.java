package com.yungnickyoung.minecraft.ribbits.entity.goal;

import com.yungnickyoung.minecraft.ribbits.data.RibbitData;
import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import com.yungnickyoung.minecraft.ribbits.module.RibbitInstrumentModule;
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
        this.ribbit.setTicksPlayingMusic(0);

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

        // Set the instrument.
        // TODO - don't just randomize instruments, have a way to determine which instruments are available to the ribbit
        if (ribbit.getRibbitData().getInstrument() == RibbitInstrumentModule.NONE) {
            RibbitData ribbitData = this.ribbit.getRibbitData();
            ribbitData.setInstrument(RibbitInstrumentModule.getRandomInstrument());
            this.ribbit.setRibbitData(ribbitData);
        }

        Services.PLATFORM.onRibbitStartMusicGoal((ServerLevel) this.ribbit.level, this.ribbit, this.ribbit.getMasterRibbit());

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

        RibbitData ribbitData = this.ribbit.getRibbitData();
        ribbitData.setInstrument(RibbitInstrumentModule.NONE);
        this.ribbit.setRibbitData(ribbitData);
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public boolean isInterruptable() {
        return this.ribbit.getTicksPlayingMusic() > 200; // TODO - randomize goal duration?
//        return false;
    }

    @Override
    public void tick() {
        // While playing music, only the master ribbit will send music packets to players
        if (this.ribbit.equals(this.ribbit.getMasterRibbit())) {
            Set<Player> playersHearingMusic = new HashSet<>(this.ribbit.getPlayersHearingMusic());

            // Add any new players in range
            List<Player> playersInRange = this.ribbit.level.getEntitiesOfClass(Player.class, this.ribbit.getBoundingBox().inflate(32.0, 32.0, 32.0));
            for (Player player : playersInRange) {
                if (!playersHearingMusic.contains(player)) {
//                    RibbitsCommon.LOGGER.info("Starting music for " + player.getName().getString());
                    playersHearingMusic.add(player);
                    Services.PLATFORM.onPlayerEnterBandRange((ServerPlayer) player, (ServerLevel) this.ribbit.level, this.ribbit, this.ribbit.getMasterRibbit());
                }
            }

            // Remove any players no longer in the world or out of range
            playersHearingMusic.removeIf(player -> {
                if (player.isRemoved() || !playersInRange.contains(player)) {
//                    RibbitsCommon.LOGGER.info("Stopping music for " + player.getName().getString());
                    Services.PLATFORM.onPlayerExitBandRange((ServerPlayer) player, (ServerLevel) this.ribbit.level, this.ribbit);
                    return true;
                }
                return false;
            });

            this.ribbit.setPlayersHearingMusic(playersHearingMusic);
        }

        this.ribbit.setTicksPlayingMusic(this.ribbit.getTicksPlayingMusic() + 1);
    }
}
