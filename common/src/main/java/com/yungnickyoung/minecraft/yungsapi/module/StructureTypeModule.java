package com.yungnickyoung.minecraft.yungsapi.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.yungsapi.world.structure.YungJigsawStructure;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;

public class StructureTypeModule {
    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES =
            DeferredRegister.create(RibbitsCommon.MOD_ID, Registries.STRUCTURE_TYPE);

    public static final RegistrySupplier<StructureType<YungJigsawStructure>> YUNG_JIGSAW =
            STRUCTURE_TYPES.register("yung_jigsaw",
                    () -> () -> YungJigsawStructure.CODEC);

    public static void register() {
        STRUCTURE_TYPES.register();
    }
}