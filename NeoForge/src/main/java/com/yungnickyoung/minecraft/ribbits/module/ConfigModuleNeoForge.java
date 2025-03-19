package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.config.RibbitsConfigNeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;

public class ConfigModuleNeoForge {
    public static void init(IEventBus eventBus, ModContainer container) {
        container.registerConfig(ModConfig.Type.COMMON, RibbitsConfigNeoForge.SPEC, "ribbits-neoforge-" + RibbitsCommon.MC_VERSION_STRING + ".toml");
        NeoForge.EVENT_BUS.addListener(ConfigModuleNeoForge::onWorldLoad);
        eventBus.addListener(ConfigModuleNeoForge::onConfigChange);
    }

    private static void onWorldLoad(LevelEvent.Load event) {
        bakeConfig();
    }

    private static void onConfigChange(ModConfigEvent event) {
        if (event.getConfig().getSpec() == RibbitsConfigNeoForge.SPEC) {
            bakeConfig();
        }
    }

    private static void bakeConfig() {
        RibbitsCommon.CONFIG.prideFlagAllYear = RibbitsConfigNeoForge.general.prideFlagAllYear.get();
    }
}
