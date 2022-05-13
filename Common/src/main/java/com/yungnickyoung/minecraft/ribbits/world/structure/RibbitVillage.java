package com.yungnickyoung.minecraft.ribbits.world.structure;

import com.yungnickyoung.minecraft.yungsapi.api.YungJigsawConfig;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.StructureFeature;

import java.util.Optional;

public class RibbitVillage extends StructureFeature<YungJigsawConfig> {
    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    public RibbitVillage() {
        super(YungJigsawConfig.CODEC, context -> {
            // TODO
            return Optional.empty();
        });
    }
}
