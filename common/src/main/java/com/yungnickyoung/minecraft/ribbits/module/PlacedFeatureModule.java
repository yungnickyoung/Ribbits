package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

@AutoRegister(RibbitsCommon.MOD_ID)
public class PlacedFeatureModule {
    public static final ResourceKey<PlacedFeature> SWAMP_DAISY_PATCH = ResourceKey.create(Registries.PLACED_FEATURE, RibbitsCommon.id("swamp_daisy_patch"));
    public static final ResourceKey<PlacedFeature> TOADSTOOL_PATCH = ResourceKey.create(Registries.PLACED_FEATURE, RibbitsCommon.id("toadstool_patch"));
    public static final ResourceKey<PlacedFeature> UMBRELLA_LEAF_PATCH = ResourceKey.create(Registries.PLACED_FEATURE, RibbitsCommon.id("umbrella_leaf_patch"));
    public static final ResourceKey<PlacedFeature> GIANT_LILYPAD_PATCH = ResourceKey.create(Registries.PLACED_FEATURE, RibbitsCommon.id("giant_lilypad_patch"));
}
