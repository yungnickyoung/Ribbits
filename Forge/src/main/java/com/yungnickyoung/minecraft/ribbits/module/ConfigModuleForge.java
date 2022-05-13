package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.config.RibbitsConfigForge;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ConfigModuleForge {
    public static final String CUSTOM_CONFIG_PATH = "ribbits";
    public static final String VERSION_PATH = "forge-1_18_2";

    public static void init() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, RibbitsConfigForge.SPEC, "ribbits-forge-1_18_2.toml");
        MinecraftForge.EVENT_BUS.addListener(ConfigModuleForge::onWorldLoad);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ConfigModuleForge::onConfigChange);
    }

    private static void onWorldLoad(WorldEvent.Load event) {
        bakeConfig();
    }

    private static void onConfigChange(ModConfigEvent event) {
        if (event.getConfig().getSpec() == RibbitsConfigForge.SPEC) {
            bakeConfig();
        }
    }

    private static void bakeConfig() {
        // TODO
    }
}
