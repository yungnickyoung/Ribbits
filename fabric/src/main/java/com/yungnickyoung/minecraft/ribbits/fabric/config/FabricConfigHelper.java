package com.yungnickyoung.minecraft.ribbits.fabric.config;

import com.yungnickyoung.minecraft.ribbits.config.IConfigHelper;
import com.yungnickyoung.minecraft.ribbits.config.RibbitsConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.minecraft.world.InteractionResult;

public class FabricConfigHelper implements IConfigHelper {
    private static RibbitsConfig config;
    private static ConfigHolder<RibbitsConfigFabric> configHolder;

    @Override
    public void init(Object platformContext) {
        AutoConfig.register(RibbitsConfigFabric.class, Toml4jConfigSerializer::new);
        configHolder = AutoConfig.getConfigHolder(RibbitsConfigFabric.class);
        configHolder.registerSaveListener((holder, newConfig) -> {
            syncFromFabricConfig(newConfig);
            return InteractionResult.SUCCESS;
        });
        configHolder.registerLoadListener((holder, newConfig) -> {
            syncFromFabricConfig(newConfig);
            return InteractionResult.SUCCESS;
        });

        syncFromFabricConfig(configHolder.getConfig());
    }

    @Override
    public RibbitsConfig getConfig() {
        return config;
    }

    public static RibbitsConfigFabric getFabricConfig() {
        return configHolder.getConfig();
    }

    public static void save() {
        if (configHolder != null) configHolder.save();
    }

    private static void syncFromFabricConfig(RibbitsConfigFabric fabricConfig) {
        RibbitsConfig newConfig = new RibbitsConfig();
        newConfig.general.prideFlagAllYear = fabricConfig.general.prideFlagAllYear;
        newConfig.network.proxyHost = fabricConfig.network.proxyHost;
        newConfig.network.proxyPort = fabricConfig.network.proxyPort;
        newConfig.network.proxyUsername = fabricConfig.network.proxyUsername;
        newConfig.network.proxyPassword = fabricConfig.network.proxyPassword;
        config = newConfig;
    }
}
