package com.yungnickyoung.minecraft.ribbits.data;

import net.minecraft.resources.ResourceLocation;

public class RibbitUmbrellaType {
    private final ResourceLocation id;
    private final ResourceLocation modelLocation;

    public RibbitUmbrellaType(ResourceLocation id, ResourceLocation modelLocation) {
        this.id = id;
        this.modelLocation = modelLocation;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public ResourceLocation getModelLocation() {
        return this.modelLocation;
    }

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
            return this.id.equals(other.getId());
        }
    }
}
