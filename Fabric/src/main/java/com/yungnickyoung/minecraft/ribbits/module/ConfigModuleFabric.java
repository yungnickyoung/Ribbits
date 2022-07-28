package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.config.RibbitsConfigFabric;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.minecraft.world.InteractionResult;

public class ConfigModuleFabric {
    public static void init() {
        AutoConfig.register(RibbitsConfigFabric.class, Toml4jConfigSerializer::new);
        AutoConfig.getConfigHolder(RibbitsConfigFabric.class).registerSaveListener(ConfigModuleFabric::bakeConfig);
        AutoConfig.getConfigHolder(RibbitsConfigFabric.class).registerLoadListener(ConfigModuleFabric::bakeConfig);
        bakeConfig(AutoConfig.getConfigHolder(RibbitsConfigFabric.class).get());
    }

    private static InteractionResult bakeConfig(ConfigHolder<RibbitsConfigFabric> configHolder, RibbitsConfigFabric configFabric) {
        bakeConfig(configFabric);
        return InteractionResult.SUCCESS;
    }

    private static void bakeConfig(RibbitsConfigFabric configFabric) {
        // TODO
    }
}
