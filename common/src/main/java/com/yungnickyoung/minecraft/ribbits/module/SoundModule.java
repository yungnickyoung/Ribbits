package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;

public class SoundModule {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(RibbitsCommon.MOD_ID, Registries.SOUND_EVENT);

    public static final RegistrySupplier<SoundEvent> ENTITY_RIBBIT_AMBIENT = SOUND_EVENTS.register("entity.ribbit.ambient", () -> SoundEvent.createVariableRangeEvent(RibbitsCommon.id("entity.ribbit.ambient")));
    public static final RegistrySupplier<SoundEvent> ENTITY_RIBBIT_DEATH = SOUND_EVENTS.register("entity.ribbit.death", () -> SoundEvent.createVariableRangeEvent(RibbitsCommon.id("entity.ribbit.death")));
    public static final RegistrySupplier<SoundEvent> ENTITY_RIBBIT_HURT = SOUND_EVENTS.register("entity.ribbit.hurt", () -> SoundEvent.createVariableRangeEvent(RibbitsCommon.id("entity.ribbit.hurt")));
    public static final RegistrySupplier<SoundEvent> ENTITY_RIBBIT_STEP = SOUND_EVENTS.register("entity.ribbit.step", () -> SoundEvent.createVariableRangeEvent(RibbitsCommon.id("entity.ribbit.step")));
    public static final RegistrySupplier<SoundEvent> ENTITY_RIBBIT_MAGIC = SOUND_EVENTS.register("entity.ribbit.magic", () -> SoundEvent.createVariableRangeEvent(RibbitsCommon.id("entity.ribbit.magic")));

    public static final RegistrySupplier<SoundEvent> MUSIC_RIBBIT_BASS = SOUND_EVENTS.register("music.ribbit.bass", () -> SoundEvent.createVariableRangeEvent(RibbitsCommon.id("music.ribbit.bass")));
    public static final RegistrySupplier<SoundEvent> MUSIC_RIBBIT_BONGO = SOUND_EVENTS.register("music.ribbit.bongo", () -> SoundEvent.createVariableRangeEvent(RibbitsCommon.id("music.ribbit.bongo")));
    public static final RegistrySupplier<SoundEvent> MUSIC_RIBBIT_FLUTE = SOUND_EVENTS.register("music.ribbit.flute", () -> SoundEvent.createVariableRangeEvent(RibbitsCommon.id("music.ribbit.flute")));
    public static final RegistrySupplier<SoundEvent> MUSIC_RIBBIT_GUITAR = SOUND_EVENTS.register("music.ribbit.guitar", () -> SoundEvent.createVariableRangeEvent(RibbitsCommon.id("music.ribbit.guitar")));
    public static final RegistrySupplier<SoundEvent> MUSIC_MARACA = SOUND_EVENTS.register("music.ribbit.maraca", () -> SoundEvent.createVariableRangeEvent(RibbitsCommon.id("music.ribbit.maraca")));

    public static void init() {
        SOUND_EVENTS.register();
    }
}
