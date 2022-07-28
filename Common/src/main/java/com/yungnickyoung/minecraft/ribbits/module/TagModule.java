package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class TagModule {
    public static TagKey<Biome> HAS_RIBBIT_VILLAGE = TagKey.create(Registry.BIOME_REGISTRY,
            new ResourceLocation(RibbitsCommon.MOD_ID, "has_structure/ribbit_village"));
}
