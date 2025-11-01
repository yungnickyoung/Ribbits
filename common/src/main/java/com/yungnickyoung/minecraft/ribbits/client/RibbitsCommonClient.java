package com.yungnickyoung.minecraft.ribbits.client;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.client.supporters.RibbitOptionsJSON;
import com.yungnickyoung.minecraft.ribbits.client.supporters.SupportersJSON;
import com.yungnickyoung.minecraft.ribbits.config.ConfigScreenRegistry;
import com.yungnickyoung.minecraft.ribbits.module.NetworkModule;
import dev.architectury.platform.Platform;

public class RibbitsCommonClient {
    public static void init() {
        ConfigScreenRegistry.registerConfigScreen(Platform.getMod(RibbitsCommon.MOD_ID));
        NetworkModule.initClient();
        SupportersJSON.populateSupportersList();
        RibbitOptionsJSON.loadFromFile();
    }
}
