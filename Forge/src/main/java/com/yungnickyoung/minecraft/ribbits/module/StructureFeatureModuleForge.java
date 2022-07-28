package com.yungnickyoung.minecraft.ribbits.module;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.HashMap;
import java.util.Map;

public class StructureFeatureModuleForge {
    public static Map<ResourceLocation, StructureFeature<? extends FeatureConfiguration>> STRUCTURE_FEATURES = new HashMap<>();

    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(StructureFeature.class, StructureFeatureModuleForge::registerStructures);
    }

    private static void registerStructures(RegistryEvent.Register<StructureFeature<?>> event) {
        IForgeRegistry<StructureFeature<?>> registry = event.getRegistry();
        STRUCTURE_FEATURES.forEach((name, structureFeature) -> register(registry, name, structureFeature));
    }

    private static void register(IForgeRegistry<StructureFeature<?>> registry, ResourceLocation name, StructureFeature<? extends FeatureConfiguration> structureFeature) {
        structureFeature.setRegistryName(name);
        registry.register(structureFeature);
    }
}
