package com.yungnickyoung.minecraft.ribbits.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Defines data that determines profession, instrument, and umbrella used by all Ribbits.
 * Each Ribbit defines its own data randomly.
 */
public class RibbitData {
    public static final Codec<RibbitData> CODEC = RecordCodecBuilder.create(
            (data) -> data.group(
                    Codec.INT.fieldOf("profession").orElse(0).forGetter((ribbit) -> ribbit.profession),
                    Codec.INT.fieldOf("instrument").orElse(0).forGetter((ribbit) -> ribbit.instrument),
                    Codec.INT.fieldOf("umbrellaType").orElse(0).forGetter((ribbit) -> ribbit.umbrellaType))
                    .apply(data, RibbitData::new));

    private final int profession;
    private final int instrument;
    private final int umbrellaType;

    public RibbitData(int profession, int instrument, int umbrellaType) {
        this.profession = profession;
        this.instrument = instrument;
        this.umbrellaType = umbrellaType;
    }

    public int getProfession() {
        return this.profession;
    }

    public int getInstrument() {
        return this.instrument;
    }

    public int getUmbrellaType() {
        return this.umbrellaType;
    }

    public RibbitData setProfession(int profession) {
        return new RibbitData(profession, this.instrument, this.umbrellaType);
    }

    public RibbitData setInstrument(int instrument) {
        return new RibbitData(this.profession, instrument, this.umbrellaType);
    }

    public RibbitData setUmbrellaType(int umbrellaType) {
        return new RibbitData(this.profession, this.instrument, umbrellaType);
    }

}
