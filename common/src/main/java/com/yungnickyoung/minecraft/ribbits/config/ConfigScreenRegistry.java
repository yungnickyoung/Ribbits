package com.yungnickyoung.minecraft.ribbits.config;

import com.yungnickyoung.minecraft.ribbits.module.ConfigModule;
import dev.architectury.platform.Mod;
import dev.architectury.platform.client.ConfigurationScreenRegistry;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ConfigScreenRegistry {

    public static void registerConfigScreen(Mod mod) {
        ConfigurationScreenRegistry.register(mod, parent ->
                AutoConfig.getConfigScreen(ConfigModule.getConfig().getClass(), parent).get()
        );
    }
}