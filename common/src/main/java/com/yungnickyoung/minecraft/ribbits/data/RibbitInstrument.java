package com.yungnickyoung.minecraft.ribbits.data;

import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public record RibbitInstrument(Identifier id, Identifier modelId, String animationName,
                               SoundEvent soundEvent) {

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
            return this.id.equals(other.id());
        }
    }
}
