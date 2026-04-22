package com.yungnickyoung.minecraft.ribbits.neoforge.config;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.config.IConfigHelper;
import com.yungnickyoung.minecraft.ribbits.config.RibbitsConfig;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;

public class NeoForgeConfigHelper implements IConfigHelper {
    private static volatile RibbitsConfig config = new RibbitsConfig();

    @Override
    public void init(Object platformContext) {
        if (platformContext instanceof NeoForgeConfigContext(
                ModContainer container, IEventBus modEventBus
        )) {
            container.registerConfig(
                    ModConfig.Type.COMMON,
                    RibbitsConfigNeoForge.SPEC,
                    "ribbits-neoforge-" + RibbitsCommon.MC_VERSION_STRING + ".toml"
            );
            modEventBus.addListener(NeoForgeConfigHelper::onConfigLoading);
            modEventBus.addListener(NeoForgeConfigHelper::onConfigReloading);
            refreshSnapshot();
        }
    }

    @Override
    public RibbitsConfig getConfig() {
        return config;
    }

    private static void onConfigLoading(ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() == RibbitsConfigNeoForge.SPEC) {
            refreshSnapshot();
        }
    }

    private static void onConfigReloading(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == RibbitsConfigNeoForge.SPEC) {
            refreshSnapshot();
        }
    }

    private static void refreshSnapshot() {
        try {
            config = RibbitsConfigNeoForge.snapshot();
        } catch (IllegalStateException ignored) {
        }
    }
}
