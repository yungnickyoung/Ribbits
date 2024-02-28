package com.yungnickyoung.minecraft.ribbits.entity.goal;

import com.yungnickyoung.minecraft.ribbits.data.RibbitData;
import com.yungnickyoung.minecraft.ribbits.data.RibbitInstrument;
import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import com.yungnickyoung.minecraft.ribbits.module.RibbitInstrumentModule;
import com.yungnickyoung.minecraft.ribbits.services.Services;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RibbitPlayMusicGoal extends Goal {
    private final RibbitEntity ribbit;
    private final double speedModifier;
    private final int minRequiredPlayTicks;
    private final int maxRequiredPlayTicks;

    private int requiredPlayTicks;

    private Path path;
    private double pathedTargetX;
    private double pathedTargetY;
    private double pathedTargetZ;
    private int ticksUntilNextPathRecalculation;

    public RibbitPlayMusicGoal(RibbitEntity ribbit, double speedModifier, int minRequiredPlayTicks, int maxRequiredPlayTicks) {
        this.ribbit = ribbit;
        this.speedModifier = speedModifier;
        this.minRequiredPlayTicks = minRequiredPlayTicks;
        this.maxRequiredPlayTicks = maxRequiredPlayTicks;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        // Scan for other ribbits playing music and sync master ribbit with them
        this.ribbit.level.getEntitiesOfClass(RibbitEntity.class, this.ribbit.getBoundingBox().inflate(64.0d, 16.0d, 64.0d)).stream().filter(RibbitEntity::getPlayingInstrument).forEach((ribbit) -> {
            if (ribbit.getMasterRibbit() != null) {
                this.ribbit.setMasterRibbit(ribbit.getMasterRibbit());
            }
        });

        if (this.ribbit.getMasterRibbit() != null && this.ribbit.getMasterRibbit().isBandFull()) {
            this.ribbit.setMasterRibbit(null);
            return false;
        }

        return !this.ribbit.getUmbrellaFalling() && !this.ribbit.isDeadOrDying();
    }

    @Override
    public boolean canContinueToUse() {
        return !this.ribbit.getUmbrellaFalling() && !this.ribbit.isDeadOrDying() && (this.ribbit.getPlayingInstrument() || this.ribbit.getMasterRibbit() == null || !this.ribbit.getMasterRibbit().isBandFull());
    }

    @Override
    public void start() {
        this.ribbit.getRibbitData().setInstrument(RibbitInstrumentModule.NONE);

        if (this.ribbit.getMasterRibbit() != null) {
            this.path = this.ribbit.getNavigation().createPath(this.ribbit.getMasterRibbit(), 0);
            this.ribbit.getNavigation().moveTo(this.path, this.speedModifier);
        }

        if (this.ribbit.getMasterRibbit() == null) {
            this.ribbit.setMasterRibbit(this.ribbit);
        }

        this.requiredPlayTicks = this.ribbit.getRandom().nextInt(this.minRequiredPlayTicks, this.maxRequiredPlayTicks);
    }

    @Override
    public void stop() {
        this.ribbit.getMasterRibbit().removeRibbitFromPlayingMusic(this.ribbit);
        this.ribbit.getMasterRibbit().removeBandMember(this.ribbit.getRibbitData().getInstrument());

        if (this.ribbit.isMasterRibbit()) {
            this.ribbit.findNewMasterRibbit();
        }

        this.ribbit.setPlayingInstrument(false);
        this.ribbit.setTicksPlayingMusic(0);

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
        return this.ribbit.getTicksPlayingMusic() > this.requiredPlayTicks;
    }

    @Override
    public void tick() {
        if (this.ribbit.getMasterRibbit() == null || this.ribbit.getMasterRibbit().isDeadOrDying() || !this.ribbit.getMasterRibbit().getPlayingInstrument()) {
            // Scan for other ribbits playing music and sync master ribbit with them
            this.ribbit.level.getEntitiesOfClass(RibbitEntity.class, this.ribbit.getBoundingBox().inflate(64.0d, 16.0d, 64.0d)).stream().filter(RibbitEntity::getPlayingInstrument).forEach((ribbit) -> {
                if (ribbit.getMasterRibbit() != null) {
                    this.ribbit.setMasterRibbit(ribbit.getMasterRibbit());
                }
            });

            if (this.ribbit.getMasterRibbit() != null && this.ribbit.getMasterRibbit().isBandFull()) {
                this.ribbit.setMasterRibbit(null);
                return;
            }

            if (this.ribbit.getMasterRibbit() == null) {
                this.ribbit.setMasterRibbit(this.ribbit);
            }
        }

        RibbitEntity masterRibbit = this.ribbit.getMasterRibbit();

        this.ribbit.getLookControl().setLookAt(masterRibbit, 30.0f, 30.0f);
        double d = this.ribbit.distanceToSqr(masterRibbit.getX(), masterRibbit.getY(), masterRibbit.getZ());
        this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);

        if (!this.ribbit.getPlayingInstrument() && this.ticksUntilNextPathRecalculation == 0 && (this.pathedTargetX == 0.0 && this.pathedTargetY == 0.0 && this.pathedTargetZ == 0.0 || masterRibbit.distanceToSqr(this.pathedTargetX, this.pathedTargetY, this.pathedTargetZ) >= 1.0 || this.ribbit.getRandom().nextFloat() < 0.05f)) {
            this.pathedTargetX = masterRibbit.getX();
            this.pathedTargetY = masterRibbit.getY();
            this.pathedTargetZ = masterRibbit.getZ();
            this.ticksUntilNextPathRecalculation = 4 + this.ribbit.getRandom().nextInt(7);
            if (d > 1024.0) {
                this.ticksUntilNextPathRecalculation += 10;
            } else if (d > 256.0) {
                this.ticksUntilNextPathRecalculation += 5;
            }
            if (!this.ribbit.getNavigation().moveTo(masterRibbit, this.speedModifier)) {
                this.ticksUntilNextPathRecalculation += 15;
            }
            this.ticksUntilNextPathRecalculation = this.adjustedTickDelay(this.ticksUntilNextPathRecalculation);
        }

        if (!this.ribbit.getPlayingInstrument() && d <= 9.0f) {
            // Set the instrument.
            if (ribbit.getRibbitData().getInstrument() == RibbitInstrumentModule.NONE) {
                RibbitData ribbitData = this.ribbit.getRibbitData();
                RibbitInstrument instrument = RibbitInstrumentModule.getRandomInstrument(masterRibbit.getBandMembers());

                if (instrument == null) {
                    return;
                }

                ribbitData.setInstrument(instrument);
                this.ribbit.setRibbitData(ribbitData);

                this.ribbit.getMasterRibbit().addBandMember(this.ribbit.getRibbitData().getInstrument());
            }

            this.ribbit.getNavigation().stop();
            this.ribbit.setPlayingInstrument(true);
            this.ribbit.setTicksPlayingMusic(0);

            Services.PLATFORM.onRibbitStartMusicGoal((ServerLevel) this.ribbit.level, this.ribbit, masterRibbit);

            // If this ribbit is not the master ribbit, add it to the master ribbit's list of ribbits playing music
            masterRibbit.addRibbitToPlayingMusic(this.ribbit);
        }

        if (this.ribbit.getPlayingInstrument()) {
            this.ribbit.getNavigation().stop();

            if (d > 9.0f) {
                this.ribbit.setPlayingInstrument(false);
                this.ribbit.getRibbitData().setInstrument(RibbitInstrumentModule.NONE);
                masterRibbit.removeRibbitFromPlayingMusic(this.ribbit);
                this.ribbit.setTicksPlayingMusic(0);
                return;
            }

            // While playing music, only the master ribbit will send music packets to players
            if (this.ribbit.equals(masterRibbit)) {
                Set<Player> playersHearingMusic = new HashSet<>(this.ribbit.getPlayersHearingMusic());

                // Add any new players in range
                List<Player> playersInRange = this.ribbit.level.getEntitiesOfClass(Player.class, this.ribbit.getBoundingBox().inflate(32.0, 32.0, 32.0));
                for (Player player : playersInRange) {
                    if (!playersHearingMusic.contains(player)) {
//                    RibbitsCommon.LOGGER.info("Starting music for " + player.getName().getString());
                        playersHearingMusic.add(player);
                        Services.PLATFORM.onPlayerEnterBandRange((ServerPlayer) player, (ServerLevel) this.ribbit.level, this.ribbit);
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
}
