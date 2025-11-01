package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.data.RibbitInstrument;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class RibbitInstrumentModule {
    /**
     * Map of all Ribbit instrument ResourceLocations to their RibbitInstrument objects.
     */
    private static final Map<ResourceLocation, RibbitInstrument> INSTRUMENT_REGISTRY = new HashMap<>();

    /**
     * Set of all valid RibbitInstruments. Same as the registry without the dummy NONE instrument.
     */
    private static final Set<RibbitInstrument> VALID_INSTRUMENTS = new HashSet<>();

    /* Registration of built-in RibbitInstruments. */
    public static final RibbitInstrument NONE = register("none", "", "", null);
    // Defer these: created when sound events are ready
    public static RibbitInstrument BASS;
    public static RibbitInstrument BONGO;
    public static RibbitInstrument FLUTE;
    public static RibbitInstrument GUITAR;

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
     *
     * @param id ResourceLocation of the RibbitInstrument to get.
     * @return RibbitInstrument with the given ResourceLocation, or null if not found.
     */
    public static @Nullable RibbitInstrument getInstrument(ResourceLocation id) {
        return INSTRUMENT_REGISTRY.get(id);
    }

    /**
     * Gets a random RibbitInstrument.
     *
     * @return Random RibbitInstrument.
     */
    public static RibbitInstrument getRandomInstrument() {
        Random random = new Random();
        List<RibbitInstrument> instrumentList = VALID_INSTRUMENTS.stream().toList();
        if (instrumentList.isEmpty()) return NONE; // guard while registry suppliers resolve
        return instrumentList.get(random.nextInt(instrumentList.size()));
    }

    /**
     * Gets a random RibbitInstrument excluding current band member instruments.
     *
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
     *
     * @return number of Valid RibbitInstruments.
     */
    public static int getNumInstruments() {
        return VALID_INSTRUMENTS.size();
    }

    /**
     * The AutoRegister system will call this method after mod initialization is complete.
     * The method itself is a NO-OP, but calling it will trigger the static initialization above.
     */
    public static void init() {
        RibbitsCommon.LOGGER.info("Registering Ribbit instruments...");

        // Defer instrument creation until sound events exist
        SoundModule.MUSIC_RIBBIT_BASS.listen(evt ->
                BASS = register("bass", "bass_ribbit", "play_bass", evt));
        SoundModule.MUSIC_RIBBIT_BONGO.listen(evt ->
                BONGO = register("bongo", "bongo_ribbit", "play_bongo", evt));
        SoundModule.MUSIC_RIBBIT_FLUTE.listen(evt ->
                FLUTE = register("flute", "flute_ribbit", "play_flute", evt));
        SoundModule.MUSIC_RIBBIT_GUITAR.listen(evt ->
                GUITAR = register("guitar", "guitar_ribbit", "play_guitar", evt));
    }
}
