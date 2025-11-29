package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.client.supporters.SupportersJSON;

public class NetworkModule {
    public static void init() {
        SupportersJSON.populateSupportersList();
    }
}