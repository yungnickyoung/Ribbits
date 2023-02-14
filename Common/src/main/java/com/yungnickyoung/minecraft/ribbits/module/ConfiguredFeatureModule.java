package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.world.feature.config.RibbitsSimplePlantBlockConfig;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegister;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegisterConfiguredFeature;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;

import java.util.List;

@AutoRegister(RibbitsCommon.MOD_ID)
public class ConfiguredFeatureModule {
    @AutoRegister("veg_patch")
    public static final AutoRegisterConfiguredFeature VEG_PATCH = AutoRegisterConfiguredFeature.of(() ->
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
}
