package com.yungnickyoung.minecraft.ribbits.client.sound;

import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.List;

public class RibbitInstrumentSoundInstance extends AbstractTickableSoundInstance {
    private final RibbitEntity ribbit;

    public RibbitInstrumentSoundInstance(RibbitEntity ribbit, SoundEvent soundEvent) {
        super(soundEvent, SoundSource.NEUTRAL);
        this.ribbit = ribbit;
        this.attenuation = SoundInstance.Attenuation.NONE;
        this.looping = true;
        this.delay = 0;
        this.volume = 0.0F;
    }

    @Override
    public void tick() {
        this.x = this.ribbit.getX();
        this.y = this.ribbit.getY();
        this.z = this.ribbit.getZ();

        List<RibbitEntity> nearbyRibbits = this.ribbit.level.getNearbyEntities(RibbitEntity.class, TargetingConditions.DEFAULT, this.ribbit, this.ribbit.getBoundingBox().inflate(20.0d, 5.0d, 20.0d));

        if (this.ribbit.isRemoved() || nearbyRibbits.stream().noneMatch(RibbitEntity::getPlayingInstrument)) {
            this.ribbit.setPlayingMusic(false);
            this.stop();
        }

        if (this.ribbit.getPlayingInstrument()) {
            this.volume = 1.0f;
        } else {
            this.volume = 0.0f;
        }
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    @Override
    public boolean canPlaySound() {
        return super.canPlaySound();
    }
}
