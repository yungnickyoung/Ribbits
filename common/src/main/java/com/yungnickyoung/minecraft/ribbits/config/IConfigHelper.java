package com.yungnickyoung.minecraft.ribbits.config;

/**
 * Loader-specific config backend.
 * <p>
 * Implementations live in the loader projects and are discovered via {@link java.util.ServiceLoader}.
 */
public interface IConfigHelper {
    /**
     * Initialize the config system for the current loader.
     * <p>
     * NeoForge may receive a {@code ModContainer} instance as {@code platformContext};
     * Fabric will typically receive {@code null}.
     */
    void init(Object platformContext);

    RibbitsConfig getConfig();
}

