package com.yungnickyoung.minecraft.ribbits.module;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.HashMap;
import java.util.Map;

public class StructureFeatureModuleForge {
    public static Map<ResourceLocation, StructureFeature<? extends FeatureConfiguration>> STRUCTURE_FEATURES = new HashMap<>();

    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(StructureFeatureModuleForge::commonSetup);
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> STRUCTURE_FEATURES.forEach((name, structurePieceType) -> Registry.register(Registry.STRUCTURE_FEATURE, name, structurePieceType)));
    }
}
