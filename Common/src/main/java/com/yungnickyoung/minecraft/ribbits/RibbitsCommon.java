package com.yungnickyoung.minecraft.ribbits;

import com.yungnickyoung.minecraft.ribbits.module.ConfigModule;
import com.yungnickyoung.minecraft.ribbits.services.Services;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RibbitsCommon {
    public static final String MOD_ID = "ribbits";
    public static final String MOD_NAME = "Ribbits";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static final ConfigModule CONFIG = new ConfigModule();

    public static void init() {
        Services.MODULES.loadModules();
    }
}
