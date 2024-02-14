package com.yungnickyoung.minecraft.ribbits.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class RibbitInstrument {
    private final ResourceLocation id;
    private final ResourceLocation modelId;
    private final String animationName;
    private final SoundEvent soundEvent;

    public RibbitInstrument(ResourceLocation id, ResourceLocation modelId, String animationName, SoundEvent soundEvent) {
        this.id = id;
        this.modelId = modelId;
        this.animationName = animationName;
        this.soundEvent = soundEvent;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public ResourceLocation getModelId() {
        return this.modelId;
    }

    public String getAnimationName() {
        return this.animationName;
    }

    public SoundEvent getSoundEvent() {
        return this.soundEvent;
    }

    @Override
    public String toString() {
        return this.id.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof RibbitInstrument other)) {
            return false;
        } else {
            return this.id.equals(other.getId());
        }
    }
}
