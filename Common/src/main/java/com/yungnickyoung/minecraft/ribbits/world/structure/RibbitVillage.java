package com.yungnickyoung.minecraft.ribbits.world.structure;

import com.yungnickyoung.minecraft.yungsapi.api.YungJigsawConfig;
import com.yungnickyoung.minecraft.yungsapi.api.YungJigsawManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;

public class RibbitVillage extends StructureFeature<YungJigsawConfig> {
    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    public RibbitVillage() {
        super(YungJigsawConfig.CODEC, context -> {
            BlockPos startPos = new BlockPos(context.chunkPos().getMiddleBlockX(), 0, context.chunkPos().getMiddleBlockZ());
            return YungJigsawManager.assembleJigsawStructure(
                    context,
                    PoolElementStructurePiece::new,
                    startPos,
                    true,
                    true,
                    80);
        });
    }
}
