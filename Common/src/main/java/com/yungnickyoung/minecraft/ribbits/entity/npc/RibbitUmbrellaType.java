package com.yungnickyoung.minecraft.ribbits.entity.npc;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import net.minecraft.resources.ResourceLocation;

public class RibbitUmbrellaType {
    public static final RibbitUmbrellaType UMBRELLA_1 = new RibbitUmbrellaType(new ResourceLocation(RibbitsCommon.MOD_ID, "geo/umbrella_ribbit_1.geo.json"));
    public static final RibbitUmbrellaType UMBRELLA_2 = new RibbitUmbrellaType(new ResourceLocation(RibbitsCommon.MOD_ID, "geo/umbrella_ribbit_2.geo.json"));
    public static final RibbitUmbrellaType UMBRELLA_3 = new RibbitUmbrellaType(new ResourceLocation(RibbitsCommon.MOD_ID, "geo/umbrella_ribbit_3.geo.json"));

    private final ResourceLocation modelLocation;
    private final int id;

    private static int nextId = 0;

    private RibbitUmbrellaType(ResourceLocation modelLocation) {
        this.modelLocation = modelLocation;
        this.id = nextId;

        nextId++;
    }

    public int getId() {
        return this.id;
    }

    public ResourceLocation getModelLocation() {
        return this.modelLocation;
    }
}
