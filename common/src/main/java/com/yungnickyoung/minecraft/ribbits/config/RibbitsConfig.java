package com.yungnickyoung.minecraft.ribbits.config;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "ribbits-" + RibbitsCommon.MC_VERSION_STRING)
public class RibbitsConfig implements ConfigData {

    @ConfigEntry.Gui.CollapsibleObject
    public General general = new General();

    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip
    public Network network = new Network();

    public static class General {
        @ConfigEntry.Gui.Tooltip
        public boolean prideFlagAllYear = false;
    }

    public static class Network {
        @ConfigEntry.Gui.Tooltip
        public String proxyHost = "";

        @ConfigEntry.Gui.Tooltip
        public int proxyPort = 8080;

        @ConfigEntry.Gui.Tooltip
        public String proxyUsername = "";

        @ConfigEntry.Gui.Tooltip
        public String proxyPassword = "";
    }
}