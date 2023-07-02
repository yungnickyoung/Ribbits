package com.yungnickyoung.minecraft.ribbits.entity.npc;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.module.SoundModule;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class RibbitProfession {
    public static final RibbitProfession GARDENER = new RibbitProfession("gardener", new ResourceLocation(RibbitsCommon.MOD_ID, "geo/gardener_ribbit.geo.json"), null);
    public static final RibbitProfession SORCERER = new RibbitProfession("sorcerer", new ResourceLocation(RibbitsCommon.MOD_ID, "geo/sorcerer_ribbit.geo.json"), null);
    public static final RibbitProfession BASSIST = new RibbitProfession("bassist", new ResourceLocation(RibbitsCommon.MOD_ID, "geo/bass_ribbit.geo.json"), SoundModule.MUSIC_RIBBIT_BASS.get());
    public static final RibbitProfession BONGOIST = new RibbitProfession("bongoist", new ResourceLocation(RibbitsCommon.MOD_ID, "geo/bongo_ribbit.geo.json"), SoundModule.MUSIC_RIBBIT_BONGO.get());
    public static final RibbitProfession FLAUTIST = new RibbitProfession("flautist", new ResourceLocation(RibbitsCommon.MOD_ID, "geo/flute_ribbit.geo.json"), SoundModule.MUSIC_RIBBIT_FLUTE.get());
    public static final RibbitProfession GUITARIST = new RibbitProfession("guitarist", new ResourceLocation(RibbitsCommon.MOD_ID, "geo/guitar_ribbit.geo.json"), SoundModule.MUSIC_RIBBIT_GUITAR.get());

    private final String name;
    private final ResourceLocation modelLocation;
    private final SoundEvent insrumentTrack;
    private final int id;

    private static int nextId = 0;
    public static final List<RibbitProfession> nitwitProfessions = List.of(BASSIST, BONGOIST, FLAUTIST, GUITARIST);

    private RibbitProfession(String name, ResourceLocation modelLocation, @Nullable SoundEvent insrumentTrack) {
        this.name = name;
        this.modelLocation = modelLocation;
        this.insrumentTrack = insrumentTrack;
        this.id = nextId;

        nextId++;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public ResourceLocation getModelLocation() {
        return this.modelLocation;
    }

    public SoundEvent getInstrumentTrack() {
        return this.insrumentTrack;
    }

    public String toString() {
        return this.name;
    }
}
