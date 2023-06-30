package com.yungnickyoung.minecraft.ribbits.world.structure;

import com.yungnickyoung.minecraft.yungsapi.api.YungJigsawConfig;
import com.yungnickyoung.minecraft.yungsapi.api.YungJigsawManager;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;

import java.util.Optional;

public class RibbitVillageStructure extends StructureFeature<YungJigsawConfig> {
    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    public RibbitVillageStructure() {
        super(YungJigsawConfig.CODEC, ctx -> {
            // Only generate if location is valid
            if (!checkLocation(ctx)) {
                return Optional.empty();
            }

            BlockPos startPos = new BlockPos(ctx.chunkPos().getMiddleBlockX(), 0, ctx.chunkPos().getMiddleBlockZ());
            return YungJigsawManager.assembleJigsawStructure(
                    ctx,
                    PoolElementStructurePiece::new,
                    startPos,
                    true,
                    true,
                    80);
        });
    }

    private static boolean checkLocation(PieceGeneratorSupplier.Context<YungJigsawConfig> ctx) {
        // Check for any avoided structures nearby
        for (ResourceKey<StructureSet> structureSetToAvoid : ctx.config().getStructureSetAvoid()) {
            if (ctx.chunkGenerator().hasFeatureChunkInRange(structureSetToAvoid, ctx.seed(), ctx.chunkPos().x, ctx.chunkPos().z, ctx.config().getStructureAvoidRadius())) {
                return false;
            }
        }
        return true;
    }
}
