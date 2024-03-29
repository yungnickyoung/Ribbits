package com.yungnickyoung.minecraft.ribbits.client.sound;

import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class RibbitInstrumentSoundInstance extends AbstractTickableSoundInstance {
    private final RibbitEntity ribbit;
    private int sourceId;

    /**
     * The number of ticks to offset the sound by when playback is started.
     * Used to sync up the instrument track with other instruments.
     */
    private final int ticksOffset;

    public RibbitInstrumentSoundInstance(RibbitEntity ribbit, int ticksOffset, SoundEvent soundEvent) {
        super(soundEvent, SoundSource.NEUTRAL);
        this.ribbit = ribbit;
        this.ticksOffset = ticksOffset;
        this.attenuation = Attenuation.LINEAR;
        this.looping = true;
        this.delay = 0;
        this.volume = 2.0f;

        this.ribbit.setPlayingInstrument(true);
    }

    @Override
    public void tick() {
        this.x = this.ribbit.getX();
        this.y = this.ribbit.getY();
        this.z = this.ribbit.getZ();

        if (this.ribbit.isRemoved() || !this.ribbit.getPlayingInstrument()) {
            this.stop();
        }
    }

    public void stopSound() {
        super.stop();
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    @Override
    public boolean canPlaySound() {
        return super.canPlaySound();
    }

    public RibbitEntity getRibbit() {
        return this.ribbit;
    }

    public int getTicksOffset() {
        return ticksOffset;
    }

    public int getSourceId() {
        return this.sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }
}
