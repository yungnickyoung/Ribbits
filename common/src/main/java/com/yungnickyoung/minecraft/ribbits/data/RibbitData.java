package com.yungnickyoung.minecraft.ribbits.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yungnickyoung.minecraft.ribbits.module.RibbitInstrumentModule;
import com.yungnickyoung.minecraft.ribbits.module.RibbitProfessionModule;
import com.yungnickyoung.minecraft.ribbits.module.RibbitUmbrellaTypeModule;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

/**
 * Defines data that determines profession, instrument, and umbrella used by a Ribbit.
 */
public class RibbitData {
    public static final Codec<RibbitData> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                    ResourceLocation.CODEC.fieldOf("profession").forGetter(data -> data.profession.id()),
                    ResourceLocation.CODEC.fieldOf("umbrella").forGetter(data -> data.umbrellaType.id()),
                    ResourceLocation.CODEC.fieldOf("instrument").forGetter(data -> data.instrument.id()))
            .apply(instance, instance.stable(RibbitData::new)));

    public static final StreamCodec<FriendlyByteBuf, RibbitData> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, data -> data.profession.id(),
            ResourceLocation.STREAM_CODEC, data -> data.umbrellaType.id(),
            ResourceLocation.STREAM_CODEC, data -> data.instrument.id(),
            RibbitData::new
    );

    private RibbitProfession profession;
    private final RibbitUmbrellaType umbrellaType;
    private RibbitInstrument instrument;

    private RibbitData(ResourceLocation professionId, ResourceLocation umbrellaTypeId, ResourceLocation instrumentId) {
        this.profession = RibbitProfessionModule.getProfession(professionId);
        this.umbrellaType = RibbitUmbrellaTypeModule.getUmbrellaType(umbrellaTypeId);
        this.instrument = RibbitInstrumentModule.getInstrument(instrumentId);
    }

    public RibbitData(RibbitProfession profession, RibbitUmbrellaType umbrellaType, RibbitInstrument instrument) {
        this.profession = profession;
        this.umbrellaType = umbrellaType;
        this.instrument = instrument;
    }

    public RibbitData(FriendlyByteBuf buf) {
        this.profession = RibbitProfessionModule.getProfession(buf.readResourceLocation());
        this.umbrellaType = RibbitUmbrellaTypeModule.getUmbrellaType(buf.readResourceLocation());
        this.instrument = RibbitInstrumentModule.getInstrument(buf.readResourceLocation());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.getProfession().id());
        buf.writeResourceLocation(this.getUmbrellaType().id());
        buf.writeResourceLocation(this.getInstrument().id());
    }

    public RibbitProfession getProfession() {
        return this.profession;
    }

    public void setProfession(RibbitProfession profession) {
        this.profession = profession;
    }

    public RibbitUmbrellaType getUmbrellaType() {
        return this.umbrellaType;
    }

    public RibbitInstrument getInstrument() {
        return this.instrument;
    }

    public void setInstrument(@Nullable RibbitInstrument instrument) {
        this.instrument = instrument;
    }
}
