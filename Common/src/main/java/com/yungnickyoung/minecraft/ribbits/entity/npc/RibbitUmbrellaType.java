package com.yungnickyoung.minecraft.ribbits.entity.npc;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

public class RibbitUmbrellaType {
    public static final Codec<RibbitUmbrellaType> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                    ResourceLocation.CODEC.fieldOf("id").forGetter(umbrellaType -> umbrellaType.id),
                    ResourceLocation.CODEC.fieldOf("model_location").forGetter(umbrellaType -> umbrellaType.modelLocation))
            .apply(instance, instance.stable(RibbitUmbrellaType::new)));

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
