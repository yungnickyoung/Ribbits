package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public class TagModuleFabric {
    public static void init() {
        TagModule.HAS_RIBBIT_VILLAGE = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(RibbitsCommon.MOD_ID, "has_ribbit_village"));
    }
}
