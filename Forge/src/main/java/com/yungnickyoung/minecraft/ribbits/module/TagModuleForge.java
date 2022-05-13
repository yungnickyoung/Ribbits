package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class TagModuleForge {
    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(TagModuleForge::commonSetup);
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            TagModule.HAS_RIBBIT_VILLAGE = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(RibbitsCommon.MOD_ID, "has_ribbit_village"));
        });
    }
}
