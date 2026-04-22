package com.yungnickyoung.minecraft.ribbits.data;

import net.minecraft.resources.Identifier;

public record RibbitUmbrellaType(Identifier id, String modelLocationSuffix) {

    @Override
    public String toString() {
        return this.id.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof RibbitUmbrellaType other)) {
            return false;
        } else {
            return this.id.equals(other.id());
        }
    }
}
