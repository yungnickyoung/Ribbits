package com.yungnickyoung.minecraft.ribbits.world.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.List;

public record RibbitsSimplePlantBlockConfig(
        BlockStateProvider toPlace,
        List<BlockState> cannotPlaceOn
) implements FeatureConfiguration {
    public static final Codec<RibbitsSimplePlantBlockConfig> CODEC = RecordCodecBuilder.create((instance) -> instance
            .group(
                    BlockStateProvider.CODEC.fieldOf("to_place").forGetter((config) -> config.toPlace),
                    BlockState.CODEC.listOf().fieldOf("cannot_place_on").forGetter((config) -> config.cannotPlaceOn))
            .apply(instance, RibbitsSimplePlantBlockConfig::new));
}

