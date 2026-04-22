package com.yungnickyoung.minecraft.ribbits.fabric.compat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import com.yungnickyoung.minecraft.ribbits.fabric.config.RibbitsConfigFabric;
import me.shedaniel.autoconfig.AutoConfigClient;

public class ModMenuCompat implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> AutoConfigClient.getConfigScreen(RibbitsConfigFabric.class, parent).get();
    }

}
