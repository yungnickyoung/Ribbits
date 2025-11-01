package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.config.RibbitsConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.minecraft.world.InteractionResult;

public class ConfigModule {
    private static RibbitsConfig config;
    private static ConfigHolder<RibbitsConfig> configHolder;

    public static void init() {
        AutoConfig.register(RibbitsConfig.class, Toml4jConfigSerializer::new);
        configHolder = AutoConfig.getConfigHolder(RibbitsConfig.class);
        configHolder.registerSaveListener(ConfigModule::onConfigSave);
        configHolder.registerLoadListener(ConfigModule::onConfigLoad);
        config = configHolder.get();
    }

    private static InteractionResult onConfigSave(ConfigHolder<RibbitsConfig> holder, RibbitsConfig newConfig) {
        config = newConfig;
        return InteractionResult.SUCCESS;
    }

    private static InteractionResult onConfigLoad(ConfigHolder<RibbitsConfig> holder, RibbitsConfig newConfig) {
        config = newConfig;
        return InteractionResult.SUCCESS;
    }

    public static RibbitsConfig getConfig() {
        return config;
    }

    public static ConfigHolder<RibbitsConfig> getConfigHolder() {
        return configHolder;
    }

    public boolean prideFlagAllYear() {
        return config != null && config.general.prideFlagAllYear;
    }
}