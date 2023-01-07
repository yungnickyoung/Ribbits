package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.world.feature.config.RibbitsSimplePlantBlockConfig;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;

import java.util.List;

public class ConfiguredFeatureModule {
    public static final ConfiguredFeature<RandomPatchConfiguration, ?> VEG_PATCH = register("veg_patch",
            new ConfiguredFeature<>(
                    Feature.FLOWER,
                    new RandomPatchConfiguration(
                            16, // tries
                            6, // xz spread
                            2, // y spread
                            PlacementUtils.onlyWhenEmpty(FeatureModule.SIMPLE_PLANT_BLOCK, // PlacedFeature
                                    new RibbitsSimplePlantBlockConfig(
                                            new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                                                    .add(BlockModule.UMBRELLA_LEAF.get().defaultBlockState(), 1)
                                                    .add(BlockModule.TOADSTOOL.get().defaultBlockState(), 3)
                                                    .add(BlockModule.SWAMP_DAISY.get().defaultBlockState(), 1)
                                                    .build()),
                                            List.of(Blocks.PODZOL.defaultBlockState(),
                                                    Blocks.COARSE_DIRT.defaultBlockState()))))));

    private static <FC extends FeatureConfiguration> ConfiguredFeature<FC, ?> register(String key, ConfiguredFeature<FC, ?> feature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(RibbitsCommon.MOD_ID, key), feature);
    }

    public static void bootstrap() {
    }
}
