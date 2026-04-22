package com.yungnickyoung.minecraft.ribbits.client;

import com.yungnickyoung.minecraft.ribbits.client.supporters.RibbitOptionsJSON;
import com.yungnickyoung.minecraft.ribbits.client.supporters.SupportersJSON;

public class RibbitsCommonClient {
    public static void init() {
        SupportersJSON.populateSupportersList();
        RibbitOptionsJSON.loadFromFile();
    }
}
