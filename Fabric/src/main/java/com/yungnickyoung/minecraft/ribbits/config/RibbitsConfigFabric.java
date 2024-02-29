package com.yungnickyoung.minecraft.ribbits.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name="ribbits-fabric-1_20_1")
public class RibbitsConfigFabric implements ConfigData {
    @ConfigEntry.Category("Ribbits")
    @ConfigEntry.Gui.TransitiveObject
    public ConfigGeneralFabric general = new ConfigGeneralFabric();
}
