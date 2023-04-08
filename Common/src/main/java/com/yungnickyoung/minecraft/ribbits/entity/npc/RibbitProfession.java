package com.yungnickyoung.minecraft.ribbits.entity.npc;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import net.minecraft.resources.ResourceLocation;

public class RibbitProfession {
    public static final RibbitProfession GARDENER = new RibbitProfession("gardener", new ResourceLocation(RibbitsCommon.MOD_ID, "geo/gardener_ribbit.geo.json"));
    public static final RibbitProfession SORCERER = new RibbitProfession("sorcerer", new ResourceLocation(RibbitsCommon.MOD_ID, "geo/sorcerer_ribbit.geo.json"));
    public static final RibbitProfession BASSIST = new RibbitProfession("bassist", new ResourceLocation(RibbitsCommon.MOD_ID, "geo/bass_ribbit.geo.json"));
    public static final RibbitProfession BONGOIST = new RibbitProfession("bongoist", new ResourceLocation(RibbitsCommon.MOD_ID, "geo/bongo_ribbit.geo.json"));
    public static final RibbitProfession FLAUTIST = new RibbitProfession("flautist", new ResourceLocation(RibbitsCommon.MOD_ID, "geo/flute_ribbit.geo.json"));
    public static final RibbitProfession GUITARIST = new RibbitProfession("guitarist", new ResourceLocation(RibbitsCommon.MOD_ID, "geo/guitar_ribbit.geo.json"));

    private final String name;
    private final ResourceLocation modelLocation;
    private final int id;

    private static int nextId = 0;

    private RibbitProfession(String name, ResourceLocation modelLocation) {
        this.name = name;
        this.modelLocation = modelLocation;
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

    public String toString() {
        return this.name;
    }
}
