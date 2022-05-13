package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.world.structure.RibbitVillage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;

public class StructureFeatureModuleForge {
    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(StructureFeature.class, StructureFeatureModuleForge::registerStructures);
    }

    private static void registerStructures(RegistryEvent.Register<StructureFeature<?>> event) {
        IForgeRegistry<StructureFeature<?>> registry = event.getRegistry();
        StructureFeatureModule.RIBBIT_VILLAGE = register(registry, "ribbit_village", new RibbitVillage());
    }

    private static <FC extends FeatureConfiguration> StructureFeature<FC> register(IForgeRegistry<StructureFeature<?>> registry, String name, StructureFeature<FC> structureFeature) {
        structureFeature.setRegistryName(new ResourceLocation(RibbitsCommon.MOD_ID, name));
        registry.register(structureFeature);
        return structureFeature;
    }
}
