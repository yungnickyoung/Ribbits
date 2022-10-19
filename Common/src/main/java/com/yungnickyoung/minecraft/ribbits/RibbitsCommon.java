package com.yungnickyoung.minecraft.ribbits;

import com.yungnickyoung.minecraft.ribbits.module.BlockModule;
import com.yungnickyoung.minecraft.ribbits.module.ConfigModule;
import com.yungnickyoung.minecraft.ribbits.services.Services;
import com.yungnickyoung.minecraft.yungsapi.api.YungAutoRegister;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegister;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegisterCreativeTab;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@AutoRegister(RibbitsCommon.MOD_ID)
public class RibbitsCommon {
    public static final String MOD_ID = "ribbits";
    public static final String MOD_NAME = "Ribbits";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static final ConfigModule CONFIG = new ConfigModule();

    @AutoRegister("general")
    public static AutoRegisterCreativeTab TAB_RIBBITS = new AutoRegisterCreativeTab.Builder()
            .iconItem(() -> new ItemStack(BlockModule.RED_TOADSTOOL.get()))
            .build();
    public static void init() {
        YungAutoRegister.scanPackageForAnnotations("com.yungnickyoung.minecraft.ribbits");
        Services.MODULES.loadCommonModules();
    }
}
