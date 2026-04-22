package com.yungnickyoung.minecraft.ribbits.config;

/**
 * Common config data model (loader-agnostic).
 * <p>
 * Fabric persists this via Cloth/AutoConfig; NeoForge persists this via NeoForge's config system.
 */
public class RibbitsConfig {
    public General general = new General();

    public Network network = new Network();

    public static class General {
        public boolean prideFlagAllYear = false;
    }

    public static class Network {
        public String proxyHost = "";

        public int proxyPort = 8080;

        public String proxyUsername = "";

        public String proxyPassword = "";
    }
}
