package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.data.RibbitInstrument;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegister;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

@AutoRegister(RibbitsCommon.MOD_ID)
public class RibbitInstrumentModule {
    /** Map of all Ribbit instrument ResourceLocations to their RibbitInstrument objects. */
    private static final Map<ResourceLocation, RibbitInstrument> INSTRUMENT_REGISTRY = new HashMap<>();

    /** Set of all valid RibbitInstruments. Same as the registry without the dummy NONE instrument. */
    private static final Set<RibbitInstrument> VALID_INSTRUMENTS = new HashSet<>();

    /* Registration of built-in RibbitInstruments. */
    public static final RibbitInstrument NONE = register("none", "", "", null);
    public static final RibbitInstrument BASS = register("bass", "geo/bass_ribbit.geo.json", "play_bass", SoundModule.MUSIC_RIBBIT_BASS.get());
    public static final RibbitInstrument BONGO = register("bongo", "geo/bongo_ribbit.geo.json", "play_bongo", SoundModule.MUSIC_RIBBIT_BONGO.get());
    public static final RibbitInstrument FLUTE = register("flute", "geo/flute_ribbit.geo.json", "play_flute", SoundModule.MUSIC_RIBBIT_FLUTE.get());
    public static final RibbitInstrument GUITAR = register("guitar", "geo/guitar_ribbit.geo.json", "play_guitar", SoundModule.MUSIC_RIBBIT_GUITAR.get());

    /**
     * Registers a RibbitInstrument with the given name, model, and sound event.
     */
    public static RibbitInstrument register(String name, String modelPath, String animationName, SoundEvent instrumentSoundEvent) {
        ResourceLocation id = RibbitsCommon.id(name);
        RibbitInstrument instrument = new RibbitInstrument(id, RibbitsCommon.id(modelPath), animationName, instrumentSoundEvent);
        INSTRUMENT_REGISTRY.put(id, instrument);
        if (!name.equals("none")) VALID_INSTRUMENTS.add(instrument);
        return instrument;
    }

    /**
     * Gets a RibbitInstrument by its ResourceLocation.
     * @param id ResourceLocation of the RibbitInstrument to get.
     * @return RibbitInstrument with the given ResourceLocation, or null if not found.
     */
    public static @Nullable RibbitInstrument getInstrument(ResourceLocation id) {
        return INSTRUMENT_REGISTRY.get(id);
    }

    /**
     * Gets a random RibbitInstrument.
     * @return Random RibbitInstrument.
     */
    public static RibbitInstrument getRandomInstrument() {
        Random random = new Random();
        List<RibbitInstrument> instrumentList = VALID_INSTRUMENTS.stream().toList();
        return instrumentList.get(random.nextInt(instrumentList.size()));
    }

    /**
     * Gets a random RibbitInstrument excluding current band member instruments.
     * @return Random RibbitInstrument.
     */
    public static RibbitInstrument getRandomInstrument(Set<RibbitInstrument> currBandMembers) {
        Random random = new Random();
        List<RibbitInstrument> instrumentList = VALID_INSTRUMENTS.stream().filter(instrument -> !currBandMembers.contains(instrument)).toList();

        if (instrumentList.isEmpty()) return null;

        return instrumentList.get(random.nextInt(instrumentList.size()));
    }

    /**
     * Gets the number of RibbitInstruments.
     * @return number of Valid RibbitInstruments.
     */
    public static int getNumInstruments() {
        return VALID_INSTRUMENTS.size();
    }

    /**
     * The AutoRegister system will call this method after mod initialization is complete.
     * The method itself is a NO-OP, but calling it will trigger the static initialization above.
     */
    @AutoRegister("_ignored")
    public static void initRibbitsInstruments() {
        RibbitsCommon.LOGGER.info("Registering Ribbit instruments...");
    }
}
