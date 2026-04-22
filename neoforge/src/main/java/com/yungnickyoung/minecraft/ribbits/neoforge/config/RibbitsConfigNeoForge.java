package com.yungnickyoung.minecraft.ribbits.neoforge.config;

import com.yungnickyoung.minecraft.ribbits.config.RibbitsConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class RibbitsConfigNeoForge {
    public static final ModConfigSpec SPEC;

    private static final ModConfigSpec.BooleanValue PRIDE_FLAG_ALL_YEAR;

    private static final ModConfigSpec.ConfigValue<String> PROXY_HOST;
    private static final ModConfigSpec.IntValue PROXY_PORT;
    private static final ModConfigSpec.ConfigValue<String> PROXY_USERNAME;
    private static final ModConfigSpec.ConfigValue<String> PROXY_PASSWORD;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.push("general");
        PRIDE_FLAG_ALL_YEAR = builder
                .comment("If enabled, Pride Ribbits will exist all year instead of only in June.")
                .define("prideFlagAllYear", false);
        builder.pop();

        builder.push("network");
        PROXY_HOST = builder
                .comment("Proxy server hostname or IP (empty disables proxy).")
                .define("proxyHost", "");
        PROXY_PORT = builder
                .comment("Proxy server port (default: 8080).")
                .defineInRange("proxyPort", 8080, 0, 65535);
        PROXY_USERNAME = builder
                .comment("Proxy username (empty disables proxy auth).")
                .define("proxyUsername", "");
        PROXY_PASSWORD = builder
                .comment("Proxy password.")
                .define("proxyPassword", "");
        builder.pop();

        SPEC = builder.build();
    }

    private RibbitsConfigNeoForge() {
    }

    public static RibbitsConfig snapshot() {
        RibbitsConfig config = new RibbitsConfig();
        config.general.prideFlagAllYear = PRIDE_FLAG_ALL_YEAR.get();
        config.network.proxyHost = PROXY_HOST.get();
        config.network.proxyPort = PROXY_PORT.get();
        config.network.proxyUsername = PROXY_USERNAME.get();
        config.network.proxyPassword = PROXY_PASSWORD.get();
        return config;
    }
}

