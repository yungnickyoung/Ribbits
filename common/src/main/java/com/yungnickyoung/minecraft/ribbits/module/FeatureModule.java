package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.world.feature.RibbitsVegetationBlockFeature;
import com.yungnickyoung.minecraft.ribbits.world.feature.RibbitsVegetationFeatureConfig;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;

public class FeatureModule {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(RibbitsCommon.MOD_ID, Registries.FEATURE);

    public static final RibbitsVegetationBlockFeature RIBBITS_VEGETATION_FEATURE = new RibbitsVegetationBlockFeature();
    public static final RegistrySupplier<Feature<RibbitsVegetationFeatureConfig>> RIBBITS_VEGETATION_FEATURE_REG =
            FEATURES.register("vegetation_block_feature", () -> RIBBITS_VEGETATION_FEATURE);

    public static void init() {
        FEATURES.register();
    }
}
