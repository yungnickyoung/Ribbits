package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegister;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegisterSoundEvent;

@AutoRegister(RibbitsCommon.MOD_ID)
public class SoundModule {

    @AutoRegister("entity.ribbit.ambient")
    public static final AutoRegisterSoundEvent ENTITY_RIBBIT_AMBIENT = AutoRegisterSoundEvent.create();

    @AutoRegister("entity.ribbit.death")
    public static final AutoRegisterSoundEvent ENTITY_RIBBIT_DEATH = AutoRegisterSoundEvent.create();

    @AutoRegister("entity.ribbit.hurt")
    public static final AutoRegisterSoundEvent ENTITY_RIBBIT_HURT = AutoRegisterSoundEvent.create();

    @AutoRegister("entity.ribbit.step")
    public static final AutoRegisterSoundEvent ENTITY_RIBBIT_STEP = AutoRegisterSoundEvent.create();

    @AutoRegister("music.ribbit.bass")
    public static final AutoRegisterSoundEvent MUSIC_RIBBIT_BASS = AutoRegisterSoundEvent.create();

    @AutoRegister("music.ribbit.bongo")
    public static final AutoRegisterSoundEvent MUSIC_RIBBIT_BONGO = AutoRegisterSoundEvent.create();

    @AutoRegister("music.ribbit.flute")
    public static final AutoRegisterSoundEvent MUSIC_RIBBIT_FLUTE = AutoRegisterSoundEvent.create();

    @AutoRegister("music.ribbit.guitar")
    public static final AutoRegisterSoundEvent MUSIC_RIBBIT_GUITAR = AutoRegisterSoundEvent.create();

    @AutoRegister("music.ribbit.maracas")
    public static final AutoRegisterSoundEvent MUSIC_RIBBIT_MARACAS = AutoRegisterSoundEvent.create();

}
