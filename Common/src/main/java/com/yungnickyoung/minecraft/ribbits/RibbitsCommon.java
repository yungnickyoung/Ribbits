package com.yungnickyoung.minecraft.ribbits;

import com.yungnickyoung.minecraft.ribbits.module.BlockModule;
import com.yungnickyoung.minecraft.ribbits.module.ConfigModule;
import com.yungnickyoung.minecraft.ribbits.services.Services;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegisterCreativeTabBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RibbitsCommon {
    public static final String MOD_ID = "ribbits";
    public static final String MOD_NAME = "Ribbits";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static final ConfigModule CONFIG = new ConfigModule();

    public static CreativeModeTab TAB_RIBBITS = new AutoRegisterCreativeTabBuilder(new ResourceLocation(RibbitsCommon.MOD_ID, "general"))
            .iconItem(() -> new ItemStack(BlockModule.RED_TOADSTOOL.get()))
            .build();
    public static void init() {
        Services.MODULES.loadCommonModules();
    }
}
