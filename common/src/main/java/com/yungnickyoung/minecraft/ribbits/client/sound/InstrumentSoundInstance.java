package com.yungnickyoung.minecraft.ribbits.client.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;

public abstract class InstrumentSoundInstance<E extends Entity> extends AbstractTickableSoundInstance {
    protected E entity;
    protected int sourceId;

    /**
     * The number of ticks to offset the sound by when playback is started.
     * Used to sync up the instrument track with other instruments.
     */
    private final int ticksOffset;

    public InstrumentSoundInstance(E entity, int ticksOffset, SoundEvent soundEvent) {
        super(soundEvent, SoundSource.NEUTRAL, SoundInstance.createUnseededRandom());
        this.entity = entity;
        this.ticksOffset = ticksOffset;
        this.attenuation = Attenuation.LINEAR;
        this.looping = true;
        this.delay = 0;
        this.volume = 2.0f;
        this.x = entity.getX();
        this.y = entity.getY();
        this.z = entity.getZ();
    }

    @Override
    public void tick() {
        this.x = this.entity.getX();
        this.y = this.entity.getY();
        this.z = this.entity.getZ();
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

    public E getEntity() {
        return this.entity;
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

    public boolean isForSameEntity(InstrumentSoundInstance<E> other) {
        return this.entity.equals(other.entity);
    }
}
