package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.entity.npc.RibbitProfession;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegister;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

@AutoRegister(RibbitsCommon.MOD_ID)
public class RibbitProfessionModule {
    /** Map of all Ribbit profession ResourceLocations to their RibbitProfession objects. */
    private static final Map<ResourceLocation, RibbitProfession> PROFESSION_REGISTRY = new HashMap<>();

    /* Registration of built-in RibbitProfessions. */
    public static final RibbitProfession GARDENER = register("gardener", "geo/gardener_ribbit.geo.json", null);
    public static final RibbitProfession SORCERER = register("sorcerer", "geo/sorcerer_ribbit.geo.json", null);
    public static final RibbitProfession BASSIST = register("bassist", "geo/bass_ribbit.geo.json", SoundModule.MUSIC_RIBBIT_BASS.get());
    public static final RibbitProfession BONGOIST = register("bongoist", "geo/bongo_ribbit.geo.json", SoundModule.MUSIC_RIBBIT_BONGO.get());
    public static final RibbitProfession FLAUTIST = register("flautist", "geo/flute_ribbit.geo.json", SoundModule.MUSIC_RIBBIT_FLUTE.get());
    public static final RibbitProfession GUITARIST = register("guitarist", "geo/guitar_ribbit.geo.json", SoundModule.MUSIC_RIBBIT_GUITAR.get());

    private static final Set<RibbitProfession> NITWIT_PROFESSIONS = Set.of(BASSIST, BONGOIST, FLAUTIST, GUITARIST);

    /**
     * Registers a RibbitProfession with the given name and model path.
     * An optional SoundEvent can be provided if the Ribbit is meant to play music.
     */
    public static RibbitProfession register(String name, String modelPath, @Nullable SoundEvent instrumentSoundEvent) {
        ResourceLocation id = RibbitsCommon.id(name);
        RibbitProfession profession = new RibbitProfession(id, RibbitsCommon.id(modelPath), instrumentSoundEvent);
        PROFESSION_REGISTRY.put(id, profession);
        return profession;
    }

    /**
     * Gets a RibbitProfession by its ResourceLocation.
     * @param id ResourceLocation of the RibbitProfession to get.
     * @return RibbitProfession with the given ResourceLocation, or null if not found.
     */
    public static @Nullable RibbitProfession getProfession(ResourceLocation id) {
        return PROFESSION_REGISTRY.get(id);
    }

    /**
     * Gets a random RibbitProfession.
     * @return Random RibbitProfession.
     */
    public static RibbitProfession getRandomProfession() {
        Random random = new Random();
        List<RibbitProfession> professionList = PROFESSION_REGISTRY.values().stream().toList();
        return professionList.get(random.nextInt(professionList.size()));
    }

    /**
     * Returns true if the given RibbitProfession is a nitwit.
     * @param profession RibbitProfession to check.
     * @return True if the given RibbitProfession is a nitwit.
     */
    public static boolean isNitwit(RibbitProfession profession) {
        return NITWIT_PROFESSIONS.contains(profession);
    }

    /**
     * The AutoRegister system will call this method after mod initialization is complete.
     * The method itself is a NO-OP, but calling it will trigger the static initialization above.
     */
    @AutoRegister("_ignored")
    public static void initRibbitsProfessions() {
        RibbitsCommon.LOGGER.info("Registering Ribbit professions...");
    }
}
