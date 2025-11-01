package com.yungnickyoung.minecraft.yungsapi.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.yungsapi.world.structure.jigsaw.element.*;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;

public class StructurePoolElementTypeModule {
    public static final DeferredRegister<StructurePoolElementType<?>> STRUCTURE_POOL_ELEMENT_TYPES =
            DeferredRegister.create(RibbitsCommon.MOD_ID, Registries.STRUCTURE_POOL_ELEMENT);

    public static final RegistrySupplier<StructurePoolElementType<MaxCountSinglePoolElement>> MAX_COUNT_SINGLE_ELEMENT =
            STRUCTURE_POOL_ELEMENT_TYPES.register("max_count_single_element",
                    () -> () -> MaxCountSinglePoolElement.CODEC);

    public static final RegistrySupplier<StructurePoolElementType<MaxCountLegacySinglePoolElement>> MAX_COUNT_LEGACY_SINGLE_ELEMENT =
            STRUCTURE_POOL_ELEMENT_TYPES.register("max_count_legacy_single_element",
                    () -> () -> MaxCountLegacySinglePoolElement.CODEC);

    public static final RegistrySupplier<StructurePoolElementType<MaxCountFeaturePoolElement>> MAX_COUNT_FEATURE_ELEMENT =
            STRUCTURE_POOL_ELEMENT_TYPES.register("max_count_feature_element",
                    () -> () -> MaxCountFeaturePoolElement.CODEC);

    public static final RegistrySupplier<StructurePoolElementType<MaxCountListPoolElement>> MAX_COUNT_LIST_ELEMENT =
            STRUCTURE_POOL_ELEMENT_TYPES.register("max_count_list_element",
                    () -> () -> MaxCountListPoolElement.CODEC);

    public static final RegistrySupplier<StructurePoolElementType<YungJigsawSinglePoolElement>> YUNG_SINGLE_ELEMENT =
            STRUCTURE_POOL_ELEMENT_TYPES.register("yung_single_element",
                    () -> () -> YungJigsawSinglePoolElement.CODEC);

    public static final RegistrySupplier<StructurePoolElementType<YungJigsawFeatureElement>> YUNG_FEATURE_ELEMENT =
            STRUCTURE_POOL_ELEMENT_TYPES.register("yung_feature_element",
                    () -> () -> YungJigsawFeatureElement.CODEC);

    public static void register() {
        STRUCTURE_POOL_ELEMENT_TYPES.register();
    }
}