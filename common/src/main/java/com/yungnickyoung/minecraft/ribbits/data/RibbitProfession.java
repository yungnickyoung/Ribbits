package com.yungnickyoung.minecraft.ribbits.data;

import net.minecraft.resources.Identifier;

public record RibbitProfession(Identifier id, Identifier modelLocation) {

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
            return this.id.equals(other.id());
        }
    }
}
