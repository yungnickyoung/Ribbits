package com.yungnickyoung.minecraft.ribbits.platform.neoforge;

import com.yungnickyoung.minecraft.ribbits.platform.IPlatformHelper;

public class PlatformHelperImpl {
    public static IPlatformHelper getPlatformService() {
        return new NeoForgePlatformHelper();
    }
}
