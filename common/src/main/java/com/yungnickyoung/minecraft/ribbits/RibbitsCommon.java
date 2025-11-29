package com.yungnickyoung.minecraft.ribbits;

import com.yungnickyoung.minecraft.ribbits.module.ConfigModule;
import com.yungnickyoung.minecraft.ribbits.module.NetworkModule;
import com.yungnickyoung.minecraft.yungsapi.api.YungAutoRegister;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RibbitsCommon {
    public static final String MOD_ID = "ribbits";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    // TODO - change this whenever updating to a new Minecraft version
    public static final String MC_VERSION_STRING = "1_21_10";


    public static void init() {
        YungAutoRegister.scanPackageForAnnotations("com.yungnickyoung.minecraft.ribbits");
        ConfigModule.init();
        NetworkModule.init();
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
