package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.config.IConfigHelper;
import com.yungnickyoung.minecraft.ribbits.config.RibbitsConfig;

import java.util.ServiceLoader;

public class ConfigModule {
    private static final IConfigHelper SERVICE =
            ServiceLoader.load(IConfigHelper.class).findFirst().orElseThrow();

    private static boolean initialized = false;

    public static void init(Object platformContext) {
        if (initialized) return;
        initialized = true;
        SERVICE.init(platformContext);
    }

    public static RibbitsConfig getConfig() {
        RibbitsConfig config = SERVICE.getConfig();
        return config != null ? config : new RibbitsConfig();
    }
}
