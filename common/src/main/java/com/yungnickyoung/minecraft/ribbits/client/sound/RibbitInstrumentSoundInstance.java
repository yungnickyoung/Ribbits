package com.yungnickyoung.minecraft.ribbits.client.sound;

import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import net.minecraft.sounds.SoundEvent;

public class RibbitInstrumentSoundInstance extends InstrumentSoundInstance<RibbitEntity> {
    public RibbitInstrumentSoundInstance(RibbitEntity ribbit, int ticksOffset, SoundEvent soundEvent) {
        super(ribbit, ticksOffset, soundEvent);
        ribbit.setPlayingInstrument(true);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.entity.isRemoved() || !this.getRibbit().getPlayingInstrument()) {
            this.stop();
        }
    }

    public RibbitEntity getRibbit() {
        return this.entity;
    }
}
