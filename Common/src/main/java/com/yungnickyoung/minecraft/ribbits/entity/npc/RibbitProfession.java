package com.yungnickyoung.minecraft.ribbits.entity.npc;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import javax.annotation.Nullable;
import java.util.Optional;

public class RibbitProfession {
    public static final Codec<RibbitProfession> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                    ResourceLocation.CODEC.fieldOf("id").forGetter(profession -> profession.id),
                    ResourceLocation.CODEC.fieldOf("model_location").forGetter(profession -> profession.modelLocation),
                    SoundEvent.CODEC.optionalFieldOf("instrument_track").forGetter(profession -> profession.instrumentSoundEvent))
            .apply(instance, instance.stable(RibbitProfession::new)));

    private final ResourceLocation id;
    private final ResourceLocation modelLocation;
    private final Optional<SoundEvent> instrumentSoundEvent;

    private RibbitProfession(ResourceLocation id, ResourceLocation modelLocation, Optional<SoundEvent> instrumentSoundEvent) {
        this.id = id;
        this.modelLocation = modelLocation;
        this.instrumentSoundEvent = instrumentSoundEvent;
    }

    public RibbitProfession(ResourceLocation id, ResourceLocation modelLocation, @Nullable SoundEvent instrumentSoundEvent) {
        this(id, modelLocation, Optional.ofNullable(instrumentSoundEvent));
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public ResourceLocation getModelLocation() {
        return this.modelLocation;
    }

    public Optional<SoundEvent> getInstrumentSoundEvent() {
        return this.instrumentSoundEvent;
    }

    @Override
    public String toString() {
        return this.id.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof RibbitProfession other)) {
            return false;
        } else {
            return this.id.equals(other.getId());
        }
    }
}
