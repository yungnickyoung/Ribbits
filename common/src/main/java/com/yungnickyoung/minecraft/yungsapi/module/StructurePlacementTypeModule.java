package com.yungnickyoung.minecraft.yungsapi.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.yungsapi.world.structure.placement.EnhancedRandomSpread;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;

public class StructurePlacementTypeModule {
    public static final DeferredRegister<StructurePlacementType<?>> STRUCTURE_PLACEMENT_TYPES =
            DeferredRegister.create(RibbitsCommon.MOD_ID, Registries.STRUCTURE_PLACEMENT);
    public static final RegistrySupplier<StructurePlacementType<EnhancedRandomSpread>> ENHANCED_RANDOM_SPREAD =
            STRUCTURE_PLACEMENT_TYPES.register("enhanced_random_spread",
                    () -> () -> EnhancedRandomSpread.CODEC);

    public static void register() {
        STRUCTURE_PLACEMENT_TYPES.register();
    }
}
